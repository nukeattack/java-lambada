package searchbox;

class FlushByItemCount implements FlushPolicy {
  Long maxItems;

  FlushByItemCount(Long maxItems){
    this.maxItems = maxItems;
  }

  @Override
  public Type getType() {
    return Type.ACTION_COUNT;
  }

  @Override
  public boolean test(BulkProcessor processor) {
    return processor.preparedActions.size() >= maxItems;
  }

  @Override
  public void onFlush() {

  }
}
