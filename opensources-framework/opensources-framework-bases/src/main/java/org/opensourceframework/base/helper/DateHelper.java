package org.opensourceframework.base.helper;

import org.opensourceframework.base.exception.BizException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.lang3.time.FastDateFormat;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期操作帮助类
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
public class DateHelper {
	public static final String YMD_DATA = "yyyy-MM-dd";
	public static final String YMD01_DATA = "yyyy/MM/dd";
	public static final String YMDHMS_DATA = "yyyy-MM-dd HH:mm:ss";
    public static final String YMDH_DATA = "yyyy-MM-dd HH";
	public static final String YMDSTRING_DATA = "yyyyMMddHHmmss";
	public static final String YMDGB_DATA = "yyyy年MM月dd日";
	public static final String YMDTHMSGB_DATA = "yyyy年MM月dd日 HH时mm分ss秒";
	public static final String YMD24H_DATA = "yyyy-MM-dd HH:mm:ss";
	public static final String[] DAYNAMES = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};


	public static final int ONE_DAY_MILLISECONDS = 86400000;


	public static final int ONE_HOUR_MILLISECONDS = 3600000;


	public static final int ONE_MINUTE_MILLISECONDS = 60000;


	public static final long ONE_YEAR_MILLISECONDS = 1471228928L;


	public static int getYear() {
		return Calendar.getInstance().get(1);
	}


	public static int getMonth() {
		return Calendar.getInstance().get(2);
	}


	public static int getDayOfMonth() {
		return Calendar.getInstance().get(5);
	}

	public static int getDayOfMonth(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(5);
	}


	public static String getDayOfWeek() {
		Calendar calendar = Calendar.getInstance();
		int dayOfWeek = calendar.get(7);
		return DAYNAMES[(dayOfWeek - 1)];
	}


	public static String getDateWeek(String date) {
		String dateWeek = null;
		Calendar calendar = Calendar.getInstance();
        calendar.setTime(parseDate(date, YMD_DATA));
        int dayOfWeek = calendar.get(7);
        dateWeek =  dayOfWeek + "";
        return dateWeek;
	}


	public static String getYMD() {
		return getYMD("-");
	}


	public static String getYMD(String separator) {
		Calendar now = Calendar.getInstance();
		return now.get(1) + separator + (now.get(2) + 1) + separator + now.get(5);
	}


	public static String getYMD_CN() {
		Calendar now = Calendar.getInstance();
		return now.get(1) + "年" + (now.get(2) + 1) + "月" + now.get(5) + "日";
	}

	public static Date getToday() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(11, 0);
		calendar.set(12, 0);
		calendar.set(13, 0);
		calendar.set(14, 0);
		return calendar.getTime();
	}


	public static int getDayDiff(Date start, Date end) {
		Date d1 = getDayBegin(start);
		Date d2 = getDayBegin(end);
		return (int) ((d2.getTime() - d1.getTime()) / 86400000L);
	}

	public static long getMiniteDiff(Date start, Date end) {
		return end.getTime() / 60000L - start.getTime() / 60000L;
	}

	public static String getTimeDiff(Date start, Date end) {
		int days = (int) ((end.getTime() - start.getTime()) / 86400000L);
		int hours = (int) ((end.getTime() - start.getTime()) / 3600000L % 24L);
		int minutes = (int) ((end.getTime() - start.getTime()) / 60000L % 60L);

		String timeDiff = "";
		if (days > 0) {
			timeDiff = days + "天";
		}
		timeDiff = timeDiff + hours + "小时" + minutes + "分";
		return timeDiff;
	}


	public static String getMonthFirstDay(Date date) {
		return formatDate(getMonthBegin(date), YMD_DATA);
	}


	public static String getMonthLastDay(Date date) {
		return formatDate(getMonthEnd(date), YMD_DATA);
	}

	public static Date getMonthBegin() {
		return getMonthBegin(new Date());
	}

	public static Date getMonthEnd() {
		return getMonthEnd(new Date());
	}


	public static Date getMonthBegin(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(5, 1);
		return getDayBegin(calendar);
	}


	public static Date getMonthEnd(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(2, 1);
		calendar.set(5, 1);
		calendar.add(5, -1);
		return getDayEnd(calendar);
	}

	public static Date getDayBegin() {
		return getDayBegin(Calendar.getInstance());
	}

	public static Date getDayEnd() {
		return getDayEnd(Calendar.getInstance());
	}


	public static Date getDayBegin(Date date) {
		Calendar calendar = Calendar.getInstance();
		if (date != null) {
			calendar.setTime(date);
		}

		return getDayBegin(calendar);
	}

	public static Date getDayBegin(Calendar calendar) {
		calendar.set(11, 0);
		calendar.set(12, 0);
		calendar.set(13, 0);
		calendar.set(14, 0);
		return calendar.getTime();
	}


	public static Date getDayEnd(Date date) {
		Calendar calendar = Calendar.getInstance();
		if (date != null) {
			calendar.setTime(date);
		}

		return getDayEnd(calendar);
	}

	public static Date getDayEnd(Calendar calendar) {
		calendar.set(11, 23);
		calendar.set(12, 59);
		calendar.set(13, 59);
		calendar.set(14, 999);
		return calendar.getTime();
	}

	public static Date addSeconds(Date date, int seconds) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(13, seconds);
		return calendar.getTime();
	}

	public static Date addMinutes(Date date, int minutes) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(12, minutes);
		return calendar.getTime();
	}

	public static Date addHours(Date date, int hours) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(11, hours);
		return calendar.getTime();
	}

	public static Date addDays(Date date, int days) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(6, days);
		return calendar.getTime();
	}

	public static Date addMonths(Date date, int months) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(2, months);
		return calendar.getTime();
	}

	public static Date addYears(Date date, int years) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(1, years);
		return calendar.getTime();
	}

	public static int getAge(Date birthday) {
		if (birthday == null) {
			return 0;
		}
		long time = (System.currentTimeMillis() - birthday.getTime()) / 1000L;
		if (time < 0L) {
			return 0;
		}
		return (int) (time / 31536000L);
	}

	public static Calendar getCalendar(long time) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(time);
		return calendar;
	}

	public static Date getDate(long time) {
		Calendar calendar = getCalendar(time);
		return calendar.getTime();
	}

	public static String YYYYMMDD(Date date) {
		if (date == null) {
			return "";
		}
		FastDateFormat dateFormat = FastDateFormat.getInstance(YMD_DATA);
		return dateFormat.format(date);
	}

	public static String YYYYMMDDHHMMSS(Date date) {
		if (date == null) {
			return "";
		}
		FastDateFormat dateFormat = FastDateFormat.getInstance(YMDHMS_DATA);
		return dateFormat.format(date);
	}


	public static String formatDate(Date date , String pattern) {
		if (pattern == null) {
			return "";
		}

		if (date == null) {
			return "";
		}
		FastDateFormat dateFormat = FastDateFormat.getInstance(pattern);
		return dateFormat.format(date);
	}

	public static String formatDate(Calendar date , String pattern) {
		if (pattern == null) {
			return "";
		}

		if (date == null) {
			return "";
		}
		FastDateFormat dateFormat = FastDateFormat.getInstance(pattern);
		return dateFormat.format(date);
	}


	public static Date parseDate(String date , String pattern) {
		if (pattern == null) {
			pattern = YMDHMS_DATA;
		}

		if (StringUtils.isBlank(date)) {
			return null;
		}
		FastDateFormat dateFormat = FastDateFormat.getInstance(pattern);
        try {
            return dateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            throw new BizException(e.getMessage());
        }
    }

    /**
     * 获取时间间隔
     *
     * @param start
     * @param end
     * @return
     */
    public static long getHourDiff(Date start, Date end) {
        return end.getTime() / 3600000L - start.getTime() / 3600000L;
    }

	public static Date getStartOfDay(Date date) {
		return DateUtils.truncate(date, 5);
	}


	public static Date getEndOfDay(Date date) {
		return DateUtils.addMilliseconds(DateUtils.ceiling(date, 5), -1);
	}
}
