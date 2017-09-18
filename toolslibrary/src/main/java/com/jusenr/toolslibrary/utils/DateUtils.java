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

    private DateUtils() {
        throw new AssertionError();
    }

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
     * A display of the date format corresponding to the seconds value and the incoming date.
     *
     * @param seconds seconds
     * @param date    date
     * @return The string displayed in the corresponding date format for the specific (date by itself).
     * @throws ParseException exception
     */
    public static String getSecondsToDate(int seconds, Date date) throws ParseException {
        return getMillisToDate(seconds * 1000L, date);
    }

    /**
     * Displays a date format similar to the current date based on the millisecond value.
     *
     * @param millis millis
     * @return A string that is displayed in the corresponding date format for a specific (current date).
     * @throws ParseException exception
     */
    public static String getMillisToDate(long millis) throws ParseException {
        Long time = System.currentTimeMillis();
        return getMillisToDate(millis, new Date(time));
    }

    /**
     * Displays a date format similar to the current date based on the seconds value.
     *
     * @param seconds seconds
     * @return A string that is displayed in the corresponding date format for a specific (current date).
     * @throws ParseException exception
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
     * Calculates the number of days in which two dates differ, whether or not to take absolute values.
     *
     * @param date1 first date
     * @param date2 Second date
     * @param isAbs Do you take absolute values?
     * @return dim dd
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
     * Calculates the number of days in which the two dates differ, and takes the absolute value.
     *
     * @param date1 first date
     * @param date2 Second date
     * @return dim dd
     */
    public static int getDaysUnAbs(String date1, String date2) {
        return getDaysUnAbs(date1, date2, true);
    }

    /**
     * Converts long format strings to time e.g.yyyy-MM-dd HH:mm:ss.
     *
     * @param date date
     * @return Long format date
     */
    public static Date strToDateLong(String date) {
        ParsePosition pos = new ParsePosition(0);
        return mTimeFormat.parse(date, pos);
    }

    /**
     * Gets the current date time
     *
     * @return Current date time
     */
    public static String getCurrentTime() {
        return mTimeFormat.format(new Date());
    }

    /**
     * Gets the current date
     *
     * @return Current date
     */
    public static String getCurrentDate() {
        return mDataFormat.format(new Date());
    }

    /**
     * Gets the input date is a few weeks
     *
     * @param date date
     * @return days of the week
     */
    public static String getWeekDate(Date date) {
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        calendar.setTime(date);
        return weeks[calendar.get(calendar.DAY_OF_WEEK) - 1];
    }

    /**
     * Gets the input date is a few weeks
     *
     * @param context context
     * @param date    date
     * @return days of the week
     */
    public static String getWeekDate(Context context, Date date) {
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        calendar.setTime(date);
        return context.getResources().getStringArray(R.array.weeks)[calendar.get(calendar.DAY_OF_WEEK) - 1];
    }

    /**
     * Gets the input date is a few weeks
     *
     * @param date date
     * @return days of the week
     */
    public static String getWeekDate(String date) {
        try {
            Date d = mDataFormat.parse(date);
            return getWeekDate(d);
        } catch (ParseException e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
        }
        return "The weeks are unknown.";
    }

    /**
     * Milliseconds to date
     *
     * @param millisecond millisecond
     * @param pattern     pattern
     * @return date [pattern]
     */
    public static String millisecondToDate(long millisecond, String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        Date date = new Date(millisecond);
        return format.format(date);
    }

    /**
     * Millisecond value to date (yyyy/MM/dd)
     *
     * @param millisecond millisecond
     * @return date [yyyy/MM/dd]
     */
    public static String millisecondToDate(long millisecond) {
        return millisecondToDate(millisecond * 1000L, YMD_PATTERN2);
    }

    /**
     * Second turn date
     *
     * @param second  second
     * @param pattern pattern
     * @return date [pattern]
     */
    public static String secondToDate(int second, String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        Date date = new Date(second * 1000L);
        return format.format(date);
    }

    /**
     * Converts a timestamp to a string
     *
     * @param timeStamp  time stamp
     * @param dataFormat yyyy-MM-dd HH:mm:ss string
     * @return yyyy-MM-dd HH:mm:ss Format date string
     */
    public static String long_2_str(long timeStamp, String dataFormat) {
        return new SimpleDateFormat(dataFormat).format(new Date(timeStamp * 1000L));
    }

    /**
     * Converts a yyyy-MM-dd HH:mm:ss string into a date
     *
     * @param dateStr    dateStr
     * @param dataFormat dataFormat。
     * @return Date and returns null. when an exception is converted
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
     * time calculation
     *
     * @param millisecond millisecond
     * @return Text to be displayed after calculation
     */
    public static String timeCalculate(long millisecond) {
        long diff = System.currentTimeMillis() - millisecond;//time difference
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
     * time calculation
     *
     * @param millisecond millisecond
     * @return Text to be displayed after calculation
     */
    public static String timeCalculation(Context context, long millisecond) {
        long diff = System.currentTimeMillis() - millisecond;//time difference
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
     * Converts long time into a time format string
     *
     * @param time time
     * @return time format string
     */
    public static String generateTime(long time) {
        int totalSeconds = (int) (time / 1000);
        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;

        return hours > 0 ? String.format("%02d:%02d:%02d", hours, minutes, seconds) : String.format("%02d:%02d", minutes, seconds);
    }

    /**
     * Get years
     *
     * @param mills mills
     * @return years
     */
    public static int getYearInMills(long mills) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(mills);
        return calendar.get(Calendar.YEAR);
    }

    /**
     * Get weeks
     *
     * @param mills mills
     * @return weeks
     */
    public static int getWeekInMills(long mills) {
        Calendar cal = Calendar.getInstance();//This sentence must be set, otherwise, the United States believes that the first day is Sunday, and our country believes that Monday, the calculation of the current date is the first few weeks there will be errors.
        cal.setFirstDayOfWeek(Calendar.MONDAY); // The first day of the week set for Monday.
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);// Starting every Monday.
        cal.setMinimalDaysInFirstWeek(7); // Set for at least 7 days a week.
        cal.setTime(new Date());
        int weeks = cal.get(Calendar.WEEK_OF_YEAR);
        return weeks;
    }

    /**
     * Get months
     *
     * @param mills mills
     * @return months
     */
    public static int getMonthInMills(long mills) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(mills);
        return calendar.get(Calendar.MONTH) + 1;
    }

    /**
     * Get days
     *
     * @param mills mills
     * @return days
     */
    public static int getDayInMills(long mills) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(mills);
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * Get hours
     *
     * @param mills mills
     * @return hours
     */
    public static int getHourInMills(long mills) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(mills);
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    /**
     * Get minutes
     *
     * @param mills mills
     * @return minutes
     */
    public static int getMinuteInMills(long mills) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(mills);
        return calendar.get(Calendar.MINUTE);
    }
}