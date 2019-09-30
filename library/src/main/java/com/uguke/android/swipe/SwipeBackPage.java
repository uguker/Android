package com.uguke.android.swipe;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.fragment.app.FragmentActivity;

import com.uguke.android.app.SupportFragment;

/**
 * 回退页
 * @author LeiJue
 */
public class SwipeBackPage {

    FragmentActivity mActivity;
    SupportFragment mFragment;
    SwipeBackLayout mSwipeLayout;

    public SwipeBackPage(FragmentActivity activity) {
        mActivity = activity;
    }

    public SwipeBackPage(SupportFragment fragment) {
        mFragment = fragment;
        mActivity = mFragment.mActivity;
    }

    void onCreate() {
        if (mSwipeLayout == null) {
            if (mFragment == null) {
                Window window = mActivity.getWindow();
                window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                window.getDecorView().setBackgroundColor(Color.TRANSPARENT);
                ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
                mSwipeLayout = new SwipeBackLayout(mActivity);
                mSwipeLayout.setLayoutParams(params);
            } else {
                SwipeBackLayout layout = new SwipeBackLayout(mActivity);
                ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(-1, -1);
                layout.setLayoutParams(params);
                layout.setBackgroundColor(Color.TRANSPARENT);
            }
        }
    }

    void attachToActivity() {
        if (mSwipeLayout != null) {
            mSwipeLayout.attachToActivity(mActivity);
        }
    }

    View attachToFragment(View view) {
        if (mSwipeLayout != null) {
            mSwipeLayout.attachToFragment(mFragment, view);
            return mSwipeLayout;
        }
        return null;
    }

    void onViewCreated(View view) {
        if (view instanceof SwipeBackLayout) {
            View childView = ((SwipeBackLayout) view).getChildAt(0);
            mFragment.getSupportDelegate().setBackground(childView);
        } else {
            mFragment.getSupportDelegate().setBackground(view);
        }
    }

    void onHiddenChanged(boolean hidden) {
        if (hidden && mSwipeLayout != null) {
            mSwipeLayout.hiddenFragment();
        }
    }

    void onDestroy() {
        if (mFragment != null) {
            mSwipeLayout.internalCallOnDestroyView();
        }
        mActivity = null;
        mFragment = null;
    }


    public SwipeBackLayout getSwipeBackLayout() {
        return mSwipeLayout;
    }
}
