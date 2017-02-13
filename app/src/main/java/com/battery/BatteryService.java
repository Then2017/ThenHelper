package com.battery;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.main.thenhelper.MainActivity;
import com.main.thenhelper.R;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class BatteryService extends Service {
    Timer timer;
    int batteryInterval=5000;
    String[] batteryconfig=new String[1];
    public BatteryService() {
    }
    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        Bundle bundle = intent.getExtras();
        batteryconfig=bundle.getStringArray("batteryInterval");
        batteryInterval = Integer.parseInt(batteryconfig[0]);
        Log.e("ThenHelper", "BatteryService onstart and batteryInterval :"+batteryInterval);
        Intent appIntent;
        try {
            appIntent = new Intent(MainActivity.mContext, Class.forName("com.main.thenhelper.MainActivity"));
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            appIntent = new Intent(MainActivity.mContext,MainActivity.class);
            e.printStackTrace();
        }
        appIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        appIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_SINGLE_TOP);

        Notification.Builder builder = new Notification.Builder(this);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, appIntent, 0);
        builder.setContentIntent(contentIntent);
        builder.setSmallIcon(R.mipmap.ico);
        builder.setTicker("Foreground Service Start");
        builder.setContentTitle("ThenHelper Battery Service");
        builder.setContentText("Make this service run in the foreground.");
        Notification notification = builder.build();
        startForeground(1, notification);
//        Notification notification = new Notification(R.mipmap.ico, "Foreground Battery Service Started.", System.currentTimeMillis());
//        //  PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, BatteryService.class), 0);
//        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, appIntent, 0);
//        notification.setLatestEventInfo(this, "ThenHelper Battery Service", "Recording Battery Information...", contentIntent);
//        // 注意使用  startForeground ，id 为 0 将不会显示 notification
//        startForeground(1, notification);

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Message msg = new Message();
                msg.what = 1;
                mHandler.sendMessage(msg);
            }
        }, 0, batteryInterval);
    }
    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void StartRecoder(){
        String platform;
        BatteryMain batteryMain=new BatteryMain();
        try {
            platform=BatteryHelper.execCommand("getprop ro.board.platform");
          //  platform= Build.DISPLAY;
//            if (platform.substring(0,3).toLowerCase().equals("msm")||platform.substring(0,2).toLowerCase().equals("mt")) {
                BatteryHelper.WriteFileValue(batteryMain.getBatteryInfo(platform));
//            }else {
//                MainActivity.BatteryInfo.setText("ThenHelper does not support this platform, if you need to test,please call Then");
//            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    //更新UI
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    StartRecoder();
                    break;
            }
        }
    };

    @Override
    public void onDestroy()
    {
        // TODO Auto-generated method stub
        super.onDestroy();
        stopForeground(true);
        if(timer!=null){
            timer.cancel();
            Log.e("ThenHelper", "End and stop BatteryService and timer ");
        }
    }

}
