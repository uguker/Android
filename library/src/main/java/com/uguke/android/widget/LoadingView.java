package com.uguke.android.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.annotation.IntRange;
import androidx.core.content.ContextCompat;

import com.uguke.android.util.ResUtils;
import com.uguke.android.R;

/**
 * 加载控件
 * @author LeiJue
 */
public class LoadingView extends View {

    private Paint mPaint;
    private RectF mArcRectF;
    private int mViewWidth;
    private int mViewHeight;
    /** 叶数 **/
    private int mArcCount;
    /** 圆弧颜色 **/
    private int[] mArcColors;
    /** 旋转圆弧属性 **/
    private RotatingArc mRotatingArc;

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            invalidate();
            mHandler.sendEmptyMessageDelayed(0, 10);
            return false;
        }
    });

    public LoadingView(Context context) {
        super(context);
        init(context);
    }

    public LoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public LoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mArcRectF = new RectF();
        mArcCount = 1;
        mArcColors = new int[] {ContextCompat.getColor(context, R.color.colorAccent)};
        mRotatingArc = new RotatingArc(30, 270, 4);
        mRotatingArc.mIntervalAngle = 30;
        mRotatingArc.mStrokeWidth = ResUtils.toPixel(3);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mHandler.removeCallbacksAndMessages(null);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mViewWidth = MeasureSpec.getSize(widthMeasureSpec);
        mViewHeight = MeasureSpec.getSize(heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mRotatingArc.rotate(mArcCount);
        for (int i = 0; i < mArcCount; i++) {
            int squareLength = Math.min(mViewWidth, mViewHeight);
            float strokeWidth = mRotatingArc.getRealStrokeWidth(squareLength, mArcCount);
            mPaint.setColor(mArcColors[i % mArcColors.length]);
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStrokeWidth(strokeWidth);
            float startAngle = mRotatingArc.mStartAngle + i * (360 / mArcCount);
            drawArc(canvas, startAngle, mRotatingArc.mSweepAngle);
        }
    }

    public LoadingView setArcColors(@ColorInt int... colors) {
        if (colors.length == 0) {
            this.mArcColors = new int[] {ContextCompat.getColor(getContext(), R.color.colorAccent)};
        } else {
            this.mArcColors = colors;
            if (colors.length > mArcCount) {
                mArcCount = colors.length;
            }
        }
        return this;
    }

    public LoadingView setArcCount(@IntRange(from = 1) int count) {
        mArcCount = count > 1 ? count : 1;
        return this;
    }

    /**
     * 设置增量角度（仅对叶数为1有效）
     * @param add 增量角度
     */
    public LoadingView setArcAddAngle(float add) {
        mRotatingArc.mAddAngle = add;
        return this;
    }

    /**
     * 设置最小角度（仅对叶数为1有效）
     * @param min 最小角度
     */
    public LoadingView setArcMinAngle(float min) {
        mRotatingArc.mMinAngle = min;
        return this;
    }

    public LoadingView setArcIntervalAngle(float angle) {
        mRotatingArc.mIntervalAngle = angle;
        return this;
    }

    /**
     * 设置抖动比例(即改变弧的宽度)
     * @param ratio 抖动比例
     */
    public LoadingView setArcShakeRatio(float ratio) {
        mRotatingArc.mSnakeRatio = ratio;
        return this;
    }

    public LoadingView setArcStrokeWidth(float width) {
        mRotatingArc.mStrokeWidth = getResources().getDisplayMetrics().density * width;
        return this;
    }

    /**
     * 设置转一圈需要时间
     * @param time 转一圈需要时间
     */
    public LoadingView setRoundUseTime(int time) {
        mRotatingArc.mRotateRate = 3600 / time;
        return this;
    }

    public void start() {
        mHandler.sendEmptyMessageDelayed(0, 10);
    }

    public void stop() {
        mHandler.removeMessages(0);
    }

    private void drawArc(Canvas canvas, float startAngle, float sweepAngle) {
        int squareLength = Math.min(mViewWidth, mViewHeight);
        float strokeWidth = mRotatingArc.getRealStrokeWidth(squareLength, mArcCount);
        mArcRectF.set(
                (Math.abs(squareLength - mViewWidth) >> 1) + strokeWidth,
                (Math.abs(squareLength - mViewHeight) >> 1) + strokeWidth,
                (Math.abs(squareLength - mViewWidth) >> 1) + squareLength - strokeWidth,
                (Math.abs(squareLength - mViewHeight) >> 1) + squareLength - strokeWidth);
        // 画圆弧
        canvas.drawArc(mArcRectF, startAngle, sweepAngle, false, mPaint);
        // 画两个圆圈,是弧线变得圆润
        mPaint.setStyle(Paint.Style.FILL);
        PointF startPoint = mRotatingArc.getStartPoint(mArcRectF);
        PointF endPoint = mRotatingArc.getEndPoint(mArcRectF);
        canvas.drawCircle(startPoint.x, startPoint.y, strokeWidth / 2, mPaint);
        canvas.drawCircle(endPoint.x, endPoint.y, strokeWidth / 2, mPaint);
    }

    /**
     * 转动的弧线数据类
     */
    static final class RotatingArc {
        /** 单页最小角度 **/
        private float mMinAngle;
        /** 单页最小角度 **/
        private float mAddAngle;
        /** 旋转速度 **/
        private float mRotateRate;
        /** 抖动比例 **/
        private float mSnakeRatio;
        /** 圆弧宽度 **/
        private float mStrokeWidth;
        /** 叶型圆弧角度间隔 **/
        private float mIntervalAngle;
        /** 开始画弧线的角度 **/
        private float mStartAngle;
        /** 需要画弧线的角度 **/
        private float mSweepAngle;
        /** 是否正在增加角度 **/
        private boolean mAngleAdding;
        private PointF mStartPoint;
        private PointF mEndPoint;

        RotatingArc(float minAngle, float addAngle, float rotateRate) {
            this.mRotateRate = rotateRate;
            this.mMinAngle = minAngle;
            this.mAddAngle = addAngle;
            this.mAngleAdding = true;
            this.mStartPoint = new PointF(0, 0);
            this.mEndPoint = new PointF(0, 0);
        }

        void rotate(int arcCount) {
            // 开始的幅度角度
            mStartAngle += mAngleAdding ? mRotateRate : mRotateRate * 2;
            if (arcCount > 1) {
                mSweepAngle = (360 / arcCount - mIntervalAngle);
                mSweepAngle = (int) (mSweepAngle - mSweepAngle / 2 * getRouteNumber());
            } else {
                // 需要画的弧度
                mSweepAngle += mAngleAdding ? mRotateRate : -mRotateRate;
                mAngleAdding = mAngleAdding ? mSweepAngle < mMinAngle + mAddAngle : mSweepAngle <= mMinAngle;
            }
        }

        PointF getStartPoint(RectF rectF) {
            float startX = (float) ((1.0 + Math.cos(Math.PI * mStartAngle / 180))) / 2 * rectF.width() + rectF.left;
            float startY = (float) ((1.0 + Math.sin(Math.PI * mStartAngle / 180))) / 2 * rectF.height() + rectF.top;
            mStartPoint.set(startX, startY);
            return mStartPoint;
        }

        PointF getEndPoint(RectF rectF) {
            float endAngle = mStartAngle + mSweepAngle;
            float startX = (float) ((1.0 + Math.cos(Math.PI * endAngle / 180))) / 2 * rectF.width() + rectF.left;
            float startY = (float) ((1.0 + Math.sin(Math.PI * endAngle / 180))) / 2 * rectF.height() + rectF.top;
            mEndPoint.set(startX, startY);
            return mEndPoint;
        }

        float getRealStrokeWidth(int squareLength, int arcCount) {
            return getRouteNumber() * squareLength / 2 * (arcCount > 1 ? mSnakeRatio : 0) + mStrokeWidth;
        }

        float getRouteNumber() {
            return (float) ((1.0 + Math.sin(Math.PI * mStartAngle / 180))) / 2;
        }

    }
    
}
