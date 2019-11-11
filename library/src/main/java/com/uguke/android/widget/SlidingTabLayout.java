package com.uguke.android.widget;

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
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.uguke.android.R;

import java.util.ArrayList;
import java.util.List;

/** 
 * 滑动TabLayout
 * @author LeiJue
 * */
public class SlidingTabLayout extends HorizontalScrollView implements ViewPager.OnPageChangeListener {
    private Context mContext;
    private ViewPager mViewPager;
    private List<TabEntity> mTabEntities = new ArrayList<>();
    private LinearLayout mTabContainer;
    private int mCurrentTab;
    private float mCurrentPositionOffset;
    private int mTabCount;
    /** 用于绘制显示器 */
    private Rect mIndicatorRect = new Rect();
    /** 用于实现滚动居中 */
    private Rect mTabRect = new Rect();
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
    private int mIndicatorGravity;
    private boolean mIndicatorWidthEqualTitle;

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

    private int mHeight;

    private int mLastScrollX;
    private boolean mSnapOnTabClick;

    public SlidingTabLayout(Context context) {
        this(context, null, 0);
    }

    public SlidingTabLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlidingTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //设置滚动视图是否可以伸缩其内容以填充视口
        setFillViewport(true);
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
    }

    /**
     * 获取属性值
     */
    private void obtainAttributes(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.SlidingTabLayout);

        mIndicatorStyle = ta.getInt(R.styleable.SlidingTabLayout_tlIndicatorStyle, STYLE_NORMAL);
        mIndicatorColor = ta.getColor(R.styleable.SlidingTabLayout_tlIndicatorColor, Color.parseColor(mIndicatorStyle == STYLE_BLOCK ? "#4B6A87" : "#ffffff"));
        mIndicatorHeight = ta.getDimension(R.styleable.SlidingTabLayout_tlIndicatorHeight,
                toPixel(mIndicatorStyle == STYLE_TRIANGLE ? 4 : (mIndicatorStyle == STYLE_BLOCK ? -1 : 2)));
        mIndicatorWidth = ta.getDimension(R.styleable.SlidingTabLayout_tlIndicatorWidth, toPixel(mIndicatorStyle == STYLE_TRIANGLE ? 10 : -1));
        mIndicatorCornerRadius = ta.getDimension(R.styleable.SlidingTabLayout_tlIndicatorCornerRadius, toPixel(mIndicatorStyle == STYLE_BLOCK ? -1 : 0));
        mIndicatorMarginLeft = ta.getDimension(R.styleable.SlidingTabLayout_tlIndicatorMarginLeft, toPixel(0));
        mIndicatorMarginTop = ta.getDimension(R.styleable.SlidingTabLayout_tlIndicatorMarginTop, toPixel(mIndicatorStyle == STYLE_BLOCK ? 7 : 0));
        mIndicatorMarginRight = ta.getDimension(R.styleable.SlidingTabLayout_tlIndicatorMarginRight, toPixel(0));
        mIndicatorMarginBottom = ta.getDimension(R.styleable.SlidingTabLayout_tlIndicatorMarginBottom, toPixel(mIndicatorStyle == STYLE_BLOCK ? 7 : 0));
        mIndicatorGravity = ta.getInt(R.styleable.SlidingTabLayout_tlIndicatorGravity, Gravity.BOTTOM);
        mIndicatorWidthEqualTitle = ta.getBoolean(R.styleable.SlidingTabLayout_tlIndicatorWidthEqualTitle, false);

        mUnderlineColor = ta.getColor(R.styleable.SlidingTabLayout_tlUnderlineColor, Color.parseColor("#ffffff"));
        mUnderlineHeight = ta.getDimension(R.styleable.SlidingTabLayout_tlUnderlineHeight, toPixel(0));
        mUnderlineGravity = ta.getInt(R.styleable.SlidingTabLayout_tlUnderlineGravity, Gravity.BOTTOM);

        mDividerColor = ta.getColor(R.styleable.SlidingTabLayout_tlDividerColor, Color.parseColor("#ffffff"));
        mDividerWidth = ta.getDimension(R.styleable.SlidingTabLayout_tlDividerWidth, toPixel(0));
        mDividerPadding = ta.getDimension(R.styleable.SlidingTabLayout_tlDividerPadding, toPixel(12));

        mTextSize = ta.getDimension(R.styleable.SlidingTabLayout_tlTextSize, toPixel(14));
        mTextSelectedColor = ta.getColor(R.styleable.SlidingTabLayout_tlTextSelectedColor, Color.parseColor("#ffffff"));
        mTextUnselectedColor = ta.getColor(R.styleable.SlidingTabLayout_tlTextUnselectedColor, Color.parseColor("#AAffffff"));
        mTextBold = ta.getInt(R.styleable.SlidingTabLayout_tlTextBold, TEXT_BOLD_NONE);
        mTextAllCaps = ta.getBoolean(R.styleable.SlidingTabLayout_tlTextAllCaps, false);

        mTabSpaceEqual = ta.getBoolean(R.styleable.SlidingTabLayout_tlTabSpaceEqual, false);
        mTabWidth = ta.getDimension(R.styleable.SlidingTabLayout_tlTabWidth, toPixel(-1));
        mTabPadding = ta.getDimension(R.styleable.SlidingTabLayout_tlTabPadding, mTabSpaceEqual || mTabWidth > 0 ? toPixel(0) : toPixel(20));

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
        mTabEntities.addAll(convertToList(titles));
        notifyDataSetChanged();
    }

    public void setViewPager(ViewPager pager) {
        if (pager == null || pager.getAdapter() == null) {
            throw new IllegalStateException("ViewPager or ViewPager's adapter can not be null !");
        }
        mViewPager = pager;
        mTabEntities.clear();
        mTabEntities.addAll(convertToList(pager.getAdapter()));
        mViewPager.removeOnPageChangeListener(this);
        mViewPager.addOnPageChangeListener(this);
        notifyDataSetChanged();
    }

    /** 关联ViewPager,用于不想在ViewPager适配器中设置titles数据的情况 */
    public void setViewPager(ViewPager pager, String[] titles) {
        if (pager == null || pager.getAdapter() == null) {
            throw new IllegalStateException("ViewPager or ViewPager's adapter can not be null !");
        }
        int count = titles == null ? 0 : titles.length;
        if (count != pager.getAdapter().getCount()) {
            throw new IllegalStateException("Titles length must be the same as the page count !");
        }
        mTabEntities.clear();
        mTabEntities.addAll(convertToList(titles));
        mViewPager = pager;
        mViewPager.removeOnPageChangeListener(this);
        mViewPager.addOnPageChangeListener(this);
        notifyDataSetChanged();
    }

    /** 关联ViewPager,用于不想在ViewPager适配器中设置titles数据的情况 */
    public void setViewPager(ViewPager pager, List<? extends TabEntity> tabEntities) {
        if (pager == null || pager.getAdapter() == null) {
            throw new IllegalStateException("ViewPager or ViewPager's adapter can not be null !");
        }
        int count = tabEntities == null ? 0 : tabEntities.size();
        if (count != pager.getAdapter().getCount()) {
            throw new IllegalStateException("Titles length must be the same as the page count !");
        }
        mTabEntities.clear();
        if (tabEntities != null) {
            mTabEntities.addAll(tabEntities);
        }
        mViewPager = pager;
        mViewPager.removeOnPageChangeListener(this);
        mViewPager.addOnPageChangeListener(this);
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
            tabView = View.inflate(mContext, R.layout.android_widget_layout_tab, null);
            CharSequence pageTitle = mTabEntities.get(i).getTitle();
            addTab(i, String.valueOf(pageTitle), tabView);
        }

        updateTabStyles();
    }

    public void addNewTab(final String title) {
        addNewTab(new TabEntity() {
            @Override
            public CharSequence getTitle() {
                return title;
            }
        });
    }

    public void addNewTab(@NonNull final TabEntity entity) {
        View tabView = View.inflate(mContext, R.layout.android_widget_layout_tab, null);
        mTabEntities.add(entity);
        CharSequence pageTitle = mTabEntities.get(mTabCount).getTitle();
        addTab(mTabCount, String.valueOf(pageTitle), tabView);
        mTabCount = mTabEntities.size();
        updateTabStyles();
    }

    private List<TabEntity> convertToList(CharSequence [] titles) {
        List<TabEntity> tabEntities = new ArrayList<>();
        if (titles == null) {
            return tabEntities;
        }
        for (final CharSequence str : titles) {
            tabEntities.add(new TabEntity() {
                @Override
                public CharSequence getTitle() {
                    return str;
                }
            });
        }
        return tabEntities;
    }

    private List<TabEntity> convertToList(final PagerAdapter adapter) {
        List<TabEntity> tabEntities = new ArrayList<>();
        int count = adapter.getCount();
        for (int i = 0; i < count; i++) {
            final int ii = i;
            tabEntities.add(new TabEntity() {
                @Override
                public CharSequence getTitle() {
                    return adapter.getPageTitle(ii);
                }
            });
        }
        return tabEntities;
    }

    /**
     * 创建并添加tab
     */
    private void addTab(final int position, String title, View tabView) {
        TextView titleView = tabView.findViewById(R.id.android_tab_title);
        if (titleView != null) {
            titleView.setText(title);
        }
        tabView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = mTabContainer.indexOfChild(v);
                if (position != -1) {
                    if (mCurrentTab != position) {
                        if (mSnapOnTabClick) {
                            setCurrentTab(position, false);
                            if (mViewPager == null) {
                                scrollToCurrentTab();
                                updateTabSelection(mCurrentTab);
                                invalidate();
                            }
                        } else {
                            setCurrentTab(position);
                            if (mViewPager == null) {
                                scrollToCurrentTab();
                                updateTabSelection(mCurrentTab);
                                invalidate();
                            }
                        }
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
            TextView titleView = tabView.findViewById(R.id.android_tab_title);
            if (titleView != null) {
                titleView.setTextColor(i == mCurrentTab ? mTextSelectedColor : mTextUnselectedColor);
                titleView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);
                titleView.setPadding((int) mTabPadding, 0, (int) mTabPadding, 0);
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
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        // position:当前View的位置
        // mCurrentPositionOffset:当前View的偏移量比例.[0,1)
        mCurrentTab = position;
        mCurrentPositionOffset = positionOffset;
        scrollToCurrentTab();
        invalidate();
    }

    @Override
    public void onPageSelected(int position) {
        updateTabSelection(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {}

    /**
     * HorizontalScrollView滚到当前tab,并且居中显示
     */
    private void scrollToCurrentTab() {
        if (mTabCount <= 0) {
            return;
        }
        int offset = (int) (mCurrentPositionOffset * mTabContainer.getChildAt(mCurrentTab).getWidth());
        // 当前Tab的left+当前Tab的Width乘以positionOffset
        int newScrollX = mTabContainer.getChildAt(mCurrentTab).getLeft() + offset;
        if (mCurrentTab > 0 || offset > 0) {
            // HorizontalScrollView移动到当前tab,并居中
            newScrollX -= getWidth() / 2 - getPaddingLeft();
            calcIndicatorRect();
            newScrollX += ((mTabRect.right - mTabRect.left) / 2);
        }

        if (newScrollX != mLastScrollX) {
            mLastScrollX = newScrollX;
            // scrollTo（int x,int y）:x,y代表的不是坐标点,而是偏移量
            // x:表示离起始位置的x水平方向的偏移量
            // y:表示离起始位置的y垂直方向的偏移量
            scrollTo(newScrollX, 0);
        }
    }

    /**
     * 更新选择项
     */
    private void updateTabSelection(int position) {
        for (int i = 0; i < mTabCount; ++i) {
            View tabView = mTabContainer.getChildAt(i);
            final boolean isSelect = i == position;
            TextView titleView = tabView.findViewById(R.id.android_tab_title);
            if (titleView != null) {
                titleView.setTextColor(isSelect ? mTextSelectedColor : mTextUnselectedColor);
                if (mTextBold == TEXT_BOLD_WHEN_SELECT) {
                    titleView.getPaint().setFakeBoldText(isSelect);
                }
            }
        }
    }

    private float mMargin;

    private void calcIndicatorRect() {
        View currentTabView = mTabContainer.getChildAt(mCurrentTab);
        float left = currentTabView.getLeft();
        float right = currentTabView.getRight();
        //for mIndicatorWidthEqualTitle
        if (mIndicatorStyle == STYLE_NORMAL && mIndicatorWidthEqualTitle) {
            TextView titleView = currentTabView.findViewById(R.id.android_tab_title);
            mTextPaint.setTextSize(mTextSize);
            float textWidth = mTextPaint.measureText(titleView.getText().toString());
            mMargin = (right - left - textWidth) / 2;
        }

        if (mCurrentTab < mTabCount - 1) {
            View nextTabView = mTabContainer.getChildAt(mCurrentTab + 1);
            float nextTabLeft = nextTabView.getLeft();
            float nextTabRight = nextTabView.getRight();

            left = left + mCurrentPositionOffset * (nextTabLeft - left);
            right = right + mCurrentPositionOffset * (nextTabRight - right);

            //for mIndicatorWidthEqualTitle
            if (mIndicatorStyle == STYLE_NORMAL && mIndicatorWidthEqualTitle) {
                TextView titleView = nextTabView.findViewById(R.id.android_tab_title);
                mTextPaint.setTextSize(mTextSize);
                float nextTextWidth = mTextPaint.measureText(titleView.getText().toString());
                float nextMargin = (nextTabRight - nextTabLeft - nextTextWidth) / 2;
                mMargin = mMargin + mCurrentPositionOffset * (nextMargin - mMargin);
            }
        }

        mIndicatorRect.left = (int) left;
        mIndicatorRect.right = (int) right;
        //for mIndicatorWidthEqualTitle
        if (mIndicatorStyle == STYLE_NORMAL && mIndicatorWidthEqualTitle) {
            mIndicatorRect.left = (int) (left + mMargin - 1);
            mIndicatorRect.right = (int) (right - mMargin - 1);
        }

        mTabRect.left = (int) left;
        mTabRect.right = (int) right;

        if (mIndicatorWidth >= 0) {
            //indicatorWidth大于0时,圆角矩形以及三角形
            float indicatorLeft = currentTabView.getLeft() + (currentTabView.getWidth() - mIndicatorWidth) / 2;

            if (mCurrentTab < mTabCount - 1) {
                View nextTab = mTabContainer.getChildAt(mCurrentTab + 1);
                indicatorLeft = indicatorLeft + mCurrentPositionOffset * (currentTabView.getWidth() + nextTab.getWidth()) / 2;
            }

            mIndicatorRect.left = (int) indicatorLeft;
            mIndicatorRect.right = (int) (mIndicatorRect.left + mIndicatorWidth);
        }
    }

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
        calcIndicatorRect();
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
            } else  {
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

    public void setCurrentTab(int currentTab) {
        mCurrentTab = currentTab;
        if (mViewPager != null) {
            mViewPager.setCurrentItem(currentTab);
        } else {
            updateTabSelection(currentTab);
        }
    }

    public void setCurrentTab(int currentTab, boolean smoothScroll) {
        mCurrentTab = currentTab;
        if (mViewPager != null) {
            mViewPager.setCurrentItem(currentTab, smoothScroll);
        } else {
            updateTabSelection(currentTab);
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

    public void setIndicatorWidthEqualTitle(boolean indicatorWidthEqualTitle) {
        mIndicatorWidthEqualTitle = indicatorWidthEqualTitle;
        invalidate();
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

    public void setTextSize(float size) {
        mTextSize = toPixel(size);
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

    public void setSnapOnTabClick(boolean snapOnTabClick) {
        mSnapOnTabClick = snapOnTabClick;
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
            setDotMargin(position, 4, 2);
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
            float textWidth = mTextPaint.measureText(titleView.getText().toString());
            float textHeight = mTextPaint.descent() - mTextPaint.ascent();
            // 设置Margin
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) dotView.getLayoutParams();
            params.leftMargin = mTabWidth >= 0 ? (int) (mTabWidth / 2 + textWidth / 2 + toPixel(left)) : (int) (mTabPadding + textWidth + toPixel(left));
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
                scrollToCurrentTab();
            }
        }
        super.onRestoreInstanceState(state);
    }

    protected int toPixel(float dip) {
        float density = Resources.getSystem().getDisplayMetrics().density;
        return (int) Math.ceil(dip * density);
    }
}
