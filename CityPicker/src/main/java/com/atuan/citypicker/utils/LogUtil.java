package com.atuan.citypicker.utils;

import android.util.Log;

import com.atuan.citypicker.BuildConfig;

public class LogUtil {
    static String className;
    static String methodName;
    static int lineNumber;
    static StringBuffer str = new StringBuffer();
    static long lastTime = 0;
    public static boolean isDebug = BuildConfig.DEBUG;

    public static void i(String message) {
        if (isDebug) {
            getNames(new Throwable().getStackTrace());
            Log.i(className, createLog(message));
        }
    }

    public static void i(String tag, String msg) {
        if (isDebug) {
            getNames(new Throwable().getStackTrace());
            Log.i(className, createLog(tag + " " + msg));
        }
    }

    public static void w(String tag, String msg) {
        if (isDebug) {
            getNames(new Throwable().getStackTrace());
            Log.w(className, createLog(tag + " " + msg));
        }
    }

    public static void w(String message) {
        if (isDebug) {
            getNames(new Throwable().getStackTrace());
            Log.w(className, createLog(message));
        }
    }

    public static void d(String tag, String msg) {
        if (isDebug) {
            getNames(new Throwable().getStackTrace());
            Log.d(className, createLog(tag + " " + msg));
        }
    }

    public static void d(String message) {
        if (isDebug) {
            getNames(new Throwable().getStackTrace());
            Log.d(className, createLog(message));
        }
    }

    public static void e(String tag, String msg) {
        if (isDebug) {
            getNames(new Throwable().getStackTrace());
            Log.e(className, createLog(tag + " " + msg));
        }
    }

    public static void e(String message) {
        if (isDebug) {
            getNames(new Throwable().getStackTrace());
            Log.e(className, createLog(message));
        }
    }

    private static void getNames(StackTraceElement[] sElements) {
        try {
            className = sElements[1].getFileName();
            methodName = sElements[1].getMethodName();
            lineNumber = sElements[1].getLineNumber();
        } catch (Exception e) {
        }
    }

    private static String createLog(String msg) {
        if (msg == null) {
            msg = "null";
        }
        str.setLength(0);
        str.append(methodName).append("():[").append(lineNumber).append("]=>").append(msg);
        return str.toString();
    }

}