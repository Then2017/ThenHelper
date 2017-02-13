package com.ckt.utils;

import android.util.Log;

import java.util.Set;

public class L {
  public static void d(String content) {
    Log.d("LOGTools", content);
  }

  /**
   * print Set<T> collections 
   * @param values
   */
  public static <T> void p(Set<T> values) {
    if (values == null) {
      L.d("Set values are NULL");
    } else {
      L.d("Set<T> data are =================>>>>");
      for (T item : values) {
        d(o2S(item));
      }
      L.d("Set<T> data are  <<<<=================");
    }
  }

  /**object to string.
   * @param obj
   * @return
   */
  public static String o2S(Object obj) {
    String result;
    if (obj == null) {
      result = "#NULL";
    } else {
      result = obj.toString();
    }
    return result;
  }
  public static void e(String string) {
    Log.e("LOGTools", string); 
  }
}
