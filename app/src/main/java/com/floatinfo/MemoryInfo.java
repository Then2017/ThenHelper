package com.floatinfo;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.main.thenhelper.MainActivity;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Then on 2015/9/4.
 */
public class MemoryInfo {
    private Context mcontext;
    DecimalFormat df  = new DecimalFormat("######0.0");
    String cpuinfo="---";
    Thread CPUThread;
    boolean CPUThreadrunable;
    public MemoryInfo(Context context) {
        mcontext = context;
    }

    //得到总内存
    private long getTotalMemory(){
        long totalMemorySize=0;
        try {
            String dir = "/proc/meminfo";
            FileReader fr = new FileReader(dir);
            BufferedReader br = new BufferedReader(fr, 2048);
            String memoryLine = br.readLine();
            String subMemoryLine = memoryLine.substring(memoryLine.indexOf("MemTotal:"));
            br.close();
            totalMemorySize = Integer.parseInt(subMemoryLine.replaceAll("\\D+", ""));
          } catch (IOException e) {
            e.printStackTrace();
        }
        return totalMemorySize;
    }
    //系统可用内存
    ActivityManager.MemoryInfo mi =null;
    private  long getAvailableMemory() {
         mi = new ActivityManager.MemoryInfo();
        ActivityManager mActivityManager=(ActivityManager) mcontext.getSystemService(Context.ACTIVITY_SERVICE);
        mActivityManager.getMemoryInfo(mi);
        return mi.availMem/1024;
    }
    //是否低内存
    private  String islowmemory(){
        if(mi.lowMemory){
            return "low";
        }else {
            return "";
        }
    }
    //启动CPU更新
    public void startCPU(){
        Log.e("ThenHelper", "Start update CPU info CPUThread");
        CPUThreadrunable=true;
        CPUThread=new CPUThread();
        CPUThread.start();
    }
    //停止CPU更新
    public void stopCPU(){
        CPUThreadrunable=false;
    }
    //线程更新CPU信息
    public  class CPUThread extends Thread {
        public void run () {
            do {
                try {
                    cpuinfo=getCPUdata();
                    sleep(100);
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while(CPUThreadrunable);
            Log.e("ThenHelper", "Stop update CPU info CPUThread");
        }
    }
    //CPU使用率
    public String getCPUdata() {
        String Result,str="---";
        Process p = null;
        try {
            p =  Runtime.getRuntime().exec(new String[]{"/system/bin/sh", "-c","top -m 1 -n 1"});
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            while ((Result = br.readLine()) != null) {
            if (Result.trim().length() < 1) {
                continue;
            } else {
                String[] CPU = Result.split(",");
                String CPUuser = CPU[0].substring(CPU[0].length()-2,CPU[0].length()).toString();
                String CPUsys = CPU[1].substring(CPU[1].length()-2,CPU[1].length()).toString();
                str="User="+CPUuser.trim()+" Sys="+CPUsys.trim();
                break;
            }
        }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return str;
    }


    public String getMemoryInfoString(){
        String str= "Memory Info : "+df.format((getTotalMemory() - getAvailableMemory()) / (float) getTotalMemory() * 100) +"% used \n"+
                "Detail : "+(getTotalMemory()-getAvailableMemory())/1024+"MB/"+ getTotalMemory()/1024+"MB "+islowmemory()+"\n"+
                "CPU : "+cpuinfo+" (top)\n";
        return str;
    }
}
