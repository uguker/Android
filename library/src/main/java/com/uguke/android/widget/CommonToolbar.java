package com.uguke.android.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.IntDef;
import androidx.annotation.StringRes;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;

import com.uguke.android.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static android.util.TypedValue.COMPLEX_UNIT_PX;

/**
 * 标题栏
 * @author LeiJue
 */
public class CommonToolbar extends RelativeLayout {

    public static final int LEFT = GravityCompat.START | android.view.Gravity.CENTER_VERTICAL;
    public static final int CENTER = android.view.Gravity.CENTER;

    @IntDef({LEFT, CENTER})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Gravity {}

    private int mPaddingLeft;
    private int mPaddingRight;
    /** 行为间隔 **/
    private int mActionSpace;
    /** 标题间隔 **/
    private int mTitleSpace;
    /** 内部间隔 **/
    private int mBackSpace;
    /** 是否开始水波纹 **/
    private boolean mRippleEnable;
    /** 文本是否为粗体 **/
    private boolean [] mTextBold;
    /** 文本大小 **/
    private float [] mTextSizes;
    /** 文本颜色 **/
    private int [] mTextColors;
    private Context mContext;
    /** 标题 **/
    private TextView mTitle;
    /** 返回文本 **/
    private TextView mBackText;
    /** 行为文本 **/
    private TextView mActionText;
    /** 返回图标 **/
    private ImageView mBackIcon;
    /** 图标容器 **/
    private LinearLayout mContainer;
    /** 分割线 **/
    private View mDivider;
    /** Action控件 **/
    private LinkedHashMap<Integer, View> mActionViews;
    /** Action控件 **/
    private SparseBooleanArray mVisibleArray;

    public CommonToolbar(Context context) {
        this(context, null, 0);
    }

    public CommonToolbar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CommonToolbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;

        // 设置默认的标题ID
        if (getId() == NO_ID) {
            setId(R.id.__android_toolbar);
        }

        initViews();

        TypedArray ta = mContext.obtainStyledAttributes(attrs, R.styleable.CommonToolbar, defStyleAttr, R.style.CommonToolbarStyle);
        // 返回按钮
        Drawable backIcon = ta.getDrawable(R.styleable.CommonToolbar_tbBackIcon);
        // 文本信息
        String title = ta.getString(R.styleable.CommonToolbar_tbTitle);
        String back = ta.getString(R.styleable.CommonToolbar_tbBackText);
        mPaddingLeft = ta.getDimensionPixelOffset(R.styleable.CommonToolbar_tbPaddingLeft, mPaddingLeft);
        mPaddingRight = ta.getDimensionPixelOffset(R.styleable.CommonToolbar_tbPaddingRight, mPaddingRight);
        mTextBold[0] = ta.getBoolean(R.styleable.CommonToolbar_tbTitleTextBold, false);
        mTextBold[1] = ta.getBoolean(R.styleable.CommonToolbar_tbBackTextBold, false);
        mTextBold[2] = ta.getBoolean(R.styleable.CommonToolbar_tbActionTextBold, false);
        mTextColors[0] = ta.getColor(R.styleable.CommonToolbar_tbTitleTextColor, mTextColors[0]);
        mTextColors[1] = ta.getColor(R.styleable.CommonToolbar_tbBackTextColor, mTextColors[1]);
        mTextColors[2] = ta.getColor(R.styleable.CommonToolbar_tbActionTextColor, mTextColors[2]);
        mTextSizes[0] = ta.getDimension(R.styleable.CommonToolbar_tbTitleTextSize, mTextSizes[0]);
        mTextSizes[1] =  ta.getDimension(R.styleable.CommonToolbar_tbBackTextSize, mTextSizes[1]);
        mTextSizes[2] =  ta.getDimension(R.styleable.CommonToolbar_tbActionTextSize, mTextSizes[2]);
        // 获取间隔信息
        mBackSpace = ta.getDimensionPixelOffset(R.styleable.CommonToolbar_tbBackSpace, mBackSpace);
        mTitleSpace = ta.getDimensionPixelOffset(R.styleable.CommonToolbar_tbTitleSpace, mTitleSpace);
        mActionSpace = ta.getDimensionPixelOffset(R.styleable.CommonToolbar_tbActionSpace, mActionSpace);
        int gravity = ta.getInt(R.styleable.CommonToolbar_tbTitleGravity, CENTER);

        mRippleEnable = ta.getBoolean(R.styleable.CommonToolbar_tbRippleEnable, true);
        boolean backIconVisible = ta.getBoolean(R.styleable.CommonToolbar_tbBackIconVisible, false);
        boolean backTextVisible = ta.getBoolean(R.styleable.CommonToolbar_tbBackTextVisible, false);
        boolean dividerVisible = ta.getBoolean(R.styleable.CommonToolbar_tbDividerVisible, false);
        int dividerMargin = ta.getDimensionPixelOffset(R.styleable.CommonToolbar_tbDividerMargin, 0);
        int dividerHeight = ta.getDimensionPixelOffset(R.styleable.CommonToolbar_tbDividerHeight,
                getResources().getDimensionPixelSize(R.dimen.divider));
        int dividerColor = ta.getColor(R.styleable.CommonToolbar_tbDividerColor,
                ContextCompat.getColor(mContext, R.color.divider));
        ta.recycle();
        // 设置文本信息
        mTitle.setText(title);
        mBackText.setText(back);
        // 设置粗体
        mTitle.setTypeface(Typeface.defaultFromStyle(mTextBold[0] ? Typeface.BOLD : Typeface.NORMAL));
        mBackText.setTypeface(Typeface.defaultFromStyle(mTextBold[1] ? Typeface.BOLD : Typeface.NORMAL));
        // 设置图像
        mBackIcon.setImageDrawable(backIcon);
        mBackIcon.setVisibility(backIconVisible ? View.VISIBLE : View.GONE);
        mBackText.setVisibility(backTextVisible ? VISIBLE : GONE);
        mDivider.setVisibility(dividerVisible ? VISIBLE : GONE);
        mDivider.setBackgroundColor(dividerColor);
        mDivider.getLayoutParams().height = dividerHeight;
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) mDivider.getLayoutParams();
        params.setMargins(dividerMargin, 0, dividerMargin, 0);
        mContainer.setPadding(mTitleSpace - mActionSpace / 2, 0, mPaddingRight - mActionSpace / 2 , 0);
        // 设置文本大小
        mTitle.setTextSize(COMPLEX_UNIT_PX, mTextSizes[0]);
        mBackText.setTextSize(COMPLEX_UNIT_PX, mTextSizes[1]);
        mActionText.setTextSize(COMPLEX_UNIT_PX, mTextSizes[2]);
        // 设置文本颜色
        mTitle.setTextColor(mTextColors[0]);
        mBackText.setTextColor(mTextColors[1]);
        mActionText.setTextColor(mTextColors[2]);
        setRippleEnable(mRippleEnable);
        setTitleGravity(gravity);
    }

    @Override
    public void setPadding(int left, int top, int right, int bottom) {
        mPaddingLeft = left;
        mPaddingRight = right;
        // 设置间隔
        mContainer.setPadding(mTitleSpace - mActionSpace / 2, 0, mPaddingRight - mActionSpace / 2 , 0);
        refreshToolbar();
    }

    public CommonToolbar setPadding(float left, float right) {
        setPadding(toPixel(left), 0, toPixel(right), 0);
        return this;
    }

    @Deprecated
    @Override
    public void setPaddingRelative(int start, int top, int end, int bottom) {
        setPadding(start, top, end, bottom);
    }

    /**
     * 初始化控件
     */
    private void initViews() {
        mActionViews = new LinkedHashMap<>();
        mVisibleArray = new SparseBooleanArray();
        mRippleEnable = true;
        mTextBold = new boolean[3];
        mTextSizes = new float[3];
        mTextColors = new int[3];
        // 设置布局
        LayoutInflater.from(getContext()).inflate(R.layout.android_widget_layout_toolbar, this, true);
        // 获取控件
        mTitle = findViewById(R.id.__android_toolbar_title);
        mBackIcon = findViewById(R.id.__android_toolbar_back_icon);
        mBackText = findViewById(R.id.__android_toolbar_back_text);
        mActionText = findViewById(R.id.__android_toolbar_action_text);
        mDivider = findViewById(R.id.__android_toolbar_divider);
        mContainer = findViewById(R.id.__android_toolbar_action_container);
        // 设置背景
        ViewCompat.setBackground(mBackIcon, createItemBackground());
        ViewCompat.setBackground(mBackText, createItemBackground());
    }

    private Drawable createItemBackground() {
        TypedArray ta = mContext.obtainStyledAttributes(new int[] {
                android.R.attr.actionBarItemBackground });
        Drawable drawable = ta.getDrawable(0);
        ta.recycle();
        return drawable;
    }

    private void refreshToolbar() {
        boolean iconGone = mBackIcon.getVisibility() == GONE;
        boolean textGone = mBackText.getVisibility() == GONE;
        MarginLayoutParams params = (MarginLayoutParams) mTitle.getLayoutParams();
        mBackIcon.setPadding(
                Math.min(mBackSpace, mPaddingLeft), 0,
                Math.min(mBackSpace, mPaddingLeft), 0);
        mBackText.setPadding(0, 0, 0, 0);
        if (iconGone && textGone) {
            // 图标和文字都没显示
            params.leftMargin = mPaddingLeft;
        } else if (!iconGone && textGone) {
            // 图标显示文字不显示
            mBackIcon.setPadding(mPaddingLeft, 0, mPaddingLeft, 0);
            params.leftMargin = mTitle.getGravity() == LEFT ? mTitleSpace - mPaddingLeft : 0;
            // 图片间隔
            params = (MarginLayoutParams) mBackIcon.getLayoutParams();
            params.leftMargin = 0;
            params.rightMargin = 0;
        } else if (iconGone) {
            // 文字显示图标不显示
            params.leftMargin = mTitle.getGravity() == LEFT ? mTitleSpace : 0;
            // 返回文本间隔
            params = (MarginLayoutParams) mBackText.getLayoutParams();
            params.leftMargin = mPaddingLeft;
        } else {
            // 标题间隔
            params.leftMargin = mTitle.getGravity() == LEFT ? mTitleSpace : 0;
            // 返回图标间隔
            params = (MarginLayoutParams) mBackIcon.getLayoutParams();
            params.leftMargin = mPaddingLeft > mBackSpace ? mPaddingLeft - mBackSpace : 0;
            params.rightMargin = Math.min(mPaddingLeft - mBackSpace, 0);
        }
    }

    public CommonToolbar setTitleGravity(@Gravity int gravity) {
        if (mTitle.getGravity() != gravity) {
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(-1, -1);
            if (gravity == LEFT) {
                params.addRule(RelativeLayout.RIGHT_OF, R.id.__android_toolbar_back_container);
                params.addRule(RelativeLayout.LEFT_OF, R.id.__android_toolbar_action_container);
            }
            mTitle.setLayoutParams(params);
            mTitle.setGravity(gravity);
            refreshToolbar();
        }
        return this;
    }

    public CommonToolbar setRippleEnable(boolean rippleEnable) {
        if (mRippleEnable != rippleEnable) {
            mRippleEnable = rippleEnable;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && rippleEnable) {
                ViewCompat.setBackground(mBackIcon, createItemBackground());
                ViewCompat.setBackground(mBackText, createItemBackground());
                for (Map.Entry<Integer, View> entry : mActionViews.entrySet()) {
                    View view = entry.getValue();
                    ViewCompat.setBackground(view, createItemBackground());
                }

            } else {
                ViewCompat.setBackground(mBackIcon, null);
                ViewCompat.setBackground(mBackText, null);
                for (Map.Entry<Integer, View> entry : mActionViews.entrySet()) {
                    View view = entry.getValue();
                    ViewCompat.setBackground(view, null);
                }
            }
        }
        return this;
    }

    public CommonToolbar setBackSpace(float space) {
        mBackSpace = toPixel(space);
        refreshToolbar();
        return this;
    }

    public CommonToolbar setTitle(CharSequence title) {
        mTitle.setText(title);
        return this;
    }

    public CommonToolbar setTitle(int id) {
        mTitle.setText(id);
        return this;
    }

    public CommonToolbar setTitleTextColor(int color) {
        mTitle.setTextColor(color);
        return this;
    }

    public CommonToolbar setTitleTextSize(float size) {
        mTitle.setTextSize(size);
        return this;
    }

    public CommonToolbar setTitleTextBold(boolean bold) {
        mTitle.setTypeface(Typeface.defaultFromStyle(bold ? Typeface.BOLD : Typeface.NORMAL));
        return this;
    }

    public CommonToolbar setTitleSpace(float space) {
        mTitleSpace = toPixel(space);
        mContainer.setPadding(mTitleSpace - mActionSpace, 0, mPaddingRight - mActionSpace / 2 , 0);
        refreshToolbar();
        return this;
    }

    public CommonToolbar setBackIcon(@DrawableRes int resId) {
        mBackIcon.setImageResource(resId);
        return this;
    }

    public CommonToolbar setBackIcon(Drawable drawable) {
        mBackIcon.setImageDrawable(drawable);
        return this;
    }

    public CommonToolbar setBackIcon(Bitmap bitmap) {
        mBackIcon.setImageBitmap(bitmap);
        return this;
    }

    public CommonToolbar setBackIconVisible(boolean visible) {
        // 状态未变动
        if ((mBackIcon.getVisibility() == VISIBLE) != visible) {
            mBackIcon.setVisibility(visible ? VISIBLE : GONE);
        }
        return this;
    }

    public CommonToolbar setBackListener(View.OnClickListener listener) {
        mBackIcon.setOnClickListener(listener);
        mBackText.setOnClickListener(listener);
        return this;
    }

    public CommonToolbar setBackText(CharSequence text) {
        mBackText.setText(text);
        return this;
    }

    public CommonToolbar setBackText(@StringRes int id) {
        mBackText.setText(id);
        return this;
    }

    public CommonToolbar setBackTextColor(int color) {
        mBackText.setTextColor(color);
        return this;
    }

    public CommonToolbar setBackTextSize(float size) {
        mBackText.setTextSize(size);
        return this;
    }

    public CommonToolbar setBackTextBold(boolean bold) {
        mBackText.setTypeface(Typeface.defaultFromStyle(bold ? Typeface.BOLD : Typeface.NORMAL));
        return this;
    }

    public CommonToolbar setBackTextVisible(boolean visible) {
        // 状态未变动
        if ((mBackText.getVisibility() == VISIBLE) != visible) {
            mBackText.setVisibility(visible ? VISIBLE : GONE);
        }
        return this;
    }

    public CommonToolbar setActionText(int key, CharSequence text) {
        // 如果对应key有控件
        View view = mActionViews.get(key);
        if (view != null) {
            mActionViews.remove(key);
            mContainer.removeView(view);
        }
        ViewGroup.MarginLayoutParams params = new ViewGroup.MarginLayoutParams(-2, -1);
        params.leftMargin = mActionSpace / 2;
        params.rightMargin = mActionSpace / 2;
        TextView tv = new AppCompatTextView(mContext);
        tv.setText(text);
        tv.setTextSize(COMPLEX_UNIT_PX, mTextSizes[2]);
        tv.setTextColor(mTextColors[2]);
        tv.setGravity(android.view.Gravity.CENTER);
        tv.setLayoutParams(params);
        tv.setClickable(true);
        tv.setFocusable(true);
        tv.setVisibility(mVisibleArray.get(key) ? VISIBLE : GONE);
        tv.setTypeface(Typeface.defaultFromStyle(mTextBold[2] ? Typeface.BOLD : Typeface.NORMAL));
        if (mRippleEnable) {
            ViewCompat.setBackground(tv, createItemBackground());
        }
        mActionViews.put(key, tv);
        List<Map.Entry<Integer, View>> list = new ArrayList<>(mActionViews.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<Integer, View>>() {
            @Override
            public int compare(Map.Entry<Integer, View> o1, Map.Entry<Integer, View> o2) {
                return o1.getKey().compareTo(o2.getKey());
            }
        });
        int index = 0;
        mActionViews.clear();
        for (int i = 0; i < list.size(); i++) {
            Map.Entry<Integer, View> entry = list.get(i);
            if (key == entry.getKey()) {
                index = i;
            }
            mActionViews.put(entry.getKey(), entry.getValue());
        }
        mContainer.addView(tv, index);
        return this;
    }

    public CommonToolbar setActionText(int key, int id) {
        return setActionText(key, mContext.getString(id));
    }

    public CommonToolbar setActionTextColor(int color) {
        mTextColors[2] = color;
        for (Map.Entry<Integer, View> entry : mActionViews.entrySet()) {
            View view = entry.getValue();
            if (view instanceof TextView) {
                ((TextView) view).setTextColor(color);
            }
        }
        return this;
    }

    public CommonToolbar setActionTextColor(int key, int color) {
        View view = mActionViews.get(key);
        if (view != null) {
            if (view instanceof TextView) {
                ((TextView) view).setTextColor(color);
            }
        }
        return this;
    }

    public CommonToolbar setActionTextSize(float size) {
        for (Map.Entry<Integer, View> entry : mActionViews.entrySet()) {
            View view = entry.getValue();
            if (view instanceof TextView) {
                ((TextView) view).setTextSize(size);
            }
        }
        return this;
    }

    public CommonToolbar setActionTextSize(int key, float size) {
        View view = mActionViews.get(key);
        if (view != null) {
            if (view instanceof TextView) {
                ((TextView) view).setTextSize(size);
            }
        }
        return this;
    }

    public CommonToolbar setActionTextBold(boolean bold) {
        mTextBold[2] = bold;
        for (Map.Entry<Integer, View> entry : mActionViews.entrySet()) {
            View view = entry.getValue();
            if (view instanceof TextView) {
                ((TextView) view).setTypeface(Typeface.defaultFromStyle(bold ? Typeface.BOLD : Typeface.NORMAL));
            }
        }
        return this;
    }

    public CommonToolbar setActionTextBold(int key, boolean bold) {
        View view = mActionViews.get(key);
        if (view != null) {
            if (view instanceof TextView) {
                ((TextView) view).setTypeface(Typeface.defaultFromStyle(bold ? Typeface.BOLD : Typeface.NORMAL));
            }
        }
        return this;
    }

    public CommonToolbar setActionTextListener(int key, View.OnClickListener listener) {
        View view = mActionViews.get(key);
        if (view != null) {
            if (view instanceof TextView) {
                view.setOnClickListener(listener);
            }
        }
        return this;
    }

    public CommonToolbar setActionIcon(int key, @DrawableRes int resId) {
        // 如果对应key有控件
        View view = mActionViews.get(key);
        if (view != null) {
            mActionViews.remove(key);
            mContainer.removeView(view);
        }
        ViewGroup.MarginLayoutParams params = new ViewGroup.MarginLayoutParams(-2, -1);
        params.leftMargin = mActionSpace / 2;
        params.rightMargin = mActionSpace / 2;
        ImageView iv = new AppCompatImageView(mContext);
        iv.setImageResource(resId);
        iv.setLayoutParams(params);
        iv.setClickable(true);
        iv.setFocusable(true);
        iv.setVisibility(mVisibleArray.get(key) ? VISIBLE : GONE);
        if (mRippleEnable) {
            ViewCompat.setBackground(iv, createItemBackground());
        }
        mActionViews.put(key, iv);
        List<Map.Entry<Integer, View>> list = new ArrayList<>(mActionViews.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<Integer, View>>() {
            @Override
            public int compare(Map.Entry<Integer, View> o1, Map.Entry<Integer, View> o2) {
                return o1.getKey().compareTo(o2.getKey());
            }
        });

        int index = 0;
        mActionViews.clear();
        for (int i = 0; i < list.size(); i++) {
            Map.Entry<Integer, View> entry = list.get(i);
           if (key == entry.getKey()) {
               index = i;
           }
           mActionViews.put(entry.getKey(), entry.getValue());
        }
        mContainer.addView(iv, index);
        return this;
    }

    public CommonToolbar setActionVisible(int key, boolean visible) {
        mVisibleArray.put(key, visible);
        View view = mActionViews.get(key);
        if (view != null) {
            // 状态未变动
            if ((view.getVisibility() == VISIBLE) != visible) {
                view.setVisibility(visible ? VISIBLE : GONE);
            }
        }
        return this;
    }

    public CommonToolbar setActionSpace(float space) {
        mActionSpace = toPixel(space);
        for (Map.Entry<Integer, View> entry : mActionViews.entrySet()) {
            View view = entry.getValue();
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            params.leftMargin = mActionSpace / 2;
            params.rightMargin = mActionSpace / 2;
        }
        mContainer.setPadding(mTitleSpace - mActionSpace, 0, mPaddingRight - mActionSpace / 2 , 0);
        return this;
    }

    public CommonToolbar setActionIconListener(int key, View.OnClickListener listener) {
        View view = mActionViews.get(key);
        if (view != null) {
            if (view instanceof ImageView) {
                view.setOnClickListener(listener);
            }
        }
        return this;
    }

    public CommonToolbar setDividerColor(int color) {
        mDivider.setBackgroundColor(color);
        return this;
    }

    public CommonToolbar setDividerHeight(float height) {
        mDivider.getLayoutParams().height = toPixel(height);
        return this;
    }

    public CommonToolbar setDividerMargin(float left, float right) {
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) mDivider.getLayoutParams();
        params.leftMargin = toPixel(left);
        params.rightMargin = toPixel(right);
        return this;
    }

    public CommonToolbar setDividerVisible(boolean visible) {
        if ((mDivider.getVisibility() == VISIBLE) != visible) {
            mDivider.setVisibility(visible ? VISIBLE : GONE);
        }
        return this;
    }

    public TextView getTitleView() {
        return mTitle;
    }

    public ImageView getBackIconView() {
        return mBackIcon;
    }

    public TextView getBackTextView() {
        return mBackText;
    }

    @SuppressWarnings("unchecked")
    public <T> T getActionView(int key) {
        return (T) mActionViews.get(key);
    }

    private int toPixel(float dip) {
        float density = Resources.getSystem().getDisplayMetrics().density;
        return (int) Math.ceil(dip * density);
    }
}
