package com.screenrecord;

import android.content.Context;
import android.graphics.Point;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.projection.MediaProjection;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;
import android.media.*;

import com.main.thenhelper.MainActivity;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;

/**
 * Created by Then on 2015/9/5.
 */
public class ScreenRecord implements MediaRecorder.OnErrorListener{
    private Context mcontext;
    MediaProjection mediaProjection;
    public String ScreenRecordPath;
    String filepath;
    private int mScreenDensity;
    private  int DISPLAY_WIDTH ;
    private  int DISPLAY_HEIGHT ;
    private VirtualDisplay mVirtualDisplay;
    private MediaProjectionCallback mMediaProjectionCallback;
    private MediaRecorder mMediaRecorder;
    private boolean hasaudio;
    private String DIYfilename;
    private int bitratenum;

    public ScreenRecord(Context context){
        mcontext=context;
    }

    public String setRecord(MediaProjection mProjection,String Resolution,String filename,int bitrate,boolean audio) {
        ScreenRecordPath= Environment.getExternalStorageDirectory()+ "/ThenHelper/ScreenRecord/";
        SimpleDateFormat filedf = new SimpleDateFormat("MMdd_HHmm_ss");
        String fileDate = filedf.format(new java.util.Date());
        File filefolder = new File(ScreenRecordPath);
        if (!filefolder.exists()) {
            filefolder.mkdirs();
        }
        mediaProjection=mProjection;
        DIYfilename=filename;
        bitratenum=bitrate;
        hasaudio= audio;

        filepath=ScreenRecordPath+DIYfilename+"_"+fileDate+".mp4";
        setResolution(Resolution);
        stratRecord();
        Log.e("ThenHelper", "Screen Recording Start!");
        return filepath;
    }

    private void stratRecord() {
        mMediaRecorder = new MediaRecorder();
        initRecorder();
        mVirtualDisplay = createVirtualDisplay();
        mMediaProjectionCallback = new MediaProjectionCallback();
        mediaProjection.registerCallback(mMediaProjectionCallback,null);
    }

    public void stopScreen() {
        if (mVirtualDisplay != null) {
            mVirtualDisplay.release();
        }
        if(mMediaProjectionCallback!=null){
            mediaProjection.unregisterCallback(mMediaProjectionCallback);
        }
        if(mediaProjection!=null){
            mediaProjection.stop();
        }
        if(mMediaRecorder!=null) {
            mMediaRecorder.setOnErrorListener(null);
//            mMediaRecorder.setOnInfoListener(null);
//            mMediaRecorder.setPreviewDisplay(null);
            mMediaRecorder.stop();
            mMediaRecorder.reset();
            mMediaRecorder.release();
        }
        Log.e("ThenHelper", "Recording Stopped");
        if(filepath!=null) {
            Scanner(filepath);
        }
        mVirtualDisplay=null;
        mMediaRecorder=null;
        mediaProjection=null;
        mMediaProjectionCallback=null;
    }

    private VirtualDisplay createVirtualDisplay() {
        return mediaProjection.createVirtualDisplay("ScreenRecord",
                DISPLAY_WIDTH, DISPLAY_HEIGHT, mScreenDensity, DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
                mMediaRecorder.getSurface(), null /*Callbacks*/, null /*Handler*/);
    }

    private class MediaProjectionCallback extends MediaProjection.Callback {
        @Override
        public void onStop() {
            stopScreen();
            Log.e("ThenHelper", "MediaProjection Stopped by others");
        }
    }

    private void initRecorder() {
        mMediaRecorder.reset();
        mMediaRecorder.setOnErrorListener(this);
        if(hasaudio) {
            mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        }
        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.SURFACE);
        mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
        if(hasaudio) {
            mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mMediaRecorder.setAudioEncodingBitRate(16);
            mMediaRecorder.setAudioSamplingRate(44100);
        }
        mMediaRecorder.setVideoEncodingBitRate(bitratenum * 1024 * 1024);
        mMediaRecorder.setVideoFrameRate(30);
        mMediaRecorder.setVideoSize(DISPLAY_WIDTH, DISPLAY_HEIGHT);
        mMediaRecorder.setOutputFile(filepath);
        try {
            mMediaRecorder.prepare();
            mMediaRecorder.start();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void Scanner (String path) {
        MediaScannerConnection.scanFile(MainActivity.mContext, new String[]{path}, null, null);
    }

    //设置分辨率
    private void setResolution(String Resolution){
        WindowManager windowManager = (WindowManager) mcontext.getSystemService(Context.WINDOW_SERVICE);
        Point point = new Point();
        windowManager.getDefaultDisplay().getRealSize(point);
        DisplayMetrics screendm = mcontext.getResources().getDisplayMetrics();
        DISPLAY_WIDTH = point.x;
        DISPLAY_HEIGHT = point.y;
        mScreenDensity = screendm.densityDpi;
        if(Resolution.equals("Adaptive")) {
            return;
        }else if(Resolution.equals("1080X1920")){
            DISPLAY_HEIGHT=1920;
            DISPLAY_WIDTH=1080;
        }else if(Resolution.equals("720X1280")){
            DISPLAY_HEIGHT=1280;
            DISPLAY_WIDTH=720;
        }else if(Resolution.equals("540X960")){
            DISPLAY_HEIGHT=960;
            DISPLAY_WIDTH=540;
        }else if(Resolution.equals("480X854")){
            DISPLAY_HEIGHT=854;
            DISPLAY_WIDTH=480;
        }else if(Resolution.equals("480X800")){
            DISPLAY_HEIGHT=800;
            DISPLAY_WIDTH=480;
        }else if(Resolution.equals("320X480")){
            DISPLAY_HEIGHT=480;
            DISPLAY_WIDTH=320;
        }
    }

    //删除所有
    public int deleteall(){
        ScreenRecordPath= Environment.getExternalStorageDirectory()+ "/ThenHelper/ScreenRecord/";
        int filecount=0;
        File ScreenPath = new File(ScreenRecordPath);
        if (!ScreenPath.exists()){
            Log.e("ThenHelper", "does not exist BatteryMonitorPath ");
        }else {
            File[] files = ScreenPath.listFiles();
            for (File file : files) {
                file.delete();
                Scanner(file.getAbsolutePath());
                filecount++;
            }
        }
        Scanner(ScreenRecordPath);
        return filecount;
    }

    //防错
    @Override
    public void onError(MediaRecorder mediaRecorder, int i, int i1) {
        try {
            if (mediaRecorder != null)
                mediaRecorder.reset();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}


////***********************************************
//    // in the same activity
//    private static final String VIDEO_MIME_TYPE = "video/avc";
//    private static final int VIDEO_WIDTH = 1920;
//    private static final int VIDEO_HEIGHT = 1080;
//    // …
//    private boolean mMuxerStarted = false;
//    private MediaProjection mMediaProjection;
//    private Surface mInputSurface;
//    private MediaMuxer mMuxer;
//    private MediaCodec mVideoEncoder;
//    private MediaCodec.BufferInfo mVideoBufferInfo;
//    private int mTrackIndex = -1;
//
//    private final Handler mDrainHandler = new Handler(Looper.getMainLooper());
//    private Runnable mDrainEncoderRunnable = new Runnable() {
//        @Override
//        public void run() {
//            drainEncoder();
//        }
//    };
//
//    private void startRecording(String filepath) {
//        DisplayManager dm = (DisplayManager)mcontext.getSystemService(Context.DISPLAY_SERVICE);
//        Display defaultDisplay = dm.getDisplay(Display.DEFAULT_DISPLAY);
//        if (defaultDisplay == null) {
//            throw new RuntimeException("No display found.");
//        }
//        prepareVideoEncoder();
//
//        try {
//            mMuxer = new MediaMuxer(filepath, MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4);
//        } catch (IOException ioe) {
//            throw new RuntimeException("MediaMuxer creation failed", ioe);
//        }
//
//        // Get the display size and density.
////        DisplayMetrics metrics = mcontext.getResources().getDisplayMetrics();
////        int screenWidth = metrics.widthPixels;
////        int screenHeight = metrics.heightPixels;
////        int screenDensity = metrics.densityDpi;
//        WindowManager windowManager = (WindowManager) MainActivity.mContext.getSystemService(Context.WINDOW_SERVICE);
//        Point point = new Point();
//        windowManager.getDefaultDisplay().getRealSize(point);
//        DisplayMetrics screendm = mcontext.getResources().getDisplayMetrics();
//        int mWidth = point.y;
//        int mHeight =point.x;
//        int mScreenDensity = screendm.densityDpi;
//
//        // Start the video input.
//        mediaProjection.createVirtualDisplay("Recording Display", mWidth,
//                mHeight, mScreenDensity, DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR /* flags */, mInputSurface, null /* callback */, null /* handler */);
//        // Start the encoders
//        drainEncoder();
//    }
//
//    private void prepareVideoEncoder() {
//        mVideoBufferInfo = new MediaCodec.BufferInfo();
//        MediaFormat format = MediaFormat.createVideoFormat(VIDEO_MIME_TYPE, VIDEO_WIDTH, VIDEO_HEIGHT);
//        int frameRate = 30; // 30 fps
//
//        // Set some required properties. The media codec may fail if these aren't defined.
//        format.setInteger(MediaFormat.KEY_COLOR_FORMAT,
//                MediaCodecInfo.CodecCapabilities.COLOR_FormatSurface);
//        format.setInteger(MediaFormat.KEY_BIT_RATE, 6000000); // 6Mbps
//        format.setInteger(MediaFormat.KEY_FRAME_RATE, frameRate);
//        format.setInteger(MediaFormat.KEY_CAPTURE_RATE, frameRate);
//        format.setInteger(MediaFormat.KEY_REPEAT_PREVIOUS_FRAME_AFTER, 1000000 / frameRate);
//        format.setInteger(MediaFormat.KEY_CHANNEL_COUNT, 1);
//        format.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, 1); // 1 seconds between I-frames
//
//        // Create a MediaCodec encoder and configure it. Get a Surface we can use for recording into.
//        try {
//            mVideoEncoder = MediaCodec.createEncoderByType(VIDEO_MIME_TYPE);
//            mVideoEncoder.configure(format, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);
//            mInputSurface = mVideoEncoder.createInputSurface();
//            mVideoEncoder.start();
//        } catch (IOException e) {
//            releaseEncoders();
//        }
//    }
//
//    private boolean drainEncoder() {
//        mDrainHandler.removeCallbacks(mDrainEncoderRunnable);
//        while (true) {
//            int bufferIndex = mVideoEncoder.dequeueOutputBuffer(mVideoBufferInfo, 0);
//
//            if (bufferIndex == MediaCodec.INFO_TRY_AGAIN_LATER) {
//                // nothing available yet
//                break;
//            } else if (bufferIndex == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED) {
//                // should happen before receiving buffers, and should only happen once
//                if (mTrackIndex >= 0) {
//                    throw new RuntimeException("format changed twice");
//                }
//                mTrackIndex = mMuxer.addTrack(mVideoEncoder.getOutputFormat());
//                if (!mMuxerStarted && mTrackIndex >= 0) {
//                    mMuxer.start();
//                    mMuxerStarted = true;
//                }
//            } else if (bufferIndex < 0) {
//                // not sure what's going on, ignore it
//            } else {
//                ByteBuffer encodedData = mVideoEncoder.getOutputBuffer(bufferIndex);
//                if (encodedData == null) {
//                    throw new RuntimeException("couldn't fetch buffer at index " + bufferIndex);
//                }
//
//                if ((mVideoBufferInfo.flags & MediaCodec.BUFFER_FLAG_CODEC_CONFIG) != 0) {
//                    mVideoBufferInfo.size = 0;
//                }
//
//                if (mVideoBufferInfo.size != 0) {
//                    if (mMuxerStarted) {
//                        encodedData.position(mVideoBufferInfo.offset);
//                        encodedData.limit(mVideoBufferInfo.offset + mVideoBufferInfo.size);
//                        mMuxer.writeSampleData(mTrackIndex, encodedData, mVideoBufferInfo);
//                    } else {
//                        // muxer not started
//                    }
//                }
//
//                mVideoEncoder.releaseOutputBuffer(bufferIndex, false);
//
//                if ((mVideoBufferInfo.flags & MediaCodec.BUFFER_FLAG_END_OF_STREAM) != 0) {
//                    break;
//                }
//            }
//        }
//
//        mDrainHandler.postDelayed(mDrainEncoderRunnable, 10);
//        return false;
//    }
//
//    public void releaseEncoders() {
//        mDrainHandler.removeCallbacks(mDrainEncoderRunnable);
//        if (mMuxer != null) {
//            if (mMuxerStarted) {
//                mMuxer.stop();
//            }
//            mMuxer.release();
//            mMuxer = null;
//            mMuxerStarted = false;
//        }
//        if (mVideoEncoder != null) {
//            mVideoEncoder.stop();
//            mVideoEncoder.release();
//            mVideoEncoder = null;
//        }
//        if (mInputSurface != null) {
//            mInputSurface.release();
//            mInputSurface = null;
//        }
//        if (mediaProjection != null) {
//            mediaProjection.stop();
//            mediaProjection = null;
//        }
//        mVideoBufferInfo = null;
//        mDrainEncoderRunnable = null;
//        mTrackIndex = -1;
//    }

