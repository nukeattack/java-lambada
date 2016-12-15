package searchbox;

class FlushByVolume implements FlushPolicy {
  private Long maxVolume;

  public FlushByVolume(Long maxVolume){
    this.maxVolume = maxVolume;
  }

  @Override
  public Type getType() {
    return Type.VOLUME;
  }

  @Override
  public boolean test(BulkProcessor processor) {
    return processor.preparedActionsVolume > maxVolume;
  }

  @Override
  public void onFlush() {

  }
}
