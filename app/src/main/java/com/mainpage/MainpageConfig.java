package com.mainpage;

import android.util.Log;

/**
 * Created by theny550 on 2015/8/2.
 */
public class MainpageConfig {
    private String model;
    private String innerversion;
    private String outerversion;
    private String platform;
    private String simstatus;
    private String usbconfig;
    private String logrunable;
    private String buildtype;
    private String rleasekey;
    private String isroot;

    public String getIsroot() {
        if(isroot.equals("1\n")){
            return isroot="yes\n";
        }else {
            return isroot="no\n";
        }
    }

    public void setIsroot(String isroot) {
        this.isroot = isroot;
    }

    public String getRleasekey() {
        return rleasekey;
    }

    public void setRleasekey(String rleasekey) {
        this.rleasekey = rleasekey;
    }

    public String getBuildtype() {
        return buildtype;
    }

    public void setBuildtype(String buildtype) {
        this.buildtype = buildtype;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getInnerversion() {
        return innerversion;
    }

    public void setInnerversion(String innerversion) {
        this.innerversion = innerversion;
    }

    public String getOuterversion() {
        return outerversion;
    }

    public void setOuterversion(String outerversion) {
        this.outerversion = outerversion;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getSimstatus() {
        if(simstatus.contains(",")) {
            String[] str = simstatus.split(",");
            if (str[0].equals("ABSENT")) {
                str[0] = "absent";
            } else {
                str[0] = "ready";
            }
            if (str[1].equals("ABSENT\n")) {
                str[1] = "absent";
            } else {
                str[1] = "ready";
            }
            return "SIM1-" + str[0] + ",SIM2-" + str[1] + "\n";
        }else {
            if (simstatus.equals("ABSENT")) {
                simstatus= "absent\n";
            } else {
                simstatus = "ready\n";
            }
            return simstatus;
        }
    }

    public void setSimstatus(String simstatus) {
        this.simstatus = simstatus;
    }

    public String getUsbconfig() {
        return usbconfig;
    }

    public void setUsbconfig(String usbconfig) {
        this.usbconfig = usbconfig;
    }

    public String getLogrunable() {
        return logrunable;
    }

    public void setLogrunable(String logrunable) {
        this.logrunable = logrunable;
    }

}
