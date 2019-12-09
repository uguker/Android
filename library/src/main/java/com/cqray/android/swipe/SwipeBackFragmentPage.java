package com.cqray.android.swipe;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.FloatRange;

import com.cqray.android.app.SupportFragment;

/**
 * 回退页
 * @author LeiJue
 */
class SwipeBackFragmentPage implements SwipeBack<SwipeBackFragmentPage> {

    SupportFragment mFragment;
    SwipeBackLayout mSwipeLayout;

    public SwipeBackFragmentPage(SupportFragment fragment) {
        mFragment = fragment;

    }

    void onCreate() {
        if (mSwipeLayout == null) {
            mSwipeLayout = new SwipeBackLayout(mFragment.getContext());
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(-1, -1);
            mSwipeLayout.setLayoutParams(params);
            mSwipeLayout.setBackgroundColor(Color.TRANSPARENT);
            mSwipeLayout.setSwipeEdgePercent(0.1f);
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

    void onDestroyView() {
        if (mFragment != null) {
            mSwipeLayout.internalCallOnDestroyView();
        }
        mFragment = null;
    }

    @Override
    public SwipeBackLayout getSwipeBackLayout() {
        return mSwipeLayout;
    }

    @Override
    public SwipeBackFragmentPage setSwipeBackEnable(boolean enable) {
        if (mSwipeLayout != null) {
            mSwipeLayout.setSwipeBackEnable(enable);
        }
        return this;
    }

    @Override
    public SwipeBackFragmentPage setScrimAlpha(@FloatRange(from = 0.0f, to = 1.0f) float alpha) {
        if (mSwipeLayout != null) {
            mSwipeLayout.setScrimAlpha(alpha);
        }
        return this;
    }

    @Override
    public SwipeBackFragmentPage setEdgeOrientation(@SwipeBackLayout.EdgeOrientation int orientation) {
        if (mSwipeLayout != null) {
            mSwipeLayout.setEdgeOrientation(orientation);
        }
        return this;
    }

    @Override
    public SwipeBackFragmentPage setSwipeEdge(int widthPixel) {
        if (mSwipeLayout != null) {
            mSwipeLayout.setSwipeEdge(widthPixel);
        }
        return this;
    }

    @Override
    public SwipeBackFragmentPage setSwipeEdgePercent(@FloatRange(from = 0.0f, to = 1.0f) float percent) {
        if (mSwipeLayout != null) {
            mSwipeLayout.setSwipeEdgePercent(percent);
        }
        return this;
    }

    @Override
    public SwipeBackFragmentPage setShadow(Drawable shadow, @SwipeBackLayout.EdgeOrientation int orientation) {
        if (mSwipeLayout != null) {
            mSwipeLayout.setShadow(shadow, orientation);
        }
        return this;
    }

    @Override
    public SwipeBackFragmentPage setShadow(int resId, @SwipeBackLayout.EdgeOrientation int orientation) {
        if (mSwipeLayout != null) {
            mSwipeLayout.setShadow(resId, orientation);
        }
        return this;
    }

    @Override
    public SwipeBackFragmentPage setOffsetPercent(float percent) {
        if (mSwipeLayout != null) {
            mSwipeLayout.setOffsetPercent(percent);
        }
        return this;
    }

    @Override
    public SwipeBackFragmentPage setClosePercent(float percent) {
        if (mSwipeLayout != null) {
            mSwipeLayout.setClosePercent(percent);
        }
        return this;
    }

    @Override
    public SwipeBackFragmentPage setScrimColor(int color) {
        if (mSwipeLayout != null) {
            mSwipeLayout.setScrimColor(color);
        }
        return this;
    }

    @Override
    public SwipeBackFragmentPage addOnSwipeListener(SwipeBackLayout.OnSwipeListener listener) {
        if (mSwipeLayout != null) {
            mSwipeLayout.addOnSwipeListener(listener);
        }
        return this;
    }
}
