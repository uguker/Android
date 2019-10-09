package com.uguke.android.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.io.File;
import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

import static android.os.Build.VERSION.SDK_INT;
import static android.os.Build.VERSION_CODES.LOLLIPOP;

/**
 * 设备工具类
 * @author LeiJue
 */
public final class DeviceUtils {

    /**  **/
    public static final String ERROR_ANDROID_ID = "9774d56d682e549c";
    public static final String DEVICE_UNIQUE_ID = "device_unique_id";

    private DeviceUtils() {
        throw new UnsupportedOperationException("can't instantiate me.");
    }

    public static TelephonyManager getTelephonyManager(Context context) {
        return (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
    }

    public static boolean isPhone(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return tm != null && tm.getPhoneType() != TelephonyManager.PHONE_TYPE_NONE;
    }

    public static boolean isSimReady(Context context) {
        TelephonyManager tm = (TelephonyManager) context .getSystemService(Context.TELEPHONY_SERVICE);
        return tm != null && tm.getSimState() == TelephonyManager.SIM_STATE_READY;
    }

    public static boolean isTablet() {
        return (Resources.getSystem().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    public static boolean isRoot() {
        String su = "su";
        String[] locations = {
                "/system/bin/",
                "/system/xbin/",
                "/sbin/",
                "/system/sd/xbin/",
                "/system/bin/failsafe/",
                "/data/local/xbin/",
                "/data/local/bin/",
                "/data/local/"};
        for (String location : locations) {
            if (new File(location + su).exists()) {
                return true;
            }
        }
        return false;
    }

    public static String getName() {
        try{
            @SuppressLint("PrivateApi")
            Class<?> cls = Class.forName("android.os.SystemProperties");
            Object object = cls.newInstance();
            Method getName = cls.getDeclaredMethod("get", String.class);
            return (String) getName.invoke(object, "persist.sys.device_name");
        } catch (Exception e){
            return "Unknown";
        }
    }

    public static String getModel() {
        String model = Build.MODEL;
        if (model != null) {
            model = model.trim().replaceAll("\\s*", "");
        } else {
            model = "";
        }
        return model;
    }

    public static String getVendor() {
        return Build.MANUFACTURER;
    }

    @SuppressLint({"HardwareIds", "MissingPermission"})
    public static String getDeviceId(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (SDK_INT >= Build.VERSION_CODES.O) {
            return tm != null ? tm.getImei() : "";
        }
        return tm != null ? tm.getDeviceId(): "";
    }

    @SuppressLint("HardwareIds")
    public static String getUniqueId(Context context) {
        // 如果缓存有UUID，则直接返回
        SharedUtils utils = SharedUtils.create(context, DEVICE_UNIQUE_ID);
        String result =  utils.getString(DEVICE_UNIQUE_ID);
        if (!TextUtils.isEmpty(result)) {
            return result;
        }
        String buildId = Build.MANUFACTURER + Build.BRAND + Build.MODEL + (SDK_INT >= LOLLIPOP ?
                        Build.SUPPORTED_ABIS[0] : Build.SUPPORTED_ABIS) + Build.DEVICE + Build.VERSION.INCREMENTAL;
        String androidId = Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
        String macAddress = NetworkUtils.getMacAddress(context);
        result = buildId;
        // 获取到正确的AndroidId
        if (!ERROR_ANDROID_ID.equals(androidId)) {
            result += androidId;
        }
        // 获取到正确的Mac地址
        if (!NetworkUtils.ERROR_MAC_ADDRESS.equals(macAddress)) {
            result += macAddress;
        }
        // 如果AndroidId和Mac地址都没有正确获取
        if (result.equals(buildId)) {
            result = md5(UUID.randomUUID().toString().replaceAll("-", ""));
            SharedUtils.create(context).put(DEVICE_UNIQUE_ID, result);
            return result;
        }
        return md5(result);
    }

    private static String md5(String text) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] bytes = md.digest(text.getBytes());
            StringBuilder ret = new StringBuilder(bytes.length * 2);
            char [] digits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
            for (byte b : bytes) {
                ret.append(digits[(b >> 4) & 0x0f]);
                ret.append(digits[b & 0x0f]);
            }
            return ret.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }
}
