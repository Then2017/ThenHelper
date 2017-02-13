package com.addcontacts;


import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.SystemClock;
import android.provider.CallLog;
import android.provider.Telephony;
import android.util.Log;

import com.google.android.mms.pdu2.GenericPdu;
import com.google.android.mms.pdu2.PduParser;
import com.google.android.mms.pdu2.PduPersister;
import com.main.thenhelper.MainActivity;
import com.main.thenhelper.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Then on 2015/8/9.
 */
public class MessageMain {
//    private Context mcontext;
//    private ContentResolver mResolver;
//
//    public MessageMain(Context context) {
//        mcontext = context;
//        mResolver = mcontext.getContentResolver();
//    }

    private SimpleDateFormat sDateFormat = new SimpleDateFormat("mmssSSS");
    //同对话的短信
    public void insertsameSMS(String Messagetype,String Messageisread,String phone,String text) {
            String temptext=text;
            if (Messagetype.equals("Receive")) {
                Messagetype = "1";
                if (Messageisread.equals("Read")) {
                    Messageisread = "1";
                    text = "I'm the Receive and read SMS with the same numbers";
                } else {
                    Messageisread = "0";
                    text = "I'm the Receive and unread SMS with the same numbers";
                }
            } else if (Messagetype.equals("Send")) {
                Messagetype = "2";
                Messageisread = "1";
                text = "I'm the Send and read SMS with the same numbers";
            }
        if (!temptext.equals("")) {
            text=temptext;
        }
        ContentValues values = new ContentValues();
        values.put("address", phone);
        values.put("type", Messagetype);
        values.put("read", Messageisread);
        values.put("body", text);
        values.put("date", new Date().getTime());
        values.put("person", "test");
        MainActivity.mContext.getApplicationContext().getContentResolver()
                .insert(Uri.parse("content://sms/inbox"), values);
    }
    //不同对话的短信
    public void insertdifferentSMS(String Messagetype,String Messageisread,String text) {
        String date = sDateFormat.format(new Date());
        String temptext=text;
        if(Messagetype.equals("Receive")) {
            Messagetype="1";
            if(Messageisread.equals("Read")){
                Messageisread="1";
                text="I'm the Receive and read SMS with different numbers";
            }else {
                Messageisread="0";
                text="I'm the Receive and unread SMS with different numbers";
            }
        }else if(Messagetype.equals("Send")) {
            Messagetype="2";
            Messageisread="1";
            text="I'm the Send and read SMS with different numbers";
        }
        if (!temptext.equals("")) {
            text=temptext;
        }
        ContentValues values = new ContentValues();
        values.put("address", "1388"+date);
        values.put("type", Messagetype);
        values.put("read", Messageisread);
        values.put("body", text);
        values.put("date", new Date().getTime());
        values.put("person", "test");
        MainActivity.mContext.getApplicationContext().getContentResolver().insert(Uri.parse("content://sms/inbox"), values);
    }
    //添加同对话彩信
    public long insterMms(String  msgTypestr,String phonenum){
        int msgType;
        if(msgTypestr.equals("Receive")){
            msgType=1;
        }else {
            msgType=2;
        }
        List<String> recipients = new ArrayList<String>(1);
        recipients.add(phonenum);

        byte[] pduMms = readFile("mmspdu");
        PduPersister persister = PduPersister.getPduPersister(MainActivity.mContext);
        GenericPdu retrieveConf = null;
        Uri insertMmsUri = null;
        try {
            boolean isSend = false;
            Uri mmsUri = null;
            if(msgType == Telephony.Mms.MESSAGE_BOX_INBOX){
                isSend = false;
                mmsUri = Telephony.Mms.Inbox.CONTENT_URI;
            }else{
                isSend = true;
                if(msgType == Telephony.Mms.MESSAGE_BOX_OUTBOX){
                    mmsUri = Telephony.Mms.Outbox.CONTENT_URI;
                }else if(msgType == Telephony.Mms.MESSAGE_BOX_SENT){
                    mmsUri = Telephony.Mms.Sent.CONTENT_URI;
                }else if(msgType == Telephony.Mms.MESSAGE_BOX_DRAFTS){
                    mmsUri = Telephony.Mms.Draft.CONTENT_URI;
                }else {
                    isSend = true;
                    mmsUri = Telephony.Mms.CONTENT_URI;
                }
            }
            retrieveConf = new PduParser(pduMms).parseByType(isSend);
            insertMmsUri = persister.persist(retrieveConf, mmsUri, true, false, new HashSet<String>(recipients));
           // Log.e("ThenHelper", "testCase_insertMms insertMmsUri = " + insertMmsUri);
        } catch (Exception e) {
            Log.e("ThenHelper", "testCase_insertMms Exception e");
            e.printStackTrace();
        }
        long id =-1;
        if(insertMmsUri != null){
            id = ContentUris.parseId(insertMmsUri);
        }
        return id;
    }
    //添加不同对话彩信
    public long insterdifferentMms(String  msgTypestr){
        int msgType;
        if(msgTypestr.equals("Receive")){
            msgType=1;
        }else {
            msgType=2;
        }
        String date = sDateFormat.format(new Date());
        String phonenum="1388"+date;
        List<String> recipients = new ArrayList<String>(1);
        recipients.add(phonenum);

        byte[] pduMms = readFile("mmspdu");
        PduPersister persister = PduPersister.getPduPersister(MainActivity.mContext);
        GenericPdu retrieveConf = null;
        Uri insertMmsUri = null;
        try {
            boolean isSend = false;
            Uri mmsUri = null;
            if(msgType == Telephony.Mms.MESSAGE_BOX_INBOX){
                isSend = false;
                mmsUri = Telephony.Mms.Inbox.CONTENT_URI;
            }else{
                isSend = true;
                if(msgType == Telephony.Mms.MESSAGE_BOX_OUTBOX){
                    mmsUri = Telephony.Mms.Outbox.CONTENT_URI;
                }else if(msgType == Telephony.Mms.MESSAGE_BOX_SENT){
                    mmsUri = Telephony.Mms.Sent.CONTENT_URI;
                }else if(msgType == Telephony.Mms.MESSAGE_BOX_DRAFTS){
                    mmsUri = Telephony.Mms.Draft.CONTENT_URI;
                }else {
                    isSend = true;
                    mmsUri = Telephony.Mms.CONTENT_URI;
                }
            }
            retrieveConf = new PduParser(pduMms).parseByType(isSend);
            insertMmsUri = persister.persist(retrieveConf, mmsUri, true, false, new HashSet<String>(recipients));
            // Log.e("ThenHelper", "testCase_insertMms insertMmsUri = " + insertMmsUri);
        } catch (Exception e) {
            Log.e("ThenHelper", "testCase_insertMms Exception e");
            e.printStackTrace();
        }
        long id =-1;
        if(insertMmsUri != null){
            id = ContentUris.parseId(insertMmsUri);
        }
        return id;
    }
    //彩信资源pdu
    public byte[] readFile(String filePath) {
        InputStream byteReader = null;
        try {
            byteReader = MainActivity.mContext.getResources().getAssets().open("mmspdu");
        } catch (IOException e) {
            e.printStackTrace();
        }
        ByteArrayOutputStream baos = null;
        try{
            baos = new ByteArrayOutputStream();
            int len = -1;
            byte[] buffer = new byte[512];
            while ((len = byteReader.read(buffer,0, 512)) != -1){
                baos.write(buffer,0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return baos.toByteArray();
    }
    //标题彩信
    public long insertMmsWithSub(int type)
    {
        String date = sDateFormat.format(new Date());
        String phonenum="1388"+date;
        boolean result = true;
        long threadId = getOrCreateThreadId(phonenum);
        String subject="this is a test for MMS subject";
        ContentValues values = new ContentValues();
        values.put(Telephony.Mms.MESSAGE_BOX, type);
       // values.put("sim_id",simId);
        values.put(Telephony.Mms.THREAD_ID, threadId);
        values.put(Telephony.Mms.TEXT_ONLY, 1);
      /*values.put(Mms.Part.TEXT,"Tester");for debug*/
      /*X-Mms-Message-Type，由MMS协议定义的彩信类型，其中send-req为128、notification-ind为130、retrieve-conf为132 */
        values.put(Telephony.Mms.MESSAGE_TYPE, 0x80);
        values.put(Telephony.Mms.SUBJECT,subject);
        values.put(Telephony.Mms.DATE_SENT, SystemClock.currentThreadTimeMillis());
        values.put(Telephony.Mms.MESSAGE_SIZE, 15);
        Uri newMms = MainActivity.mContext.getContentResolver().insert(Telephony.Mms.CONTENT_URI, values);
        long newId = -1;
        if (newMms != null){
            newId = ContentUris.parseId(newMms);
        }
        return newId;
    }
    /**
     * Call Threads hide method
     * getOrCreateThreadId( Context context, Set<String> recipients)
     * @return
     */
    public long getOrCreateThreadId(String phoneNumber) {
        Uri.Builder uriBuilder = Uri.parse("content://mms-sms/threadID").buildUpon();
        //for (String phoneNumber : recipients) {
            uriBuilder.appendQueryParameter("recipient", phoneNumber);
      //  }
        Uri uri = uriBuilder.build();
        // if (DEBUG) Log.v(TAG, "getOrCreateThreadId uri: " + uri);
        Cursor cursor = MainActivity.mContext.getContentResolver().query(uri, new String[] {"_id"}, null, null, null);
        if (cursor != null) {
            try {
                if (cursor.moveToFirst()) {
                    return cursor.getLong(0);
                } else {
                    Log.e("ThenHelper", "getOrCreateThreadId returned no rows!");
                }
            } finally {
                cursor.close();
            }
        }
        return -1;
    }

    //删除信息
    public int deletemessage(){
        Uri  allUri = Telephony.MmsSms.CONTENT_CONVERSATIONS_URI;
        return MainActivity.mContext.getContentResolver().delete(allUri, null, null);
        }

    //统计信息
    public int Totalmessage(){
        ContentResolver contentResolver = MainActivity.mContext.getContentResolver();
        final String[] projection = new String[]{"*"};
        Uri uri = Uri.parse("content://sms/inbox");
        Uri uri1 = Uri.parse("content://sms/sent");
        Uri uri2 = Uri.parse("content://sms/draft");
        Cursor query = contentResolver.query(uri, projection, null, null, "date DESC");
        Cursor query1 = contentResolver.query(uri1, projection, null, null, "date DESC");
        Cursor query2 = contentResolver.query(uri2, projection, null, null, "date DESC");
        int i=query.getCount()+query1.getCount()+query2.getCount();
        query.close();
        query1.close();
        query2.close();

        Uri uri3 = Uri.parse("content://mms");
        Cursor query3 = contentResolver.query(uri3, projection, null, null, "date DESC");
        i=i+query3.getCount();
        query3.close();
        return i;
    }
    //短信字段解释:
    /*_id          一个自增字段，从1开始
    thread_id    序号，同一发信人的id相同
    address      发件人手机号码（根据这个查找联系人姓名？）
    person       联系人列表里的序号，陌生人为null
    date         发件日期，单位是milliseconds，从1970/01/01至今所经过的时间)
    protocol     协议，分为： 0 SMS_RPOTO, 1 MMS_PROTO
    read         是否阅读，0未读， 1已读
    status       状态，-1接收，0 complete, 64 pending, 128 failed
            type
    ALL    = 0;
    INBOX  = 1;
    SENT   = 2;
    DRAFT  = 3;
    OUTBOX = 4;
    FAILED = 5;
    QUEUED = 6;
    body                    短信内容
    service_center          短信服务中心号码编号
    subject                 短信的主题
    reply_path_present      TP-Reply-Path
            locked

    3.访问短信数据库的Uri
    content://sms/inbox        收件箱
    content://sms/sent        已发送
    content://sms/draft        草稿
    content://sms/outbox        发件箱
    content://sms/failed        发送失败
    content://sms/queued        待发送列表 */
}
