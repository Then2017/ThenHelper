package com.sensor;


import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.main.thenhelper.R;

import java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;

public class SensorService extends Service
{

    //定义浮动窗口布局
    RelativeLayout mFloatLayout;
    WindowManager.LayoutParams wmParams;
    //创建浮动窗口设置布局参数的对象
    WindowManager mWindowManager;

    Button Close,Show,ChangeColor;
    private int sensortype=0;
    private String sensorname;
    final float[] data = new float[3];
    DecimalFormat df  = new DecimalFormat("######0.000");
    private  TextView SensorName,SensorDataX,SensorDataY,SensorDataZ,OtherInfo;
    private CompassView compass_pointer;
    SensorManager sm = null;
    Sensor item;
    SensorEventListener mySensorListener=null;
    Handler mHandler=new Handler();
    private Timer timer;
    private boolean showrunable=false;
    SensorOtherInfo sensorOtherInfo;
    int backgroundcolor = 1;
    int[] ischange = {0,1};
    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        Bundle bundle = intent.getExtras();
        String[] type = bundle.getStringArray("sensortype");
        String[] name = bundle.getStringArray("sensorname");
        sensortype=Integer.parseInt(type[0]);
        sensorname=name[0];
        Log.e("ThenHelper", "  Service onstart :" + sensortype + "  " + sensorname);
        createFloatView();
        monitorsensor();
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                mHandler.post(GuiUpdater);
            }
        }, 0, 200);
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
       // Log.e("ThenHelper", "oncreat"+sensorname);
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        // TODO Auto-generated method stub
        return null;
    }

    private void createFloatView()
    {
        wmParams = new WindowManager.LayoutParams();
        //获取的是WindowManagerImpl.CompatModeWrapper
        mWindowManager = (WindowManager)getApplication().getSystemService(getApplication().WINDOW_SERVICE);
        Log.e("ThenHelper", "Create Sensor Float Windows first"+sensorname);
        //设置window type
        wmParams.type = LayoutParams.TYPE_PHONE;
        //设置图片格式，效果为背景透明
        wmParams.format = PixelFormat.TRANSLUCENT;
        //设置浮动窗口不可聚焦（实现操作除浮动窗口外的其他可见窗口的操作）
        wmParams.flags = LayoutParams.FLAG_NOT_FOCUSABLE;
        //调整悬浮窗显示的停靠位置为左侧置顶
        wmParams.gravity = Gravity.LEFT | Gravity.TOP;
        // 以屏幕左上角为原点，设置x、y初始值，相对于gravity
        wmParams.x = 0;
        wmParams.y = 0;
        //设置悬浮窗口长宽数据
        wmParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
		 /*// 设置悬浮窗口长宽数据
        wmParams.width = 200;
        wmParams.height = 80;*/
        LayoutInflater inflater = LayoutInflater.from(getApplication());
        //获取浮动窗口视图所在布局
        mFloatLayout = (RelativeLayout) inflater.inflate(R.layout.sensor_float, null);
        //添加mFloatLayout
        mWindowManager.addView(mFloatLayout, wmParams);
        //浮动窗口按钮
        ChangeColor = (Button)mFloatLayout.findViewById(R.id.ChangeColor);
        Show = (Button)mFloatLayout.findViewById(R.id.Show);
        Close = (Button)mFloatLayout.findViewById(R.id.Close);
        SensorName= (TextView)mFloatLayout.findViewById(R.id.SensorName);
        SensorDataX = (TextView)mFloatLayout.findViewById(R.id.SensorDataX);
        SensorDataY = (TextView)mFloatLayout.findViewById(R.id.SensorDataY);
        SensorDataZ = (TextView)mFloatLayout.findViewById(R.id.SensorDataZ);
        OtherInfo=(TextView)mFloatLayout.findViewById(R.id.OtherInfo);
        compass_pointer=(CompassView)mFloatLayout.findViewById(R.id.compass_pointer);
        compass_pointer.setVisibility(View.GONE);
        OtherInfo.setVisibility(View.GONE);
        SensorName.setText(sensorname + " : ");
        Close.setBackgroundColor(0x00000000);
        Show.setBackgroundColor(0x00000000);
        ChangeColor.setBackgroundColor(0x00000000);
        Close.setVisibility(View.GONE);
        ChangeColor.setVisibility(View.GONE);

        mFloatLayout.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec
                .makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        //绑定触摸移动监听
        final int StatusBarHeight=getStatusBarHeight();
        mFloatLayout.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                wmParams.x = (int) event.getRawX() - mFloatLayout.getWidth() / 2;
                //25为状态栏高度
                wmParams.y = (int) event.getRawY() - mFloatLayout.getHeight() / 2 - StatusBarHeight;
                mWindowManager.updateViewLayout(mFloatLayout, wmParams);
                return false;
            }
        });
        //关闭本Service
        Close.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Toast.makeText(SensorService.this, "Stop sensor float window ", Toast.LENGTH_SHORT).show();
                Intent localIntent = new Intent(SensorService.this, SensorService.class);
                SensorService.this.stopService(localIntent);
            }
        });
        //更多按钮
        Show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(showrunable==false) {
                    showrunable=true;
                    Close.setVisibility(View.VISIBLE);
                    ChangeColor.setVisibility(View.VISIBLE);
                    return;
                }
                if(showrunable==true) {
                    showrunable=false;
                    Close.setVisibility(View.GONE);
                    ChangeColor.setVisibility(View.GONE);
                    return;
                }
            }
        });
        //改变背景色
        ChangeColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (backgroundcolor % 5 == 0) {
                    mFloatLayout.setBackgroundColor(0xff00F5FF);//蓝
                    backgroundcolor++;
                    return;
                }
                if (backgroundcolor % 5 == 2) {
                    mFloatLayout.setBackgroundColor(0xffffffff);//白
                    backgroundcolor++;
                    return;
                }
                if (backgroundcolor % 5 == 3) {
                    mFloatLayout.setBackgroundColor(0xffEE7600);//橙
                    backgroundcolor++;
                    return;
                }
                if (backgroundcolor % 5 == 4) {
                    mFloatLayout.setBackgroundColor(0xffCD0000);//红
                    backgroundcolor++;
                    return;
                }
                if (backgroundcolor % 5 == 1) {
                    mFloatLayout.setBackgroundColor(0x00ffffff);//透明
                    backgroundcolor++;
                    return;
                }
            }
        });
        //双击变颜色
        mFloatLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Timer tExit = null;
                if (ischange[0] == 0) {
                    ischange[0] = 1; // 准备
                    tExit = new Timer();
                    tExit.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            ischange[0] = 0; // 取消
                        }
                    }, 200); // 如果X秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务
                } else {
                    if (ischange[1] % 5 == 1) {
                        Updatecolor(0xff00F5FF);//蓝
                        ischange[1]++;
                        return;
                    }
                    if (ischange[1] % 5 == 2) {
                        Updatecolor(0xffffffff);//白
                        ischange[1]++;
                        return;
                    }
                    if (ischange[1] % 5 == 3) {
                        Updatecolor(0xffEE7600);//橙
                        ischange[1]++;
                        return;
                    }
                    if (ischange[1] % 5 == 4) {
                        Updatecolor(0xffCD0000);//红
                        ischange[1]++;
                        return;
                    }
                    if (ischange[1] % 5 == 0) {
                        Updatecolor(0xff000000);//黑
                        ischange[1]++;
                        return;
                    }
                }
            }
        });
    }
    //改变颜色
    public void Updatecolor(int color){
        //   Log.e("ThenHelper"," "+color);
        SensorName.setTextColor(color);
        SensorDataX.setTextColor(color);
        SensorDataY.setTextColor(color);
        SensorDataZ.setTextColor(color);
        Close.setTextColor(color);
        Show.setTextColor(color);
        ChangeColor.setTextColor(color);
        OtherInfo.setTextColor(color);
    }
    //UI更新
    private Runnable GuiUpdater = new Runnable()
    {
        public void run(){
        SensorDataX.setText("X : " + df.format(data[0]));
        SensorDataY.setText("Y : " + df.format(data[1]));
        SensorDataZ.setText("Z : " + df.format(data[2]));
        //气压得海拔
        if(item.getType()==6){
            OtherInfo.setText("Other Info :\n" +
                            "Altitude=" + sensorOtherInfo.pressureToAltitude(data[0]) + "m"
            );
        }
        //方向得东西南北
        if(item.getType()==3){
            float direction = data[0] * -1.0f;
            sensorOtherInfo.setmTargetDirection(direction);
            OtherInfo.setText("Other Info :\n"+
                            "Compass="+sensorOtherInfo.orientationToWhere(sensorOtherInfo.normalizeDegree(direction))
            );
        }
        //亮度值
        if(item.getType()==5){
                OtherInfo.setText("Other Info :\n"+
                        "Brightness="+sensorOtherInfo.getexistValue("/sys/class/leds/lcd-backlight/brightness"));
            }
        }
    };
    //监听Sensor
    public void monitorsensor(){
        sm=(SensorManager)this.getSystemService(Context.SENSOR_SERVICE);
        item=sm.getDefaultSensor(sensortype);
        if(item.getType()==5){
            sensorOtherInfo=new SensorOtherInfo();
            OtherInfo.setVisibility(View.VISIBLE);
        }
        if(item.getType()==6){
            sensorOtherInfo=new SensorOtherInfo();
            OtherInfo.setVisibility(View.VISIBLE);
        }
        if(item.getType()==3){
            sensorOtherInfo=new SensorOtherInfo();
            OtherInfo.setVisibility(View.VISIBLE);
            sensorOtherInfo.initResources(compass_pointer);
            compass_pointer.setVisibility(View.VISIBLE);
        }
          mySensorListener = new SensorEventListener(){
            //复写onSensorChanged方法
            public void onSensorChanged(SensorEvent sensorEvent){
                if(sensorEvent.sensor.getType() == item.getType()){
                    data[0] = sensorEvent.values[0];
                    data[1] = sensorEvent.values[1];
                    data[2] = sensorEvent.values[2];

                }
            }
            //复写onAccuracyChanged方法
            public void onAccuracyChanged(Sensor sensor , int accuracy){
                Log.e("ThenHelper", "onAccuracyChanged "+sensorname);
            }
        };
        sm.registerListener(mySensorListener, sm.getDefaultSensor(item.getType()), SensorManager.SENSOR_DELAY_NORMAL);
        Log.e("ThenHelper", "registerListener " + sensorname);
    }

    /**
     * 用于获取状态栏的高度。
     *
     * @return 返回状态栏高度的像素值。
     */
    private int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        Log.e("ThenHelper", "status_bar_height= "+result);
        return result;
    }
    @Override
    public void onDestroy()
    {
        // TODO Auto-generated method stub
        super.onDestroy();
        if(mFloatLayout != null)
        {
            //移除悬浮窗口
            mWindowManager.removeView(mFloatLayout);
        }
        if (sm != null) {
            sm.unregisterListener(mySensorListener);
            sm = null;
            Log.e("ThenHelper", "End SensorService and unregisterListener "+sensorname);
        }
    }

}