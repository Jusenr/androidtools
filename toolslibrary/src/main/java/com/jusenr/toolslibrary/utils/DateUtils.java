package com.jusenr.toolslibrary.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.jusenr.toolslibrary.R;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Date, time, tool class
 * Created by guchenkai on 2015/11/26.
 */
public final class DateUtils {
    public static final String YMD_PATTERN = "yyyy-MM-dd";
    public static final String YMD_PATTERN2 = "yyyy/MM/dd";
    public static final String YMD_PATTERN3 = "yyyy/MM/dd HH:mm";
    public static final String YMD_HMS_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final String YMD_HMS_PATTERN2 = "yyyy-MM-dd HH:mm";
    public static final String[] weeks = new String[]{"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
    private static final String TAG = DateUtils.class.getSimpleName();
    private static final SimpleDateFormat mDataFormat = new SimpleDateFormat(YMD_PATTERN);
    private static final SimpleDateFormat mTimeFormat = new SimpleDateFormat(YMD_HMS_PATTERN);

    private static final int ONE_MINUTE = 60 * 1000;
    private static final int ONE_HOUR = 60 * 60 * 1000;
    private static final int ONE_DAY = 24 * 60 * 60 * 1000;
    private static final int ONE_WEEK = 7 * 24 * 60 * 60 * 1000;
    private static final int ONE_MONTH = 30 * 24 * 60 * 60 * 1000;
    private static final int ONE_YEAR = 12 * 30 * 24 * 60 * 60 * 1000;

    /**
     * Gets the offset of the time zone at which the current time is relative to the GMT +0:00 time zone
     *
     * @return int offset
     */
    public static int getRawOffset() {
        TimeZone tz = TimeZone.getDefault();
        int offset = tz.getRawOffset();
        System.out.println("getRawOffset: offset=" + offset);//28800000 of china
        return offset / 1000;
    }

    /**
     * Gets the time zone of the current time zone
     *
     * @return time zone String GMT+05:45
     */
    public static String getCurrentTimeZone() {
        TimeZone tz = TimeZone.getDefault();
        String strTz = tz.getDisplayName(false, TimeZone.SHORT);
        System.out.println("getCurrentTimeZone: " + strTz);
        return strTz;
    }

    /**
     * Gets the time zone difference of the current time zone
     *
     * @return Time zone difference [float] e.g. 8.0,2.5,-2.0
     */
    public static float getCurrentTimeZone2Int() {
        String timeZone = getCurrentTimeZone();//GMT+05:45,GMT-07:00
        String[] split = timeZone.split(":");
        String sub0 = split[0].substring(split[0].length() - 3, split[0].length());
        int anInt = Integer.parseInt(split[1]);
        String s = new DecimalFormat("#.00").format((float) anInt / 60);
        float index = Float.parseFloat(sub0) + Float.parseFloat(anInt > 0 ? s : "0");
        Log.i("####", "Time zone difference: " + index);
        return index;
    }

    /**
     * UTC time to Local time
     *
     * @param utcTime utcTime
     * @return localTime
     */
    public static long generateUTCTime2LocalTime(long utcTime) {
//        float f = DateUtils.getCurrentTimeZone2Int() * 60 * 60;
//        long localTime = utcTime + (int) f;
        long localTime = utcTime + getRawOffset();
        Log.d("####", "generateUTCTime2LocalTime: utcTime--" + utcTime + "--localTime--" + localTime);
        return localTime;
    }

    /**
     * Local time to UTC time
     *
     * @param localTime localTime
     * @return utcTime
     */
    public static long generateLocalTime2UTCTime(long localTime) {
//        float f = DateUtils.getCurrentTimeZone2Int() * 60 * 60;
//        long utcTime = localTime - (int) f;
        long utcTime = localTime - getRawOffset();
        Log.d("####", "generateLocalTime2UTCTime: localTime--" + localTime + "--utcTime--" + utcTime);
        return utcTime;
    }

    /**
     * The current time is formatted into the form of day and month,
     * and then the resulting string is resolved into the Date type,
     * that is, the same day at 0.
     *
     * @param date date
     * @return The millisecond value at 0 a. m.
     * @throws ParseException exception
     */
    public static long getDateToTime(Date date) throws ParseException {
        return mDataFormat.parse(mDataFormat.format(date)).getTime();
    }

    /**
     * The millisecond value of the day is obtained by millisecond value of 0.
     *
     * @param millis millis
     * @return The millisecond value at 0 a. m.
     * @throws ParseException exception
     */
    public static long getMillisToTime(long millis) throws ParseException {
        return getDateToTime(new Date(millis));
    }

    /**
     * Gets the millisecond value of 0 on the day by the second value.
     *
     * @param seconds second
     * @return The millisecond value at 0 a. m.
     * @throws ParseException exception
     */
    public static long getSecondsToTime(int seconds) throws ParseException {
        return getMillisToTime(seconds * 1000L);
    }

    /**
     * A display of a similar date format based on the millisecond value and the incoming date.
     *
     * @param millis millis
     * @param date   date
     * @return The string displayed in the corresponding date format for the specific (date by itself).
     * @throws ParseException exception
     */
    public static String getMillisToDate(long millis, Date date) throws ParseException {

        Log.d("tag", mDataFormat.format(new Date(millis)));
        if (millis > getDateToTime(date)) {
            return millisecondToDate(millis, "H:dd");
        } else if ((millis + 1 * ONE_DAY) >= getDateToTime(date)) {
            return "昨天" + millisecondToDate(millis, "H:dd");
        } else if ((millis + 3 * ONE_DAY) >= getDateToTime(date)) {
            return millisecondToDate(millis, "E H:dd");
        } else {
            return millisecondToDate(millis, "yyyy年M月d日 H:dd");
        }
    }

    /**
     * 根据  秒值 与 传入的日期  显示与之相近的日期格式的显示
     *
     * @param seconds
     * @param date
     * @return 具体 (日期自己传入) 对应日期格式显示的字符串
     * @throws ParseException
     */
    public static String getSecondsToDate(int seconds, Date date) throws ParseException {
        return getMillisToDate(seconds * 1000L, date);
    }

    /**
     * 根据毫秒值  显示与之   当前日期   相近的日期格式的显示
     *
     * @param millis
     * @return 具体（当前日期） 对应日期格式显示的字符串
     * @throws ParseException
     */
    public static String getMillisToDate(long millis) throws ParseException {
        Long time = System.currentTimeMillis();
        return getMillisToDate(millis, new Date(time));
    }

    /**
     * 根据秒值  显示与之   当前日期   相近的日期格式的显示
     *
     * @param seconds
     * @return 具体（当前日期） 对应日期格式显示的字符串
     * @throws ParseException
     */
    public static String getSecondsToDate(int seconds) throws ParseException {
        return getMillisToDate(seconds * 1000L);
    }

    public static long getStringToMills(String dateStr, String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        Date date = null;
        try {
            date = format.parse(dateStr);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 计算两个日期相差的天数，是否取绝对值
     *
     * @param date1 第一个日期
     * @param date2 第二个日期
     * @param isAbs 是否取绝对值
     * @return 相差天数
     */
    public static int getDaysUnAbs(String date1, String date2, boolean isAbs) {
        int day = 0;
        if (TextUtils.isEmpty(date1) || TextUtils.isEmpty(date2))
            return 0;
        try {
            Date date = mDataFormat.parse(date1);
            Date myDate = mDataFormat.parse(date2);
            day = (int) ((date.getTime() - myDate.getTime()) / (24 * 60 * 60 * 1000));
        } catch (ParseException e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
        }
        return isAbs ? Math.abs(day) : day;
    }

    /**
     * 计算两个日期相差的天数，取绝对值
     *
     * @param date1 第一个日期
     * @param date2 第二个日期
     * @return 相差天数
     */
    public static int getDaysUnAbs(String date1, String date2) {
        return getDaysUnAbs(date1, date2, true);
    }

    /**
     * 将长时间格式字符串转换为时间 yyyy-MM-dd HH:mm:ss
     *
     * @param date 日期
     * @return 长时间格式日期
     */
    public static Date strToDateLong(String date) {
        ParsePosition pos = new ParsePosition(0);
        return mTimeFormat.parse(date, pos);
    }

    /**
     * 获取当前的日期时间
     *
     * @return 当前的日期时间
     */
    public static String getCurrentTime() {
        return mTimeFormat.format(new Date());
    }

    /**
     * 获取当前的日期
     *
     * @return 当前的日期
     */
    public static String getCurrentDate() {
        return mDataFormat.format(new Date());
    }

    /**
     * 获取输入日期是星期几
     *
     * @param date 日期
     * @return 星期几
     */
    public static String getWeekDate(Date date) {
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        calendar.setTime(date);
        return weeks[calendar.get(calendar.DAY_OF_WEEK) - 1];
    }

    /**
     * 获取输入日期是星期几
     *
     * @param context context
     * @param date    日期
     * @return 星期几
     */
    public static String getWeekDate(Context context, Date date) {
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        calendar.setTime(date);
        return context.getResources().getStringArray(R.array.weeks)[calendar.get(calendar.DAY_OF_WEEK) - 1];
    }

    /**
     * 获取输入日期是星期几
     *
     * @param date 日期
     * @return 星期几
     */
    public static String getWeekDate(String date) {
        try {
            Date d = mDataFormat.parse(date);
            return getWeekDate(d);
        } catch (ParseException e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
        }
        return "星期数未知";
    }

    /**
     * 毫秒转日期
     *
     * @param millisecond 毫秒数
     * @param pattern     正则式
     * @return 日期
     */
    public static String millisecondToDate(long millisecond, String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        Date date = new Date(millisecond);
        return format.format(date);
    }

    /**
     * 毫秒值转日期 (yyyy/MM/dd)
     *
     * @param millisecond 毫秒数
     * @return 日期
     */
    public static String millisecondToDate(long millisecond) {
        return millisecondToDate(millisecond * 1000L, YMD_PATTERN2);
    }

    /**
     * 秒转日期
     *
     * @param second  秒数
     * @param pattern 正则式
     * @return 日期
     */
    public static String secondToDate(int second, String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        Date date = new Date(second * 1000L);
        return format.format(date);
    }

    /**
     * 将时间戳转换成字符串
     *
     * @param timeStamp  时间戳
     * @param dataFormat yyyy-MM-dd HH:mm:ss字符串
     * @return String yyyy-MM-dd HH:mm:ss格式日期字符串
     */
    public static String long_2_str(long timeStamp, String dataFormat) {
        return new SimpleDateFormat(dataFormat).format(new Date(timeStamp * 1000L));
    }

    /**
     * 将yyyy-MM-dd HH:mm:ss字符串转换成日期
     *
     * @param dateStr    时间字符串
     * @param dataFormat 当前时间字符串的格式。
     * @return Date 日期 ,转换异常时返回null。
     */
    public static Date parseDate(String dateStr, String dataFormat) {
        try {
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat dateFormat = new SimpleDateFormat(dataFormat);
            Date date = dateFormat.parse(dateStr);
            return new Date(date.getTime());
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            return null;
        }
    }

    /**
     * 时间计算
     *
     * @param millisecond 毫秒数
     * @return 计算后显示的文字
     */
    public static String timeCalculate(long millisecond) {
        long diff = System.currentTimeMillis() - millisecond;//时间差
        if (diff < 1000 * 60/* && diff > 0*/)
            return "刚刚";
        else if (diff >= 1000 * 60 && diff < 1000 * 60 * 60)
            return diff / (1000 * 60) + "分钟以前";
        else if (diff >= 1000 * 60 * 60 && diff < 1000 * 60 * 60 * 24)
            return diff / (1000 * 60 * 60) + "小时以前";
        else if (diff >= 1000 * 60 * 60 * 24 && diff < 1000L * 60 * 60 * 24 * 30)
            return diff / (1000 * 60 * 60 * 24) + "天以前";
        else if (diff >= 1000L * 60 * 60 * 24 * 30 && diff < 1000L * 60 * 60 * 24 * 30 * 12) {
            return diff / (1000L * 60 * 60 * 24 * 30) + "月以前";
        } else {
            SimpleDateFormat format = new SimpleDateFormat(YMD_PATTERN);
            Date date = new Date(millisecond);
            return format.format(date);
        }
    }

    /**
     * 时间计算
     *
     * @param millisecond 毫秒数
     * @return 计算后显示
     */
    public static String timeCalculation(Context context, long millisecond) {
        long diff = System.currentTimeMillis() - millisecond;//时间差
        if (diff < ONE_MINUTE/* && diff > 0*/) {
            return context.getString(R.string.message_text_just_now);
        } else if (diff >= ONE_MINUTE && diff < ONE_HOUR) {
            return context.getResources().getQuantityString(R.plurals.message_text_minutes_ago, (int) (diff / ONE_MINUTE), (int) (diff / ONE_MINUTE));
        } else if (diff >= ONE_HOUR && diff < ONE_DAY) {
            return context.getResources().getQuantityString(R.plurals.message_text_hours_ago, (int) (diff / ONE_HOUR), (int) (diff / ONE_HOUR));
        } else if (diff >= ONE_DAY && diff < ONE_MONTH) {
            return context.getResources().getQuantityString(R.plurals.message_text_days_ago, (int) (diff / ONE_DAY), (int) (diff / ONE_DAY));
        } else if (diff >= ONE_MONTH && diff < ONE_YEAR) {
            return context.getResources().getQuantityString(R.plurals.message_text_months_ago, (int) (diff / ONE_MONTH), (int) (diff / ONE_MONTH));
        } else {
            SimpleDateFormat format = new SimpleDateFormat(YMD_PATTERN);
            Date date = new Date(millisecond);
            return format.format(date);
        }
    }

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        long l = System.currentTimeMillis() / 1000L;
        System.out.println(l);
        System.out.println(timeCalculate(l));
        getCurrentTimeZone();
        getRawOffset();
        System.out.println(getWeekInMills(l));
    }

    /**
     * 把long时间转换成时间格式字符串
     *
     * @param time 时间
     * @return
     */
    public static String generateTime(long time) {
        int totalSeconds = (int) (time / 1000);
        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;

        return hours > 0 ? String.format("%02d:%02d:%02d", hours, minutes, seconds) : String.format("%02d:%02d", minutes, seconds);
    }

    /**
     * 获取年
     *
     * @param mills
     * @return
     */
    public static int getYearInMills(long mills) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(mills);
        return calendar.get(Calendar.YEAR);
    }

    /**
     * 获取周数
     *
     * @param mills
     * @return
     */
    public static int getWeekInMills(long mills) {
        Calendar cal = Calendar.getInstance();//这一句必须要设置，否则美国认为第一天是周日，而我国认为是周一，对计算当期日期是第几周会有错误
        cal.setFirstDayOfWeek(Calendar.MONDAY); // 设置每周的第一天为星期一
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);// 每周从周一开始
        cal.setMinimalDaysInFirstWeek(7); // 设置每周最少为7天
        cal.setTime(new Date());
        int weeks = cal.get(Calendar.WEEK_OF_YEAR);
        return weeks;
    }

    /**
     * 获取月份
     *
     * @param mills
     * @return
     */
    public static int getMonthInMills(long mills) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(mills);
        return calendar.get(Calendar.MONTH) + 1;
    }

    /**
     * 获取天数
     *
     * @param mills
     * @return
     */
    public static int getDayInMills(long mills) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(mills);
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 获取小时
     *
     * @param mills
     * @return
     */
    public static int getHourInMills(long mills) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(mills);
        return calendar.get(Calendar.HOUR_OF_DAY);
    }


    /**
     * 获取分钟
     *
     * @param mills
     * @return
     */
    public static int getMinuteInMills(long mills) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(mills);
        return calendar.get(Calendar.MINUTE);
    }
}