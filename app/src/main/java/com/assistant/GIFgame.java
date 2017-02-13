package com.assistant;

/**
 * Created by admin on 2015/9/7.
 */

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ant.liao.GifView;
import com.main.thenhelper.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;


public class GIFgame extends Service
{

    //定义浮动窗口布局
    RelativeLayout mFloatLayout;
    WindowManager.LayoutParams wmParams;
    //创建浮动窗口设置布局参数的对象
    WindowManager mWindowManager;

    Button Close,Show,ChangeColor;

    DecimalFormat df  = new DecimalFormat("######0.000");
    private boolean showrunable=false;
    private GifView gif1,gif2,gif3,gif4,gif5,gif6;
    private ImageView gif1num,gif2num,gif3num,gif4num,gif5num,gif6num;
    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);

        Log.e("ThenHelper", "  Service onstart GIFgame");
        createFloatView();
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
        Log.e("ThenHelper", "Create GIFgame Float Windows first");
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
        mFloatLayout = (RelativeLayout) inflater.inflate(R.layout.gif_game, null);
        //添加mFloatLayout
        mWindowManager.addView(mFloatLayout, wmParams);
        //浮动窗口按钮
        ChangeColor = (Button)mFloatLayout.findViewById(R.id.ChangeColor);
        Show = (Button)mFloatLayout.findViewById(R.id.Show);
        Close = (Button)mFloatLayout.findViewById(R.id.Close);
        Close.setBackgroundColor(0x00000000);
        Show.setBackgroundColor(0x00000000);
        ChangeColor.setBackgroundColor(0x00000000);
        Close.setVisibility(View.GONE);
        ChangeColor.setVisibility(View.GONE);
       // mFloatLayout.setBackgroundColor(0xffffffff);

        mFloatLayout.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec
                .makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        //绑定触摸移动监听
        final int hight=getStatusBarHeight();
        mFloatLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                wmParams.x = (int) event.getRawX() - mFloatLayout.getWidth() / 2;
                //25为状态栏高度
                wmParams.y = (int) event.getRawY() - mFloatLayout.getHeight() / 2 - hight;
                mWindowManager.updateViewLayout(mFloatLayout, wmParams);
                return false;
            }
        });
        //更多按钮
        Show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (showrunable == false) {
                    showrunable = true;
                    Close.setVisibility(View.VISIBLE);
                    ChangeColor.setVisibility(View.VISIBLE);
                    return;
                }
                if (showrunable == true) {
                    showrunable = false;
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
                if (backgroundcolor[0] % 5 == 1) {
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
                if (backgroundcolor[0] % 5 == 0) {
                    mFloatLayout.setBackgroundColor(0x00ffffff);//黑
                    backgroundcolor[0]++;
                    return;
                }
            }
        });
        //关闭本Service
        Close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Toast.makeText(GIFgame.this, "Stop game float window ", Toast.LENGTH_SHORT).show();
                Intent localIntent = new Intent(GIFgame.this, GIFgame.class);
                GIFgame.this.stopService(localIntent);
                Log.e("ThenHelper", "GIFgame float windows close");
            }
        });
        //设置数据
//        gif1num=(ImageView)mFloatLayout.findViewById(R.id.gif1num);
//        gif2num=(ImageView)mFloatLayout.findViewById(R.id.gif2num);
//        gif3num=(ImageView)mFloatLayout.findViewById(R.id.gif3num);
//        gif4num=(ImageView)mFloatLayout.findViewById(R.id.gif4num);
//        gif5num=(ImageView)mFloatLayout.findViewById(R.id.gif5num);
//        gif6num=(ImageView)mFloatLayout.findViewById(R.id.gif6num);
//        gif1num.setVisibility(View.INVISIBLE);
//        gif2num.setVisibility(View.INVISIBLE);
//        gif3num.setVisibility(View.INVISIBLE);
//        gif4num.setVisibility(View.INVISIBLE);
//        gif5num.setVisibility(View.INVISIBLE);
//        gif6num.setVisibility(View.INVISIBLE);
        //设置GIF图片
        gif1=(GifView)mFloatLayout.findViewById(R.id.gif1);
        gif2=(GifView)mFloatLayout.findViewById(R.id.gif2);
        gif3=(GifView)mFloatLayout.findViewById(R.id.gif3);
        gif4=(GifView)mFloatLayout.findViewById(R.id.gif4);
        gif5=(GifView)mFloatLayout.findViewById(R.id.gif5);
        gif6=(GifView)mFloatLayout.findViewById(R.id.gif6);
        // 设置Gif图片源
        gif1.setGifImage(R.drawable.maomi1);
        gif2.setGifImage(R.drawable.maomi2);
        gif3.setGifImage(R.drawable.maomi3);
        gif4.setGifImage(R.drawable.maomi4);
        gif5.setGifImage(R.drawable.maomi5);
        gif6.setGifImage(R.drawable.maomi6);
        // 设置显示的大小，拉伸或者压缩
        gif1.setShowDimension(240, 240);
        gif2.setShowDimension(240, 240);
        gif3.setShowDimension(240, 240);
        gif4.setShowDimension(240, 240);
        gif5.setShowDimension(240, 240);
        gif6.setShowDimension(240, 240);
        // 设置加载方式：先加载后显示、边加载边显示、只显示第一帧再显示
        gif1.setGifImageType(GifView.GifImageType.COVER);
        gif2.setGifImageType(GifView.GifImageType.COVER);
        gif3.setGifImageType(GifView.GifImageType.COVER);
        gif4.setGifImageType(GifView.GifImageType.COVER);
        gif5.setGifImageType(GifView.GifImageType.COVER);
        gif6.setGifImageType(GifView.GifImageType.COVER);
        final int[] num=getrandom(6,7);

        // 添加监听器
        gif1.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Log.e("ThenHelper", "tap gif1");
              //  gif1num.setImageResource(getnumimage(num[0]));
               // gif1.setVisibility(View.GONE);
                gif1.setGifImage(getnumimage(num[0]));
            //    gif1num.setVisibility(View.VISIBLE);
                return false;
            }
        });
        gif2.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Log.e("ThenHelper", "tap gif2");
                gif2.setGifImage(getnumimage(num[1]));
              //  gif2num.setImageResource(getnumimage(num[1]));
            //    gif2.setVisibility(View.GONE);
            //    gif2num.setVisibility(View.VISIBLE);
                return false;
            }
        });
        gif3.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Log.e("ThenHelper", "tap gif3");
                gif3.setGifImage(getnumimage(num[2]));
              //  gif3num.setImageResource(getnumimage(num[2]));
               // gif3.setVisibility(View.GONE);
              //  gif3num.setVisibility(View.VISIBLE);
                return false;
            }
        });
        gif4.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Log.e("ThenHelper", "tap gif4");
                gif4.setGifImage(getnumimage(num[3]));
//                gif4num.setImageResource(getnumimage(num[3]));
//                gif4.setVisibility(View.GONE);
//                gif4num.setVisibility(View.VISIBLE);
                return false;
            }
        });
        gif5.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Log.e("ThenHelper", "tap gif5");
                gif5.setGifImage(getnumimage(num[4]));
//                gif5num.setImageResource(getnumimage(num[4]));
//                gif5.setVisibility(View.GONE);
//                gif5num.setVisibility(View.VISIBLE);
                return false;
            }
        });
        gif6.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Log.e("ThenHelper", "tap gif6");
                gif6.setGifImage(getnumimage(num[5]));
//                gif6num.setImageResource(getnumimage(num[5]));
//                gif6.setVisibility(View.GONE);
//                gif6num.setVisibility(View.VISIBLE);
                return false;
            }
        });
    }
    //对应数字图片
    public int getnumimage(int i){
        int num=0;
        switch (i){
            case 1:
                num= R.drawable.num1;
            break;
            case 2:
                num=  R.drawable.num2;
            break;
            case 3:
                num=  R.drawable.num3;
            break;
            case 4:
                num=  R.drawable.num4;
            break;
            case 5:
                num=  R.drawable.num5;
            break;
            case 6:
                num=  R.drawable.num6;
            break;
        }
        return num;
    }
    //产生随机数，num是个数，max是最大允许的范围
        public int[] getrandom(int num ,int max) {
            ArrayList<Integer> list = new ArrayList<Integer>();
            Random rand = new Random();
            while(true){
                int rm = rand.nextInt(max);
                if(!list.contains(rm)&&rm!=0){
                    list.add(rm);
                    if(list.size()>=num)break;
                }
            }
            int result[] = new int[num];
            for(int i = 0;i<list.size();i++){
                result[i] = list.get(i);
             //   Log.e("ThenHelper",  result[i]+"");
            }
            return result;
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
            gif1=null;
            gif2=null;
            gif3=null;
            gif4=null;
            gif5=null;
            gif6=null;
//            gif1num=null;
//            gif2num=null;
//            gif3num=null;
//            gif4num=null;
//            gif5num=null;
//            gif6num=null;

            //移除悬浮窗口
            mWindowManager.removeView(mFloatLayout);
        }
    }

}
