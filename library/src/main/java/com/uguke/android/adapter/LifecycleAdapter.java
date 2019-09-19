package com.uguke.android.adapter;

import android.os.Bundle;
import android.view.View;

/**
 * 生命周期适配器
 * @author LeiJue
 */
public interface LifecycleAdapter {

    /**
     * 初始化
     * @param savedInstanceState 保存的数据
     */
    void onCreate(Bundle savedInstanceState);

    /**
     * 控件完成创建
     * @param target Fragment或Activity对象
     * @param view 根控件
     */
    void onViewCreated(Object target, View view);

    /**
     * 销毁
     */
    void onDestroy();
}
