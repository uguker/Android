package com.uguke.android.app.delegate;

import android.view.View;

import androidx.annotation.NonNull;

/**
 * 控件初始化回调
 * @author LeiJue
 */
public interface ViewCallback {

    /**
     * 控件初始化完成
     * @param view 布局根控件
     */
    void onViewCreated(@NonNull View view);
}
