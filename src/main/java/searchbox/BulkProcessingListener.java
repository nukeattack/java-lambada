package searchbox;

import io.searchbox.client.JestResult;
import io.searchbox.core.Bulk;

/**
 * A listener for the execution.
 */
public interface BulkProcessingListener {
  /**
   * Callback before the bulk is executed.
   */
  void beforeBulk(Bulk request);

  /**
   * Callback after a successful execution of bulk request.
   */
  void afterBulk(Bulk request, JestResult response);

  /**
   * Callback after a failedBulks execution of bulk request.
   */
  void afterBulk(Bulk request, Throwable failure);
}
