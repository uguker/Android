package com.uguke.android.adapter;

import android.content.Context;

/**
 * Toast弹窗适配
 * @author LeiJue
 */
public interface ToastAdapter {

    /**
     * 显示Toast
     * @param context  上下文
     * @param text     文本
     * @param duration 时长
     */
    void show(Context context, String text, int duration);
}
