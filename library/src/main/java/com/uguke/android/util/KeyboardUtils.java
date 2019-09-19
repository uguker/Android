package com.uguke.android.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 键盘工具
 * @author LeiJue
 */
public final class KeyboardUtils {

    private KeyboardUtils() {
        throw new UnsupportedOperationException("can't instantiate me.");
    }

    public static void clearMemory(Context context) {
        if (context == null) {
            return;
        }
        InputMethodManager inputMethodManager = (InputMethodManager)
                context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager == null) {
            return;
        }
        String [] viewArray = new String[]{"mCurRootView", "mServedView", "mNextServedView"};
        Field filed;
        Object filedObject;
        for (String view:viewArray) {
            try{
                filed = inputMethodManager.getClass().getDeclaredField(view);
                if (!filed.isAccessible()) {
                    filed.setAccessible(true);
                }
                filedObject = filed.get(inputMethodManager);
                if (filedObject != null && filedObject instanceof View) {
                    View fileView = (View) filedObject;
                    // 被InputMethodManager持有的引用是需要被销毁的
                    if (fileView.getContext() == context) {
                        // 置空
                        filed.set(inputMethodManager, null);
                    } else {
                        // 不是想要销毁的目标，进入另一层界面了，不要处理，避免影响原逻辑,也就不用继续for循环了。
                        break;
                    }
                }
            } catch(Throwable t) {
                t.printStackTrace();
            }
        }
    }

    /**
     * 动态显示软键盘
     * <p>在manifest.xml中activity中设置</p>
     * <p>android:windowSoftInputMode="adjustPan"</p>
     * @param act 活动
     */
    public static void show(Activity act) {
        View view = act.getCurrentFocus();
        if (view == null) {
            view = new View(act);
        } InputMethodManager imm = (InputMethodManager) act
                .getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
        }
    }

    /**
     * 动态显示软键盘
     * @param view 视图
     */
    public static void show(View view) {
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        InputMethodManager imm = (InputMethodManager) view.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
        }
    }

    /**
     * 动态隐藏软键盘
     * @param act 活动
     */
    public static void hide(Activity act) {
        View view = act.getCurrentFocus();
        if (view == null) {
            view = new View(act);
        }
        InputMethodManager imm = (InputMethodManager) act
                .getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * 动态隐藏软键盘
     * @param view 视图
     */
    public static void hide(View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * 切换键盘显示与否状态
     */
    public static void toggle(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm == null) {
            return;
        }
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    public static boolean isShowing(Activity act) {
        //获取当前屏幕内容的高度
        int screenHeight = act.getWindow().getDecorView().getHeight();
        //获取View可见区域的bottom
        Rect rect = new Rect();
        act.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);

        return screenHeight - rect.bottom - getBarHeight(act) != 0;
    }

    //@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private static int getBarHeight(Activity act) {
        DisplayMetrics metrics = new DisplayMetrics();
        //这个方法获取可能不是真实屏幕的高度
        act.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int usableHeight = metrics.heightPixels;
        int realHeight = getRealHeight(act);
        if (realHeight > usableHeight) {
            return realHeight - usableHeight;
        } else {
            return 0;
        }
    }

    private static int getRealHeight(Activity act) {
        WindowManager manager = (WindowManager) act.getSystemService(Context.WINDOW_SERVICE);
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


}
