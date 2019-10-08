package com.uguke.android.swipe;

import android.graphics.drawable.Drawable;

import androidx.annotation.FloatRange;

/**
 * SwipeBack方法
 * @author LeiJue
 */
public interface SwipeBack<T extends SwipeBack> {

    /**
     * 获取控件
     * @return 控件实体
     */
    SwipeBackLayout getSwipeBackLayout();

    /**
     * 设置能否滑动
     * @param enable 能否滑动
     * @return page实体
     */
    T setSwipeBackEnable(boolean enable);

    /**
     * 滑动中，上一个页面View的遮罩透明度
     * @param alpha 遮罩透明度
     * @return page实体
     */
    T setScrimAlpha(@FloatRange(from = 0.0f, to = 1.0f) float alpha);

    /**
     * 滑动中，上一个页面View的遮罩颜色
     * @param color 颜色
     * @return page实体
     */

    T setScrimColor(int color);

    /**
     * 滑动方向
     * @param orientation 滑动方向
     * @return page实体
     */
    T  setEdgeOrientation(@SwipeBackLayout.EdgeOrientation int orientation);

    /**
     * 可以启动滑动的范围
     * @param widthPixel 范围值
     * @return page实体
     */
    T setSwipeEdge(int widthPixel);

    /**
     * 可以启动滑动的范围百分比
     * @param percent 范围值百分比
     * @return page实体
     */
    T setSwipeEdgePercent(@FloatRange(from = 0.0f, to = 1.0f) float percent);

    /**
     * 设置阴影Drawable
     * @param shadow 阴影
     * @param orientation 方向
     * @return page实体
     */
    T setShadow(Drawable shadow, @SwipeBackLayout.EdgeOrientation int orientation);

    /**
     * 设置阴影资源
     * @param resId 阴影资源
     * @param orientation 方向
     * @return page实体
     */
    T setShadow(int resId, @SwipeBackLayout.EdgeOrientation int orientation);

    /**
     * 设置滑动时上一个界面的偏移百分比
     * @param percent 偏移百分比
     * @return page实体
     */
    T setOffsetPercent(float percent);

    /**
     * 设置滑动到多少百分比后松手会关闭界面
     * @param percent 百分比
     * @return page实体
     */
    T setClosePercent(float percent);

    /**
     * 监听事件
     * @param listener 监听事件
     * @return page实体
     */
    T addOnSwipeListener(SwipeBackLayout.OnSwipeListener listener);
}
