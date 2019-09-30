package com.uguke.android.listener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * 消除监听事件
 * @author LeiJue
 */
public interface OnDismissListener<T> {
    /**
     * 消除监听
     * @param target 目标
     * @param obj 附带值
     */
    void onDismiss(@NonNull T target, @Nullable Object obj);
}