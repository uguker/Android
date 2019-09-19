package com.uguke.android.util;

import android.graphics.drawable.Drawable;
import android.text.method.PasswordTransformationMethod;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

/**
 * 文本工具类
 * @author LeiJue
 */
public class TvUtils {

    private TvUtils() {
        throw new UnsupportedOperationException("can't instantiate me.");
    }

    public static void setDrawables(@NonNull TextView tv, Drawable left, Drawable top, Drawable right, Drawable bottom) {

        if (left != null) {
            left.setBounds(0, 0, left.getMinimumWidth(), left.getMinimumHeight());
        }
        if (top != null) {
            top.setBounds(0, 0, top.getMinimumWidth(), top.getMinimumHeight());
        }
        if (right != null) {
            right.setBounds(0, 0, right.getMinimumWidth(), right.getMinimumHeight());
        }
        if (bottom != null) {
            bottom.setBounds(0, 0, bottom.getMinimumWidth(), bottom.getMinimumHeight());
        }
        tv.setCompoundDrawables(left, top, right, bottom);
    }

    public static void setDrawableLeft(@NonNull TextView tv, Drawable drawable) {

        if (drawable != null) {
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        }
        // 设置图标
        tv.setCompoundDrawables(
                drawable,
                tv.getCompoundDrawables()[1],
                tv.getCompoundDrawables()[2],
                tv.getCompoundDrawables()[3]);
    }

    public static void setDrawableTop(@NonNull TextView tv, Drawable drawable) {

        if (drawable != null) {
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        }
        // 设置图标
        tv.setCompoundDrawables(
                tv.getCompoundDrawables()[0],
                drawable,
                tv.getCompoundDrawables()[2],
                tv.getCompoundDrawables()[3]);
    }

    public static void setDrawableRight(@NonNull TextView tv, Drawable drawable) {

        if (drawable != null) {
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        }
        // 设置图标
        tv.setCompoundDrawables(
                tv.getCompoundDrawables()[0],
                tv.getCompoundDrawables()[1],
                drawable,
                tv.getCompoundDrawables()[3]);
    }

    public static void setDrawableBottom(@NonNull TextView tv, Drawable drawable) {
        if (drawable != null) {
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        }
        // 设置图标
        tv.setCompoundDrawables(
                tv.getCompoundDrawables()[0],
                tv.getCompoundDrawables()[1],
                tv.getCompoundDrawables()[2],
                drawable);
    }

    public static void setPasswordVisible(@NonNull EditText et, boolean visible) {
        int selection = et.getSelectionEnd();
        et.setTransformationMethod(visible ? null :
                PasswordTransformationMethod.getInstance());
        et.setSelection(selection);
    }
}
