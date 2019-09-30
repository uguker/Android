package com.uguke.android.swipe;


public interface SwipeBackProvider {

    SwipeBackLayout getSwipeBackLayout();

    /**
     * 设置侧滑回退是否可用
     * @param enable true 可用 false 不可用
     */
    void setSwipeBackEnable(boolean enable);

    void setEdgeLevel(SwipeBackLayout.EdgeLevel edgeLevel);

    void setEdgeLevel(int widthPixel);

    void setOffsetPercent(float percent);

    //boolean onSwipeBackPriority();

    /**
     * 定义是否支持侧滑回退
     * @return true 支持 false 不支持
     */
    boolean onSwipeBackSupport();
}