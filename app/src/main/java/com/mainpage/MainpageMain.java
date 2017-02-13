package com.mainpage;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import com.main.thenhelper.MainActivity;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by theny550 on 2015/8/2.
 */
public class MainpageMain {
    DecimalFormat df  = new DecimalFormat("######0.0");

    public MainpageConfig getInfo() throws IOException{
        MainpageConfig config=new MainpageConfig();
        config.setModel(execCommand("getprop ro.product.model"));
        config.setPlatform(execCommand("getprop ro.board.platform"));
        config.setOuterversion(execCommand("getprop ro.build.display.id"));
        config.setInnerversion(execCommand("getprop ckt.internal.version"));
        config.setSimstatus(execCommand("getprop gsm.sim.state"));
        config.setRleasekey(execCommand("getprop ro.build.tags"));
        config.setIsroot(execCommand("getprop ro.debuggable"));
       // config.setUsbconfig(execCommand("getprop sys.usb.state"));
       // config.setLogrunable(execCommand("getprop persist.sys.qlogd"));
        config.setBuildtype(execCommand("getprop ro.build.type"));
        return config;
    }

    //设备信息显示
    public String DUTinfo() throws IOException {
        MainpageConfig config=getInfo();
        String IMEI=getIMEIsingle(),BTMAC=getBTMAC(),WIFIMAC=getWIFIMAC();
        if(checkIMEI(IMEI)){
            IMEI="IMEI : "+IMEI+"\n";
        }else{
            IMEI= "IMEI : " + IMEI + " (this IMEI is invalid)\n";
        }

        if(checkMAC(BTMAC)||BTMAC.contains("!")){
            BTMAC="BT MAC : "+BTMAC+"\n";
        }else{
            BTMAC="BT MAC : "+BTMAC+ " (this MAC is invalid)\n";
        }

        if(checkMAC(WIFIMAC)||WIFIMAC.contains("!")) {
            WIFIMAC="WiFi MAC : "+WIFIMAC+"\n";
        }else {
            WIFIMAC="WiFi MAC : "+WIFIMAC+ " (this MAC is invalid)\n";
        }

        String info="Device Info : \n"+
                "Model : " + config.getModel() +
                "Platform : " + config.getPlatform() +
                "Build type : " + config.getBuildtype() +
                "SIM status : " + config.getSimstatus() +
                "Signature : " + config.getRleasekey() +
                "Root : " + config.getIsroot()+
                "External version : \n" + config.getOuterversion() +
                "Internal version : \n" + config.getInnerversion()+
                "Screen : "+getScreenResolution()+
                WIFIMAC+
                BTMAC+
                IMEI
                ;
        return info;
    }
    //test
    public String getDoubleIMEI(){
        getIMEI info=new getIMEI();
        MtkDoubleInfo mtkDoubleInfo = new MtkDoubleInfo();
        GaotongDoubleInfo gaotongDoubleInfo = new GaotongDoubleInfo();
        String str="";
        if(info.isDoubleSim(MainActivity.mContext) instanceof MtkDoubleInfo){
            mtkDoubleInfo=getIMEI.initMtkDoubleSim(MainActivity.mContext);
        }else if(info.isDoubleSim(MainActivity.mContext) instanceof GaotongDoubleInfo){
            gaotongDoubleInfo=getIMEI.initQualcommDoubleSim(MainActivity.mContext);
            str=gaotongDoubleInfo.getImei_1()+gaotongDoubleInfo.getImei_2();
        }else {
            str=getIMEIsingle();
        }
        return str;
    }
    //判断IMEI是否合法
    public boolean checkIMEI(String IMEI) {
        if(IMEI==null){
            return false;
        }
        if(IMEI.length()!=15){
            return false;
        }
        String imeiString=IMEI.substring(0,IMEI.length()-1);//前14位
        char[] imeiChar=imeiString.toCharArray();
        int resultInt=0;
        for (int i = 0; i < imeiChar.length; i++) {
            int a=Integer.parseInt(String.valueOf(imeiChar[i]));
            i++;
            final int temp=Integer.parseInt(String.valueOf(imeiChar[i]))*2;
            final int b=temp<10?temp:temp-9;
            resultInt+=a+b;
        }
        resultInt%=10;
        resultInt=resultInt==0?0:10-resultInt;
        if((imeiString+resultInt).equals(IMEI)){
            return true;
        }else {
            return false;
        }
    }
    //获取IMEI
    public String getIMEIsingle(){
        TelephonyManager tm = (TelephonyManager) MainActivity.mContext.getSystemService(Context.TELEPHONY_SERVICE);
        String str=tm.getDeviceId();
        return str;
    }
    //判断MAC地址是否合法
    public boolean checkMAC(String MAC){
        Pattern p = Pattern.compile("[0-9A-F][048C]:[0-9A-F]{2}:[0-9A-F]{2}:[0-9A-F]{2}:[0-9A-F]{2}:[0-9A-F]{2}");
        Matcher m = p.matcher(MAC.toUpperCase());
        return m.matches();
    }
    //获取BTMAC
    public String getBTMAC(){
        BluetoothAdapter bAdapt= BluetoothAdapter.getDefaultAdapter();
        if (bAdapt != null) {
            if (!bAdapt.isEnabled()) {
                return "Pls open BT first!";
            }else {
                return bAdapt.getAddress();
            }
        }else {
            return "Device doesn't have BT!";
        }
    }
    //获取WIFIMAC
    public String getWIFIMAC(){
        WifiManager wifi = (WifiManager) MainActivity.mContext.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        if(info.getMacAddress()!=null) {
            return info.getMacAddress();
        }else {
            return "Pls open WiFi first!";
        }
    }
    //获取屏幕分辨率
    public String getScreenResolution(){
        WindowManager windowManager = (WindowManager) MainActivity.mContext.getSystemService(Context.WINDOW_SERVICE);

//        DisplayMetrics metric = new DisplayMetrics();
//        windowManager.getDefaultDisplay().getMetrics(metric);
//        int width = metric.widthPixels;  // 屏幕宽度（像素）
//        int height = metric.heightPixels;  // 屏幕高度（像素）
//        float density = metric.density;  // 屏幕密度（0.75 / 1.0 / 1.5）
//        int densityDpi = metric.densityDpi;  // 屏幕密度DPI（120 / 160 / 240）
        Point point = new Point();
        windowManager.getDefaultDisplay().getRealSize(point);
        DisplayMetrics dm = MainActivity.mContext.getResources().getDisplayMetrics();
       // Log.e("ThenHelper",""+dm.ydpi+"   "+dm.xdpi);
        double x = Math.pow(point.x / dm.xdpi, 2);
        double y = Math.pow(point.y / dm.ydpi, 2);
        double screenInches = Math.sqrt(x + y);
        String str="";
        if(point.y==1920&&point.x==1080){
            str="FHD";
        }else if(point.y==1080&&point.x==720){
            str="HD";
        }
        else if(point.y==2560&&point.x==1440){
            str="WQHD";
        }

//        windowManager.getDefaultDisplay().getMetrics(dm);
//        double diagonalPixels = Math.sqrt(Math.pow(dm.widthPixels, 2) + Math.pow(dm.heightPixels, 2));
//        double a= diagonalPixels / (160 * dm.density);
//        Log.e("ThenHelper","1111 "+a);
        return str+" "+point.x+"X"+point.y+" "+df.format(screenInches)+"′ DPI="+dm.densityDpi+" density="+dm.density+"\n";
    }
    //执行命令
    public static String execCommand(String command) throws IOException {
        Process proc = null;
        Runtime runtime = Runtime.getRuntime();
        StringBuffer stringBuffer = new StringBuffer();
        proc = runtime.exec(new String[]{"/system/bin/sh", "-c",command});
        BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
        try {
//            if (proc.waitFor() != 0) {
//                System.err.println("ThenHelper exit value = " + proc.exitValue());
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

    //封面图片
    public static Bitmap getLoacalBitmap(String url) {
        try {
            FileInputStream fis = new FileInputStream(url);
            return BitmapFactory.decodeStream(fis);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
