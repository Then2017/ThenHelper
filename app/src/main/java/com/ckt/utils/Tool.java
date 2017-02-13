package com.ckt.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class Tool {
  /** sim info */
  public static final Uri SIM_INFO_URI = Uri.parse("content://telephony/siminfo");
  public static final String SIM_INFO_COLUMN_SLOT = "slot";
  public static final int SLOT_NONE = -1;
  public static final String SIM_INFO_COLUMN_ICC_ID = "icc_id";
  public static final String SIM_INFO_COLUMN_ID = "_id";
  /** sim info */

//  private static final long TIME = 1404660469881l;
  private static final long TIME = 1300000000000l;
  private static final long TIME_ADJUST = 100000000000l;




  /**
   * A Help method to call hide method by reflection
   * @param clasName Class full name.ex:SystemPropertie.java, it class name "android.os.SystemProperties"
   * @param methodName The method that you want call.
   * @param argsClazzs The method's parameters class array.Ex: getInt(String,Int);<br>
   *                    their argsClasss={String.class,Integer.class}
   * @param argsObjs The method's parameter's objects.
   * @param destObj The method's object.for static method input null
   * @return Object failed or void method will return null.
   */
  public static Object callHideMethod(String clasName, String methodName, Class[] argsClazzs,
      Object[] argsObjs, Object destObj) {
    Class clazz;
    Object result = null;
    try {
      clazz = Class.forName(clasName);
      @SuppressWarnings("unchecked")
      Method getIntMethod =  clazz.getMethod(methodName, argsClazzs);
      //here for static method,just input null.the object on which to call this method (or null for static methods)
      result = getIntMethod.invoke(destObj, argsObjs);
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    } catch (NoSuchMethodException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    } catch (IllegalArgumentException e) {
      e.printStackTrace();
    } catch (InvocationTargetException e) {
      e.printStackTrace();
    }
    return result;
  }

/*  *//**
   * Call the android.os.SystemProperties.getInt Methods.
   * @param key systemProperties's key.
   * @param defaultInt if not set the system property will use this value.
   * @return
   *//*
  public static int getIntFromSystemProp(String key, int defaultInt) {
    String clasName = "android.os.SystemProperties";
    Integer value = Integer.valueOf(defaultInt);
    Object[] inArgs = {key,value};
  //  public static int getInt(String key, int def)
    //use String
    Object result = callHideStaticMethod(clasName,"getInt",new Class[] {String.class,Integer.class},inArgs);
    int sysProp = 0;
    if(result != null && result instanceof Integer ){
      sysProp = (Integer) result;
    }
    return sysProp;
  }*/
  /**
   * Call the android.os.SystemProperties.getInt Methods.
   * @param key systemProperties's key.
   * @param defaultInt if not set the system property will use this value.
   * @return
   */
  public static int getIntFromSystemProp(String key, int defaultInt) {
    String clasName = "android.os.SystemProperties";
    String value = String.valueOf(defaultInt);
    Object[] inArgs = {key,value};
  //  public static int getInt(String key, int def)
    //use String
    Object result = callHideMethod(clasName,"get",new Class[] {String.class,String.class},inArgs, null);
    int sysProp = 0;
    if(result != null && result instanceof String ){
      sysProp = Integer.parseInt((String) result);
    }
    return sysProp;
  }

  /**
   * Get a static field value
   * @param clasName the class full name
   * @param name the field name.
   * @return null failed to get the field.
   */
  public static Object getStaticHideClassField(String clasName, String name){
    Object result = null;
    try{
    Class clazz = Class.forName(clasName);
    Field field =  clazz.getDeclaredField(name);
    result = field.getBoolean(null);
    }catch(ClassNotFoundException notFound){
      L.d(clasName +"NOt exist!");
    } catch (NoSuchFieldException e) {
      L.d(clasName +"Not exist field: "+ name);
    } catch (IllegalAccessException e) {
      L.d(clasName +"Not exist field: "+ name +" Can't access");
    } catch (IllegalArgumentException e) {
      L.d(clasName +"Not exist field: "+ name +" illegalArument");
    }
    return result;
  }
  /**
   * Can use this method to check the current platform
   * is support multi-sim or not.
   * @return
   */
  public static boolean isGeminiSupport(){
    boolean result = false;
    Object gemini = getStaticHideClassField("com.mediatek.common.featureoption.FeatureOption",
                  "MTK_GEMINI_SUPPORT");
    if(gemini != null && gemini instanceof Boolean){
      result = (Boolean) gemini;
    }
    return result;
  }
  /**
   * Get the current sim id array
   * @param context
   * @return if no sim id inserted. return {1}. else return the sim ids array
   */
    public static int[] getGeminiSimId(Context context) {
        int[] simIds = { 1 };
        String[] project = { SIM_INFO_COLUMN_ID };
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(SIM_INFO_URI, project,
                    SIM_INFO_COLUMN_SLOT + "!=" + SLOT_NONE, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                simIds = new int[cursor.getCount()];
                do {
                    cursor.getInt(0);// only one column here.
                } while (cursor.moveToNext());
            }
        } finally {
            if(cursor != null)
            {
                cursor.close();
            }
        }
        return simIds;
    }

  public static List<Integer> getEnabeledSimSlotId(Context context) {
      List<Integer> result = new ArrayList<Integer>();
      String[] project = {SIM_INFO_COLUMN_SLOT};
      Cursor cursor = null;
      try {
          cursor = context.getContentResolver().query(SIM_INFO_URI, project,
                  SIM_INFO_COLUMN_SLOT + "!=" + SLOT_NONE, null, null);
          if (cursor != null && cursor.moveToFirst()) {
              do {
                  result.add(cursor.getInt(0));// only one column here.
              } while (cursor.moveToNext());
          }
      } finally {
          if(cursor != null)
          {
              cursor.close();
          }
      }
      return result;
  }
  /**
   * Generate one phone number.
   * @return
   */
  public static final String generteOnePhoneNumber(){
    long currTime = System.currentTimeMillis();
    System.out.println(currTime);
    String result = String.valueOf(currTime);
    result = result.subSequence(0, result.length()).toString();
    if(!result.startsWith("130")){
      result = "130"+result.subSequence(result.length() -8, result.length()) ;
    }
    try {
      Thread.currentThread().sleep(5);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    return result;
  }
}
