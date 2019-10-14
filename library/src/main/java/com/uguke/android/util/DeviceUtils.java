package com.uguke.android.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import java.io.File;

import static android.os.Build.VERSION.SDK_INT;
import static android.os.Build.VERSION_CODES.LOLLIPOP;

/**
 * 设备工具类
 * @author LeiJue
 */
public final class DeviceUtils {

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

    @SuppressLint("HardwareIds")
    public static String getAndroidId(Context context) {
        return Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
    }

    public static String getBuildId() {
        return EncryptUtils.md5(Build.MANUFACTURER + Build.BRAND + Build.MODEL + (SDK_INT >= LOLLIPOP ?
                Build.SUPPORTED_ABIS[0] : Build.CPU_ABI) + Build.DEVICE + Build.VERSION.INCREMENTAL);
    }
}
