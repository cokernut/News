package top.cokernut.news;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import top.cokernut.news.utils.ActivityManager;

/**
 * Created by Admin on 2016/6/3.
 */
public class NewsApp extends Application {

    private static Context mContext;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
    }

    public static Context getContext() {
        return mContext;
    }

    public static void exitAll() {
        ActivityManager.getInstance().popAllActivity();
        System.exit(0);
    }
}
