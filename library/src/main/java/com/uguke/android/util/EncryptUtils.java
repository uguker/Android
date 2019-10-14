package com.uguke.android.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 加密工具
 * @author LeiJue
 */
public class EncryptUtils {

    /** 哈希码 **/
    private static final char [] HEX_DIGITS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    private EncryptUtils() {
        throw new UnsupportedOperationException("can't instantiate me...");
    }

    /**
     * MD5加密
     * @param string 加密字符串
     * @return 加密结果字符串
     * @see #md5(String, String)
     */
    public static String md5(String string) {
        return CheckUtils.isEmpty(string) ? "" : md5(string, "");
    }

    /**
     * MD5加密(多次)
     * @param string 加密字符串
     * @param times  重复加密次数
     * @return 加密结果字符串
     */
    public static String md5(String string, int times) {
        if (CheckUtils.isEmpty(string)) {
            return "";
        }
        String md5 = string;
        for (int i = 0; i < times; i++) {
            md5 = md5(md5);
        }
        return md5;
    }

    /**
     * MD5加密(加盐)
     * @param string 加密字符串
     * @param slat   加密盐值key
     * @return 加密结果字符串
     */
    public static String md5(String string, String slat) {
        if (CheckUtils.isEmpty(string)) {
            return "";
        }
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] bytes = md.digest((string + slat).getBytes());
            return hex(bytes);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String md5(File file) {
        if (!file.isFile()) {
            return "";
        }
        MessageDigest digest;
        FileInputStream in;
        byte[] buffer = new byte[1024];
        int length = 1024;
        int len;
        try {
            digest = MessageDigest.getInstance("MD5");
            in = new FileInputStream(file);
            while ((len = in.read(buffer, 0, length)) != -1) {
                digest.update(buffer, 0, len);
            }
            in.close();
        } catch (NoSuchAlgorithmException | IOException e) {
            e.printStackTrace();
            return "";
        }
        BigInteger bigInt = new BigInteger(1, digest.digest());
        return bigInt.toString(16);
    }

    public static String sha1(String text){
        try {
            MessageDigest md = MessageDigest.getInstance("SHA1");
            byte[] bytes = md.digest(text.getBytes());
            return hex(bytes);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String sha224(String text){
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-224");
            byte[] bytes = md.digest(text.getBytes());
            return hex(bytes);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String sha256(String text){
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] bytes = md.digest(text.getBytes());
            return hex(bytes);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String sha384(String text){
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-384");
            byte[] bytes = md.digest(text.getBytes());
            return hex(bytes);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String sha512(String text){
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            byte[] bytes = md.digest(text.getBytes());
            return hex(bytes);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 转化为哈希字符串
     * @param text 待转换字节数组
     */
    public static String hex(byte[] text) {
        StringBuilder ret = new StringBuilder(text.length * 2);
        for (byte b : text) {
            ret.append(HEX_DIGITS[(b >> 4) & 0x0f]);
            ret.append(HEX_DIGITS[b & 0x0f]);
        }
        return ret.toString();
    }
}
