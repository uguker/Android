package com.uguke.android.swipe;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import androidx.annotation.FloatRange;
import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.core.view.ViewCompat;
import androidx.customview.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.FragmentationMagician;

import com.uguke.android.R;
import com.uguke.android.app.SupportActivity;
import com.uguke.android.app.SupportFragment;

/**
 * 滑动返回控件
 * @author LeiJue
 */
public class SwipeBackLayout extends FrameLayout {
    /** 左滑动边缘 **/
    public static final int EDGE_LEFT = ViewDragHelper.EDGE_LEFT;
    /** 右滑动边缘 **/
    public static final int EDGE_RIGHT = ViewDragHelper.EDGE_RIGHT;
    /** 两侧滑动 **/
    public static final int EDGE_ALL = EDGE_LEFT | EDGE_RIGHT;

    @IntDef({EDGE_LEFT, EDGE_RIGHT, EDGE_ALL})
    @Retention(RetentionPolicy.SOURCE)
    public @interface EdgeOrientation {}

    /** 拖拽限制状态 **/
    public static final int STATE_IDLE = ViewDragHelper.STATE_IDLE;
    /** 拖拽中状态 **/
    public static final int STATE_DRAGGING = ViewDragHelper.STATE_DRAGGING;
    /** 拖拽后自然沉降的状态 **/
    public static final int STATE_SETTLING = ViewDragHelper.STATE_SETTLING;
    /** 拖拽结束状态 **/
    public static final int STATE_FINISHED = 3;
    /** 默认遮罩颜色 **/
    private static final int DEFAULT_SCRIM_COLOR = 0x99000000;
    /** 默认视差偏移百分比 **/
    private static final float DEFAULT_PARALLAX = 0.33f;
    /** 透明度满值 **/
    private static final int FULL_ALPHA = 255;
    private static final float DEFAULT_SCROLL_THRESHOLD = 0.4f;
    private static final int OVER_SCROLL_DISTANCE = 10;

    private float mClosePercent = DEFAULT_SCROLL_THRESHOLD;

    private ViewDragHelper mHelper;

    private float mScrollPercent;

    private int mScrimColor = DEFAULT_SCRIM_COLOR;
    private float mScrimOpacity;

    private View mContentView;
    private FragmentActivity mActivity;
    private SupportFragment mFragment;
    private Fragment mPreFragment;

    private Drawable mShadowLeft;
    private Drawable mShadowRight;
    private Rect mTmpRect = new Rect();

    private int mEdgeFlag;
    private boolean mSwipeBackEnable = true;
    private int mCurrentSwipeOrientation;
    private float mOffsetPercent = DEFAULT_PARALLAX;

    private boolean mCallOnDestroyView;

    private boolean mInLayout;

    private int mContentLeft;
    private int mContentTop;
    private float mSwipeAlpha = 0.5f;

    /** 监听事件 **/
    private List<OnSwipeListener> mListeners;

    private Context mContext;

    /** 边缘等级 **/
    public enum EdgeLevel {
        /** 满屏 **/
        MAX,
        /** 20dp **/
        MIN,
        /** 半屏 **/
        MED
    }

    public SwipeBackLayout(Context context) {
        this(context, null);
    }

    public SwipeBackLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwipeBackLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        mHelper = ViewDragHelper.create(this, new ViewDragCallback());
        setShadow(R.drawable.swipe_shadow_left, EDGE_LEFT);
        setEdgeOrientation(EDGE_LEFT);
    }

    public ViewDragHelper getViewDragHelper() {
        return mHelper;
    }

    /**
     * 滑动中，上一个页面View的阴影透明度
     * @param alpha 0.0f:无阴影, 1.0f:较重的阴影, 默认:0.5f
     */
    public void setSwipeAlpha(@FloatRange(from = 0.0f, to = 1.0f) float alpha) {
        mSwipeAlpha = alpha;
    }

    /**
     * 关闭百分比, 滚动多少将关闭界面（百分比）
     * @param percent 关闭百分比（百分比）
     */
    public void setClosePercent(@FloatRange(from = 0.0f, to = 1.0f) float percent) {
        if (percent >= 1 || percent <= 0) {
            throw new IllegalArgumentException("Percent value should be between 0 and 1.0");
        }
        mClosePercent = percent;
    }

    /**
     * 设置偏移百分比（百分比）
     * @param percent 偏移百分比
     */
    public void setOffsetPercent(float percent) {
        mOffsetPercent = percent;
    }

    public void setEdgeOrientation(@EdgeOrientation int orientation) {
        mEdgeFlag = orientation;
        mHelper.setEdgeTrackingEnabled(orientation);

        if (orientation == EDGE_RIGHT || orientation == EDGE_ALL) {
            setShadow(R.drawable.swipe_shadow_right, EDGE_RIGHT);
        }
    }

    public void setShadow(Drawable shadow, @EdgeOrientation int orientation) {
        // 左边的阴影
        if ((orientation & EDGE_LEFT) != 0) {
            mShadowLeft = shadow;
        }
        // 右边的阴影
        if ((orientation & EDGE_RIGHT) != 0) {
            mShadowRight = shadow;
        }
        invalidate();
    }

    public void setShadow(int resId, @EdgeOrientation int orientation) {
        setShadow(ContextCompat.getDrawable(mContext, resId), orientation);
    }

    public void addOnSwipeListener(OnSwipeListener listener) {
        if (mListeners == null) {
            mListeners = new ArrayList<>();
        }
        mListeners.add(listener);
    }

    public void removeOnSwipeListener(OnSwipeListener listener) {
        if (mListeners == null) {
            return;
        }
        mListeners.remove(listener);
    }

    public interface OnSwipeListener {
        /**
         * Invoke when state change
         *
         * @param state flag to describe scroll state
         * @see #STATE_IDLE
         * @see #STATE_DRAGGING
         * @see #STATE_SETTLING
         * @see #STATE_FINISHED
         */
        void onDragStateChange(int state);

        /**
         * Invoke when edge touched
         *
         * @param oritentationEdgeFlag edge flag describing the edge being touched
         * @see #EDGE_LEFT
         * @see #EDGE_RIGHT
         */
        /**
         * 开始滑动
         * @param orientation 边缘方向
         */
        void onEdgeTouch(int orientation);

        /**
         * Invoke when scroll percent over the threshold for the first time
         *
         * @param scrollPercent scroll percent of this view
         */
        /**
         * 首次滚动超过关闭百分比时调用
         * @param percent 关闭百分比
         */
        void onScrollToClose(float percent);
    }

    @Override
    protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
        boolean isDrawView = child == mContentView;
        boolean drawChild = super.drawChild(canvas, child, drawingTime);
        if (isDrawView && mScrimOpacity > 0 && mHelper.getViewDragState() != ViewDragHelper.STATE_IDLE) {
            drawShadow(canvas, child);
            drawScrim(canvas, child);
        }
        return drawChild;
    }

    private void drawShadow(Canvas canvas, View child) {
        final Rect childRect = mTmpRect;
        child.getHitRect(childRect);

        if ((mCurrentSwipeOrientation & EDGE_LEFT) != 0) {
            mShadowLeft.setBounds(childRect.left - mShadowLeft.getIntrinsicWidth(), childRect.top, childRect.left, childRect.bottom);
            mShadowLeft.setAlpha((int) (mScrimOpacity * FULL_ALPHA));
            mShadowLeft.draw(canvas);
        } else if ((mCurrentSwipeOrientation & EDGE_RIGHT) != 0) {
            mShadowRight.setBounds(childRect.right, childRect.top, childRect.right + mShadowRight.getIntrinsicWidth(), childRect.bottom);
            mShadowRight.setAlpha((int) (mScrimOpacity * FULL_ALPHA));
            mShadowRight.draw(canvas);
        }
    }

    private void drawScrim(Canvas canvas, View child) {
        final int baseAlpha = (mScrimColor & 0xff000000) >>> 24;
        final int alpha = (int) (baseAlpha * mScrimOpacity * mSwipeAlpha);
        final int color = alpha << 24 | (mScrimColor & 0xffffff);

        if ((mCurrentSwipeOrientation & EDGE_LEFT) != 0) {
            canvas.clipRect(0, 0, child.getLeft(), getHeight());
        } else if ((mCurrentSwipeOrientation & EDGE_RIGHT) != 0) {
            canvas.clipRect(child.getRight(), 0, getRight(), getHeight());
        }
        canvas.drawColor(color);
    }

    public void setScrimColor(int color) {
        mScrimColor = color;
        invalidate();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        mInLayout = true;
        if (mContentView != null) {
            mContentView.layout(mContentLeft, mContentTop,
                    mContentLeft + mContentView.getMeasuredWidth(),
                    mContentTop + mContentView.getMeasuredHeight());
        }
        mInLayout = false;
    }

    @Override
    public void requestLayout() {
        if (!mInLayout) {
            super.requestLayout();
        }
    }

    @Override
    public void computeScroll() {
        mScrimOpacity = 1 - mScrollPercent;
        if (mScrimOpacity >= 0) {
            if (mHelper.continueSettling(true)) {
                ViewCompat.postInvalidateOnAnimation(this);
            }

            // Fragment中上一个SwipeBackLayout变化情况
            if (mPreFragment != null && mPreFragment.getView() != null) {
                if (mCallOnDestroyView) {
                    mPreFragment.getView().setX(0);
                    return;
                }

                if (mHelper.getCapturedView() != null) {
                    int leftOffset = (int) ((mHelper.getCapturedView().getLeft() - getWidth()) * mOffsetPercent * mScrimOpacity);
                    mPreFragment.getView().setX(leftOffset > 0 ? 0 : leftOffset);
                }
            }
            // Activity中上一个SwipeBackLayout变化情况
            SwipeBackActivityPage page = SwipeBackHelper.getPrePage(mActivity);
            if (mActivity != null && page != null) {
                if (mHelper.getCapturedView() != null) {
                    int leftOffset = (int) ((mHelper.getCapturedView().getLeft() - getWidth()) * mOffsetPercent * mScrimOpacity);
                    page.getSwipeBackLayout().setX(leftOffset > 0 ? 0 : leftOffset);
                }
            }
        }
    }

    void setFragment(final SupportFragment fragment, View view) {
        mFragment = fragment;
        mContentView = view;
    }

    public void hiddenFragment() {
        if (mPreFragment != null && mPreFragment.getView() != null) {
            mPreFragment.getView().setVisibility(GONE);
        }
    }

    public void attachToActivity(FragmentActivity activity) {
        mActivity = activity;
        TypedArray a = activity.getTheme().obtainStyledAttributes(new int[]{
                android.R.attr.windowBackground
        });
        int background = a.getResourceId(0, 0);
        a.recycle();

        ViewGroup decor = (ViewGroup) activity.getWindow().getDecorView();
        ViewGroup decorChild = (ViewGroup) decor.getChildAt(0);
        decorChild.setBackgroundResource(background);
        decor.removeView(decorChild);
        addView(decorChild);
        setContentView(decorChild);
        decor.addView(this);
    }

    public void attachToFragment(SupportFragment fragment, View view) {
        addView(view);
        setFragment(fragment, view);
    }

    public void internalCallOnDestroyView() {
        mCallOnDestroyView = true;
    }

    public void setSwipeBackEnable(boolean enable) {
        mSwipeBackEnable = enable;
    }

    public void setEdgeLevel(EdgeLevel edgeLevel) {
        validateEdgeLevel(-1, edgeLevel);
    }

    public void setEdgeLevel(int widthPixel) {
        validateEdgeLevel(widthPixel, null);
    }

    private void setContentView(View view) {
        mContentView = view;
    }

    private void validateEdgeLevel(int widthPixel, EdgeLevel edgeLevel) {
        try {
            DisplayMetrics metrics = new DisplayMetrics();
            WindowManager windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
            windowManager.getDefaultDisplay().getMetrics(metrics);
            Field mEdgeSize = mHelper.getClass().getDeclaredField("mEdgeSize");
            mEdgeSize.setAccessible(true);
            if (widthPixel >= 0) {
                mEdgeSize.setInt(mHelper, widthPixel);
            } else {
                if (edgeLevel == EdgeLevel.MAX) {
                    mEdgeSize.setInt(mHelper, metrics.widthPixels);
                } else if (edgeLevel == EdgeLevel.MED) {
                    mEdgeSize.setInt(mHelper, metrics.widthPixels / 2);
                } else {
                    mEdgeSize.setInt(mHelper, ((int) (20 * metrics.density + 0.5f)));
                }
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private class ViewDragCallback extends ViewDragHelper.Callback {

        @Override
        public boolean tryCaptureView(@NonNull View child, int pointerId) {
            boolean dragEnable = mHelper.isEdgeTouched(mEdgeFlag, pointerId);
            if (dragEnable) {
                if (mHelper.isEdgeTouched(EDGE_LEFT, pointerId)) {
                    mCurrentSwipeOrientation = EDGE_LEFT;
                } else if (mHelper.isEdgeTouched(EDGE_RIGHT, pointerId)) {
                    mCurrentSwipeOrientation = EDGE_RIGHT;
                }

                if (mListeners != null) {
                    for (OnSwipeListener listener : mListeners) {
                        listener.onEdgeTouch(mCurrentSwipeOrientation);
                    }
                }

                if (mPreFragment == null) {
                    if (mFragment != null && mFragment.getFragmentManager() != null) {
                        List<Fragment> fragmentList = FragmentationMagician.getActiveFragments(mFragment.getFragmentManager());
                        if (fragmentList != null && fragmentList.size() > 1) {
                            int index = fragmentList.indexOf(mFragment);
                            for (int i = index - 1; i >= 0; i--) {
                                Fragment fragment = fragmentList.get(i);
                                if (fragment != null && fragment.getView() != null) {
                                    fragment.getView().setVisibility(VISIBLE);
                                    mPreFragment = fragment;
                                    break;
                                }
                            }
                        }
                    }
                } else {
                    View preView = mPreFragment.getView();
                    if (preView != null && preView.getVisibility() != VISIBLE) {
                        preView.setVisibility(VISIBLE);
                    }
                }
            }
            return dragEnable;
        }

        @Override
        public int clampViewPositionHorizontal(@NonNull View child, int left, int dx) {
            int ret = 0;
            if ((mCurrentSwipeOrientation & EDGE_LEFT) != 0) {
                ret = Math.min(child.getWidth(), Math.max(left, 0));
            } else if ((mCurrentSwipeOrientation & EDGE_RIGHT) != 0) {
                ret = Math.min(0, Math.max(left, -child.getWidth()));
            }
            return ret;
        }

        @Override
        public void onViewPositionChanged(@NonNull View changedView, int left, int top, int dx, int dy) {
            super.onViewPositionChanged(changedView, left, top, dx, dy);

            if ((mCurrentSwipeOrientation & EDGE_LEFT) != 0) {
                mScrollPercent = Math.abs((float) left / (mContentView.getWidth() + mShadowLeft.getIntrinsicWidth()));
            } else if ((mCurrentSwipeOrientation & EDGE_RIGHT) != 0) {
                mScrollPercent = Math.abs((float) left / (mContentView.getWidth() + mShadowRight.getIntrinsicWidth()));
            }
            mContentLeft = left;
            mContentTop = top;
            invalidate();

            if (mListeners != null && mHelper.getViewDragState() == STATE_DRAGGING && mScrollPercent <= 1 && mScrollPercent > 0) {
                for (OnSwipeListener listener : mListeners) {
                    listener.onScrollToClose(mScrollPercent);
                }
            }

            if (mScrollPercent > 1) {
                if (mFragment != null) {
                    if (mCallOnDestroyView) {
                        return;
                    }
                    if (!mFragment.isDetached()) {
                        onDragFinished();
                        mFragment.getSupportDelegate().popQuiet();
                    }
                } else {
                    if (!mActivity.isFinishing()) {
                        onDragFinished();
                        mActivity.finish();
                        mActivity.overridePendingTransition(0, 0);
                    }
                }
            }
        }

        @Override
        public int getViewHorizontalDragRange(@NonNull View child) {
            if (mFragment != null) {
                return 1;
            }
            if (mActivity instanceof SupportActivity && ((SupportActivity) mActivity).onSwipeBackPriority()) {
                return 1;
            }
            return 0;
        }

        @Override
        public void onViewReleased(@NonNull View releasedChild, float xvel, float yvel) {
            final int childWidth = releasedChild.getWidth();

            int left = 0, top = 0;
            if ((mCurrentSwipeOrientation & EDGE_LEFT) != 0) {
                left = xvel > 0 || xvel == 0 && mScrollPercent > mClosePercent ? (childWidth
                        + mShadowLeft.getIntrinsicWidth() + OVER_SCROLL_DISTANCE) : 0;
            } else if ((mCurrentSwipeOrientation & EDGE_RIGHT) != 0) {
                left = xvel < 0 || xvel == 0 && mScrollPercent > mClosePercent ? -(childWidth
                        + mShadowRight.getIntrinsicWidth() + OVER_SCROLL_DISTANCE) : 0;
            }

            mHelper.settleCapturedViewAt(left, top);
            invalidate();
        }

        @Override
        public void onViewDragStateChanged(int state) {
            super.onViewDragStateChanged(state);
            if (mListeners != null) {
                for (OnSwipeListener listener : mListeners) {
                    listener.onDragStateChange(state);
                }
            }
        }

        @Override
        public void onEdgeTouched(int edgeFlags, int pointerId) {
            super.onEdgeTouched(edgeFlags, pointerId);
            if ((mEdgeFlag & edgeFlags) != 0) {
                mCurrentSwipeOrientation = edgeFlags;
            }
        }
    }

    private void onDragFinished() {
        if (mListeners != null) {
            for (OnSwipeListener listener : mListeners) {
                listener.onDragStateChange(STATE_FINISHED);
            }
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (!mSwipeBackEnable) {
            return super.onInterceptTouchEvent(ev);
        }
        try {
            return mHelper.shouldInterceptTouchEvent(ev);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!mSwipeBackEnable) {
            return super.onTouchEvent(event);
        }
        try {
            mHelper.processTouchEvent(event);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
