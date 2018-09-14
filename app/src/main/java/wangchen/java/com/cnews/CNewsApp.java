package wangchen.java.com.cnews;

import android.app.Application;
import android.util.Log;

import java.util.HashMap;


//To save global variables

public class CNewsApp extends Application {

  private NewsDBHelper newsDBHelper;
  private HashMap<TypeName, Integer> readTimes;

  @Override
  public void onCreate() {
    super.onCreate();
    newsDBHelper = new NewsDBHelper(this);

    readTimes = new HashMap<>();
    for(TypeName t : TypeName.values()) {
      if(t.equals(TypeName.RECOMMAND)) continue;
      readTimes.put(t, 0);
      Log.d("put", "put");
    }

  }

  public NewsDBHelper getDB() {
     return newsDBHelper;
  }

  public void updateReadTimes(TypeName t) {
    Log.d("update", t.getName());
    int times = readTimes.get(t) + 1;
    readTimes.put(t, times);
  }

  public HashMap<TypeName, Integer> getReadTimes() {
    return readTimes;
  }
}
