package top.cokernut.news.utils;

import android.content.Context;
import android.content.SharedPreferences;

import top.cokernut.news.NewsApp;

/**
 * 用来快速获取相关的设置
 * Created by Troy on 2015/9/20.
 */
public class PrefUtils {

    private static final String PRE_NAME = "me.itangqi.buildingblocks_preferences";


    public static final String PRE_AUTO_UPDATE = "auto_update";


    private static SharedPreferences getSharedPreferences() {
        return NewsApp.getContext()
                .getSharedPreferences(PRE_NAME, Context.MODE_PRIVATE);
    }

    public static boolean isAutoUpdate() {
        return getSharedPreferences().getBoolean(PRE_AUTO_UPDATE, false);
    }
}
