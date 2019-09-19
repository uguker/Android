package com.uguke.android.util;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.ArrayRes;
import androidx.annotation.AttrRes;
import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.DimenRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

/**
 * 资源管理工具类
 * @author LeiJue
 */
public class ResUtils {

    private ResUtils() {
        throw new UnsupportedOperationException("can't instantiate me.");
    }

    public static View inflate(ViewGroup container, @LayoutRes int resource) {
        return LayoutInflater.from(container.getContext()).inflate(resource, container, false);
    }

    public static View inflate(Context context, @LayoutRes int resource) {
        return LayoutInflater.from(context).inflate(resource, null);
    }

    public static View inflate(LayoutInflater inflater, @LayoutRes int resource) {
        return inflater.inflate(resource, null);
    }

    @NonNull
    public static int [] getIdArray(@NonNull Context context, @ArrayRes int id) {
        TypedArray ar = context.getResources().obtainTypedArray(id);
        int len = ar.length();
        int[] resIds = new int[len];
        for (int i = 0; i < len; i++) {
            resIds[i] = ar.getResourceId(i, 0);
        }
        ar.recycle();
        return resIds;
    }

    @ColorInt
    public static int getAttrColor(@NonNull Context context, @AttrRes int id) {
        TypedArray array = context.obtainStyledAttributes(new int[] { id });
        int color = array.getColor(0, Color.WHITE);
        array.recycle();
        return color;
    }

    public static Drawable getDrawableByAttr(@NonNull Context context, @AttrRes int id) {
        TypedArray array = context.obtainStyledAttributes(new int[] { id });
        Drawable drawable = array.getDrawable(0);
        array.recycle();
        return drawable;
    }

    public static int getColor(Context context, @ColorRes int id) {
        return ContextCompat.getColor(context, id);
    }

    public static int getAttrPixel(@NonNull Context context, @AttrRes int id) {
        TypedArray array = context.obtainStyledAttributes(new int[] { id });
        int pixel = (int) array.getDimension(0, 0);
        array.recycle();
        return pixel;
    }

    public static float getAttrDip(@NonNull Context context, @AttrRes int id) {
        TypedArray array = context.obtainStyledAttributes(new int[] { id });
        float density = Resources.getSystem().getDisplayMetrics().density;
        int pixel = (int) array.getDimension(0, 0);
        array.recycle();
        return pixel / density;
    }

    public static int getPixel(@NonNull Context context, @DimenRes int id) {
        return context.getResources().getDimensionPixelSize(id);
    }

    public static float getDip(@NonNull Context context, @DimenRes int id) {
        float density = Resources.getSystem().getDisplayMetrics().density;
        return context.getResources().getDimensionPixelSize(id) / density;
    }

    public static int toPixel(float dip) {
        float density = Resources.getSystem().getDisplayMetrics().density;
        return (int) Math.ceil(dip * density);
    }

    public static float toDip(float pixel) {
        float density = Resources.getSystem().getDisplayMetrics().density;
        return pixel / density;
    }
}
