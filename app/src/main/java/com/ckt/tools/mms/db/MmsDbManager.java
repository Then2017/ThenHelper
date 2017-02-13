package com.ckt.tools.mms.db;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.R.bool;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.SystemClock;
import android.provider.CallLog;
import android.provider.Telephony;
import android.provider.Telephony.Mms;
import android.provider.Telephony.MmsSms;
import android.provider.Telephony.Sms;
import android.util.Log;

import com.ckt.utils.L;
import com.ckt.utils.Tool;
import com.google.android.mms.pdu2.GenericPdu;
import com.google.android.mms.pdu2.PduParser;
import com.google.android.mms.pdu2.PduPersister;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SuppressLint("NewApi")
public class MmsDbManager {
  private static final String TAG = MmsDbManager.class.getSimpleName();
  private Context mContext;
  private ContentResolver mResolver;
  private static MmsDbManager mInstance;
  public int i;

  public static MmsDbManager getInstance(Context context) {
    if (mInstance == null) {
      mInstance = new MmsDbManager(context);
    }
    return mInstance;
  }

  private MmsDbManager(Context context) {
    mContext = context;
    mResolver = mContext.getContentResolver();
  }

  public boolean insertSms(List<String> recipients, String content, int type, int simId) {
    boolean result = true;
    Set<String> newRecipients = new HashSet<String>(recipients);
    long threadId = getOrCreateThreadId(mContext, newRecipients);
    String msgContent = "This is a test message";

    // MmsSms.CONTENT_URI
    ContentValues contentValues = new ContentValues(6);
    if (simId != -1) {
      // set the sim id
      contentValues.put("sim_id", simId);
    }
    // set message body
    contentValues.put(Telephony.Sms.BODY, msgContent);
    // set message type
    contentValues.put(Sms.TYPE, type);
    // set the message date
    contentValues.put(Sms.DATE, System.currentTimeMillis());
    // only sent successful message set data_sent
    if (type == Sms.MESSAGE_TYPE_SENT) {// if not inbox is send out message.
      contentValues.put(Sms.DATE_SENT, SystemClock.currentThreadTimeMillis());
    }
    if (threadId == -1) {
      result = false;
      L.d(" newSms failed! threadId is invalid!");
    }else{
     contentValues.put(Sms.TYPE, type);
     contentValues.put(Sms.THREAD_ID, threadId);
    Uri newSms = mResolver.insert(Sms.CONTENT_URI, contentValues);
    L.d("After insert newSms:" + newSms);
    }
    return result;
  }

  /**
   * Call Threads hide method
   * getOrCreateThreadId( Context context, Set<String> recipients)
   * @param context
   * @param recipients
   * @return
   */
  public static long getOrCreateThreadId(Context context, Set<String> recipients) {
    Uri.Builder uriBuilder = Uri.parse("content://mms-sms/threadID").buildUpon();
    for (String phoneNumber : recipients) {
      uriBuilder.appendQueryParameter("recipient", phoneNumber);
    }

    Uri uri = uriBuilder.build();
    // if (DEBUG) Log.v(TAG, "getOrCreateThreadId uri: " + uri);

    Cursor cursor = context.getContentResolver().query(uri, new String[] {"_id"}, null, null, null);
    if (cursor != null) {
      try {
        if (cursor.moveToFirst()) {
          return cursor.getLong(0);
        } else {
          Log.e(TAG, "getOrCreateThreadId returned no rows!");
        }
      } finally {
        cursor.close();
      }
    }
      return -1;
  }
  public int delAllData(){
    Uri  allUri = MmsSms.CONTENT_CONVERSATIONS_URI;
    return mContext.getContentResolver().delete(allUri, null, null);

  }

  public long getCurrentAll() {
    Uri allUri = MmsSms.CONTENT_CONVERSATIONS_URI;
    String[] str = {MmsSms._ID};
    Cursor curso = mContext.getContentResolver().query(allUri, str,null, null, MmsSms._ID);
    if(curso != null ){
      return curso.getCount();
    } else {
      return 0;
    }
  }
  public long insertMmsWithSub(List<String> recipients,int type,int simId)
  {
      boolean result = true;
      Set<String> newRecipients =new HashSet<String>(recipients);
      long threadId = getOrCreateThreadId(mContext, newRecipients);
      String subject="thats a test";
      ContentValues values = new ContentValues();
      values.put(Mms.MESSAGE_BOX, type);
      values.put("sim_id",simId);
      values.put(Mms.THREAD_ID, threadId);
      values.put(Mms.TEXT_ONLY, 1);
      /*values.put(Mms.Part.TEXT,"Tester");for debug*/
      /*X-Mms-Message-Type����MMSЭ�鶨��Ĳ������ͣ�����send-reqΪ128��notification-indΪ130��retrieve-confΪ132 */
      values.put(Mms.MESSAGE_TYPE, 0x80);
      values.put(Mms.SUBJECT,subject);
      values.put(Mms.DATE_SENT, SystemClock.currentThreadTimeMillis());
      values.put(Mms.MESSAGE_SIZE, 15);

      Uri newMms = mResolver.insert(Mms.CONTENT_URI, values);
      long newId = -1;
      if (newMms != null){
        newId = ContentUris.parseId(newMms);
      }
        return newId;
  }
  public void insertCallLog(String string, int int2) {
    ContentValues values = new ContentValues();
    values.put(CallLog.Calls.NUMBER, string);
    values.put(CallLog.Calls.TYPE, int2);
    mContext.getContentResolver().insert(CallLog.Calls.CONTENT_URI, values);
  }
  
  public int gdnclick() {
    Cursor cursor =
        mContext.getContentResolver().query(CallLog.Calls.CONTENT_URI, null, null, null, null);
    i = cursor.getCount();
    cursor.close();
    return i;
  }


}
