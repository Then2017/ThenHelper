package com.sensor;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.main.thenhelper.MainActivity;
import com.main.thenhelper.R;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DecimalFormat;

/**
 * Created by theny550 on 2015/9/19.
 */
public class SensorOtherInfo {
    DecimalFormat df  = new DecimalFormat("######0.00");
    //方向传感器计算
    // 调整方向传感器获取的值
    public float normalizeDegree(float degree) {
        return (degree + 720) % 360;
    }
    public String orientationToWhere(float mTargetDirection){
        StringBuffer str=new StringBuffer("");
        // 下面是根据mTargetDirection，作方向名称图片的处理
        float direction = normalizeDegree(mTargetDirection * -1.0f);
        str.append((int)direction+"° ");
        if (direction > 112.5f && direction < 247.5f) {
            // south
            str.append("S");
        } else if (direction < 67.5 || direction > 292.5f) {
            // north
            str.append("N");
        }
        if (direction > 22.5f && direction < 157.5f) {
            // east
            str.append("E");
        } else if (direction > 202.5f && direction < 337.5f) {
            // west
            str.append("W");
        }
        if (direction > 22.5f && direction < 157.5f) {
            // east
            str.append("/东");
        } else if (direction > 202.5f && direction < 337.5f) {
            // west
            str.append("/西");
        }
        if (direction > 112.5f && direction < 247.5f) {
            // south
            str.append("南");
        } else if (direction < 67.5 || direction > 292.5f) {
            // north
            str.append("北");
        }
        return str.toString();
    }
    //气压传感器计算
    //DecimalFormat dfpressure = new DecimalFormat("0.00");
    public String pressureToAltitude(float values){
        float sPV = values;
        // df.getRoundingMode();
        // 计算海拔
        double height = 44330000*(1-(Math.pow((Double.parseDouble(df.format(sPV))/1013.25), (float)1.0/5255.0)));
        return df.format(height);
    }












    private final float MAX_ROATE_DEGREE = 1.0f;// 最多旋转一周，即360°
    private float mDirection;// 当前浮点方向
    private float mTargetDirection;// 目标浮点方向
    private AccelerateInterpolator mInterpolator;// 动画从开始到结束，变化率是一个加速的过程,就是一个动画速率
    protected final Handler mHandler = new Handler();
    private boolean mStopDrawing;// 是否停止指南针旋转的标志位
    CompassView mPointer;// 指南针view
    public void setmTargetDirection(float mT){
        this.mTargetDirection=mT;
    }
    // 初始化view
    public void initResources(CompassView compassView ) {
        mDirection = 0.0f;// 初始化起始方向
        mTargetDirection = 0.0f;// 初始化目标方向
        mInterpolator = new AccelerateInterpolator();// 实例化加速动画对象
        mStopDrawing = false;
      //  mCompassView = rl.findViewById(R.id.view_compass);// 实际上是一个LinearLayout，装指南针ImageView和位置TextView
        mPointer =compassView;// 自定义的指南针view
        mHandler.postDelayed(mCompassViewUpdater, 20);// 20毫秒执行一次更新指南针图片旋转
    }
    // 这个是更新指南针旋转的线程，handler的灵活使用，每20毫秒检测方向变化值，对应更新指南针旋转
    protected Runnable mCompassViewUpdater = new Runnable() {
        @Override
        public void run() {
            if (mPointer != null && !mStopDrawing) {
                if (mDirection != mTargetDirection) {

                    // calculate the short routine
                    float to = mTargetDirection;
                    if (to - mDirection > 180) {
                        to -= 360;
                    } else if (to - mDirection < -180) {
                        to += 360;
                    }
                    // limit the max speed to MAX_ROTATE_DEGREE
                    float distance = to - mDirection;
                    if (Math.abs(distance) > MAX_ROATE_DEGREE) {
                        distance = distance > 0 ? MAX_ROATE_DEGREE
                                : (-1.0f * MAX_ROATE_DEGREE);
                    }

                    // need to slow down if the distance is short
                    mDirection = normalizeDegree(mDirection
                            + ((to - mDirection) * mInterpolator
                            .getInterpolation(Math.abs(distance) > MAX_ROATE_DEGREE ? 0.4f
                                    : 0.3f)));// 用了一个加速动画去旋转图片，很细致
                    mPointer.updateDirection(mDirection);// 更新指南针旋转
                }
                mHandler.postDelayed(mCompassViewUpdater, 20);// 20毫米后重新执行自己，比定时器好
            }
        }
    };

    //读取值
    public   String getexistValue(String key)   {
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




