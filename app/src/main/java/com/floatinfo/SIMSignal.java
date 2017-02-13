package com.floatinfo;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.PhoneStateListener;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;

/**
 * Created by Then on 2015/8/29.
 */
public class SIMSignal {
    private Context mcontext;
    public TelephonyManager telephoneManager;
    PhoneStateListener phoneStateListener;
    GsmCellLocation gsmCellLocation;
    CdmaCellLocation cdmaCellLocation;
    public static String SIMstrength ="unknow or no signal";
    int type = NETWORK_TYPE_UNKNOWN;
   // int networktype = getNetworkClass(type);
    String networkname="",simstatus="",simprovider="";



    public SIMSignal(Context context) {
        mcontext = context;
    }

    //获取网络类型
    public int getNetworkType() {
        ConnectivityManager mConnectivityManager = (ConnectivityManager) mcontext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
        if (mNetworkInfo != null && mNetworkInfo.isAvailable()) {
            Log.e("ThenHelper", " a" + mNetworkInfo.getType());
            return mNetworkInfo.getType();
        }
        return 9999;
    }

    //返回网络类型
    private static final int NETWORK_TYPE_UNAVAILABLE = -1;
    // private static final int NETWORK_TYPE_MOBILE = -100;
    private static final int NETWORK_TYPE_WIFI = -101;
    private static final int NETWORK_CLASS_WIFI = -101;
    private static final int NETWORK_CLASS_UNAVAILABLE = -1;

    private static final int NETWORK_CLASS_UNKNOWN = 0;

    private static final int NETWORK_CLASS_2_G = 1;

    private static final int NETWORK_CLASS_3_G = 2;

    private static final int NETWORK_CLASS_4_G = 3;

    public static final int NETWORK_TYPE_UNKNOWN = 0;

    public static final int NETWORK_TYPE_GPRS = 1;

    public static final int NETWORK_TYPE_EDGE = 2;

    public static final int NETWORK_TYPE_UMTS = 3;

    public static final int NETWORK_TYPE_CDMA = 4;

    public static final int NETWORK_TYPE_EVDO_0 = 5;

    public static final int NETWORK_TYPE_EVDO_A = 6;

    public static final int NETWORK_TYPE_1xRTT = 7;

    public static final int NETWORK_TYPE_HSDPA = 8;

    public static final int NETWORK_TYPE_HSUPA = 9;

    public static final int NETWORK_TYPE_HSPA = 10;

    public static final int NETWORK_TYPE_IDEN = 11;

    public static final int NETWORK_TYPE_EVDO_B = 12;

    public static final int NETWORK_TYPE_LTE = 13;

    public static final int NETWORK_TYPE_EHRPD = 14;

    public static final int NETWORK_TYPE_HSPAP = 15;
    //SIM卡状态常量
    private static final String SIM_ABSENT = "Absent"; //手机内无SIM卡
    private static final String SIM_READY = "Ready"; //SIM卡已准备好
    private static final String SIM_PIN_REQUIRED = "PIN required"; //需要SIM卡的PIN解锁
    private static final String SIM_PUK_REQUIRED = "PUK required"; //需要SIM卡的PUK解锁
    private static final String SIM_NETWORK_LOCKED = "Network locked"; //需要Network PIN解锁
    private static final String SIM_UNKNOWN = "Unknown"; //状态未知
    /**
     * 将SIM卡状态值以字符串形式返回
     * @param simState
     * @return
     */
    private static String getSimstatus(int simState) {
        switch (simState) {
            case TelephonyManager.SIM_STATE_ABSENT:
                return SIM_ABSENT;
            case TelephonyManager.SIM_STATE_READY:
                return SIM_READY;
            case TelephonyManager.SIM_STATE_PIN_REQUIRED:
                return SIM_PIN_REQUIRED;
            case TelephonyManager.SIM_STATE_PUK_REQUIRED:
                return SIM_PUK_REQUIRED;
            case TelephonyManager.SIM_STATE_NETWORK_LOCKED:
                return SIM_NETWORK_LOCKED;
            case TelephonyManager.SIM_STATE_UNKNOWN:
                return SIM_UNKNOWN;
            default:
                //不应该走到这个分支
                return null;
        }
    }
    public static int getNetworkClass(int networkType) {
        switch (networkType) {
            case NETWORK_TYPE_GPRS:
            case NETWORK_TYPE_EDGE:
            case NETWORK_TYPE_CDMA:
            case NETWORK_TYPE_1xRTT:
            case NETWORK_TYPE_IDEN:
                return NETWORK_CLASS_2_G;
            case NETWORK_TYPE_UMTS:
            case NETWORK_TYPE_EVDO_0:
            case NETWORK_TYPE_EVDO_A:
            case NETWORK_TYPE_HSDPA:
            case NETWORK_TYPE_HSUPA:
            case NETWORK_TYPE_HSPA:
            case NETWORK_TYPE_EVDO_B:
            case NETWORK_TYPE_EHRPD:
            case NETWORK_TYPE_HSPAP:
                return NETWORK_CLASS_3_G;
            case NETWORK_TYPE_LTE:
                return NETWORK_CLASS_4_G;
            default:
                return NETWORK_CLASS_UNKNOWN;
        }
    }
    public static String getNetworkClassName(int networkType) {
        switch (networkType) {
            case NETWORK_TYPE_GPRS:
                return "2G GPRS";
            case NETWORK_TYPE_EDGE:
                return "2G EDGE";
            case NETWORK_TYPE_CDMA:
                return "2G CDMA";
            case NETWORK_TYPE_1xRTT:
                return "2G 1xRTT";
            case NETWORK_TYPE_IDEN:
                return "2G IDEN";
            case NETWORK_TYPE_UMTS:
                return "3G UMTS";
            case NETWORK_TYPE_EVDO_0:
                return "3G EVDO_0";
            case NETWORK_TYPE_EVDO_A:
                return "3G EVDO_A";
            case NETWORK_TYPE_HSDPA:
                return "3G HSDPA";
            case NETWORK_TYPE_HSUPA:
                return "3G HSUPA";
            case NETWORK_TYPE_HSPA:
                return "3G HSPA";
            case NETWORK_TYPE_EVDO_B:
                return "3G EVDO_B";
            case NETWORK_TYPE_EHRPD:
                return "3G EHRPD";
            case NETWORK_TYPE_HSPAP:
                return "3G HSPAP";
            case NETWORK_TYPE_LTE:
                return "4G LTE";
            default:
                return "Unknow";
        }
    }
    /**
     * 功能描述：通过手机信号获取基站信息
     * # 通过TelephonyManager 获取lac:mcc:mnc:cell-id
     * # MCC，Mobile Country Code，移动国家代码（中国的为460）；
     * # MNC，Mobile Network Code，移动网络号码（中国移动为0，中国联通为1，中国电信为2）；
     * # LAC，Location Area Code，位置区域码；
     * # CID，Cell Identity，基站编号；
     * # BSSS，Base station signal strength，基站信号强度。
     */

    //获取信号强度
    public void getSIMsignal() {
        telephoneManager = (TelephonyManager) mcontext.getSystemService(Context.TELEPHONY_SERVICE);
        type = telephoneManager.getNetworkType();
        simstatus = getSimstatus(telephoneManager.getSimState());
        networkname= getNetworkClassName(type);
        simprovider=getProvider(telephoneManager);

        // SubscriptionManager subscriptionManager=(SubscriptionManager)scontext.getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE);
        //MSimTelephonyManager
        phoneStateListener = new PhoneStateListener() {
//            @Override //基站位置
//            public void onCellLocationChanged(CellLocation location) {
//                //基站查询网址http://www.minigps.net/cellsearch.html
//                if(simprovider.equals("CUCC")||simprovider.equals("CMCC")) {
//                    // 中国移动和中国联通获取LAC、CID的方式
//                    gsmCellLocation = (GsmCellLocation) telephoneManager.getCellLocation();
//                    int lac = gsmCellLocation.getLac();
//                    int cid = gsmCellLocation.getCid();
//                    Log.e("ThenHelper", "LAC = " + lac + "CID = " + cid);
//                }else {
//                    // 中国电信获取LAC、CID的方式
//                    cdmaCellLocation = (CdmaCellLocation) telephoneManager.getCellLocation();
//                    int lac = cdmaCellLocation.getNetworkId();
//                    int cid = cdmaCellLocation.getBaseStationId()/16;
//                }
//            }

            @Override//数据连接
            public void onDataConnectionStateChanged(int state, int networkType) {
                //数据连接状态改变可能导致网络类型的改变
                simstatus=getSimstatus(telephoneManager.getSimState());
               Log.e("ThenHelper", "onDataConnectionStateChanged " + simstatus);
            }
            @Override//网络状态
            public void onServiceStateChanged(ServiceState serviceState) {
                type= telephoneManager.getNetworkType();
                simstatus=getSimstatus(telephoneManager.getSimState());
                networkname=getNetworkClassName(type);
            }
            @Override//信号强度
            public void onSignalStrengthsChanged(SignalStrength signalStrength) {
                // TODO Auto-generated method stub
                super.onSignalStrengthsChanged(signalStrength);
                if (simstatus.equals("Ready")) {
//                    if (type == telephoneManager.NETWORK_TYPE_UNKNOWN) {
//                        SIMstrength = "unknow network";
//                    }else {
                        int lac=0,cid=0;
                        //基站查询网址http://www.minigps.net/cellsearch.html
                        if(simprovider.equals("CUCC")||simprovider.equals("CMCC")) {
                            // 中国移动和中国联通获取LAC、CID的方式
                            gsmCellLocation = (GsmCellLocation) telephoneManager.getCellLocation();
                            lac = gsmCellLocation.getLac();
                            cid = gsmCellLocation.getCid();
                        }else {
                            // 中国电信获取LAC、CID的方式
                            cdmaCellLocation = (CdmaCellLocation) telephoneManager.getCellLocation();
                            lac = cdmaCellLocation.getNetworkId();
                            cid = cdmaCellLocation.getBaseStationId()/16;
                        }
                        if (type == telephoneManager.NETWORK_TYPE_LTE) {
                            String ssignal = signalStrength.toString();
                            String[] parts = ssignal.split(" ");
                            int dbm = Integer.parseInt(parts[9]);
                            int asu = (dbm + 113) / 2;
                            SIMstrength =networkname + "= " + dbm + "dBm" + " " + asu + "asu\n"+
                                    simprovider+": LAC="+lac+" CID="+cid;
                         //   Log.e("ThenHelper", "111 " + SIMstrength);
                        } else {
                            if (!signalStrength.isGsm()) {
                                int dbm = signalStrength.getCdmaDbm();
                                int asu = (dbm + 113) / 2;
                                SIMstrength =networkname + "= " + dbm + "dBm" + " " + asu + "asu\n"+
                                        simprovider+": LAC="+lac+" CID="+cid;
                            //    Log.e("ThenHelper", "222 " + SIMstrength);
                            } else if (signalStrength.isGsm()) {
                                int asu = signalStrength.getGsmSignalStrength();
                                int dbm = (asu * 2 - 113);
                                SIMstrength =networkname + "= " + dbm + "dBm" + " " + asu + "asu\n"+
                                        simprovider+": LAC="+lac+" CID="+cid;
                          //      Log.e("ThenHelper", "333 " + SIMstrength);
                            }
                        }
                 //   }
                }else {
                    SIMstrength ="SIM "+getSimstatus(telephoneManager.getSimState());
                }
            }
        };
        telephoneManager.listen(phoneStateListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS
                |PhoneStateListener.LISTEN_SERVICE_STATE| PhoneStateListener.LISTEN_DATA_CONNECTION_STATE);
//                |PhoneStateListener.LISTEN_CELL_LOCATION);
        Log.e("ThenHelper", "Start SIMSignal listen");
        // telephoneManager.listen(phoneStateListener, PhoneStateListener.LISTEN_NONE);
    }
/**
      * 获取运营商
      *
      * @return
     */
public static String getProvider(TelephonyManager telephoneManager) {
        String provider = "Unknow :";
        try {
            String IMSI = telephoneManager.getSubscriberId();
            Log.e("ThenHelper", "getProvider.IMSI:" + IMSI);
            if (IMSI == null) {
                if (TelephonyManager.SIM_STATE_READY == telephoneManager.getSimState()) {
                    String operator = telephoneManager.getSimOperator();
                    Log.e("ThenHelper", "getProvider.operator:" + operator);
                    if (operator != null) {
                        if (operator.equals("46000")
                                || operator.equals("46002")
                                || operator.equals("46007")) {
                            provider = "CMCC";
                        } else if (operator.equals("46001")) {
                            provider = "CUCC";
                        } else if (operator.equals("46003")) {
                            provider = "CTCC";
                        } else if(operator.equals("00101")){
                            provider="Test";
                        }
                    }
                }
            } else {
                if (IMSI.startsWith("46000") || IMSI.startsWith("46002")
                        || IMSI.startsWith("46007")) {
                    provider = "CMCC";
                } else if (IMSI.startsWith("46001")) {
                    provider = "CUCC";
                } else if (IMSI.startsWith("46003")) {
                    provider = "CTCC";
                }else if (IMSI.startsWith("00101")) {
                    provider = "Test";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
             return provider;
    }

    //取消监听
    public void Canclelisten() {
        telephoneManager.listen(phoneStateListener, PhoneStateListener.LISTEN_NONE);
        Log.e("ThenHelper", "Cancel SIMSignal listen and mReceiver");
    }
}