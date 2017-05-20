package cn.zmh.animation.util;

import android.util.Log;

public class ZLog {
    private static boolean IsDebug = false;
    private static final String TAG = "zmh001_";

    public static void v(String TAG, String msg) {
        Log.v(TAG, msg);
    }

    public static void d(String TAG, String msg) {
        if (IsDebug) {
            Log.d(ZLog.TAG + TAG, msg + " Thread:" + Thread.currentThread());
        }
    }

    public static void e(String TAG, String msg) {
        if (IsDebug) {
            Log.e(ZLog.TAG + TAG, msg + " Thread:" + Thread.currentThread());
        }
    }

}
