package com.sensor;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.main.thenhelper.MainActivity;

import java.text.DecimalFormat;

/**
 * Created by Then on 2015/8/22.
 */
public class SensorListener implements SensorEventListener {
    private Context mContext;
    private SensorManager mSensorManager;
    private Sensor item;
    private int id;
    final float[] data = new float[3];
    private TextView X,Y,Z;
    DecimalFormat df  = new DecimalFormat("######0.000");

    public SensorListener(Context context,Sensor sensor,int textViewid) {
        mContext = context;
        item=sensor;
        id=textViewid+100;
    }
    // 注册传感器服务
    public void enableSensor() {
        // 在这里真正注册Service服务
        mSensorManager = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);

        if (mSensorManager == null) {
            Log.e("ThenHelper","Sensors not supported "+item.getName());
        }
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(item.getType()),SensorManager.SENSOR_DELAY_NORMAL );
        Log.e("ThenHelper", "registerListener " + item.getName());
        X=(TextView) SensorMain.sensorlayout.findViewById(id);
        X.setVisibility(View.VISIBLE);
        Y=(TextView) SensorMain.sensorlayout.findViewById(id+100);
        Y.setVisibility(View.VISIBLE);
        Z=(TextView) SensorMain.sensorlayout.findViewById(id+200);
        Z.setVisibility(View.VISIBLE);
    }
    // 取消注册传感器服务
    public void disableSensor() {
        if (mSensorManager != null) {
            mSensorManager.unregisterListener(this);
            mSensorManager = null;
        }
        Log.e("ThenHelper", "cancel sensor manager successfully");
    }
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor == null) {
            return;
        }
        if (sensorEvent.sensor.getType() ==item.getType() ) {
            data[0] = sensorEvent.values[0];
            data[1] = sensorEvent.values[1];
            data[2] = sensorEvent.values[2];
            X.setText("  X : " + df.format(data[0]) );
            Y.setText("  Y : " + df.format(data[1]) );
            Z.setText("  Z : " + df.format(data[2]));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        Log.e("ThenHelper", "onAccuracyChanged "+item.getName());
    }
}
