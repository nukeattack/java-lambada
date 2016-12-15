package searchbox;

class FlushByPeriod implements FlushPolicy {
  private Long period;
  private Long lastFlush;

  public FlushByPeriod(Long period){
    this.period = period;
    this.lastFlush = System.currentTimeMillis();
  }

  @Override
  public Type getType() {
    return Type.TIME;
  }

  @Override
  public boolean test(BulkProcessor processor) {
    return System.currentTimeMillis() - lastFlush > period;
  }

  @Override
  public void onFlush() {
    lastFlush = System.currentTimeMillis();
  }
}
