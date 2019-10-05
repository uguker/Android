package com.uguke.android.swipe;

import android.graphics.drawable.Drawable;

/**
 * SwipeBack方法
 * @author LeiJue
 */
public interface SwipeBack<T extends SwipeBack> {

    SwipeBackLayout getSwipeBackLayout();

    T setSwipeBackEnable(boolean enable);

    T setSwipeAlpha(float alpha);

    T  setEdgeOrientation(@SwipeBackLayout.EdgeOrientation int orientation);

    T setEdgeLevel(SwipeBackLayout.EdgeLevel edgeLevel);

    T setEdgeLevel(int widthPixel);

    T setShadow(Drawable shadow, @SwipeBackLayout.EdgeOrientation int orientation);

    T setShadow(int resId, @SwipeBackLayout.EdgeOrientation int orientation);

    T setOffsetPercent(float percent);

    T setClosePercent(float percent);

    T setScrimColor(int color);

    T addOnSwipeListener(SwipeBackLayout.OnSwipeListener listener);
}
