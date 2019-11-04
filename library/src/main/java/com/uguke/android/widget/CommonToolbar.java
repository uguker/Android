package com.uguke.android.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
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

import com.uguke.android.util.ResUtils;
import com.uguke.android.util.ViewUtils;
import com.uguke.android.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 标题栏
 * @author LeiJue
 */
public class CommonToolbar extends RelativeLayout {

    public static final int START = GravityCompat.START | android.view.Gravity.CENTER_VERTICAL;
    public static final int CENTER = android.view.Gravity.CENTER;

    @IntDef({START, CENTER})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Gravity {}


    private int mPaddingLeft;

    private int mPaddingRight;
    /** 行为间隔 **/
    private float mActionMargin;
    /** 标题间隔 **/
    private float mTitleMargin;
    /** 内部间隔 **/
    private float mInnerSpace;
    /** 标题文本Gravity **/
    private int mTitleTextGravity;
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
    /** 点击事件监听 **/
    private View.OnClickListener mBackListener;

    public CommonToolbar(Context context) {
        super(context);
        mContext = context;
        initViews();
        obtainAttributes(null);
    }

    public CommonToolbar(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initViews();
        obtainAttributes(attrs);
    }

    public CommonToolbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initViews();
        obtainAttributes(attrs);
    }

    @Override
    public void setPadding(int left, int top, int right, int bottom) {
        mPaddingLeft = left;
        mPaddingRight = right;
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
        mRippleEnable = true;
        mTextBold = new boolean[3];
        mTextSizes = new float[3];
        mTextColors = new int[3];
        // 文字大小
        mTextSizes[0] = ResUtils.getDip(mContext, R.dimen.h1);
        mTextSizes[1] = ResUtils.getDip(mContext, R.dimen.h3);
        mTextSizes[2] = ResUtils.getDip(mContext, R.dimen.h3);
        // 颜色
        mTextColors[0] = ContextCompat.getColor(mContext, R.color.text);
        mTextColors[1] = ContextCompat.getColor(mContext, R.color.text);
        mTextColors[2] = ContextCompat.getColor(mContext, R.color.text);

        mPaddingLeft = ResUtils.getPixel(mContext, R.dimen.content);
        mPaddingRight = ResUtils.getPixel(mContext, R.dimen.content);
        // 间隔
        mTitleMargin = ResUtils.getDip(mContext, R.dimen.small);
        mActionMargin = ResUtils.getDip(mContext, R.dimen.smaller);
        LayoutInflater.from(getContext()).inflate(R.layout.android_widget_layout_toolbar, this, true);
        mTitle = findViewById(R.id.android_toolbar_title);
        mBackIcon = findViewById(R.id.android_toolbar_back_icon);
        mBackText = findViewById(R.id.android_toolbar_back_text);
        mActionText = findViewById(R.id.android_toolbar_action_text);
        mDivider = findViewById(R.id.android_toolbar_divider);
        mContainer = findViewById(R.id.android_toolbar_action_container);
        mBackIcon.setFocusable(true);
        mBackIcon.setClickable(true);
        ViewCompat.setBackground(mBackIcon, createItemBackground());
        mBackText.setFocusable(true);
        mBackText.setClickable(true);
        ViewCompat.setBackground(mBackText, createItemBackground());

    }

    private void obtainAttributes(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray ta = mContext.obtainStyledAttributes(attrs, R.styleable.CommonToolbar);
            // 返回按钮
            Drawable backIcon = ta.getDrawable(R.styleable.CommonToolbar_tb_backIcon);
            // 文本信息
            String title = ta.getString(R.styleable.CommonToolbar_tb_title);
            String back = ta.getString(R.styleable.CommonToolbar_tb_backText);
            mTextBold[0] = ta.getBoolean(R.styleable.CommonToolbar_tb_titleTextBold, false);
            mTextBold[1] = ta.getBoolean(R.styleable.CommonToolbar_tb_backTextBold, false);
            mTextBold[2] = ta.getBoolean(R.styleable.CommonToolbar_tb_actionTextBold, false);
            mTextColors[0] = ta.getColor(R.styleable.CommonToolbar_tb_titleTextColor, mTextColors[0]);
            mTextColors[1] = ta.getColor(R.styleable.CommonToolbar_tb_backTextColor, mTextColors[1]);
            mTextColors[2] = ta.getColor(R.styleable.CommonToolbar_tb_actionTextColor, mTextColors[2]);
            mTextSizes[0] = ResUtils.toDip(ta.getDimensionPixelOffset(R.styleable.CommonToolbar_tb_titleTextSize,
                    ResUtils.toPixel(mTextSizes[0])));
            mTextSizes[1] =  ResUtils.toDip(ta.getDimensionPixelOffset(R.styleable.CommonToolbar_tb_backTextSize,
                    ResUtils.toPixel(mTextSizes[1])));
            mTextSizes[2] =  ResUtils.toDip(ta.getDimensionPixelOffset(R.styleable.CommonToolbar_tb_actionTextSize,
                    ResUtils.toPixel(mTextSizes[2])));
            // 获取间隔信息
            mInnerSpace = ResUtils.toDip(ta.getDimensionPixelOffset(R.styleable.CommonToolbar_tb_actionMargin,
                    ResUtils.getPixel(mContext, R.dimen.content)));
            mTitleMargin = ResUtils.toDip(ta.getDimensionPixelOffset(R.styleable.CommonToolbar_tb_actionMargin,
                    ResUtils.toPixel(mTitleMargin)));
            mActionMargin = ResUtils.toDip(ta.getDimensionPixelOffset(R.styleable.CommonToolbar_tb_actionMargin,
                    ResUtils.toPixel(mActionMargin)));
            mTitleTextGravity = ta.getInt(R.styleable.CommonToolbar_tb_titleTextGravity, CENTER);

            boolean backIconVisible = ta.getBoolean(R.styleable.CommonToolbar_tb_backIconVisible, false);
            boolean backTextVisible = ta.getBoolean(R.styleable.CommonToolbar_tb_backTextVisible, false);
            mRippleEnable = ta.getBoolean(R.styleable.CommonToolbar_tb_rippleEnable, true);
            boolean dividerVisible = ta.getBoolean(R.styleable.CommonToolbar_tb_dividerVisible, false);
            int dividerMargin = ta.getDimensionPixelOffset(R.styleable.CommonToolbar_tb_dividerMargin, 0);
            int dividerHeight = ta.getDimensionPixelOffset(R.styleable.CommonToolbar_tb_dividerHeight,
                    ResUtils.getPixel(mContext, R.dimen.divider));
            int dividerColor = ta.getColor(R.styleable.CommonToolbar_tb_dividerColor,
                    ContextCompat.getColor(mContext, R.color.divider));
            ta.recycle();
            // 设置文本信息
            mTitle.setText(title);
            mBackText.setText(back);
            // 设置粗体
            ViewUtils.setTextBold(mTitle, mTextBold[0]);
            ViewUtils.setTextBold(mBackText, mTextBold[1]);
            // 设置图像
            mBackIcon.setImageDrawable(backIcon);
            mBackIcon.setVisibility(backIconVisible ? View.VISIBLE : View.GONE);
            mBackIcon.setOnClickListener(mBackListener);
            mBackText.setVisibility(backTextVisible ? VISIBLE : GONE);
            mDivider.setVisibility(dividerVisible ? VISIBLE : GONE);
            mDivider.setBackgroundColor(dividerColor);
            ViewUtils.setHeight(mDivider, dividerHeight);
            ViewUtils.setMargins(mDivider, dividerMargin, 0, dividerMargin, 0);
        }
        mContainer.setPadding(
                ResUtils.getPixel(mContext, R.dimen.content), 0,
                ResUtils.getPixel(mContext, R.dimen.content) - ResUtils.toPixel(mActionMargin) / 2 , 0);

        // 设置文本大小
        mTitle.setTextSize(mTextSizes[0]);
        mBackText.setTextSize(mTextSizes[1]);
        mActionText.setTextSize(mTextSizes[2]);
        // 设置文本颜色
        mTitle.setTextColor(mTextColors[0]);
        mBackText.setTextColor(mTextColors[1]);
        mActionText.setTextColor(mTextColors[2]);
        setTitleTextGravity(mTitleTextGravity);
        setRippleEnable(mRippleEnable);
        refreshToolbar();
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
        if (iconGone && textGone) {
            // 图标和文字都没显示
            mTitle.setPadding(
                    ResUtils.toPixel(mInnerSpace), 0,
                    ResUtils.toPixel(mInnerSpace), 0);
            params.leftMargin = 0;
        } else if (!iconGone && textGone) {
            // 文字显示图标不显示
            mBackIcon.setPadding(
                    ResUtils.toPixel(mInnerSpace), 0,
                    ResUtils.toPixel(mInnerSpace), 0);
            mTitle.setPadding(0, 0, 0, 0);
            int space = ResUtils.toPixel(mTitleMargin) -
                    ResUtils.toPixel(mInnerSpace);
            params.leftMargin = mTitle.getGravity() == START ? space : 0;
        } else if (iconGone) {
            // 图标显示文字不显示
            mBackText.setPadding(
                    ResUtils.toPixel(mInnerSpace), 0,
                    ResUtils.toPixel(mInnerSpace), 0);
            mTitle.setPadding(0, 0, 0, 0);
            int space = ResUtils.toPixel(mTitleMargin) -
                    ResUtils.toPixel(mInnerSpace);
            params.leftMargin = mTitle.getGravity() == START ? space : 0;
        } else {
            // 都显示
            mBackIcon.setPadding(
                    ResUtils.toPixel(mInnerSpace), 0,
                    ResUtils.toPixel(mInnerSpace), 0);
            mBackText.setPadding(0, 0, 0, 0);
            // 设置mTitle间隔
            params.leftMargin = mTitle.getGravity() == START ? ResUtils.toPixel(mTitleMargin) : 0;
            // 设置图标文字间的间隔
            params = (MarginLayoutParams) mBackText.getLayoutParams();
            params.leftMargin = -ResUtils.toPixel(mInnerSpace / 2);
        }
    }

    public CommonToolbar setTitleTextGravity(@Gravity int gravity) {
        if (mTitle.getGravity() != gravity) {
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(-1, -1);
            if (gravity == START) {
                params.addRule(RelativeLayout.RIGHT_OF, R.id.android_toolbar_back_container);
                params.addRule(RelativeLayout.LEFT_OF, R.id.android_toolbar_action_container);
            }
            mTitle.setLayoutParams(params);
            mTitle.setGravity(gravity);
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

    public CommonToolbar setInnerSpace(float space) {
        mInnerSpace = space;
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
        ViewUtils.setTextBold(mTitle, bold);
        return this;
    }

    public CommonToolbar setTitleMargin(float margin) {
        mTitleMargin = margin;
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
        mBackListener = listener;
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
        params.leftMargin = ResUtils.toPixel(mActionMargin) / 2;
        params.rightMargin = ResUtils.toPixel(mActionMargin) / 2;
        TextView tv = new AppCompatTextView(mContext);
        tv.setText(text);
        tv.setTextSize(mTextSizes[2]);
        tv.setTextColor(mTextColors[2]);
        tv.setGravity(android.view.Gravity.CENTER);
        tv.setLayoutParams(params);
        tv.setClickable(true);
        tv.setFocusable(true);
        ViewUtils.setTextBold(tv, mTextBold[2]);
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
        mTextSizes[2] = size;
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
                ViewUtils.setTextBold((TextView) view, bold);
            }
        }
        return this;
    }

    public CommonToolbar setActionTextBold(int key, boolean bold) {
        View view = mActionViews.get(key);
        if (view != null) {
            if (view instanceof TextView) {
                ViewUtils.setTextBold((TextView) view, bold);
            }
        }
        return this;
    }

    public CommonToolbar setActionTextVisible(int key, boolean visible) {
        View view = mActionViews.get(key);
        if (view != null) {
            if (view instanceof TextView && (view.getVisibility() == VISIBLE) != visible) {
                mActionText.setVisibility(visible ? VISIBLE : GONE);
            }
        }
        return this;
    }

    public CommonToolbar setActionTextVisible(boolean visible) {
        for (Map.Entry<Integer, View> entry : mActionViews.entrySet()) {
            View view = entry.getValue();
            if (view instanceof TextView && (view.getVisibility() == VISIBLE) != visible) {
                mActionText.setVisibility(visible ? VISIBLE : GONE);
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
        params.leftMargin = ResUtils.toPixel(mActionMargin) / 2;
        params.rightMargin = ResUtils.toPixel(mActionMargin) / 2;
        ImageView iv = new AppCompatImageView(mContext);
        iv.setImageResource(resId);
        iv.setLayoutParams(params);
        iv.setClickable(true);
        iv.setFocusable(true);
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

    public CommonToolbar setActionIconVisible(int key, boolean visible) {
        View view = mActionViews.get(key);
        if (view != null) {
            // 状态未变动
            if ((view.getVisibility() == VISIBLE) != visible) {
                view.setVisibility(visible ? VISIBLE : GONE);
            }
        }
        return this;
    }

    public CommonToolbar setActionMargin(float margin) {
        mActionMargin = margin;
        for (Map.Entry<Integer, View> entry : mActionViews.entrySet()) {
            View view = entry.getValue();
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            params.leftMargin = ResUtils.toPixel(margin) / 2;
            params.rightMargin = ResUtils.toPixel(margin) / 2;
        }
        mContainer.setPadding(ResUtils.getPixel(mContext, R.dimen.content), 0,
                mPaddingRight - ResUtils.toPixel(margin) / 2 , 0);
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
        ViewUtils.setHeight(mDivider, height);
        return this;
    }

    public CommonToolbar setDividerMargin(float left, float right) {
        ViewUtils.setMargins(mDivider, left, 0, right, 0);
        return this;
    }

    public CommonToolbar setDividerVisible(boolean visible) {
        if ((mDivider.getVisibility() == VISIBLE) != visible) {
            mDivider.setVisibility(visible ? VISIBLE : GONE);
        }
        return this;
    }

    public TextView getActionTextView() {
        return mActionText;
    }

    public TextView getTitleView() {
        return mTitle;
    }

    public ImageView getBackIconView() {
        return mBackIcon;
    }

    @SuppressWarnings("unchecked")
    public <T> T getActionView(int key) {
        return (T) mActionViews.get(key);
    }
}
