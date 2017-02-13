package com.main.thenhelper;

import android.annotation.SuppressLint;
import android.app.Activity;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.provider.Settings;
import android.provider.Telephony;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.addcontacts.ContactsMain;
import com.addcontacts.MessageMain;
import com.addmedia.MediaMain;
import com.addstorage.StorageConfig;
import com.addstorage.StorageMain;
import com.battery.*;
import com.assistant.*;
import com.floatinfo.FloatinfoMain;
import com.floatinfo.FloatinfoService;
import com.mainpage.MainpageMain;
import com.screenrecord.DoodleService;
import com.screenrecord.RecordFloatService;
import com.screenrecord.ScreenRecord;
import com.screenrecord.ScreenRecordService;
import com.sensor.SensorMain;
import com.sensor.SensorService;

public class MainActivity extends Activity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {
    //***************************************************************************************************
    public static Context mContext;
    private Fragment lastFragment, newFragment;
    public static Fragment mainFragment=null, batteryFragment=null, assistantFragment=null,addcontactsFragment=null,
            addmediaFragement=null,addstorageFragement=null,sensorFragement=null,floatFragement=null,screenrecordFragement=null;
   // public static MainActivity mainActivity;
    //定义变量battery
    public static TextView BatteryInfo,StatisticsInfo,Starttime,Endtime,Startdate,Enddate;
    private static Button StartBattery,ClearBattery,Statistics,pickStartdate,pickEnddate,
            pickEndtime,pickStarttime,Selectfile,StartTAG,EndTAG,StatisticsTAG,
             BatteryLinechart;
    private static EditText BatteryInterval;
    private static CheckBox Iswakeup,SbytimeBox,alwaysscreenon;
    public static final int msgKey1 = 1;
    private static final int msgKey2 = 2;
    public static boolean StartBatteryrunable ,BatteryRecordThreadrunable,othersThreadrunable,pauseThread;
    public static Thread BatteryInfoThread,BatteryRecordThread;
    public static String platform;
    public static String SelectFile,Selectdatetype;
    private static int whichselect,batteryInterval;
    public static BatteryMain batteryMain;
    public static ImageView Coverimage;
    //定义变量assistant
    private static Button StartLog,gifgame,Root,createIMEI,createMAC,getroot,setbrightness,checkvolume,crashtest,vibrator;
    private static Thread CatchLogThread;
    public static boolean StartLogrunable =false,StartLogThreadrunable=false,isvibrated=false;
    public static CatchlogMain catchlogMain=new CatchlogMain();
    public static AssistantMain assistantMain=new AssistantMain();

    //定义变量mainpage
    private static TextView DevicesInfo;
    private static Button UpdateInfo;
    public static MainpageMain mainpageMain;

    //定义变量addcontacts
    //联系人
    private static Button mbtnadd,deletecontact,getcontactnum;
    private static EditText meditcontactnumber;
    public static CheckBox mcheckboxphoto, mcheckboxfavorite;
    public static TextView ContactProgressnum,showcontactnum;
    public static ProgressBar Contactprogressbar;
    public static boolean AddContactsThreadrunable,AddContactsrunable;
    public static Thread InsertcontactsThread;
    private static final int msgKey3 = 3;
    private static final int msgKey10 = 10;
    private static String contactsnum;
    private static int Contactprogresscount,totalcontact;
    //电话记录
    private static Button Addcalllog,getcallnum,deletecall;
    private static EditText calllognum, calllogphone;
    private static Spinner call_category;
    private static TextView showcallnum;
    public static String callphone,callnum;
    private static int calllogtype,totalcalllog;
    private  static Thread InsertcalllogThread;
    public static boolean AddCalllogThreadrunable,AddCalllogrunable;
    private static final int msgKey4 = 4;

    public static ContactsMain contactsMain;
    //信息
    private static Button addmessage,deletemessage,getmessagenum;
    private static TextView showmessagenum;
    private static EditText Messagenum,Messagephone,Messagecontent;
    public static CheckBox Issamesession;
    private static RadioGroup Messagetype,Messageisread,messagetypeGroup;
    public static RadioButton ReceiveMessage,SendMessage,ReadMessage,UnreadMessage,SMSradio,MMSradio;
    private static int totalmessage;
    public static String SelectMessagetype,MessageReadflag,Mnum,Mphone,SMSorMMS;
    private static Thread InsertSMSThread;
    public static boolean AddSMSThreadrunable,AddSMSrunable,Missame;
    private static final int msgKey7 = 7;
    public static MessageMain messageMain;
    //定义变量addmedia
    private static Button Addmedia,Deletemedia,getmedianum;
    public static RadioGroup mediaRadioGroup;
    public static RadioButton Musicradio,Videoradio,Pictureradio;
    public static EditText medianum;
    public static TextView Progressnum,showmedianum;
    public static ProgressBar Mediaprogressbar;
    public static String selectedmedia,addmedianum;
    public static int Mediacount,totalmedia;
    public static Thread AddmediaThread;
    private static final int msgKey5 = 5;
    private static final int msgKey6 = 6;
    public static boolean AddMediaThreadrunable,AddMediarunable;
    public static MediaMain mediaMain;
    //定义变量addstorage
    public static TextView StorageInfo,availablesize;
    public static EditText storagesize,retainsize;
    public static Button FillStorage,CleanStorage,StorageSpeed;
    public static RadioGroup StorageRadioGroup,storagesizetype;
    public static RadioButton SDcardradio,Storageradio,Dataradio,KBradio,MBradio,GBradio;
    public static CheckBox StorageDIYSize,retainsizebox;
    public static ProgressBar AddStorageBar;
    public static Thread FillStorageThread;
    public static boolean FillStorageThreadrunable,FillStoragerunable;
    private static final int msgKey8 = 8;
    private static final int msgKey9 = 9;
    private static long Fillstoragecount,DIYsizenum;
    private static int retainsizenum;
    public static String selectedstorage,selectedstoragetype;
    public static StorageMain storageMain;

    //定义变量sensor
    public static ScrollView sensorscrollView;
    public static SensorMain sensorMain;

    //定义变量floatinfo
    public static Button Start_floatinfo;
    public static CheckBox BrightnessBox,CurrentBox,PackageBox,SignalBox,WIFIBox,BTBox,MemoryBox,DoodleBox,CPUBox;
    public static String[] floatinfostr;
    public static FloatinfoMain floatinfoMain;

    //定义变量screenrecord
    public static Button StartRecord,recorddelete;
    public static CheckBox recordaudioBox,recordfloatBox,ShowtouchBox,ShowlocationBox;
    public static EditText recordname,recordBitRate,recordCountdown,recorddelay;
    public static Spinner recordsizespinner;
    public static String recordResolution,recordfilename;
    public static int recordBitRatenum,recordCountdownnum,recorddelaynum;
    public static boolean recordrunable;
    public static MediaProjectionManager mediaProjectionManager;
    public static MediaProjection mediaProjection;
    public static ScreenRecord screenRecord;

    public String aa="";
    //***************************************************************************************************
    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initThenHelper();
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();
        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        File FileStorage = new File(Environment.getExternalStorageDirectory()+"/ThenHelper");
        if (!FileStorage.exists()) {
            FileStorage.mkdir();
        }
        mContext=getApplicationContext();
        Log.e("ThenHelper", "start app");
    }
    @Override

    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
      /*  FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                .commit();*/
        if (newFragment != null) {
            lastFragment = newFragment;
        }
        switch (position) {
            case 0:
                mTitle = getString(R.string.title_section1);
                if (mainFragment == null) {
                    mainFragment = PlaceholderFragment.newInstance(1);
                }
                newFragment = mainFragment;
                break;
            case 1:
                mTitle = getString(R.string.title_section2);
                if (batteryFragment == null) {
                    batteryFragment = PlaceholderFragment.newInstance(2);
                }
                newFragment = batteryFragment;
                break;
            case 2:
                mTitle = getString(R.string.title_section3);
                if (assistantFragment == null) {
                    assistantFragment = PlaceholderFragment.newInstance(3);
                }
                newFragment = assistantFragment;
                break;
            case 3:
                mTitle = getString(R.string.title_section4);
                if (addcontactsFragment == null) {
                    addcontactsFragment = PlaceholderFragment.newInstance(4);
                }
                newFragment = addcontactsFragment;
                break;
            case 4:
                mTitle = getString(R.string.title_section5);
                if (addmediaFragement == null) {
                    addmediaFragement = PlaceholderFragment.newInstance(5);
                }
                newFragment = addmediaFragement;
                break;
            case 5:
                mTitle = getString(R.string.title_section6);
                if (addstorageFragement == null) {
                    addstorageFragement = PlaceholderFragment.newInstance(6);
                }
                newFragment = addstorageFragement;
                break;
            case 6:
                mTitle = getString(R.string.title_section7);
                if (sensorFragement == null) {
                    sensorFragement = PlaceholderFragment.newInstance(7);
                }
                newFragment = sensorFragement;
                break;
            case 7:
                mTitle = getString(R.string.title_section8);
                if (floatFragement == null) {
                    floatFragement = PlaceholderFragment.newInstance(8);
                }
                newFragment = floatFragement;
                break;
            case 8:
                mTitle = getString(R.string.title_section9);
                if (screenrecordFragement == null) {
                    screenrecordFragement = PlaceholderFragment.newInstance(9);
                }
                newFragment = screenrecordFragement;
                break;
        }

        if (newFragment == lastFragment) {
            return;
        } else {
            switchContent(lastFragment, newFragment);
        }
    }
    //转换Fragment
    public void switchContent(Fragment from, Fragment to) {
        FragmentManager fragmentManager = getFragmentManager();
        if (!to.isAdded()) { // 先判断是否被add过
            if (from != null) {
                fragmentManager.beginTransaction().hide(from).commit();
            }
            fragmentManager.beginTransaction().add(R.id.container, to).commit(); // 隐藏当前的fragment，add下一个到Activity中
        } else {
            fragmentManager.beginTransaction().hide(from).show(to).commit(); // 隐藏当前的fragment，显示下一个
        }
    }
    //重写返回键
    private long exitTime = 0;
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
//            Intent intent= new Intent(Intent.ACTION_MAIN);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); //如果是服务里调用，必须加入new task标识
//            intent.addCategory(Intent.CATEGORY_HOME);
//            startActivity(intent);
            Log.e("ThenHelper","press back key");
            // 判断是否在两秒之内连续点击返回键，是则退出，否则不退出
            if (System.currentTimeMillis() - exitTime > 2000) {
                Toast.makeText(MainActivity.mContext, "Press back key again to exit", Toast.LENGTH_SHORT).show();
                // 将系统当前的时间赋值给exitTime
                exitTime = System.currentTimeMillis();
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setIcon(R.mipmap.ico);
                builder.setMessage(" All services will be closed after exit !");
                builder.setTitle("Exit ThenHelper ?").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Log.e("ThenHelper","exit ThenHelper by back key");
                        // exitThenHelper();
                        finish();
                    }
                }).setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //选择了取消
                    }
                });
                builder.create().show();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
            case 4:
                mTitle = getString(R.string.title_section4);
                break;
            case 5:
                mTitle = getString(R.string.title_section5);
                break;
            case 6:
                mTitle = getString(R.string.title_section6);
                break;
            case 7:
                mTitle = getString(R.string.title_section7);
                break;
            case 8:
                mTitle = getString(R.string.title_section8);
                break;
            case 9:
                mTitle = getString(R.string.title_section9);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    private  PowerManager.WakeLock wakelock;
    private static boolean wakelockflag=false;
    @Override
    public void onPause(){
        super.onPause();
        pauseThread=true;
        if (wakelock != null&&wakelockflag==false) {
            wakelock.release();
            wakelock = null;
        }
        Log.e("ThenHelper", "MainActivity on pause");
    }
    @Override
    public void onResume(){
        super.onResume();
        pauseThread = false;
        if(mainpageMain==null||mContext==null){
            mContext=getApplicationContext();
            Toast.makeText(MainActivity.mContext, "Error : Pls exit ThenHelper and restart it !", Toast.LENGTH_LONG);
            initThenHelper();
            Log.e("ThenHelper", "Error 0 with ThenHelper ");
        }
        if(BatteryInfoThread!=null){
            BatteryInfoThread.interrupt();
            pauseThread = false;
        }
        if(wakelock==null) {
            PowerManager pm = (PowerManager) MainActivity.mContext.getSystemService(Context.POWER_SERVICE);
            wakelock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ON_AFTER_RELEASE, "==KeepScreenOn==");
            if(null!=wakelock) {
                wakelock.acquire();
            }
        }
        Log.e("ThenHelper", "MainActivity on resume");
    }
    @Override
    public void onDestroy()
    {
        // TODO Auto-generated method stub
        super.onDestroy();
        exitThenHelper();
        setContentView(R.layout.nullxml);
        //System.exit(0);
        Log.e("ThenHelper", "*****************onDestroy ThenHelper*****************");
    }
    @Override
    public void onStop()
    {
        // TODO Auto-generated method stub
        super.onStop();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        static int flag ;
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            flag = sectionNumber;
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
           // View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            View rootView = null;
            switch(flag){
                case 1:
                    rootView = inflater.inflate(R.layout.fragment_main, container,
                            false);
                    try {
                        init_main(rootView,this);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case 2:
                    rootView = inflater.inflate(R.layout.fragment_battery, container,
                            false);
                    init_battery(rootView, this);
                    break;
                case 3:
                    rootView = inflater.inflate(R.layout.fragment_assistant, container,
                            false);
                    init_assistant(rootView, this);
                    break;
                case 4:
                    rootView = inflater.inflate(R.layout.fragment_add_contacts, container,
                            false);
                    init_addcontacts(rootView, this);
                    break;
                case 5:
                    rootView = inflater.inflate(R.layout.fragment_add_media, container,
                            false);
                    init_addmedia(rootView, this);
                    break;
                case 6:
                    rootView = inflater.inflate(R.layout.fragment_add_storage, container,
                            false);
                    init_addstorage(rootView, this);
                    break;
                case 7:
                    rootView = inflater.inflate(R.layout.fragment_sensor, container,
                            false);
                    init_sensor(rootView, this);
                    break;
                case 8:
                    rootView = inflater.inflate(R.layout.fragment_floatinfo, container,
                            false);
                    init_floatinfo(rootView, this);
                    break;
                case 9:
                    rootView = inflater.inflate(R.layout.fragment_screen_record, container,
                            false);
                    init_screenrecord(rootView, this);
                    break;
            }
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }

        private  void init_main(View view,final Fragment f) throws IOException {
            Log.e("ThenHelper", "go to main");
            //初始化类
            mainpageMain=new MainpageMain();
            batteryMain=new BatteryMain();
            contactsMain=new ContactsMain();
            mediaMain=new MediaMain();
            messageMain=new MessageMain();
            storageMain=new StorageMain();
            floatinfoMain=new FloatinfoMain(MainActivity.mContext);
            screenRecord=new ScreenRecord(MainActivity.mContext);
            sensorMain=new SensorMain();
            platform=BatteryHelper.execCommand("getprop ro.board.platform");
         //  mainActivity=new MainActivity();

            DevicesInfo=(TextView)view.findViewById(R.id.DevicesInfo);
            UpdateInfo=(Button)view.findViewById(R.id.UpdateInfo);
            Coverimage=(ImageView)view.findViewById(R.id.Coverimage);
            //封面图片
            Coverimage.setImageResource(R.drawable.coverimage);

            //弹出框
            AlertDialog.Builder builder  = new AlertDialog.Builder(f.getActivity());
            builder.setTitle("ThenHelper V1.1202e" ) ;
            builder.setMessage("1.如果出现字体重叠,请退出重进即可\n"
                    + "2.带*号的才是必填项\n"
                    +"3.在APK界面不会自动灭屏\n"
                    + "************************\n"
                    + "主要更新如下:\n"
                    + "1.assistant页面增加亮度精确调节/音量值精确查看/一直震动\n"
                    + "2.修复ScreenRecord过程中重启手机,视频无法播放的问题\n"
                    + "3.修复reliance项目因权限限制导致的ANR问题\n"
            +"4.修复添加通话记录,号码过长无法成功问题\n") ;
            builder.setIcon(R.mipmap.ico);
            builder.setPositiveButton("Know All", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    Toast.makeText(mContext, "You are smart!", Toast.LENGTH_SHORT).show();
                }
            });
            builder.show();
          //  更新设备信息
//            String strDUTinfo=mainpageMain.DUTinfo();
//            if(strDUTinfo.contains("invalid")){
//                DevicesInfo.setTextColor(Color.RED);
//            }

            DevicesInfo.setText("Click the UpdateInfo button to update");
            UpdateInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        String str = mainpageMain.DUTinfo();
                        if (str.contains("invalid")) {
                            DevicesInfo.setTextColor(Color.RED);
                        }
                        DevicesInfo.setText(str);
                        Coverimage.setVisibility(View.GONE);
                        DevicesInfo.setVisibility(View.VISIBLE);
                        Toast.makeText(f.getActivity(), "Update Device Info Successfully!", Toast.LENGTH_SHORT);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        private  void init_battery(View view,final Fragment f) {
            Log.e("ThenHelper", "go to battery");
            Iswakeup=(CheckBox)view.findViewById(R.id.iswakeup);
            SbytimeBox=(CheckBox)view.findViewById(R.id.SbytimeBox);
            BatteryInfo= (TextView) view.findViewById(R.id.BatteryInfo);
            StartTAG=(Button)view.findViewById(R.id.StartTAG);
            EndTAG=(Button)view.findViewById(R.id.EndTAG);
            StatisticsTAG=(Button)view.findViewById(R.id.StatisticsTAG);
            Selectfile = (Button) view.findViewById(R.id.Selectfile);
            StartBattery = (Button) view.findViewById(R.id.StartBattery);
            ClearBattery = (Button) view.findViewById(R.id.CleanBattery);
            Statistics=(Button) view.findViewById(R.id.Statistics);
            pickStartdate=(Button)view.findViewById(R.id.pickStartdate);
            pickEnddate=(Button)view.findViewById(R.id.pickEnddate);
            pickStarttime=(Button)view.findViewById(R.id.pickStarttime);
            pickEndtime=(Button)view.findViewById(R.id.pickEndtime);
            Starttime =(TextView) view.findViewById(R.id.Starttime);
            Endtime=(TextView)view.findViewById(R.id.Endtime);
            Startdate=(TextView)view.findViewById(R.id.Startdate);
            Enddate=(TextView)view.findViewById(R.id.Enddate);
            StatisticsInfo=(TextView) view.findViewById(R.id.StatisticsInfo);
            BatteryInterval=(EditText)view.findViewById(R.id.BatteryInterval);
            BatteryLinechart=(Button)view.findViewById(R.id.BatteryLinechart);
            alwaysscreenon=(CheckBox)view.findViewById(R.id.alwaysscreenon);
            pickStartdate.setEnabled(false);
            pickStarttime.setEnabled(false);
            pickEnddate.setEnabled(false);
            pickEndtime.setEnabled(false);
            SbytimeBox.setEnabled(false);
            //折线图
            BatteryLinechart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (SelectFile.equals("")) {
                        Toast.makeText(f.getActivity(), "Pls select file first!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    final int[] pointnum = {0};
                    final EditText points = new EditText(f.getActivity());
                    final String[] array = new String[]{"Capacity", "Current", "Voltage", "Temp", "Brightness"};
                    points.setHint("Default is 0, all data");
                    int inputType = InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_NORMAL;
                    points.setInputType(inputType);
                    new AlertDialog.Builder(f.getActivity()).setTitle("Get one data every XX data ")
                            .setIcon(R.mipmap.ico)
                            .setView(points).setPositiveButton("OK", new android.content.DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String insert = points.getText().toString();
                            if (insert.equals("")) {
                                Toast.makeText(f.getActivity(), "Line Chart will use all data.", Toast.LENGTH_SHORT).show();
                            } else {
                                pointnum[0] = Integer.parseInt(insert);
                            }
                            AlertDialog.Builder builder = new AlertDialog.Builder(f.getActivity());
                            builder.setIcon(R.mipmap.ico);
                            builder.setTitle("Data type for line chart");
                            builder.setSingleChoiceItems(array, -1, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    whichselect = whichButton;
                                }
                            });
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    Selectdatetype = array[whichselect];
                                    Intent lineIntent = new BatteryCanvas().getDIYIntent(
                                            mContext, batteryMain.BatteryMonitorPath + SelectFile, Selectdatetype, pointnum[0],
                                            Startdate.getText().toString(), Enddate.getText().toString(), Starttime.getText().toString(),
                                            Endtime.getText().toString());
                                    if (lineIntent == null) {
                                        Toast.makeText(f.getActivity(), "Do not have the time point or skip point" +
                                                " is invalid", Toast.LENGTH_SHORT).show();
                                        return;
                                    } else {
                                        startActivity(lineIntent);
                                    }
                                    Log.e("ThenHelper", "select the  date type :" + Selectdatetype);
                                }
                            });
                            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {

                                }
                            });
                            builder.create().show();
                        }
                    }).setNegativeButton("Cancel", null).show();

                }
            });


            //禁止灭屏
            alwaysscreenon.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if(b){
                        wakelockflag=true;
                       // batteryMain.keepScreenOn();
                    }else{
                        wakelockflag=false;
                       // batteryMain.releaseScreenOn();
                    }
                }
            });


            //提示
//            AlertDialog.Builder builder  = new AlertDialog.Builder(f.getActivity());
//            builder.setTitle("Read me :") ;
//            builder.setIcon(R.mipmap.ico);
//            builder.setMessage("In order to prevent the system to kill when memory is very small ("+
//            " this issue almost can't happen! ) ." +
//                    "You can uninstall ThenHelper.apk and do follow operitions :\n" +
//                    "  1.adb root\n" +
//                    "  2.adb remount\n" +
//                    "  3.adb push ThenHelper.apk \n/system/app\n" +
//                    "  4.adb reboot\n" +
//                    "It won't be killed as SYSTEM APP forever when recoding battery info!\n") ;
//            builder.setPositiveButton("OK", null);
//            builder.show();

            //是否唤醒
            Iswakeup.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked == true) {
                        batteryMain.acquireWakeLock();
                        return;
                    }
                    if (isChecked == false) {
                        batteryMain.releaseWakeLock();
                        return;
                    }
                }
            });
            //选择文件
            Selectfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    File file = new File(batteryMain.BatteryMonitorPath);
                    List<String> list = new ArrayList<String>();
                    if (!file.exists()) {
                        Toast.makeText(f.getActivity(), "does not exist" + batteryMain.BatteryMonitorPath, Toast.LENGTH_SHORT).show();
                        Log.e("ThenHelper", "does not exist BatteryMonitorPath");
                        return;
                    } else {
                        File[] files = file.listFiles();
                        for (File txt : files) {
                            list.add(txt.getName());
                        }
                    }
                    int size = list.size();
                    if (list.size() == 0) {
                        Toast.makeText(f.getActivity(), "no files in" + batteryMain.BatteryMonitorPath, Toast.LENGTH_SHORT).show();
                        Log.e("ThenHelper", "no files in BatteryMonitorPath");
                        return;
                    }
                    final String[] array = (String[]) list.toArray(new String[size]);
                    AlertDialog.Builder builder = new AlertDialog.Builder(f.getActivity());
                    builder.setIcon(R.mipmap.ico);
                    builder.setTitle("Pls select the file");
                    builder.setSingleChoiceItems(array, -1, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            whichselect = whichButton;
                        }
                    });
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            SelectFile = array[whichselect];
                            Log.e("ThenHelper", "select the file :" + SelectFile);
                            StatisticsInfo.setText("The file : " + SelectFile);
                            String[] strs = batteryMain.getDateTime(batteryMain.BatteryMonitorPath + SelectFile);
                            Startdate.setText(strs[0]);
                            Starttime.setText(strs[1].substring(0, strs[1].length() - 3));
                            Enddate.setText(strs[2]);
                            Endtime.setText(strs[3].substring(0, strs[3].length() - 3));
                            SbytimeBox.setEnabled(true);
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {

                        }
                    });
                    builder.create().show();
                }
            });
            //选择时间段
            SbytimeBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                        pickStartdate.setEnabled(true);
                        pickStarttime.setEnabled(true);
                        pickEnddate.setEnabled(true);
                        pickEndtime.setEnabled(true);
                    } else {
                        pickStartdate.setEnabled(false);
                        pickStarttime.setEnabled(false);
                        pickEnddate.setEnabled(false);
                        pickEndtime.setEnabled(false);
                    }
                }
            });
            //设置时间日期
            pickStartdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Calendar cal = Calendar.getInstance();
                    DatePickerDialog dialog = new DatePickerDialog(f.getActivity(), new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            Log.e("ThenHelper","set date");
                            String month=monthOfYear+"";
                            String day=dayOfMonth+"";
                            monthOfYear=monthOfYear+1;
                            Pattern p = Pattern
                                    .compile("^[0-9]$");
                            Matcher m = p.matcher(monthOfYear+"");
                            Matcher d =p.matcher(dayOfMonth+"");
                            if (m.matches()) {
                               month=0 + (monthOfYear + "");
                            }
                            if (d.matches()) {
                                day=0 + (dayOfMonth + "");
                            }
                            Startdate.setText(year + "-" + month + "-" + day);
                          //  pickStartdate.setBackgroundColor(Color.parseColor("#E0FFFF"));
                        }
                    },cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
                    dialog.show();
                }
            });
            pickEnddate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Calendar cal = Calendar.getInstance();
                    DatePickerDialog dialog = new DatePickerDialog(f.getActivity(), new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            Log.e("ThenHelper", "set date");
                            String month = monthOfYear + "";
                            String day = dayOfMonth + "";
                            monthOfYear = monthOfYear + 1;
                            Pattern p = Pattern
                                    .compile("^[0-9]$");
                            Matcher m = p.matcher(monthOfYear + "");
                            Matcher d = p.matcher(dayOfMonth + "");
                            if (m.matches()) {
                                month = 0 + (monthOfYear + "");
                            }
                            if (d.matches()) {
                                day = 0 + (dayOfMonth + "");
                            }
                            Enddate.setText(year + "-" + month + "-" + day);
                        }
                    }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
                    dialog.show();
                }
            });
            pickStarttime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Calendar cal = Calendar.getInstance();
                    TimePickerDialog dialog=new TimePickerDialog(f.getActivity(),new TimePickerDialog.OnTimeSetListener(){
                        @Override
                        public void onTimeSet(TimePicker view,int hour,int minute) {
                            Log.e("ThenHelper","set starttime");
                            String shour=hour+"";
                            String sminute=minute+"";
                            Pattern p = Pattern
                                    .compile("^[0-9]$");
                            Matcher m = p.matcher(hour+"");
                            Matcher d =p.matcher(minute+"");
                            if (m.matches()) {
                                shour=0 + (hour + "");
                            }
                            if (d.matches()) {
                                sminute=0 + (minute + "");
                            }
                            Starttime.setText(shour+":"+sminute);
                        }
                    },cal.get(Calendar.HOUR_OF_DAY),cal.get(Calendar.MINUTE),true);
                    dialog.show();
                }
            });
            pickEndtime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Calendar cal = Calendar.getInstance();
                    TimePickerDialog dialog = new TimePickerDialog(f.getActivity(), new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hour, int minute) {
                            Log.e("ThenHelper", "set endtime");
                            String shour = hour + "";
                            String sminute = minute + "";
                            Pattern p = Pattern
                                    .compile("^[0-9]$");
                            Matcher m = p.matcher(hour + "");
                            Matcher d = p.matcher(minute + "");
                            if (m.matches()) {
                                shour = 0 + (hour + "");
                            }
                            if (d.matches()) {
                                sminute = 0 + (minute + "");
                            }
                            Endtime.setText(shour + ":" + sminute);
                        }
                    }, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true);
                    dialog.show();
                }
            });
            //统计BYTAG
            StatisticsTAG.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (SelectFile.equals("")){
                        Toast.makeText(f.getActivity(), "Pls select file first!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    batteryMain.TAGstring="";
                    try {
                        batteryMain.StatisticsInfobyTAG(batteryMain.BatteryMonitorPath + SelectFile);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Log.e("ThenHelper", batteryMain.TAGstring);
                    if (batteryMain.TAGstring.equals("")){
                        Toast.makeText(f.getActivity(), "The selected file does not have TAG!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    AlertDialog.Builder builder  = new AlertDialog.Builder(f.getActivity());
                    builder.setTitle("Statistics Info by TAG" ) ;
                    builder.setMessage(batteryMain.TAGstring) ;
                    builder.setIcon(R.mipmap.ico);
                    builder.setPositiveButton("OK", null);
                    builder.show();
                    Toast.makeText(f.getActivity(), "Statistics by TAG successfully", Toast.LENGTH_SHORT).show();
                  //  Toast.makeText(f.getActivity(), "Statistics by TAG Successful", Toast.LENGTH_SHORT).show();
                }
            });
            //统计BYTIME
            Statistics.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.e("ThenHelper", "statistics by time");
                    if (SelectFile.equals("")){
                        Toast.makeText(f.getActivity(), "Pls select file first!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (batteryMain.Checkdatetime(Startdate.getText().toString(), Enddate.getText().toString(),
                            Starttime.getText().toString(), Endtime.getText().toString()) == false) {
                        Toast.makeText(f.getActivity(), "Pls set the time first", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    BatteryConfig config=null;
                    try {
                        config = batteryMain.StatisticsInfo(batteryMain.BatteryMonitorPath + SelectFile, Startdate.getText().toString(),
                                Enddate.getText().toString(), Starttime.getText().toString(), Endtime.getText().toString());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (config.getHascount().equals("flase")) {
                        Toast.makeText(f.getActivity(), "Does not have the time point in the file", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    String str="";
                    if (config.getHascharge().equals("true")) {
                          str=" Power Consumption : " + (config.getScapacity() - config.getEcapacity()) + "%\n" +
                                        " Avg Current : " + config.getCurrent() + "mA\n" +
                                        " Avg Voltage : " + config.getVoltage() + "mV\n" +
                                        " Max Temp : " + config.getMaxtemp() + "℃\n" +
                                        " Min Temp : " + config.getMintemp() + "℃\n" +
                                        " Warning : Device has been " +
                                        " charged during the time you input.";

                    } else {
                         str= " Power Consumption : " + (config.getScapacity() - config.getEcapacity()) + "%\n" +
                                        " Avg Current : " + config.getCurrent() + "mA\n" +
                                        " Avg Voltage : " + config.getVoltage() + "mV\n" +
                                        " Max Temp : " + config.getMaxtemp() + "℃\n" +
                                        " Min Temp : " + config.getMintemp() + "℃\n";
                    }
                    AlertDialog.Builder builder  = new AlertDialog.Builder(f.getActivity());
                    builder.setTitle("Statistics Info by Time") ;
                    builder.setMessage(str) ;
                    builder.setIcon(R.mipmap.ico);
                    builder.setPositiveButton("OK", null);
                    builder.show();
                    Toast.makeText(f.getActivity(), "Statistics by time successfully", Toast.LENGTH_SHORT).show();
                }

            });
            //启动电池信息更新
            BatteryInfoThread=new BatteryInfoThread();
            BatteryInfoThread.start();
            //开始TAG
            StartTAG.setEnabled(false);
            EndTAG.setEnabled(false);
            StartTAG.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final EditText name = new EditText(f.getActivity());
                    new AlertDialog.Builder(f.getActivity()).setTitle("Pls enter start TAG")
                            .setIcon(R.mipmap.ico)
                            .setView(name).setPositiveButton("OK", new android.content.DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String insert = name.getText().toString();
                            if (insert.equals("")) {
                                Toast.makeText(f.getActivity(), "Pls enter start TAG", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            BatteryHelper.InsertTAG("Start TAG," + insert);
                            StartTAG.setEnabled(false);
                            EndTAG.setEnabled(true);
                            Toast.makeText(f.getActivity(), "Insert start TAG successfully", Toast.LENGTH_SHORT).show();
                        }
                    }).setNegativeButton("Cancel", null).show();
                }
            });
            //结束TAG
            EndTAG.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final EditText name=new EditText(f.getActivity());
                    new AlertDialog.Builder(f.getActivity()).setTitle("Pls enter end TAG")
                            .setIcon(R.mipmap.ico)
                            .setView(name).setPositiveButton("OK", new android.content.DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String insert = name.getText().toString();
                            if (insert.equals("")) {
                                Toast.makeText(f.getActivity(), "Pls enter end TAG", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            BatteryHelper.InsertTAG("End TAG,"+insert);
                            StartTAG.setEnabled(true);
                            EndTAG.setEnabled(false);
                            Toast.makeText(f.getActivity(), "Insert end TAG successfully", Toast.LENGTH_SHORT).show();
                        }
                    }).setNegativeButton("Cancel", null).show();
                }
            });
            //判断是否已经启动
            if(batteryMain.isServiceRunning(mContext,"com.battery.BatteryService")){
                StartBatteryrunable = true;
                StartBattery.setText("Stop Record");
                ClearBattery.setEnabled(false);
                StartTAG.setEnabled(true);
                Toast.makeText(f.getActivity(), "BatteryService has been started", Toast.LENGTH_SHORT).show();
                Log.e("ThenHelper", "com.battery.BatteryService has been started ");
            }
            StartBattery.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  if (StartBatteryrunable == false) {
                      if(BatteryInterval.getText().toString().equals("")){
                          Toast.makeText(f.getActivity(), "Record starts with interval time : 5000ms ", Toast.LENGTH_SHORT).show();
                      }else if(Integer.parseInt(BatteryInterval.getText().toString())<1000){
                          Toast.makeText(f.getActivity(), "Interval time must be more than 1000ms", Toast.LENGTH_SHORT).show();
                          return;
                      }else {
                          batteryInterval = Integer.parseInt(BatteryInterval.getText().toString());
                      }
                      StartBatteryrunable = true;
                      String BatteryMonitorTXT=batteryMain.startRecord();
                      BatteryHelper.WriteFileTitle();
                      Toast.makeText(f.getActivity(), "Start to record , temp file is "+BatteryMonitorTXT, Toast.LENGTH_LONG).show();
                      StartBattery.setText("Stop Record");
                      ClearBattery.setEnabled(false);
                      StartTAG.setEnabled(true);
                      BatteryInterval.setEnabled(false);
                      Intent localIntent = new Intent(mContext,BatteryService.class);
                      MainActivity.mContext.stopService(localIntent);
                      Bundle bundle = new Bundle();
                      bundle.putStringArray("batteryInterval", new String[]{batteryInterval + ""});
                      Intent intent = new Intent(mContext,BatteryService.class);
                      intent.putExtras(bundle);
                      mContext.startService(intent);
                      Log.e("ThenHelper", "start BatteryService ");

//                      BatteryRecordThreadrunable = true;
//                      //StartBattery.setBackgroundColor(Color.parseColor("#E0FFFF"));
//                      String BatteryMonitorTXT=batteryMain.startRecord();
//                      BatteryRecordThread = new BatteryRecordThread();
//                      BatteryHelper.WriteFileTitle();
//                      BatteryRecordThread.setDaemon(true);
//                      BatteryRecordThread.start();
//                      StartBattery.setText("Stop Record");
//                      Toast.makeText(f.getActivity(), "Start to record battery info, record keeping in "+BatteryMonitorTXT, Toast.LENGTH_LONG).show();
//                      ClearBattery.setEnabled(false);
//                      StartTAG.setEnabled(true);
                      return;
                  }
                  if (StartBatteryrunable == true) {
                      StartBatteryrunable = false;
                      StartBattery.setText("Start Record");
                      ClearBattery.setEnabled(true);
                      StartTAG.setEnabled(false);
                      EndTAG.setEnabled(false);
                      BatteryInterval.setEnabled(true);
                      Intent localIntent = new Intent(mContext,BatteryService.class);
                      mContext.stopService(localIntent);
                      String newfile=batteryMain.renameRecord();
                      Toast.makeText(f.getActivity(), "Stop to record, and the file renames to "+newfile, Toast.LENGTH_LONG).show();
                      batteryMain.Scanner(newfile);
                      Log.e("ThenHelper", "BatteryService is running,stop it. " );

//                      mHandler.removeCallbacks(BatteryRecordThread);
//                      BatteryRecordThreadrunable = false;
//                      BatteryRecordThread.interrupt();
//                      StartBattery.setText("Start Record");
//                      Toast.makeText(f.getActivity(), "Stop to record battery info", Toast.LENGTH_SHORT).show();
//                      ClearBattery.setEnabled(true);
//                      StartTAG.setEnabled(false);
//                      EndTAG.setEnabled(false);
                      return;
                  }
              }
          });
        ClearBattery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("ThenHelper", "delete record files");
                AlertDialog.Builder builder = new AlertDialog.Builder(f.getActivity());
                builder.setIcon(R.mipmap.ico);
                builder.setTitle("Delete all files?").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Log.e("ThenHelper","delete files");
                        int filecount = batteryMain.delteRecord();
                        Toast.makeText(f.getActivity(), "Total delete " + filecount + " records", Toast.LENGTH_SHORT).show();
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
        private void init_assistant(View view,final Fragment f) {
            Log.e("ThenHelper", "go to assistant");
            StartLog = (Button) view.findViewById(R.id.StartLog);
            gifgame = (Button) view.findViewById(R.id.gifgame);
            Root=(Button) view.findViewById(R.id.Root);
            createIMEI=(Button) view.findViewById(R.id.createIMEI);
            createMAC=(Button) view.findViewById(R.id.createMAC);
            setbrightness=(Button) view.findViewById(R.id.setbrightness);
            getroot=(Button)view.findViewById(R.id.getroot);
            checkvolume=(Button) view.findViewById(R.id.checkvolume);
            crashtest=(Button) view.findViewById(R.id.crashtest);
            vibrator=(Button) view.findViewById(R.id.vibrator);

            //震动
            vibrator.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (isvibrated==false){
                        isvibrated=true;
                        assistantMain.vibrator();
                        vibrator.setText("Virator on");
                    }else {
                        isvibrated=false;
                        assistantMain.cancelvibrator();
                        vibrator.setText("Virator off");
                    }
                }
            });
            //crash测试
            crashtest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder  = new AlertDialog.Builder(f.getActivity());
                    builder.setTitle("Do you want to crash ThenHelper?" ) ;
                    builder.setIcon(R.mipmap.ico);
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            Log.e("ThenHelper","crash diy");
                            int a=0;
                            int b=1;
                            int c=b/a;
                        }
                    });
                    builder.setNegativeButton("Cancle",null);
                    builder.show();
                }
            });
            //查看音量
            checkvolume.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder  = new AlertDialog.Builder(f.getActivity());
                    builder.setTitle("Volume Value" ) ;
                    builder.setMessage(assistantMain.getVolume()) ;
                    builder.setIcon(R.mipmap.ico);
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                        }
                    });
                    builder.show();
                }
            });
            //设置屏幕亮度
            setbrightness.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    final EditText brightness = new EditText(f.getActivity());
                    int inputType = InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_NORMAL;
                    brightness.setInputType(inputType);
                    int currentvalueint=assistantMain.screenBrightness_check();
                    int currentvalue=(int)(((float) currentvalueint / 255)*100);
                    new AlertDialog.Builder(f.getActivity()).setTitle("Pls enter value (0-100%), Now is "+currentvalue+"%,"+currentvalueint)
                            .setIcon(R.mipmap.ico)
                            .setView(brightness).setPositiveButton("OK", new android.content.DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            int value=Integer.parseInt(brightness.getText().toString());
                            if(value<=100&&value>=0) {
                                value=value*255/100;
                                Log.e("ThenHelper","set brightness is "+value);
                                assistantMain.setScreenBritness(value);
                                return;
                            }else {
                                Toast.makeText(f.getActivity(), "Pls enter 0-100% brightness!", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                    }).setNegativeButton("Cancel", null).show();
                }
            });
            //获取root权限
            getroot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    boolean isroot=assistantMain.RootCommand("chmod 777 "+mContext.getPackageCodePath());
                    if(isroot){
                        Toast.makeText(f.getActivity(), "ThenHelper ask for root...", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(f.getActivity(), "ThenHelper can't ask for root...", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            //GIF游戏
            gifgame.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                      //  assistantMain.execCommand("su");
                        String a=assistantMain.execCommand("cat /sys/class/leds/lcd-backlight/brightness");
                        Log.e("ThenHelper", "  "+a);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            //创建一个MAC
            createMAC.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder  = new AlertDialog.Builder(f.getActivity());
                    builder.setTitle("new MAC" ) ;
                    builder.setMessage(assistantMain.createMAC()) ;
                    builder.setIcon(R.mipmap.ico);
                    builder.setPositiveButton("OK", null);
                    builder.show();
                }
            });
            //创建一个IMEI
            createIMEI.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder  = new AlertDialog.Builder(f.getActivity());
                    builder.setTitle("new IMEI" ) ;
                    builder.setMessage(assistantMain.createIMEI()) ;
                    builder.setIcon(R.mipmap.ico);
                    builder.setPositiveButton("OK", null);
                    builder.show();
                }
            });
            //调出ROOT界面
            Root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                //    OpenAPPs.startAPPs(mContext,"com.goldsand.usertoroot");
                //    OpenAPPs.startAPPs(mContext,"com.android.mms");
//                    Intent phoneIntent = new Intent("android.intent.action.CALL",
//                            Uri.parse("tel:" +"*#*#620000#*#*"));
//                    //启动
//                    startActivity(phoneIntent);
                    //messageMain.insertMmsWithSub(1);
                //    messageMain.insterMms(1);
                //    messageMain.insterMms(2);


                }
            });


            StartLog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (StartLogrunable == false) {
                        StartLogrunable = true;
                        StartLog.setText("正在抓取");
                        catchlogMain.startRecord();
                        StartLogThreadrunable=true;
                        CatchLogThread=new CatchLogThread();
                        CatchLogThread.setDaemon(true);
                        CatchLogThread.start();
                        return;
                    }
                    if (StartLogrunable == true) {
                        StartLogrunable = false;
                        StartLog.setText("开始抓取");
                       // CatchLogThread.interrupt();
                        StartLogThreadrunable=false;
                        return;
                    }
                }
            });

        }
        private void init_addcontacts(View view,final Fragment f) {
            Log.e("ThenHelper","go to addcontacts");
            //联系人
            getcontactnum=(Button)view.findViewById(R.id.getcontactnum);
            showcontactnum=(TextView)view.findViewById(R.id.showcontactnum);
            deletecontact=(Button)view.findViewById(R.id.deletecontact);
            mbtnadd = (Button) view.findViewById(R.id.contacts_button_add);
            meditcontactnumber = (EditText) view.findViewById(R.id.contacts_edit_number);
            mcheckboxphoto = (CheckBox) view.findViewById(R.id.contacts_checkBox_photo);
            mcheckboxfavorite = (CheckBox) view.findViewById(R.id.contacts_checkBox_favorite);
            Contactprogressbar=(ProgressBar)view.findViewById(R.id.ContactprogressBar);
            ContactProgressnum=(TextView)view.findViewById(R.id.ContactProgressnum);

            //统计联系人
            totalcontact=contactsMain.Totalcontact();
            showcontactnum.setText("Total : " + totalcontact);
            getcontactnum.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int i = contactsMain.Totalcontact();
                    showcontactnum.setText("Total : " + i);
                    Log.e("ThenHelper", "Total " + i + " contacts");
                    Toast.makeText(f.getActivity(), "Total "+i+" contacts", Toast.LENGTH_SHORT).show();
                }
            });
           //删除联系人
            deletecontact.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(f.getActivity());
                    builder.setIcon(R.mipmap.ico);
                    builder.setTitle("Delete all contacts?").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            int i = contactsMain.deletecontact();
                            Log.e("ThenHelper", "Delete " + i + " contacts");
                            Toast.makeText(f.getActivity(), "Delete " + i + " contacts", Toast.LENGTH_SHORT).show();
                            totalcontact = contactsMain.Totalcontact();
                            showcontactnum.setText("Total : " + totalcontact);
                        }
                    }).setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            //选择了取消
                        }
                    });
                    builder.create().show();
                }
            });
            //添加联系人
            mbtnadd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (AddContactsrunable==false){
                        contactsnum = meditcontactnumber.getText().toString();
                        if (contactsnum.equals("")||Integer.parseInt(contactsnum)==0) {
                            Toast.makeText(f.getActivity(), "Pls enter the number", Toast.LENGTH_SHORT).show();
                            return ;
                        }else if (Integer.parseInt(contactsnum)>500) {
                            Toast.makeText(f.getActivity(), "Pls enter the number( <=500 )", Toast.LENGTH_SHORT).show();
                            return ;
                        }
                        meditcontactnumber.setEnabled(false);
                        deletecontact.setEnabled(false);
                        Contactprogressbar.setMax(Integer.parseInt(contactsnum));
                        Contactprogressbar.setProgress(0);
                        AddContactsrunable=true;
                        AddContactsThreadrunable =true;
                    contactsMain.initExtradata();
                    contactsMain.getRes();
                        Log.e("ThenHelper", "Insert contacts start");
                        InsertcontactsThread = new InsertcontactsThread();
                        InsertcontactsThread.start();
                        mbtnadd.setText("Stop");
                        Addcalllog.setEnabled(false);
                        return;
                    }
                    if(AddContactsrunable==true){
                        AddContactsrunable=false;
                        AddContactsThreadrunable=false;
                      //  InsertcontactsThread.interrupt();
                        mbtnadd.setText("Add");
                        Addcalllog.setEnabled(true);
                        meditcontactnumber.setEnabled(true);
                        deletecontact.setEnabled(true);
                        Log.e("ThenHelper", "Insert contacts stop");
                        return;
                    }
                }
            });

            //通话记录
            deletecall=(Button)view.findViewById(R.id.deletecall);
            getcallnum=(Button)view.findViewById(R.id.getcallnum);
            showcallnum=(TextView)view.findViewById(R.id.showcallnum);
            Addcalllog = (Button) view.findViewById(R.id.addcalllog);
            calllognum = (EditText) view.findViewById(R.id.calllognum);
            calllogphone = (EditText) view.findViewById(R.id.phonenum);
            call_category = (Spinner) view.findViewById(R.id.calllogspinner);
            List<String> spinnerlist = new ArrayList<>();
            ArrayAdapter<String> adapter;
            spinnerlist.add("Dialed calls");
            spinnerlist.add("Missed calls");
            spinnerlist.add("Received calls");
            adapter = new ArrayAdapter<String>(f.getActivity(),android.R.layout.simple_spinner_item, spinnerlist);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            call_category.setAdapter(adapter);
            call_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View arg1, int pos, long id) {
                    String category = parent.getItemAtPosition(pos).toString();
                    if (category.equals("Dialed calls")) {
                        calllogtype = 2;
                        return;
                    } else if (category.equals("Missed calls")) {
                        calllogtype = 3;
                        return;
                    } else if (category.equals("Received calls")) {
                        calllogtype = 1;
                        return;
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                }
            });

            //删除通话记录
            deletecall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(f.getActivity());
                    builder.setIcon(R.mipmap.ico);
                    builder.setTitle("Delete all call history?").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            int i=contactsMain.Deletecallnum();
                            Log.e("ThenHelper","Delete "+i+" call history.");
                            Toast.makeText(f.getActivity(), "Delete "+i+" call history.", Toast.LENGTH_SHORT).show();
                            totalcalllog=contactsMain.Totalcallnum();
                            showcallnum.setText("Total : " + totalcalllog);
                        }
                    }).setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            //选择了取消
                        }
                    });
                    builder.create().show();
                }
            });

            //统计通话记录
            totalcalllog=contactsMain.Totalcallnum();
            showcallnum.setText("Total : " + totalcalllog);
            getcallnum.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int i=contactsMain.Totalcallnum();
                    showcallnum.setText("Total : "+i);
                    Log.e("ThenHelper", "Total " + i + " call history.");
                    Toast.makeText(f.getActivity(), "Total "+i+" call history.", Toast.LENGTH_SHORT).show();
                }
            });
            //添加通话记录
            Addcalllog.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (AddCalllogrunable == false) {
                        callnum = calllognum.getText().toString();
                        callphone = calllogphone.getText().toString();
                        if (callnum.equals("") || Integer.parseInt(callnum) == 0 || callphone.equals("")) {
                            Toast.makeText(f.getActivity(), "Pls enter the number or phone number", Toast.LENGTH_SHORT).show();
                            return;
                        } else if (Integer.parseInt(callnum) > 500) {
                            Toast.makeText(f.getActivity(), "Pls enter the number( <=500 )", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        calllognum.setEnabled(false);
                        calllogphone.setEnabled(false);
                        mbtnadd.setEnabled(false);
                        deletecall.setEnabled(false);
                        Contactprogressbar.setMax(Integer.parseInt(callnum));
                        Contactprogressbar.setProgress(0);
                        AddCalllogrunable = true;
                        AddCalllogThreadrunable = true;
                        Log.e("ThenHelper", "Insert call log start");
                        InsertcalllogThread = new InsertcalllogThread();
                        InsertcalllogThread.start();
                        Addcalllog.setText("Stop");
                        return;
                    }
                    if (AddCalllogrunable == true) {
                        AddCalllogrunable = false;
                        AddCalllogThreadrunable = false;
                      //  InsertcalllogThread.interrupt();
                        Addcalllog.setText("Add");
                        calllognum.setEnabled(true);
                        calllogphone.setEnabled(true);
                        mbtnadd.setEnabled(true);
                        deletecall.setEnabled(true);
                        Log.e("ThenHelper", "Insert call log stop");
                        return;
                    }
                }
            });


        }
        private void init_addmedia(View view,final Fragment f) {
            Log.e("ThenHelper","go to addmedia");
            //短信**************************************************************
            //初始化
            addmessage=(Button)view.findViewById(R.id.addmessage);
            Messagenum=(EditText)view.findViewById(R.id.Messagenum);
            Messagephone=(EditText)view.findViewById(R.id.Messagephone);
            Messagecontent=(EditText)view.findViewById(R.id.Messagecontent);
            Issamesession=(CheckBox)view.findViewById(R.id.Issamesession);
            Messageisread=(RadioGroup)view.findViewById(R.id.Messageisread);
            ReadMessage=(RadioButton)view.findViewById(R.id.Messageread);
            UnreadMessage=(RadioButton)view.findViewById(R.id.Messageunread);
            Messagetype=(RadioGroup)view.findViewById(R.id.Messagetype);
            ReceiveMessage=(RadioButton)view.findViewById(R.id.ReceiveMessage);
            SendMessage=(RadioButton)view.findViewById(R.id.SendMessage);
            messagetypeGroup=(RadioGroup)view.findViewById(R.id.messagetypeGroup);
            SMSradio=(RadioButton)view.findViewById(R.id.SMSradio);
            MMSradio=(RadioButton)view.findViewById(R.id.MMSradio);
            showmessagenum=(TextView)view.findViewById(R.id.showmessagenum);
            getmessagenum=(Button)view.findViewById(R.id.getmessagenum);
            deletemessage=(Button)view.findViewById(R.id.deletemessage);

            //删除信息
            deletemessage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(f.getActivity());
                    builder.setIcon(R.mipmap.ico);
                    builder.setTitle("Delete the all message ?").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            int i= messageMain.deletemessage();
                            Log.e("ThenHelper","Delete "+i+" messages");
                            Toast.makeText(f.getActivity(), "Delete "+i+" messages", Toast.LENGTH_SHORT).show();
                            totalmessage=messageMain.Totalmessage();
                            showmessagenum.setText("Total : " + totalmessage);
                        }
                    }).setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            //选择了取消
                        }
                    });
                    builder.create().show();
                }
            });
            //统计信息
            totalmessage=messageMain.Totalmessage();
            showmessagenum.setText("Total : " + totalmessage);
            getmessagenum.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int i = messageMain.Totalmessage();
                    showmessagenum.setText("Total : " + i);
                    Log.e("ThenHelper", "Total " + i + " messages.");
                    Toast.makeText(f.getActivity(), "Total " + i + " messages.", Toast.LENGTH_SHORT).show();
                }
            });
            //短信还是彩信
            SMSradio.setChecked(true);
            messagetypeGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int i) {
                    if (i == SMSradio.getId()) {
                        SMSorMMS=SMSradio.getText().toString();
                        Messagecontent.setEnabled(true);
                        if(SMSorMMS.equals("SMS")) {
                            ReadMessage.setEnabled(true);
                            UnreadMessage.setEnabled(true);
                        }
                        return;
                    }
                    if (i == MMSradio.getId()) {
                        SMSorMMS=MMSradio.getText().toString();
                        Messagecontent.setEnabled(false);
                        ReadMessage.setEnabled(false);
                        UnreadMessage.setEnabled(false);
                        return;
                    }
                }
            });

            //Message类型
            ReceiveMessage.setChecked(true);
            ReadMessage.setChecked(true);
            Messagetype.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int i) {
                    if (i == ReceiveMessage.getId()) {
                        SelectMessagetype = ReceiveMessage.getText().toString();
                        if(SMSorMMS.equals("SMS")) {
                            ReadMessage.setEnabled(true);
                            UnreadMessage.setEnabled(true);
                        }
                        return;
                    }
                    if (i == SendMessage.getId()) {
                        SelectMessagetype = SendMessage.getText().toString();
                        ReadMessage.setEnabled(false);
                        UnreadMessage.setEnabled(false);
                        return;
                    }
                }
            });
            //是否已读
            Messageisread.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int i) {
                    if (i == ReadMessage.getId()) {
                        MessageReadflag = ReadMessage.getText().toString();
                        return;
                    }
                    if (i == UnreadMessage.getId()) {
                        MessageReadflag = UnreadMessage.getText().toString();
                        return;
                    }
                }
            });
            //判断是否相同对话
            Messagephone.setEnabled(false);
            Issamesession.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b == true) {
                        Messagephone.setEnabled(true);
                        Missame = true;
                    }
                    if (b == false) {
                        Messagephone.setEnabled(false);
                        Missame = false;
                    }
                }
            });
            //添加短信按钮
            addmessage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    boolean isdefault = checkDefaultMmsSettings();
                    if (isdefault == false) {
                        return;
                    }
                    if (AddSMSrunable == false) {
                        Mnum = Messagenum.getText().toString();
                        Mphone = Messagephone.getText().toString();
                        if (Mnum.equals("") || Integer.parseInt(Mnum) == 0) {
                            Toast.makeText(f.getActivity(), "Pls enter the number of message", Toast.LENGTH_SHORT).show();
                            return;
                        } else if (Integer.parseInt(Mnum) > 300) {
                            Toast.makeText(f.getActivity(), "Pls enter the number( <=300 )", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (Issamesession.isChecked() == true && Mphone.equals("")) {
                            Toast.makeText(f.getActivity(), "Pls enter the phone numbers", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Messagenum.setEnabled(false);
                        Addmedia.setEnabled(false);
                        Messagephone.setEnabled(false);
                        Issamesession.setEnabled(false);
                        Messagecontent.setEnabled(false);
                        Mediaprogressbar.setMax(Integer.parseInt(Mnum));
                        Mediaprogressbar.setProgress(0);
                        AddSMSrunable = true;
                        AddSMSThreadrunable = true;
                        Log.e("ThenHelper", "Insert SMS start");
                        InsertSMSThread = new InsertSMSThread();
                        InsertSMSThread.start();
                        addmessage.setText("Stop");
                        return;
                    }
                    if (AddSMSrunable == true) {
                        AddSMSrunable = false;
                        AddSMSThreadrunable = false;
                        //   InsertSMSThread.interrupt();
                        addmessage.setText("Add");
                        Messagenum.setEnabled(true);
                        if (Issamesession.isChecked()) {
                            Messagephone.setEnabled(true);
                        }
                        Issamesession.setEnabled(true);
                        Addmedia.setEnabled(true);
                        Messagecontent.setEnabled(true);
                        Log.e("ThenHelper", "Insert SMS stop");
                        return;
                    }

                }
            });



            //媒体***************************************************************
            //初始化
            Progressnum=(TextView)view.findViewById(R.id.MediaProgressnum);
            Addmedia=(Button)view.findViewById(R.id.AddMedia);
            Deletemedia=(Button)view.findViewById(R.id.DeleteMedia);
            mediaRadioGroup=(RadioGroup)view.findViewById(R.id.MediaradioGroup);
            medianum=(EditText)view.findViewById(R.id.medianum);
            Mediaprogressbar=(ProgressBar)view.findViewById(R.id.MediaprogressBar);
            Musicradio = (RadioButton) view.findViewById(R.id.Musicradio);
            Videoradio = (RadioButton) view.findViewById(R.id.Videoradio);
            Pictureradio = (RadioButton) view.findViewById(R.id.Pictureradio);
            showmedianum=(TextView)view.findViewById(R.id.showmedianum);
            getmedianum=(Button)view.findViewById(R.id.getmedianum);
            //显示媒体数量
            totalmedia=mediaMain.Totalmedia();
            showmedianum.setText("Total : "+totalmedia);
            getmedianum.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int i = mediaMain.Totalmedia();
                    showmedianum.setText("Total : " + i);
                    Log.e("ThenHelper", "Total " + i + selectedmedia + " .");
                    Toast.makeText(f.getActivity(), "Total " + i + " " + selectedmedia + "s.", Toast.LENGTH_SHORT).show();
                }
            });


            Pictureradio.setChecked(true);
            mediaRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    if (checkedId == Pictureradio.getId()) {
                        selectedmedia = Pictureradio.getText().toString();
                        int i = mediaMain.Totalmedia();
                        showmedianum.setText("Total : " + i);
                        return;
                    }
                    if (checkedId == Videoradio.getId()) {
                        selectedmedia = Videoradio.getText().toString();
                        int i = mediaMain.Totalmedia();
                        showmedianum.setText("Total : " + i);
                        return;
                    }
                    if (checkedId == Musicradio.getId()) {
                        selectedmedia = Musicradio.getText().toString();
                        int i = mediaMain.Totalmedia();
                        showmedianum.setText("Total : " + i);
                        return;
                    }
                }
            });

            Addmedia.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (AddMediarunable == false) {
                        Progressnum.setText("Current Progress : 0");
                        addmedianum = medianum.getText().toString();
                        if (addmedianum.equals("") || Integer.parseInt(addmedianum) == 0) {
                            Toast.makeText(f.getActivity(), "Pls enter the number", Toast.LENGTH_SHORT).show();
                            medianum.setEnabled(true);
                            return;
                        } else if (Integer.parseInt(addmedianum) > 500) {
                            medianum.setEnabled(true);
                            Toast.makeText(f.getActivity(), "Pls enter the number( <=500 )", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        mediaMain.CreateFileMedia();
//                        if(a==true) {
//                            addmedianum = (Integer.parseInt(addmedianum) - 1) + "";
//                            Mediacount=1;
//                        }else {
//                            Mediacount=1;
//                        }
//                        if(Integer.parseInt(addmedianum) == 0){
//                            Mediaprogressbar.setMax(1);
//                            Mediaprogressbar.setProgress(1);
//                            Progressnum.setText("Current Progress : " + 1);
//                            Toast.makeText(f.getActivity(), "Create " + selectedmedia + " files successfully!", Toast.LENGTH_SHORT).show();
//                            return;
//                        }
                        Mediaprogressbar.setMax(Integer.parseInt(addmedianum));
                        Mediaprogressbar.setProgress(0);
                        medianum.setEnabled(false);
                        addmessage.setEnabled(false);
                        AddMediarunable = true;
                        AddMediaThreadrunable = true;
                        Log.e("ThenHelper", "Insert media start");
                        AddmediaThread = new AddmediaThread();
                        AddmediaThread.start();
                        Addmedia.setText("Stop");
                        Deletemedia.setEnabled(false);
                        return;
                    }
                    if (AddMediarunable == true) {
                        AddMediarunable = false;
                        AddMediaThreadrunable = false;
                        //   AddmediaThread.interrupt();
                        Addmedia.setText("Add");
                        medianum.setEnabled(true);
                        Deletemedia.setEnabled(true);
                        addmessage.setEnabled(true);
                        Log.e("ThenHelper", "Insert media stop");
                        return;
                    }
                }
            });
            //删除创建的媒体文件
            Deletemedia.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        final String[] array = new String[]{"Created by ThenHelper", "All (Media in " +
                                "SDcard can't be deleted but disappear until reboot)"};
                    AlertDialog.Builder builder = new AlertDialog.Builder(f.getActivity());
                    builder.setIcon(R.mipmap.ico);
                    builder.setTitle("Which one do you want to delete ?");
                    builder.setSingleChoiceItems(array, -1, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            whichselect = whichButton;
                        }
                    });
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            if (array[whichselect].equals("Created by ThenHelper")) {
                                Deletemedia.setEnabled(false);
                                int filenum = mediaMain.deletemedia(selectedmedia);
                                //totalmedia=mediaMain.Totalmedia();
                                showmedianum.setText("Total : --");
                                Toast.makeText(f.getActivity(), "Delete ThenHelper created " + filenum + " " + selectedmedia + " files!", Toast.LENGTH_SHORT).show();
                                Deletemedia.setEnabled(true);
                                Log.e("ThenHelper", "delete " + selectedmedia + " successfully");
                            } else if (array[whichselect].contains("All")) {
                                Deletemedia.setEnabled(false);
                                int filenum = mediaMain.deletemediaall();
                                totalmedia = mediaMain.Totalmedia();
                                showmedianum.setText("Total : " + totalmedia);
                                Toast.makeText(f.getActivity(), "Delete all " + filenum + " " + selectedmedia + " files! " +
                                        "Media in SDcard can't be deleted but disappears until reboot", Toast.LENGTH_SHORT).show();
                                Deletemedia.setEnabled(true);
                                Log.e("ThenHelper", "delete all " + selectedmedia + " successfully");
                            } else {
                                Toast.makeText(f.getActivity(), "Pls select one item!", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {

                        }
                    });
                    builder.create().show();
                }
            });
        }
        private void init_addstorage(View view,final Fragment f) {
            Log.e("ThenHelper", "go to addstorage");
            //初始化
            StorageInfo=(TextView)view.findViewById(R.id.StorageInfo);
            availablesize=(TextView)view.findViewById(R.id.availablesize);
            FillStorage=(Button)view.findViewById(R.id.FillStorage);
            CleanStorage=(Button)view.findViewById(R.id.CleanStorage);
            StorageSpeed=(Button)view.findViewById(R.id.StorageSpeed);
            AddStorageBar=(ProgressBar)view.findViewById(R.id.AddstorageBar);
            StorageRadioGroup=(RadioGroup)view.findViewById(R.id.StorageGroup);
            SDcardradio = (RadioButton) view.findViewById(R.id.SDcardradio);
            Storageradio = (RadioButton) view.findViewById(R.id.Storageradio);
            Dataradio=(RadioButton) view.findViewById(R.id.Dataradio);
            storagesizetype=(RadioGroup)view.findViewById(R.id.storagesizetype);
            KBradio = (RadioButton) view.findViewById(R.id.KBradio);
            MBradio = (RadioButton) view.findViewById(R.id.MBradio);
            GBradio = (RadioButton) view.findViewById(R.id.GBradio);
            StorageDIYSize=(CheckBox)view.findViewById(R.id.storageDIYSize);
            retainsizebox=(CheckBox)view.findViewById(R.id.retainsizebox);
            storagesize=(EditText)view.findViewById(R.id.storagesize);
            retainsize=(EditText)view.findViewById(R.id.retainsize);

            //设置速度
            StorageSpeed.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final EditText writespeed = new EditText(f.getActivity());
                    int inputType = InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_NORMAL;
                    writespeed.setInputType(inputType);
                    new AlertDialog.Builder(f.getActivity()).setTitle("Pls enter write speed (MB)")
                            .setIcon(R.mipmap.ico)
                            .setView(writespeed).setPositiveButton("OK", new android.content.DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            int value=Integer.parseInt(writespeed.getText().toString());
                            if(value<=50&&value>=1) {
                                storageMain.storagebytes=value*1024*1024;
                                StorageSpeed.setText(value+"MB");
                                return;
                            }else {
                                Toast.makeText(f.getActivity(), "Suggest 1-30MB", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                    }).setNegativeButton("Cancel", null).show();
                }
            });
            //保留剩余空间
            retainsize.setEnabled(false);
            retainsizebox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if(b){
                        retainsize.setEnabled(true);
                    }else {
                        retainsize.setEnabled(false);
                    }
                }
            });
            //自定义填充存储大小
            storagesize.setEnabled(false);
            StorageDIYSize.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked == true) {
                        storagesize.setEnabled(true);
                    }
                    if (isChecked == false) {
                        storagesize.setEnabled(false);
                    }
                }
            });
            MBradio.setChecked(true);
//            KBradio.setEnabled(false);
//            MBradio.setEnabled(false);
//            GBradio.setEnabled(false);
            storagesizetype.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    if (checkedId == KBradio.getId()) {
                        selectedstoragetype = KBradio.getText().toString();
                        Log.e("ThenHelper", "selectedstoragetype KB");
                        return;
                    }
                    if (checkedId == MBradio.getId()) {
                        selectedstoragetype = MBradio.getText().toString();
                        Log.e("ThenHelper", "selectedstoragetype MB");
                        return;
                    }
                    if (checkedId == GBradio.getId()) {
                        selectedstoragetype = GBradio.getText().toString();
                        Log.e("ThenHelper", "selectedstoragetype GB");
                        return;
                    }

                }
            });

            //填充路径选择
            Dataradio.setChecked(true);
            StorageRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    if (checkedId == SDcardradio.getId()) {
                        selectedstorage = SDcardradio.getText().toString();
                        if(storageMain.hasSDcard()){
                            storageMain.storagebytes=15*1024*1024;
                            StorageSpeed.setText("15MB");
                            storageMain.showinfo();
                            Log.e("ThenHelper", "selectedstorage SDcard");
                            return;
                        }
                        Dataradio.setChecked(true);
                        Toast.makeText(f.getActivity(), "No SDcard or your UE can't support this feature!", Toast.LENGTH_SHORT).show();
                        Log.e("ThenHelper", "does not have SDcard");
                        return;
                    }
                    if (checkedId == Storageradio.getId()) {
                        selectedstorage = Storageradio.getText().toString();
                        if(storageMain.hasPhonestorage()){
                            storageMain.storagebytes=25*1024*1024;
                            StorageSpeed.setText("25MB");
                            storageMain.showinfo();
                            Log.e("ThenHelper", "selectedstorage Phone Storage");
                            return;
                        }
                        Dataradio.setChecked(true);
                        Toast.makeText(f.getActivity(), "Does not have inner SDcard!", Toast.LENGTH_SHORT).show();
                        Log.e("ThenHelper", "does not have phone storage");
                        return;
                    }
                    if (checkedId == Dataradio.getId()) {
                        selectedstorage = Dataradio.getText().toString();
                        storageMain.storagebytes=25*1024*1024;
                        StorageSpeed.setText("25MB");
                        storageMain.showinfo();
                        Log.e("ThenHelper", "selectedstorage Data");
                        return;
                    }
                }
            });


            storageMain.showinfo();

            FillStorage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    StorageConfig storageconfig = storageMain.getstorageInfo();
                    if (retainsizebox.isChecked()){
                        if(retainsize.getText().toString().equals("")||retainsize.getText().toString().equals("0")){
                            Toast.makeText(f.getActivity(), "Pls enter the size you want!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    if (StorageDIYSize.isChecked()){
                        if(storagesize.getText().toString().equals("")||storagesize.getText().toString().equals("0")){
                            Toast.makeText(f.getActivity(), "Pls enter the size you want!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if(Integer.parseInt(storagesize.getText().toString())>4&&selectedstoragetype.equals("GB")){
                            Toast.makeText(f.getActivity(), "Pls enter the size ( =<4GB )", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        DIYsizenum=Integer.parseInt(storagesize.getText().toString());
                        if(selectedstoragetype.equals("GB")){
                            DIYsizenum=DIYsizenum*100;
                        }
                    }
                    if(selectedstoragetype.equals("KB")){
                        retainsizenum=1;
                    }else if(selectedstoragetype.equals("MB")){
                        retainsizenum=1024;
                    }else if(selectedstoragetype.equals("GB")){
                        retainsizenum=1024*1024;
                    }
                    if(retainsizebox.isChecked()){
                        if (storageconfig.getTotalavailsize()/retainsizenum <= Integer.parseInt(retainsize.getText().toString())) {
                            Toast.makeText(f.getActivity(), "Storage has reached retain size!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }else {
                        if (storageconfig.getAvailCount() <= 5) {
                            Toast.makeText(f.getActivity(), "Pls enter the size you want!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    if (FillStoragerunable == false) {
                        if (!StorageDIYSize.isChecked()) {
                            AddStorageBar.setMax((int) storageconfig.getTotalavailsize());
                        }else{
                            AddStorageBar.setMax((int)DIYsizenum);
                        }
                        AddStorageBar.setProgress(0);
                        FillStoragerunable = true;
                        FillStorageThreadrunable = true;
                        Log.e("ThenHelper", "Fill storage start");
                        FillStorageThread = new FillStorageThread();
                        FillStorageThread.start();
                        FillStorage.setText("Stop");
                        CleanStorage.setEnabled(false);
                        storagesize.setEnabled(false);
                        StorageDIYSize.setEnabled(false);
                        return;
                    }
                    if (FillStoragerunable == true) {
                        FillStoragerunable = false;
                        FillStorageThreadrunable = false;
                     //   FillStorageThread.interrupt();
                        FillStorage.setText("Start");
                        CleanStorage.setEnabled(true);
                        if(StorageDIYSize.isChecked()) {
                            storagesize.setEnabled(true);
                        }
                        StorageDIYSize.setEnabled(true);
                        Log.e("ThenHelper", "Fill storage stop");
                        return;
                    }
                }
            });
            CleanStorage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(f.getActivity());
                    builder.setIcon(R.mipmap.ico);
                    builder.setTitle("Delete all files?").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            Log.e("ThenHelper", "Clean storagepath");
                            int num=storageMain.Cleanfiles();
                            StorageConfig storageconfig=storageMain.getstorageInfo();
                            Toast.makeText(f.getActivity(), "Total delete "+num+" files!"+"(Available size is "+storageconfig.getTotalavailsize()/1024+" MB)", Toast.LENGTH_SHORT).show();
                            storageMain.showinfo();
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
        private void init_sensor(View view,final Fragment f){
            Log.e("ThenHelper", "go to sensor");
            sensorscrollView=(ScrollView)view.findViewById(R.id.sensorscrollView);
            int i=sensorMain.checkallsensor(sensorscrollView);
            Toast.makeText(f.getActivity(), "Total find "+i+" sensors", Toast.LENGTH_SHORT).show();
        }
        private void init_floatinfo(View view,final Fragment f) {
            Log.e("ThenHelper", "go to init_floatinfo");
            //初始化
            Start_floatinfo=(Button)view.findViewById(R.id.Start_floatinfo);
            BrightnessBox=(CheckBox)view.findViewById(R.id.BrightnessBox);
            CurrentBox=(CheckBox)view.findViewById(R.id.CurrentBox);
            PackageBox=(CheckBox)view.findViewById(R.id.PackageBox);
            SignalBox=(CheckBox)view.findViewById(R.id.SignalBox);
            WIFIBox=(CheckBox)view.findViewById(R.id.WIFIBox);
            BTBox=(CheckBox)view.findViewById(R.id.BTBox);
            MemoryBox=(CheckBox)view.findViewById(R.id.MemoryBox);
            DoodleBox=(CheckBox)view.findViewById(R.id.DoodleBox);
            CPUBox =(CheckBox)view.findViewById(R.id.CPUBox);
            //勾选各项
            BrightnessBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                        floatinfostr[0] = "1";
                    } else {
                        floatinfostr[0] = "0";
                    }
                }
            });
            CurrentBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                        floatinfostr[1] = "1";
                    } else {
                        floatinfostr[1] = "0";
                    }
                }
            });
            PackageBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                        floatinfostr[2] = "1";
                    } else {
                        floatinfostr[2] = "0";
                    }
                }
            });
            SignalBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                        floatinfostr[3] = "1";
                    } else {
                        floatinfostr[3] = "0";
                    }
                }
            });
            WIFIBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if(b){
                        floatinfostr[4]="1";
                    }else {
                        floatinfostr[4]="0";
                    }
                }
            });
            BTBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                        floatinfostr[5] = "1";
                    } else {
                        floatinfostr[5] = "0";
                    }
                }
            });
            MemoryBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                        floatinfostr[6] = "1";
                    } else {
                        floatinfostr[6] = "0";
                    }
                }
            });
            DoodleBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                        floatinfostr[7] = "1";
                    } else {
                        floatinfostr[7] = "0";
                    }
                }
            });
            CPUBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                        floatinfostr[8] = "1";
                    } else {
                        floatinfostr[8] = "0";
                    }
                }
            });
            //启动悬浮窗
            Start_floatinfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    floatinfoMain.startFloatinfo();

                }
            });

        }
        private void init_screenrecord(View view,final Fragment f) {
            Log.e("ThenHelper", "go to init_screenrecord");
            recordaudioBox=(CheckBox)view.findViewById(R.id.recordaudioBox);
            recordfloatBox=(CheckBox)view.findViewById(R.id.recordfloatBox);
            ShowtouchBox=(CheckBox)view.findViewById(R.id.ShowtouchBox);
            ShowlocationBox=(CheckBox)view.findViewById(R.id.ShowlocationBox);
            recordname=(EditText)view.findViewById(R.id.recordname);
            recordBitRate=(EditText)view.findViewById(R.id.recordBitRate);
            recordCountdown=(EditText)view.findViewById(R.id.recordCountdown);
            recorddelay=(EditText)view.findViewById(R.id.recorddelay);
            StartRecord=(Button)view.findViewById(R.id.StartRecord);
            recorddelete=(Button)view.findViewById(R.id.recorddelete);
            recordsizespinner=(Spinner)view.findViewById(R.id.recordsizespinner);

            //显示触摸位置
            ShowtouchBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if(b){
                        //android.provider.Settings.System.putInt(mContext.getContentResolver(), "oem_unlocking", 1);
                        android.provider.Settings.System.putInt(mContext.getContentResolver(), "show_touches", 1);
                    }else {
                        android.provider.Settings.System.putInt(mContext.getContentResolver(), "show_touches", 0);
                       // android.provider.Settings.System.putInt(mContext.getContentResolver(), "oem_unlocking", 0);
                    }
                }
            });
            ShowlocationBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if(b){
                        android.provider.Settings.System.putInt(mContext.getContentResolver(), "pointer_location", 1);
                    }else {
                        android.provider.Settings.System.putInt(mContext.getContentResolver(), "pointer_location", 0);
                    }
                }
            });
            ShowtouchBox.setChecked(true);
            //开始录制
            recordaudioBox.setChecked(true);
            StartRecord.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //判断拍摄延迟
                    if(recorddelay.getText().toString().equals("")){
                        recorddelaynum=4;
                    }else if(Integer.parseInt(recorddelay.getText().toString())>15){
                        Toast.makeText(f.getActivity(), "Delay time is error ( values <=15 or no input).", Toast.LENGTH_SHORT).show();
                        return;
                    }else {
                        recorddelaynum=Integer.parseInt(recorddelay.getText().toString())+1;
                    }
                    //判断倒计时
                    if(recordCountdown.getText().toString().equals("")){
                        recordCountdownnum=-1;
                    }else if(Integer.parseInt(recordCountdown.getText().toString())<10) {
                        Toast.makeText(f.getActivity(), "Countdown time is error ( values >=10 or no input).", Toast.LENGTH_SHORT).show();
                        return;
                    }else {
                            recordCountdownnum=Integer.parseInt(recordCountdown.getText().toString());
                    }
                    //判断bitrate
                    if(recordBitRate.getText().toString().equals("")){
                        recordBitRatenum=8;
                    }else if(Integer.parseInt(recordBitRate.getText().toString())<=15&&Integer.parseInt(recordBitRate.getText().toString())>=1){
                        recordBitRatenum=Integer.parseInt(recordBitRate.getText().toString());
                    }else {
                        Toast.makeText(f.getActivity(), "Record BitRate must be between 1 and 15", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    //判断文件名
                    if(recordname.getText().toString().equals("")) {
                        recordfilename = "ScreenRecord";
                    }else if(!recordname.getText().toString().contains("/")&&
                            !recordname.getText().toString().contains("\\")&&
                            !recordname.getText().toString().contains("<")&&
                            !recordname.getText().toString().contains(">")&&
                            !recordname.getText().toString().contains("*")&&
                            !recordname.getText().toString().contains("?")&&
                            !recordname.getText().toString().contains("|")&&
                            !recordname.getText().toString().contains("\"")&&
                            recordname.getText().toString().length()<=255&&
                            !recordname.getText().toString().startsWith(" ")){
                        recordfilename=recordname.getText().toString();
                    }else {
                        Toast.makeText(f.getActivity(), "File name must meet the follw rules :\n" +
                                " 1. can't contains any / \\ < > * ? | \" \n" +
                                " 2. length <=255 \n" +
                                " 3. can't start with space .", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    //悬浮模式
                    if(recordfloatBox.isChecked()){
                        StartRecord();
                        Intent localIntent = new Intent(MainActivity.mContext,RecordFloatService.class);
                        MainActivity.mContext.stopService(localIntent);
                        MainActivity.mContext.startService(localIntent);
                        StartRecord.setEnabled(false);
                        recorddelete.setEnabled(false);
                        Toast.makeText(f.getActivity(), "Start screenrecord float windows", Toast.LENGTH_SHORT).show();
                        return;
                    }
//                    //提示显示触摸位置
//                    if(!ShowtouchBox.isChecked()){
//                        AlertDialog.Builder builder = new AlertDialog.Builder(f.getActivity());
//                        builder.setIcon(R.mipmap.ico);
//                        builder.setTitle("Do you open Show Touches ?").setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int whichButton) {
//                                ShowtouchBox.setChecked(true);
//                            }
//                        }).setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int whichButton) {
//                                //选择了取消
//                            }
//                        });
//                        builder.create().show();
//                    }
                    if (recordrunable == false) {
                        recordrunable = true;
                        StartRecord.setText("Stop");
                        StartRecord();
                        recorddelete.setEnabled(false);
                        recordfloatBox.setEnabled(false);
                        return;
                    }
                    if (recordrunable == true) {
                        recordrunable = false;
                        StartRecord.setText("Start");
                        Intent localIntent = new Intent(mContext,ScreenRecordService.class);
                        mContext.stopService(localIntent);
                        recorddelete.setEnabled(true);
                        recordfloatBox.setEnabled(true);
                        return;
                    }
                }
            });
            //删除所有
            recorddelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(f.getActivity());
                    builder.setIcon(R.mipmap.ico);
                    builder.setTitle("Delete all files?").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            Log.e("ThenHelper", "delete screenrecord files");
                            int filecount = screenRecord.deleteall();
                            Toast.makeText(f.getActivity(), "Total delete " + filecount + " records", Toast.LENGTH_SHORT).show();
                        }
                    }).setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            //选择了取消
                        }
                    });
                    builder.create().show();
                }
            });
            //分辨率设置
            List<String> spinnerlist = new ArrayList<>();
            ArrayAdapter<String> adapter;
            spinnerlist.add("WVGA 480X800");
            spinnerlist.add("Adaptive");
            spinnerlist.add("FHD 1080X1920");
            spinnerlist.add("HD 720X1280");
            spinnerlist.add("QHD 540X960");
            spinnerlist.add("FWVGA 480X854");
            spinnerlist.add("HVGA 320X480");
            adapter = new ArrayAdapter<String>(f.getActivity(),android.R.layout.simple_spinner_item, spinnerlist);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            recordsizespinner.setAdapter(adapter);
            recordsizespinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View arg1, int pos, long id) {
                    String category = parent.getItemAtPosition(pos).toString();
                    if (category.equals("FHD 1080X1920")) {
                        recordResolution="1080X1920";
                        return;
                    } else if (category.equals("HD 720X1280")) {
                        recordResolution="720X1280";
                        return;
                    }else if (category.equals("Adaptive")) {
                        recordResolution="Adaptive";
                        return;
                    }else if (category.equals("QHD 540X960")) {
                        recordResolution="540X960";
                        return;
                    }else if (category.equals("FWVGA 480X854")) {
                        recordResolution="480X854";
                        return;
                    }else if (category.equals("WVGA 480X800")) {
                        recordResolution="480X800";
                        return;
                    }else if (category.equals("HVGA 320X480")) {
                        recordResolution="320X480";
                        return;
                    }
                }
                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                }
            });

        }
        //***************************************************************************************************
        //请求录制屏幕
        public void StartRecord(){
            mediaProjectionManager=(MediaProjectionManager) MainActivity.mContext.getSystemService(Context.MEDIA_PROJECTION_SERVICE);
            Intent intent=mediaProjectionManager.createScreenCaptureIntent();
            startActivityForResult(intent, 101);
        }
        //接收Activity值
        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            // TODO Auto-generated method stub
            super.onActivityResult(requestCode, resultCode, data);
            if(requestCode==100){
                if(resultCode==RESULT_OK) {
                    Toast.makeText(MainActivity.mContext, "Set ThenHelper as default Message successfully", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(MainActivity.mContext, "Pls allow or can't work", Toast.LENGTH_SHORT).show();
                }
            }else if (requestCode == 101) {
                if(resultCode==RESULT_OK) {
                    mediaProjection = mediaProjectionManager.getMediaProjection(resultCode, data);
                    if(!recordfloatBox.isChecked()){
                        Intent localIntent = new Intent(MainActivity.mContext, ScreenRecordService.class);
                        MainActivity.mContext.stopService(localIntent);
                        MainActivity.mContext.startService(localIntent);
                    }
                }else{
                    recordrunable = false;
                    StartRecord.setText("Start");
                    StartRecord.setEnabled(true);
                    recorddelete.setEnabled(true);
                    recordfloatBox.setEnabled(true);
                    Toast.makeText(MainActivity.mContext, "Pls allow or can't work", Toast.LENGTH_SHORT).show();
                }
            }
        }

        //判断是否为默认短信
        @SuppressLint("NewApi")
        private boolean  checkDefaultMmsSettings() {
            final String myPackageName = mContext.getPackageName();
            boolean result = false;
            if (!Telephony.Sms.getDefaultSmsPackage(mContext).equals(myPackageName)) {
                // App is not default.
                Intent intent = new Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT);
                intent.putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME, myPackageName);
                startActivityForResult(intent,100);
                result = false;
            }else{
                result = true;
            }
            return result;
        }
        //***************************************************************************************************
        //线程1,刷新电池信息
        public  class BatteryInfoThread extends Thread {
            @Override
            public void run() {
                    do {
                        while (pauseThread){
                            try {
                                sleep(600000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        try {
                            Message msg = new Message();
                            msg.what = msgKey1;
                            mHandler.sendMessage(msg);
                            Thread.sleep(1000);
                          //  Log.e("ThenHelper","aaaa");
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } while (othersThreadrunable);
            }
        }
        //线程2,记录电池信息
        public  class BatteryRecordThread extends Thread {
            public void run () {
                do {
                    try {
                        Message msg = new Message();
                        msg.what = msgKey2;
                        mHandler.sendMessage(msg);
                        Thread.sleep(5000);
                    }
                    catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } while(BatteryRecordThreadrunable);
            Log.e("ThenHelper","Stop batteryRecordThread successfully!");
            }
        }
        //线程3，抓取日记
        public class CatchLogThread extends Thread{
            public void run() {
                try {
                   // BatteryHelper.execCommand("logcat -v time -f /sdcard/log.txt");
                    //catchlogMain.execCommand("setprop ro.debuggable 1");
                    catchlogMain.execCommand("top -m 1 -n 1");

                } catch (IOException e) {
                    e.printStackTrace();
                }
                Log.e("ThenHelper","endlog");
            }
        }
        //线程4,添加联系人
        public class InsertcontactsThread extends Thread {
           // private int count=1;
            @Override
            public void run() {
                Contactprogresscount=1;
                while (AddContactsThreadrunable&&Contactprogresscount <= Integer.parseInt(contactsnum)){
                    try {
                        Message msg = new Message();
                        msg.what = msgKey10;
                        mHandler.sendMessage(msg);
                        contactsMain.insertContacts();
                        Contactprogresscount++;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                try {
                    sleep(600);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Message msg = new Message();
                msg.what = msgKey3;
                mHandler.sendMessage(msg);
                Log.e("ThenHelper", "Insert contacts successfully");
            }
        }
        //线程5,添加电话记录
        public class InsertcalllogThread extends Thread {
            //private int count=1;
            @Override
            public void run() {
                Contactprogresscount=1;
                while (AddCalllogThreadrunable&&Contactprogresscount <= Integer.parseInt(callnum)){
                    try {
                        Message msg = new Message();
                        msg.what = msgKey10;
                        mHandler.sendMessage(msg);
                        contactsMain.insertCallLog(callphone, calllogtype);
                        callphone=(Long.parseLong(callphone)+1)+"";
                        Contactprogresscount++;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                try {
                    sleep(600);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Message msg = new Message();
                msg.what = msgKey4;
                mHandler.sendMessage(msg);
                Log.e("ThenHelper", "Insert calllog successfully");
            }
        }
        //线程6,添加短信
        public class InsertSMSThread extends Thread {
          //  private int count=1;
            @Override
            public void run() {
                Mediacount=1;
                while (AddSMSThreadrunable&&Mediacount <= Integer.parseInt(Mnum)){
                    try {
                        Message msg = new Message();
                        msg.what = msgKey5;
                        mHandler.sendMessage(msg);
                        if(Missame==true) {
                            if(SMSorMMS.equals("SMS")) {
                                messageMain.insertsameSMS(SelectMessagetype, MessageReadflag, Mphone, Messagecontent.getText().toString());
                            }else {
                                messageMain.insterMms(SelectMessagetype, Mphone);
                            }
                        }else {
                            if(SMSorMMS.equals("SMS")) {
                                messageMain.insertdifferentSMS(SelectMessagetype, MessageReadflag, Messagecontent.getText().toString());
                            }else {
                                messageMain.insterdifferentMms(SelectMessagetype);
                            }
                        }
                        Mediacount++;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                try {
                    sleep(600);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Message msg = new Message();
                msg.what = msgKey7;
                mHandler.sendMessage(msg);
                Log.e("ThenHelper", "Insert SMS successfully");
            }
        }
        //线程7，添加媒体
        public class AddmediaThread extends Thread {
            @Override
            public void run() {
                Mediacount=1;
                SimpleDateFormat sDateFormat = new SimpleDateFormat(
                        "HHmmssSSS");
                while (AddMediaThreadrunable&&Mediacount <= Integer.parseInt(addmedianum)){
                    try {
                        Message msg = new Message();
                        msg.what = msgKey5;
                        mHandler.sendMessage(msg);
                        String date = sDateFormat.format(new Date());
                        if(selectedmedia.equals("Music")) {
                            String[] split = (mediaMain.musicpath + "/test.mp3").split("\\.");
                            String path = split[0] + date + "." + split[1];
                            mediaMain.copymusic(path);
                            mediaMain.Scanner(path);
                        }else if(selectedmedia.equals("Video")) {
                            String[] split = (mediaMain.videopath + "/test.mp4").split("\\.");
                            String path = split[0] + date + "." + split[1];
                            mediaMain.copyvideo(path);
                            mediaMain.Scanner(path);
                        }else if(selectedmedia.equals("Picture")) {
                            String[] split = (mediaMain.picturepath + "/test.png").split("\\.");
                            String path = split[0] + date + "." + split[1];
                            mediaMain.copypicture(path);
                            mediaMain.Scanner(path);
                        }
                        Mediacount++;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                try {
                    sleep(600);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Message msg = new Message();
                msg.what = msgKey6;
                mHandler.sendMessage(msg);
                 Log.e("ThenHelper", "Add media "+selectedmedia+" successfully");
            }
        }
        //线程8，填充手机存储
        public class FillStorageThread extends Thread {
            @Override
            public void run() {
                Fillstoragecount=1;
                int SDcardcount=1;
                String filepath = storageMain.CreateFileStorage();
                if (!StorageDIYSize.isChecked()) {
                    while (FillStorageThreadrunable) {
                        try {
                            storageMain.WriteToStorage(filepath);
                            Fillstoragecount++;
                            SDcardcount++;
                            if (selectedstorage.equals("SDcard") && SDcardcount >= 273) {
                                filepath = storageMain.CreateFileStorage();
                                SDcardcount=1;
                                Log.e("ThenHelper", "Create another 4G file.");
                            }
                            Message msg = new Message();
                            msg.what = msgKey8;
                            mHandler.sendMessage(msg);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    try {
                        sleep(600);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Message msg = new Message();
                    msg.what = msgKey9;
                    mHandler.sendMessage(msg);
                    Log.e("ThenHelper", "Fill " + selectedstorage + " successfully");
                }else {
                    while (FillStorageThreadrunable&&Fillstoragecount<=DIYsizenum) {
                        try {
                            if(selectedstoragetype.equals("KB")){
                                storageMain.WriteToStorageKB(filepath);
                            }
                            if(selectedstoragetype.equals("MB")){
                                storageMain.WriteToStorageMB(filepath);
                            }
                            if(selectedstoragetype.equals("GB")){
                                storageMain.WriteToStorageGB(filepath);
                            }
                            Fillstoragecount++;
                            Message msg = new Message();
                            msg.what = msgKey8;
                            mHandler.sendMessage(msg);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    try {
                        sleep(600);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Message msg = new Message();
                    msg.what = msgKey9;
                    mHandler.sendMessage(msg);
                    Log.e("ThenHelper", "Fill " + selectedstorage + " successfully");
                }

            }
        }

        //关机广播
        public class ShutdownReceiver extends BroadcastReceiver {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(Intent.ACTION_SHUTDOWN)) {
                    Log.e("ThenHelper","shutdown");
                    Intent localIntent = new Intent(mContext,ScreenRecordService.class);
                    mContext.stopService(localIntent);
                    Log.e("ThenHelper", "destroy screen record for shutdown");
                }
            }
        }
        //信息处理类
        Handler mHandler = new Handler() {
            @Override
            public void handleMessage (Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    //电池信息更新
                    case msgKey1:
                        try {
                            platform=BatteryHelper.execCommand("getprop ro.board.platform");
                         //     platform= Build.DISPLAY;
//                            if (platform.substring(0,3).toLowerCase().equals("msm")||platform.substring(0,2).toLowerCase().equals("mt")) {
                                BatteryConfig config;
                                config=batteryMain.getBatteryInfo(platform);
                                if (config!=null){
                                BatteryInfo.setText(
                                           //     " Model&Platform : "+ Build.MODEL+"_"+platform+
                                                        " Battery Info :"+"\n"+
                                        " Date : " + config.getDate() + "\n" +
                                                " Time : " + config.getTime() + "\n" +
                                                " Brightness : "+config.getBrightness()+"\n"+
                                                " Capacity : " + config.getCapacity() + "%\n" +
                                                " Current : " + config.getCurrent() + "mA\n" +
                                                " Voltage : " + config.getVoltage() + "mV\n" +
                                                " Temp : " + config.getTemp() + "℃\n" +
                                                " Status : " + config.getStatus() + "\n" +
                                                " Chargetype : " + config.getChargetype() + "\n" +
                                                " Health : " + config.getHealth() + "\n"
                                );
                                }else {
                                    BatteryInfo.setText("error pls check devices");
                                }
//                            }else {
//                                BatteryInfo.setText("ThenHelper does not support this platform, if you need to test,please call Then");
//                            }

                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        break;
                    //电池信息写入文件
                    case msgKey2:
                        try {
                            platform=BatteryHelper.execCommand("getprop ro.board.platform");
                            if (platform.substring(0,3).toLowerCase().equals("msm")) {
                                BatteryHelper.WriteFileValue(batteryMain.getBatteryInfo(platform));
                            }else {
                                BatteryInfo.setText("ThenHelper does not support this platform, if you need to test,please call Then");
                            }
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        break;
                    //填充联系人完成
                    case msgKey3:
                        mbtnadd.setText("Add");
                        meditcontactnumber.setEnabled(true);
                        Addcalllog.setEnabled(true);
                        deletecontact.setEnabled(true);
                        AddContactsThreadrunable=false;
                        AddContactsrunable=false;
                        totalcontact=contactsMain.Totalcontact();
                        showcontactnum.setText("Total : "+totalcontact);
                        if (Contactprogresscount>Integer.parseInt(contactsnum)) {
                            Toast.makeText(MainActivity.mContext, "Create contacts successfully!", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    //填充电话记录完成
                    case msgKey4:
                        Addcalllog.setText("Add");
                        calllognum.setEnabled(true);
                        calllogphone.setEnabled(true);
                        mbtnadd.setEnabled(true);
                        deletecall.setEnabled(true);
                        AddCalllogThreadrunable=false;
                        AddCalllogrunable=false;
                        totalcalllog=contactsMain.Totalcallnum();
                        showcallnum.setText("Total : "+totalcalllog);
                        if (Contactprogresscount>Integer.parseInt(callnum)) {
                            Toast.makeText(MainActivity.mContext, "Create call history successfully!", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    //媒体页面进度条
                    case msgKey5:
                        Mediaprogressbar.setProgress( Mediaprogressbar.getProgress()+1);
                        Progressnum.setText("Current Progress : "+Mediacount );
                        break;
                    //填充媒体完成
                    case msgKey6:
                        Addmedia.setText("Add");
                        medianum.setEnabled(true);
                        addmessage.setEnabled(true);
                        Deletemedia.setEnabled(true);
                        AddMediaThreadrunable=false;
                        AddMediarunable=false;
                        totalmedia=mediaMain.Totalmedia();
                        showmedianum.setText("Total : "+totalmedia);
                        if (Mediacount>Integer.parseInt(addmedianum)) {
                            Toast.makeText(MainActivity.mContext, "Create " + selectedmedia + " files successfully!", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    //填充短信完成
                    case msgKey7:
                        addmessage.setText("Add");
                        Messagenum.setEnabled(true);
                        if(Issamesession.isChecked()) {
                            Messagephone.setEnabled(true);
                        }
                        Issamesession.setEnabled(true);
                        Addmedia.setEnabled(true);
                        Messagecontent.setEnabled(true);
                        AddSMSThreadrunable=false;
                        AddSMSrunable=false;
                        totalmessage=messageMain.Totalmessage();
                        showmessagenum.setText("Total : "+totalmessage);
                        if (Mediacount>Integer.parseInt(Mnum)) {
                            Toast.makeText(MainActivity.mContext, "Create " + Mnum + " SMS successfully!", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    //填充存储进度条
                    case msgKey8:
                        StorageConfig storageconfig=storageMain.getstorageInfo();
                        DecimalFormat  df  = new DecimalFormat("######0.00");
                        if(selectedstoragetype.equals("KB")){
                            retainsizenum=1;
                        }else if(selectedstoragetype.equals("MB")){
                            retainsizenum=1024;
                        }else if(selectedstoragetype.equals("GB")){
                            retainsizenum=1024*1024;
                        }
                        if(retainsizebox.isChecked()){
                            if (storageconfig.getTotalavailsize()/retainsizenum <= Integer.parseInt(retainsize.getText().toString())) {
                                FillStorageThreadrunable = false;
                            }
                        }else {
                            if (storageconfig.getAvailCount() <= 5) {
                                FillStorageThreadrunable = false;
                            }
                        }
                        if (!StorageDIYSize.isChecked()) {
                            long fill = Fillstoragecount * (storageMain.storagebytes / 1024);
                            AddStorageBar.setProgress((int) fill);
                        }else{
                            AddStorageBar.setProgress((int)Fillstoragecount);
                        }
                        availablesize.setText("Available Size : "+storageconfig.getTotalavailsize()+"KB=="+storageconfig.getTotalavailsize()/1024+"MB=="+
                                df.format(((double)storageconfig.getTotalavailsize())/1024/1024)+"GB\n");
                        break;
                    //填充存储完成
                    case msgKey9:
                        FillStorage.setText("Start");
                        CleanStorage.setEnabled(true);
                        if(StorageDIYSize.isChecked()) {
                            storagesize.setEnabled(true);
                        }
                        StorageDIYSize.setEnabled(true);
                        FillStorageThreadrunable=false;
                        FillStoragerunable=false;
                        if (!StorageDIYSize.isChecked()) {
                            Toast.makeText(MainActivity.mContext, "Fill "+selectedstorage+" successfully!", Toast.LENGTH_SHORT).show();
                        }
                        if (StorageDIYSize.isChecked()&&Fillstoragecount>DIYsizenum){
                            Toast.makeText(MainActivity.mContext, "Fill "+selectedstorage+" successfully!", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    //联系人页面进度条
                    case msgKey10:
                        Contactprogressbar.setProgress( Contactprogressbar.getProgress()+1);
                        ContactProgressnum.setText("Current Progress : "+Contactprogresscount );
                        break;
                    default:
                        break;
                }
            }
        };

    }
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save away the original text, so we still have it if the activity
        // needs to be killed while paused.
//        savedInstanceState.putInt("IntTest", mCount);
//        savedInstanceState.putString("StrTest", "savedInstanceState test");

        super.onSaveInstanceState(savedInstanceState);
      //  Log.e("ThenHelper", "onSaveInstanceState");
    }
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
//        int mCount = savedInstanceState.getInt("IntTest");
//        String StrTest = savedInstanceState.getString("StrTest");
      //  Log.e("ThenHelper", "onRestoreInstanceState");
    }
    public static void exitThenHelper(){
        wakelockflag=false;
        pauseThread=false;
        FillStorageThreadrunable=false;
        FillStoragerunable=false;
        othersThreadrunable=false;
        BatteryRecordThreadrunable=false;
        AddCalllogrunable=false;
        AddCalllogThreadrunable=false;
        AddContactsrunable=false;
        AddContactsThreadrunable=false;
        AddMediarunable=false;
        AddMediaThreadrunable=false;
        AddSMSThreadrunable=false;
        AddSMSrunable=false;
        recordrunable=false;
        isvibrated=false;
        //取消sensor注册
        sensorMain.exitallsensor();

        Intent stopbattery = new Intent(mContext,BatteryService.class);
        mContext.stopService(stopbattery);
        File file=new File(batteryMain.BatteryMonitorTXT);
        if(file.exists()){
            String newfile=batteryMain.renameRecord();
            Toast.makeText(MainActivity.mContext, "Stop to record, and the file renames to "+newfile, Toast.LENGTH_LONG).show();
            batteryMain.Scanner(newfile);
        }
        Intent stopfloatinfo = new Intent(mContext,FloatinfoService.class);
        mContext.stopService(stopfloatinfo);
        Intent stopsensor = new Intent(mContext,SensorService.class);
        mContext.stopService(stopsensor);
        Intent stopgifgame = new Intent(mContext,GIFgame.class);
        mContext.stopService(stopgifgame);
        Intent stopscreenrecord = new Intent(mContext,ScreenRecordService.class);
        mContext.stopService(stopscreenrecord);
        Intent stopscreenrecordfloat = new Intent(mContext,RecordFloatService.class);
        mContext.stopService(stopscreenrecordfloat);
        Intent stopDoodleService = new Intent(mContext,DoodleService.class);
        mContext.stopService(stopDoodleService);


        //Fragement
        mainFragment= null;batteryFragment=null;assistantFragment=null;addcontactsFragment=null;
        addmediaFragement=null;addstorageFragement=null;sensorFragement=null;floatFragement=null;screenrecordFragement=null;
        Log.e("ThenHelper","*********************exit ThenHelper*********************");
    }
    public void initThenHelper(){
        Log.e("ThenHelper","*********************init ThenHelper*********************");
        //初始化变量
        //Fragement
        mainFragment= null;batteryFragment=null;assistantFragment=null;addcontactsFragment=null;
        addmediaFragement=null;addstorageFragement=null;sensorFragement=null;floatFragement=null;screenrecordFragement=null;
        //all
        wakelockflag=false;
        pauseThread=false;
        //assistant
        isvibrated=false;
        //battery
        StartBatteryrunable=false;BatteryRecordThreadrunable=false;othersThreadrunable=true;pauseThread=false;
        SelectFile="";
        whichselect=0;
        batteryInterval=5000;
        //contact
        AddContactsThreadrunable=false;AddContactsrunable=false;
        Contactprogresscount=1;
        //calllog
        AddCalllogThreadrunable=false;AddCalllogrunable=false;
        //message
        AddSMSThreadrunable=false;AddSMSrunable=false;Missame=false;
        totalmessage=0;
        SelectMessagetype="Receive";MessageReadflag="Read";SMSorMMS="SMS";
        //media
        AddMediaThreadrunable=false;AddMediarunable=false;
        selectedmedia="Picture";
        Mediacount=1;
        totalmedia=0;
        //storage
        selectedstorage="Data";
        selectedstoragetype="MB";
        Fillstoragecount=0;
        DIYsizenum=0;
        FillStorageThreadrunable=false;FillStoragerunable=false;
        //floatwin
        floatinfostr=new String[]{"0","0","0","0","0","0","0","0","0","0"};
        //screenrecord
        recordrunable=false;
        recordResolution="Adaptive"; recordfilename="ScreenRecord";
        recordBitRatenum=8;recordCountdownnum=-1; recorddelaynum=-1;
    }
    public void test(){
        Log.e("ThenHelper","111");
    }
}


