package com.sensor;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaScannerConnection;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidplot.util.PlotStatistics;
import com.androidplot.xy.BarFormatter;
import com.androidplot.xy.BarRenderer;
import com.androidplot.xy.BoundaryMode;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.LineAndPointRenderer;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;
import com.main.thenhelper.MainActivity;
import com.main.thenhelper.R;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by admin on 2015/9/11.
 */
public class SensorView extends LinearLayout implements SensorEventListener{
    private Context mcontext;
    private Sensor sensor;
    private SensorManager sensorManager;
    private CheckBox enabledCB,logToSDcardCB,EnableGraphCB;
    private RadioGroup sensorDelayRG;
    private RadioButton sensorDelayNormalRB,sensorDelayUiRB,sensorDelayGameRB,sensorDelayFastestRB;
    private Button infoButton;
    private TextView sensorVal0TV,sensorVal1TV,sensorVal2TV,deltaTimeTV,eventCountTV,OtherInfo;
    private int currentSensorDelay=3;
    private long sensorEventCounter = 0;;
    private boolean isListeningToEvents = false;
    private boolean isGraphEnabled = false;
    private boolean isLogging = false;
    private float currV0 = 0.0f;
    private float currV1 = 0.0f;
    private float currV2 = 0.0f;
    private long currentTime;
    private long deltaTime;
    private long lastTime;
    private int currFreq = 0;
    private long currTimeMS = 0;
    private SensorLogger sensorLogger;
    Handler mHandler=new Handler();
    private Timer timer;
    private List<SensorView> sensorViewList;
    DecimalFormat df  = new DecimalFormat("######0.000000");
    SensorOtherInfo sensorOtherInfo;

    public SensorView(Context context, Sensor paramSensor, SensorManager paramSensorManager,List<SensorView> sv) {
        super(context);
        this.mcontext=context;
        this.sensor=paramSensor;
        this.sensorManager=paramSensorManager;
        sensorViewList=sv;
        init();
        enabledCB.setText(getsensorname(sensor));
    }

    //初始化
    private void init(){
        LayoutInflater inflater=(LayoutInflater)mcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout ly = (LinearLayout) inflater.inflate(R.layout.sensor_view, this);
        sensorVal0TV = (TextView)ly.findViewById(R.id.sensorVal0TV);
        sensorVal1TV = (TextView)ly.findViewById(R.id.sensorVal1TV);
        sensorVal2TV = (TextView)ly.findViewById(R.id.sensorVal2TV);
        eventCountTV=(TextView)ly.findViewById(R.id.eventCountTV);
        deltaTimeTV = (TextView)ly.findViewById(R.id.deltaTimeTV);
        OtherInfo=(TextView)ly.findViewById(R.id.OtherInfo);
        sensorDelayRG = (RadioGroup)ly.findViewById(R.id.sensorDelayRG);
        sensorDelayNormalRB=(RadioButton)ly.findViewById(R.id.sensorDelayNormalRB);
        sensorDelayUiRB=(RadioButton)ly.findViewById(R.id.sensorDelayUiRB);
        sensorDelayGameRB=(RadioButton)ly.findViewById(R.id.sensorDelayGameRB);
        sensorDelayFastestRB=(RadioButton)ly.findViewById(R.id.sensorDelayFastestRB);
        infoButton=(Button)ly.findViewById(R.id.infoButton);
        logToSDcardCB=(CheckBox)ly.findViewById(R.id.logToSDcardCB);
        enabledCB=(CheckBox)ly.findViewById(R.id.enabledCB);
        EnableGraphCB=(CheckBox)ly.findViewById(R.id.EnableGraphCB);
        OtherInfo.setVisibility(View.INVISIBLE);
        logToSDcardCB.setEnabled(false);
        EnableGraphCB.setEnabled(false);
        //记录Log
        logToSDcardCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    isLogging=true;
                    String str="Sensor : "+sensor.getName()+"\nDelay : "+getsensordelay(currentSensorDelay)+"\n";
                    try {
                        sensorLogger=new SensorLogger(str,getsensorname(sensor));
                        sensorLogger.start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else {
                    isLogging=false;
                    sensorLogger.stopLogger();
                }
            }
        });
        //显示信息
        infoButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                // 打印每个传感器信息
                StringBuilder strLog = new StringBuilder();
                strLog.append("Sensor Type : " + sensor.getType() + "\r\n");
                strLog.append("Sensor Name : " + sensor.getName() + "\r\n");
                strLog.append("Sensor Version : " + sensor.getVersion() + "\r\n");
                strLog.append("Sensor Vendor : " + sensor.getVendor() + "\r\n");
                strLog.append("Maximum Range : " + sensor.getMaximumRange() + "\r\n");
                strLog.append("Minimum Delay : " + sensor.getMinDelay() + "\r\n");
                strLog.append("Power : " + sensor.getPower() + "\r\n");
                strLog.append("Resolution : " + sensor.getResolution() + "\r\n");
                strLog.append("\r\n");

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.sensorFragement.getActivity());
                builder.setTitle(enabledCB.getText());
                builder.setMessage(strLog.toString());
                builder.setNegativeButton("Float windows", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // if (isServiceRunning(MainActivity.mContext, "com.sensor.SensorService")) {
                        Intent localIntent = new Intent(mcontext, SensorService.class);
                        mcontext.stopService(localIntent);
                        Log.e("ThenHelper", "SensorService is running,stop it. ");
                        //  }
                        Intent intent = new Intent(mcontext, SensorService.class);
                        Bundle bundle = new Bundle();
                        bundle.putStringArray("sensortype", new String[]{sensor.getType() + ""});
                        bundle.putStringArray("sensorname", new String[]{enabledCB.getText().toString()});
                        intent.putExtras(bundle);
                        mcontext.startService(intent);
                        Log.e("ThenHelper", "start float windows : " + enabledCB.getText().toString());
                    }
                });
                builder.setPositiveButton("OK", null);
                builder.show();
            }
        });
        //勾选启动
        enabledCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                Iterator iterator= sensorViewList.iterator();
//                int i=0;
//                while (iterator.hasNext()) {
//                        if(((SensorView) iterator.next()).isopen()){
//                            i++;
//                        }
//                    }
//                if(i>15){
//                    Toast.makeText(MainActivity.sensorFragement.getActivity(), "You can only open 15 sensor with max at the same time!", Toast.LENGTH_SHORT).show();
//                    return;
//                }
                if(b){
                    sensorDelayNormalRB.setEnabled(false);
                    sensorDelayUiRB.setEnabled(false);
                    sensorDelayGameRB.setEnabled(false);
                    sensorDelayFastestRB.setEnabled(false);
                    logToSDcardCB.setEnabled(true);
                    EnableGraphCB.setEnabled(true);
                    sensorEventCounter=0;
                    subscribeToSensorEvents(currentSensorDelay);
                    timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            mHandler.post(GuiUpdater);
                        }
                    },0, 200);
                }else {
                    sensorDelayNormalRB.setEnabled(true);
                    sensorDelayUiRB.setEnabled(true);
                    sensorDelayGameRB.setEnabled(true);
                    sensorDelayFastestRB.setEnabled(true);
                    logToSDcardCB.setEnabled(false);
                    EnableGraphCB.setEnabled(false);
                    if(timer!=null){
                        timer.cancel();
                    }
                    unSubscribeToSensorEvents();
                }
            }
        });
        //设置延迟
        sensorDelayRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                //Log.e("ThenHelper",""+i);
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
        //勾选绘图
        EnableGraphCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    isGraphEnabled=true;
                    aprHistoryPlot.setVisibility(View.VISIBLE);
                }else {
                    isGraphEnabled=false;
                }
            }
        });

        initXYPlot(ly);
    }
    //检查是否开启
    public boolean isopen(){
        if(enabledCB.isChecked()){
            return true;
        }
        return false;
    }
    //开启所有
    public void allsensoron(int delay){
        EnableGraphCB.setChecked(false);
        logToSDcardCB.setChecked(false);
        switch (delay){
            case 3:
                sensorDelayNormalRB.setChecked(true);
                break;
            case 2:
                sensorDelayUiRB.setChecked(true);
                break;
            case 1:
                sensorDelayGameRB.setChecked(true);
                break;
            case 0:
                sensorDelayFastestRB.setChecked(true);
                break;
        }
        enabledCB.setChecked(true);
    }
    //关闭所有
    public void allsensoroff(){
        enabledCB.setChecked(false);
    }
    //开始监控
    private void subscribeToSensorEvents(int paramInt)
    {
        sensorManager.registerListener(this, sensor, paramInt);
        isListeningToEvents = true;
        if(sensor.getType()==6||sensor.getType()==3||sensor.getType()==5){
            sensorOtherInfo=new SensorOtherInfo();
            OtherInfo.setVisibility(View.VISIBLE);
        }
        Log.e("ThenHelper","start sensorlistener "+sensor.getName());
    }

    //取消监控
    public void unSubscribeToSensorEvents()
    {
        if(logToSDcardCB.isChecked()){
            logToSDcardCB.setChecked(false);
        }
        if (!isListeningToEvents) {
            return;
        }else {
            sensorManager.unregisterListener(this);
            isListeningToEvents = false;
            Log.e("ThenHelper", "cancle sensorlistener " + sensor.getName());
        }
    }
    //sensor延迟
    public String getsensordelay(int currentSensorDelay){
        String str="Unknow";
        switch (currentSensorDelay){
            case 0:
                str="SENSOR_DELAY_FASTEST";
                break;
            case 1:
                str="SENSOR_DELAY_GAME";
                break;
            case 2:
                str="SENSOR_DELAY_UI";
                break;
            case 3:
                str="SENSOR_DELAY_NORMAL";
                break;
        }
        return str;
    }
    //sensor名称
    public String getsensorname(Sensor sensor){
            String sensorname;
            switch (sensor.getType()) {
                case 1:
                    sensorname = "TYPE1_ACCELEROMETER";
                    break;
                case 2:
                    sensorname = "TYPE2_MAGNETIC_FIELD";
                    break;
                case 3:
                    sensorname = "TYPE3_ORIENTATION";
                    break;
                case 4:
                    sensorname = "TYPE4_GYROSCOPE";
                    break;
                case 5:
                    sensorname = "TYPE5_LIGHT";
                    break;
                case 6:
                    sensorname = "TYPE6_PRESSURE";
                    break;
                case 7:
                    sensorname = "TYPE7_TEMPERATURE";
                    break;
                case 8:
                    sensorname = "TYPE8_PROXIMITY";
                    break;
                case 9:
                    sensorname = "TYPE9_GRAVITY";
                    break;
                case 10:
                    sensorname = "TYPE10_LINEAR_ACCELERATION";
                    break;
                case 11:
                    sensorname = "TYPE11_ROTATION_VECTOR";
                    break;
                case 12:
                    sensorname = "TYPE12_RELATIVE_HUMIDITY";
                    break;
                case 13:
                    sensorname = "TYPE13_AMBIENT_TEMPERATURE";
                    break;
                case 14:
                    sensorname = "TYPE14_MAGNETIC_FIELD_UNCALIBRATED";
                    break;
                case 15:
                    sensorname = "TYPE15_GAME_ROTATION_VECTOR";
                    break;
                case 16:
                    sensorname = "TYPE16_GYROSCOPE_UNCALIBRATED";
                    break;
                case 17:
                    sensorname = "TYPE17_SIGNIFICANT_MOTION";
                    break;
                case 18:
                    sensorname = "TYPE18_STEP_DETECTOR";
                    break;
                case 19:
                    sensorname = "TYPE19_STEP_COUNTER";
                    break;
                case 20:
                    sensorname = "TYPE20_GEOMAGNETIC_ROTATION_VECTOR";
                    break;
                default:
                    sensorname = sensor.getName();
                    break;
            }
                return sensorname;
    }
    //UI更新
    private Runnable GuiUpdater = new Runnable()
    {
        public void run()
        {
           deltaTimeTV.setText(" Freq: "+currTimeMS + "ms/" + currFreq + "hz");
            sensorVal0TV.setText( "X:\n" + df.format(currV0));
            sensorVal1TV.setText( "Y:\n" + df.format(currV1));
            sensorVal2TV.setText("Z:\n" + df.format(currV2));
            eventCountTV.setText("event count: " + sensorEventCounter);
            //气压得海拔
            if(sensor.getType()==6){
                OtherInfo.setText(" Other Info : " +
                                "Altitude=" + sensorOtherInfo.pressureToAltitude(currV0) + "m"
                );
            }
            //方向得东西南北
            if(sensor.getType()==3){
                float direction = currV0 * -1.0f;
                OtherInfo.setText(" Other Info : "+
                                "Compass="+sensorOtherInfo.orientationToWhere(sensorOtherInfo.normalizeDegree(direction))
                );
            }
            //亮度值
            if(sensor.getType()==5){
                OtherInfo.setText(" Other Info : "+
                        "Brightness="+sensorOtherInfo.getexistValue("/sys/class/leds/lcd-backlight/brightness"));
            }
        }
    };
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        sensorEventCounter++;
        currentTime = sensorEvent.timestamp;
        deltaTime = (this.currentTime - this.lastTime);
        lastTime = this.currentTime;
        currTimeMS = (this.deltaTime / 1000000L);
        currFreq = (int)(1.0F / ((float)this.deltaTime / 1.0E+009F));

        if (sensorEvent.sensor == null) {
            return;
        }
        if (sensorEvent.sensor.getType() ==sensor.getType() ) {
            if(sensorEvent.values.length==3) {
                currV0 = sensorEvent.values[0];
                currV1 = sensorEvent.values[1];
                currV2 = sensorEvent.values[2];
            }
            if(sensorEvent.values.length==1) {
                currV0 = sensorEvent.values[0];
            }
            if(sensorEvent.values.length==2) {
                currV0 = sensorEvent.values[0];
                currV1 = sensorEvent.values[1];
            }
            if(isGraphEnabled){
                //绘图
                // update instantaneous data:
                if(aprLevelsPlotopen) {
                    Number[] series1Numbers = {currV0,currV1, currV2};
                    aprLevelsSeries.setModel(Arrays.asList(series1Numbers), SimpleXYSeries.ArrayFormat.Y_VALS_ONLY);
                }
                // get rid the oldest sample in history:
                if (rollHistorySeries.size() > HISTORY_SIZE) {
                    rollHistorySeries.removeFirst();
                    pitchHistorySeries.removeFirst();
                    azimuthHistorySeries.removeFirst();
                }
                // add the latest history sample:
                azimuthHistorySeries.addLast(null, currV0);
                pitchHistorySeries.addLast(null, currV1);
                rollHistorySeries.addLast(null, currV2);
        /*// update the plot with the updated history Lists:
        azimuthHistorySeries.setModel(azimuthHistory, SimpleXYSeries.ArrayFormat.Y_VALS_ONLY);
        pitchHistorySeries.setModel(pitchHistory, SimpleXYSeries.ArrayFormat.Y_VALS_ONLY);
        rollHistorySeries.setModel(rollHistory, SimpleXYSeries.ArrayFormat.Y_VALS_ONLY);*/
                // redraw the Plots:
                aprLevelsPlot.redraw();
                aprHistoryPlot.redraw();
            }
            if(isLogging){
                //写Log
                sensorLogger.addData(sensorEventCounter,currentTime,sensorEvent.values);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    //绘图
    /**
     * A simple formatter to convert bar indexes into sensor names.
     */
    private class APRIndexFormat extends Format {
        @Override
        public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
            Number num = (Number) obj;
            // using num.intValue() will floor the value, so we add 0.5 to round instead:
            int roundNum = (int) (num.floatValue() + 0.5f);
            switch(roundNum) {
                case 0:
                    toAppendTo.append("X");
                    break;
                case 1:
                    toAppendTo.append("Y");
                    break;
                case 2:
                    toAppendTo.append("Z");
                    break;
                default:
                    toAppendTo.append("Unknown");
            }
            return toAppendTo;
        }

        @Override
        public Object parseObject(String source, ParsePosition pos) {
            return null;  // We don't use this so just return null for now.
        }
    }
    private static final int HISTORY_SIZE = 75;            // number of points to plot in history
    private XYPlot aprLevelsPlot = null;
    private XYPlot aprHistoryPlot = null;
    private SimpleXYSeries aprLevelsSeries = null;
    private SimpleXYSeries azimuthHistorySeries = null;
    private SimpleXYSeries pitchHistorySeries = null;
    private SimpleXYSeries rollHistorySeries = null;
    private boolean aprLevelsPlotopen=false,aprLevelsPlotclick=false;
    //private LinkedList<Number> azimuthHistory;
    //private LinkedList<Number> pitchHistory;
    //private LinkedList<Number> rollHistory;

    /*{
        azimuthHistory = new LinkedList<Number>();
        pitchHistory = new LinkedList<Number>();
        rollHistory = new LinkedList<Number>();
    }*/
    public void initXYPlot(LinearLayout ly) {
        // setup the APR Levels plot:
        aprLevelsPlot = (XYPlot)ly.findViewById(R.id.aprLevelsPlot);

        aprLevelsSeries = new SimpleXYSeries("Value Level");
        aprLevelsSeries.useImplicitXVals();
        aprLevelsPlot.addSeries(aprLevelsSeries, new BarFormatter(Color.rgb(51, 255, 255), Color.rgb(0, 80, 0)));
        aprLevelsPlot.setDomainStepValue(2);
        aprLevelsPlot.setTicksPerRangeLabel(4);
//        BarRenderer renderer = ((BarRenderer)aprLevelsPlot.getRenderer(BarRenderer.class));
//        //柱子设置是依次排列的，默认是层叠在一个柱子里的，如下图
//        renderer.setBarRenderStyle(BarRenderer.BarRenderStyle.SIDE_BY_SIDE);
//        //设置柱子为定宽模式
//        renderer.setBarWidthStyle(BarRenderer.BarWidthStyle.FIXED_WIDTH);
//        renderer.setBarWidth(100);
//        //设置柱子为变宽模式，会根据间隔自动变化
//        renderer.setBarWidthStyle(BarRenderer.BarWidthStyle.VARIABLE_WIDTH);
//        renderer.setBarGap(300);

        // per the android documentation, the minimum and maximum readings we can get from
        // any of the orientation sensors is -180 and 359 respectively so we will fix our plot's
        // boundaries to those values.  If we did not do this, the plot would auto-range which
        // can be visually confusing in the case of dynamic plots.
       // aprLevelsPlot.setRangeBoundaries(-180, 359, BoundaryMode.FIXED);

        // use our custom domain value formatter:
        aprLevelsPlot.setDomainValueFormat(new APRIndexFormat());
        // update our domain and range axis labels:
       // aprLevelsPlot.setDomainLabel("Axis");
        aprLevelsPlot.getDomainLabelWidget().pack();
        //aprLevelsPlot.setRangeLabel("Angle (Degs)");
        aprLevelsPlot.getRangeLabelWidget().pack();
        aprLevelsPlot.setGridPadding(15, 0, 15, 0);
        //aprLevelsPlot.addListener(new PlotStatistics(1000, true));
        //aprLevelsPlot.disableAllMarkup();

        // setup the APR History plot:
        aprHistoryPlot = (XYPlot)ly.findViewById(R.id.aprHistoryPlot);
        azimuthHistorySeries = new SimpleXYSeries("X");
        azimuthHistorySeries.useImplicitXVals();
        pitchHistorySeries = new SimpleXYSeries("Y");
        pitchHistorySeries.useImplicitXVals();
        rollHistorySeries = new SimpleXYSeries("Z");
        rollHistorySeries.useImplicitXVals();

     //   aprHistoryPlot.setRangeBoundaries(-180, 359, BoundaryMode.FIXED);
        aprHistoryPlot.setDomainBoundaries(0, 75, BoundaryMode.AUTO);
        Paint localPaint = aprHistoryPlot.getBorderPaint();
        localPaint.setStrokeWidth(1.0F);
        aprHistoryPlot.setBorderPaint(localPaint);
        aprHistoryPlot.addSeries(azimuthHistorySeries, new LineAndPointFormatter(Color.rgb(100, 100, 200), null,null, null));
        aprHistoryPlot.addSeries(pitchHistorySeries, new LineAndPointFormatter(Color.rgb(100, 200, 100), null, null, null));
        aprHistoryPlot.addSeries(rollHistorySeries, new LineAndPointFormatter(Color.rgb(255, 100, 100), null, null, null));
        aprHistoryPlot.setDomainStepValue(2);
        aprHistoryPlot.setTicksPerRangeLabel(4);
        // aprHistoryPlot.setDomainLabel("Sample Index");
        aprHistoryPlot.getDomainLabelWidget().pack();
        //  aprHistoryPlot.setRangeLabel("Angle (Degs)");
        aprHistoryPlot.getRangeLabelWidget().pack();
       // aprHistoryPlot.disableAllMarkup();
        aprHistoryPlot.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(aprLevelsPlotclick==false){
                    aprLevelsPlotclick=true;
                    aprLevelsPlot.setVisibility(View.VISIBLE);
                    aprLevelsPlotopen=true;
                    Toast.makeText(MainActivity.mContext, "Show More" , Toast.LENGTH_SHORT).show();
                    return;
                }
                if(aprLevelsPlotclick==true){
                    aprLevelsPlotclick=false;
                    aprLevelsPlot.setVisibility(View.GONE);
                    aprLevelsPlotopen=false;
                    return;
                }
            }
        });
        // setup checkboxes:
        final PlotStatistics levelStats = new PlotStatistics(1000, false);
        final PlotStatistics histStats = new PlotStatistics(1000, false);

        aprLevelsPlot.addListener(levelStats);
        aprHistoryPlot.addListener(histStats);

        //显示帧率false
        levelStats.setAnnotatePlotEnabled(false);
        histStats.setAnnotatePlotEnabled(false);
        //硬件加速
        aprLevelsPlot.setLayerType(View.LAYER_TYPE_NONE, null);
        aprHistoryPlot.setLayerType(View.LAYER_TYPE_NONE, null);
        //aprLevelsPlot.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
       // aprHistoryPlot.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        // get a ref to the BarRenderer so we can make some changes to it:
        BarRenderer barRenderer = (BarRenderer) aprLevelsPlot.getRenderer(BarRenderer.class);
        if(barRenderer != null) {
            // make our bars a little thicker than the default so they can be seen better:
            barRenderer.setBarWidth(25);
        }
        aprLevelsPlot.setVisibility(View.GONE);
        aprHistoryPlot.setVisibility(View.GONE);
    }


}
