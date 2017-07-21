package top.cokernut.news.utils;

import android.support.v7.app.AppCompatActivity;

import java.util.Stack;

/**
 * Created by Administrator on 2017/7/19.
 */

public class ActivityManager {
    private static Stack<AppCompatActivity> activityStack;
    private static ActivityManager instance;


    private ActivityManager() {
    }


    public static ActivityManager getInstance() {
        if (instance == null) {
            instance = new ActivityManager();
        }
        return instance;
    }


    public void popActivity() {
        AppCompatActivity activity = activityStack.lastElement();
        if (activity != null) {
            activity.finish();
            activity = null;
        }
    }


    public void popActivity(AppCompatActivity activity) {
        if (activity != null) {
            activity.finish();
            activityStack.remove(activity);
            activity = null;
        }
    }


    public AppCompatActivity currentActivity() {
        AppCompatActivity activity = activityStack.lastElement();
        return activity;
    }


    public void pushActivity(AppCompatActivity activity) {
        if (activityStack == null) {
            activityStack = new Stack<AppCompatActivity>();
        }
        activityStack.add(activity);
    }


    public void popAllActivityExceptOne(Class cls) {
        while (true) {
            AppCompatActivity activity = currentActivity();
            if (activity == null) {
                break;
            }
            if (activity.getClass().equals(cls)) {
                break;
            }
            popActivity(activity);
        }
    }

    public void popAllActivity() {
        while (true) {
            AppCompatActivity activity = currentActivity();
            if (activity == null) {
                break;
            }
            popActivity(activity);
        }
    }
}
