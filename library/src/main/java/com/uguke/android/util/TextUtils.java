package com.uguke.android.util;

import com.uguke.android.util.CheckUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * 文本工具类
 * @author LeiJue
 */
public class TextUtils {

    private TextUtils() {
        throw new UnsupportedOperationException("can't instantiate me...");
    }

    public static boolean isEmpty(CharSequence str) {
        return str == null || str.length() == 0;
    }

    public static CharSequence convertNull(CharSequence str) {
        return str == null ? "" : str;
    }

    public static String convertNull(String str) {
        return str == null ? "" : str;
    }

    public static List<String> slipt(String str, String regex) {
        if (CheckUtils.isEmpty(str, regex)) {
            return new ArrayList<>();
        }
        List<String> list = new ArrayList<>();
        list.addAll(Arrays.asList(str.split(regex)));
        return list;
    }

    public static String combine(String [] strings, String regex) {
        if (CheckUtils.isEmpty((Object) strings)) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        int len = strings.length;
        for (int i = 0; i < len; i ++) {
            builder.append(strings[i]);
            if (i < len - 1) {
                builder.append(regex);
            }
        }
        return builder.toString();
    }

    public static String combine(List<String> strings, String regex) {
        if (CheckUtils.isEmpty(strings)) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        int len = strings.size();
        for (int i = 0; i < len; i ++) {
            builder.append(strings.get(i));
            if (i < len - 1) {
                builder.append(regex);
            }
        }
        return builder.toString();
    }

    public static String format(String format, Object ... args) {
        return String.format(Locale.getDefault(), format, args);
    }

    public static String formatNumberUp(double number, int scale) {
        return formatNumberUp(number, scale, 1);
    }

    public static String formatNumberUp(double number, int scale, float ratio) {
        return new BigDecimal(number / ratio).setScale(Math.abs(scale), BigDecimal.ROUND_HALF_UP).toString();
    }

    public static String formatNumberDown(double number, int scale) {
        return formatNumberDown(number, scale, 1);
    }

    public static String formatNumberDown(double number, int scale, float ratio) {
        return new BigDecimal(number / ratio).setScale(Math.abs(scale), BigDecimal.ROUND_HALF_DOWN).toString();
    }
}
