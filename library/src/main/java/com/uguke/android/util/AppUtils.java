package com.uguke.android.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * App信息工具类
 * @author LeiJue
 */
public class AppUtils {

    private AppUtils() {
        throw new UnsupportedOperationException("can't instantiate me.");
    }

    @Nullable
    public static PackageInfo getPackageInfo(@NonNull Context context) {
        return getPackageInfo(context, context.getPackageName());
    }

    @Nullable
    public static PackageInfo getPackageInfo(@NonNull Context context, String packageName) {
        PackageManager manager = context.getPackageManager();
        try {
            return manager.getPackageInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean isSystemApp(@NonNull Context context) {
        return isSystemApp(context, context.getPackageName());
    }

    public static boolean isSystemApp(@NonNull Context context, String packageName) {
        PackageInfo pi = getPackageInfo(context, packageName);
        return pi != null && (pi.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0;
    }

    public static boolean isDebugApp(@NonNull Context context) {
        return isSystemApp(context, context.getPackageName());
    }

    public static boolean isDebugApp(@NonNull Context context, String packageName) {
        PackageInfo pi = getPackageInfo(context, packageName);
        return pi != null && (pi.applicationInfo.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
    }

    @Nullable
    public static Drawable getAppIcon(@NonNull Context context) {
        return getAppIcon(context, context.getPackageName());
    }

    @Nullable
    public static Drawable getAppIcon(@NonNull Context context, String packageName) {
        PackageManager pm = context.getPackageManager();
        PackageInfo pi = getPackageInfo(context, packageName);
        return pi == null ? null : pi.applicationInfo.loadIcon(pm);
    }

    @NonNull
    public static String getAppName(@NonNull Context context) {
        return getAppName(context, context.getPackageName());
    }

    @NonNull
    public static String getAppName(@NonNull Context context, String packageName) {
        PackageManager pm = context.getPackageManager();
        PackageInfo pi = getPackageInfo(context, packageName);
        return pi == null ? "" : pi.applicationInfo.loadLabel(pm).toString();
    }

    @NonNull
    public static String getAppPath(@NonNull Context context) {
        return getAppPath(context, context.getPackageName());
    }

    @NonNull
    public static String getAppPath(@NonNull Context context, String packageName) {
        PackageInfo pi = getPackageInfo(context, packageName);
        return pi == null ? "" : pi.applicationInfo.sourceDir;
    }

    @NonNull
    public static String getVersionName(@NonNull Context context) {
        return getVersionName(context, context.getPackageName());
    }

    @NonNull
    public static String getVersionName(@NonNull Context context, String packageName) {
        PackageInfo pi = getPackageInfo(context, packageName);
        return pi == null ? "" : pi.versionName;
    }

    public static int getVersionCode(@NonNull Context context) {
        return getVersionCode(context, context.getPackageName());
    }

    public static int getVersionCode(@NonNull Context context, String packageName) {
        PackageInfo pi = getPackageInfo(context, packageName);
        return pi == null ? -1 : pi.versionCode;
    }

}
