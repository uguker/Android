package com.uguke.android.widget;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;

import com.uguke.android.util.ResUtils;
import com.uguke.android.util.ViewUtils;
import com.uguke.android.R;

/**
 * 标题栏
 * @author LeiJue
 */
public class CommonToolbar extends RelativeLayout {

    /** 行为间隔 **/
    private float mActionSpace;
    /** 标题间隔 **/
    private float mTitleSpace;
    /** 内部间隔 **/
    private float mInnerSpace;
    /** 是否是常见模式 **/
    private boolean mMaterialStyle;
    /** 是否开始水波纹 **/
    private boolean mRippleEffect;
    /** 文本大小 **/
    private float [] mTextSizes;
    /** 文本颜色 **/
    private int [] mTextColors;
    /** 自定义图标 **/
    private Drawable mBackDrawable;
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
    /** 图标列表 **/
    private SparseArray<ImageView> mActionIcons;

    /** 点击事件监听 **/
    private View.OnClickListener mBackListener;
    /** 回退结束事件 **/
    private View.OnClickListener mFinishListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            Activity activity = ViewUtils.getActivity(v);
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
    };

    public CommonToolbar(Context context) {
        super(context);
        mContext = context;
        initViews();
        initAttrs(null);
    }

    public CommonToolbar(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initViews();
        initAttrs(attrs);
    }

    public CommonToolbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initViews();
        initAttrs(attrs);
    }

    /**
     * 初始化控件
     */
    private void initViews() {
        mActionIcons = new SparseArray<>();
        mRippleEffect = true;
        mMaterialStyle = false;
        mTextSizes = new float[3];
        mTextColors = new int[3];
        // 文字大小
        mTextSizes[0] = ResUtils.getDip(mContext, R.dimen.h1);
        mTextSizes[1] = ResUtils.getDip(mContext, R.dimen.h2);
        mTextSizes[2] = ResUtils.getDip(mContext, R.dimen.h2);
        // 颜色
        mTextColors[0] = ContextCompat.getColor(mContext, R.color.text);
        mTextColors[1] = ContextCompat.getColor(mContext, R.color.text);
        mTextColors[2] = ContextCompat.getColor(mContext, R.color.text);
        // 间隔
        mTitleSpace = ResUtils.getDip(mContext, R.dimen.small);
        mActionSpace = ResUtils.getDip(mContext, R.dimen.smaller);
        // 添加标题
        mTitle = new AppCompatTextView(mContext);
        mTitle.setId(R.id.android_toolbar_title);
        mTitle.setGravity(Gravity.CENTER);
        mTitle.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        addView(mTitle);
        // 初始化Back部分
        initBack();
        // 初始化Action部分
        initAction();
        // 添加分割线
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
                mContext.getResources().getDimensionPixelOffset(R.dimen.divider));
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        mDivider = new View(mContext);
        mDivider.setLayoutParams(params);
        mDivider.setBackgroundResource(R.color.divider);
        addView(mDivider);
    }

    private void initBack() {
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        // 添加Back容器
        RelativeLayout layout = new RelativeLayout(mContext);
        layout.setId(R.id.android_toolbar_back_container);
        layout.setLayoutParams(params);
        addView(layout);
        // 添加返回图标
        params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        mBackIcon = new AppCompatImageView(mContext);
        mBackIcon.setId(R.id.android_toolbar_back_icon);
        mBackIcon.setLayoutParams(params);
        mBackIcon.setFocusable(true);
        mBackIcon.setClickable(true);
        ViewCompat.setBackground(mBackIcon, createItemBackground());
        layout.addView(mBackIcon);
        // 添加返回文本
        params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        params.addRule(RIGHT_OF, R.id.android_toolbar_back_icon);
        mBackText = new AppCompatTextView(mContext);
        mBackText.setId(R.id.android_toolbar_back_text);
        mBackText.setLayoutParams(params);
        mBackText.setGravity(Gravity.CENTER_VERTICAL);
        mBackText.setFocusable(true);
        mBackText.setClickable(true);
        ViewCompat.setBackground(mBackText, createItemBackground());
        layout.addView(mBackText);
    }

    private void initAction() {
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        // 添加Action容器
        mContainer = new LinearLayout(mContext);
        mContainer.setClipChildren(false);
        mContainer.setId(R.id.android_toolbar_action_container);
        mContainer.setLayoutParams(params);
        mContainer.setOrientation(LinearLayout.HORIZONTAL);
        mContainer.setGravity(Gravity.CENTER_VERTICAL);
        mContainer.setPadding(0, 0, ResUtils.toPixel(16), 0);
        addView(mContainer);
        // 添加Action文本
        mActionText = new AppCompatTextView(mContext);
        mActionText.setId(R.id.android_toolbar_back_text);
        mActionText.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        mActionText.setGravity(Gravity.CENTER_VERTICAL);
        mActionText.setFocusable(true);
        mActionText.setClickable(true);
        ViewCompat.setBackground(mActionText, createItemBackground());
        mContainer.addView(mActionText);
    }

    @SuppressLint("CustomViewStyleable")
    private void initAttrs(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray ta = mContext.obtainStyledAttributes(attrs, R.styleable.CommonToolbar);
            // 返回按钮
            Drawable backIcon = ta.getDrawable(R.styleable.CommonToolbar_ct_backIcon);
            // 文本信息
            String title = ta.getString(R.styleable.CommonToolbar_ct_title);
            String back = ta.getString(R.styleable.CommonToolbar_ct_backText);
            String action = ta.getString(R.styleable.CommonToolbar_ct_actionText);
            boolean titleBold = ta.getBoolean(R.styleable.CommonToolbar_ct_titleTextBold, false);
            boolean backBold = ta.getBoolean(R.styleable.CommonToolbar_ct_backTextBold, false);
            boolean actionBold = ta.getBoolean(R.styleable.CommonToolbar_ct_actionTextBold, false);
            mTextColors[0] = ta.getColor(R.styleable.CommonToolbar_ct_titleTextColor, mTextColors[0]);
            mTextColors[1] = ta.getColor(R.styleable.CommonToolbar_ct_backTextColor, mTextColors[1]);
            mTextColors[2] = ta.getColor(R.styleable.CommonToolbar_ct_actionTextColor, mTextColors[2]);
            mTextSizes[0] = ResUtils.toDip(ta.getDimensionPixelOffset(R.styleable.CommonToolbar_ct_titleTextSize,
                    ResUtils.toPixel(mTextSizes[0])));
            mTextSizes[1] =  ResUtils.toDip(ta.getDimensionPixelOffset(R.styleable.CommonToolbar_ct_backTextSize,
                    ResUtils.toPixel(mTextSizes[1])));
            mTextSizes[2] =  ResUtils.toDip(ta.getDimensionPixelOffset(R.styleable.CommonToolbar_ct_actionTextSize,
                    ResUtils.toPixel(mTextSizes[2])));
            // 获取间隔信息
            mInnerSpace = ResUtils.toDip(ta.getDimensionPixelOffset(R.styleable.CommonToolbar_ct_actionSpace,
                    ResUtils.getPixel(mContext, R.dimen.content)));
            mTitleSpace = ResUtils.toDip(ta.getDimensionPixelOffset(R.styleable.CommonToolbar_ct_actionSpace,
                    ResUtils.toPixel(mTitleSpace)));
            mActionSpace = ResUtils.toDip(ta.getDimensionPixelOffset(R.styleable.CommonToolbar_ct_actionSpace,
                    ResUtils.toPixel(mActionSpace)));
            boolean backIconEnable = ta.getBoolean(R.styleable.CommonToolbar_ct_backIconEnable, true);
            boolean backIconVisible = ta.getBoolean(R.styleable.CommonToolbar_ct_backIconVisible, false);
            boolean backTextVisible = ta.getBoolean(R.styleable.CommonToolbar_ct_backTextVisible, false);
            boolean actionTextVisible = ta.getBoolean(R.styleable.CommonToolbar_ct_actionTextVisible, false);
            mRippleEffect = ta.getBoolean(R.styleable.CommonToolbar_ct_rippleEffect, true);
            mMaterialStyle = ta.getBoolean(R.styleable.CommonToolbar_ct_materialStyle, false);
            boolean dividerVisible = ta.getBoolean(R.styleable.CommonToolbar_ct_dividerVisible, false);
            int dividerSpace = ta.getDimensionPixelOffset(R.styleable.CommonToolbar_ct_dividerSpace, 0);
            int dividerHeight = ta.getDimensionPixelOffset(R.styleable.CommonToolbar_ct_dividerHeight,
                    ResUtils.getPixel(mContext, R.dimen.divider));
            int dividerColor = ta.getColor(R.styleable.CommonToolbar_ct_dividerColor,
                    ContextCompat.getColor(mContext, R.color.divider));
            ta.recycle();
            // 设置文本信息
            mTitle.setText(title);
            mBackText.setText(back);
            mActionText.setText(action);
            // 设置粗体
            ViewUtils.setTextBold(mTitle, titleBold);
            ViewUtils.setTextBold(mBackText, backBold);
            ViewUtils.setTextBold(mActionText, actionBold);
            // 设置图像
            mBackIcon.setImageDrawable(backIcon);
            mBackIcon.setVisibility(backIconVisible ? View.VISIBLE : View.GONE);
            mBackIcon.setOnClickListener(backIconEnable ? mFinishListener : mBackListener);
            mBackText.setVisibility(backTextVisible ? VISIBLE : GONE);
            mActionText.setVisibility(actionTextVisible ? VISIBLE : GONE);
            mDivider.setVisibility(dividerVisible ? VISIBLE : GONE);
            mDivider.setBackgroundColor(dividerColor);
            ViewUtils.setHeight(mDivider, dividerHeight);
            ViewUtils.setMargins(mDivider, dividerSpace, 0, dividerSpace, 0);
        }
        // 设置文本大小
        mTitle.setTextSize(mTextSizes[0]);
        mBackText.setTextSize(mTextSizes[1]);
        mActionText.setTextSize(mTextSizes[2]);
        // 设置文本颜色
        mTitle.setTextColor(mTextColors[0]);
        mBackText.setTextColor(mTextColors[1]);
        mActionText.setTextColor(mTextColors[2]);
        setMaterialStyle(mMaterialStyle);
        setRippleEffect(mRippleEffect);
        setActionSpace(mActionSpace);
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
            int space = ResUtils.toPixel(mTitleSpace) -
                    ResUtils.toPixel(mInnerSpace);
            params.leftMargin = mMaterialStyle ? space : 0;
        } else if (iconGone) {
            // 图标显示文字不显示
            mBackText.setPadding(
                    ResUtils.toPixel(mInnerSpace), 0,
                    ResUtils.toPixel(mInnerSpace), 0);
            mTitle.setPadding(0, 0, 0, 0);
            int space = ResUtils.toPixel(mTitleSpace) -
                    ResUtils.toPixel(mInnerSpace);
            params.leftMargin = mMaterialStyle ? space : 0;
        } else {
            // 都显示
            mBackIcon.setPadding(
                    ResUtils.toPixel(mInnerSpace), 0,
                    ResUtils.toPixel(mInnerSpace), 0);
            mBackText.setPadding(0, 0, 0, 0);
            // 设置mTitle间隔
            params.leftMargin = mMaterialStyle ? ResUtils.toPixel(mTitleSpace) : 0;
            // 设置图标文字间的间隔
            params = (MarginLayoutParams) mBackText.getLayoutParams();
            params.leftMargin = -ResUtils.toPixel(mInnerSpace / 2);
        }
    }

    public CommonToolbar setMaterialStyle(boolean materialStyle) {
        // 状态没做变化则不做处理
        if (mMaterialStyle == materialStyle) {
            return this;
        }
        mMaterialStyle = materialStyle;
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
        if (mMaterialStyle) {
            params.addRule(RelativeLayout.RIGHT_OF, R.id.android_toolbar_back_container);
            params.addRule(RelativeLayout.LEFT_OF, R.id.android_toolbar_action_container);
        }
        mBackIcon.setImageDrawable(mBackDrawable == null ? materialStyle ?
                ContextCompat.getDrawable(mContext, R.drawable.def_back_material_dark) :
                ContextCompat.getDrawable(mContext, R.drawable.def_back_common_dark) : mBackDrawable);
        mTitle.setLayoutParams(params);
        mTitle.setGravity(mMaterialStyle ? Gravity.CENTER_VERTICAL : Gravity.CENTER);
        return this;
    }

    public CommonToolbar setRippleEffect(boolean rippleEffect) {
        // 状态没做变化则不做处理
        if (mRippleEffect == rippleEffect) {
            return this;
        }
        mRippleEffect = rippleEffect;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && rippleEffect) {
            ViewCompat.setBackground(mBackIcon, createItemBackground());
            ViewCompat.setBackground(mBackText, createItemBackground());
            ViewCompat.setBackground(mActionText, createItemBackground());
            for (int i = 0; i < mActionIcons.size(); i ++) {
                ImageView view = mActionIcons.valueAt(i);
                ViewCompat.setBackground(view, createItemBackground());
            }

        } else {
            ViewCompat.setBackground(mBackIcon, null);
            ViewCompat.setBackground(mBackText, null);
            ViewCompat.setBackground(mActionText, null);
            for (int i = 0; i < mActionIcons.size(); i ++) {
                ImageView view = mActionIcons.valueAt(i);
                ViewCompat.setBackground(view, null);
            }
        }
        return this;
    }

    public CommonToolbar setInnerSpace(float space) {
        mInnerSpace = space;
        refreshToolbar();
        return this;
    }

    /////////////////////////
    // 标题部分
    /////////////////////////

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
        mTitle.setTypeface(Typeface.defaultFromStyle(bold ?
                Typeface.BOLD : Typeface.NORMAL));
        return this;
    }

    public CommonToolbar setTitleSpace(float space) {
        mTitleSpace = space;
        refreshToolbar();
        return this;
    }

    /////////////////////////
    // Back部分
    /////////////////////////

    public CommonToolbar setBackIcon(@DrawableRes int resId) {
        mBackDrawable = ContextCompat.getDrawable(mContext, resId);
        mBackIcon.setImageResource(resId);
        return this;
    }

    public CommonToolbar setBackIcon(Drawable drawable) {
        mBackDrawable = drawable;
        mBackIcon.setImageDrawable(drawable);
        return this;
    }

    public CommonToolbar setBackIcon(Bitmap bitmap) {
        mBackDrawable = new BitmapDrawable(getResources(), bitmap);
        mBackIcon.setImageBitmap(bitmap);
        return this;
    }

    public CommonToolbar setBackIconVisible(boolean visible) {
        // 状态未变动
        if ((mBackIcon.getVisibility() == VISIBLE) == visible) {
            return this;
        }
        mBackIcon.setVisibility(visible ? VISIBLE : GONE);
        refreshToolbar();
        return this;
    }

    public CommonToolbar setBackIconEnable(boolean enable) {
        mBackIcon.setOnClickListener(enable ? mFinishListener : mBackListener);
        mBackIcon.setTag(enable);
        return this;
    }

    public CommonToolbar setBackIconListener(View.OnClickListener listener) {
        mBackListener = listener;
        Boolean enable = (Boolean) mBackIcon.getTag();
        setBackIconEnable(enable == null ? true : enable);
        return this;
    }

    public CommonToolbar setFinishListener(View.OnClickListener listener) {
        mFinishListener = listener;
        Boolean enable = (Boolean) mBackIcon.getTag();
        setBackIconEnable(enable == null ? true : enable);
        return this;
    }

    public CommonToolbar setBackText(CharSequence text) {
        mBackText.setText(text);
        return this;
    }

    public CommonToolbar setBackText(int id) {
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
        mBackText.setTypeface(Typeface.defaultFromStyle(bold ?
                Typeface.BOLD : Typeface.NORMAL));
        return this;
    }

    public CommonToolbar setBackTextVisible(boolean visible) {
        // 状态未变动
        if ((mBackText.getVisibility() == VISIBLE) == visible) {
            return this;
        }
        mBackText.setVisibility(visible ? VISIBLE : GONE);
        refreshToolbar();
        return this;
    }

    public CommonToolbar setBackTextListener(View.OnClickListener listener) {
        mBackText.setOnClickListener(listener);
        return this;
    }

    /////////////////////////
    // Action部分
    /////////////////////////

    public CommonToolbar setActionText(CharSequence text) {
        mActionText.setText(text);
        return this;
    }

    public CommonToolbar setActionText(int id) {
        mActionText.setText(id);
        return this;
    }

    public CommonToolbar setActionTextColor(int color) {
        mActionText.setTextColor(color);
        return this;
    }

    public CommonToolbar setActionTextSize(float size) {
        mActionText.setTextSize(size);
        return this;
    }

    public CommonToolbar setActionTextBold(boolean bold) {
        mActionText.setTypeface(Typeface.defaultFromStyle(bold ?
                Typeface.BOLD : Typeface.NORMAL));
        return this;
    }

    public CommonToolbar setActionTextVisible(boolean visible) {
        // 状态未变动
        if ((mActionText.getVisibility() == VISIBLE) == visible) {
            return this;
        }
        mActionText.setVisibility(visible ? VISIBLE : GONE);
        setActionSpace(mActionSpace);
        return this;
    }

    public CommonToolbar setActionTextListener(View.OnClickListener listener) {
        mActionText.setOnClickListener(listener);
        return this;
    }

    public CommonToolbar addActionIcon(int key, @DrawableRes int resId) {
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        ImageView view = new AppCompatImageView(mContext);
        view.setImageResource(resId);
        view.setLayoutParams(params);
        view.setClickable(true);
        view.setFocusable(true);
        if (mRippleEffect) {
            ViewCompat.setBackground(view, createItemBackground());
        }
        mActionIcons.put(key, view);
        mContainer.addView(view, 0);
        setActionSpace(mActionSpace);
        return this;
    }

    public CommonToolbar setActionIconVisble(int key, boolean visible) {
        ImageView view = mActionIcons.get(key);
        if (view != null) {
            // 状态未变动
            if ((view.getVisibility() == VISIBLE) == visible) {
                return this;
            }
            view.setVisibility(visible ? VISIBLE : GONE);
            setActionSpace(mActionSpace);
        }
        return this;
    }

    public CommonToolbar setActionSpace(float space) {
        mActionSpace = space;
        // 文字是否已显示
        boolean textVisible = mActionText.getVisibility() == VISIBLE;
        // 图标是否已显示
        boolean iconVisible = false;
        for(int i = 0; i < mActionIcons.size(); i++) {
            View view = mActionIcons.valueAt(i);
            if (view.getVisibility() == VISIBLE) {
                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
                params.leftMargin = ResUtils.toPixel(space);
                params.rightMargin = iconVisible ? 0 : (textVisible ? ResUtils.toPixel(space) : 0);
                iconVisible = true;
            }
        }
        return this;
    }

    public CommonToolbar setActionIconListener(int key, View.OnClickListener listener) {
        ImageView view = mActionIcons.get(key);
        if (view != null) {
            view.setOnClickListener(listener);
        }
        return this;
    }

    /////////////////////////
    // 分割线部分
    /////////////////////////

    public CommonToolbar setDividerColor(int color) {
        mDivider.setBackgroundColor(color);
        return this;
    }

    public CommonToolbar setDividerHeight(float height) {
        ViewUtils.setHeight(mDivider, height);
        return this;
    }

    public CommonToolbar setDividerSpace(float space) {
        ViewUtils.setMargins(mDivider, space, 0, space, 0);
        return this;
    }

    public CommonToolbar setDividerVisible(boolean visible) {
        // 状态未改变
        if ((mDivider.getVisibility() == VISIBLE) == visible) {
            return this;
        }
        mDivider.setVisibility(visible ? VISIBLE : GONE);
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

    public ImageView getActionIconView(int key) {
        return mActionIcons.get(key);
    }

    public interface Handler {
        void onHandle(CommonToolbar toolbar);
    }
}
