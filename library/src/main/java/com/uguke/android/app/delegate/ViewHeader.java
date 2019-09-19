package com.uguke.android.app.delegate;

import androidx.annotation.LayoutRes;

public class ViewHeader {
    private int mLayoutResId;
    private boolean mFloating;

    private ViewHeader(int layoutResId, boolean floating) {
        mLayoutResId = layoutResId;
        mFloating = floating;
    }

    public int getLayoutResId() {
        return mLayoutResId;
    }

    public boolean isFloating() {
        return mFloating;
    }

    public static ViewHeader create(@LayoutRes int layoutResId) {
        return new ViewHeader(layoutResId, false);
    }

    public static ViewHeader create(@LayoutRes int layoutResId, boolean floating) {
        return new ViewHeader(layoutResId, floating);
    }
}
