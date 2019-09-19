package com.uguke.android.util;

import java.lang.ref.Reference;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

/**
 * 检查工具
 * @author LeiJue
 */
public class CheckUtils {

    /** 数字 **/
    private static final String REGEX_NUMBER              = "^(\\-|\\+)?\\d+(\\.\\d+)?$";
    /** 整数 **/
    private static final String REGEX_INTEGER             = "^-?[1-9]\\d*$";
    /** 正整数 **/
    private static final String REGEX_INTEGER_POSITIVE    = "^[1-9]\\d*$";
    /** 负整数 **/
    private static final String REGEX_INTEGER_NEGATIVE    = "^-[1-9]\\d*$";
    /** 浮点数 **/
    private static final String REGEX_FLOAT               = "^(-?\\d+)(\\.\\d+)?$";
    /** 正浮点数 **/
    private static final String REGEX_FLOAT_POSITIVE      = "^[1-9]\\d*\\.\\d*|0\\.\\d*[1-9]\\d*$";
    /** 负浮点数 **/
    private static final String REGEX_FLOAT_NEGATIVE      = "^-[1-9]\\d*\\.\\d*|0\\.\\d*[1-9]\\d*$";

    /** 中文 **/
    private static final String REGEX_CN                  = "^[\\u4e00-\\u9fa5]+$";
    /** 中文和26个字母 **/
    private static final String REGEX_CN_WORD             = "^[\\u4e00-\\u9fa5A-Za-z]+$";
    /** 中文和26个字母及数字 **/
    private static final String REGEX_CN_WORD_NUMBER      = "^[\\u4e00-\\u9fa5A-Za-z0-9]+$";
    /** 中文和26个字母及数字下划线 **/
    private static final String REGEX_CN_WORD_NORMAL      = "^[\\u4e00-\\u9fa5A-Za-z0-9_]+$";
    /** 26个字母 **/
    private static final String REGEX_WORD                = "^[A-Za-z]+$";
    /** 26个字母及数字**/
    private static final String REGEX_WORD_NUMBER         = "^[A-Za-z0-9]+$";
    /** 26个字母及数字下划线 **/
    private static final String REGEX_WORD_NORMAL         = "^[A-Za-z0-9_]+$";
    /** 26个大写字母 **/
    private static final String REGEX_WORD_UPPER          = "^[A-Z]+$";
    /** 26个小写字母 **/
    private static final String REGEX_WORD_LOWER          = "^[a-z]+$";

    /** 邮箱 **/
    private static final String REGEX_EMAIL               = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
    /** 简单的手机号 **/
    private static final String REGEX_PHONE_SIMPLE        = "^[1]\\d{10}$";
    /** 严格的手机号 **/
    private static final String REGEX_PHONE_EXACT         = "^((13[0-9])|(14[5,7])|(15[0-3,5-9])|(16[6])|(17[0,1,3,5-8])|(18[0-9])|(19[8,9]))\\d{8}$";
    /** 座机号码 **/
    private static final String REGEX_TELEPHONE           = "^0\\d{2,3}[- ]?\\d{7,8}";
    /** 15位身份证号码 **/
    private static final String REGEX_ID_CARD15           = "^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$";
    /** 18位身份证号码 **/
    private static final String REGEX_ID_CARD18           = "^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9Xx])$";
    /** Url验证 **/
    private static final String REGEX_URL                 = "http(s)?://([\\w-]+\\.)+[\\w-]+(/[\\w- ./?%&=]*)?";
    /** 昵称 **/
    private static final String REGEX_NICKNAME            = "^[\\w\\u4e00-\\u9fa5]{%d,%d}(?<!_)$";
    /** 用户名 **/
    private static final String REGEX_USERNAME            = "^[a-zA-Z][a-zA-Z0-9_]{%d,%d}$";
    /** 简单密码 **/
    private static final String REGEX_PASSWORD            = "^[a-zA-Z0-9_][a-z0-9A-Z_.@]{%d,%d}$";
    /** 验证IP地址 **/
    private static final String REGEX_REGEX_IP            = "((2[0-4]\\d|25[0-5]|[01]?\\d\\d?)\\.){3}(2[0-4]\\d|25[0-5]|[01]?\\d\\d?)";
    /** 空白行 **/
    private static final String REGEX_BLANK               = "\\n\\s*\\r";
    /** QQ号 **/
    private static final String REGEX_QQ_NUM              = "[1-9][0-9]{4,}";
    /** 中国的邮政编码 **/
    private static final String REGEX_CN_POSTAL_CODE      = "[1-9]\\d{5}(?!\\d)";

    private Map<Class, Method> sizeMethodMap = new ConcurrentHashMap<>();
    private Map<Class, Method> lengthMethodMap = new ConcurrentHashMap<>();

    private CheckUtils() {}

    private static class Holder {
        static final CheckUtils INSTANCE = new CheckUtils();
    }

    public static void notNull(Object o, String exception) {
        if (o == null) {
            throw new NullPointerException(exception);
        }
    }

    public static boolean isNull(Object data) {
        return data == null;
    }

    @SuppressWarnings("unchecked")
    public static boolean isEmpty(Object data) {
        if (data == null) {
            return true;
        }
        Class clazz = data.getClass();
        if (data instanceof CharSequence) {
            return ((CharSequence) data).length() == 0;
        } else if (data instanceof Collection) {
            return ((Collection) data).size() == 0;
        } else if (data instanceof Map) {
            return ((Map) data).size() == 0;
        } else if (data instanceof Reference) {
            return ((Reference) data).get() == null;
        } else if (clazz.isArray()) {
            if (clazz.equals(boolean [].class)) {
                return ((boolean []) data).length  == 0;
            } else if (clazz.equals(char [].class)) {
                return ((char []) data).length == 0;
            } else if (clazz.equals(byte [].class)) {
                return ((byte []) data).length == 0;
            } else if (clazz.equals(short [].class)) {
                return ((short []) data).length == 0;
            } else if (clazz.equals(int [].class)) {
                return ((int []) data).length == 0;
            } else if (clazz.equals(long [].class)) {
                return ((long []) data).length == 0;
            } else if (clazz.equals(float [].class)) {
                return ((float []) data).length == 0;
            } else if (clazz.equals(double [].class)) {
                return ((double []) data).length == 0;
            }
            return ((Object []) data).length == 0;
        } else {
            try {
                // 让反射的method方法重复利用
                Method method = Holder.INSTANCE.sizeMethodMap.get(clazz);
                if (method == null) {
                    method = clazz.getMethod("size");
                    method.setAccessible(true);
                    Holder.INSTANCE.sizeMethodMap.put(clazz, method);
                }
                return (int) method.invoke(data) == 0;
            } catch (NoSuchMethodException e) {
                // 让反射的method方法重复利用
                try {
                    Method method = Holder.INSTANCE.lengthMethodMap.get(clazz);
                    if (method == null) {
                        method = clazz.getMethod("length");
                        method.setAccessible(true);
                        Holder.INSTANCE.lengthMethodMap.put(clazz, method);
                    }
                    return (int) method.invoke(data) == 0;
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e1) {
                    return false;
                }
            } catch (IllegalAccessException | InvocationTargetException e) {
                return false;
            }
        }
    }

    public static boolean isEmpty(Object ...data) {
        for (Object o : data) {
            if (isEmpty(o)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isBlank(String str) {
        return isMatch(REGEX_BLANK, str);
    }

    public static boolean isNumber(String str) {
        return isMatch(REGEX_NUMBER, str);
    }

    public static boolean isInteger(String str) {
        return isMatch(REGEX_INTEGER, str);
    }

    public static boolean isPositiveInteger(String str) {
        return isMatch(REGEX_INTEGER_POSITIVE, str);
    }

    public static boolean isNegativeInteger(String str) {
        return isMatch(REGEX_INTEGER_NEGATIVE, str);
    }

    public static boolean isFloat(String str) {
        return isMatch(REGEX_FLOAT, str);
    }

    public static boolean isPositiveFloat(String str) {
        return isMatch(REGEX_FLOAT_POSITIVE, str);
    }

    public static boolean isNegativeFloat(String str) {
        return isMatch(REGEX_FLOAT_NEGATIVE, str);
    }

    public static boolean isCn(String str) {
        return isMatch(REGEX_CN, str);
    }

    public static boolean isCnEn(String str) {
        return isMatch(REGEX_CN_WORD, str);
    }

    /**
     * 字符串是否汉字字母和数字
     * @param str 待检验字符串
     */
    public static boolean isCnEnNumber(String str) {
        return isMatch(REGEX_CN_WORD_NUMBER, str);
    }

    /**
     * 字符串是否是汉字、字母、数字和下划线
     * @param str 待检验字符串
     */
    public static boolean isCnWordNormal(String str) {
        return isMatch(REGEX_CN_WORD_NORMAL, str);
    }

    /**
     * 字符串是否是字母
     * @param str 待检验字符串
     */
    public static boolean isWord(String str) {
        return isMatch(REGEX_WORD, str);
    }

    /**
     * 字符串是否是字母、数字
     * @param str 待检验字符串
     */
    public static boolean isWordNumber(String str) {
        return isMatch(REGEX_WORD_NUMBER, str);
    }

    /**
     * 字符串是否是字母、数字和下划线
     * @param str 待检验字符串
     */
    public static boolean isWordNormal(String str) {
        return isMatch(REGEX_WORD_NORMAL, str);
    }

    /**
     * 字符串是否是全大写字母
     * @param str 待检验字符串
     */
    public static boolean isWordUpper(String str) {
        return isMatch(REGEX_WORD_UPPER, str);
    }

    /**
     * 字符串是否是全小写字母
     * @param str 待检验字符串
     */
    public static boolean isWordLower(String str) {
        return isMatch(REGEX_WORD_LOWER, str);
    }

    /**
     * 简单检验字符串是否是手机号
     * @param str 待检验字符串
     */
    public static boolean isPhoneSimple(String str) {
        return isMatch(REGEX_PHONE_SIMPLE, str);
    }

    /**
     * 复杂检验字符串是否是手机号
     * @param str 待检验字符串
     */
    public static boolean isPhoneExact(String str) {
        return isMatch(REGEX_PHONE_EXACT, str);
    }

    /**
     * 字符串是否是座机号
     * @param str 待检验字符串
     */
    public static boolean isTelephone(String str) {
        return isMatch(REGEX_TELEPHONE, str);
    }

    public static boolean isIDCard15(String str) {
        return isMatch(REGEX_ID_CARD15, str);
    }

    public static boolean isIDCard18(String str) {
        return isMatch(REGEX_ID_CARD18, str);
    }

    public static boolean isEmail(String str) {
        return isMatch(REGEX_EMAIL, str);
    }

    public static boolean isURL(String str) {
        return isMatch(REGEX_URL, str);
    }

    public static boolean isNickname(String str) {
        return isMatch(TextUtils.format(REGEX_NICKNAME, 1, 15), str);
    }

    public static boolean isNickname(String str, int minLength, int maxLength) {
        return isMatchstr(REGEX_NICKNAME, str, minLength, maxLength);
    }

    /**
     * 字符串是否是合法用户名
     * @param str 待检验字符串
     */
    public static boolean isUsername(String str) {
        return isMatch(TextUtils.format(REGEX_USERNAME, 3, 15), str);
    }

    /**
     * 字符串是否是合法用户名
     * @param str 待检验字符串
     * @param minLength 最小长度
     * @param maxLength 最大长度
     */
    public static boolean isUsername(String str, int minLength, int maxLength) {
        return isMatchstr(REGEX_USERNAME, str, minLength, maxLength);
    }

    /**
     * 字符串是否是IP地址
     * @param str 待检验字符串
     */
    public static boolean isIP(String str) {
        return isMatch(REGEX_REGEX_IP, str);
    }

    /**
     * 简单检验字符串是否是密码
     * @param str 待检验字符串
     */
    public static boolean isPassword(String str) {
        return isMatch(TextUtils.format(REGEX_PASSWORD, 5, 15), str);
    }

    /**
     * 简单检验字符串是否是密码
     * @param str 待检验字符串
     * @param minLength 最小长度
     * @param maxLength 最大长度
     */
    public static boolean isPassword(String str, int minLength, int maxLength) {
        return isMatchstr(REGEX_PASSWORD, str, minLength, maxLength);
    }

    public static boolean isMatchstr(String regex, String str, int minLength, int maxLength) {
        // 获取绝对值最大和最小
        int tempMin = Math.min(Math.abs(minLength), Math.abs(maxLength));
        int tempMax = Math.max(Math.abs(minLength), Math.abs(maxLength));
        // 密码最小长度为1
        tempMin = tempMin < 1 ? 1 : tempMin;
        // 密码最大长度为1
        tempMax = tempMax < 1 ? 1 : tempMax;
        return isMatch(TextUtils.format(regex, tempMin - 1, tempMax - 1), str);
    }


    /**
     * 字符串是否是QQ号
     * @param str 待检验字符串
     */
    public static boolean isQQNum(String str) {
        return isMatch(REGEX_QQ_NUM, str);
    }

    /**
     * 字符串是否是中国邮政编码
     * @param str 待检验字符串
     */
    public static boolean isCnPostalCode(String str) {
        return isMatch(REGEX_CN_POSTAL_CODE, str);
    }

    public static boolean isDate(String date) {
        return DateUtils.isDate(date);
    }

    /**
     * 正则表达式检验字符串
     * @param regex 正则表达式
     * @param str 待检验字符串
     */
    public static boolean isMatch(String regex, String str) {
        return regex != null && str != null &&
                str.length() > 0 && Pattern.matches(regex, str);
    }
}
