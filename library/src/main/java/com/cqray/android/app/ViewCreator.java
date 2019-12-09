package com.cqray.android.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;

/**
 * 主要用于创建布局Header以及Footer
 * @author LeiJue
 */
public class ViewCreator {

    /** 布局资源ID **/
    private int mLayoutResId;
    /** 是否浮动 **/
    private boolean mFloating;
    /** 额外携带参数 **/
    private Object mExtras;
    /** 内容布局 **/
    private View mView;
    /** 容器 **/
    private ViewGroup mContainer;

    private ViewCreator(View view, boolean floating, Object extras) {
        mFloating = floating;
        mExtras = extras;
        mView = view;
    }

    private ViewCreator(int layoutResId, ViewGroup container, boolean floating, Object extras) {
        mLayoutResId = layoutResId;
        mContainer = container;
        mFloating = floating;
        mExtras = extras;
        mView = LayoutInflater.from(container.getContext()).inflate(layoutResId, container, false);
    }

    private ViewCreator(int layoutResId, Context context, boolean floating, Object extras) {
        mLayoutResId = layoutResId;
        mFloating = floating;
        mExtras = extras;
        mView = LayoutInflater.from(context).inflate(layoutResId, null, false);
    }

    public View getView() {
        return mView;
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

    @SuppressWarnings("unchecked")
    public <T> T findViewById(@IdRes int id) {
        if (mView == null) {
            return null;
        }
        return (T) mView.findViewById(id);
    }

    public static ViewCreator create(View view) {
        return new ViewCreator(view, false, null);
    }

    public static ViewCreator create(View view, boolean floating) {
        return new ViewCreator(view, floating, null);
    }

    public static ViewCreator create(View view, boolean floating, Object extras) {
        return new ViewCreator(view, floating, extras);
    }

    public static ViewCreator create(@NonNull Context context, @LayoutRes int layoutResId) {
        return new ViewCreator(layoutResId, context, false, null);
    }

    public static ViewCreator create(@NonNull Context context, @LayoutRes int layoutResId, boolean floating) {
        return new ViewCreator(layoutResId, context, floating, null);
    }

    public static ViewCreator create(@NonNull Context context, @LayoutRes int layoutResId, boolean floating, Object extras) {
        return new ViewCreator(layoutResId, context, floating, extras);
    }

    public static ViewCreator create(@LayoutRes int layoutResId, @NonNull ViewGroup container) {
        return new ViewCreator(layoutResId, container, false, null);
    }

    public static ViewCreator create(@LayoutRes int layoutResId, @NonNull ViewGroup container, boolean floating) {
        return new ViewCreator(layoutResId, container, floating, null);
    }

    public static ViewCreator create(@LayoutRes int layoutResId, @NonNull ViewGroup container, boolean floating, Object extras) {
        return new ViewCreator(layoutResId, container, floating, extras);
    }
}
