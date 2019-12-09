package com.cqray.android.widget;

import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.SparseBooleanArray;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;

import com.cqray.android.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 没有继承HorizontalScrollView不能滑动,对于ViewPager无依赖
 * @author LeiJue
 */
public class CommonTabLayout extends FrameLayout implements ValueAnimator.AnimatorUpdateListener {

    private Context mContext;
    private List<TabIconEntity> mTabEntities = new ArrayList<>();
    private LinearLayout mTabContainer;
    private int mCurrentTab;
    private int mLastTab;
    private int mTabCount;
    /** 用于绘制显示器 */
    private Rect mIndicatorRect = new Rect();
    private GradientDrawable mIndicatorDrawable = new GradientDrawable();

    private Paint mRectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mDividerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mTrianglePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Path mTrianglePath = new Path();
    private static final int STYLE_NORMAL = 0;
    private static final int STYLE_TRIANGLE = 1;
    private static final int STYLE_BLOCK = 2;
    private int mIndicatorStyle = STYLE_NORMAL;

    private float mTabPadding;
    private boolean mTabSpaceEqual;
    private float mTabWidth;

    /** indicator */
    private int mIndicatorColor;
    private float mIndicatorHeight;
    private float mIndicatorWidth;
    private float mIndicatorCornerRadius;
    private float mIndicatorMarginLeft;
    private float mIndicatorMarginTop;
    private float mIndicatorMarginRight;
    private float mIndicatorMarginBottom;
    private long mIndicatorAnimDuration;
    private boolean mIndicatorAnimEnable;
    private boolean mIndicatorBounceEnable;
    private int mIndicatorGravity;

    /** underline */
    private int mUnderlineColor;
    private float mUnderlineHeight;
    private int mUnderlineGravity;

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

    /** icon */
    private boolean mIconVisible;
    private int mIconGravity;
    private float mIconWidth;
    private float mIconHeight;
    private float mIconMargin;

    private int mHeight;

    /** anim */
    private ValueAnimator mValueAnimator;
    private OvershootInterpolator mInterpolator = new OvershootInterpolator(1.5f);


    public CommonTabLayout(Context context) {
        this(context, null, 0);
    }

    public CommonTabLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CommonTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // 重写onDraw方法,需要调用这个方法来清除flag
        setWillNotDraw(false);
        setClipChildren(false);
        setClipToPadding(false);

        // 添加Tab容器
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
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CommonTabLayout);

        mIndicatorStyle = ta.getInt(R.styleable.CommonTabLayout_tlIndicatorStyle, 0);
        mIndicatorColor = ta.getColor(R.styleable.CommonTabLayout_tlIndicatorColor, Color.parseColor(mIndicatorStyle == STYLE_BLOCK ? "#4B6A87" : "#ffffff"));
        mIndicatorHeight = ta.getDimension(R.styleable.CommonTabLayout_tlIndicatorHeight,
                toPixel(mIndicatorStyle == STYLE_TRIANGLE ? 4 : (mIndicatorStyle == STYLE_BLOCK ? -1 : 2)));
        mIndicatorWidth = ta.getDimension(R.styleable.CommonTabLayout_tlIndicatorWidth, toPixel(mIndicatorStyle == STYLE_TRIANGLE ? 10 : -1));
        mIndicatorCornerRadius = ta.getDimension(R.styleable.CommonTabLayout_tlIndicatorCornerRadius, toPixel(mIndicatorStyle == STYLE_BLOCK ? -1 : 0));
        mIndicatorMarginLeft = ta.getDimension(R.styleable.CommonTabLayout_tlIndicatorMarginLeft, toPixel(0));
        mIndicatorMarginTop = ta.getDimension(R.styleable.CommonTabLayout_tlIndicatorMarginTop, toPixel(mIndicatorStyle == STYLE_BLOCK ? 7 : 0));
        mIndicatorMarginRight = ta.getDimension(R.styleable.CommonTabLayout_tlIndicatorMarginRight, toPixel(0));
        mIndicatorMarginBottom = ta.getDimension(R.styleable.CommonTabLayout_tlIndicatorMarginBottom, toPixel(mIndicatorStyle == STYLE_BLOCK ? 7 : 0));
        mIndicatorAnimEnable = ta.getBoolean(R.styleable.CommonTabLayout_tlIndicatorAnimEnable, true);
        mIndicatorBounceEnable = ta.getBoolean(R.styleable.CommonTabLayout_tlIndicatorBounceEnable, true);
        mIndicatorAnimDuration = ta.getInt(R.styleable.CommonTabLayout_tlIndicatorAnimDuration, -1);
        mIndicatorGravity = ta.getInt(R.styleable.CommonTabLayout_tlIndicatorGravity, Gravity.BOTTOM);

        mUnderlineColor = ta.getColor(R.styleable.CommonTabLayout_tlUnderlineColor, Color.parseColor("#ffffff"));
        mUnderlineHeight = ta.getDimension(R.styleable.CommonTabLayout_tlUnderlineHeight, toPixel(0));
        mUnderlineGravity = ta.getInt(R.styleable.CommonTabLayout_tlUnderlineGravity, Gravity.BOTTOM);

        mDividerColor = ta.getColor(R.styleable.CommonTabLayout_tlDividerColor, Color.parseColor("#ffffff"));
        mDividerWidth = ta.getDimension(R.styleable.CommonTabLayout_tlDividerWidth, toPixel(0));
        mDividerPadding = ta.getDimension(R.styleable.CommonTabLayout_tlDividerPadding, toPixel(12));

        mTextSize = ta.getDimension(R.styleable.CommonTabLayout_tlTextSize, toPixel(13f));
        mTextSelectedColor = ta.getColor(R.styleable.CommonTabLayout_tlTextSelectedColor, Color.parseColor("#ffffff"));
        mTextUnselectedColor = ta.getColor(R.styleable.CommonTabLayout_tlTextUnselectedColor, Color.parseColor("#AAffffff"));
        mTextBold = ta.getInt(R.styleable.CommonTabLayout_tlTextBold, TEXT_BOLD_NONE);
        mTextAllCaps = ta.getBoolean(R.styleable.CommonTabLayout_tlTextAllCaps, false);

        mIconVisible = ta.getBoolean(R.styleable.CommonTabLayout_tlIconVisible, true);
        mIconGravity = ta.getInt(R.styleable.CommonTabLayout_tlIconGravity, Gravity.TOP);
        mIconWidth = ta.getDimension(R.styleable.CommonTabLayout_tlIconWidth, toPixel(0));
        mIconHeight = ta.getDimension(R.styleable.CommonTabLayout_tlIconHeight, toPixel(0));
        mIconMargin = ta.getDimension(R.styleable.CommonTabLayout_tlIconMargin, toPixel(2.5f));

        mTabSpaceEqual = ta.getBoolean(R.styleable.CommonTabLayout_tlTabSpaceEqual, true);
        mTabWidth = ta.getDimension(R.styleable.CommonTabLayout_tlTabWidth, toPixel(-1));
        mTabPadding = ta.getDimension(R.styleable.CommonTabLayout_tlTabPadding, mTabSpaceEqual || mTabWidth > 0 ? toPixel(0) : toPixel(10));

        ta.recycle();
    }

    public void setTabData(CharSequence... titles) {
        mTabEntities.clear();
        mTabEntities.addAll(convertToList(titles));
        notifyDataSetChanged();
    }

    public void setTabData(List<? extends TabEntity> tabEntities) {
        mTabEntities.clear();
        if (tabEntities != null) {
            for (final TabEntity entity : tabEntities) {
                if (entity instanceof TabIconEntity) {
                    mTabEntities.add((TabIconEntity) entity);
                } else {
                    TabIconEntity te = new TabIconEntity() {
                        @Override
                        public int getSelectedIcon() {
                            return 0;
                        }

                        @Override
                        public int getUnselectedIcon() {
                            return 0;
                        }

                        @Override
                        public CharSequence getTitle() {
                            return entity.getTitle();
                        }
                    };
                    mTabEntities.add(te);
                }
            }
        }
        notifyDataSetChanged();
    }

    /**
     * 更新数据
     */
    public void notifyDataSetChanged() {
        mTabContainer.removeAllViews();
        mTabCount = mTabEntities.size();
        View tabView;
        for (int i = 0; i < mTabCount; i++) {
            if (mIconGravity == GravityCompat.START) {
                tabView = View.inflate(mContext, R.layout.android_widget_layout_tab_left, null);
            } else if (mIconGravity == GravityCompat.END) {
                tabView = View.inflate(mContext, R.layout.android_widget_layout_tab_right, null);
            } else if (mIconGravity == Gravity.BOTTOM) {
                tabView = View.inflate(mContext, R.layout.android_widget_layout_tab_bottom, null);
            } else {
                tabView = View.inflate(mContext, R.layout.android_widget_layout_tab_top, null);
            }
            tabView.setTag(i);
            addTab(i, tabView);
        }
        updateTabStyles();
    }

    private List<TabIconEntity> convertToList(CharSequence [] titles) {
        List<TabIconEntity> tabEntities = new ArrayList<>();
        if (titles == null) {
            return tabEntities;
        }
        for (final CharSequence str : titles) {
            tabEntities.add(new TabIconEntity() {
                @Override
                public int getSelectedIcon() {
                    return 0;
                }

                @Override
                public int getUnselectedIcon() {
                    return 0;
                }

                @Override
                public CharSequence getTitle() {
                    return str;
                }
            });
        }
        return tabEntities;
    }

    /**
     * 创建并添加tab
     */
    private void addTab(final int position, View tabView) {
        TextView title = tabView.findViewById(R.id.android_tab_title);
        title.setText(mTabEntities.get(position).getTitle());
        ImageView icon = tabView.findViewById(R.id.android_tab_icon);
        icon.setImageResource(mTabEntities.get(position).getUnselectedIcon());
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
        LinearLayout.LayoutParams tabParams = mTabSpaceEqual ?
                new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT, 1.0f) :
                new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        if (mTabWidth > 0) {
            tabParams = new LinearLayout.LayoutParams((int) mTabWidth, LayoutParams.MATCH_PARENT);
        }
        mTabContainer.addView(tabView, position, tabParams);
    }

    /**
     * 更新Tab样式
     */
    private void updateTabStyles() {
        for (int i = 0; i < mTabCount; i++) {
            View tabView = mTabContainer.getChildAt(i);
            tabView.setPadding((int) mTabPadding, 0, (int) mTabPadding, 0);
            TextView title = tabView.findViewById(R.id.android_tab_title);
            title.setTextColor(i == mCurrentTab ? mTextSelectedColor : mTextUnselectedColor);
            title.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);
            if (mTextAllCaps) {
                title.setText(title.getText().toString().toUpperCase());
            }

            if (mTextBold == TEXT_BOLD_BOTH) {
                title.getPaint().setFakeBoldText(true);
            } else if (mTextBold == TEXT_BOLD_NONE) {
                title.getPaint().setFakeBoldText(false);
            }

            ImageView icon = tabView.findViewById(R.id.android_tab_icon);
            if (mIconVisible) {
                icon.setVisibility(View.VISIBLE);
                TabIconEntity tabEntity = mTabEntities.get(i);
                icon.setImageResource(i == mCurrentTab ? tabEntity.getSelectedIcon() : tabEntity.getUnselectedIcon());
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        mIconWidth <= 0 ? LinearLayout.LayoutParams.WRAP_CONTENT : (int) mIconWidth,
                        mIconHeight <= 0 ? LinearLayout.LayoutParams.WRAP_CONTENT : (int) mIconHeight);
                if (mIconGravity == GravityCompat.START) {
                    lp.rightMargin = (int) mIconMargin;
                } else if (mIconGravity == GravityCompat.END) {
                    lp.leftMargin = (int) mIconMargin;
                } else if (mIconGravity == Gravity.BOTTOM) {
                    lp.topMargin = (int) mIconMargin;
                } else {
                    lp.bottomMargin = (int) mIconMargin;
                }
                icon.setLayoutParams(lp);
            } else {
                icon.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 更新Tab选择
     */
    private void updateTabSelection(int position) {
        for (int i = 0; i < mTabCount; ++i) {
            View tabView = mTabContainer.getChildAt(i);
            final boolean isSelect = i == position;
            TextView title = tabView.findViewById(R.id.android_tab_title);
            title.setTextColor(isSelect ? mTextSelectedColor : mTextUnselectedColor);
            ImageView icon = tabView.findViewById(R.id.android_tab_icon);
            TabIconEntity tabEntity = mTabEntities.get(i);
            icon.setImageResource(isSelect ? tabEntity.getSelectedIcon() : tabEntity.getUnselectedIcon());
            if (mTextBold == TEXT_BOLD_WHEN_SELECT) {
                title.getPaint().setFakeBoldText(isSelect);
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
        if (mIndicatorWidth >= 0) {
            // indicatorWidth大于0时,圆角矩形以及三角形
            float indicatorLeft = currentTabView.getLeft() + (currentTabView.getWidth() - mIndicatorWidth) / 2;
            mIndicatorRect.left = (int) indicatorLeft;
            mIndicatorRect.right = (int) (mIndicatorRect.left + mIndicatorWidth);
        }
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        View currentTabView = mTabContainer.getChildAt(mCurrentTab);
        IndicatorPoint p = (IndicatorPoint) animation.getAnimatedValue();
        mIndicatorRect.left = (int) p.left;
        mIndicatorRect.right = (int) p.right;
        if (mIndicatorWidth >= 0) {
            //indicatorWidth大于0时,圆角矩形以及三角形
            float indicatorLeft = p.left + (currentTabView.getWidth() - mIndicatorWidth) / 2;
            mIndicatorRect.left = (int) indicatorLeft;
            mIndicatorRect.right = (int) (mIndicatorRect.left + mIndicatorWidth);
        }
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
        // draw divider
        if (mDividerWidth > 0) {
            mDividerPaint.setStrokeWidth(mDividerWidth);
            mDividerPaint.setColor(mDividerColor);
            for (int i = 0; i < mTabCount - 1; i++) {
                View tab = mTabContainer.getChildAt(i);
                canvas.drawLine(paddingLeft + tab.getRight(), mDividerPadding, paddingLeft + tab.getRight(), height - mDividerPadding, mDividerPaint);
            }
        }
        // draw underline
        if (mUnderlineHeight > 0) {
            mRectPaint.setColor(mUnderlineColor);
            if (mUnderlineGravity == Gravity.BOTTOM) {
                canvas.drawRect(paddingLeft, height - mUnderlineHeight, mTabContainer.getWidth() + paddingLeft, height, mRectPaint);
            } else {
                canvas.drawRect(paddingLeft, 0, mTabContainer.getWidth() + paddingLeft, mUnderlineHeight, mRectPaint);
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
        if (mIndicatorStyle == STYLE_TRIANGLE) {
            if (mIndicatorHeight > 0) {
                mTrianglePaint.setColor(mIndicatorColor);
                mTrianglePath.reset();
                mTrianglePath.moveTo(paddingLeft + mIndicatorRect.left, height);
                mTrianglePath.lineTo(paddingLeft + mIndicatorRect.left / 2 + mIndicatorRect.right / 2, height - mIndicatorHeight);
                mTrianglePath.lineTo(paddingLeft + mIndicatorRect.right, height);
                mTrianglePath.close();
                canvas.drawPath(mTrianglePath, mTrianglePaint);
            }
        } else if (mIndicatorStyle == STYLE_BLOCK) {
            if (mIndicatorHeight <= 0) {
                mIndicatorHeight = height - mIndicatorMarginTop - mIndicatorMarginBottom;
            } else {
                if (mIndicatorCornerRadius < 0 || mIndicatorCornerRadius > mIndicatorHeight / 2) {
                    mIndicatorCornerRadius = mIndicatorHeight / 2;
                }

                mIndicatorDrawable.setColor(mIndicatorColor);
                mIndicatorDrawable.setBounds(paddingLeft + (int) mIndicatorMarginLeft + mIndicatorRect.left,
                        (int) mIndicatorMarginTop, (int) (paddingLeft + mIndicatorRect.right - mIndicatorMarginRight),
                        (int) (mIndicatorMarginTop + mIndicatorHeight));
                mIndicatorDrawable.setCornerRadius(mIndicatorCornerRadius);
                mIndicatorDrawable.draw(canvas);
            }
        } else {
            if (mIndicatorHeight > 0) {
                mIndicatorDrawable.setColor(mIndicatorColor);
                if (mIndicatorGravity == Gravity.BOTTOM) {
                    mIndicatorDrawable.setBounds(paddingLeft + (int) mIndicatorMarginLeft + mIndicatorRect.left,
                            height - (int) mIndicatorHeight - (int) mIndicatorMarginBottom,
                            paddingLeft + mIndicatorRect.right - (int) mIndicatorMarginRight,
                            height - (int) mIndicatorMarginBottom);
                } else {
                    mIndicatorDrawable.setBounds(paddingLeft + (int) mIndicatorMarginLeft + mIndicatorRect.left,
                            (int) mIndicatorMarginTop,
                            paddingLeft + mIndicatorRect.right - (int) mIndicatorMarginRight,
                            (int) mIndicatorHeight + (int) mIndicatorMarginTop);
                }
                mIndicatorDrawable.setCornerRadius(mIndicatorCornerRadius);
                mIndicatorDrawable.draw(canvas);
            }
        }
    }

    /**
     * 设置当前选择项
     */
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

    public void setIndicatorStyle(int indicatorStyle) {
        mIndicatorStyle = indicatorStyle;
        invalidate();
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

    public void setIndicatorWidth(float indicatorWidth) {
        mIndicatorWidth = toPixel(indicatorWidth);
        invalidate();
    }

    public void setIndicatorCornerRadius(float indicatorCornerRadius) {
        mIndicatorCornerRadius = toPixel(indicatorCornerRadius);
        invalidate();
    }

    public void setIndicatorGravity(int indicatorGravity) {
        mIndicatorGravity = indicatorGravity;
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

    public void setUnderlineColor(int underlineColor) {
        mUnderlineColor = underlineColor;
        invalidate();
    }

    public void setUnderlineHeight(float underlineHeight) {
        mUnderlineHeight = toPixel(underlineHeight);
        invalidate();
    }

    public void setUnderlineGravity(int underlineGravity) {
        mUnderlineGravity = underlineGravity;
        invalidate();
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

    public void setIconVisible(boolean iconVisible) {
        mIconVisible = iconVisible;
        updateTabStyles();
    }

    public void setIconGravity(int iconGravity) {
        mIconGravity = iconGravity;
        notifyDataSetChanged();
    }

    public void setIconWidth(float iconWidth) {
        mIconWidth = toPixel(iconWidth);
        updateTabStyles();
    }

    public void setIconHeight(float iconHeight) {
        mIconHeight = toPixel(iconHeight);
        updateTabStyles();
    }

    public void setIconMargin(float iconMargin) {
        mIconMargin = toPixel(iconMargin);
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

    public int getIndicatorStyle() {
        return mIndicatorStyle;
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

    public float getIndicatorWidth() {
        return mIndicatorWidth;
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

    public int getUnderlineColor() {
        return mUnderlineColor;
    }

    public float getUnderlineHeight() {
        return mUnderlineHeight;
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

    public int getIconGravity() {
        return mIconGravity;
    }

    public float getIconWidth() {
        return mIconWidth;
    }

    public float getIconHeight() {
        return mIconHeight;
    }

    public float getIconMargin() {
        return mIconMargin;
    }

    public boolean isIconVisible() {
        return mIconVisible;
    }

    public ImageView getIconView(int index) {
        View tabView = mTabContainer.getChildAt(index);
        return tabView.findViewById(R.id.android_tab_icon);
    }

    public TextView getTitleView(int index) {
        View tabView = mTabContainer.getChildAt(index);
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
            if (!mIconVisible) {
                setDotMargin(position, 2, 2);
            } else {
                setDotMargin(position, 0, mIconGravity == GravityCompat.START || mIconGravity == GravityCompat.END ? 4 : 0);
            }
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

            float iconH = mIconHeight;
            float margin = 0;
            if (mIconVisible) {
                if (iconH <= 0) {
                    iconH = mContext.getResources().getDrawable(mTabEntities.get(position).getSelectedIcon()).getIntrinsicHeight();
                }
                margin = mIconMargin;
            }

            // 设置Margin
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) dotView.getLayoutParams();
            if (mIconGravity == Gravity.TOP || mIconGravity == Gravity.BOTTOM) {
                params.leftMargin = toPixel(left);
                params.topMargin = mHeight > 0 ? (int) (mHeight - textHeight - iconH - margin) / 2 - toPixel(bottom) : toPixel(bottom);
            } else {
                params.leftMargin = toPixel(left);
                params.topMargin = mHeight > 0 ? (int) (mHeight - Math.max(textHeight, iconH)) / 2 - toPixel(bottom) : toPixel(bottom);
            }
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
        float left;
        float right;
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
