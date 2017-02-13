package com.assistant;

import android.content.Context;
import android.media.AudioManager;
import android.os.Vibrator;
import android.provider.Settings;
import android.util.Log;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.Toast;

import com.main.thenhelper.MainActivity;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by admin on 2015/9/6.
 */
public class AssistantMain {

    /**
     * imei由15位数字组成，
     * 前6位(TAC)是型号核准号码，代表手机类型。
     * 接着2位(FAC)是最后装配号，代表产地。
     * 后6位(SNR)是串号，代表生产顺序号。
     * 最后1位 (SP)是检验码。
     * 检验码计算：
     * (1).将偶数位数字分别乘以2，分别计算个位数和十位数之和
     * (2).将奇数位数字相加，再加上上一步算得的值
     * (3).如果得出的数个位是0则校验位为0，否则为10减去个位数
     */
        public String createIMEI() {
            SimpleDateFormat dftime = new SimpleDateFormat("HHmmss");
            String time = dftime.format(new Date());
            String imeiString="35190103"+time;//前14位
            char[] imeiChar=imeiString.toCharArray();
            int resultInt=0;
            for (int i = 0; i < imeiChar.length; i++) {
                int a=Integer.parseInt(String.valueOf(imeiChar[i]));
                i++;
                final int temp=Integer.parseInt(String.valueOf(imeiChar[i]))*2;
                final int b=temp<10?temp:temp-9;
                resultInt+=a+b;
            }
            resultInt%=10;
            resultInt=resultInt==0?0:10-resultInt;
            return imeiString.substring(0,6)+" "+imeiString.substring(6,8)+" "+imeiString.substring(8,14)+" "+resultInt;
        }

    //MAC地址第二位必须是0/4/8/C这4个之一.B8649108D756
    //MAC(Medium/Media Access Control)地址，
    // 用来表示互联网上每一个站点的标识符，采用十六进制数表示，共六个字节（48位）。
    // 其中，前三个字节是由IEEE的注册管理机构RA负责给不同厂家分配的代码(高位24位)
    // ，也称为“编制上唯一的标识符”（Organizationally Unique Identifier)，后三个字节(低位24位)由各厂家自行指派给生产的适配器接口，
    // 称为扩展标识符（唯一性）。 一个地址块可以生成224个不同的地址。MAC地址实际上就是适配器地址或适配器标识符EUI-48。
    public String createMAC(){
        SimpleDateFormat dftime = new SimpleDateFormat("HH:mm:ss");
        String time = dftime.format(new Date());
        String fixstr="B8:64:91:"+time;
        return fixstr;
    }

    /**
     * 应用程序运行命令获取 Root权限，设备必须已破解(获得ROOT权限)
     * @param command 命令：String apkRoot="chmod 777 "+getPackageCodePath(); RootCommand(apkRoot);
     * @return 应用程序是/否获取Root权限
     */
    public  boolean RootCommand(String command)
    {
        Process process = null;
        DataOutputStream os = null;
        try
        {
            process = Runtime.getRuntime().exec("su");
            os = new DataOutputStream(process.getOutputStream());
            os.writeBytes(command + "\n");
            os.writeBytes("exit\n");
            os.flush();
            process.waitFor();
        } catch (Exception e)
        {
            Log.e("ThenHelper", "ROOT REE" + e.getMessage());
            return false;
        } finally
        {
            try
            {
                if (os != null)
                {
                    os.close();
                }
                process.destroy();
            } catch (Exception e)
            {
            }
        }
        Log.e("ThenHelper", "Root SUC ");
        return true;
    }

    public static String execCommand(String command) throws IOException {
        Process proc = null;
        Runtime runtime = Runtime.getRuntime();
        StringBuffer stringBuffer = new StringBuffer();
        proc = runtime.exec(new String[]{"/system/bin/sh", "-c",command});
        BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
        try {
            String line=null;
            while ((line = in.readLine()) != null) {
                stringBuffer.append(line + "\n");
            }
            if (proc.waitFor() != 0) {
                Log.e("ThenHelper", "exit value = " + proc.exitValue());
                //System.err.println("ThenHelper exit value = " + proc.exitValue());
            }
        } catch (InterruptedException e) {
            System.err.println(e);
        }finally {
            if (proc!=null){
                proc.destroy();
            }
            if (in!=null){
                in.close();
            }
        }
        return stringBuffer.toString();
    }

    //检查系统亮度
    public int screenBrightness_check(){
        //先关闭系统的亮度自动调节
        try     {
            if(android.provider.Settings.System.getInt(MainActivity.mContext.getContentResolver(),
                    android.provider.Settings.System.SCREEN_BRIGHTNESS_MODE) == android.provider.Settings.System.
                    SCREEN_BRIGHTNESS_MODE_AUTOMATIC)
            {
                android.provider.Settings.System.putInt(MainActivity.mContext.getContentResolver(),
                        android.provider.Settings.System.SCREEN_BRIGHTNESS_MODE,
                        android.provider.Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
            }
            }catch (Settings.SettingNotFoundException e)     {
            // TODO Auto-generated catch block
                 e.printStackTrace();
        }
        //获取当前亮度,获取失败则返回255
    int ScreenBrightness=(int)(android.provider.Settings.System.getInt(MainActivity.mContext.getContentResolver(),
            android.provider.Settings.System.SCREEN_BRIGHTNESS, 255));
        return ScreenBrightness;
        //文本、进度条显示
//         mSeekBar_light.setProgress(intScreenBrightness);
//        mTextView_light.setText(""+intScreenBrightness*100/255);
    }

    //屏幕亮度
    public void setScreenBritness(int brightness)     {
        //不让屏幕全暗
             if(brightness<=1)
             {
                 brightness=1;
             }
        //设置当前activity的屏幕亮度
       //     WindowManager.LayoutParams lp = this.getWindow().getAttributes();
        //0到1,调整亮度暗到全亮
//        lp.screenBrightness = Float.valueOf(brightness/255f);
//        this.getWindow().setAttributes(lp);
        //保存为系统亮度方法1
        android.provider.Settings.System.putInt(MainActivity.mContext.getContentResolver(),
                         android.provider.Settings.System.SCREEN_BRIGHTNESS,
                         brightness);
        //保存为系统亮度方法2
    //      Uri uri = android.provider.Settings.System.getUriFor("screen_brightness");
    //    android.provider.Settings.System.putInt(getContentResolver(), "screen_brightness", brightness);
    //            resolver.registerContentObserver(uri, true, myContentObserver);
    //    getContentResolver().notifyChange(uri, null);
        //更改亮度文本显示
      //  mTextView_light.setText(""+brightness*100/255);
    }

    //检查音量
    public String getVolume (){
       // DecimalFormat df  = new DecimalFormat("######0");
        AudioManager mAudioManager = (AudioManager) MainActivity.mContext.getSystemService(Context.AUDIO_SERVICE);
//通话音量
        int callmax = mAudioManager.getStreamMaxVolume( AudioManager.STREAM_VOICE_CALL );
        int callcurrent = mAudioManager.getStreamVolume( AudioManager.STREAM_VOICE_CALL );
//系统音量
//        int systemmax = mAudioManager.getStreamMaxVolume( AudioManager.STREAM_SYSTEM );
//        int systemcurrent = mAudioManager.getStreamVolume(AudioManager.STREAM_SYSTEM);
//铃声音量
        int ringmax = mAudioManager.getStreamMaxVolume( AudioManager.STREAM_RING );
        int ringcurrent = mAudioManager.getStreamVolume(AudioManager.STREAM_RING);
//音乐音量
        int musicmax = mAudioManager.getStreamMaxVolume( AudioManager.STREAM_MUSIC );
        int musiccurrent = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
//闹铃音量
        int alarmmax = mAudioManager.getStreamMaxVolume( AudioManager.STREAM_ALARM );
        int alarmaxcurrent = mAudioManager.getStreamVolume(AudioManager.STREAM_ALARM);

        String str="";
        str="Call current="+callcurrent+"  max="+callmax+"  "+(int)((float)callcurrent/callmax*100)+"%\n"
//                +"Sys volume="+systemcurrent+" max="+systemmax+"\n"
                +"Ring current="+ringcurrent+"  max="+ringmax+"  "+(int)((float)ringcurrent/ringmax*100)+"%\n"
                +"Media current="+musiccurrent+"  max="+musicmax+"  "+(int)((float)musiccurrent/musicmax*100)+"%\n"
                +"Alarm current="+alarmaxcurrent+"  max="+alarmmax+"  "+(int)((float)alarmaxcurrent/alarmmax*100)+"%\n";
        return str;
    }

    //震动
    Vibrator vibrator;
    public void vibrator(){
         vibrator = (Vibrator)MainActivity.mContext.getSystemService(Context.VIBRATOR_SERVICE);
                    // vibrator.vibrate(2000);//振动两秒
                    // 下边是可以使震动有规律的震动   -1：表示不重复 0：循环的震动
                   long[] pattern = {0, 2000, 0, 2000};
                   vibrator.vibrate(pattern, 0);
                  Log.e("ThenHelper", "vibrator works");
            }
    public void cancelvibrator(){
        if(vibrator!=null) {
            vibrator.cancel();
            Log.e("ThenHelper", "vibrator stops");
        }
    }
}
