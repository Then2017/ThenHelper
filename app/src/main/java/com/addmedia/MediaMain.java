package com.addmedia;

import android.bluetooth.le.ScanFilter;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import com.main.thenhelper.MainActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Created by theny550 on 2015/8/8.
 */
public class MediaMain {
    public String resourcespath= Environment.getExternalStorageDirectory()+"/ThenHelper/Media";
    public static String musicpath,videopath,picturepath;
   //  File Mfile,Vfile,Pfile;
    //创建文件及资源
    public void CreateFileMedia() {
        musicpath=resourcespath+"/Music";
        videopath=resourcespath+"/Video";
        picturepath=resourcespath+"/Picture";
        File Rpath=new File(resourcespath);
        File Mpath = new File(musicpath);
        File Vpath = new File(videopath);
        File Ppath = new File(picturepath);
        if (!Rpath.exists()) {
            Rpath.mkdir();
        }
        if (!Mpath.exists()) {
            Mpath.mkdir();
        }
        if (!Vpath.exists()) {
            Vpath.mkdir();
        }
        if (!Ppath.exists()) {
            Ppath.mkdir();
        }
//         Mfile = new File(musicpath+"/test.mp3");
//         Vfile = new File(videopath+"/test.mp4");
//         Pfile = new File(picturepath+"/test.jpg");
//        if (!Mfile.exists()&&MainActivity.selectedmedia.equals("Music")) {
//            try {
//                copyresources(musicpath+"/test.mp3","test.mp3");
//                Scanner(musicpath+"/test.mp3");
//                Log.e("ThenHelper", "copyresources music successful");
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            return true;
//        }
//        if (!Vfile.exists()&&MainActivity.selectedmedia.equals("Video")) {
//            try {
//                copyresources(videopath+"/test.mp4","test.mp4");
//                Scanner(videopath + "/test.mp4");
//                Log.e("ThenHelper", "copyresources video successful");
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            return true;
//        }
//        if (!Pfile.exists()&&MainActivity.selectedmedia.equals("Picture")) {
//            try {
//                copyresources(picturepath+"/test.jpg","test.jpg");
//                Scanner(picturepath + "/test.jpg");
//                Log.e("ThenHelper", "copyresources picture successful");
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            return true;
//        }
  //      Log.e("ThenHelper","do not need copy resources");
 //       return false;
    }
    public void copymusic(String targetPath) {
        try {
           // copy(musicpath+"/test.mp3",targetPath);
            InputStream myInput;
            OutputStream myOutput = new FileOutputStream(targetPath);
            myInput = MainActivity.mContext.getResources().getAssets().open("test.mp3");
            byte[] buffer = new byte[1024];
            int length = myInput.read(buffer);
            while(length > 0)
            {
                myOutput.write(buffer, 0, length);
                length = myInput.read(buffer);
            }
           // myOutput.flush();
            myInput.close();
            myOutput.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void copyvideo(String targetPath) {
        try {
          //  copy(videopath+"/test.mp4",targetPath);
            InputStream myInput;
            OutputStream myOutput = new FileOutputStream(targetPath);
            myInput = MainActivity.mContext.getResources().getAssets().open("test.mp4");
            byte[] buffer = new byte[1024];
            int length = myInput.read(buffer);
            while(length > 0)
            {
                myOutput.write(buffer, 0, length);
                length = myInput.read(buffer);
            }
          //  myOutput.flush();
            myInput.close();
            myOutput.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void copypicture(String targetPath) {
        try {
           // copy(picturepath+"/test.jpg",targetPath);
            InputStream myInput;
            OutputStream myOutput = new FileOutputStream(targetPath);
            myInput = MainActivity.mContext.getResources().getAssets().open("test.jpg");
            byte[] buffer = new byte[1024];
            int length = myInput.read(buffer);
            while(length > 0)
            {
                myOutput.write(buffer, 0, length);
                length = myInput.read(buffer);
            }
          //  myOutput.flush();
            myInput.close();
            myOutput.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    private void copyresources(String strOutFileName,String resources) throws IOException
//    {
//        InputStream myInput;
//        OutputStream myOutput = new FileOutputStream(strOutFileName);
//        myInput = MainActivity.mContext.getResources().getAssets().open(resources);
//        byte[] buffer = new byte[1024];
//        int length = myInput.read(buffer);
//        while(length > 0)
//        {
//            myOutput.write(buffer, 0, length);
//            length = myInput.read(buffer);
//        }
//        myOutput.flush();
//        myInput.close();
//        myOutput.close();
//    }

    //删除创建的媒体文件
    public int deletemedia(String selectedmedia) {
        musicpath=resourcespath+"/Music";
        videopath=resourcespath+"/Video";
        picturepath=resourcespath+"/Picture";
        File Mpath = new File(musicpath);
        File Vpath = new File(videopath);
        File Ppath = new File(picturepath);
        int filecount=0;
        if(selectedmedia.equals("Music")) {
            if (!Mpath.exists()) {
                Log.e("ThenHelper", "does not exist musicpath ");
            } else {
                File[] files = Mpath.listFiles();
                for (File file : files) {
                    file.delete();
                    Scanner(file.getAbsolutePath());
                    filecount++;
                }
            }
            return filecount;
        }
        if(selectedmedia.equals("Video")) {
            if (!Vpath.exists()) {
                Log.e("ThenHelper", "does not exist videopath ");
            } else {
                File[] files = Vpath.listFiles();
                for (File file : files) {
                    file.delete();
                    Scanner(file.getAbsolutePath());
                    filecount++;
                }
            }
            return filecount;
        }
        if(selectedmedia.equals("Picture")) {
            if (!Ppath.exists()) {
                Log.e("ThenHelper", "does not exist picturepath ");
            } else {
                File[] files = Ppath.listFiles();
                for (File file : files) {
                    file.delete();
                    Scanner(file.getAbsolutePath());
                    filecount++;
                }
            }
            return filecount;
        }
        return filecount;
    }
    //扫描媒体
    //"/storage/emulated/0/ThenHelper/Media/Video/test.mp4"
    public void Scanner (String path) {
            MediaScannerConnection.scanFile(MainActivity.mContext, new String[]{path}, null, null);
    }
//    public void Scanner (File a) {
//        File file=a;
//        if(!file.exists()){
//            return;
//        }
//        if(file.isDirectory()){
//            File[] files = file.listFiles();
//            for(File item: files) {
//                MediaScannerConnection.scanFile(MainActivity.mContext, new String[]{item.getAbsolutePath()}, null, null);
//            }
//        }
//        if(file.isFile()){
//            MediaScannerConnection.scanFile(MainActivity.mContext, new String[]{file.getAbsolutePath()}, null, null);
//        }
//    }
    //删除所有的媒体文件
    public int deletemediaall(){
        MainActivity.mContext.getContentResolver().delete(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null);
        if (MainActivity.selectedmedia.equals("Music")) {
            return MainActivity.mContext.getContentResolver().delete(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null);
        }
        if (MainActivity.selectedmedia.equals("Video")) {
            return MainActivity.mContext.getContentResolver().delete(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, null, null);
        }
        if (MainActivity.selectedmedia.equals("Picture")) {
            return MainActivity.mContext.getContentResolver().delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null);
        }
        return 0;
    }
    //统计创建的媒体信息
    public int Totalcreatedmedia(){
        musicpath=resourcespath+"/Music";
        videopath=resourcespath+"/Video";
        picturepath=resourcespath+"/Picture";
        File Mpath = new File(musicpath);
        File Vpath = new File(videopath);
        File Ppath = new File(picturepath);
        int filecount=0;
        if (MainActivity.selectedmedia.equals("Music")) {
            if(Mpath.exists()){
                File[] files = Mpath.listFiles();
                for (File file : files) {
                    if(file.getName().contains(".mp3")) {
                        filecount++;
                    }
                }
            }
            return filecount;
        }
        if (MainActivity.selectedmedia.equals("Video")) {
            if(Vpath.exists()){
                File[] files = Mpath.listFiles();
                for (File file : files) {
                    if(file.getName().contains(".mp4")) {
                        filecount++;
                    }
                }
            }
            return filecount;
        }
        if (MainActivity.selectedmedia.equals("Picture")) {
            if(Ppath.exists()){
                File[] files = Mpath.listFiles();
                for (File file : files) {
                    if(file.getName().contains(".jpg")) {
                        filecount++;
                    }
                }
            }
            return filecount;
        }
        return 0;
    }
    //统计所有媒体信息
    public int Totalmedia(){
        if (MainActivity.selectedmedia.equals("Music")) {
            ContentResolver contentResolver = MainActivity.mContext.getContentResolver();
            Cursor query = contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null,null);
            int i = query.getCount();
            query.close();
            return i;
        }
        if (MainActivity.selectedmedia.equals("Video")) {
            ContentResolver contentResolver = MainActivity.mContext.getContentResolver();
            Cursor query = contentResolver.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, null, null, null, null);
            int i = query.getCount();
            query.close();
            return i;
        }
        if (MainActivity.selectedmedia.equals("Picture")) {
            ContentResolver contentResolver = MainActivity.mContext.getContentResolver();
            Cursor query = contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null, null);
            int i = query.getCount();
            query.close();
            return i;
        }
        return 0;
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
}
