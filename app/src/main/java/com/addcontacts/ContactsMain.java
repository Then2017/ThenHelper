package com.addcontacts;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.pm.ApplicationInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

//import com.ckt.tools.mms.R;
import com.main.thenhelper.MainActivity;

/**
 * Created by admin on 2015/8/6.
 */
public class ContactsMain {

    private boolean mhasemail = false;
    private boolean mhasremark = false;
    private boolean mhasaddress = false;
    private boolean mhasphoto = false;
    private boolean mhasfavorite = false;

    private SimpleDateFormat sDateFormat = new SimpleDateFormat("MMddHHmmssSSS");
    private SimpleDateFormat phoneDateFormat = new SimpleDateFormat("mmssSSS");

    private static final Uri DATA_URI = ContactsContract.Data.CONTENT_URI;
    private static final Uri RAW_CONTACTS_URI = ContactsContract.RawContacts.CONTENT_URI;
    private static final String ACCOUNT_TYPE = ContactsContract.RawContacts.ACCOUNT_TYPE;
    private static final String ACCOUNT_NAME = ContactsContract.RawContacts.ACCOUNT_NAME;

    private static final String RAW_CONTACT_ID = ContactsContract.Data.RAW_CONTACT_ID;
    private static final String MIMETYPE = ContactsContract.Data.MIMETYPE;

    private static final String NAME_ITEM_TYPE = ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE;
    private static final String DISPLAY_NAME = ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME;

    private static final String PHONE_ITEM_TYPE = ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE;
    private static final String PHONE_NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;
    private static final String PHONE_TYPE = ContactsContract.CommonDataKinds.Phone.TYPE;
    private static final int PHONE_TYPE_HOME = ContactsContract.CommonDataKinds.Phone.TYPE_HOME;
    private static final int PHONE_TYPE_MOBILE = ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE;

    private static final String EMAIL_ITEM_TYPE = ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE;
    private static final String EMAIL_DATA = ContactsContract.CommonDataKinds.Email.DATA;
    private static final String EMAIL_TYPE = ContactsContract.CommonDataKinds.Email.TYPE;
    private static final int EMAIL_TYPE_HOME = ContactsContract.CommonDataKinds.Email.TYPE_HOME;
    private static final int EMAIL_TYPE_WORK = ContactsContract.CommonDataKinds.Email.TYPE_WORK;
    private static final String AUTHORITY = ContactsContract.AUTHORITY;

    private static final String ADDRESS_ITEM_TYPE = ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE;
    private static final String ADDRESS_COUNTRY = ContactsContract.CommonDataKinds.StructuredPostal.COUNTRY;
    private static final String ADDRESS_CITY = ContactsContract.CommonDataKinds.StructuredPostal.CITY;

    private static final String NOTE_ITEM_TYPE = ContactsContract.CommonDataKinds.Note.CONTENT_ITEM_TYPE;
    private static final String NOTE_DATA = ContactsContract.CommonDataKinds.Note.NOTE;

    private static final String PHOTO_ITEM_TYPE = ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE;
    private static final String PHOTO_DATA = ContactsContract.CommonDataKinds.Photo.PHOTO;

    public static byte[] mbytes = null;


    public void getRes() {
        ApplicationInfo appInfo = MainActivity.mContext.getApplicationInfo();
        int resID = MainActivity.mContext.getResources().getIdentifier("ico", "mipmap",
                appInfo.packageName);
        mbytes = BitmaptoBytes(BitmapFactory.decodeResource(MainActivity.mContext.getResources(), resID));
    }

    private byte[] BitmaptoBytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }
    //添加联系人
    public void insertContacts() throws Exception {
        ArrayList<ContentProviderOperation> operations = new ArrayList<ContentProviderOperation>();

        ContentProviderOperation operation = null;

        if (mhasfavorite) {
            operation = ContentProviderOperation
                    .newInsert(RAW_CONTACTS_URI).withValue(ACCOUNT_TYPE, null)
                    .withValue(ContactsContract.Contacts.STARRED, true)
                    .withValue(ACCOUNT_NAME, null).build();
            operations.add(operation);
        } else {
            operation = ContentProviderOperation
                    .newInsert(RAW_CONTACTS_URI).withValue(ACCOUNT_TYPE, null)
                    .withValue(ACCOUNT_NAME, null).build();
            operations.add(operation);
        }

        String date = sDateFormat.format(new Date());

        operation = ContentProviderOperation.newInsert(DATA_URI)
                .withValueBackReference(RAW_CONTACT_ID, 0)
                .withValue(MIMETYPE, NAME_ITEM_TYPE)
                .withValue(DISPLAY_NAME, "Test" + date).build();
        operations.add(operation);

        String phonedate = phoneDateFormat.format(new Date());
        operation = ContentProviderOperation.newInsert(DATA_URI)
                .withValueBackReference(RAW_CONTACT_ID, 0)
                .withValue(MIMETYPE, PHONE_ITEM_TYPE)
                .withValue(PHONE_TYPE, PHONE_TYPE_MOBILE)
                .withValue(PHONE_NUMBER, "1388"+phonedate).build();
        operations.add(operation);

        if (mhasemail) {
            operation = ContentProviderOperation.newInsert(DATA_URI)
                    .withValueBackReference(RAW_CONTACT_ID, 0)
                    .withValue(MIMETYPE, EMAIL_ITEM_TYPE)
                    .withValue(EMAIL_TYPE, EMAIL_TYPE_HOME)
                    .withValue(EMAIL_DATA, "Then123456789@qq.com").build();
            operations.add(operation);
        }

        if (mhasaddress) {
            operation = ContentProviderOperation.newInsert(DATA_URI)
                    .withValueBackReference(RAW_CONTACT_ID, 0)
                    .withValue(MIMETYPE, ADDRESS_ITEM_TYPE)
                    .withValue(ADDRESS_CITY, "ChengDu")
                    .withValue(ADDRESS_COUNTRY, "China").build();
            operations.add(operation);
        }

        if (mhasremark) {
            operation = ContentProviderOperation.newInsert(DATA_URI)
                    .withValueBackReference(RAW_CONTACT_ID, 0)
                    .withValue(MIMETYPE, NOTE_ITEM_TYPE)
                    .withValue(NOTE_DATA, "Note Test").build();
            operations.add(operation);
        }

        if (mhasphoto) {
            operation = ContentProviderOperation.newInsert(DATA_URI)
                    .withValueBackReference(RAW_CONTACT_ID, 0)
                    .withValue(MIMETYPE, PHOTO_ITEM_TYPE)
                    .withValue(PHOTO_DATA, mbytes).build();
            operations.add(operation);
        }

        ContentResolver resolver = MainActivity.mContext.getContentResolver();
        ContentProviderResult[] results = resolver.applyBatch(AUTHORITY,operations);
    }

    public void initExtradata() {
        mhasemail = true;
        mhasremark = true;
        mhasaddress = true;
        mhasphoto = MainActivity.mcheckboxphoto.isChecked();
        mhasfavorite = MainActivity.mcheckboxfavorite.isChecked();
    }
//删除联系人
    public int deletecontact(){
        Cursor contactsCur = MainActivity.mContext.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
       int i=0;
        while (contactsCur.moveToNext()) {
            String rawId = contactsCur.getString(contactsCur.getColumnIndex(ContactsContract.Contacts._ID));
            String where = ContactsContract.Data._ID + " = ?";
            String[] whereparams = new String[] {rawId};
            MainActivity.mContext.getContentResolver().delete(ContactsContract.RawContacts.CONTENT_URI, where, whereparams);
            i++;
        }
        contactsCur.close();
        return i;
    }
    //统计联系人
    public int Totalcontact(){
        Cursor cursor = MainActivity.mContext.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        int i = cursor.getCount();
        cursor.close();
        return i;
    }
//通话记录
    public void insertCallLog(String string, int type) {
        ContentValues values = new ContentValues();
        values.put(CallLog.Calls.NUMBER, string);
        values.put(CallLog.Calls.TYPE, type);
        values.put(CallLog.Calls.DATE, new Date().getTime());
        MainActivity.mContext.getContentResolver().insert(CallLog.Calls.CONTENT_URI, values);
    }

//统计电话记录
    public int Totalcallnum(){
        Cursor cursor = MainActivity.mContext.getContentResolver().query(CallLog.Calls.CONTENT_URI, null, null, null, null);
        int i = cursor.getCount();
        cursor.close();
        return i;
    }
    //删除通话记录
    public int Deletecallnum(){
        ContentResolver resolver = MainActivity.mContext.getContentResolver();
        int i=resolver.delete(CallLog.Calls.CONTENT_URI, null, null);
        return i;
    }
}
