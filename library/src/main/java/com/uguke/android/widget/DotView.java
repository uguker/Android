package com.uguke.android.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.view.ViewCompat;

import com.uguke.android.R;

/**
 * 用于需要圆角矩形框背景的TextView的情况,减少直接使用TextView时引入的shape资源文件
 * @author LeiJue
 **/
public class DotView extends AppCompatTextView {

    /** 最大数量 **/
    private static final String MAX_COUNT = "99+";
    /** 第一级别数量 **/
    private static final int COUNT_LEVEL1 = 10;
    /** 第二级别数量 **/
    private static final int COUNT_LEVEL2 = 100;

    private GradientDrawable mBackground = new GradientDrawable();
    private int mBackgroundColor;
    private int mCornerRadius;
    private int mStrokeWidth;
    private int mStrokeColor;
    private int mInnerPadding;
    private boolean mRadiusHalfHeight;
    private boolean mWidthHeightEqual;

    public DotView(Context context) {
        this(context, null);
    }

    public DotView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DotView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        obtainAttributes(context, attrs);
    }

    private void obtainAttributes(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.DotView);
        mBackgroundColor = ta.getColor(R.styleable.DotView_mvBackgroundColor, Color.TRANSPARENT);
        mCornerRadius = ta.getDimensionPixelSize(R.styleable.DotView_mvCornerRadius, 0);
        mStrokeWidth = ta.getDimensionPixelSize(R.styleable.DotView_mvStrokeWidth, 0);
        mInnerPadding = ta.getDimensionPixelSize(R.styleable.DotView_mvInnerPadding, toPixel(3));
        mStrokeColor = ta.getColor(R.styleable.DotView_mvStrokeColor, Color.TRANSPARENT);
        mRadiusHalfHeight = ta.getBoolean(R.styleable.DotView_mvRadiusHalfHeight, false);
        mWidthHeightEqual = ta.getBoolean(R.styleable.DotView_mvWidthHeightEqual, false);
        ta.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mWidthHeightEqual && getWidth() > 0 && getHeight() > 0) {
            int max = Math.max(getWidth(), getHeight());
            int measureSpec = MeasureSpec.makeMeasureSpec(max, MeasureSpec.EXACTLY);
            super.onMeasure(measureSpec, measureSpec);
            return;
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (mRadiusHalfHeight) {
            mCornerRadius = getHeight() / 2;
            setBackgroundSelector();
        } else {
            setBackgroundSelector();
        }
    }

    @Override
    public void setBackgroundColor(int backgroundColor) {
        mBackgroundColor = backgroundColor;
        setBackgroundSelector();
    }

    public DotView setCornerRadius(float cornerRadius) {
        mCornerRadius = toPixel(cornerRadius);
        setBackgroundSelector();
        return this;
    }

    public DotView setStrokeWidth(float strokeWidth) {
        mStrokeWidth = toPixel(strokeWidth);
        setBackgroundSelector();
        return this;
    }

    public DotView setStrokeColor(int strokeColor) {
        mStrokeColor = strokeColor;
        setBackgroundSelector();
        return this;
    }

    public DotView setRadiusHalfHeight(boolean radiusHalfHeight) {
        mRadiusHalfHeight = radiusHalfHeight;
        setBackgroundSelector();
        return this;
    }

    public DotView setWidthHeightEqual(boolean widthHeightEqual) {
        mWidthHeightEqual = widthHeightEqual;
        setBackgroundSelector();
        return this;
    }

    public int getBackgroundColor() {
        return mBackgroundColor;
    }

    public int getCornerRadius() {
        return mCornerRadius;
    }

    public int getStrokeWidth() {
        return mStrokeWidth;
    }

    public int getStrokeColor() {
        return mStrokeColor;
    }

    public boolean isRadiusHalfHeight() {
        return mRadiusHalfHeight;
    }

    public boolean isWidthHeightEqual() {
        return mWidthHeightEqual;
    }

    private void setBackgroundSelector() {
        StateListDrawable bg = new StateListDrawable();
        mBackground.setColor(mBackgroundColor);
        mBackground.setCornerRadius(mCornerRadius);
        mBackground.setStroke(mStrokeWidth, mStrokeColor);
        bg.addState(new int[]{-android.R.attr.state_pressed}, mBackground);
        ViewCompat.setBackground(this, bg);
    }

    private int toPixel(float dip) {
        float density = Resources.getSystem().getDisplayMetrics().density;
        return (int) Math.ceil(dip * density);
    }

    public void showNum(int num) {
        ViewGroup.LayoutParams params = getLayoutParams();
        setVisibility(View.VISIBLE);
        if (num <= 0) {
            //圆点,设置默认宽高
            setText("");
            params.width = mInnerPadding * 2;
            params.height = mInnerPadding * 2;
        } else {
            params.height = (int) (mInnerPadding * 2 + getTextSize());
            if (num < COUNT_LEVEL1) {
                // 第一级别，显示为圆形
                params.width = (int) (mInnerPadding * 2 + getTextSize());
                setText(String.valueOf(num));
            } else {
                // 圆角矩形,圆角是高度的一半
                String text = num < COUNT_LEVEL2 ? String.valueOf(num) : MAX_COUNT;
                Rect rt = new Rect();
                getPaint().getTextBounds(text, 0, text.length(), rt);
                params.width = mInnerPadding * 2 + rt.width();
                setText(text);
            }
        }
        setLayoutParams(params);
    }

}
