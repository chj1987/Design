package com.example.chj.design.utils;

import android.app.Activity;

import java.util.Stack;

/**
 * Created by ff on 2018/5/22.
 */

public class ActivityUtils {
    private static Stack<Activity> mActivityStack;

    /**
     * 添加一个Activity到堆栈中
     *
     * @param activity
     */
    public static void addActivity(Activity activity) {
        if (mActivityStack == null) {
            mActivityStack = new Stack<>();
        }
        mActivityStack.add(activity);
    }

    /**
     * 顶部的Activity
     *
     * @return
     */
    public static Activity getTopActivity() {
        if (mActivityStack.isEmpty()) {
            return null;
        } else {

            return mActivityStack.get(mActivityStack.size() - 1);
        }

    }

    /**
     * 移除指定的Activity
     *
     * @param activity
     */
    public static void removeActivity(Activity activity) {
        if (activity != null) {
            mActivityStack.remove(activity);
        }
    }

    /**
     * 移除所有的Activity
     **/
    public static void removeAllActivity() {
        if (mActivityStack != null && mActivityStack.size() > 0) {
            for (Activity activity : mActivityStack) {
                activity.finish();
            }
        }
    }
}
