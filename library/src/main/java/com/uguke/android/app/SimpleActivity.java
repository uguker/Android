package com.uguke.android.app;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;

/**
 * 简单Activity
 * @author LeiJue
 */
public class SimpleActivity extends SupportActivity implements LoadingProvider {

//    @Override
//    public final void setContentView(@LayoutRes int id) {
//        mLayoutDelegate.setSimpleContentView(id);
//    }
//
//    @Override
//    public void setContentView(View view) {
//        mLayoutDelegate.setSimpleContentView(view);
//    }
//
//    @Override
//    public void setContentView(View view, ViewGroup.LayoutParams params) {
//        view.setLayoutParams(params);
//        mLayoutDelegate.setSimpleContentView(view);
//    }

    @Override
    public void showContent() {
        mLayoutDelegate.showContent();
    }

    @Override
    public void showEmpty() {
        mLayoutDelegate.showEmpty();
    }

    @Override
    public void showEmpty(String text) {
        mLayoutDelegate.showEmpty(text);
    }

    @Override
    public void showError() {
        mLayoutDelegate.showError();
    }

    @Override
    public void showError(String text) {
        mLayoutDelegate.showError(text);
    }

    @Override
    public void showLoading() {
        mLayoutDelegate.showLoading();
    }

    @Override
    public void showLoading(String text) {
        mLayoutDelegate.showLoading(text);
    }
}
