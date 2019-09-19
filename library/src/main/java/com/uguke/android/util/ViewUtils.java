package com.uguke.android.util;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

/**
 * 控件工具类
 * @author LeiJue
 */
public class ViewUtils {

    private static long sLastTime = 0;

    private ViewUtils() {
        throw new UnsupportedOperationException("can't instantiate me.");
    }

    /**
     * 设置控件宽度（单位px）
     * @param view  控件
     * @param width 宽度
     */
    public static void setWidth(View view, int width) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.width = width;
    }

    /**
     * 设置控件宽度（单位dp）
     * @param view  控件
     * @param width 宽度
     */
    public static void setWidth(View view, float width) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.width = ResUtils.toPixel(width);
    }

    /**
     * 设置控件高度（单位px）
     * @param view   控件
     * @param height 高度
     */
    public static void setHeight(View view, int height) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.height = height;
    }

    /**
     * 设置控件高度（单位dp）
     * @param view   控件
     * @param height 高度
     */
    public static void setHeight(View view, float height) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.height = ResUtils.toPixel(height);
    }

    public static void setSize(View view, int width, int height) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.width = width;
        params.height = height;
    }

    public static void setSize(View view, float width, float height) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.width = ResUtils.toPixel(width);
        params.height = ResUtils.toPixel(height);
    }

    /**
     * 设置控件外边距（单位px）
     * @param view   控件
     * @param left   左边距
     * @param top    上边距
     * @param right  右边距
     * @param bottom 下边距
     */
    public static void setMargins(View view, int left, int top, int right, int bottom) {
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        params.setMargins(left, top, right, bottom);
    }

    /**
     * 设置控件外边距（单位dp）
     * @param view   控件
     * @param left   左边距
     * @param top    上边距
     * @param right  右边距
     * @param bottom 下边距
     */
    public static void setMargins(View view, float left, float top, float right, float bottom) {
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        params.leftMargin = ResUtils.toPixel(left);
        params.topMargin = ResUtils.toPixel(top);
        params.rightMargin = ResUtils.toPixel(right);
        params.bottomMargin = ResUtils.toPixel(bottom);
    }

    /**
     * 设置文本格式为粗体
     * @param tv    文本控件
     * @param bold  粗体
     */
    public static void setTextBold(TextView tv, boolean bold) {
        tv.setTypeface(Typeface.defaultFromStyle(bold ? Typeface.BOLD : Typeface.NORMAL));
    }

    public static void setVisible(View view, boolean visible) {
        if (view == null) {
            return;
        }
        view.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
    }

    public static void setGone(View view, boolean gone) {
        if (view == null) {
            return;
        }
        view.setVisibility(gone ? View.GONE : View.VISIBLE);
    }

    public static Activity getActivity(@NonNull View view) {
        Context context = view.getContext();
        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return (Activity) context;
            }
        }
        throw new ClassCastException("View.getContext() can't cast be Activity");
    }

    public static void bringToFont(View view) {
        if (view != null) {
            view.bringToFront();
        }
    }
}
