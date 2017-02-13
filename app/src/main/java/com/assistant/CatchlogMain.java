package com.assistant;

import android.util.Log;

import com.main.thenhelper.MainActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;

/**
 * Created by theny550 on 2015/8/1.
 */
public class CatchlogMain {
    public static String CatchlogPath;
    public static String CatchlogTXT;
    public String startRecord() {
        SimpleDateFormat filedf = new SimpleDateFormat("yyyyMMdd_HHmm_ss");
        String fileDate = filedf.format(new java.util.Date());
        CatchlogPath = CatchlogConstant.FileStorage + "/CatchLog/";
        CatchlogTXT = CatchlogPath + fileDate + ".txt";
        File CatchlPath = new File(CatchlogPath);
        File CatchTXT = new File(CatchlogTXT);
        if (!CatchlPath.exists()) {
            CatchlPath.mkdir();
        }
        if (!CatchTXT.exists()) {
            try {
                CatchTXT.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return CatchlogTXT;
    }
    public static String execCommand(String command) throws IOException {
        Process proc = null;
        Runtime runtime = Runtime.getRuntime();
        StringBuffer stringBuffer = new StringBuffer();
        proc = runtime.exec(new String[]{"/system/bin/sh", "-c",command});
        BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
        try {
            String line=null;
            while ((line = in.readLine()) != null&& (MainActivity.StartLogThreadrunable=true)) {
                stringBuffer.append(line + "\n");
                WriteToFile(CatchlogTXT,stringBuffer.toString(),true);
            }
            Log.e("ThenHelper","333");
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

    public static void WriteToFile(String FileName,String strContent, boolean isAppended) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(FileName,isAppended);
            strContent=strContent+"\r\n";
            byte[] initContent = strContent.getBytes("UTF-8");
            fileOutputStream.write(initContent);
            fileOutputStream.close();
            fileOutputStream.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
