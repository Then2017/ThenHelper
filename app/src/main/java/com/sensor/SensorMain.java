package com.sensor;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.main.thenhelper.MainActivity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by theny550 on 2015/8/16.
 */
public class SensorMain {
    //创建Sensor列表
    public void CreateView(RelativeLayout layout, final Sensor item, final String text,int id){
        //创建一个button按钮
        final CheckBox checkBox = new CheckBox(MainActivity.mContext);
        checkBox.setText(text);
        checkBox.setId(id);
        checkBox.setTextColor(Color.BLACK);
        //确定这个控件的大小和位置
        RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams( ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT );
        rlp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        if(id!=100) {
            rlp.addRule(RelativeLayout.BELOW, id + 499);
        }
        layout.addView(checkBox, rlp);
        final SensorListener sensorlistener=new SensorListener(MainActivity.mContext,item,id);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    Log.e("ThenHelper", "checkbox true " + text);
                    sensorlistener.enableSensor();
                    //  checkBox.setBackgroundColor(0xAEEEEE);
                } else {
                    Log.e("ThenHelper", "checkbox false " + text);
                    sensorlistener.disableSensor();
                    //   checkBox.setBackgroundColor(00);
                }
            }
        });

        TextView X=new TextView(MainActivity.mContext);
        X.setText("  No values");
        X.setTextColor(Color.BLACK);
        X.setId(id + 100);
        X.setVisibility(View.GONE);
        RelativeLayout.LayoutParams rlp2 = new RelativeLayout.LayoutParams( ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT );
        rlp2.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        rlp2.addRule(RelativeLayout.BELOW, id);
        layout.addView(X, rlp2);

        TextView Y=new TextView(MainActivity.mContext);
        Y.setText("  No values");
        Y.setTextColor(Color.BLACK);
        Y.setId(id + 200);
        Y.setVisibility(View.GONE);
        RelativeLayout.LayoutParams rlp3 = new RelativeLayout.LayoutParams( ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT );
        rlp3.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        rlp3.addRule(RelativeLayout.BELOW, id + 100);
        layout.addView(Y, rlp3);

        TextView Z=new TextView(MainActivity.mContext);
        Z.setText("  No values");
        Z.setTextColor(Color.BLACK);
        Z.setId(id + 300);
        Z.setVisibility(View.GONE);
        RelativeLayout.LayoutParams rlp4 = new RelativeLayout.LayoutParams( ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT );
        rlp4.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        rlp4.addRule(RelativeLayout.BELOW, id + 200);
        layout.addView(Z, rlp4);

        Button button=new Button(MainActivity.mContext,null,android.R.attr.buttonStyleSmall);
        button.setText("View");
        button.setTextSize(9);
        button.setId(id + 400);
        button.setTextColor(Color.BLACK);
        button.setBackgroundColor(0xffD7D7D6);
        button.setPadding(5,5,5,5);
        RelativeLayout.LayoutParams rlp5 = new RelativeLayout.LayoutParams( ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        rlp5.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        rlp5.addRule(RelativeLayout.ALIGN_TOP, id);
        layout.addView(button, rlp5);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("ThenHelper", "Click " + checkBox.getText() + " View");
                // 打印每个传感器信息
                StringBuilder strLog = new StringBuilder();
                strLog.append("Sensor Type : " + item.getType() + "\r\n");
                strLog.append("Sensor Name : " + item.getName() + "\r\n");
                strLog.append("Sensor Version : " + item.getVersion() + "\r\n");
                strLog.append("Sensor Vendor : " + item.getVendor() + "\r\n");
                strLog.append("Maximum Range : " + item.getMaximumRange() + "\r\n");
                strLog.append("Minimum Delay : " + item.getMinDelay() + "\r\n");
                strLog.append("Power : " + item.getPower() + "\r\n");
                strLog.append("Resolution : " + item.getResolution() + "\r\n");
                strLog.append("\r\n");

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.sensorFragement.getActivity());
                builder.setTitle(checkBox.getText());
                builder.setMessage(strLog.toString());
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                       // if (isServiceRunning(MainActivity.mContext, "com.sensor.SensorService")) {
                            Intent localIntent = new Intent(MainActivity.mContext, SensorService.class);
                            MainActivity.mContext.stopService(localIntent);
                            Log.e("ThenHelper", "SensorService is running,stop it. " );
                      //  }
                        Intent intent = new Intent(MainActivity.mContext, SensorService.class);
                        Bundle bundle = new Bundle();
                        bundle.putStringArray("sensortype", new String[]{item.getType() + ""});
                        bundle.putStringArray("sensorname", new String[]{text});
                        intent.putExtras(bundle);
                        MainActivity.mContext.startService(intent);
                        Log.e("ThenHelper", "start float windows : " +text);
                    }
                });
                builder.setNegativeButton("Cancle", null);
                builder.show();
            }
        });
        TextView Dividers = new TextView(MainActivity.mContext);
        Dividers.setText("________________________________________________________");
        Dividers.setTextColor(Color.BLACK);
        Dividers.setId(id + 500);
        RelativeLayout.LayoutParams rlp6 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        rlp6.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        rlp6.addRule(RelativeLayout.BELOW, id + 300);
        layout.addView(Dividers, rlp6);
    }

    //创建Sensor列表
    public static LinearLayout sensorlayout=null;
    private List<SensorView> sensorViewList;
    Iterator localIterator;
    public int checkallsensor(ScrollView SV){
        sensorViewList = new ArrayList();
        sensorlayout=new LinearLayout(MainActivity.mContext);
        sensorlayout.setOrientation(LinearLayout.VERTICAL);
        SensorManager sm=(SensorManager) MainActivity.mContext.getSystemService(MainActivity.mContext.SENSOR_SERVICE);
        // 获取全部传感器列表
        List<Sensor> sensors = sm.getSensorList(Sensor.TYPE_ALL);
        int i=0;
        for (Sensor item : sensors) {
            //CreateView(sensorlayout,item,sensorname, sensorID);
            SensorView localSensorView = new SensorView(MainActivity.mContext,item,sm,sensorViewList);
            sensorViewList.add(localSensorView);
            sensorlayout.addView(localSensorView);
          i++;
        }
        localIterator=sensorViewList.iterator();
        SensorViewall sva=new SensorViewall(MainActivity.mContext,sensorViewList,i);
        sensorlayout.addView(sva);
      //  RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams( ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT );
        SV.addView(sensorlayout);
        return i;
    }

    //取消所有注册
    public void exitallsensor(){
        if(localIterator!=null) {
            while (true) {
                if (!localIterator.hasNext()) {
                    return;
                }
                ((SensorView) localIterator.next()).unSubscribeToSensorEvents();
            }
        }
    }

    //判断Service是否已启动
    public static boolean isServiceRunning(Context mContext,String className) {
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
}
//
////     TYPE_ACCELEROMETER
////     加速度传感器，单位是m/s2，测量应用于设备X、Y、Z轴上的加速度
////     传感器类型值（Sensor Type）：1 (0x00000001)
//     boolean type1=sm.registerListener(mySensorListener, sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
////     TYPE_MAGNETIC_FIELD
////     磁力传感器，单位是uT(微特斯拉)，测量设备周围三个物理轴（x，y，z）的磁场
////     传感器类型值（Sensor Type）：2 (0x00000002)
//     boolean type2=sm.registerListener(mySensorListener, sm.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD), SensorManager.SENSOR_DELAY_NORMAL);
////     TYPE_ORIENTATION
////     方向传感器,测量设备围绕三个物理轴（x，y，z）的旋转角度
////     传感器类型值（Sensor Type）：3 (0x00000003)
//     boolean type3=sm.registerListener(mySensorListener, sm.getDefaultSensor(Sensor.TYPE_ORIENTATION), SensorManager.SENSOR_DELAY_NORMAL);
////     TYPE_GYROSCOPE
////     陀螺仪传感器，单位是rad/s，测量设备x、y、z三轴的角加速度
////     传感器类型值（Sensor Type）：4 (0x00000004)
//     boolean type4=sm.registerListener(mySensorListener, sm.getDefaultSensor(Sensor.TYPE_GYROSCOPE), SensorManager.SENSOR_DELAY_NORMAL);
////     TYPE_LIGHT
////     光线感应传感器，单位lx，检测周围的光线强度
////     传感器类型值（Sensor Type）：5 (0x00000005)
//     boolean type5=sm.registerListener(mySensorListener, sm.getDefaultSensor(Sensor.TYPE_LIGHT), SensorManager.SENSOR_DELAY_NORMAL);
////     TYPE_PRESSURE
////     压力传感器，单位是hPa(百帕斯卡)，返回当前环境下的压强
////     传感器类型值（Sensor Type）：6 (0x00000006)
//     boolean type6=sm.registerListener(mySensorListener, sm.getDefaultSensor(Sensor.TYPE_PRESSURE), SensorManager.SENSOR_DELAY_NORMAL);
////     TYPE_TEMPERATURE
////     温度传感器，目前已被TYPE_AMBIENT_TEMPERATURE替代
////     传感器类型值（Sensor Type）：7 (0x00000007)
//     boolean type7=sm.registerListener(mySensorListener, sm.getDefaultSensor(Sensor.TYPE_TEMPERATURE), SensorManager.SENSOR_DELAY_NORMAL);
////     TYPE_PROXIMITY
////     距离传感器，单位是cm，用来测量某个对象到屏幕的距离
////     传感器类型值（Sensor Type）：8 (0x00000008)
//     boolean type8=sm.registerListener(mySensorListener, sm.getDefaultSensor(Sensor.TYPE_PROXIMITY), SensorManager.SENSOR_DELAY_NORMAL);
////     TYPE_GRAVITY
////     重力传感器，单位是m/s2，测量应用于设备X、Y、Z轴上的重力
////     传感器类型值（Sensor Type）：9 (0x00000009)
//     boolean type9=sm.registerListener(mySensorListener, sm.getDefaultSensor(Sensor.TYPE_GRAVITY), SensorManager.SENSOR_DELAY_NORMAL);
////     TYPE_LINEAR_ACCELERATION
////     线性加速度传感器，单位是m/s2，该传感器是获取加速度传感器去除重力的影响得到的数据
////     传感器类型值（Sensor Type）：10 (0x0000000a)
//     boolean type10=sm.registerListener(mySensorListener, sm.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION), SensorManager.SENSOR_DELAY_NORMAL);
////     TYPE_ROTATION_VECTOR
////     旋转矢量传感器，旋转矢量代表设备的方向
////     传感器类型值（Sensor Type）：11 (0x0000000b)
//     boolean type11=sm.registerListener(mySensorListener, sm.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR), SensorManager.SENSOR_DELAY_NORMAL);
////     TYPE_RELATIVE_HUMIDITY
////     湿度传感器，单位是%，来测量周围环境的相对湿度
////     传感器类型值（Sensor Type）：12 (0x0000000c)
//     boolean type12=sm.registerListener(mySensorListener, sm.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY), SensorManager.SENSOR_DELAY_NORMAL);
////     TYPE_AMBIENT_TEMPERATURE
////     温度传感器，单位是℃
////     传感器类型值（Sensor Type）： 13 (0x0000000d)
//     boolean type13=sm.registerListener(mySensorListener, sm.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE), SensorManager.SENSOR_DELAY_NORMAL);
////     TYPE_MAGNETIC_FIELD_UNCALIBRATED
////     未校准磁力传感器，提供原始的，未校准的磁场数据
////     传感器类型值（Sensor Type）：14 (0x0000000e)
//     boolean type14=sm.registerListener(mySensorListener, sm.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD_UNCALIBRATED), SensorManager.SENSOR_DELAY_NORMAL);
////     TYPE_GAME_ROTATION_VECTOR
////     游戏动作传感器，不收电磁干扰影响
////     传感器类型值（Sensor Type）：15 (0x0000000f)
//     boolean type15=sm.registerListener(mySensorListener, sm.getDefaultSensor(Sensor.TYPE_GAME_ROTATION_VECTOR), SensorManager.SENSOR_DELAY_NORMAL);
////     TYPE_GYROSCOPE_UNCALIBRATED
////     未校准陀螺仪传感器，提供原始的，未校准、补偿的陀螺仪数据，用于后期处理和融合定位数据
////     传感器类型值（Sensor Type）：16 (0x00000010)
//     boolean type16=sm.registerListener(mySensorListener, sm.getDefaultSensor(Sensor.TYPE_GYROSCOPE_UNCALIBRATED), SensorManager.SENSOR_DELAY_NORMAL);
////     TYPE_SIGNIFICANT_MOTION
////             特殊动作触发传感器
////     传感器类型值（Sensor Type）：17 (0x00000011)
//     boolean type17=sm.registerListener(mySensorListener, sm.getDefaultSensor(Sensor.TYPE_SIGNIFICANT_MOTION), SensorManager.SENSOR_DELAY_NORMAL);
////     TYPE_STEP_DETECTOR
////     步行检测传感器，用户每走一步就触发一次事件
////     传感器类型值（Sensor Type）：18 (0x00000012)
//     boolean type18=sm.registerListener(mySensorListener, sm.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR), SensorManager.SENSOR_DELAY_NORMAL);
////     TYPE_STEP_COUNTER
////             计步传感器
////     传感器类型值（Sensor Type）：19 (0x00000013)
//     boolean type19=sm.registerListener(mySensorListener, sm.getDefaultSensor(Sensor.TYPE_STEP_COUNTER), SensorManager.SENSOR_DELAY_NORMAL);
////     TYPE_GEOMAGNETIC_ROTATION_VECTOR
////     地磁旋转矢量传感器，提供手机的旋转矢量，当手机处于休眠状态时，仍可以记录设备的方位
////     传感器类型值（Sensor Type）：20 (0x00000014)
//     boolean type20=sm.registerListener(mySensorListener, sm.getDefaultSensor(Sensor.TYPE_GEOMAGNETIC_ROTATION_VECTOR), SensorManager.SENSOR_DELAY_NORMAL);
