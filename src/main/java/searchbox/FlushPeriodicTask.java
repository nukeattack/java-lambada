package searchbox;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 *
 */
class FlushPeriodicTask implements Runnable, Closeable {
  private long interval;
  private long startTime;
  private Runnable flushCommand;
  private AtomicBoolean isClosed;

  public FlushPeriodicTask(Runnable flushCommand, long intervalMs) {
    this.flushCommand = flushCommand;
    this.interval = intervalMs;
    this.isClosed = new AtomicBoolean(false);
  }

  public void setInterval(long interval) {
    this.interval = interval;
    reset();
  }

  @Override
  public void run() {
    while (!isClosed.get()) {
      startTime = System.currentTimeMillis();
      while (timeLeft() > 0)
      {
        try {
          synchronized (this) {
            wait(timeLeft());
          }
        } catch (InterruptedException e) {
        }
      }
      if(!isClosed.get()){
        flushCommand.run();
      }
    }
  }

  private long timeLeft(){
    long passed = (System.currentTimeMillis() - startTime);
    long result = interval-passed;
    result = result < 0 ? 0 : result;
    return result;
  }

  public void reset() {
    synchronized (FlushPeriodicTask.this) {
      startTime = System.currentTimeMillis();
      FlushPeriodicTask.this.notify();
    }
  }

  @Override
  public void close() throws IOException {
    this.isClosed.set(true);
    synchronized (FlushPeriodicTask.this) {
      FlushPeriodicTask.this.notify();
    }
  }
}
