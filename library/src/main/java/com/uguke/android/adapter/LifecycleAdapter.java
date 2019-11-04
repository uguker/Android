package com.uguke.android.adapter;

import android.os.Bundle;
import android.view.View;

import java.io.Serializable;

/**
 * 简单生命周期管理
 * @author LeiJue
 */
public interface LifecycleAdapter extends Serializable {

    /**
     * 创建
     * @param savedInstanceState 保存的实例状态
     */
    void onCreate(Bundle savedInstanceState);

    /**
     * 布局创建
     * @param target 目标实例
     * @param view 布局实例
     */
    void onViewCreated(Object target, View view);

    /**
     * 布局显示
     * @param target 目标实例
     */
    void onViewVisible(Object target);

    /**
     * 布局隐藏
     * @param target 目标实例
     */
    void onViewInvisible(Object target);

    /**
     * 销毁
     */
    void onDestroy();

}
