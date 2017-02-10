package com.shayke.galleryfrgament;

import android.util.Log;

import java.text.MessageFormat;


public class LogUtil {

    //TODO: use your own TAG here
    private static final String TAG = "MY_TAG";

    /*********************************
     * public static methods
     *********************************/

    /**
     * utility method to place in methods just for the sake of seeing that they were called
     */
    public static void logMethodCalled() {
        doLog(Log.DEBUG, "called");
    }

    public static void v(String text) {
        doLog(Log.VERBOSE, text);
    }

    public static void d(String text) {
        doLog(Log.DEBUG, text);
    }

    public static void i(String text) {
        doLog(Log.INFO, text);
    }

    public static void w(String text) {
        doLog(Log.WARN, text);
    }

    public static void e(String text) {
        doLog(Log.ERROR, text);
    }

    /*********************************
     * private static methods
     *********************************/

    private static void doLog(int logLevel, String logText) {

        StackTraceElement[] stackTrace = Thread.currentThread()
                .getStackTrace();

        //take stackTrace element at index 4 because:
        //0: VMStack.getThreadStackTrace(Native Method)
        //1: java.lang.Thread.getStackTrace
        //2: LogUtil -> doLog method (this method)
        //3: LogUtil -> log method
        //4: this is the calling method!
        if (stackTrace != null && stackTrace.length > 4) {

            StackTraceElement element = stackTrace[4];

            String fullClassName = element.getClassName();
            String simpleClassName = fullClassName.substring(fullClassName.lastIndexOf(".") + 1);

            //add class and method data to logText
            logText = MessageFormat.format("T:{0} | {1} , {2}() | {3}", Thread.currentThread()
                    .getId(), simpleClassName, element.getMethodName(), logText);
        }

        Log.println(logLevel, TAG, logText);
    }

}
/*
//main activity

LogUtil.logMethodCalled();

      /*  LogUtil.v("test v");
        LogUtil.d("test d");
        LogUtil.i("test i");
        LogUtil.w("test w");
        LogUtil.e("test e");
   */
