package searchbox;

import searchbox.BulkProcessor;

/**
 *
 */
public interface FlushPolicy {
  enum Type {
    VOLUME, ACTION_COUNT, TIME
  }
  Type getType();
  boolean test(BulkProcessor processor);
  void onFlush();
}
