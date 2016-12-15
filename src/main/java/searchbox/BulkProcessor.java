package searchbox;

import com.google.gson.Gson;
import io.searchbox.action.BulkableAction;
import io.searchbox.client.JestClient;
import io.searchbox.core.Bulk;
import io.searchbox.core.DocumentResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;

/**
 */
public class BulkProcessor implements Closeable {
  public static final long DEFAULT_WRITE_RETRY_INTERVAL = 100;
  public static final int DEFAULT_WRITE_RETRY_COUNT = 5;
  public static final int BULK_BUFFER_SIZE = 3;

  private final static Logger LOGGER = LoggerFactory.getLogger(BulkProcessor.class);
  private Gson gson;
  private JestClient jestClient;
  private BulkProcessingListener eventListener;

  private BlockingQueue<Bulk> bulksQueue = new LinkedBlockingQueue<>(BULK_BUFFER_SIZE);
  private Collection<BulkWriter> writers;
  private ExecutorService threadExecutorService = Executors.newFixedThreadPool(10);
  private FlushPeriodicTask flushPeriodicTask;

  private long writeRetryInterval = DEFAULT_WRITE_RETRY_INTERVAL;
  private int writeRetryCount = DEFAULT_WRITE_RETRY_COUNT;

  BlockingQueue<BulkableAction<DocumentResult>> preparedActions = new LinkedBlockingQueue<>();
  long preparedActionsVolume = 0;
  StatCounter counters;

  private Map<FlushPolicy.Type, FlushPolicy> flushPolicies = new HashMap<>();

  public BulkProcessor(JestClient client, Gson gson, BulkProcessingListener bulkProcessingListener) {
    this.writers = new LinkedList<>();
    this.jestClient = client;
    this.gson = gson;
    this.eventListener = bulkProcessingListener;
    counters = new StatCounter();
    this.flushPeriodicTask = new FlushPeriodicTask(new Runnable() {
      @Override
      public void run() {
        System.out.println(BulkProcessor.this.counters.toString());
        BulkProcessor.this.flushIfReady();
      }
    }, TimeUnit.SECONDS.toMillis(5));
    threadExecutorService.execute(flushPeriodicTask);
  }

  public BulkProcessor addFlushByPeriod(long time, TimeUnit timeUnit){
    if (time > 0 && timeUnit != null) {
      flushPolicies.put(FlushPolicy.Type.TIME, new FlushByPeriod(timeUnit.toMillis(time)));
      flushPeriodicTask.setInterval(timeUnit.toMillis(time));
    }

    return this;
  }

  public BulkProcessor addFlushByVolume(long volumeBytes){
    if (volumeBytes > 0) {
      flushPolicies.put(FlushPolicy.Type.VOLUME, new FlushByVolume(volumeBytes));
    }
    
    return this;
  }

  public BulkProcessor addFlushByCount(long maxCount){
    if (maxCount > 0) {
      flushPolicies.put(FlushPolicy.Type.ACTION_COUNT, new FlushByItemCount(maxCount));
    }
    
    return this;
  }

  public BulkProcessor withConcurrentRequests(int count) {
    this.bulksQueue = new LinkedBlockingQueue<>(count);
    if (count <= 0) {
      throw new IllegalArgumentException("Count should be greater than 0. Actual : " + count);
    }
    if (count > writers.size()) {
      while (writers.size() < count) {
        addWriter();
      }
    } else {
      for (int i = 0; i < writers.size() - count; i++) {
        removeWriter();
      }
    }
    return this;
  }

  public BulkProcessor withBackoffPolicy(int maxRetries, long retryInterval) {
    this.writeRetryCount = maxRetries;
    this.writeRetryInterval = retryInterval;
    for(BulkWriter writer : writers){
      writer.withBackoffPolicy(maxRetries, retryInterval);
    }
    return this;
  }

  public void execute(BulkableAction<DocumentResult> action) {
    if (writers.isEmpty()) {
      addWriter();
    }

    try {
      preparedActions.add(action);
    } catch (Exception e) {
      LOGGER.error("Thread interrupted ", e);
    }
    preparedActionsVolume += action.getData(gson).length();
    counters.add(StatCounter.STAT_TYPE.TOTAL_ACTIONS, 1);

    flushIfReady();
  }

  private void flushIfReady(){
    boolean ready = false;
    for(FlushPolicy policy : flushPolicies.values()){
      if(policy.test(this)){
        ready = true;
        break;
      }
    }
    if(ready){
      for(FlushPolicy policy : flushPolicies.values()){
        policy.onFlush();
      }
      flush();
    }
  }

  boolean isProcessing() {
    for(BulkWriter writer : writers){
      if(writer.isProcessing())return true;
    }
    return preparedActions.size() != 0 || bulksQueue.size() != 0;
  }

  public void flush() {
    LOGGER.info("Adding bulk for processing. Docment indexing stats: total : {} , success : {} , failed : {}",
                getTotalBulkCount(), getSuccessedBulkCount(), getFailedBulkCount());
    if (!preparedActions.isEmpty()) {
      LOGGER.info("Sending document bulk with size : {} , item count : {}",
                  preparedActionsVolume, preparedActions.size());
      List<BulkableAction> actions = extractPreparedActions();
      Bulk bulk = new Bulk.Builder().addAction(actions).build();

      try {
        bulksQueue.put(bulk);
      } catch (InterruptedException e) {
        LOGGER.error("Error putting bulk to queue", e);
      }
    }
  }

  public long getSuccessedBulkCount() {
    long total = 0;
    for (BulkWriter writer : writers) {
      total += writer.getSucessedCount();
    }
    return total;
  }

  public long getFailedBulkCount() {
    long total = 0;
    for (BulkWriter writer : writers) {
      total += writer.getFailedCount();
    }
    return total;
  }

  public long getTotalBulkCount() {
    long total = 0;
    for (BulkWriter writer : writers) {
      total += writer.getTotalCount();
    }
    return total;
  }

  @Override
  public void close() throws IOException {
    flushPeriodicTask.close();
    flush();
    for (BulkWriter writer : writers) {
      writer.close();
    }
    LOGGER.debug("Bulk writer closed");
  }

  private List<BulkableAction> extractPreparedActions(){
    int size = preparedActions.size();
    BulkableAction action;
    List<BulkableAction> result = new LinkedList<>();
    while ( result.size() < size && (action = preparedActions.poll()) != null){
      result.add(action);
    }
    return result;
  }

  private void addWriter() {
    BulkWriter writer = new BulkWriter(jestClient, bulksQueue, eventListener, writeRetryCount, writeRetryInterval);
    writers.add(writer);
    threadExecutorService.execute(writer);
  }

  private void removeWriter() {
    try {
      ((Queue<BulkWriter>) (writers)).poll().close();
    } catch (IOException e) {
      LOGGER.error("Error closing writer");
    }
  }
}
