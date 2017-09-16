package com.jusenr.toolslibrary.utils;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;

import java.security.MessageDigest;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * string处理
 * Created by guchenkai on 2015/11/24.
 */
public final class StringUtils {

    /**
     * 字符串判空
     *
     * @param target 目标
     * @return 是否为空
     */
    public static boolean isEmpty(String target) {
        if (TextUtils.isEmpty(target) || TextUtils.equals("null", target))
            return true;
        return false;
    }

    /**
     * 判断字符串target1是否包含字符串target2
     *
     * @param text1 目标1
     * @param text2 目标2
     * @return 是否包含
     */

    public static boolean containsString(String text1, String text2) {
        if (text1.contains(text2))
            return true;
        return false;
    }

    /**
     * 判断两个字符串是否相同
     *
     * @param target1 目标1
     * @param target2 目标2
     * @return 是否相同
     */
    public static boolean equals(String target1, String target2) {
        return TextUtils.equals(target1, target2);
    }

    /**
     * 获得UUID
     *
     * @return UUID
     */
    public static String getUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 匹配正则表达式
     *
     * @param text  被匹配文本
     * @param regex 正则表达式
     * @return 是否匹配
     */
    public static boolean checkRegex(final String text, final String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);
        return matcher.matches();
    }

    /**
     * 验证邮箱
     *
     * @param email 电子邮箱地址
     * @return 是否匹配
     */
    public static boolean checkEmailFormat(String email) {
        return checkRegex(email,
                "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$");
    }

    /**
     * 验证手机号码
     *
     * @param mobile 手机号码
     * @return 是否匹配
     */
    public static boolean checkMobileFormat(String mobile) {
        return checkRegex(mobile, "^[1][3,4,5,7,8][0-9]{9}$");
    }

    /**
     * 验证密码(6到16位)
     *
     * @param password 密码文本
     * @return 是否匹配
     */
    public static boolean checkPasswordFormat(String password) {
        return checkRegex(password, "[A-Z0-9a-z]{6,16}");
    }

    /**
     * 验证密码(6到18位)
     *
     * @param password 密码文本
     * @return 是否匹配
     */
    public static boolean checkBindPasswordFormat(String password) {
        return checkRegex(password, "[A-Z0-9a-z]{6,18}");
    }

    /**
     * 验证中英文数字组合
     *
     * @param string 文本
     * @return 是否匹配
     */
    public static boolean checkCnEnNumFormat(String string) {
        String reg = "^[\\u4E00-\\u9FA5A-Za-z0-9]+$";
        return checkRegex(string, reg);
    }

    /**
     * 查字符串是否有空格
     *
     * @param string 文本
     * @return 是否有空格
     */
    public static boolean checkHasSpaceFormat(String string) {
        if (StringUtils.isEmpty(string)) return true;
        if (string.contains(" "))
            return true;
        return false;
    }

    /**
     * Check if the string is a numeric string
     *
     * @param target target
     * @return Match [boolean]
     */
    public static boolean isNumeric(String target) {
        if (StringUtils.isEmpty(target)) return false;
        if (target.substring(0, 1).equals("-")) target = target.replaceFirst("-", "");
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern != null ? pattern.matcher(target) : null;
        if (isNum != null && !isNum.matches()) {
            return false;
        }
        return true;
    }

    static String regEx = "[\\u4e00-\\u9fa5]"; // Unicode encoding to determine whether the Chinese characters.

    public static int getChineseCount(String str) {
        int count = 0;
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        while (m.find()) {
            for (int i = 0; i <= m.groupCount(); i++) {
                count = count + 1;
            }
        }
        return count;
    }

    public static boolean isContainChinese(String str) {
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        if (m.find()) {
            return true;
        }
        return false;
    }

    public static String getCutStringByByteCount(String str, int count) {
        String newStr = "";
        int addCount = 0;
        for (int i = 0; i < str.length(); i++) {
            addCount++;
            String substring = str.substring(i, i + 1);
            if (isContainChinese(substring))
                addCount++;
            if (addCount > count)
                break;
            newStr = newStr + substring;
        }
        if (newStr.length() <= str.length())
            return newStr;
        else
            return str;
    }

    public static String getCutStringByByteCount(String str, int count, String endString) {
        String cutStringByByteCount = getCutStringByByteCount(str, count);
        if (cutStringByByteCount.equals(str))
            return cutStringByByteCount;
        else return cutStringByByteCount + endString;
    }


    /**
     * Semiangle into full width
     *
     * @param input Alphanumeric string
     * @return Double byte string
     */
    public static String ToSBC(String input) {
        if (TextUtils.isEmpty(input))
            return "";
        // Half turn angle:
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 32) {
                c[i] = (char) 12288;
                continue;
            }
            if (c[i] < 127 && c[i] > 32)
                c[i] = (char) (c[i] + 65248);
        }
        return new String(c);
    }

    public static CharSequence filterEmoji(CharSequence source) {
        if (!containsEmoji(source)) {
            return source;
        } else {
            return "";
        }
    }

    public static boolean containsEmoji(CharSequence source) {
        int len = source.length();
        for (int i = 0; i < len; i++) {
            char codePoint = source.charAt(i);
            if (isEmojiCharacter(codePoint)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isEmojiCharacter(char codePoint) {
        boolean temp = (codePoint == 0x0)
                || (codePoint == 0x9)
                || (codePoint == 0xA)
                || (codePoint == 0xD)
                || ((codePoint >= 0x20) && (codePoint <= 0xD7FF))
                || ((codePoint >= 0xE000) && (codePoint <= 0xFFFD))
                || ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF));

        return !temp;
    }

    public static String generateSign(Map<String, String> param, String secretkey) {
        Map<String, String> map = sortMapByKey(param);
        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
        String result = sb.delete(sb.length() - 1, sb.length()).append(secretkey).toString();
        return getMD5Str(result);
    }

    public static String getMD5Str(String str) {
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(str.getBytes("UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        byte[] byteArray = messageDigest.digest();
        StringBuffer md5StrBuff = new StringBuffer();
        for (int i = 0; i < byteArray.length; i++) {
            if (Integer.toHexString(0xFF & byteArray[i]).length() == 1) {
                md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
            } else {
                md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
            }
        }
        return md5StrBuff.toString().toLowerCase();
    }

    public static boolean isLetterOrDigit(String str) {
        boolean isLetterOrDigit = false;
        for (int i = 0; i < str.length(); i++) {
            //Judge each character with the method of determining numbers in the char wrapper class.
            if (Character.isLetterOrDigit(str.charAt(i))) {
                isLetterOrDigit = true;
            }
        }
        String rex = "^[a-zA-Z0-9]+$";
        return isLetterOrDigit && str.matches(rex);
    }

    public static boolean isPhoneNumber(String str) {
        String regExp = "^[1]([3|7|5|8]{1}\\d{1})\\d{8}$";
        return str.matches(regExp);
    }

    /**
     * Use Map to sort by key
     *
     * @param map map
     * @return map
     */
    public static Map<String, String> sortMapByKey(Map<String, String> map) {
        if (map == null || map.isEmpty())
            return null;
        Map<String, String> sortMap = new TreeMap<>(new MapKeyComparator());
        sortMap.putAll(map);
        return sortMap;
    }

    /**
     * Sorter
     */
    public static class MapKeyComparator implements Comparator<String> {

        @Override
        public int compare(String o1, String o2) {
            return o1.compareTo(o2);
        }
    }

    /**
     * Returns to a set of strings, respectively, after adding colors.
     *
     * @param text  string array
     * @param color color array
     * @return CharSequence[]
     */
    public static CharSequence[] getCharSequences(CharSequence[] text, int[] color) {
        CharSequence[] charSequences = new CharSequence[text.length];
        for (int i = 0; i < text.length; i++) {
            Spannable colorString = new SpannableString(text[i]);
            colorString.setSpan(new ForegroundColorSpan(color[i]), 0, colorString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            charSequences[i] = colorString;
        }

        return charSequences;
    }
}
