package com.uguke.android.app;

import android.view.View;

/**
 * 布局创建完成监听
 * @author LeiJue
 */
public interface ViewCreatedCallback {

    /**
     * 控件创建完毕
     * @param view 根控件
     */
    void onViewCreated(View view);
}
