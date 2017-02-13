package com.screenrecord;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.main.thenhelper.MainActivity;
import com.main.thenhelper.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by admin on 2015/09/30   .
 */

public class DoodleService extends Service {

    //定义浮动窗口布局
    RelativeLayout mFloatLayout;
    WindowManager.LayoutParams wmParams;
    //创建浮动窗口设置布局参数的对象
    WindowManager mWindowManager;

    Button Close,Eraser,Show,Undo,Clear,Save;
    Spinner Size,Color,Shape;
    private boolean showrunable=false;
    TableLayout Moretable;
    boolean startrunable;

    Doodle doodle;


    private void createFloatView()
    {
        wmParams = new WindowManager.LayoutParams();
        //获取的是WindowManagerImpl.CompatModeWrapper
        mWindowManager = (WindowManager)getApplication().getSystemService(getApplication().WINDOW_SERVICE);
        Log.e("ThenHelper", "Create Doodle Windows first");
        //设置window type
        wmParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        //设置图片格式，效果为背景透明
        wmParams.format = PixelFormat.TRANSLUCENT;
        //设置浮动窗口不可聚焦（实现操作除浮动窗口外的其他可见窗口的操作）
        wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        //调整悬浮窗显示的停靠位置为左侧置顶
        wmParams.gravity = Gravity.LEFT | Gravity.BOTTOM;
        // 以屏幕左上角为原点，设置x、y初始值，相对于gravity
        wmParams.x = 0;
        wmParams.y = 0;
        //设置悬浮窗口长宽数据
        wmParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        wmParams.height = WindowManager.LayoutParams.MATCH_PARENT;
		 /*// 设置悬浮窗口长宽数据
        wmParams.width = 200;
        wmParams.height = 80;*/
        LayoutInflater inflater = LayoutInflater.from(getApplication());
        //获取浮动窗口视图所在布局
        mFloatLayout = (RelativeLayout) inflater.inflate(R.layout.doodle_float, null);
        //添加mFloatLayout
        mWindowManager.addView(mFloatLayout, wmParams);
        //浮动窗口按钮
        Show = (Button)mFloatLayout.findViewById(R.id.Show);
        Close = (Button)mFloatLayout.findViewById(R.id.Close);
        Eraser = (Button)mFloatLayout.findViewById(R.id.Eraser);
        Undo=(Button)mFloatLayout.findViewById(R.id.Undo);
        Clear=(Button)mFloatLayout.findViewById(R.id.Clear);
        Save=(Button)mFloatLayout.findViewById(R.id.Save);

        Shape=(Spinner)mFloatLayout.findViewById(R.id.Shape);
        Size=(Spinner)mFloatLayout.findViewById(R.id.Size);
        Color = (Spinner)mFloatLayout.findViewById(R.id.Color);

        Moretable=(TableLayout)mFloatLayout.findViewById(R.id.Moretable);
        doodle=(Doodle)mFloatLayout.findViewById(R.id.doodle);

        doodle.setSize(dip2px(5));
        doodle.setColor("#ff0000");

        Color.setVisibility(View.VISIBLE);
        Size.setVisibility(View.VISIBLE);
     //   Shape.setVisibility(View.VISIBLE);
        Eraser.setBackgroundColor(0x00000000);
        Close.setBackgroundColor(0x00000000);
        Show.setBackgroundColor(0x00000000);
        Undo.setBackgroundColor(0x00000000);
        Clear.setBackgroundColor(0x00000000);
        Save.setBackgroundColor(0x00000000);
        Moretable.setVisibility(View.GONE);

        mFloatLayout.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec
                .makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        //关闭本Service
        Close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
               // Toast.makeText(DoodleService.this, "Stop Doodle float window ", Toast.LENGTH_SHORT).show();
                Intent localIntent = new Intent(DoodleService.this, DoodleService.class);
                DoodleService.this.stopService(localIntent);
            }
        });
        //保存
        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/ThenHelper/ScreenCap/Cap" + System.currentTimeMillis() + ".png";
                if (!new File(path).exists()) {
                    new File(path).getParentFile().mkdirs();
                }
                savePicByPNG(doodle.getBitmap(), path);
                Toast.makeText(MainActivity.mContext, "Pictures saves in " + path, Toast.LENGTH_LONG).show();
            }
        });
        //回退
        Undo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //doodle.back();
            }
        });
        //橡皮擦
        Eraser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                doodle.setType(Doodle.ActionType.Path);
//                doodle.setColor("#00FFFFFF");
//                doodle.setSize(dip2px(15));
            }
        });
        //形状
        List<String> spinnershape = new ArrayList<>();
        ArrayAdapter<String> adaptershape;
        spinnershape.add("Path");
        spinnershape.add("Line");
        spinnershape.add("Rect");
        spinnershape.add("Circle");
        spinnershape.add("FillecRect");
        spinnershape.add("FilledCircle");
        adaptershape = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, spinnershape);
        adaptershape.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Shape.setAdapter(adaptershape);
        Shape.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View arg1, int pos, long id) {
                String category = parent.getItemAtPosition(pos).toString();
                if (category.equals("Path")) {
                    doodle.setType(Doodle.ActionType.Path);
                    return;
                } else if (category.equals("Line")) {
                    doodle.setType(Doodle.ActionType.Line);
                    return;
                } else if (category.equals("Rect")) {
                    doodle.setType(Doodle.ActionType.Rect);
                    return;
                }else if (category.equals("Circle")) {
                    doodle.setType(Doodle.ActionType.Circle);
                    return;
                }else if (category.equals("FillecRect")) {
                    doodle.setType(Doodle.ActionType.FillecRect);
                    return;
                }else if (category.equals("FilledCircle")) {
                    doodle.setType(Doodle.ActionType.FilledCircle);
                    return;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        //颜色
        List<String> spinnercolor = new ArrayList<>();
        ArrayAdapter<String> adaptercolor;
        spinnercolor.add("Red");
        spinnercolor.add("Green");
        spinnercolor.add("Blue");
        adaptercolor = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, spinnercolor);
        adaptercolor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Color.setAdapter(adaptercolor);
        Color.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View arg1, int pos, long id) {
                String category = parent.getItemAtPosition(pos).toString();
                if (category.equals("Red")) {
                    doodle.setColor("#ff0000");
                    return;
                } else if (category.equals("Green")) {
                    doodle.setColor("#00ff00");
                    return;
                } else if (category.equals("Blue")) {
                    doodle.setColor("#0000ff");
                    return;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        //粗细
        List<String> spinnerlist = new ArrayList<>();
        ArrayAdapter<String> adapter;
        spinnerlist.add("Fine");
        spinnerlist.add("Crude");
        spinnerlist.add("Normal");
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, spinnerlist);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Size.setAdapter(adapter);
        Size.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View arg1, int pos, long id) {
                String category = parent.getItemAtPosition(pos).toString();
                if (category.equals("Normal")) {
                    doodle.setSize(dip2px(10));
                    return;
                } else if (category.equals("Crude")) {
                    doodle.setSize(dip2px(15));
                    return;
                }else if (category.equals("Fine")) {
                    doodle.setSize(dip2px(5));
                    return;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        //更多按钮
        Show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(showrunable==false) {
                    showrunable=true;
                    Moretable.setVisibility(View.VISIBLE);
                    return;
                }
                if(showrunable==true) {
                    showrunable=false;
                    Moretable.setVisibility(View.GONE);
                    return;
                }
            }
        });

        //清除屏幕
        Clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("ThenHelper","clear doodle");
                doodle.setVisibility(View.GONE);
                doodle.setVisibility(View.VISIBLE);
            }
        });
    }
    public static void savePicByPNG(Bitmap b, String filePath) {
        FileOutputStream fos = null;
        try {
//			if (!new File(filePath).exists()) {
//				new File(filePath).createNewFile();
//			}
            fos = new FileOutputStream(filePath);
            if (null != fos) {
                b.compress(Bitmap.CompressFormat.PNG, 90, fos);
                fos.flush();
                fos.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //画笔大小调节
    private int dip2px(float dpValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        Log.e("ThenHelper", "DoodleService onCreate");
        startrunable=false;
        createFloatView();

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy()
    {
        // TODO Auto-generated method stub
        super.onDestroy();
        if(mFloatLayout != null)
        {
            //移除悬浮窗口
            mWindowManager.removeView(mFloatLayout);
        }

        Log.e("ThenHelper", "End DoodleService ");
    }
}
