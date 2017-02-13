package com.sensor;

import android.media.MediaScannerConnection;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.main.thenhelper.MainActivity;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Then on 2015/9/11.
 */
public class SensorLogger extends Thread {
    private  String SensorMonitorPath= Environment.getExternalStorageDirectory()+ "/ThenHelper/SensorMonitor/";
    SimpleDateFormat filedf = new SimpleDateFormat("yyyyMMdd_HHmm_ss");
    String fileDate = filedf.format(new java.util.Date());
    Date date;
    private FileOutputStream fileOutputStream;
    private StringBuffer currentStrBuff = new StringBuffer();
    private Handler handler=new Handler();
    File file;
    private ArrayList<StringBuffer> sbPool = new ArrayList(6);

    public SensorLogger(String str,String sensorname) throws IOException {
        File path=new File(SensorMonitorPath);
        file=new File(SensorMonitorPath+sensorname+"_"+fileDate+".txt");
        if(!path.exists()){
            path.mkdirs();
        }
        if(!file.exists()){
            file.createNewFile();
        }
        WriteToFile(file.getAbsolutePath(),str,true);
        Toast.makeText(MainActivity.mContext, "The File saves in "+file.getAbsolutePath(), Toast.LENGTH_LONG).show();
    }

    public void addData(long counter,long timestamp,float[] values){
        date = new Date(System.currentTimeMillis());
        currentStrBuff.append(counter + ","+timestamp+"," + filedf.format(date) + ",X=" + values[0] + ",Y=" + values[1] + ",Z=" + values[2] + "\n");
        if (currentStrBuff.length() > 2048) {
            handler.post(new myRunner(currentStrBuff));
        }

    }
    //扫描媒体
    //"/storage/emulated/0/ThenHelper/Media/Video/test.mp4"
    public void Scanner (String path) {
        MediaScannerConnection.scanFile(MainActivity.mContext, new String[]{path}, null, null);
    }

    private class myRunner implements Runnable
    {
        StringBuffer stringBuffer;
        public myRunner(StringBuffer str)
        {
            stringBuffer=str;
        }
        @Override
        public void run() {
            WriteToFile(file.getAbsolutePath(),stringBuffer.toString(),true);
            stringBuffer.setLength(0);
        }
    }

    @Override
    public void run()
    {
        try
        {
            Log.e("ThenHelper", "Strat SensorLogger");
            Looper.prepare();
            handler = new Handler();
            Looper.loop();
            Scanner(file.getAbsolutePath());
            Log.e("ThenHelper", "SensorLogger thread exited gracefully");
            return;
        }
        catch (Throwable localThrowable)
        {
            Scanner(file.getAbsolutePath());
            Log.e("ThenHelper", "failed to greate the looper thread.");
            localThrowable.printStackTrace();
        }
    }
    public void stopLogger(){
        this.handler.post(new Runnable()
        {
            public void run()
            {
                try
                {
                    fileOutputStream.write(currentStrBuff.toString().getBytes());
                    fileOutputStream.flush();
                    if(fileOutputStream!=null) {
                        fileOutputStream.close();
                    }
                    Scanner(file.getAbsolutePath());
                    Looper.myLooper().quit();
                    Log.e("ThenHelper", "stopLogger successfully!");
                    return;
                }
                catch (IOException localIOException)
                {
                    Log.e("ThenHelper", "Failed to write last data/flush/close");
                    localIOException.printStackTrace();
                }
            }
        });
    }
    public void WriteToFile(String FileName,String strContent, boolean isAppended) {
        try {
            fileOutputStream = new FileOutputStream(FileName,isAppended);
            strContent=strContent+"\r\n";
            byte[] initContent = strContent.getBytes("UTF-8");
            fileOutputStream.write(initContent);
         //   fileOutputStream.close();
            fileOutputStream.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
