package com.sensor;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.MediaScannerConnection;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.main.thenhelper.MainActivity;
import com.main.thenhelper.R;

import java.io.File;
import java.util.Iterator;
import java.util.List;

/**
 * Created by admin on 2015/9/14.
 */
public class SensorViewall extends LinearLayout {
    private Context mcontext;
    private List<SensorView> sensorViewList;
    private int num=0;
    public SensorViewall(Context context,List<SensorView> sl,int i) {
        super(context);
        mcontext=context;
        sensorViewList=sl;
        num=i;
        init();
    }

    //More界面
    private TextView totalsensor;
    private RadioGroup sensorDelayRG;
    private RadioButton sensorDelayNormalRB,sensorDelayUiRB,sensorDelayGameRB,sensorDelayFastestRB;
    private Button allsensoron,deletealllog;
    private int currentSensorDelay=3;
    private boolean allon=false,allonclick=false;
    public void init(){
        LayoutInflater inflater=(LayoutInflater) mcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout ly = (LinearLayout) inflater.inflate(R.layout.sensor_viewall, this);
        sensorDelayRG = (RadioGroup)ly.findViewById(R.id.sensorDelayRG);
        sensorDelayNormalRB=(RadioButton)ly.findViewById(R.id.sensorDelayNormalRB);
        sensorDelayUiRB=(RadioButton)ly.findViewById(R.id.sensorDelayUiRB);
        sensorDelayGameRB=(RadioButton)ly.findViewById(R.id.sensorDelayGameRB);
        sensorDelayFastestRB=(RadioButton)ly.findViewById(R.id.sensorDelayFastestRB);
        allsensoron=(Button)ly.findViewById(R.id.allsensoron);
        deletealllog=(Button)ly.findViewById(R.id.deletealllog);
        totalsensor=(TextView)ly.findViewById(R.id.totalsensor);

        totalsensor.setText("Total : "+num);
        //设置延迟
        sensorDelayRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
               // Log.e("ThenHelper", "" + i+"");
                if(sensorDelayNormalRB.getId()==i){
                    currentSensorDelay=3;
                    return;
                }
                if(sensorDelayUiRB.getId()==i){
                    currentSensorDelay=2;
                    return;
                }
                if(sensorDelayGameRB.getId()==i){
                    currentSensorDelay=1;
                    return;
                }
                if(sensorDelayFastestRB.getId()==i){
                    currentSensorDelay=0;
                    return;
                }

            }
        });
        //全部开启
        allsensoron.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Iterator iterator=sensorViewList.iterator();
                Iterator iteratortemp=sensorViewList.iterator();

                if(allon==false) {
                    allon=true;
                    allsensoron.setEnabled(false);
                    while (iteratortemp.hasNext()) {
                        ((SensorView) iteratortemp.next()).allsensoroff();
                    }
                    int i = 0;
                    while (iterator.hasNext()) {
                        ((SensorView) iterator.next()).allsensoron(currentSensorDelay);
                        i++;
                    }
                    allsensoron.setText("All sensor off");
                    allsensoron.setEnabled(true);
                    Toast.makeText(MainActivity.mContext, "ThenHelper opens "+i+" sensors!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(allon==true){
                    allon=false;
                    allsensoron.setEnabled(false);
                    while (iterator.hasNext()) {
                        ((SensorView) iterator.next()).allsensoroff();
                    }
                    allsensoron.setText("All sensor on");
                    allsensoron.setEnabled(true);
                    return;
                }
            }
        });
        //删除log
        deletealllog.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.sensorFragement.getActivity());
                builder.setIcon(R.mipmap.ico);
                builder.setTitle("Delete all files?").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        int i = delteRecord();
                        Toast.makeText(MainActivity.mContext, "Delete " + i + " sensor logs!", Toast.LENGTH_SHORT).show();
                        Log.e("ThenHelper", "Delete " + i + " sensor logs!");
                    }
                }).setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //选择了取消
                    }
                });
                builder.create().show();
            }
        });
    }
    //删除记录
    public int delteRecord () {
        int filecount=0;
        File BatteryPath = new File(Environment.getExternalStorageDirectory()+ "/ThenHelper/SensorMonitor/");
        if (!BatteryPath.exists()){
            Log.e("ThenHelper", "does not exist SensorMonitorPath ");
        }else {
            File[] files = BatteryPath.listFiles();
            for (File file : files) {
                file.delete();
                Scanner(file.getAbsolutePath());
                filecount++;
            }
        }
        Scanner(Environment.getExternalStorageDirectory()+ "/ThenHelper/SensorMonitor/");
        return filecount;
    }
    //扫描媒体
    //"/storage/emulated/0/ThenHelper/Media/Video/test.mp4"
    public void Scanner (String path) {
        MediaScannerConnection.scanFile(MainActivity.mContext, new String[]{path}, null, null);
    }
}
