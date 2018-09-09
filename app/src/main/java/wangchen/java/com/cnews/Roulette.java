package wangchen.java.com.cnews;

import android.util.Log;

import java.util.HashMap;

public class Roulette {
  public static int nextType(HashMap<TypeName, Integer> map) {
    double sum = 0.0;
    for(TypeName key: map.keySet()) {
      sum += (double)map.get(key);
    }
    double r = Math.random() * sum;

    sum = 0.0;
    for(TypeName key: map.keySet()) {
      sum += (double)map.get(key);
      if(sum > r) {
        return TypeName.getTypeInt(key.getName());
      }
    }
    return 1;
  }
}
