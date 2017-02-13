package com.battery;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by admin on 2015/7/28.
 */
public class BatteryConstant {
    public static String capacity="/sys/class/power_supply/battery/capacity";
    public static  String current="/sys/class/power_supply/battery/current_now";
    public static  String voltage="/sys/class/power_supply/battery/voltage_now";
    public static String temp="/sys/class/power_supply/battery/temp";
    public static  String status="/sys/class/power_supply/battery/status";
    public static  String chargetype="/sys/class/power_supply/battery/charge_type";
    public static  String health="/sys/class/power_supply/battery/health";
    public static String brightness = "/sys/class/leds/lcd-backlight/brightness";

    //mt6735m
    public static  String mt6735m_current="/sys/devices/platform/battery/FG_Battery_CurrentConsumption";
    public static  String mt6735m_voltage="/sys/class/power_supply/battery/batt_vol";
    public static String mt6735m_temp="/sys/class/power_supply/battery/batt_temp";
    public static  String mt6735m_chargetype="NA";
}

