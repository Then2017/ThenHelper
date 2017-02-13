package com.battery;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by admin on 2015/7/28.
 */
public class BatteryConfig {
    private String capacity;
    private long current;
    private long voltage;
    private int temp;
    private String Status;
    private String chargetype;
    private String health;
    private String brightness;
    private String date;
    private String time;
    private int maxtemp;
    private int mintemp;
    private int Scapacity;
    private int Ecapacity;
    private String hascharge;
    private String STAG;
    private String ETAG;
    private String Sdatetime;
    private String Edatetime;

    public String getSdatetime() {
        return Sdatetime;
    }

    public void setSdatetime(String sdatetime) {
        Sdatetime = sdatetime;
    }

    public String getEdatetime() {
        return Edatetime;
    }

    public void setEdatetime(String edatetime) {
        Edatetime = edatetime;
    }

    public String getETAG() {
        return ETAG;
    }

    public void setETAG(String ETAG) {
        this.ETAG = ETAG;
    }

    public String getSTAG() {
        return STAG;
    }

    public void setSTAG(String STAG) {
        this.STAG = STAG;
    }

    public String getHascount() {
        return hascount;
    }

    public void setHascount(String hascount) {
        this.hascount = hascount;
    }

    private String hascount;

    public int getScapacity() {
        return Scapacity;
    }

    public void setScapacity(int scapacity) {
        Scapacity = scapacity;
    }

    public int getEcapacity() {
        return Ecapacity;
    }

    public void setEcapacity(int ecapacity) {
        Ecapacity = ecapacity;
    }

    public String getHascharge() {
        return hascharge;
    }

    public void setHascharge(String hascharge) {
        this.hascharge = hascharge;
    }

    public String getCapacity() {
        return capacity;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    public long getCurrent() {
        return current;
    }

    public void setCurrent(long current) {
        this.current = current;
    }

    public long getVoltage() {
        return voltage;
    }

    public void setVoltage(long voltage) {
        this.voltage = voltage;
    }

    public int getTemp() {
        return temp;
    }

    public void setTemp(int temp) {
        this.temp = temp;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getChargetype() {
        return chargetype;
    }

    public void setChargetype(String chargetype) {
        this.chargetype = chargetype;
    }

    public String getHealth() {
        return health;
    }

    public void setHealth(String health) {
        this.health = health;
    }

    public String getBrightness() {
        return brightness;
    }

    public void setBrightness(String brightness) {
        this.brightness = brightness;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getMaxtemp() {
        return maxtemp;
    }

    public void setMaxtemp(int maxtemp) {
        this.maxtemp = maxtemp;
    }

    public int getMintemp() {
        return mintemp;
    }

    public void setMintemp(int mintemp) {
        this.mintemp = mintemp;
    }
}
