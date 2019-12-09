package com.cqray.android.swipe;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.FloatRange;

/**
 * 回退页
 * @author LeiJue
 */
class SwipeBackActivityPage implements SwipeBack<SwipeBackActivityPage> {

    Activity mActivity;
    SwipeBackLayout mSwipeLayout;

    public SwipeBackActivityPage(Activity activity) {
        mActivity = activity;
    }

    void onCreate() {
        if (mSwipeLayout == null) {
            Window window = mActivity.getWindow();
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            window.getDecorView().setBackgroundColor(Color.TRANSPARENT);
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            mSwipeLayout = new SwipeBackLayout(mActivity);
            mSwipeLayout.setLayoutParams(params);
            mSwipeLayout.setSwipeEdgePercent(0.1f);
        }
    }

    void attachToActivity() {
        if (mSwipeLayout != null) {
            mSwipeLayout.attachToActivity(mActivity);
        }
    }

    void onDestroy() {
        mActivity = null;
    }

    @Override
    public SwipeBackLayout getSwipeBackLayout() {
        return mSwipeLayout;
    }

    @Override
    public SwipeBackActivityPage setSwipeBackEnable(boolean enable) {
        if (mSwipeLayout != null) {
            mSwipeLayout.setSwipeBackEnable(enable);
        }
        return this;
    }

    @Override
    public SwipeBackActivityPage setScrimAlpha(@FloatRange(from = 0.0f, to = 1.0f) float alpha) {
        if (mSwipeLayout != null) {
            mSwipeLayout.setScrimAlpha(alpha);
        }
        return this;
    }

    @Override
    public SwipeBackActivityPage setEdgeOrientation(@SwipeBackLayout.EdgeOrientation int orientation) {
        if (mSwipeLayout != null) {
            mSwipeLayout.setEdgeOrientation(orientation);
        }
        return this;
    }

    @Override
    public SwipeBackActivityPage setSwipeEdge(int widthPixel) {
        if (mSwipeLayout != null) {
            mSwipeLayout.setSwipeEdge(widthPixel);
        }
        return this;
    }

    @Override
    public SwipeBackActivityPage setSwipeEdgePercent(@FloatRange(from = 0.0f, to = 1.0f) float percent) {
        if (mSwipeLayout != null) {
            mSwipeLayout.setSwipeEdgePercent(percent);
        }
        return this;
    }

    @Override
    public SwipeBackActivityPage setShadow(Drawable shadow, @SwipeBackLayout.EdgeOrientation int orientation) {
        if (mSwipeLayout != null) {
            mSwipeLayout.setShadow(shadow, orientation);
        }
        return this;
    }

    @Override
    public SwipeBackActivityPage setShadow(int resId, @SwipeBackLayout.EdgeOrientation int orientation) {
        if (mSwipeLayout != null) {
            mSwipeLayout.setShadow(resId, orientation);
        }
        return this;
    }

    @Override
    public SwipeBackActivityPage setOffsetPercent(float percent) {
        if (mSwipeLayout != null) {
            mSwipeLayout.setOffsetPercent(percent);
        }
        return this;
    }

    @Override
    public SwipeBackActivityPage setClosePercent(float percent) {
        if (mSwipeLayout != null) {
            mSwipeLayout.setClosePercent(percent);
        }
        return this;
    }

    @Override
    public SwipeBackActivityPage setScrimColor(int color) {
        if (mSwipeLayout != null) {
            mSwipeLayout.setScrimColor(color);
        }
        return this;
    }

    @Override
    public SwipeBackActivityPage addOnSwipeListener(SwipeBackLayout.OnSwipeListener listener) {
        if (mSwipeLayout != null) {
            mSwipeLayout.addOnSwipeListener(listener);
        }
        return this;
    }

}
