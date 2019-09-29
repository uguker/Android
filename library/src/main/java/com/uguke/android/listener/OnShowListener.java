package com.uguke.android.listener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * 显示监听
 * @author LeiJue
 */
public interface OnShowListener<T> {
    /**
     * 显示监听
     * @param target 目标
     * @param obj 附带值
     */
    void onShow(@NonNull T target, @Nullable Object obj);
}
