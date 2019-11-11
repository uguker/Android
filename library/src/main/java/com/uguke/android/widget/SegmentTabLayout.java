package com.uguke.android.widget;

import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.SparseBooleanArray;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.uguke.android.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 类IOS TabLayout
 * @author LeiJue
 */
public class SegmentTabLayout extends FrameLayout implements ValueAnimator.AnimatorUpdateListener {

    private Context mContext;
    private List<TabEntity> mTabEntities = new ArrayList<>();
    private LinearLayout mTabContainer;
    private int mCurrentTab;
    private int mLastTab;
    private int mTabCount;
    /** 用于绘制显示器 */
    private Rect mIndicatorRect = new Rect();
    private GradientDrawable mIndicatorDrawable = new GradientDrawable();
    private GradientDrawable mRectDrawable = new GradientDrawable();

    private Paint mDividerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private float mTabPadding;
    private boolean mTabSpaceEqual;
    private float mTabWidth;

    /** indicator */
    private int mIndicatorColor;
    private float mIndicatorHeight;
    private float mIndicatorCornerRadius;
    private float mIndicatorMarginLeft;
    private float mIndicatorMarginTop;
    private float mIndicatorMarginRight;
    private float mIndicatorMarginBottom;
    private long mIndicatorAnimDuration;
    private boolean mIndicatorAnimEnable;
    private boolean mIndicatorBounceEnable;

    /** divider */
    private int mDividerColor;
    private float mDividerWidth;
    private float mDividerPadding;

    /** title */
    private static final int TEXT_BOLD_NONE = 0;
    private static final int TEXT_BOLD_WHEN_SELECT = 1;
    private static final int TEXT_BOLD_BOTH = 2;
    private float mTextSize;
    private int mTextSelectedColor;
    private int mTextUnselectedColor;
    private int mTextBold;
    private boolean mTextAllCaps;

    private int mBarColor;
    private int mBarStrokeColor;
    private float mBarStrokeWidth;

    private int mHeight;

    /** anim */
    private ValueAnimator mValueAnimator;
    private OvershootInterpolator mInterpolator = new OvershootInterpolator(0.8f);

    private float[] mRadiusArr = new float[8];

    public SegmentTabLayout(Context context) {
        this(context, null, 0);
    }

    public SegmentTabLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SegmentTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //重写onDraw方法,需要调用这个方法来清除flag
        setWillNotDraw(false);
        setClipChildren(false);
        setClipToPadding(false);

        mContext = context;
        mTabContainer = new LinearLayout(context);
        addView(mTabContainer);

        obtainAttributes(context, attrs);

        // 获取控件高度
        String height = attrs.getAttributeValue("http://schemas.android.com/apk/res/android", "layout_height");
        if (!height.equals(ViewGroup.LayoutParams.MATCH_PARENT + "") &&
                height.equals(ViewGroup.LayoutParams.WRAP_CONTENT + "")) {
            int[] systemAttrs = {android.R.attr.layout_height};
            TypedArray a = context.obtainStyledAttributes(attrs, systemAttrs);
            mHeight = a.getDimensionPixelSize(0, ViewGroup.LayoutParams.WRAP_CONTENT);
            a.recycle();
        }

        mValueAnimator = ValueAnimator.ofObject(new PointEvaluator(), mLastP, mCurrentP);
        mValueAnimator.addUpdateListener(this);
    }

    private void obtainAttributes(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.SegmentTabLayout);

        mIndicatorColor = ta.getColor(R.styleable.SegmentTabLayout_tlIndicatorColor, Color.parseColor("#222831"));
        mIndicatorHeight = ta.getDimension(R.styleable.SegmentTabLayout_tlIndicatorHeight, -1);
        mIndicatorCornerRadius = ta.getDimension(R.styleable.SegmentTabLayout_tlIndicatorCornerRadius, -1);
        mIndicatorMarginLeft = ta.getDimension(R.styleable.SegmentTabLayout_tlIndicatorMarginLeft, toPixel(0));
        mIndicatorMarginTop = ta.getDimension(R.styleable.SegmentTabLayout_tlIndicatorMarginTop, 0);
        mIndicatorMarginRight = ta.getDimension(R.styleable.SegmentTabLayout_tlIndicatorMarginRight, toPixel(0));
        mIndicatorMarginBottom = ta.getDimension(R.styleable.SegmentTabLayout_tlIndicatorMarginBottom, 0);
        mIndicatorAnimEnable = ta.getBoolean(R.styleable.SegmentTabLayout_tlIndicatorAnimEnable, false);
        mIndicatorBounceEnable = ta.getBoolean(R.styleable.SegmentTabLayout_tlIndicatorBounceEnable, true);
        mIndicatorAnimDuration = ta.getInt(R.styleable.SegmentTabLayout_tlIndicatorAnimDuration, -1);

        mDividerColor = ta.getColor(R.styleable.SegmentTabLayout_tlDividerColor, mIndicatorColor);
        mDividerWidth = ta.getDimension(R.styleable.SegmentTabLayout_tlDividerWidth, toPixel(1));
        mDividerPadding = ta.getDimension(R.styleable.SegmentTabLayout_tlDividerPadding, 0);

        mTextSize = ta.getDimension(R.styleable.SegmentTabLayout_tlTextSize, toPixel(13f));
        mTextSelectedColor = ta.getColor(R.styleable.SegmentTabLayout_tlTextSelectedColor, Color.parseColor("#ffffff"));
        mTextUnselectedColor = ta.getColor(R.styleable.SegmentTabLayout_tlTextUnselectedColor, mIndicatorColor);
        mTextBold = ta.getInt(R.styleable.SegmentTabLayout_tlTextBold, TEXT_BOLD_NONE);
        mTextAllCaps = ta.getBoolean(R.styleable.SegmentTabLayout_tlTextAllCaps, false);

        mTabSpaceEqual = ta.getBoolean(R.styleable.SegmentTabLayout_tlTabSpaceEqual, true);
        mTabWidth = ta.getDimension(R.styleable.SegmentTabLayout_tlTabWidth, toPixel(-1));
        mTabPadding = ta.getDimension(R.styleable.SegmentTabLayout_tlTabPadding, mTabSpaceEqual || mTabWidth > 0 ? toPixel(0) : toPixel(10));

        mBarColor = ta.getColor(R.styleable.SegmentTabLayout_tlBarColor, Color.TRANSPARENT);
        mBarStrokeColor = ta.getColor(R.styleable.SegmentTabLayout_tlBarStrokeColor, mIndicatorColor);
        mBarStrokeWidth = ta.getDimension(R.styleable.SegmentTabLayout_tlBarStrokeWidth, toPixel(1));
        ta.recycle();
    }

    public void setTabData(List<? extends TabEntity> tabEntities) {
        mTabEntities.clear();
        if (tabEntities != null) {
            mTabEntities.addAll(tabEntities);
        }
        notifyDataSetChanged();
    }

    public void setTabData(final CharSequence[] titles) {
        mTabEntities.clear();
        if (titles != null) {
            for (final CharSequence title :titles) {
                mTabEntities.add(new TabEntity() {
                    @Override
                    public CharSequence getTitle() {
                        return title;
                    }
                });
            }
        }
        notifyDataSetChanged();
    }

    /** 更新数据 */
    public void notifyDataSetChanged() {
        mTabContainer.removeAllViews();
        mTabCount = mTabEntities.size();
        View tabView;
        for (int i = 0; i < mTabCount; i++) {
            tabView = View.inflate(mContext, R.layout.android_widget_layout_tab_segment, null);
            tabView.setTag(i);
            addTab(i, tabView);
        }
        updateTabStyles();
    }

    /**
     *
     */
    private void addTab(final int position, View tabView) {
        TextView title = tabView.findViewById(R.id.android_tab_title);
        title.setText(mTabEntities.get(position).getTitle());
        tabView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (Integer) v.getTag();
                if (mCurrentTab != position) {
                    setCurrentTab(position);
                    for (OnTabSelectedListener listener : mListeners) {
                        if (listener != null) {
                            listener.onTabSelected(position);
                        }
                    }
                } else {
                    for (OnTabSelectedListener listener : mListeners) {
                        if (listener != null) {
                            listener.onTabReselected(position);
                        }
                    }
                }
            }
        });

        // 每一个Tab的布局参数
        LinearLayout.LayoutParams params = mTabSpaceEqual ?
                new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT, 1.0f) :
                new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        if (mTabWidth > 0) {
            params = new LinearLayout.LayoutParams((int) mTabWidth, LayoutParams.MATCH_PARENT);
        }
        mTabContainer.addView(tabView, position, params);
    }

    private void updateTabStyles() {
        for (int i = 0; i < mTabCount; i++) {
            View tabView = mTabContainer.getChildAt(i);
            tabView.setPadding((int) mTabPadding, 0, (int) mTabPadding, 0);
            TextView titleView = tabView.findViewById(R.id.android_tab_title);
            titleView.setTextColor(i == mCurrentTab ? mTextSelectedColor : mTextUnselectedColor);
            titleView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);
            if (mTextAllCaps) {
                titleView.setText(titleView.getText().toString().toUpperCase());
            }

            if (mTextBold == TEXT_BOLD_BOTH) {
                titleView.getPaint().setFakeBoldText(true);
            } else if (mTextBold == TEXT_BOLD_NONE) {
                titleView.getPaint().setFakeBoldText(false);
            }
        }
    }

    private void updateTabSelection(int position) {
        for (int i = 0; i < mTabCount; ++i) {
            View tabView = mTabContainer.getChildAt(i);
            final boolean isSelect = i == position;
            TextView titleView = tabView.findViewById(R.id.android_tab_title);
            titleView.setTextColor(isSelect ? mTextSelectedColor : mTextUnselectedColor);
            if (mTextBold == TEXT_BOLD_WHEN_SELECT) {
                titleView.getPaint().setFakeBoldText(isSelect);
            }
        }
    }

    private void calcOffset() {
        final View currentTabView = mTabContainer.getChildAt(mCurrentTab);
        mCurrentP.left = currentTabView.getLeft();
        mCurrentP.right = currentTabView.getRight();

        final View lastTabView = mTabContainer.getChildAt(mLastTab);
        mLastP.left = lastTabView.getLeft();
        mLastP.right = lastTabView.getRight();

        if (mLastP.left == mCurrentP.left && mLastP.right == mCurrentP.right) {
            invalidate();
        } else {
            mValueAnimator.setObjectValues(mLastP, mCurrentP);
            if (mIndicatorBounceEnable) {
                mValueAnimator.setInterpolator(mInterpolator);
            }

            if (mIndicatorAnimDuration < 0) {
                mIndicatorAnimDuration = mIndicatorBounceEnable ? 500 : 250;
            }
            mValueAnimator.setDuration(mIndicatorAnimDuration);
            mValueAnimator.start();
        }
    }

    private void calcIndicatorRect() {
        View currentTabView = mTabContainer.getChildAt(mCurrentTab);
        float left = currentTabView.getLeft();
        float right = currentTabView.getRight();

        mIndicatorRect.left = (int) left;
        mIndicatorRect.right = (int) right;

        if (!mIndicatorAnimEnable) {
            if (mCurrentTab == 0) {
                // The corners are ordered top-left, top-right, bottom-right, bottom-left
                mRadiusArr[0] = mIndicatorCornerRadius;
                mRadiusArr[1] = mIndicatorCornerRadius;
                mRadiusArr[2] = 0;
                mRadiusArr[3] = 0;
                mRadiusArr[4] = 0;
                mRadiusArr[5] = 0;
                mRadiusArr[6] = mIndicatorCornerRadius;
                mRadiusArr[7] = mIndicatorCornerRadius;
            } else if (mCurrentTab == mTabCount - 1) {
                // The corners are ordered top-left, top-right, bottom-right, bottom-left
                mRadiusArr[0] = 0;
                mRadiusArr[1] = 0;
                mRadiusArr[2] = mIndicatorCornerRadius;
                mRadiusArr[3] = mIndicatorCornerRadius;
                mRadiusArr[4] = mIndicatorCornerRadius;
                mRadiusArr[5] = mIndicatorCornerRadius;
                mRadiusArr[6] = 0;
                mRadiusArr[7] = 0;
            } else {
                // The corners are ordered top-left, top-right, bottom-right, bottom-left
                mRadiusArr[0] = 0;
                mRadiusArr[1] = 0;
                mRadiusArr[2] = 0;
                mRadiusArr[3] = 0;
                mRadiusArr[4] = 0;
                mRadiusArr[5] = 0;
                mRadiusArr[6] = 0;
                mRadiusArr[7] = 0;
            }
        } else {
            // The corners are ordered top-left, top-right, bottom-right, bottom-left
            mRadiusArr[0] = mIndicatorCornerRadius;
            mRadiusArr[1] = mIndicatorCornerRadius;
            mRadiusArr[2] = mIndicatorCornerRadius;
            mRadiusArr[3] = mIndicatorCornerRadius;
            mRadiusArr[4] = mIndicatorCornerRadius;
            mRadiusArr[5] = mIndicatorCornerRadius;
            mRadiusArr[6] = mIndicatorCornerRadius;
            mRadiusArr[7] = mIndicatorCornerRadius;
        }
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        IndicatorPoint p = (IndicatorPoint) animation.getAnimatedValue();
        mIndicatorRect.left = (int) p.left;
        mIndicatorRect.right = (int) p.right;
        invalidate();
    }

    private boolean mIsFirstDraw = true;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isInEditMode() || mTabCount <= 0) {
            return;
        }
        int height = getHeight();
        int paddingLeft = getPaddingLeft();
        if (mIndicatorHeight < 0) {
            mIndicatorHeight = height - mIndicatorMarginTop - mIndicatorMarginBottom;
        }
        if (mIndicatorCornerRadius < 0 || mIndicatorCornerRadius > mIndicatorHeight / 2) {
            mIndicatorCornerRadius = mIndicatorHeight / 2;
        }
        // draw rect
        mRectDrawable.setColor(mBarColor);
        mRectDrawable.setStroke((int) mBarStrokeWidth, mBarStrokeColor);
        mRectDrawable.setCornerRadius(mIndicatorCornerRadius);
        mRectDrawable.setBounds(getPaddingLeft(), getPaddingTop(), getWidth() - getPaddingRight(), getHeight() - getPaddingBottom());
        mRectDrawable.draw(canvas);
        // draw divider
        if (!mIndicatorAnimEnable && mDividerWidth > 0) {
            mDividerPaint.setStrokeWidth(mDividerWidth);
            mDividerPaint.setColor(mDividerColor);
            for (int i = 0; i < mTabCount - 1; i++) {
                View tab = mTabContainer.getChildAt(i);
                canvas.drawLine(paddingLeft + tab.getRight(), mDividerPadding, paddingLeft + tab.getRight(), height - mDividerPadding, mDividerPaint);
            }
        }
        //draw indicator line
        if (mIndicatorAnimEnable) {
            if (mIsFirstDraw) {
                mIsFirstDraw = false;
                calcIndicatorRect();
            }
        } else {
            calcIndicatorRect();
        }
        mIndicatorDrawable.setColor(mIndicatorColor);
        mIndicatorDrawable.setBounds(paddingLeft + (int) mIndicatorMarginLeft + mIndicatorRect.left,
                (int) mIndicatorMarginTop, (int) (paddingLeft + mIndicatorRect.right - mIndicatorMarginRight),
                (int) (mIndicatorMarginTop + mIndicatorHeight));
        mIndicatorDrawable.setCornerRadii(mRadiusArr);
        mIndicatorDrawable.draw(canvas);

    }

    public void setCurrentTab(int currentTab) {
        mLastTab = mCurrentTab;
        mCurrentTab = currentTab;
        updateTabSelection(currentTab);
        if (mIndicatorAnimEnable) {
            calcOffset();
        } else {
            invalidate();
        }
    }

    public void setTabPadding(float tabPadding) {
        mTabPadding = toPixel(tabPadding);
        updateTabStyles();
    }

    public void setTabSpaceEqual(boolean tabSpaceEqual) {
        mTabSpaceEqual = tabSpaceEqual;
        updateTabStyles();
    }

    public void setTabWidth(float tabWidth) {
        mTabWidth = toPixel(tabWidth);
        updateTabStyles();
    }

    public void setIndicatorColor(int indicatorColor) {
        mIndicatorColor = indicatorColor;
        invalidate();
    }

    public void setIndicatorHeight(float indicatorHeight) {
        mIndicatorHeight = toPixel(indicatorHeight);
        invalidate();
    }

    public void setIndicatorCornerRadius(float indicatorCornerRadius) {
        mIndicatorCornerRadius = toPixel(indicatorCornerRadius);
        invalidate();
    }

    public void setIndicatorMargin(float indicatorMarginLeft, float indicatorMarginTop,
                                   float indicatorMarginRight, float indicatorMarginBottom) {
        mIndicatorMarginLeft = toPixel(indicatorMarginLeft);
        mIndicatorMarginTop = toPixel(indicatorMarginTop);
        mIndicatorMarginRight = toPixel(indicatorMarginRight);
        mIndicatorMarginBottom = toPixel(indicatorMarginBottom);
        invalidate();
    }

    public void setIndicatorAnimDuration(long indicatorAnimDuration) {
        mIndicatorAnimDuration = indicatorAnimDuration;
    }

    public void setIndicatorAnimEnable(boolean indicatorAnimEnable) {
        mIndicatorAnimEnable = indicatorAnimEnable;
    }

    public void setIndicatorBounceEnable(boolean indicatorBounceEnable) {
        mIndicatorBounceEnable = indicatorBounceEnable;
    }

    public void setDividerColor(int dividerColor) {
        mDividerColor = dividerColor;
        invalidate();
    }

    public void setDividerWidth(float dividerWidth) {
        mDividerWidth = toPixel(dividerWidth);
        invalidate();
    }

    public void setDividerPadding(float dividerPadding) {
        mDividerPadding = toPixel(dividerPadding);
        invalidate();
    }

    public void setTextSize(float textSize) {
        mTextSize = toPixel(textSize);
        updateTabStyles();
    }

    public void setTextSelectedColor(int selectedColor) {
        mTextSelectedColor = selectedColor;
        updateTabStyles();
    }

    public void setTextUnselectedColor(int unselectedColor) {
        mTextUnselectedColor = unselectedColor;
        updateTabStyles();
    }

    public void setTextBold(int textBold) {
        mTextBold = textBold;
        updateTabStyles();
    }

    public void setTextAllCaps(boolean textAllCaps) {
        mTextAllCaps = textAllCaps;
        updateTabStyles();
    }

    public int getTabCount() {
        return mTabCount;
    }

    public int getCurrentTab() {
        return mCurrentTab;
    }

    public float getTabPadding() {
        return mTabPadding;
    }

    public boolean isTabSpaceEqual() {
        return mTabSpaceEqual;
    }

    public float getTabWidth() {
        return mTabWidth;
    }

    public int getIndicatorColor() {
        return mIndicatorColor;
    }

    public float getIndicatorHeight() {
        return mIndicatorHeight;
    }

    public float getIndicatorCornerRadius() {
        return mIndicatorCornerRadius;
    }

    public float getIndicatorMarginLeft() {
        return mIndicatorMarginLeft;
    }

    public float getIndicatorMarginTop() {
        return mIndicatorMarginTop;
    }

    public float getIndicatorMarginRight() {
        return mIndicatorMarginRight;
    }

    public float getIndicatorMarginBottom() {
        return mIndicatorMarginBottom;
    }

    public long getIndicatorAnimDuration() {
        return mIndicatorAnimDuration;
    }

    public boolean isIndicatorAnimEnable() {
        return mIndicatorAnimEnable;
    }

    public boolean isIndicatorBounceEnable() {
        return mIndicatorBounceEnable;
    }

    public int getDividerColor() {
        return mDividerColor;
    }

    public float getDividerWidth() {
        return mDividerWidth;
    }

    public float getDividerPadding() {
        return mDividerPadding;
    }

    public float getTextSize() {
        return mTextSize;
    }

    public int getTextSelectedColor() {
        return mTextSelectedColor;
    }

    public int getTextUnselectedColor() {
        return mTextUnselectedColor;
    }

    public int getTextBold() {
        return mTextBold;
    }

    public boolean isTextAllCaps() {
        return mTextAllCaps;
    }

    public TextView getTitleView(int tab) {
        View tabView = mTabContainer.getChildAt(tab);
        return tabView.findViewById(R.id.android_tab_title);
    }

    // ===========DotView（右上角原点部分）===========//

    private Paint mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private SparseBooleanArray mInitSetArray = new SparseBooleanArray();

    @Nullable
    public DotView getDotView(int position) {
        View view = mTabContainer.getChildAt(position);
        return view == null ? null : (DotView) view.findViewById(R.id.android_tab_dot);
    }

    /**
     * 显示未读消息
     * @param position 显示tab位置
     * @param num num小于等于0显示红点,num大于0显示数字
     */
    public void showDot(int position, int num) {
        DotView dotView = getDotView(position);
        if (dotView != null) {
            dotView.showNum(num);
            if (mInitSetArray.get(position)) {
                return;
            }
            setDotMargin(position, 2, 2);
            mInitSetArray.put(position, true);
        }
    }

    /**
     * 显示未读红点
     * @param position 显示tab位置
     */
    public void showDot(int position) {
        showDot(position, 0);
    }

    /**
     * 隐藏指定位置控件
     * @param position 显示tab位置
     */
    public void hideDot(int position) {
        DotView dotView = getDotView(position);
        if (dotView != null) {
            dotView.setVisibility(View.GONE);
        }
    }

    public void setDotMargin(int position, float left, float bottom) {
        DotView dotView = getDotView(position);
        if (dotView != null) {
            TextView titleView = ((View) dotView.getParent()).findViewById(R.id.android_tab_title);
            mTextPaint.setTextSize(mTextSize);
            mTextPaint.measureText(titleView.getText().toString());
            float textHeight = mTextPaint.descent() - mTextPaint.ascent();
            // 设置Margin
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) dotView.getLayoutParams();
            params.leftMargin = toPixel(left);
            params.topMargin = mHeight > 0 ? (int) (mHeight - textHeight) / 2 - toPixel(bottom) : toPixel(bottom);
            dotView.setLayoutParams(params);
        }
    }

    private List<OnTabSelectedListener> mListeners = new ArrayList<>();

    public void addOnTabSelectedListener(OnTabSelectedListener listener) {
        mListeners.add(listener);
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("instanceState", super.onSaveInstanceState());
        bundle.putInt("currentTab", mCurrentTab);
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            mCurrentTab = bundle.getInt("currentTab");
            state = bundle.getParcelable("instanceState");
            if (mCurrentTab != 0 && mTabContainer.getChildCount() > 0) {
                updateTabSelection(mCurrentTab);
            }
        }
        super.onRestoreInstanceState(state);
    }

    static class IndicatorPoint {
        public float left;
        public float right;
    }

    private IndicatorPoint mCurrentP = new IndicatorPoint();
    private IndicatorPoint mLastP = new IndicatorPoint();

    static class PointEvaluator implements TypeEvaluator<IndicatorPoint> {
        @Override
        public IndicatorPoint evaluate(float fraction, IndicatorPoint startValue, IndicatorPoint endValue) {
            float left = startValue.left + fraction * (endValue.left - startValue.left);
            float right = startValue.right + fraction * (endValue.right - startValue.right);
            IndicatorPoint point = new IndicatorPoint();
            point.left = left;
            point.right = right;
            return point;
        }
    }

    protected int toPixel(float dip) {
        float density = Resources.getSystem().getDisplayMetrics().density;
        return (int) Math.ceil(dip * density);
    }
}
