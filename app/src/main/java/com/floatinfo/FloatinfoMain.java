package com.floatinfo;

import android.app.ActivityManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.widget.Toast;

import com.main.thenhelper.MainActivity;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by Then on 2015/8/23.
 */
public class FloatinfoMain {
    private Context mcontext;
    private WifiInfo wifiInfo = null;       //获得的Wifi信息
    private WifiManager wifiManager = null; //Wifi管理器

    StringBuffer btstr=new StringBuffer();
    String btstrfi="";
    BluetoothAdapter btadapter=null;
    BTReceiver btReceiver=null;
    DecimalFormat df  = new DecimalFormat("######0.00");
    ActivityManager am=null;
    public FloatinfoMain(Context context) {
        mcontext = context;
    }

    //本地蓝牙信息
    public void startBT(){
        btadapter = BluetoothAdapter.getDefaultAdapter();
        if(btadapter==null){
            btstr.setLength(0);
            btstr.append("Device doesn't have BT\n");
            Log.e("ThenHelper", "Device doesn't have BT");
            return;
        }
        if (!btadapter.isEnabled()) {
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //   intent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 3600); //3600为蓝牙设备可见时间
            mcontext.startActivity(intent);
            Toast.makeText(MainActivity.mContext, "Pls open Bluetooth first!", Toast.LENGTH_SHORT).show();
            return;
         //   btadapter.enable();
        }
        while(!(btadapter.getState()==BluetoothAdapter.STATE_ON)){
          //  Log.e("ThenHelper", "btadapter wait discovery");
        }
        btadapter.startDiscovery();
        Log.e("ThenHelper", "btadapter start discovery");
        //获得已配对的远程蓝牙设备的集合
//        Set<BluetoothDevice> devices = adapter.getBondedDevices();
//        if(devices.size()>0){
//            for(Iterator<BluetoothDevice> it = devices.iterator();it.hasNext();){
//                BluetoothDevice device = (BluetoothDevice)it.next();
//                //打印出远程蓝牙设备的物理地址
//                System.out.println(device.getAddress());
//            }
//        }else{
//            System.out.println("还没有已配对的远程蓝牙设备！");
//        }
    }
    public void cancelBT(){
       // btstr.append("Cancel Discovery.\n");
        if(btadapter!=null) {
            if (btadapter.isDiscovering()) {
                btadapter.cancelDiscovery();
            }
        }
       // btadapter.disable();
    }
    //周围蓝牙信息
    public class BTReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            //当设备开始扫描时。
            if(BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)){
                btstr.setLength(0);
                btstr.append("BT Discovery Started :\n");
            } else if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                //从Intent得到blueDevice对象
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                //信号强度。
                int rssi = intent.getExtras().getShort(BluetoothDevice.EXTRA_RSSI);
                btstr.append(device.getName() + "=" + rssi + "dBm\n");
               // FloatinfoService.BTSignal.setText(btstr.toString());
            }else if(BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)){
                btstr.append("Discovery Finished.\n");
                mcontext.unregisterReceiver(btReceiver);
                btReceiver = null;
                Log.e("ThenHelper", "unregisterReceiver btReceiver in BT BroadcastReceiver");
            }
        }
    }
    public void registeBT(){
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.bluetooth.device.action.FOUND");//发现蓝牙
        filter.addAction("android.bluetooth.adapter.action.DISCOVERY_STARTED");//开始搜索
        filter.addAction("android.bluetooth.adapter.action.DISCOVERY_FINISHED");//搜索完成
        btReceiver=new BTReceiver();
        mcontext.registerReceiver(btReceiver, filter);
        Log.e("ThenHelper", "registerReceiver btReceiver");
    }
    public void unregisteBT(){
        if(btReceiver!=null) {
            mcontext.unregisterReceiver(btReceiver);
            btReceiver = null;
        }
        Log.e("ThenHelper", "unregisterReceiver btReceiver");
    }
    public String getBTinfo(){
        return btstr.toString();
    }

    //WIFI信息  密码data/misc/wifi/wpa_supplicant.conf
    public String getWIFIinfo(){
        StringBuffer str=new StringBuffer();
        StringBuffer str1=new StringBuffer();
        wifiManager = (WifiManager) mcontext.getSystemService(Context.WIFI_SERVICE);
        wifiInfo = wifiManager.getConnectionInfo();
        int APchannel=getChannelByFrequency(wifiInfo.getFrequency());
        int num=0,num1=0;
        String currentcap="";
        if(wifiManager.isWifiEnabled()) {
            str1.append("Other AP : \n");
            List<ScanResult> results = wifiManager.getScanResults();
            for (ScanResult result : results) {
                String cap=result.capabilities;
                if(!("\""+result.SSID+"\"").equals(wifiInfo.getSSID())) {
                    int i = getChannelByFrequency(result.frequency);
                    if (APchannel == i) {
                        num++;
                    }
                    if(i>15){
                        str1.append(result.SSID + "=" + result.level + "dBm " + i + "C 5G "+getWiFiCipherType(cap)+"\n");
                    }else if(i<15&i>0) {
                        str1.append(result.SSID + "=" + result.level + "dBm " + i + "C 2.4G "+getWiFiCipherType(cap)+"\n");
                    }else if(i==-1){
                        str1.append(result.SSID + "=" + result.level + "dBm unknowC "+getWiFiCipherType(cap)+"\n");
                    }
                    num1++;
                }else {
                    currentcap=getWiFiCipherType(cap);
                }
            }
            if(isWifiConnected(mcontext)) {
                str.append("Current AP : \n");
                if(APchannel>15){
                    str.append("SSID=" + wifiInfo.getSSID() + "\nChannel=" + APchannel + "C 5G "+currentcap+"\n");
                }else if(APchannel<15&APchannel>0) {
                    str.append("SSID=" + wifiInfo.getSSID() + "\nChannel=" + APchannel + "C 2.4G "+currentcap+"\n");
                }else if(APchannel==-1){
                    str.append("SSID=" + wifiInfo.getSSID() + "\nChannel=unknow "+currentcap+"\n");
                }
                str.append("Signal=" + wifiInfo.getRssi() + "dBm LinkSpeed=" + wifiInfo.getLinkSpeed() + wifiInfo.LINK_SPEED_UNITS + "\n");
                str.append("Total find "+num1+"AP, Channel "+APchannel+" has "+num+"AP\n");
            }else{
                str.append("Device doesn't connect any AP.\n");
            }

        }else
        {
            str.append("WiFi is closed");
        }
        return str.toString()+str1.toString();
    }
    //判断WIFI是否连接
    public static boolean isWifiConnected(Context context)
    {
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiNetworkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if(wifiNetworkInfo.isConnected())
        {
            return true ;
        }
        return false ;
    }

    //判断加密方式
    public String getWiFiCipherType(String cap) {
                    if (cap.contains("WPA") || cap.contains("wpa")) {
                       return "WPA";
                    } else if (cap.contains("WEP") || cap.contains("wep")) {
                       return "WEP";
                    }else if(cap.contains("EAP")||cap.contains("eap")||cap.contains("8021")){
                       return "EAP";
                    } else
                    {
                      return "None";
                    }
    }
    /**
     * 根据频率获得信道
     *
     * @param frequency
     * @return
     */
    public static int getChannelByFrequency(int frequency) {
        int channel = -1;
        switch (frequency) {
            case 2412:
                channel = 1;
                break;
            case 2417:
                channel = 2;
                break;
            case 2422:
                channel = 3;
                break;
            case 2427:
                channel = 4;
                break;
            case 2432:
                channel = 5;
                break;
            case 2437:
                channel = 6;
                break;
            case 2442:
                channel = 7;
                break;
            case 2447:
                channel = 8;
                break;
            case 2452:
                channel = 9;
                break;
            case 2457:
                channel = 10;
                break;
            case 2462:
                channel = 11;
                break;
            case 2467:
                channel = 12;
                break;
            case 2472:
                channel = 13;
                break;
            case 2484:
                channel = 14;
                break;
            case 5745:
                channel = 149;
                break;
            case 5765:
                channel = 153;
                break;
            case 5785:
                channel = 157;
                break;
            case 5805:
                channel = 161;
                break;
            case 5825:
                channel = 165;
                break;
        }
        return channel;
    }

    //获取当前运行的程序的包名
//    然而上面的方法是得到却是当前Activity的进程名，一般进程名就是包名，但是如果在Manifest设置Activity的进程名，上面方法就无效了，如：
//    android:name="com.xxx.demo.DemoActivity"
//    android:process=".aaa" />
    public  ActivityManager.RunningAppProcessInfo getCurrentPkg() {
        ActivityManager.RunningAppProcessInfo currentInfo = null;
        Field field = null;
        int START_TASK_TO_FRONT = 2;
        String pkgName = null;
        try {
            field = ActivityManager.RunningAppProcessInfo.class.getDeclaredField("processState");
        } catch (Exception e) { e.printStackTrace(); }
         am = (ActivityManager) mcontext.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appList = am.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo app : appList) {
            if (app.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                Integer state = null;
                try {
                    state = field.getInt( app );
                } catch (Exception e) { e.printStackTrace(); }
                if (state != null && state == START_TASK_TO_FRONT) {
                    currentInfo = app;
                    break;
                }
            }
        }
        if (currentInfo != null) {
            pkgName = currentInfo.processName;
        }
        return currentInfo;
    }
    //获取当前程序信息
    public String getAppPackageInfo(){
        ActivityManager.RunningAppProcessInfo currentInfo=null;
        currentInfo=getCurrentPkg();
        String str="Unknow",packagename;
        if (currentInfo != null) {
            packagename=currentInfo.processName;
            // 获得该进程占用的内存
            int[] myMempid = new int[] { currentInfo.pid };
            // 此MemoryInfo位于android.os.Debug.MemoryInfo包中，用来统计进程的内存信息
            Debug.MemoryInfo[] memoryInfo = am.getProcessMemoryInfo(myMempid);
            // 获取进程占内存用信息kb单位
            int memSize = memoryInfo[0].getTotalPss();
            str = "APP name : "+getAppName(packagename)+"\n"+
                    "Package : "+currentInfo.processName+"\n"+
                    "Main activity : "+getAppClassNanem(packagename)+"\n"+
                    "Pid="+currentInfo.pid+" Uid="+currentInfo.uid+" CPU="+df.format(getProcessCpuRate(currentInfo.pid))+"%\n"+
                    "Memory used="+memSize+"KB  " + df.format(memSize / (float) getTotalMemory())+"%\n";
        }
        return str;
    }
    //得到当前应用CPU数据
    public float getProcessCpuRate(int pid)
    {
        float totalCpuTime1 = getTotalCpuTime();
        float processCpuTime1 = getAppCpuTime(pid);
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        float totalCpuTime2 = getTotalCpuTime();
        float processCpuTime2 = getAppCpuTime(pid);
        float cpuRate = 100 * (processCpuTime2 - processCpuTime1)
                / (totalCpuTime2 - totalCpuTime1);
        return cpuRate;
    }

    // 获取系统总CPU使用时间
    public  long getTotalCpuTime()
    {
        String[] cpuInfos = null;
        try
        {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    new FileInputStream("/proc/stat")), 1000);
                String load = reader.readLine();
            reader.close();
            cpuInfos = load.split(" ");
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
        long totalCpu = Long.parseLong(cpuInfos[2])
                + Long.parseLong(cpuInfos[3]) + Long.parseLong(cpuInfos[4])
                + Long.parseLong(cpuInfos[6]) + Long.parseLong(cpuInfos[5])
                + Long.parseLong(cpuInfos[7]) + Long.parseLong(cpuInfos[8]);
        return totalCpu;
    }
    // 获取应用占用的CPU时间
    public  long getAppCpuTime(int pid)
    {
        String[] cpuInfos = null;
        try
        {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    new FileInputStream("/proc/" + pid + "/stat")), 1000);
            String load = reader.readLine();
            reader.close();
            cpuInfos = load.split(" ");
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
        long appCpuTime = Long.parseLong(cpuInfos[13])
                + Long.parseLong(cpuInfos[14]) + Long.parseLong(cpuInfos[15])
                + Long.parseLong(cpuInfos[16]);
        return appCpuTime;
    }
    //得到总内存
    private long getTotalMemory(){
        long totalMemorySize=0;
        try {
            String dir = "/proc/meminfo";
            FileReader fr = new FileReader(dir);
            BufferedReader br = new BufferedReader(fr, 2048);
            String memoryLine = br.readLine();
            String subMemoryLine = memoryLine.substring(memoryLine.indexOf("MemTotal:"));
            br.close();
            totalMemorySize = Integer.parseInt(subMemoryLine.replaceAll("\\D+", ""));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return totalMemorySize;
    }
    //获取程序名
    public String getAppName(String packname){
        PackageManager pm = mcontext.getPackageManager();
        try {
            ApplicationInfo info = pm.getApplicationInfo(packname, 0);
            return info.loadLabel(pm).toString();
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "Unknow";
    }
    //获取程序入口
    public String getAppClassNanem(String packname){
        // PackageManager pm = this.getPackageManager();
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> mApps = mcontext.getPackageManager().queryIntentActivities(mainIntent, 0);
        for (int i = 0; i < mApps.size(); i++) {
            //  PackageInfo packageInfo = pm.getPackageInfo(packname, 0);
            ResolveInfo info = mApps.get(i);
            String packagename = info.activityInfo.packageName;
            String appname = info.activityInfo.name;
            if(packagename.equals(packname)){
                return appname;
            }
        }
        return "Unknow";
    }

    //启动悬浮窗
    public void startFloatinfo(){
      // if (isServiceRunning(MainActivity.mContext, "com.floatinfo.FloatinfoService")) {
            Intent localIntent = new Intent(MainActivity.mContext,FloatinfoService.class);
            MainActivity.mContext.stopService(localIntent);
            Log.e("ThenHelper", "com.floatinfo.FloatinfoService is running,stop it. " );
       // }
        Intent intent = new Intent(MainActivity.mContext,FloatinfoService.class);
        Bundle bundle = new Bundle();
        bundle.putStringArray("floatinfo",MainActivity.floatinfostr);
        intent.putExtras(bundle);
        MainActivity.mContext.startService(intent);
        Log.e("ThenHelper", "start com.floatinfo.FloatinfoService windows ");
    }
    //判断Service是否已启动
    public  boolean isServiceRunning(Context mContext,String className) {
        boolean isRunning = false;
        ActivityManager activityManager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceList = activityManager.getRunningServices(100);
        if (!(serviceList.size()>0)) {
            return false;
        }
        for (int i=0; i<serviceList.size(); i++) {
            if (serviceList.get(i).service.getClassName().equals(className) == true) {
                isRunning = true;
                break;
            }
        }
        return isRunning;
    }


    //读取值
    public   String getexistValue(String key) throws IOException {
        String value = "0";
        File f = new File(key);
        if (f.exists()) {
            value =  getValue(f);
        }
        return value;
    }

    public   String getValue(File f) {
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
}
