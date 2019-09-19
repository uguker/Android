package com.uguke.android.adapter;

import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.uguke.android.widget.LoadingView;

/**
 * 加载界面适配器
 * @author LeiJue
 */
public interface LoadingAdapter {

    /**
     * 需要重新布局则在这里设置
     * @param target Fragment或Activity对象
     * @param parent 父容器
     * @param view 加载控件
     */
    void convert(Object target, CoordinatorLayout parent, LoadingView view);

    /**
     * 显示加载页面
     * @param texts 文本信息集合
     */
    void show(String ...texts);

    /**
     * 隐藏加载页面
     */
    void hide();

    /**
     * 是否使用文本信息
     * @return true 使用文本信息 false不使用
     */
    boolean isUseTexts();
}
