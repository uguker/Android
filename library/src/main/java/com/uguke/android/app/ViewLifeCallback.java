package com.uguke.android.app;

import android.view.View;

public interface ViewLifeCallback {

    /**
     * 控件创建
     */
    void onCreate();

    /**
     * 控件创建完毕
     */
    void onViewCreated(View view);

    /**
     * 控件销毁
     */
    void onDestroy();
}
