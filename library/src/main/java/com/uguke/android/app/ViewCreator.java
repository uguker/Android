package com.uguke.android.app;

import android.view.ViewGroup;

import androidx.annotation.LayoutRes;

/**
 * 主要用于创建布局Header以及Footer
 * @author LeiJue
 */
public class ViewCreator {

    /** 布局资源ID **/
    private int mLayoutResId;

    private boolean mFloating;
    /** 额外携带参数 **/
    private Object mExtras;
    /** 容器 **/
    private ViewGroup mContainer;

    private ViewCreator(int layoutResId, ViewGroup container, boolean floating, Object extras) {
        mLayoutResId = layoutResId;
        mContainer = container;
        mFloating = floating;
        mExtras = extras;
    }

    public int getLayoutResId() {
        return mLayoutResId;
    }

    public ViewGroup getContainer() {
        return mContainer;
    }

    public Object getExtras() {
        return mExtras;
    }

    public boolean isFloating() {
        return mFloating;
    }

    public static ViewCreator create(@LayoutRes int layoutResId) {
        return new ViewCreator(layoutResId, null, false, null);
    }

    public static ViewCreator create(@LayoutRes int layoutResId, boolean floating) {
        return new ViewCreator(layoutResId, null, floating, null);
    }

    public static ViewCreator create(@LayoutRes int layoutResId, ViewGroup container) {
        return new ViewCreator(layoutResId, container, false, null);
    }

    public static ViewCreator create(@LayoutRes int layoutResId, ViewGroup container, boolean floating) {
        return new ViewCreator(layoutResId, container, floating, null);
    }

    public static ViewCreator create(@LayoutRes int layoutResId, ViewGroup container, boolean floating, Object extras) {
        return new ViewCreator(layoutResId, container, floating, extras);
    }

}
