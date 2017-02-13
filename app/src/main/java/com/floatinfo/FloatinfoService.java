package com.floatinfo;


import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.main.thenhelper.MainActivity;
import com.main.thenhelper.R;
import com.screenrecord.DoodleService;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class FloatinfoService extends Service
{

    //定义浮动窗口布局
    RelativeLayout mFloatLayout;
    WindowManager.LayoutParams wmParams;
    //创建浮动窗口设置布局参数的对象
    WindowManager mWindowManager;

    Button Start,Close,Show,ChangeColor;
    public  TextView Brightness,Current,PackageName,floatinfotitle,SIMSignal,BTSignal,WIFISignal,Memory,Doodle,CPU;
    String[] floatinfostr=new String[]{},infostr=new String[]{};
    long Currenttotal=0,avgCurrent=0,Currentcount=0;
    boolean timerrunable=false,showrunable=false;
    int chargeflag=0,cpunum=0;

    Context mcontext=this;
    public SIMSignal simSignal=new SIMSignal(mcontext);
    public MemoryInfo memoryInfo=new MemoryInfo(mcontext);
    public  CPUInfo cpuInfo=new CPUInfo();

    Thread infoThread;
    boolean infoThreadrunable=false;

    public  FloatinfoMain floatinfoMain=new FloatinfoMain(mcontext);


    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        Bundle bundle = intent.getExtras();
        floatinfostr = bundle.getStringArray("floatinfo");

        Log.e("ThenHelper", "  Service onstart : floatinfo ");
        createFloatView();
        visibleTextView();
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();

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
        Log.e("ThenHelper", "Create FloatInfo Float Window first");
        //设置window type
        wmParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        //设置图片格式
       // wmParams.format = PixelFormat.OPAQUE;
        wmParams.format = PixelFormat.TRANSPARENT;
        //设置浮动窗口不可聚焦（实现操作除浮动窗口外的其他可见窗口的操作）
        wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
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
        mFloatLayout = (RelativeLayout) inflater.inflate(R.layout.floatinfo_float, null);
        //添加mFloatLayout
        mWindowManager.addView(mFloatLayout, wmParams);
        //浮动窗口按钮
        Close = (Button)mFloatLayout.findViewById(R.id.Close);
        Start = (Button)mFloatLayout.findViewById(R.id.Start);
        Show= (Button)mFloatLayout.findViewById(R.id.Show);
        ChangeColor=(Button)mFloatLayout.findViewById(R.id.ChangeColor);
        floatinfotitle=(TextView)mFloatLayout.findViewById(R.id.floatinfotitle);
        Brightness= (TextView)mFloatLayout.findViewById(R.id.Brightness);
        Current = (TextView)mFloatLayout.findViewById(R.id.Current);
        PackageName = (TextView)mFloatLayout.findViewById(R.id.PackageName);
        SIMSignal=(TextView)mFloatLayout.findViewById(R.id.SIMSignal);
        WIFISignal=(TextView)mFloatLayout.findViewById(R.id.WIFISignal);
        BTSignal=(TextView)mFloatLayout.findViewById(R.id.BTSignal);
        Memory=(TextView)mFloatLayout.findViewById(R.id.Memory);
        Doodle=(TextView)mFloatLayout.findViewById(R.id.Doodle);
        CPU=(TextView)mFloatLayout.findViewById(R.id.CPU);

        CPU.setVisibility(View.GONE);
        ChangeColor.setVisibility(View.GONE);
        Start.setVisibility(View.GONE);
        Close.setVisibility(View.GONE);
        Memory.setVisibility(View.GONE);
        WIFISignal.setVisibility(View.GONE);
        BTSignal.setVisibility(View.GONE);
        SIMSignal.setVisibility(View.GONE);
        Brightness.setVisibility(View.GONE);
        Current.setVisibility(View.GONE);
        PackageName.setVisibility(View.GONE);
        Doodle.setVisibility(View.GONE);

        ChangeColor.setTextColor(Color.BLACK);
        ChangeColor.setBackgroundColor(0x00000000);
        Show.setTextColor(Color.BLACK);
        Show.setBackgroundColor(0x00000000);
        Close.setTextColor(Color.BLACK);
        Close.setBackgroundColor(0x00000000);
        Start.setTextColor(Color.BLACK);
        Start.setBackgroundColor(0x00000000);
        final int StatusBarHeight=getStatusBarHeight();
        mFloatLayout.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec
                .makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        //绑定触摸移动监听
        mFloatLayout.setOnTouchListener(new View.OnTouchListener() {
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
        //双击变颜色
        final int[] ischange = {0,1};
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
        //更多按钮
        Show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(showrunable==false) {
                    showrunable=true;
                    Start.setVisibility(View.VISIBLE);
                    Close.setVisibility(View.VISIBLE);
                    ChangeColor.setVisibility(View.VISIBLE);
                    return;
                }
                if(showrunable==true) {
                    showrunable=false;
                    Start.setVisibility(View.GONE);
                    Close.setVisibility(View.GONE);
                    ChangeColor.setVisibility(View.GONE);
                    return;
                }
            }
        });
        //改变背景色
        final int[] backgroundcolor = {1};
        ChangeColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (backgroundcolor[0] % 5 == 0) {
                    mFloatLayout.setBackgroundColor(0xff00F5FF);//蓝
                    backgroundcolor[0]++;
                    return;
                }
                if (backgroundcolor[0] % 5 == 2) {
                    mFloatLayout.setBackgroundColor(0xffffffff);//白
                    backgroundcolor[0]++;
                    return;
                }
                if (backgroundcolor[0] % 5 == 3) {
                    mFloatLayout.setBackgroundColor(0xffEE7600);//橙
                    backgroundcolor[0]++;
                    return;
                }
                if (backgroundcolor[0] % 5 == 4) {
                    mFloatLayout.setBackgroundColor(0xffCD0000);//红
                    backgroundcolor[0]++;
                    return;
                }
                if (backgroundcolor[0] % 5 == 1) {
                    mFloatLayout.setBackgroundColor(0x00ffffff);//透明
                    backgroundcolor[0]++;
                    return;
                }
            }
        });
        //开启监控
        Start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (timerrunable == false) {
                   // Current.setTextColor(floatinfotitle.getCurrentTextColor());
                    startinitwork();
                    timerrunable = true;
                    infoThreadrunable=true;
                    infoThread =new infoThread();
                    infoThread.start();
                    Start.setText("Stop");
                    Log.e("ThenHelper", "Floatinfo timer start");
                    return;
                }
                if (timerrunable == true) {
                    stopallwork();
                    if(infoThread!=null&&infoThread.isAlive()){
                        infoThreadrunable=false;
                        infoThread.interrupt();
                    }
                    timerrunable = false;
                    Start.setText("Start");
                    Log.e("ThenHelper", "Floatinfo timer stop");
                    return;
                }
            }
        });
        //关闭本Service
        Close.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // TODO Auto-generated method stub
                Toast.makeText(FloatinfoService.this, "Stop Floatinfo float window ", Toast.LENGTH_SHORT).show();
                Intent localIntent = new Intent(FloatinfoService.this, FloatinfoService.class);
                FloatinfoService.this.stopService(localIntent);
                Log.e("ThenHelper", "Floatinfo timer close");
            }
        });
    }
    //启动各组件
    public void startinitwork(){
        if(floatinfostr[0].equals("1")) {
        }
        if(floatinfostr[1].equals("1")) {
            Currenttotal=0;
            avgCurrent=0;
            Currentcount=0;
            chargeflag=0;
            Current.setText("Electric current : --mA  Avg : --mA\n");
        }
        if(floatinfostr[2].equals("1")) {
        }
        if(floatinfostr[3].equals("1")) {
            simSignal.getSIMsignal();
        }
        if(floatinfostr[4].equals("1")) {
        }
        if(floatinfostr[5].equals("1")) {
            floatinfoMain.registeBT();
            floatinfoMain.startBT();
        }
        if(floatinfostr[6].equals("1")) {
            memoryInfo.startCPU();
        }
        if(floatinfostr[7].equals("1")) {
            Show.performClick();
            Intent localIntent = new Intent(MainActivity.mContext, DoodleService.class);
            MainActivity.mContext.stopService(localIntent);
            MainActivity.mContext.startService(localIntent);
        }
        if(floatinfostr[8].equals("1")) {
            cpunum=cpuInfo.getCpunum();
        }
    }
    //关闭各组件
    public void stopallwork(){
        if(floatinfostr[0].equals("1")) {
        }
        if(floatinfostr[1].equals("1")) {
            Currenttotal=0;
            avgCurrent=0;
            Currentcount=0;
            chargeflag=0;
        }
        if(floatinfostr[2].equals("1")) {
        }
        if(floatinfostr[3].equals("1")) {
            if(simSignal.telephoneManager!=null) {
                simSignal.Canclelisten();
            }
        }
        if(floatinfostr[4].equals("1")) {
        }
        if(floatinfostr[5].equals("1")) {
            if(floatinfoMain.btReceiver!=null) {
                floatinfoMain.unregisteBT();
            }
            floatinfoMain.cancelBT();
        }
        if(floatinfostr[6].equals("1")) {
            memoryInfo.stopCPU();
        }
        if(floatinfostr[7].equals("1")) {
            Doodle.setText("Doodle Stop");
        }
        if(floatinfostr[8].equals("1")) {
        }
    }
    //显示勾选控件
    public void visibleTextView(){
        if(floatinfostr[0].equals("1")) {
            Brightness.setVisibility(View.VISIBLE);
        }
        if(floatinfostr[1].equals("1")) {
            Current.setVisibility(View.VISIBLE);
        }
        if(floatinfostr[2].equals("1")) {
            PackageName.setVisibility(View.VISIBLE);
        }
        if (floatinfostr[3].equals("1")) {
            SIMSignal.setVisibility(View.VISIBLE);
        }
        if(floatinfostr[4].equals("1")) {
            WIFISignal.setVisibility(View.VISIBLE);
        }
        if(floatinfostr[5].equals("1")) {
            BTSignal.setVisibility(View.VISIBLE);
        }
        if(floatinfostr[6].equals("1")) {
            Memory.setVisibility(View.VISIBLE);
        }
        if(floatinfostr[7].equals("1")) {
            Doodle.setVisibility(View.VISIBLE);
        }
        if(floatinfostr[8].equals("1")) {
            CPU.setVisibility(View.VISIBLE);
        }
    }
    //改变颜色
    public void Updatecolor(int color){
     //   Log.e("ThenHelper"," "+color);
        Brightness.setTextColor(color);
        Current.setTextColor(color);
        PackageName.setTextColor(color);
        floatinfotitle.setTextColor(color);
        Start.setTextColor(color);
        Close.setTextColor(color);
        Show.setTextColor(color);
        SIMSignal.setTextColor(color);
        BTSignal.setTextColor(color);
        WIFISignal.setTextColor(color);
        Memory.setTextColor(color);
        ChangeColor.setTextColor(color);
        Doodle.setTextColor(color);
        CPU.setTextColor(color);
    }
    //获取数据
    public String [] getalldata(){
        String[] str=new String[10];
        //亮度
        if(floatinfostr[0].equals("1")) {
            try {
                str[0]="Bright : "+floatinfoMain.getexistValue(FloatinfoConstant.info_brightness);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //电流
        if(floatinfostr[1].equals("1")) {
            try {
                int c=Integer.parseInt(floatinfoMain.getexistValue(FloatinfoConstant.info_current))/1000;
                Currentcount++;
                Currenttotal=c+Currenttotal;
                avgCurrent=Currenttotal/Currentcount;
                if(floatinfoMain.getexistValue(FloatinfoConstant.info_status).equals("Charging")
                        ||floatinfoMain.getexistValue(FloatinfoConstant.info_status).equals("Full")){
                    chargeflag=1;
                }
                if(chargeflag==0) {
                    str[1]="Electric current : " + c + "mA  Avg : " + avgCurrent + "mA\n";
                }else {
                 //   Current.setTextColor(Color.RED);
                    str[1]="Electric current : " + c + "mA  Avg : " + avgCurrent + "mA\n" +
                            "Device has been charged after starting.\n";
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //当前程序信息
        if(floatinfostr[2].equals("1")) {
            str[2]="Current Progress :\n" +floatinfoMain.getAppPackageInfo()+"\n";
        }
        //SIM信号强度
        if(floatinfostr[3].equals("1")) {
            str[3]="SIM Signal : "+simSignal.SIMstrength+"\n";
        }
        //WIFI信号强度
        if(floatinfostr[4].equals("1")) {
            str[4]=floatinfoMain.getWIFIinfo()+"\n";
        }
        //BT信号强度
        if(floatinfostr[5].equals("1")) {
            str[5]=floatinfoMain.getBTinfo();
        }
        //内存CPU状态
        if(floatinfostr[6].equals("1")) {
            str[6]=memoryInfo.getMemoryInfoString();
        }
        //涂鸦
        if(floatinfostr[7].equals("1")) {
            str[7]="Doodle Start";
        }
        //CPU
        if(floatinfostr[8].equals("1")) {
            //str[8]=cpuInfo.getCPUHZInfo(cpunum);
            str[8]=cpuInfo.getInfo(cpunum);
        }
        return str;
    }
    //更新UI
    public void Updatealldata(){
        String[] str=infostr;
        //亮度
        if(floatinfostr[0].equals("1")) {
                Brightness.setText(str[0]);
        }
        //电流
        if(floatinfostr[1].equals("1")) {
                    Current.setText(str[1]);

        }
        //当前程序信息
        if(floatinfostr[2].equals("1")) {
            PackageName.setText(str[2]);
        }
        //SIM信号强度
        if(floatinfostr[3].equals("1")) {
            SIMSignal.setText(str[3]);
        }
        //WIFI信号强度
        if(floatinfostr[4].equals("1")) {
            WIFISignal.setText(str[4]);
        }
        //BT信号强度
        if(floatinfostr[5].equals("1")) {
            BTSignal.setText(str[5]);
        }
        //内存状态
        if(floatinfostr[6].equals("1")) {
            Memory.setText(str[6]);
        }
        //涂鸦
        if(floatinfostr[7].equals("1")) {
            Doodle.setText(str[7]);
        }
        //涂鸦
        if(floatinfostr[8].equals("1")) {
            CPU.setText(str[8]);
        }
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

    //启动线程
    public  class infoThread extends Thread {
        public void run () {
            Log.e("ThenHelper", "Start update infoThread");
            do {
                try {
                    infostr=getalldata();
                    Message msg = new Message();
                    msg.what = 1;
                    mHandler.sendMessage(msg);
                    sleep(800);
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while(infoThreadrunable);
            Log.e("ThenHelper", "Stop update infoThread");
        }
    }
    //更新UI
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    Updatealldata();
                    break;
            }
        }
    };

    @Override
    public void onDestroy()
    {
        // TODO Auto-generated method stub
        super.onDestroy();
        if(mFloatLayout != null)
        {
            //移除悬浮窗口
            if(infoThread!=null&&infoThread.isAlive()){
                infoThreadrunable=false;
                infoThread.interrupt();
            }
            stopallwork();
            timerrunable=false;
            Currenttotal=0;
            avgCurrent=0;
            Currentcount=0;
            chargeflag=0;
            if(simSignal.telephoneManager!=null) {
                simSignal.Canclelisten();
            }
            if(floatinfoMain.btReceiver!=null) {
                floatinfoMain.unregisteBT();
            }
            mWindowManager.removeView(mFloatLayout);
        }
        Log.e("ThenHelper", "End and stop Floatinfo ");

    }

}