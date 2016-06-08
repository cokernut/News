package top.cokernut.news;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.baidu.apistore.sdk.ApiStoreSDK;
import com.facebook.drawee.backends.pipeline.Fresco;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 2016/6/3.
 */
public class NewsApp extends Application {

    private static Context mContext;
    private static List<Activity> mActivities = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        ApiStoreSDK.init(this, "210e73956076a724ee7600c4a22eb9be");
        Fresco.initialize(this);
        mContext = this;
    }

    public static Context getContext() {
        return mContext;
    }

    public static void addActivity(Activity activity) {
        mActivities.add(activity);
    }

    public static void removeActivity(Activity activity) {
        mActivities.remove(activity);
    }

    public static void exitAll() {
        for (Activity activity : mActivities) {
            activity.finish();
        }
        System.exit(0);
    }
}
