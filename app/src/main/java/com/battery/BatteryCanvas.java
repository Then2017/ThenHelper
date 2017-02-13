package com.battery;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Environment;
import android.util.Log;

import com.main.thenhelper.MainActivity;

import org.achartengine.ChartFactory;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by admin on 2015/9/2.
 */


public class BatteryCanvas  {

    Double[] Xdatastr;
    Date[] Datetimestr;
    int MAX,MIN;
    String title;
    public int getDateType(String type){
        if(type.equals("Capacity")){
            return 2;
        }
        if(type.equals("Current")){
            return 3;
        }
        if(type.equals("Voltage")){
            return 4;
        }
        if(type.equals("Temp")){
            return 5;
        }
        if(type.equals("Brightness")){
            return 9;
        }
        return -1;
    }
    public String getDateTitle(String type){
        if(type.equals("Capacity")){
            return "Capacity : %";
        }
        if(type.equals("Current")){
            return "Current : mA";
        }
        if(type.equals("Voltage")){
            return "Voltage : mV";
        }
        if(type.equals("Temp")){
            return "Temp : ℃";
        }
        if(type.equals("Brightness")){
            return "Brightness";
        }
        return "Unknow";
    }

    public int getData(String filepath,String type,int pointnum,String Sdate,String Edate,String Stime,String Etime) throws IOException {
        int datatype=getDateType(type);
        title=getDateTitle(type);
        int count=0;
        File file = new File(filepath);
        if (file.isDirectory())
        {
            Log.e("ThenHelper", "The File doesn't not exist.");
        }else {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date datetime = null;
            InputStream instream = null;
            InputStreamReader inputreader = null;
            BufferedReader buffreader = null;
            int max=-100000,min=100000,temppoint=0,endpoint=0;
            ArrayList<Double> Xdata = new ArrayList<Double>();
            ArrayList<Date> Datetime = new ArrayList<Date>();
            try {
                instream = new FileInputStream(file);
                inputreader = new InputStreamReader(instream);
                buffreader = new BufferedReader(inputreader);
                String line;
                buffreader.readLine();
                Date Sdatetime = null;
                Date Edatetime=null;
                Date tempdatetime=null;
                try {
                    Sdatetime=formatter.parse(Sdate + " " + Stime+":00");
                    Edatetime=formatter.parse(Edate + " " + Etime+":00");
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                while ((line = buffreader.readLine()) != null) {
                    String[] linesplit = line.split(",");
                    if (linesplit[0].equals("TAG")){
                        continue;
                    }
                    tempdatetime=formatter.parse(linesplit[0]+" "+linesplit[1]);
                    if ((tempdatetime.getTime()>=Sdatetime.getTime())&&(tempdatetime.getTime()<Edatetime.getTime())) {
                        if(temppoint<=pointnum&&temppoint!=0){
                            temppoint++;
                        }else {
                            Xdata.add(Double.parseDouble(linesplit[datatype]));
                            datetime = formatter.parse(linesplit[0] + " " + linesplit[1]);
                            Datetime.add(datetime);
                            if (max < Integer.parseInt(linesplit[datatype])) {
                                max = Integer.parseInt(linesplit[datatype]);
                            }
                            if (min > Integer.parseInt(linesplit[datatype])) {
                                min = Integer.parseInt(linesplit[datatype]);
                            }
                            count++;
                            temppoint=1;
                        }
                        continue;
                    }
                    if(temppoint>0&&tempdatetime.getTime()>=Edatetime.getTime()&&endpoint==0){
                        Xdata.add(Double.parseDouble(linesplit[datatype]));
                        datetime = formatter.parse(linesplit[0] + " " + linesplit[1]);
                        Datetime.add(datetime);
                        if (max < Integer.parseInt(linesplit[datatype])) {
                            max = Integer.parseInt(linesplit[datatype]);
                        }
                        if (min > Integer.parseInt(linesplit[datatype])) {
                            min = Integer.parseInt(linesplit[datatype]);
                        }
                        count++;
                        endpoint=1;
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            } finally {
                if (instream != null) {
                    instream.close();
                }
                if (inputreader != null) {
                    inputreader.close();
                }
                if (buffreader != null) {
                    buffreader.close();
                }
            }
            Xdatastr = (Double[]) Xdata.toArray(new Double[0]);
            Datetimestr = (Date[]) Datetime.toArray(new Date[0]);
            MAX=max;
            MIN=min;
        }
        return count;
    }

    public Intent getDIYIntent(Context context,String filepath,String type,int pointnum,String Sdate,String Edate,String Stime,String Etime){
        try {
            if( getData(filepath,type,pointnum,Sdate,Edate,Stime,Etime)<3){
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Intent mIntent= ChartFactory.getTimeChartIntent(context,
                DrawableUtil.setAchartDataset(title,Datetimestr,Xdatastr), DrawableUtil.setAchartRenderer(type,MIN-5,MAX+5), "HH:mm_dd");
        return mIntent;
    }

}
