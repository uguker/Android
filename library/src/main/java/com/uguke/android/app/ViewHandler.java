package com.uguke.android.app;

import android.view.View;
import android.view.ViewGroup;

/**
 * 页面布局统一控件处理（全局设置样式）
 * @author LeiJue
 */
public interface ViewHandler<P extends ViewGroup, V extends View> {
    /**
     * 需要重新布局则在这里设置
     * @param obj Fragment或Activity对象
     * @param parent 父容器
     * @param v 控件
     */
    void onHandle(Object obj, P parent, V v);
}
