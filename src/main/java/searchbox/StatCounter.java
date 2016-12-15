package searchbox;


import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 */
public class StatCounter {
  enum STAT_TYPE{
    TOTAL_ACTIONS,
    TOTAL_PROCESSED_BULKS;
  }
  private Map<STAT_TYPE, AtomicLong> counters;
  private Gson gson = new Gson();

  public StatCounter(){
    counters = new HashMap<>();
    for(STAT_TYPE stat_type : STAT_TYPE.values()){
      counters.put(stat_type, new AtomicLong(0));
    }
  }

  public void add(STAT_TYPE type, long value){
    counters.get(type).getAndAdd(value);
  }

  @Override
  public String toString() {
    return gson.toJson(counters);
  }
}
