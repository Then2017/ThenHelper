package com.screenrecord;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.main.thenhelper.MainActivity;
import com.main.thenhelper.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Then on 2015/9/26.
 */
public class ScreenRecordService extends Service {
    Timer stoptimer,delaytimer;
    private int countdowntime,delaytime;
    String filepath;
    Toast toast;
    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        Log.e("ThenHelper", "ScreenRecord Service onstart.");
        Intent appIntent;
        try {
            appIntent = new Intent(MainActivity.mContext, Class.forName("com.main.thenhelper.MainActivity"));
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            appIntent = new Intent(MainActivity.mContext,MainActivity.class);
            e.printStackTrace();
        }
        appIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        appIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        Notification.Builder builder = new Notification.Builder(this);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 1, appIntent, 0);
        builder.setContentIntent(contentIntent);
        builder.setSmallIcon(R.mipmap.ico);
        builder.setTicker("Foreground Service Start");
        builder.setContentTitle("ThenHelper Screen Service");
        builder.setContentText("Make this service run in the foreground.");
        Notification notification = builder.build();
        startForeground(1, notification);

    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        delaytime=MainActivity.recorddelaynum;
        countdowntime=MainActivity.recordCountdownnum;
        int totalcountdown=countdowntime;
        if(delaytime!=-1) {
             totalcountdown = delaytime + totalcountdown;
        }

        if(delaytime!=-1){
            toast=Toast.makeText(MainActivity.mContext, delaytime + " s...", Toast.LENGTH_SHORT);
            delaytimer = new Timer();
            delaytimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    Message msg1 = new Message();
                    msg1.what = 1;
                    mHandler.sendMessage(msg1);
                    delaytime--;
                    if(delaytime ==0&&delaytimer!=null){
                        Message msg2 = new Message();
                        msg2.what = 2;
                        mHandler.sendMessage(msg2);
                        if(delaytimer!=null) {
                            delaytimer.cancel();
                        }
                    }
                }
            }, 0,1000);
            Log.e("ThenHelper", "use delaytime to screen record.");
        }else {
            filepath = MainActivity.screenRecord.setRecord(MainActivity.mediaProjection, MainActivity.recordResolution,
                   MainActivity.recordfilename, MainActivity.recordBitRatenum,MainActivity.recordaudioBox.isChecked());
        }

        if(countdowntime!=-1){
            stoptimer = new Timer();
            stoptimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    Intent localIntent = new Intent(MainActivity.mContext,ScreenRecordService.class);
                    MainActivity.mContext.stopService(localIntent);
                    Log.e("ThenHelper", "End and stop ScreenRecordService by countdown time !");
                }
            }, totalcountdown*1000);
        }

    }
    //更新UI
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    toast.setText(delaytime + " s...");
                    toast.show();
                    break;
                case 2:
                    toast.setText("Start Screen Record !");
                    filepath = MainActivity.screenRecord.setRecord(MainActivity.mediaProjection, MainActivity.recordResolution,
                            MainActivity.recordfilename, MainActivity.recordBitRatenum,MainActivity.recordaudioBox.isChecked());
                    if(delaytimer!=null) {
                        delaytimer.cancel();
                    }
                  //  toast.cancel();
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
        delaytime=-1;
        countdowntime=-1;
        MainActivity.recordrunable = false;
        MainActivity.StartRecord.setText("Start");
        MainActivity.recorddelete.setEnabled(true);
        MainActivity.screenRecord.stopScreen();
        MainActivity.recordfloatBox.setEnabled(true);
        if(MainActivity.mediaProjection!=null){
            MainActivity.mediaProjection.stop();
            MainActivity.mediaProjection=null;
        }
        if(filepath!=null) {
            Toast.makeText(MainActivity.mContext, "Stop to record , video is in " + filepath, Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(MainActivity.mContext, "Stop Screen Record !", Toast.LENGTH_LONG).show();
        }
        if(stoptimer!=null){
            stoptimer.cancel();
            stoptimer=null;
        }
        if(delaytimer!=null){
            delaytimer.cancel();
            delaytimer=null;
        }
        Log.e("ThenHelper", "End and stop ScreenRecordService ");
    }



    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
