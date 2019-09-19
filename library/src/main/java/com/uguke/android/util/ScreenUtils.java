package com.uguke.android.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 屏幕相关工具
 * @author LeiJue
 */
public final class ScreenUtils {

    private static final String TAG_COLOR = "TAG_COLOR";
    /**
     * 隐藏构造函数
     */
    private ScreenUtils() {
        throw new UnsupportedOperationException("can't instantiate me.");
    }

    public static void setFull(Activity activity) {
        if (activity == null) {
            return;
        }
        //设置全屏的Flags
        int flags;
        int curApiVersion = Build.VERSION.SDK_INT;
        // Android 4.4+ 生效
        if(curApiVersion >= Build.VERSION_CODES.KITKAT){
            //隐藏导航栏永久在Android的活动
            //触摸屏，导航栏不显示
            flags = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
        } else {
            flags = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        }
        activity.getWindow().getDecorView().setSystemUiVisibility(flags);
    }

    public static int getRealHeight(Context context) {
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (manager == null) {
            return 0;
        }
        Display display = manager.getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        try {
            Class<?> cla = Class.forName("android.view.Display");
            Method method = cla.getMethod("getRealMetrics", DisplayMetrics.class);
            method.invoke(display, displayMetrics);
            return displayMetrics.heightPixels;
        } catch(Exception e){
            e.printStackTrace();
        }
        return 0;
    }

    public static int getRealWidth(Context context) {
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (manager == null) {
            return 0;
        }
        Display display = manager.getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        try {
            Class<?> cla = Class.forName("android.view.Display");
            Method method = cla.getMethod("getRealMetrics", DisplayMetrics.class);
            method.invoke(display, displayMetrics);
            return displayMetrics.widthPixels;
        } catch(Exception e){
            e.printStackTrace();
        }
        return 0;
    }

    public static int getWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static int getHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    public static float getDensity() {
        return Resources.getSystem().getDisplayMetrics().density;
    }

    
    public static int getStatusBarHeight() {
        try {
            @SuppressLint("PrivateApi")
            Class<?> c = Class.forName("com.android.internal.R$dimen");
            Object obj = c.newInstance();
            Field field = c.getField("status_bar_height");
            int id = Integer.parseInt(field.get(obj).toString());
            return Resources.getSystem().getDimensionPixelSize(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }


    public static int getActionbarSize(Context context) {
        TypedValue tv = new TypedValue();
        if (context.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            return TypedValue.complexToDimensionPixelSize(tv.data, Resources.getSystem().getDisplayMetrics());
        }
        return 0;
    }


    public static int getNavigationBarSize(Context context) {
        if (isPortrait()) {
            return getRealHeight(context) - getHeight();
        }
        return getRealWidth(context) - getWidth();
    }
    
    /**
     * 设置屏幕为横屏
     * <p>还有一种就是在Activity中加属性android:screenOrientation="landscape"</p>
     * <p>不设置Activity的android:configChanges时，切屏会重新调用各个生命周期，切横屏时会执行一次，切竖屏时会执行两次</p>
     * <p>设置Activity的android:configChanges="orientation"时，切屏还是会重新调用各个生命周期，切横、竖屏时只会执行一次</p>
     * <p>设置Activity的android:configChanges="orientation|keyboardHidden|screenSize"（4.0以上必须带最后一个参数）时</p>
     * <p>切屏不会重新调用各个生命周期，只会执行onConfigurationChanged方法</p>
     * @param activity 活动
     */
    public static void setLandscape(Activity activity) {
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    /**
     * 设置屏幕为竖屏
     * <p>还有一种就是在Activity中加属性android:screenOrientation="landscape"</p>
     * <p>不设置Activity的android:configChanges时，切屏会重新调用各个生命周期，切横屏时会执行一次，切竖屏时会执行两次</p>
     * <p>设置Activity的android:configChanges="orientation"时，切屏还是会重新调用各个生命周期，切横、竖屏时只会执行一次</p>
     * <p>设置Activity的android:configChanges="orientation|keyboardHidden|screenSize"（4.0以上必须带最后一个参数）时</p>
     * <p>切屏不会重新调用各个生命周期，只会执行onConfigurationChanged方法</p>
     * @param activity 活动
     */
    public static void setPortrait(Activity activity) {
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    /**
     * 获取屏幕旋转角度
     * @param activity 活动
     * @return 角度
     */
    public static int getScreenRotation(Activity activity) {
        switch (activity.getWindowManager().getDefaultDisplay().getRotation()) {
            case Surface.ROTATION_0:
                return 0;
            case Surface.ROTATION_90:
                return 90;
            case Surface.ROTATION_180:
                return 180;
            case Surface.ROTATION_270:
                return 270;
            default:
                return 0;
        }
    }

    public static boolean isLandscape() {
        return Resources.getSystem().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE;
    }

    public static boolean isPortrait() {
        return Resources.getSystem().getConfiguration().orientation
                == Configuration.ORIENTATION_PORTRAIT;
    }


    /**
     * 判断是否锁屏
     * @return 是否锁屏
     */
    public static boolean isLock(Context context) {
        KeyguardManager km = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        return km != null && km.inKeyguardRestrictedInputMode();
    }
}
