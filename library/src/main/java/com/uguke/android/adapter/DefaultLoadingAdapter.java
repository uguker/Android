package com.uguke.android.adapter;

import android.view.View;

import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.uguke.android.util.ResUtils;
import com.uguke.android.widget.LoadingView;

/**
 * 默认的加载适配器
 * @author LeiJue
 */
public class DefaultLoadingAdapter implements LoadingAdapter {

    LoadingView mView;
    CoordinatorLayout mParent;

    @Override
    public void convert(Object obj, CoordinatorLayout parent, LoadingView view) {
        mParent = parent;
        mView = view;

        mView.getLayoutParams().width = ResUtils.toPixel(50);
        mView.getLayoutParams().height = ResUtils.toPixel(50);
    }

    @Override
    public void show(String... texts) {
        mParent.setVisibility(View.VISIBLE);
        mView.setVisibility(View.VISIBLE);
        mView.start();
    }

    @Override
    public void hide() {
        if (mParent != null) {
            mParent.setVisibility(View.GONE);
            mView.setVisibility(View.GONE);
            mView.stop();
        }
    }

    @Override
    public boolean isUseTexts() {
        return false;
    }
}
