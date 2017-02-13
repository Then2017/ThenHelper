package com.battery;


import android.app.ActivityManager;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaScannerConnection;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.app.DatePickerDialog;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import com.main.thenhelper.MainActivity;

/**
 * Created by theny550 on 2015/8/1.
 */
public class BatteryMain {
    private  String capacity;
//    private static String current;
//    private static String voltage;
//    private static String temp;
    private  String status;
    private  String chargetype;
    private  String health;
    private  String brightness;

    public static String BatteryMonitorPath= Environment.getExternalStorageDirectory()+ "/ThenHelper/BatteryMonitor/";
    public static String BatteryMonitorTXT="";



    //判断Service是否已启动
    public  boolean isServiceRunning(Context mContext,String className) {
        boolean isRunning = false;
        ActivityManager activityManager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceList = activityManager.getRunningServices(Integer.MAX_VALUE);
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

    //电池信息更新
    public BatteryConfig getBatteryInfo (String platform) throws IOException {
        BatteryConfig config=new BatteryConfig();
        SimpleDateFormat dfdate = new SimpleDateFormat("yyyy-MM-dd");
        String date = dfdate.format(new Date());
        SimpleDateFormat dftime = new SimpleDateFormat("HH:mm:ss");
        String time = dftime.format(new Date());
        capacity = BatteryHelper.getBatteryValue(BatteryConstant.capacity);
        config.setCapacity(capacity);
        status = BatteryHelper.getBatteryValue(BatteryConstant.status);
        config.setStatus(status);
        health = BatteryHelper.getBatteryValue(BatteryConstant.health);
        config.setHealth(health);
        brightness = BatteryHelper.getBatteryValue(BatteryConstant.brightness);
        config.setBrightness(brightness);
        config.setDate(date);
        config.setTime(time);
        if(platform.contains("mt6735")){
            //long current = Long.parseLong(BatteryHelper.getBatteryValue(BatteryConstant.mt6735m_current));
            config.setCurrent(0);
            long voltage = Long.parseLong(BatteryHelper.getBatteryValue(BatteryConstant.mt6735m_voltage));
            config.setVoltage(voltage);
            int temp = Integer.parseInt(BatteryHelper.getBatteryValue(BatteryConstant.mt6735m_temp)) / 10;
            config.setTemp(temp);
            config.setChargetype("NA");
        }else {
            long current = Long.parseLong(BatteryHelper.getBatteryValue(BatteryConstant.current)) / 1000;
            config.setCurrent(current);
            long voltage = Long.parseLong(BatteryHelper.getBatteryValue(BatteryConstant.voltage)) / 1000;
            config.setVoltage(voltage);
            int temp = Integer.parseInt(BatteryHelper.getBatteryValue(BatteryConstant.temp)) / 10;
            config.setTemp(temp);
            chargetype = BatteryHelper.getBatteryValue(BatteryConstant.chargetype);
            config.setChargetype(chargetype);
        }
        return config;
    }
    //删除记录
    public int delteRecord () {
        int filecount=0;
        File BatteryPath = new File(BatteryMonitorPath);
        if (!BatteryPath.exists()){
            Log.e("ThenHelper", "does not exist BatteryMonitorPath ");
            }else {
                File[] files = BatteryPath.listFiles();
                for (File file : files) {
                    file.delete();
                    Scanner(file.getAbsolutePath());
                    filecount++;
                }
        }
        Scanner(BatteryMonitorPath);
        return filecount;
    }
    //扫描媒体
    public  void Scanner (String path) {
            MediaScannerConnection.scanFile(MainActivity.mContext, new String[]{path}, null, null);
    }
    //停止后重命名文件
    public  String renameRecord(){
        SimpleDateFormat filedf = new SimpleDateFormat("MMdd_HHmm_ss");
        String fileDate = filedf.format(new java.util.Date());
        String str=BatteryMain.BatteryMonitorTXT;
        File file=new File(str);
        File newfile=new File(str.substring(0,str.length()-4)+"_To_"+fileDate+".txt");
        if(file.exists()){
            file.renameTo(newfile);
        }
        return newfile.getAbsolutePath();
    }
    //创建记录
    public String startRecord() {
        SimpleDateFormat filedf = new SimpleDateFormat("yyyyMMdd_HHmm_ss");
        String fileDate = filedf.format(new java.util.Date());
       // BatteryMonitorPath = BatteryConstant.FileStorage + "/BatteryMonitor/";
        BatteryMonitorTXT = BatteryMonitorPath +fileDate + ".txt";
        File BatteryPath = new File(BatteryMonitorPath);
        File BattertTXT = new File(BatteryMonitorTXT);
        if (!BatteryPath.exists()) {
            BatteryPath.mkdir();
        }
        if (!BattertTXT.exists()) {
            try {
                BattertTXT.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return BatteryMonitorTXT;
    }

    //统计
    public boolean Checkdatetime(String Sdate,String Edate,String Stime,String Etime) {
        if (Sdate.equals("----")||Edate.equals("----")||Stime.equals("----")||Etime.equals("----")) {
            return false;
        }else {
        return true;
        }
    }
    //文本开始时间与结束时间
    public String[] getDateTime(String strFilePath){
        File file = new File(strFilePath);
        String Sdate="----",Stime="----",Edate="----",Etime="----";
        //如果path是传递过来的参数，可以做一个非目录的判断
        if (file.isDirectory())
        {
            Log.e("ThenHelper", "The File doesn't not exist.");
        }
        else
        {
            InputStream instream = null;
            InputStreamReader inputreader = null;
            BufferedReader buffreader = null;
            boolean first=true;
            try {
                instream = new FileInputStream(file);
                inputreader = new InputStreamReader(instream);
                buffreader = new BufferedReader(inputreader);
                String line;
                buffreader.readLine();
                while ((line = buffreader.readLine()) != null) {
                    String[] linesplit = line.split(",");
                    if (linesplit[0].equals("TAG")){
                        continue;
                    }
                    if (first){
                        Sdate=linesplit[0];
                        Stime=linesplit[1];
                        first=false;
                    }
                    Edate=linesplit[0];
                    Etime=linesplit[1];
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                if (instream != null) {
                    instream.close();
                }
                if (inputreader != null) {
                    inputreader.close();
                }
                if (buffreader != null) {
                    buffreader.close();
                }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        String[] strs=new String[]{Sdate,Stime,Edate,Etime};
        return strs;
    }
    //计算电池信息
    public BatteryConfig StatisticsInfo(String strFilePath,String Sdate,String Edate,String Stime,String Etime) throws IOException {
        String path = strFilePath;
        long totalmA=0;
        long totalmV=0;
        int maxtemp=0;
        int mintemp=100;
        long count=0;
        int Scapacity=0;
        int Ecapacity=0;
        String hascharge="flase";
        String hascount="flase";
        BatteryConfig config=new BatteryConfig();
        //打开文件
        File file = new File(path);
        //如果path是传递过来的参数，可以做一个非目录的判断
        if (file.isDirectory())
        {
            Log.e("ThenHelper", "The File doesn't not exist.");
        }
        else
        {
            InputStream instream=null;
            InputStreamReader inputreader=null;
            BufferedReader buffreader=null;
            try {
                 instream = new FileInputStream(file);
                if (instream != null)
                {
                     inputreader = new InputStreamReader(instream);
                     buffreader = new BufferedReader(inputreader);
                    String line;
                    //分行读取
                    buffreader.readLine();
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date Sdatetime = null;
                    Date Edatetime=null;
                    try {
                        Sdatetime=formatter.parse(Sdate + " " + Stime+":00");
                        Edatetime=formatter.parse(Edate + " " + Etime+":00");
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                   // Log.e("ThenHelper", Sdatetime.getTime() + " " + Edatetime.getTime());
                    Date tempdatetime=null;
                    while (( line = buffreader.readLine()) != null) {
                        String[] linesplit=line.split(",");
                        if (linesplit[0].equals("TAG")){
                            continue;
                        }
                        try {
                            tempdatetime=formatter.parse(linesplit[0]+" "+linesplit[1]);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        if (Math.abs((Sdatetime.getTime()-tempdatetime.getTime()))/1000<60&&(Sdatetime.getTime()-tempdatetime.getTime())/1000<0) {
                            Scapacity=Integer.parseInt(linesplit[2]);
                        }
                        if (Math.abs((tempdatetime.getTime() - Edatetime.getTime()))/1000<60&&(tempdatetime.getTime()-Edatetime.getTime())/1000<0) {
                            Ecapacity=Integer.parseInt(linesplit[2]);
                        }
                        if ((tempdatetime.getTime()>=Sdatetime.getTime())&&(tempdatetime.getTime()<Edatetime.getTime())) {
                            totalmA=Integer.parseInt(linesplit[3])+totalmA;
                            totalmV=Integer.parseInt(linesplit[4])+totalmV;
                            count++;
                            if (Integer.parseInt(linesplit[5])>maxtemp){
                                maxtemp=Integer.parseInt(linesplit[5]);
                            }
                            if (Integer.parseInt(linesplit[5])<mintemp){
                                mintemp=Integer.parseInt(linesplit[5]);
                            }
                            if (linesplit[6].equals("Charging")||linesplit[6].equals("Full")) {
                                hascharge="true";
                            }
                        }else {
                            continue;
                        }
                    }
                    if(tempdatetime.getTime()<Edatetime.getTime()){
                        count=0;
                    }
                }
            }
            catch (java.io.FileNotFoundException e)
            {
                Log.e("ThenHelper", "The File doesn't not exist.");
            }
            catch (IOException e)
            {
                Log.e("ThenHelper","the file has mistakes"+"__"+ e.getMessage());
            }
            finally {
                if(instream!=null) {
                    instream.close();
                }
                if(inputreader!=null) {
                    inputreader.close();
                }
                if(buffreader!=null) {
                    buffreader.close();
                }
            }
        }
        if (count==0) {
            config.setHascount(hascount);
            Log.e("ThenHelper", "does exit Sdatetime or Edatetime");
        }else{hascount="true";
        long avgmA=totalmA / count;
        long avgtmV=totalmV / count;
        config.setHascount(hascount);
        config.setCurrent(avgmA);
        config.setVoltage(avgtmV);
        config.setMaxtemp(maxtemp);
        config.setMintemp(mintemp);
        config.setScapacity(Scapacity);
        config.setEcapacity(Ecapacity);
        config.setHascharge(hascharge);
        Log.e("ThenHelper", config.getHascount()+" "+config.getCurrent()+" "+config.getVoltage()+" "+ config.getMaxtemp()+" "+
                config.getMintemp()+" "+config.getScapacity()+" "+config.getEcapacity()+" "+
                config.getHascharge()+" "+config.getHascount());
        }
        return config;
    }
    //计算电池信息BYTAG
    public void StatisticsInfobyTAG(String strFilePath) throws IOException {
        String path = strFilePath;
        //打开文件
        File file = new File(path);
        //如果path是传递过来的参数，可以做一个非目录的判断
        if (file.isDirectory())
        {
            Log.e("ThenHelper", "The File doesn't not exist.");
        }
        else
        {
            InputStream instream=null;
            InputStreamReader inputreader=null;
            BufferedReader buffreader=null;
            try {
                instream = new FileInputStream(file);
                if (instream != null)
                {
                    inputreader = new InputStreamReader(instream);
                    buffreader = new BufferedReader(inputreader);
                    String line;
                    //分行读取
                    buffreader.readLine();
                    long totalmA=0;
                    long totalmV=0;
                    int maxtemp=0;
                    int mintemp=100;
                    long count=0;
                    int Scapacity=0;
                    int Ecapacity=0;
                    String Sdatetime="",Edatetime="";
                    String hascharge="flase";
                    String hascount="flase";
                    boolean start=false;
                    int i=0;
                    BatteryConfig config=new BatteryConfig();
                    while (( line = buffreader.readLine()) != null) {
                        String[] linesplit=line.split(",");
                        if (linesplit[0].equals("Start TAG")) {
                            config.setSTAG("TAG start : "+linesplit[1]+"");
                            start=true;
                            continue;
                        }
                        if (linesplit[0].equals("End TAG")) {
                            i=0;
                            config.setETAG("TAG end : "+linesplit[1]+"\n------------------------------------------------------");
                            start=false;
                            if (count==0) {
                                config.setHascount(hascount);
                                Log.e("ThenHelper", "does exit data between start TAG and end TAG");
                            }else{hascount="true";
                                long avgmA=totalmA / count;
                                long avgtmV=totalmV / count;
                                config.setHascount(hascount);
                                config.setCurrent(avgmA);
                                config.setVoltage(avgtmV);
                                config.setMaxtemp(maxtemp);
                                config.setMintemp(mintemp);
                                config.setScapacity(Scapacity);
                                config.setEcapacity(Ecapacity);
                                config.setHascharge(hascharge);
                                config.setSdatetime(Sdatetime);
                                config.setEdatetime(Edatetime);
//                                Log.e("ThenHelper", config.getHascount()+" "+config.getCurrent()+" "+config.getVoltage()+" "+ config.getMaxtemp()+" "+
//                                        config.getMintemp()+" "+config.getScapacity()+" "+config.getEcapacity()+" "+
//                                        config.getHascharge()+" "+config.getHascount());
                            }
                             totalmA=0;
                             totalmV=0;
                             maxtemp=0;
                             mintemp=100;
                             count=0;
                             Scapacity=0;
                             Ecapacity=0;
                             hascharge="flase";
                             hascount="flase";
                            Sdatetime="";
                            Edatetime="";
                            TAGinfo(config);
                            continue;
                        }
                        if (start==true) {
                            if (i==0) {
                                Sdatetime=linesplit[0]+" "+linesplit[1];
                                Scapacity = Integer.parseInt(linesplit[2]);
                                i=1;
                            }
                            Ecapacity = Integer.parseInt(linesplit[2]);
                            Edatetime = linesplit[0]+" "+linesplit[1];
                            totalmA = Integer.parseInt(linesplit[3]) + totalmA;
                            totalmV = Integer.parseInt(linesplit[4]) + totalmV;
                            count++;
                            if (Integer.parseInt(linesplit[5]) > maxtemp) {
                                maxtemp = Integer.parseInt(linesplit[5]);
                            }
                            if (Integer.parseInt(linesplit[5]) < mintemp) {
                                mintemp = Integer.parseInt(linesplit[5]);
                            }
                            if (linesplit[6].equals("Charging") || linesplit[6].equals("Full")) {
                                hascharge = "true";
                            }
                        }
                    }
                }
            }
            catch (java.io.FileNotFoundException e)
            {
                Log.e("ThenHelper", "The File doesn't not exist.");
            }
            catch (IOException e)
            {
                Log.e("ThenHelper","the file has mistakes"+"__"+ e.getMessage());
            }
            finally {
                if(instream!=null) {
                    instream.close();
                }
                if(inputreader!=null) {
                    inputreader.close();
                }
                if(buffreader!=null) {
                    buffreader.close();
                }
            }
        }
    }
    public static String TAGstring="";
    public String TAGinfo(BatteryConfig config){
        if(config.getHascount().equals("false")){
            TAGstring = TAGstring + "\n" +config.getSTAG()+"\n"+config.getETAG();
        }else {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date Sdatetime = null;
            Date Edatetime=null;
            try {
                Sdatetime=formatter.parse(config.getSdatetime());
                Edatetime=formatter.parse(config.getEdatetime());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Long a=(Edatetime.getTime()-Sdatetime.getTime())/1000/60;
            if (config.getHascharge().equals("true")) {
                TAGstring = TAGstring +
                        config.getSTAG() + "\n" +
                        "  Start : "+config.getSdatetime()+"\n"+
                        "  End : "+config.getEdatetime()+"\n"+
                        "  Time about : "+a+" mins"+"\n"+
                        "  Power Consumption : " + (config.getScapacity() - config.getEcapacity()) + "%\n" +
                        "  Avg Current : " + config.getCurrent() + "mA\n" +
                        "  Avg Voltage : " + config.getVoltage() + "mV\n" +
                        "  Max Temp : " + config.getMaxtemp() + "℃\n" +
                        "  Min Temp : " + config.getMintemp() + "℃\n" +
                        "Warning : Device has been charged" + "\n" +
                        "during the time you input. \n" +
                        config.getETAG()+"\n";
            } else {
                TAGstring = TAGstring +
                        config.getSTAG() + "\n" +
                        "  Start : "+config.getSdatetime()+"\n"+
                        "  End : "+config.getEdatetime()+"\n"+
                        "  Time about : "+a+" mins"+"\n"+
                        "  Power Consumption : " + (config.getScapacity() - config.getEcapacity()) + "%\n" +
                        "  Avg Current : " + config.getCurrent() + "mA\n" +
                        "  Avg Voltage : " + config.getVoltage() + "mV\n" +
                        "  Max Temp : " + config.getMaxtemp() + "℃\n" +
                        "  Min Temp : " + config.getMintemp() + "℃\n"+
                        config.getETAG()+"\n";
            }
        }
        return TAGstring;
    }

    //唤醒
    PowerManager.WakeLock wakeLock = null;
    //获取电源锁，保持该服务在屏幕熄灭时仍然获取CPU时，保持运行
    public void acquireWakeLock()
    {
        if (null == wakeLock)
        {
            PowerManager pm = (PowerManager)MainActivity.mContext.getSystemService(Context.POWER_SERVICE);
            wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK|PowerManager.ON_AFTER_RELEASE, "PostLocationService");
            if (null != wakeLock)
            {
                wakeLock.acquire();
            }
            Log.e("ThenHelper","battery acquireWakeLock");
        }
    }
    //释放设备电源锁
    public void releaseWakeLock()
    {
        if (null != wakeLock)
        {
            wakeLock.release();
            wakeLock = null;
            Log.e("ThenHelper","battery releaseWakeLock");
        }
    }
    /**
     * 保持屏幕唤醒状态（即背景灯不熄灭）
     *
     * @param on
     *            是否唤醒
     */
    private  PowerManager.WakeLock wl;
    public  void keepScreenOn() {
        if(wl==null) {
            PowerManager pm = (PowerManager) MainActivity.mContext.getSystemService(Context.POWER_SERVICE);
            wl = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ON_AFTER_RELEASE, "==KeepScreenOn==");
            if(null!=wl) {
                wl.acquire();
            }
            Log.e("ThenHelper","battery keepScreenOn");
        }
    }
    public void releaseScreenOn(){
        if (wl != null) {
            wl.release();
            wl = null;
        }
        Log.e("ThenHelper","battery releaseScreenOn");
    }

    /**
     * 去掉字符串中特定字符，然后转换成整型
     * @param str 待剪切的字符串
     *
     * @param cut 需要去掉的字符
     *
     * @return result 转换后的整型数
     */
    public int cutStringToInt(String str, String cut) {
        int result = 0;
        if(cut == ""){
            result = Integer.parseInt(str);
        }else{
            int location = str.indexOf(cut);
            String newStr = str.substring(0, location);
            result = Integer.parseInt(newStr);
        }
        return result;
    }
    //读取文本文件中的内容
    public static String ReadTxtFile(String strFilePath)
    {
        String path = strFilePath;
        String content = ""; //文件内容字符串
        //打开文件
        File file = new File(path);
        //如果path是传递过来的参数，可以做一个非目录的判断
        if (file.isDirectory())
        {
            Log.e("ThenHelper", "The File doesn't not exist.");
        }
        else
        {
            try {
                InputStream instream = new FileInputStream(file);
                if (instream != null)
                {
                    InputStreamReader inputreader = new InputStreamReader(instream);
                    BufferedReader buffreader = new BufferedReader(inputreader);
                    String line;
                    //分行读取
                    while (( line = buffreader.readLine()) != null) {
                        content += line + "\n";
                    }
                    instream.close();
                }
            }
            catch (java.io.FileNotFoundException e)
            {
                Log.e("ThenHelper", "The File doesn't not exist.");
            }
            catch (IOException e)
            {
                Log.e("ThenHelper", e.getMessage());
            }
        }
        return content;
    }






}
