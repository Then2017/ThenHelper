package com.floatinfo;

import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by admin on 2015/10/12   .
 */
public class CPUInfo {
    //得到总信息
    public String getInfo(int cpunum){
        StringBuffer str=new StringBuffer();
        str.append("GPU : "+getGPUHZ()/1000000+"MHz\n");
        str.append(geteachCpuRate(cpunum));
        return str.toString();
    }
    //GPU频率
    public int getGPUHZ(){
        int hz=0;
        try
        {
            BufferedReader reader=null;
            File file=new File("/sys/class/kgsl/kgsl-3d0/gpuclk");
            if(file.exists()){
                 reader = new BufferedReader(new InputStreamReader(
                        new FileInputStream("/sys/class/kgsl/kgsl-3d0/gpuclk")), 1000);
            }
            hz =Integer.parseInt(reader.readLine());
            if(reader!=null) {
                reader.close();
            }
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
        return hz;
    }
    //返回CPU频率信息
    public String getCPUHZInfo(int cpunum){
        StringBuffer str=new StringBuffer();
        for(int i=0;i<cpunum;i++){
            str.append(getCPUMaxMinHZ(i) + "\n");
        }
        return str.toString();
    }
    //CPU频率最大值最小值
    public String getCPUMaxMinHZ(int num){
        String str="NA";
        int maxhz=0;
        int minhz=0;
        try
        {
            BufferedReader reader=null;
            File maxfile=new File("/sys/devices/system/cpu/cpu"+num+"/cpufreq/scaling_max_freq");
            if(maxfile.exists()) {
                 reader = new BufferedReader(new InputStreamReader(
                        new FileInputStream("/sys/devices/system/cpu/cpu" + num + "/cpufreq/scaling_max_freq")), 1000);
                maxhz = Integer.parseInt(reader.readLine());
            }
            File minfile=new File("/sys/devices/system/cpu/cpu"+num+"/cpufreq/scaling_min_freq");
            if(minfile.exists()) {
                reader = new BufferedReader(new InputStreamReader(
                        new FileInputStream("/sys/devices/system/cpu/cpu" + num + "/cpufreq/scaling_min_freq")), 1000);
                minhz = Integer.parseInt(reader.readLine());
            }
            if(reader!=null) {
                reader.close();
            }
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
        if(maxhz==0&&minhz==0){
            str="Cpu" + num +" : off line";
        }else {
            str = "Cpu" + num + " : Max=" + maxhz / 1000 + "MHz Min=" + minhz / 1000 + "MHz";
        }
        return str;
    }
    //获取cpu当前核频率
    public int getCpuHZ(int num){
        int hz=0;
        try
        {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    new FileInputStream("/sys/devices/system/cpu/cpu"+num+"/cpufreq/scaling_cur_freq")), 1000);
            hz =Integer.parseInt(reader.readLine());
            reader.close();
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
        return hz;
    }

    //判断有几个核
    public int getCpunum(){
        int num=0;
        try
        {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    new FileInputStream("/proc/stat")), 1000);
            String load = reader.readLine();
            for(int i=0;i<=num;i++){
                load = reader.readLine();
                if(load.toString().startsWith("cpu")){
                    num++;
                }
            }
            reader.close();
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
        Log.e("ThenHelper","Total cpu "+num);
        return num;
    }
    //得到每个CPU数据
    public String geteachCpuRate(int cpunum)
    {
        ArrayList<long[]> ListS = new ArrayList();
        ArrayList<long[]> ListF = new ArrayList();
        for(int i=-1;i<cpunum;i++){
            ListS.add(geteachCpuTime(i));
        }
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for(int i=-1;i<cpunum;i++){
            ListF.add(geteachCpuTime(i));
        }

        int cpuRate;
        StringBuffer str=new StringBuffer();
        long[] S;
        long[] F;
        for(int i=0;i<cpunum+1;i++){
            F=ListF.get(i);
            S=ListS.get(i);
            if(F[0] - S[0]==0){
                cpuRate=-1;
            }else {
                cpuRate =(int)(100 * ((F[0] - S[0]) - (F[1] - S[1]))
                        / (F[0] - S[0]));
            }
     //   Log.e("ThenHelper",F[0]+" "+S[0]+" "+F[1]+" "+S[1]+" cpu"+(i-1)+" "+cpuRate);
            if(i==0){
            str.append("Cpu : "+cpuRate+"%\n");
                continue;
            }
           if(cpuRate==-1){
                str.append("Cpu" +( i-1) + " : off line\n");
           }else {
                  str.append("Cpu" + (i-1 )+ " : " + cpuRate + "% " + getCpuHZ(i-1) / 1000 + "MHz\n");
          }
        }
        return str.toString();
    }
    //得到每个CPU信息
    public  long[] geteachCpuTime(int num)
    {
        String[] cpuInfos = null;
        try
        {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    new FileInputStream("/proc/stat")), 1000);
            String load = reader.readLine();
            if(num!=-1) {
                for (int i = 0; i <= num; i++) {
                    load = reader.readLine();
                }
            }
            reader.close();
            cpuInfos = load.split(" ");
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
        long[] eachCpu={0,0};
        if(num==-1) {
            eachCpu[0] = Long.parseLong(cpuInfos[2])
                    + Long.parseLong(cpuInfos[3]) + Long.parseLong(cpuInfos[4])
                    + Long.parseLong(cpuInfos[6]) + Long.parseLong(cpuInfos[5])
                    + Long.parseLong(cpuInfos[7]) + Long.parseLong(cpuInfos[8]);
            eachCpu[1] = Long.parseLong(cpuInfos[5]);
        }else{
            eachCpu[0] = Long.parseLong(cpuInfos[1])
                    + Long.parseLong(cpuInfos[2]) + Long.parseLong(cpuInfos[3])
                    + Long.parseLong(cpuInfos[4]) + Long.parseLong(cpuInfos[5])
                    + Long.parseLong(cpuInfos[6]) + Long.parseLong(cpuInfos[7]);
            eachCpu[1] = Long.parseLong(cpuInfos[4]);
        }
        return eachCpu;
    }
}
