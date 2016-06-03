package top.cokernut.news.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author KevinHo
 * 
 */
public class DateUtil {

	public static final String DATE_FORMAT_1 = "yyyy/MM/dd HH:mm:ss";

	/**
	 * 获取两日期距离 param startDate 开始日期 param endDate 结束日期 return 毫秒
	 */
	public static Long getDaysBetween(Date startDate, Date endDate) {
		return endDate.getTime() - startDate.getTime();
	}

	/**
	 * 日期字符串转成符合格式的字符串(齐李平-2015.11.20)
	 *
	 * @param
	 * @return
	 */
	public static String stringDateToFormatString(String format, String date)
	{
		try {
			Date dateTime = stringToDate(format, date);
			SimpleDateFormat formatter = new SimpleDateFormat(format);
			return formatter.format(dateTime);

		} catch (ParseException e) {
			return "";
		}
	}

	/**
	 * 日期字符串转date类型
	 * 
	 * @param
	 * @return
	 */
	public static Date stringToDate(String format, String date)
			throws ParseException {
		SimpleDateFormat formatDate = new SimpleDateFormat(format);
		Date time = null;
		time = formatDate.parse(date);
		return time;
	}

	/**
	 * 将毫秒转换日，小时，分钟，秒
	 * 
	 * @param mss
	 * @return
	 */
	public static String formatDuring(long mss) {
		long days = mss / (1000 * 60 * 60 * 24);
		long hours = (mss % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
		long minutes = (mss % (1000 * 60 * 60)) / (1000 * 60);
		long seconds = (mss % (1000 * 60)) / 1000;
		if (days < 1) {
			if (hours < 1) {
				if (minutes < 1) {
					return seconds + "秒";
				} else {
					return minutes + "分钟" + seconds + "秒";
				}
			} else {
				return hours + "小时" + minutes + "分钟" + seconds + "秒";
			}
		} else {
			return days + "天" + hours + "小时" + minutes + "分钟" + seconds + "秒";
		}

	}
	
//	}
	
}
