package top.cokernut.news.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import top.cokernut.news.model.TimeModel;

/**
 * Created by Admin on 2016/5/3.
 */
public class TimeUtil {
    public static Calendar stringToCalendar(String str) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(stringToDate(str));
        return calendar;
    }

    public static Date stringToDate(String str) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = sdf.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 如果time1表示的时间等于time2表示的时间，则返回 0 值；
     * 如果time1表示的时间在time2表示的时间之前，则返回小于 0 的值；
     * 如果time1表示的时间在time2表示的时间之后，则返回大于 0 的值；
     */
    public static int compareTo(String time1, String time2) {
        Calendar calendar1 = stringToCalendar(time1);
        Calendar calendar2 = stringToCalendar(time2);
        return calendar1.compareTo(calendar2);
    }

    public static int compareTo(Date time1, Date time2) {
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(time1);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(time2);
        return calendar1.compareTo(calendar2);
    }

    /**
     * time1距离到time2的时间(毫秒)
     */
    public static long countTimeLong(String time1, String time2) {
        Calendar calendar1 = stringToCalendar(time1);
        Calendar calendar2 = stringToCalendar(time2);
        long timeLong = Math.abs(calendar2.getTimeInMillis() - calendar1.getTimeInMillis());
        return timeLong;
    }

    public static long countTimeLong(Date time1, Date time2) {
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(time1);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(time2);
        long timeLong = Math.abs(calendar2.getTimeInMillis() - calendar1.getTimeInMillis());
        return timeLong;
    }

    /**
     * time1距离到time2的时间(最大计时为天)
     */
    public static TimeModel countTimeDay(String time1, String time2) {
        long timeLong = countTimeLong(time1, time2);
        TimeModel model = new TimeModel();
        if (timeLong > 60 * 60 * 24 * 1000) {
            model.setDay((int) (timeLong / (60 * 60 * 24 * 1000)));
            timeLong = timeLong % (60 * 60 * 24 * 1000);
        }
        if (timeLong > 60 * 60 * 1000) {
            model.setHour((int) (timeLong / (60 * 60 * 1000)));
            timeLong = timeLong % (60 * 60 * 1000);
        }
        if (timeLong > 60 * 1000) {
            model.setMinute((int) (timeLong / (60 * 1000)));
            timeLong = timeLong % (60 * 1000);
        }
        if (timeLong > 1000) {
            model.setSecond((int) (timeLong / (1000)));
        } else {
            model.setSecond(1);
        }
        return model;
    }

    /**
     * time1距离到time2的时间(最大计时为小时)
     */
    public static TimeModel countTimeHour(String time1, String time2) {
        long timeLong = countTimeLong(time1, time2);
        TimeModel model = new TimeModel();
        if (timeLong > 60 * 60 * 1000) {
            model.setHour((int) (timeLong / (60 * 60 * 1000)));
            timeLong = timeLong % (60 * 60 * 1000);
        }
        if (timeLong > 60 * 1000) {
            model.setMinute((int) (timeLong / (60 * 1000)));
            timeLong = timeLong % (60 * 1000);
        }
        if (timeLong > 1000) {
            model.setSecond((int) (timeLong / (1000)));
        } else {
            model.setSecond(1);
        }
        return model;
    }

    /**
     * time1距离到time2的时间(最大计时为小时)
     */
    public static TimeModel countTimeHour(long timeLong) {
        TimeModel model = new TimeModel();
        if (timeLong > 60 * 60 * 1000) {
            model.setHour((int) (timeLong / (60 * 60 * 1000)));
            timeLong = timeLong % (60 * 60 * 1000);
        }
        if (timeLong > 60 * 1000) {
            model.setMinute((int) (timeLong / (60 * 1000)));
            timeLong = timeLong % (60 * 1000);
        }
        if (timeLong > 1000) {
            model.setSecond((int) (timeLong / (1000)));
        } else {
            model.setSecond(1);
        }
        return model;
    }
}
