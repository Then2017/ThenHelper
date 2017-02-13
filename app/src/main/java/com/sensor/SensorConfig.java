package com.sensor;

/**
 * Created by theny550 on 2015/8/16.
 */
public class SensorConfig {
//    #define SENSOR_TYPE_ACCELEROMETER       1 //加速度
//            #define SENSOR_TYPE_MAGNETIC_FIELD      2 //磁力
//            #define SENSOR_TYPE_ORIENTATION         3 //方向
//            #define SENSOR_TYPE_GYROSCOPE           4 //陀螺仪
//            #define SENSOR_TYPE_LIGHT               5 //光线感应
//            #define SENSOR_TYPE_PRESSURE            6 //压力
//            #define SENSOR_TYPE_TEMPERATURE         7 //温度
//            #define SENSOR_TYPE_PROXIMITY           8 //接近
//            #define SENSOR_TYPE_GRAVITY             9 //重力
//            #define SENSOR_TYPE_LINEAR_ACCELERATION 10//线性加速度
//            #define SENSOR_TYPE_ROTATION_VECTOR     11//旋转矢量
//http://www.cnblogs.com/tyjsjl/p/3695808.html
    private String SENSOR_TYPE_ACCELEROMETER;
    private String SENSOR_TYPE_MAGNETIC_FIELD;

    public String getSENSOR_TYPE_ACCELEROMETER() {
        return SENSOR_TYPE_ACCELEROMETER;
    }

    public void setSENSOR_TYPE_ACCELEROMETER(String SENSOR_TYPE_ACCELEROMETER) {
        this.SENSOR_TYPE_ACCELEROMETER = SENSOR_TYPE_ACCELEROMETER;
    }

    public String getSENSOR_TYPE_MAGNETIC_FIELD() {
        return SENSOR_TYPE_MAGNETIC_FIELD;
    }

    public void setSENSOR_TYPE_MAGNETIC_FIELD(String SENSOR_TYPE_MAGNETIC_FIELD) {
        this.SENSOR_TYPE_MAGNETIC_FIELD = SENSOR_TYPE_MAGNETIC_FIELD;
    }
}
