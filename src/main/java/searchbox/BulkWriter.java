package searchbox;

import io.searchbox.client.JestClient;
import io.searchbox.client.JestResult;
import io.searchbox.core.Bulk;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

/**
 *
 */
public class BulkWriter implements Runnable, Closeable {
  public static final long DEFAULT_WAIT_INTERVAL = 200;

  private static final Logger LOGGER = LoggerFactory.getLogger(BulkWriter.class);

  private BlockingQueue<Bulk> source;
  private JestClient client;
  private AtomicBoolean closed = new AtomicBoolean(false);
  private long writeRetryInterval;
  private int writeRetryCount;
  private BulkProcessingListener eventListener;
  public AtomicLong totalBulks = new AtomicLong(0);
  public AtomicLong succeedBulks = new AtomicLong(0);
  public AtomicLong failedBulks = new AtomicLong(0);
  private AtomicBoolean isProcessing = new AtomicBoolean(false);

  public BulkWriter(JestClient client, BlockingQueue<Bulk> source, BulkProcessingListener eventListener, int writeRetryCount, long writeRetryInterval) {
    this.source = source;
    this.client = client;
    this.eventListener = eventListener;
    this.writeRetryInterval = writeRetryInterval;
    this.writeRetryCount = writeRetryCount;
  }

  public void withBackoffPolicy(int writeRetryCount, long writeRetryInterval) {
    this.writeRetryCount = writeRetryCount;
    this.writeRetryInterval = writeRetryInterval;
  }

  @Override
  public void close() throws IOException {
    this.closed.set(true);
  }

  @Override
  public void run() {
    try {
      while (!closed.get()) {
        Bulk bulk = source.poll(DEFAULT_WAIT_INTERVAL, TimeUnit.MILLISECONDS);
        if (bulk != null) {
          try {
            isProcessing.set(true);
            eventListener.beforeBulk(bulk);
            long start = System.currentTimeMillis();
            JestResult result = executeWithBackoff(bulk);
            if (LOGGER.isDebugEnabled()) {
              LOGGER.debug("Bulk index operation executed. Total execution time: {}, Took: {}", System.currentTimeMillis()-start, result.getValue("took"));
            }
            if(result.isSucceeded()){
              succeedBulks.incrementAndGet();
              eventListener.afterBulk(bulk, result);
            }else throw new IOException(result.getJsonString());
          } catch (Throwable e) {
            failedBulks.incrementAndGet();
            eventListener.afterBulk(bulk, e);
            LOGGER.error("Bulk write exception: ", e);
          }finally {
            isProcessing.set(false);
            totalBulks.incrementAndGet();
          }
        }
      }
      LOGGER.debug("Bulk writing thread closed");
    } catch (InterruptedException e) {
      LOGGER.error("Bulk writing thread interrupted");
    }
  }

  public long getTotalCount() {
    return totalBulks.get();
  }

  public long getSucessedCount() {
    return succeedBulks.get();
  }

  public long getFailedCount() {
    return failedBulks.get();
  }

  public boolean isProcessing() {
    return isProcessing.get();
  }

  private JestResult executeWithBackoff(Bulk bulk) throws Exception {
    long waitInterval = writeRetryInterval;
    Throwable lastException = null;
    for (int i = 1; i <= writeRetryCount; i++) {
      try {
        JestResult result = client.execute(bulk);
        return result;
      } catch (Exception e) {
        LOGGER.warn("Bulk write error, retry # {} of {}", i, writeRetryCount);
        lastException = e;
      }
      Thread.sleep(waitInterval);
      waitInterval = waitInterval + writeRetryInterval;
    }
    throw new Exception("Bulk write exception ", lastException);
  }
}
