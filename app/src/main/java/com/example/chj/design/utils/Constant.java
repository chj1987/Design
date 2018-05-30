package com.example.chj.design.utils;

/**
 * Created by ff on 2018/5/28.
 */

public class Constant {
    public static int MAX_AGE = 60;//在有网络的情况下，缓存失效的时间为60秒

    public static int MAX_STALE = 60 * 60 * 24 * 28;//没有网络的情况下，缓存失效的时间为4周

    public static long DEFAULT_TIMEOUT = 5;//网络连接超时
}
