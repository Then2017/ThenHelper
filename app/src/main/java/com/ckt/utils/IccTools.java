package com.ckt.utils;

import android.telephony.SmsManager;

public class IccTools {

  /** Free space (TS 51.011 10.5.3 / 3GPP2 C.S0023 3.4.27). */
  static public final int STATUS_ON_ICC_FREE      = 0;

  /** Received and read (TS 51.011 10.5.3 / 3GPP2 C.S0023 3.4.27). */
  static public final int STATUS_ON_ICC_READ      = 1;

  /** Received and unread (TS 51.011 10.5.3 / 3GPP2 C.S0023 3.4.27). */
  static public final int STATUS_ON_ICC_UNREAD    = 3;

  /** Stored and sent (TS 51.011 10.5.3 / 3GPP2 C.S0023 3.4.27). */
  static public final int STATUS_ON_ICC_SENT      = 5;

  /** Stored and unsent (TS 51.011 10.5.3 / 3GPP2 C.S0023 3.4.27). */
  static public final int STATUS_ON_ICC_UNSENT    = 7;

  // SMS send failure result codes

  /** Generic failure cause */
  static public final int RESULT_ERROR_GENERIC_FAILURE    = 1;
  /** Failed because radio was explicitly turned off */
  static public final int RESULT_ERROR_RADIO_OFF          = 2;
  /** Failed because no pdu provided */
  static public final int RESULT_ERROR_NULL_PDU           = 3;
  /** Failed because service is currently unavailable */
  static public final int RESULT_ERROR_NO_SERVICE         = 4;
  /** Failed because we reached the sending queue limit.  {@hide} */
  static public final int RESULT_ERROR_LIMIT_EXCEEDED     = 5;
  /** Failed because FDN is enabled. {@hide} */
  static public final int RESULT_ERROR_FDN_CHECK_FAILURE  = 6;
  /**
   * @hide
   */
   static public final int RESULT_ERROR_SIM_MEM_FULL = 7;
   /**
   * @hide
   */
   static public final int RESULT_ERROR_SUCCESS = 0;
   /**
   * @hide
   */
   static public final int RESULT_ERROR_INVALID_ADDRESS = 8;
   // mtk added end
  /**
   * TODO Now don't found the string to byte rules. here just use byte[] to save to simcard.
   * TOOD Now don't support Gemini phone, to save sim card message.
   * @param message
   * @param type
   * @param simId not used now.
   * @return
   */
  public static int saveSimMessage(String message, int type,int simId){
        int result = -1;
        SmsManager smsManager = SmsManager.getDefault();
        int status = type;
        L.d("save2Sim --> type:" + type);
        String clasName = "android.telephony.SmsManager";
  //      long timestamp = System.currentTimeMillis();
        byte[] scBytes = {8, -111, 104, 49, 16, -128, 33, 5, -16};
        byte[] smsBytes ={36, 13, -111, 104, 49, 67, -104, 48, 36, -13, 0, 0, 65, -128, 16, -112, 96, 32, 35, 1, 49};
  //      String methodName= "copyTextMessageToIccCard";//"copyMessageToIcc";
        Class[] argsClazzs = {byte[].class,byte[].class,int.class};
        Object[] argsObjs ={scBytes,smsBytes,status};
        // public int copySmsToIcc (byte[] smsc, byte[] pdu, int status)
        String methodName= "copySmsToIcc";//"copyMessageToIcc";
        //  public SimSmsInsertStatus insertRawMessageToIccCard(int status, byte[] pdu, byte[] smsc)
        String methodWithResult = "insertRawMessageToIccCard";
        Class[] argsClazzWithResult = {int.class,byte[].class,byte[].class};
        Object[] argsObjsWithResult ={status,smsBytes,scBytes,};
  //      public boolean copyMessageToIcc(byte[] smsc, byte[] pdu, int status) {}
  //     Object obj =  Tool.callHideMethod(clasName, methodName, argsClazzs, argsObjs, smsManager);
  //      Class[] argsClazzs = {String.class,String.class,List.class,Integer.class,Long.class};
  //      Object[] argsObjs = {scAddress,address,text,status,timestamp};
        Object obj  = Tool.callHideMethod(clasName, methodName, argsClazzs, argsObjs, smsManager);
       if(obj != null){
           L.d( "class:"+obj.getClass().getName() +"| value:"+obj.toString());
           if(obj instanceof Integer ){//here should is int and > 0
             result = (Integer)obj;
           }
       }else{
         L.d("obj == NULL");
       }
        return result;
    }

  public static byte[] getTpdu(byte[] pdu){
    int smsc_len = (pdu[0] & 0xff) + 1;
    int tpdu_len = pdu.length - smsc_len;
    byte[] tpdu = new byte[tpdu_len];

    try {
        System.arraycopy(pdu, smsc_len, tpdu, 0, tpdu.length);
        return tpdu;
    } catch(ArrayIndexOutOfBoundsException e) {
        return null;
    }
  }
  public static byte[] getSmScPdu(byte[] pdu){
    int smsc_len = (pdu[0] & 0xff) + 1;
    byte[] smsc = new byte[smsc_len];

    try {
        System.arraycopy(pdu, 0, smsc, 0, smsc.length);
        return smsc;
    } catch(ArrayIndexOutOfBoundsException e) {
        return null;
    }
  }
}
