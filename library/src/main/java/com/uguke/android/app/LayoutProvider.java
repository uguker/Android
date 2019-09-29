package com.uguke.android.app;

import android.view.View;

import androidx.annotation.LayoutRes;

public interface LayoutProvider {
    /**
     * 设置界面
     * @param id
     */
    void setContentView(@LayoutRes int id);
    void setContentView(View view);
    void setSimpleContentView(@LayoutRes int id);
    void setSimpleContentView(View view);
    void setNativeContentView(@LayoutRes int id);
    void setNativeContentView(View view);
    void showTips(String tips);
    void showLoading(String ...texts);
    void hideLoading();
}
