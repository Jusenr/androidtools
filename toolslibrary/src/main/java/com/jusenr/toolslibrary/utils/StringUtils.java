package com.jusenr.toolslibrary.utils;

import android.text.InputFilter;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.widget.EditText;

import java.security.MessageDigest;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * String processing tools class
 * Created by guchenkai on 2015/11/24.
 */
public final class StringUtils {

    private StringUtils() {
        throw new AssertionError();
    }

    /**
     * String is null
     *
     * @param target target
     * @return IS NULL
     */
    public static boolean isEmpty(String target) {
        if (TextUtils.isEmpty(target) || TextUtils.equals("null", target))
            return true;
        return false;
    }

    /**
     * Input frame character length limit
     *
     * @param mEdit     EditText
     * @param maxLength maxLength
     */
    public void setEditable(EditText mEdit, int maxLength) {
        if (mEdit.getText().length() < maxLength) {
            mEdit.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength) {
            }});
            mEdit.setCursorVisible(true);
            mEdit.setFocusableInTouchMode(true);
            mEdit.requestFocus();
        } else {
            mEdit.setFilters(new InputFilter[]{new InputFilter() {
                @Override
                public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                    return source.length() < 1 ? dest.subSequence(dstart, dend) : "";
                }
            }});
            mEdit.setCursorVisible(false);
            mEdit.setFocusableInTouchMode(false);
            mEdit.clearFocus();
        }
    }

    /**
     * Determines whether string text1 contains string text2
     *
     * @param text1 text1
     * @param text2 text2
     * @return Contains [boolean]
     */

    public static boolean containsString(String text1, String text2) {
        if (text1.contains(text2))
            return true;
        return false;
    }

    /**
     * Determine if the two strings are the same
     *
     * @param target1 target1
     * @param target2 target2
     * @return Is it the same? [boolean]
     */
    public static boolean equals(String target1, String target2) {
        return TextUtils.equals(target1, target2);
    }

    /**
     * Get UUID
     *
     * @return UUID
     */
    public static String getUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * Matches Regular Expression
     *
     * @param text  Matched text
     * @param regex regular expression
     * @return Match [boolean]
     */
    public static boolean checkRegex(final String text, final String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);
        return matcher.matches();
    }

    /**
     * Verify mailbox
     *
     * @param email email
     * @return Match [boolean]
     */
    public static boolean checkEmailFormat(String email) {
        return checkRegex(email, "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$");
    }

    /**
     * Verify phone number
     *
     * @param mobile phone number
     * @return Match [boolean]
     */
    public static boolean checkMobileFormat(String mobile) {
        return checkRegex(mobile, "^[1][3,4,5,7,8][0-9]{9}$");
    }

    /**
     * Verify passwords (6 to 16 bits)
     *
     * @param password passwords
     * @return Match [boolean]
     */
    public static boolean checkPasswordFormat(String password) {
        return checkRegex(password, "[A-Z0-9a-z]{6,16}");
    }

    /**
     * Verify passwords (6 to 18 bits)
     *
     * @param password passwords
     * @return Match [boolean]
     */
    public static boolean checkBindPasswordFormat(String password) {
        return checkRegex(password, "[A-Z0-9a-z]{6,18}");
    }

    /**
     * Verify the combination of Chinese and English numerals
     *
     * @param text text
     * @return Match [boolean]
     */
    public static boolean checkCnEnNumFormat(String text) {
        String reg = "^[\\u4E00-\\u9FA5A-Za-z0-9]+$";
        return checkRegex(text, reg);
    }

    /**
     * Check if strings have spaces
     *
     * @param text text
     * @return Match [boolean]
     */
    public static boolean checkHasSpaceFormat(String text) {
        if (StringUtils.isEmpty(text)) return true;
        if (text.contains(" ")) return true;
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
            for (int i = 0, groupCount = m.groupCount(); i <= groupCount; i++) {
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
        for (int i = 0, length = str.length(); i < length; i++) {
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
        else
            return cutStringByByteCount + endString;
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
        for (int i = 0, length = c.length; i < length; i++) {
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
        for (int i = 0, len = source.length(); i < len; i++) {
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
            sb.append(entry.getKey())
                    .append("=")
                    .append(entry.getValue())
                    .append("&");
        }
        String result = sb.delete(sb.length() - 1, sb.length())
                .append(secretkey).toString();
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
        for (int i = 0, length = byteArray.length; i < length; i++) {
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
        for (int i = 0, length = str.length(); i < length; i++) {
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
        for (int i = 0, length = text.length; i < length; i++) {
            Spannable colorString = new SpannableString(text[i]);
            colorString.setSpan(new ForegroundColorSpan(color[i]), 0, colorString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            charSequences[i] = colorString;
        }

        return charSequences;
    }
}
