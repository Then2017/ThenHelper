package com.screenrecord;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TableLayout;

import android.widget.TextView;
import android.widget.Toast;

import com.main.thenhelper.MainActivity;
import com.main.thenhelper.R;


import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Then on 2015/9/27.
 */
public class RecordFloatService extends Service  {

    //定义浮动窗口布局
    RelativeLayout mFloatLayout;
    WindowManager.LayoutParams wmParams;
    //创建浮动窗口设置布局参数的对象
    WindowManager mWindowManager;

    Button Close,ChangeColor,Draw,Start,Show;
    TextView ScreenRecordtitle;
    private boolean showrunable=false;
    int backgroundcolor = 1;
    int[] ischange = {0,1};
    TableLayout Moretable;
    boolean startrunable;


    private void createFloatView()
    {
        wmParams = new WindowManager.LayoutParams();
        //获取的是WindowManagerImpl.CompatModeWrapper
        mWindowManager = (WindowManager)getApplication().getSystemService(getApplication().WINDOW_SERVICE);
        Log.e("ThenHelper", "Create Screen Record Windows first" );
        //设置window type
        wmParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        //设置图片格式，效果为背景透明
        wmParams.format = PixelFormat.TRANSLUCENT;
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
        mFloatLayout = (RelativeLayout) inflater.inflate(R.layout.screenrecord_float, null);
        //添加mFloatLayout
        mWindowManager.addView(mFloatLayout, wmParams);
        //浮动窗口按钮
        ChangeColor = (Button)mFloatLayout.findViewById(R.id.ChangeColor);
        Show = (Button)mFloatLayout.findViewById(R.id.Show);
        Close = (Button)mFloatLayout.findViewById(R.id.Close);
        Draw = (Button)mFloatLayout.findViewById(R.id.Draw);
        Start=(Button)mFloatLayout.findViewById(R.id.Start);
        Moretable=(TableLayout)mFloatLayout.findViewById(R.id.Moretable);
        ScreenRecordtitle=(TextView)mFloatLayout.findViewById(R.id.ScreenRecordtitle);

        Draw.setBackgroundColor(0x00000000);
        Start.setBackgroundColor(0x00000000);
        Close.setBackgroundColor(0x00000000);
        Show.setBackgroundColor(0x00000000);
        ChangeColor.setBackgroundColor(0x00000000);
//        Close.setVisibility(View.GONE);
//        ChangeColor.setVisibility(View.GONE);
//        Draw.setVisibility(View.GONE);
        Moretable.setVisibility(View.GONE);

        mFloatLayout.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec
                .makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        //绑定触摸移动监听
        final int StatusBarHeight=getStatusBarHeight();
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

        //Draw
        Draw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Show.performClick();
                Intent localIntent = new Intent(MainActivity.mContext, DoodleService.class);
                MainActivity.mContext.stopService(localIntent);
                MainActivity.mContext.startService(localIntent);
            }
        });

        //关闭本Service
        Close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Toast.makeText(RecordFloatService.this, "Stop screen record float window ", Toast.LENGTH_SHORT).show();
                Intent localIntent = new Intent(RecordFloatService.this, RecordFloatService.class);
                RecordFloatService.this.stopService(localIntent);
            }
        });
        //更多按钮
        Show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(showrunable==false) {
                    showrunable=true;
//                    Close.setVisibility(View.VISIBLE);
//                    ChangeColor.setVisibility(View.VISIBLE);
//                    Draw.setVisibility(View.VISIBLE);
                    Moretable.setVisibility(View.VISIBLE);
                    return;
                }
                if(showrunable==true) {
                    showrunable=false;
//                    Close.setVisibility(View.GONE);
//                    ChangeColor.setVisibility(View.GONE);
//                    Draw.setVisibility(View.GONE);
                    Moretable.setVisibility(View.GONE);
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
        //开始按钮
        Start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(startrunable==false){
                    startrunable=true;
                    Start.setText("Stop");
                   // MainActivity.StartRecord.performClick();
                    Intent localIntent = new Intent(MainActivity.mContext, ScreenRecordService.class);
                    MainActivity.mContext.stopService(localIntent);
                    MainActivity.mContext.startService(localIntent);
                    return;
                }
                if(startrunable==true){
                    startrunable=false;
                    Start.setText("Done");
                    Start.setEnabled(false);
                    MainActivity.StartRecord.setEnabled(true);
                    MainActivity.recorddelete.setEnabled(true);
                    Intent localIntent = new Intent(MainActivity.mContext,ScreenRecordService.class);
                    MainActivity.mContext.stopService(localIntent);
                    return;
                }
            }
        });
    }
    private int dip2px(float dpValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    //改变颜色
    public void Updatecolor(int color){
        //   Log.e("ThenHelper"," "+color);
        Close.setTextColor(color);
        Show.setTextColor(color);
        ChangeColor.setTextColor(color);
        Draw.setTextColor(color);
        Start.setTextColor(color);
        ScreenRecordtitle.setTextColor(color);
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
        Log.e("ThenHelper", "status_bar_height= " + result);
        return result;
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        Log.e("ThenHelper", "RecordFloatService onCreate");
        startrunable=false;
        createFloatView();

    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy()
    {
        // TODO Auto-generated method stub
        super.onDestroy();
        Intent ScreenRecordServicelocalIntent = new Intent(MainActivity.mContext,ScreenRecordService.class);
        MainActivity.mContext.stopService(ScreenRecordServicelocalIntent);
        if(mFloatLayout != null)
        {
            //移除悬浮窗口
            mWindowManager.removeView(mFloatLayout);
        }
        if(MainActivity.mediaProjection!=null){
            MainActivity.mediaProjection.stop();
            MainActivity.mediaProjection=null;
        }
        startrunable=false;
        MainActivity.StartRecord.setEnabled(true);
        MainActivity.recorddelete.setEnabled(true);
        Log.e("ThenHelper", "End RecordFloatService and stop record ");
    }
}
