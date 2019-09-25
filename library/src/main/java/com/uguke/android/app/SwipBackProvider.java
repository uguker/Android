package com.uguke.android.app;

import com.uguke.android.widget.SwipeBackLayout;

public interface SwipBackProvider {

    SwipeBackLayout getSwipeBackLayout();

    void setSwipeBackEnable(boolean enable);

    void setEdgeLevel(SwipeBackLayout.EdgeLevel edgeLevel);

    void setEdgeLevel(int widthPixel);

    boolean onSwipeBackPriority();

    boolean onSwipeBackSupport();
}
