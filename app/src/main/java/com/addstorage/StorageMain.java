package com.addstorage;

import android.content.Context;
import android.os.Environment;
import android.os.PowerManager;
import android.os.StatFs;
import android.os.storage.StorageManager;
import android.util.Log;

import com.main.thenhelper.MainActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.security.auth.login.LoginException;

/**
 * Created by theny550 on 2015/8/8.
 */

public class StorageMain {
    public String storagepath,storagefile;
    public  static int storagebytes=1024*1024*15;
    //是否存在SD卡
    public boolean hasSDcard(){
        File[] sdcard =MainActivity.mContext.getExternalMediaDirs();
        if (MainActivity.platform.contains("mt6735")){
            Log.e("ThenHelper","platform can't support addstorage");
            return false;
        }
        if (sdcard[1]!=null){
            return true;
        }
        return false;
    }
    //是否存在手机存储
    public boolean hasPhonestorage() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    //获取手机剩余空间信息
    public  StorageConfig getstorageInfo(){
        StorageConfig config=new StorageConfig();
        if (MainActivity.selectedstorage.equals("Phone Storage")) {
        //    String state = Environment.getExternalStorageState();
          //  if (Environment.MEDIA_MOUNTED.equals(state)) {
                File storageDir = Environment.getExternalStorageDirectory();
                StatFs sf = new StatFs(storageDir.getPath());
                config.setBlockSize(sf.getBlockSizeLong());
                config.setBlockCount(sf.getBlockCountLong());
                config.setAvailCount(sf.getAvailableBlocksLong());
                config.setTotalsize(config.getBlockSize() * config.getBlockCount() / 1024);
                config.setTotalavailsize(config.getAvailCount() * config.getBlockSize() / 1024);
                // Log.e("ThenHelper", "block大小:" + config.getBlockSize() + ",block数目:" + config.getBlockCount() + ",总大小:" + config.getTotalsize() + "KB");
                //  Log.e("ThenHelper", "可用的block数目：:"+ config.getAvailCount()+",剩余空间:"+ config.getTotalavailsize()+"KB");
        //    }
            return config;
        }
        if (MainActivity.selectedstorage.equals("SDcard")) {
            File[] sdcard =MainActivity.mContext.getExternalMediaDirs();
            StatFs sf=new StatFs(sdcard[1].getAbsolutePath());
            config.setBlockSize(sf.getBlockSizeLong());
            config.setBlockCount(sf.getBlockCountLong());
            config.setAvailCount(sf.getAvailableBlocksLong());
            config.setTotalsize(config.getBlockSize() * config.getBlockCount() / 1024);
            config.setTotalavailsize(config.getAvailCount() * config.getBlockSize() / 1024);
          //  Log.e("ThenHelper", "block大小:" + config.getBlockSize() + ",block数目:" + config.getBlockCount() + ",总大小:" + config.getTotalsize() + "KB");
           // Log.e("ThenHelper", "可用的block数目：:" + config.getAvailCount() + ",剩余空间:" + config.getTotalavailsize()+"KB");
            return config;
        }
        if (MainActivity.selectedstorage.equals("Data")) {
            File storageDir = MainActivity.mContext.getApplicationContext().getFilesDir();
            StatFs sf = new StatFs(storageDir.getAbsolutePath());
            config.setBlockSize(sf.getBlockSizeLong());
            config.setBlockCount(sf.getBlockCountLong());
            config.setAvailCount(sf.getAvailableBlocksLong());
            config.setTotalsize(config.getBlockSize() * config.getBlockCount() / 1024);
            config.setTotalavailsize(config.getAvailCount() * config.getBlockSize() / 1024);
            //  Log.e("ThenHelper", "block大小:" + config.getBlockSize() + ",block数目:" + config.getBlockCount() + ",总大小:" + config.getTotalsize() + "KB");
            // Log.e("ThenHelper", "可用的block数目：:" + config.getAvailCount() + ",剩余空间:" + config.getTotalavailsize()+"KB");
            return config;
        }
        return config;
    }

    public String AvailableSize () {
        String info=null;
        StorageConfig config=getstorageInfo();
        return info;
    }
    //创建填充文件
    public String CreateFileStorage() {
        SimpleDateFormat sDateFormat = new SimpleDateFormat("HHmmssSSS");
        String date = sDateFormat.format(new Date());
        if (MainActivity.selectedstorage.equals("SDcard")) {
            File[] sdcard = MainActivity.mContext.getExternalMediaDirs();
            storagepath = sdcard[1].getAbsolutePath();
        }
        if (MainActivity.selectedstorage.equals("Phone Storage")) {
            storagepath = Environment.getExternalStorageDirectory()+"/ThenHelper/FillStorage";
        }
        if (MainActivity.selectedstorage.equals("Data")) {
            storagepath =MainActivity.mContext.getApplicationContext().getFilesDir().getAbsolutePath();
        }
        storagefile=storagepath+"/storagedata"+date;
        File Spath = new File(storagepath);
        File Sfile = new File(storagefile);
        if (!Spath.exists()) {
            Spath.mkdirs();
        }
        if (!Sfile.exists()) {
            try {
                Sfile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return storagefile;
    }

    //清除文件
    public int  Cleanfiles(){
        int filecount=0;
        if (MainActivity.selectedstorage.equals("SDcard")) {
            File[] sdcard = MainActivity.mContext.getExternalMediaDirs();
            storagepath = sdcard[1].getAbsolutePath();
        }
        if (MainActivity.selectedstorage.equals("Phone Storage")) {
            storagepath = Environment.getExternalStorageDirectory()+"/ThenHelper/FillStorage";
        }
        if (MainActivity.selectedstorage.equals("Data")) {
            storagepath = MainActivity.mContext.getApplicationContext().getFilesDir().getAbsolutePath();
        }
        File StoragePath = new File(storagepath);
        if (!StoragePath.exists()){
            Log.e("ThenHelper", "does not exist "+storagepath);
        }else {
            File[] files = StoragePath.listFiles();
            for (File file : files) {
                file.delete();
                filecount++;
            }
        }
        return filecount;
    }
    //显示信息
    public void showinfo(){
        StorageConfig storageconfig=getstorageInfo();
        DecimalFormat df  = new DecimalFormat("######0.00");
        if (MainActivity.selectedstorage.equals("SDcard")) {
            File[] sdcard = MainActivity.mContext.getExternalMediaDirs();
            storagepath = sdcard[1].getAbsolutePath();
            MainActivity.StorageInfo.setText("SDcard Info :" + "\n" +
                    "Block Size : " + storageconfig.getBlockSize() + "byte\n" +
                    "Total Size : " + storageconfig.getTotalsize() + "KB ==" + storageconfig.getTotalsize() / 1024 + "MB" + "==" +
                    df.format(((double) storageconfig.getTotalsize()) / 1024 / 1024) + "GB\n"+
                            "Save folder : "+storagepath);
            MainActivity.availablesize.setText("Available Size : " + storageconfig.getTotalavailsize() + "KB==" + storageconfig.getTotalavailsize() / 1024 + "MB==" +
                    df.format(((double) storageconfig.getTotalavailsize()) / 1024 / 1024) + "GB\n"
            );
        }
        if (MainActivity.selectedstorage.equals("Phone Storage")) {
            storagepath = Environment.getExternalStorageDirectory()+"/ThenHelper/FillStorage";
            MainActivity.StorageInfo.setText("Phone Storage Info :" + "\n" +
                    "Block Size : " + storageconfig.getBlockSize() + "byte\n" +
                    "Total Size : " + storageconfig.getTotalsize() + "KB ==" + storageconfig.getTotalsize() / 1024 + "MB" + "==" +
                    df.format(((double) storageconfig.getTotalsize()) / 1024 / 1024) + "GB\n"+
                    "Save folder : "+storagepath);
            MainActivity.availablesize.setText("Available Size : " + storageconfig.getTotalavailsize() + "KB==" + storageconfig.getTotalavailsize() / 1024 + "MB==" +
                            df.format(((double) storageconfig.getTotalavailsize()) / 1024 / 1024) + "GB\n"
            );
        }
        if (MainActivity.selectedstorage.equals("Data")) {
            storagepath = MainActivity.mContext.getApplicationContext().getFilesDir().getAbsolutePath();
            MainActivity.StorageInfo.setText("Data Info :" + "\n" +
                    "Block Size : " + storageconfig.getBlockSize() + "byte\n" +
                    "Total Size : " + storageconfig.getTotalsize() + "KB ==" + storageconfig.getTotalsize() / 1024 + "MB" + "==" +
                    df.format(((double) storageconfig.getTotalsize()) / 1024 / 1024) + "GB\n" +
                    "Save folder : " + storagepath);
            MainActivity.availablesize.setText("Available Size : " + storageconfig.getTotalavailsize() + "KB==" + storageconfig.getTotalavailsize() / 1024 + "MB==" +
                            df.format(((double) storageconfig.getTotalavailsize()) / 1024 / 1024) + "GB\n"
            );
        }
    }

    //开始填充
    public static void WriteToStorage(String FileName) {
//            if (MainActivity.selectedstorage.equals("SDcard")) {
//                storagebytes = 1024 * 1024 * 15;
//            }
//            if (MainActivity.selectedstorage.equals("Phone Storage")) {
//                storagebytes = 1024 * 1024 * 25;
//            }
//        if (MainActivity.selectedstorage.equals("Data")) {
//            storagebytes = 1024 * 1024 * 25;
//        }
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(FileName,true);
            byte[] bytes=new byte[storagebytes];
            fileOutputStream.write(bytes);
            fileOutputStream.close();
           // fileOutputStream.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    public static void WriteToStorageKB(String FileName) {
            storagebytes = 1024;
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(FileName,true);
            byte[] bytes=new byte[storagebytes];
            fileOutputStream.write(bytes);
            fileOutputStream.close();
            // fileOutputStream.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    public static void WriteToStorageMB(String FileName) {
        storagebytes = 1024*1024;
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(FileName,true);
            byte[] bytes=new byte[storagebytes];
            fileOutputStream.write(bytes);
            fileOutputStream.close();
            // fileOutputStream.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    public static void WriteToStorageGB(String FileName) {
        storagebytes = 10737418;
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(FileName,true);
            byte[] bytes=new byte[storagebytes];
            fileOutputStream.write(bytes);
            fileOutputStream.close();
            // fileOutputStream.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    /**
     * 读文件
     * <br>------------------------------<br>
     * @param path
     * @return
     * @throws IOException
     */
    private final static Charset charset = Charset.forName("GBK");

    private static String read(String path) throws IOException {
        if (path == null || path.length() == 0) return null;
        FileInputStream fileInputStream =  new FileInputStream(path);
        FileChannel fileChannel =  fileInputStream.getChannel();
        MappedByteBuffer mappedByteBuffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, 0, fileChannel.size());
        fileInputStream.close();
        fileChannel.close();
        String str = charset.decode(mappedByteBuffer).toString();
        mappedByteBuffer = null;
        return str;
    }

    /**
     * 追加内容
     * <br>------------------------------<br>
     * @param path
     * @param str
     * @return
     * @throws IOException
     */
    private static MappedByteBuffer append(String path, String str) throws IOException {
        if (str == null || str.length() == 0) return null;
        RandomAccessFile randomAccessFile = new RandomAccessFile(path, "rw");
        FileChannel fileChannel = randomAccessFile.getChannel();
        byte[] bytes = str.getBytes();
        long size = fileChannel.size() + bytes.length;
        MappedByteBuffer mappedByteBuffer = fileChannel.map(FileChannel.MapMode.READ_WRITE, 0, size);
        fileChannel.close();
        randomAccessFile.close();

        int position = mappedByteBuffer.limit() - bytes.length;
        mappedByteBuffer.position(position);
        mappedByteBuffer.put(bytes);
        mappedByteBuffer.force();
        mappedByteBuffer.flip();
        return mappedByteBuffer;
    }

    /**
     * 文件复制
     * <br>------------------------------<br>
     * @param srcfilePath
     * @param targetPath
     * @throws IOException
     */
    private static void copy(String srcfilePath, String targetPath) throws IOException {
        File file = new File(targetPath);
        if (!file.getParentFile().exists()) {
            file.mkdirs();
        }
        RandomAccessFile inRandomAccessFile = new RandomAccessFile(srcfilePath, "r");
        FileChannel inFileChannel = inRandomAccessFile.getChannel();
        MappedByteBuffer inMappedByteBuffer = inFileChannel.map(FileChannel.MapMode.READ_ONLY, 0, inFileChannel.size());
        inRandomAccessFile.close();
        inFileChannel.close();

        RandomAccessFile outRandomAccessFile = new RandomAccessFile(targetPath, "rw");
        FileChannel outFileChannel = outRandomAccessFile.getChannel();
        MappedByteBuffer outMappedByteBuffer = outFileChannel.map(FileChannel.MapMode.READ_WRITE, 0, inMappedByteBuffer.capacity());
        outMappedByteBuffer.put(inMappedByteBuffer);
        outMappedByteBuffer.force();
        outRandomAccessFile.close();
        outFileChannel.close();
        outMappedByteBuffer.flip();
    }

    /**
     * 不会更新到文件 只会更新虚拟内存
     * <br>------------------------------<br>
     * @param path
     * @param str
     * @return
     * @throws IOException
     */
    private static MappedByteBuffer doTestPrivateMode(String path, String str) throws IOException {
        if (str == null || str.length() == 0) return null;
        RandomAccessFile randomAccessFile = new RandomAccessFile(path, "rw");
        FileChannel fileChannel = randomAccessFile.getChannel();
        byte[] bytes = str.getBytes();
        long size = fileChannel.size() + bytes.length;
        MappedByteBuffer mappedByteBuffer = fileChannel.map(FileChannel.MapMode.PRIVATE, 0, size);
        mappedByteBuffer.put(bytes);
        fileChannel.close();
        randomAccessFile.close();

        mappedByteBuffer.flip();
        System.out.println(charset.decode(mappedByteBuffer));
        return mappedByteBuffer;
    }
}
