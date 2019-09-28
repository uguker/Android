package com.uguke.android.listener;

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
    void onDismiss(T target, Object obj);
}
