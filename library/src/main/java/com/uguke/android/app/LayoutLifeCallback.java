package com.uguke.android.app;

import android.view.View;

/**
 * 布局生命周期监听
 * @author LeiJue
 */
public interface LayoutLifeCallback {

    /**
     * 控件创建完毕
     * @param view 根控件
     */
    void onViewCreated(View view);
}
