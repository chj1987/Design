package com.example.chj.design.utils;

import com.example.chj.design.BuildConfig;
import com.orhanobut.logger.Logger;

/**
 * Created by ff on 2018/5/22.
 */

public class LogUtils {
    public static void v(String tag, String msg) {
        if (BuildConfig.LOG_DEBUG) {
            Logger.t(tag).v(msg);
        }
    }

    public static void v(String tag, String msg, Throwable throwable) {
        if (BuildConfig.LOG_DEBUG) {
            Logger.t(tag).v(msg, throwable);
        }
    }

    public static void d(String tag, String msg) {
        if (BuildConfig.LOG_DEBUG) {
            Logger.t(tag).d(tag, msg);
        }
    }

    public static void d(String tag, String msg, Throwable throwable) {

        if (BuildConfig.LOG_DEBUG) {
            Logger.t(tag).d(tag, msg, throwable);
        }

    }

    public static void i(String tag, String msg) {
        if (BuildConfig.LOG_DEBUG) {
            Logger.t(tag).i(tag, msg);
        }
    }

    public static void i(String tag, String msg, Throwable throwable) {
        if (BuildConfig.LOG_DEBUG) {
            Logger.t(tag).i(tag, msg, throwable);
        }
    }

    public static void w(String tag, String msg) {
        if (BuildConfig.LOG_DEBUG) {
            Logger.t(tag).w(tag, msg);
        }
    }

    public static void w(String tag, String msg, Throwable throwable) {
        if (BuildConfig.LOG_DEBUG) {
            Logger.t(tag).w(tag, msg, throwable);
        }
    }

    public static void e(String tag, String msg) {
        if (BuildConfig.LOG_DEBUG) {
            Logger.t(tag).e(tag, msg);
        }
    }

    public static void e(String tag, String msg, Throwable throwable) {
        if (BuildConfig.LOG_DEBUG) {
            Logger.t(tag).e(tag, msg, throwable);
        }
    }
}

