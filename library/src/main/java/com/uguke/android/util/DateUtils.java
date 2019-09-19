package com.uguke.android.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 日期操作类
 * @author LeiJue
 */
public class DateUtils {

	private static final long UNIT_DAY = 24 * 60 * 60 * 1000;
	private static final long UNIT_HOUR = 60 * 60 * 1000;
	private static final long UNIT_MINUTE = 60 * 1000;
	private static final long UNIT_SECOND = 1000;
	private static final int CALENDER_START_YEAR = 1970;

	private DateUtils() {
		throw new UnsupportedOperationException("can't instantiate me...");
	}

	public static boolean isDate(String date) {
		return toDate(date) != null;
	}

	public static Style getDateStyle(String date) {
		Map<Long, Style> map = new HashMap<>(4);
		List<Long> timestamps = new ArrayList<>();
		for (Style style : Style.values()) {
			Date dateTmp = toDate(date, style);
			if (dateTmp != null) {
				timestamps.add(dateTmp.getTime());
				map.put(dateTmp.getTime(), style);
			}
		}
		Date accurateDate = getAccurateDate(timestamps);
		return map.get(accurateDate == null ? 0 : accurateDate.getTime());
	}

	//========================================================//
	//=============日期Long、String、Date相互转换==============//
	//=======================================================//

	public static long toLong(String date) {
		return toLong(toDate(date));
	}

	public static long toLong(String date, String pattern) {
		return toLong(toDate(date, pattern));
	}

	public static long toLong(Date date) {
		return date == null ? 0 : date.getTime();
	}

	public static String toString(long millis) {
		return toString(new Date(millis));
	}

	public static String toString(long millis, String pattern) {
		return toString(new Date(millis), pattern);
	}

	public static String toString(long millis, Style style) {
		return toString(new Date(millis), style);
	}

	public static String toString(String date) {
		return toString(date, null, Style.YYYY_MM_DD);
	}

	public static String toString(String date, String pattern) {
		return toString(date, null, pattern);
	}

	public static String toString(String date, Style style) {
		return toString(date, null, style);
	}

	public static String toString(String date, String oldPattern, String newPattern) {
		if (oldPattern == null) {
			return toString(toDate(date), newPattern);
		}
		return toString(toDate(date, oldPattern), newPattern);
	}

	public static String toString(String date, Style oldStyle, Style newStyle) {
		String oldPattern = oldStyle == null ? null : oldStyle.getValue();
		String newPattern = newStyle == null ? null : newStyle.getValue();
		return toString(date, oldPattern, newPattern);
	}

	public static String toString(Date date) {
		return toString(date, Style.YYYY_MM_DD);
	}

	public static String toString(Date date, String pattern) {
		if (TextUtils.isEmpty(pattern) || date == null) {
			return null;
		}
		return getDateFormat(pattern).format(date);
	}

	public static String toString(Date date, Style style) {
		String pattern = style == null ? null : style.getValue();
		return toString(date, pattern);
	}

	public static Date toDate(String date) {
		return toDate(date, (Style) null);
	}

	public static Date toDate(String date, String pattern) {
		if (TextUtils.isEmpty(date) || TextUtils.isEmpty(pattern)) {
			return null;
		}
		try {
			return getDateFormat(pattern).parse(TextUtils.convertNull(date));
		} catch (ParseException e) {
			return null;
		}
	}

	public static Date toDate(String date, Style style) {
		if (style == null) {
			List<Long> timestamps = new ArrayList<>();
			for (Style s : Style.values()) {
				Date temp = toDate(date, s.getValue());
				if (temp != null) {
					timestamps.add(temp.getTime());
				}
			}
			return getAccurateDate(timestamps);
		} else {
			return toDate(date, style.getValue());
		}
	}

	//========================================================//
	//=================获取日期单个数据信息===================//
	//=======================================================//

	public static int getYear() {
		return getDateIntValue(new Date(), Calendar.YEAR);
	}

	public static int getYear(String date) {
		return getDateIntValue(toDate(date), Calendar.YEAR);
	}

	public static int getYear(Date date) {
		return getDateIntValue(date, Calendar.YEAR);
	}

	public static Month getMonth() {
		return Month.findMonthByNumber(getDateIntValue(new Date(), Calendar.MONTH));
	}

	public static Month getMonth(String date) {
		return Month.findMonthByNumber(getDateIntValue(toDate(date), Calendar.MONTH));
	}

	public static Month getMonth(Date date) {
		return Month.findMonthByNumber(getDateIntValue(date, Calendar.MONTH));
	}

	public static Week getWeek() {
		return Week.findByNumber(getDateIntValue(new Date(), Calendar.DAY_OF_WEEK));
	}

	public static Week getWeek(String date) {
		return Week.findByNumber(getDateIntValue(toDate(date), Calendar.DAY_OF_WEEK));
	}

	public static Week getWeek(Date date) {
		return Week.findByNumber(getDateIntValue(date, Calendar.DAY_OF_WEEK));
	}

	public static int getDay() {
		return getDateIntValue(new Date(), Calendar.DAY_OF_MONTH);
	}

	public static int getDay(String date) {
		return getDateIntValue(toDate(date), Calendar.DAY_OF_MONTH);
	}

	public static int getDay(Date date) {
		return getDateIntValue(date, Calendar.DAY_OF_MONTH);
	}

	public static int getHour() {
		return getDateIntValue(new Date(), Calendar.HOUR_OF_DAY);
	}

	public static int getHour(String date) {
		return getDateIntValue(toDate(date), Calendar.DAY_OF_MONTH);
	}

	public static int getHour(Date date) {
		return getDateIntValue(date, Calendar.HOUR_OF_DAY);
	}

	public static int getMinute() {
		return getDateIntValue(new Date(), Calendar.MINUTE);
	}

	public static int getMinute(String date) {
		return getDateIntValue(toDate(date), Calendar.MINUTE);
	}

	public static int getMinute(Date date) {
		return getDateIntValue(date, Calendar.MINUTE);
	}

	public static int getSecond() {
		return getDateIntValue(new Date(), Calendar.SECOND);
	}

	public static int getSecond(String date) {
		return getDateIntValue(toDate(date), Calendar.SECOND);
	}

	public static int getSecond(Date date) {
		return getDateIntValue(date, Calendar.SECOND);
	}

	/**
	 * 获取日期中的某数值。如获取月份
	 *
	 * @param date 日期
	 * @param type 同Calender的日期格式
	 * @return 数值
	 */
	public static int getDateIntValue(Date date, int type) {
		if (date == null) {
			return -1;
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(type);
	}

	//========================================================//
	//=====================获取时间间隔=======================//
	//=======================================================//

	public static long getIntervalTime(String date) {
		return getIntervalTime(toDate(date), new Date());
	}

	public static long getIntervalTime(Date date) {
		return getIntervalTime(date, new Date());
	}

	public static long getIntervalTime(String date, String otherDate) {
		return getIntervalTime(toDate(date), toDate(otherDate));
	}

	public static long getIntervalTime(Date date, Date otherDate) {
		if (date == null || otherDate == null) {
			return date == null ? otherDate == null ? 0 : otherDate.getTime() : date.getTime();
		}
		return Math.abs(otherDate.getTime() - date.getTime());
	}

	public static int getIntervalDays(String startDate, String otherDate) {
		return getIntervalDays(toDate(startDate), toDate(otherDate));
	}

	public static int getIntervalDays(Date startDate, Date otherDate) {
		return (int) (getIntervalTime(startDate, otherDate) / UNIT_DAY);
	}

	public static int getIntervalHours(String startDate, String otherDate) {
		return getIntervalHours(toDate(startDate), toDate(otherDate));
	}

	public static int getIntervalHours(Date date, Date otherDate) {
		return (int) (getIntervalTime(date, otherDate) / UNIT_HOUR);
	}

	public static int getIntervalMinutes(String startDate, String otherDate) {
		return getIntervalMinutes(toDate(startDate), toDate(otherDate));
	}

	public static int getIntervalMinutes(Date date, Date otherDate) {
		return (int) (getIntervalTime(date, otherDate) / UNIT_MINUTE);
	}

	public static int getIntervalSeconds(String startDate, String otherDate) {
		return getIntervalSeconds(toDate(startDate), toDate(otherDate));
	}

	public static int getIntervalSeconds(Date date, Date otherDate) {
		return (int) (getIntervalTime(date, otherDate) / UNIT_SECOND);
	}

	public static String getIntervalText(String date) {
		return getIntervalText(toDate(date), new Date());
	}

	public static String getIntervalText(Date date) {
		return getIntervalText(date, new Date());
	}

	public static String getIntervalText(String date, String otherDate) {
		return getIntervalText(toDate(date), toDate(otherDate));
	}

	public static String getIntervalText(Date date, Date otherDate) {
		long time = getIntervalTime(date, otherDate);
		if (time > UNIT_DAY) {
			Date intervalDate = new Date(time);
			int year = getDateIntValue(intervalDate, Calendar.YEAR);
			int month = getDateIntValue(intervalDate, Calendar.MONTH);
			if (year > CALENDER_START_YEAR) {
				return (year - CALENDER_START_YEAR) + "年";
			} else if (month > 0) {
				return month + "月";
			} else {
				return (int) (time / UNIT_DAY) + "天";
			}
		} else if (time > UNIT_HOUR) {
			return (int) (time / UNIT_HOUR) + "小时";
		} else if (time > UNIT_MINUTE) {
			return (int) (time / UNIT_MINUTE) + "分钟";
		} else {
			return (int) (time / UNIT_SECOND) + "秒";
		}
	}

	//========================================================//
	//===================获取变化后的时间=====================//
	//=======================================================//

	public static String getChangedYear(String date, int yearAmount) {
		return getChangedDateString(date, Calendar.YEAR, yearAmount);
	}

	public static Date getChangedYear(Date date, int yearAmount) {
		return getChangedDate(date, Calendar.YEAR, yearAmount);
	}

	public static String getChangedMonth(String date, int monthAmount) {
		return getChangedDateString(date, Calendar.MONTH, monthAmount);
	}

	public static Date getChangedMonth(Date date, int monthAmount) {
		return getChangedDate(date, Calendar.MONTH, monthAmount);
	}

	public static String getChangedDay(String date, int dayAmount) {
		return getChangedDateString(date, Calendar.DATE, dayAmount);
	}

	public static Date getChangedDay(Date date, int dayAmount) {
		return getChangedDate(date, Calendar.DATE, dayAmount);
	}

	/**
	 * 增加日期中某类型的某数值。如增加日期
	 *
	 * @param date   日期字符串
	 * @param type   同Calender日期格式
	 * @param amount 数值
	 * @return 计算后日期字符串
	 */
	public static String getChangedDateString(String date, int type, int amount) {
		String dateString = null;
		Style style = getDateStyle(date);
		if (style != null) {
			Date myDate = toDate(date, style);
			myDate = getChangedDate(myDate, type, amount);
			dateString = toString(myDate, style);
		}
		return dateString;
	}

	/**
	 * 增加日期中某类型的某数值。如增加日期
	 *
	 * @param date   日期
	 * @param type   同Calender日期格式
	 * @param amount 数值
	 * @return 计算后日期
	 */
	public static Date getChangedDate(Date date, int type, int amount) {
		Date newDate = null;
		if (date != null) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			calendar.add(type, amount);
			newDate = calendar.getTime();
		}
		return newDate;
	}

	public static String getChangedDate(String date, int yearAmount, int monthAmount, int dayAmount) {
		String dateString = null;
		Style style = getDateStyle(date);
		if (style != null) {
			Date newDate = toDate(date, style);
			newDate = getChangedDate(newDate, yearAmount, monthAmount, dayAmount);
			dateString = toString(newDate, style);
		}
		return dateString;
	}

	public static Date getChangedDate(Date date, int yearAmount, int monthAmount, int dayAmount) {
		Date newDate = null;
		if (date != null) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			calendar.add(Calendar.YEAR, yearAmount);
			calendar.add(Calendar.MONTH, monthAmount);
			calendar.add(Calendar.DATE, dayAmount);
			newDate = calendar.getTime();
		}
		return newDate;
	}

	private static SimpleDateFormat getDateFormat(String pattern) {
		return new SimpleDateFormat(TextUtils.convertNull(pattern));
	}

	private static Date getAccurateDate(List<Long> timestamps) {
		Date date = null;
		long timestamp = 0;
		Map<Long, long[]> map = new HashMap<>(4);
		List<Long> absoluteValues = new ArrayList<>();

		if (timestamps != null && timestamps.size() > 0) {
			if (timestamps.size() > 1) {
				int len = timestamps.size();
				for (int i = 0; i < len; i++) {
					for (int j = i + 1; j < len; j++) {
						long absoluteValue = Math.abs(timestamps.get(i) - timestamps.get(j));
						map.put(absoluteValue, new long[]{timestamps.get(i), timestamps.get(j)});
						absoluteValues.add(absoluteValue);
					}
				}
				// 有可能有相等的情况。如2012-11和2012-11-01。时间戳是相等的。此时minAbsoluteValue为0
				// 因此不能将minAbsoluteValue取默认值0
				long minAbsoluteValue = -1;
				if (!absoluteValues.isEmpty()) {
					minAbsoluteValue = absoluteValues.get(0);
					for (int i = 1; i < absoluteValues.size(); i++) {
						if (minAbsoluteValue > absoluteValues.get(i)) {
							minAbsoluteValue = absoluteValues.get(i);
						}
					}
				}

				if (minAbsoluteValue != -1) {
					long[] timestampsLastTmp = map.get(minAbsoluteValue);
					long dateOne = timestampsLastTmp[0];
					long dateTwo = timestampsLastTmp[1];
					if (absoluteValues.size() > 1) {
						timestamp = Math.abs(dateOne) > Math.abs(dateTwo) ? dateOne : dateTwo;
					}
				}
			} else {
				timestamp = timestamps.get(0);
			}
		}

		if (timestamp != 0) {
			date = new Date(timestamp);
		}
		return date;
	}

	public enum Month {

		UNKNOWN("Unknown", "未知月", -1),
		/**
		 * 一月
		 **/
		JANUARY("January", "一月", 0),
		/**
		 * 二月
		 **/
		FEBRUARY("February", "二月", 1),
		/**
		 * 三月
		 **/
		MARCH("March", "三月", 2),
		/**
		 * 四月
		 **/
		APRIL("April", "四月", 3),
		/**
		 * 五月
		 **/
		May("May", "五月", 4),
		/**
		 * 六月
		 **/
		JUNE("June", "六月", 5),
		/**
		 * 七月
		 **/
		JULY("July", "七月", 6),
		/**
		 * 八月
		 **/
		AUGUST("August", "八月", 7),
		/**
		 * 九月
		 **/
		SEPTEMBER("September", "九月", 8),
		/**
		 * 十月
		 **/
		OCTOBER("October", "十月", 9),
		/**
		 * 十一月
		 **/
		NOVEMBER("November", "十一月", 10),
		/**
		 * 十二月
		 **/
		DECEMBER("December", "十二月", 11);

		int number;
		String cnName;
		String name;

		Month(String name, String cnName, int number) {
			this.name = name;
			this.cnName = cnName;
			this.number = number;
		}

		public String getName() {
			return name;
		}

		public String getShortName() {
			if (this != May) {
				return name.substring(0, 3) + ".";
			}
			return name;
		}

		public String getCnName() {
			return cnName;
		}

		public String getCnShortName() {
			if (cnName.equals(Month.UNKNOWN.cnName)) {
				return UNKNOWN.cnName;
			}
			return number + "月";
		}

		public int getNumber() {
			return number;
		}

		public static Month findMonthByNumber(int number) {
			for (Month month : Month.values()) {
				if (month.number == number) {
					return month;
				}
			}
			return UNKNOWN;
		}
	}

	public enum Week {
		/**
		 * 未知
		 **/
		UNKNOWN("Unknown", "未知星期", -1),
		/**
		 * 星期日
		 **/
		SUNDAY("Sunday", "星期日", 1),
		/**
		 * 星期一
		 **/
		MONDAY("Monday", "星期一", 2),
		/**
		 * 星期二
		 **/
		TUESDAY("Tuesday", "星期二", 3),
		/**
		 * 星期三
		 **/
		WEDNESDAY("Wednesday", "星期三", 4),
		/**
		 * 星期四
		 **/
		THURSDAY("Thursday", "星期四", 5),
		/**
		 * 星期五
		 **/
		FRIDAY("Friday", "星期五", 6),
		/**
		 * 星期六
		 **/
		SATURDAY("Saturday", "星期六", 7);

		String name;
		String cnName;
		int number;

		Week(String name, String cnName, int number) {
			this.name = name;
			this.cnName = cnName;
			this.number = number;
		}

		public String getName() {
			return name;
		}

		public String getShortName() {
			if (this == TUESDAY || this == THURSDAY) {
				return name.substring(0, 4) + ".";
			}
			return name.substring(0, 3) + ".";
		}

		public String getCnName() {
			return cnName;
		}

		public String getCnShortName() {
			if (cnName.equals(UNKNOWN.cnName)) {
				return cnName;
			}
			return cnName.replace("星期", "周");
		}

		public int getNumber() {
			return number;
		}

		public static Week findByNumber(int number) {
			for (Week week : Week.values()) {
				if (week.number == number) {
					return week;
				}
			}
			return UNKNOWN;
		}
	}

	public enum Style {

		MM_DD("MM-dd"),
		YYYY_MM("yyyy-MM"),
		YYYY_MM_DD("yyyy-MM-dd"),
		MM_DD_HH_MM("MM-dd HH:mm"),
		MM_DD_HH_MM_SS("MM-dd HH:mm:ss"),
		YYYY_MM_DD_HH_MM("yyyy-MM-dd HH:mm"),
		YYYY_MM_DD_HH_MM_SS("yyyy-MM-dd HH:mm:ss"),

		MM_DD_EN("MM/dd"),
		YYYY_MM_EN("yyyy/MM"),
		YYYY_MM_DD_EN("yyyy/MM/dd"),
		MM_DD_HH_MM_EN("MM/dd HH:mm"),
		MM_DD_HH_MM_SS_EN("MM/dd HH:mm:ss"),
		YYYY_MM_DD_HH_MM_EN("yyyy/MM/dd HH:mm"),
		YYYY_MM_DD_HH_MM_SS_EN("yyyy/MM/dd HH:mm:ss"),

		MM_DD_CN("MM月dd日"),
		YYYY_MM_CN("yyyy年MM月"),
		YYYY_MM_DD_CN("yyyy年MM月dd日"),
		MM_DD_HH_MM_CN("MM月dd日 HH:mm"),
		MM_DD_HH_MM_SS_CN("MM月dd日 HH:mm:ss"),
		YYYY_MM_DD_HH_MM_CN("yyyy年MM月dd日 HH:mm"),
		YYYY_MM_DD_HH_MM_SS_CN("yyyy年MM月dd日 HH:mm:ss"),

		HH_MM("HH:mm"),
		HH_MM_SS("HH:mm:ss");

		private String value;

		Style(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
	}
}