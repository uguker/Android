package com.uguke.android.app;

import android.view.ViewGroup;

import androidx.annotation.LayoutRes;

/**
 * 主要用于创建布局Header以及Footer
 * @author LeiJue
 */
public class LayoutCreator {

    /** 布局资源ID **/
    private int mLayoutResId;
    /** 额外携带参数 **/
    private Object mExtras;
    /** 容器 **/
    private ViewGroup mContainer;

    private LayoutCreator(int layoutResId, ViewGroup container, Object extras) {
        mLayoutResId = layoutResId;
        mContainer = container;
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

    public static LayoutCreator create(@LayoutRes int layoutResId) {
        return new LayoutCreator(layoutResId, null, null);
    }

    public static LayoutCreator create(@LayoutRes int layoutResId, ViewGroup container) {
        return new LayoutCreator(layoutResId, container, null);
    }

    public static LayoutCreator create(@LayoutRes int layoutResId, Object extras) {
        return new LayoutCreator(layoutResId, null, extras);
    }

    public static LayoutCreator create(@LayoutRes int layoutResId, ViewGroup container, Object extras) {
        return new LayoutCreator(layoutResId, container, extras);
    }

}
