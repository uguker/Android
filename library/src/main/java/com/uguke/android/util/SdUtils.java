package com.uguke.android.util;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.os.storage.StorageManager;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Sd卡工具类
 * @author LeiJue
 */
public class SdUtils {

    private SdUtils() {
        throw new UnsupportedOperationException("can't instantiate me.");
    }

    /**
     * Sd卡是否可用
     */
    public static boolean isAvailable() {
        return TextUtils.equals(Environment.MEDIA_MOUNTED, Environment.getExternalStorageState());
    }

    /**
     * 获取当前App的data路径
     */
    @NonNull
    public static String getDataPath(Context context) {
        return getDataPath(context.getPackageName());
    }

    /**
     * 获取指定App包名的data路径
     * @param packageName 指定包名
     */
    @NonNull
    public static String getDataPath(String packageName) {
        if (TextUtils.isEmpty(packageName) || !isAvailable()) {
            return "";
        }
        return getPath() + "/Android/data/" + packageName;
    }

    /**
     * 获取SD卡路径
     */
    @NonNull
    public static String getPath() {
        if (!isAvailable()) {
            return "";
        }
        // 命令行
        String cmd = "cat /proc/mounts";
        Runtime run = Runtime.getRuntime();
        BufferedReader reader = null;
        try {
            Process p = run.exec(cmd);
            reader = new BufferedReader(new InputStreamReader(
                    new BufferedInputStream(p.getInputStream())));
            String lineStr;
            while ((lineStr = reader.readLine()) != null) {
                if (lineStr.contains("sdcard") && lineStr.contains(".android_secure")) {
                    String[] strArray = lineStr.split(" ");
                    if (strArray.length >= 5) {
                        return strArray[1].replace("/.android_secure", "");
                    }
                }
                if (p.waitFor() != 0 && p.exitValue() == 1) {
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return Environment.getExternalStorageDirectory().getPath();
    }

    /**
     * 获取外部存储路径列表
     */
    public static List<String> getExtPath(Context context) {
        List<String> pathList = new ArrayList<>();
        StorageManager storageManager = (StorageManager)context
                .getSystemService(Context.STORAGE_SERVICE);
        try {
            Class<?>[] paramClasses = {};
            Method getVolumePathsMethod = StorageManager.class.getMethod("getVolumePaths", paramClasses);
            getVolumePathsMethod.setAccessible(true);
            Object[] params = {};
            String [] paths = (String[]) getVolumePathsMethod.invoke(storageManager, params);
            if (paths != null) {
                String sdcard = getPath();
                for (String path : paths) {
                    if (!(path).equals(sdcard)) {
                        pathList.add(path);
                    }
                }
            }
        } catch (NoSuchMethodException e1) {
            e1.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return pathList;
    }

    /**
     * 获取存储器剩余容量，单位byte
     * @param path 存储器根路径
     * @return 存储器剩余容量
     */
    public static long getFreeBytes(String path) {
        if (TextUtils.isEmpty(path)) {
            return 0;
        }
        File dir = new File(path);
        //如果是文件夹
        if (dir.exists() && dir.isDirectory()) {
            StatFs stat = new StatFs(path);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                return stat.getFreeBytes();
            } else {
                return stat.getAvailableBlocksLong() * stat.getBlockSizeLong();
            }
        }
        return 0;
    }

    /**
     * 获取存储器全部容量，单位byte
     * @param path 存储器根路径
     * @return 存储器容量
     */
    public static long getTotalBytes(String path) {
        if (TextUtils.isEmpty(path)) {
            return 0;
        }
        File dir = new File(path);
        //如果是文件夹
        if (dir.exists() && dir.isDirectory()) {
            StatFs stat = new StatFs(path);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                return stat.getTotalBytes();
            } else {
                return stat.getBlockCountLong() * stat.getBlockSizeLong();
            }
        }
        return 0;
    }

    /**
     * 获取SD卡的剩余容量，单位byte
     * @return SD卡剩余容量
     */
    public static long getSdcardFreeSize() {
        return getFreeBytes(getPath());
    }

    /**
     * 获取SD卡的全部容量，单位byte
     * @return SD卡部容量
     */
    public static long getSdcardTotalSize() {
        return getTotalBytes(getPath());
    }

    public static long getTotalSize() {
        return getTotalBytes(getPath());
    }
}
