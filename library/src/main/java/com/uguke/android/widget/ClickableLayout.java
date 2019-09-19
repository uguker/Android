package com.uguke.android.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

/**
 * 可关闭子控件所有点击事件的容器
 * @author LeiJue
 */
public class ClickableLayout extends FrameLayout {

    /**
     * 子控件是否可以接受点击事件
     */
    private boolean mChildClickable = true;

    public ClickableLayout(Context context) {
        super(context, null);
    }

    public ClickableLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ClickableLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        //返回true则拦截子控件所有点击事件，如果mChildClickable为true，则需返回false
        return !mChildClickable;
    }

    public void setChildClickable(boolean clickable) {
        mChildClickable = clickable;
    }
}
