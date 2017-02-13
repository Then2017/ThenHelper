package com.battery;

import java.io.*;

import android.os.Build;
import android.util.Log;

import com.main.thenhelper.MainActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2015/7/28.
 */
public class BatteryHelper {
    public static String getBatteryValue(String key) throws IOException{
        String value = "0";
        File f = new File(key);
        if (f.exists()) {
            value =  getValue(f);
        }
        return value;
    }

    public static String getValue(File f) {
        String text = null;
        try {
            FileInputStream fs = new FileInputStream(f);
            DataInputStream ds = new DataInputStream(fs);
            text = ds.readLine();
            ds.close();
            fs.close();
        }
        catch (Exception ex) {
            Log.e("ThenHelper", ex.getMessage());
            ex.printStackTrace();
        }
        String NA = "NA";
        if (text != null)
        {
            return text;
        }
        return NA;
    }

    public static String execCommand(String command) throws IOException {
        Process proc;
        Runtime runtime = Runtime.getRuntime();
        StringBuffer stringBuffer = new StringBuffer();
        proc = runtime.exec(new String[]{"/system/bin/sh", "-c",command});
        BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
        try {
//            if (proc.waitFor() != 0) {
//               Log.e("ThenHelper",  "execCommand :\n"+proc.exitValue());
//            }
            String line=null;
            while ((line = in.readLine()) != null) {
                stringBuffer.append(line + "\n");
            }
        } finally {
            if (proc!=null){
                proc.destroy();
            }
            if (in!=null){
                in.close();
            }
        }
        return stringBuffer.toString();
    }

    public static void WriteFileTitle(){
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("Date,");
        stringBuffer.append("Time,");
        stringBuffer.append("Capacity_%,");
        stringBuffer.append("Current_mA,");
        stringBuffer.append("Voltage_mV,");
        stringBuffer.append("Temp_°C,");
        stringBuffer.append("Status,");
        stringBuffer.append("Chargetype,");
        stringBuffer.append("Health,");
        stringBuffer.append("Brightness,");
        stringBuffer.append(Build.MODEL);
        WriteToFile(BatteryMain.BatteryMonitorTXT, stringBuffer.toString(), true);
    }
    public static void WriteFileValue(BatteryConfig config){
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(config.getDate()+",");
        stringBuffer.append(config.getTime()+",");
        stringBuffer.append(config.getCapacity()+",");
        stringBuffer.append(config.getCurrent()+",");
        stringBuffer.append(config.getVoltage()+",");
        stringBuffer.append(config.getTemp()+",");
        stringBuffer.append(config.getStatus()+",");
        stringBuffer.append(config.getChargetype()+",");
        stringBuffer.append(config.getHealth()+",");
        stringBuffer.append(config.getBrightness());
        WriteToFile(BatteryMain.BatteryMonitorTXT, stringBuffer.toString(), true);
    }
    //插入TAG
    public static void InsertTAG(String insert){
        String TAG=insert+",*********I'm the dividing line*********";
        WriteToFile(BatteryMain.BatteryMonitorTXT, TAG.toString(), true);
    }

    public static void WriteToFile(String FileName,String strContent, boolean isAppended) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(FileName,isAppended);
            strContent=strContent+"\r\n";
            byte[] initContent = strContent.getBytes("UTF-8");
            fileOutputStream.write(initContent);
            fileOutputStream.close();
            fileOutputStream.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


}
