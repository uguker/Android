package com.cqray.android.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;

import com.cqray.android.R;

/**
 * 多状态布局
 * @author LeiJue
 */
public class LoadingLayout extends RelativeLayout {

    /**  **/
    public static final int EMPTY_RES_ID = R.layout.android_widget_layout_loading_empty;
    public static final int ERROR_RES_ID =  R.layout.android_widget_layout_loading_error;
    public static final int LOADING_RES_ID =  R.layout.android_widget_layout_loading_;
    
    private int mEmptyImage;
    private int mErrorImage;

    private CharSequence mEmptyText;
    private CharSequence mErrorText;
    private CharSequence mRetryText;
    private CharSequence mLoadingText;

    private int mTextColor;
    private int mTextSize;
    private int mRetryTextColor;
    private int mRetryTextSize;
    private Drawable mBackground;
    private Drawable mRetryBackground;

    private int mEmptyMargin;
    private int mErrorMargin;
    private int mRetryMargin;
    private int mLoadingMargin;

    private int mLoadingSize;
    private int [] mLoadingColors;
    private boolean mLoadingVertical;

    /** 子控件是否可点击 **/
    private boolean mChildClickable;
    private View mMaskView;
    private SparseArray<View> mLayouts = new SparseArray<>();
    private View.OnClickListener mRetryListener;

    public LoadingLayout(Context context) {
        this(context, null, 0);
    }

    public LoadingLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        if (getId() == NO_ID) {
            setId(R.id.__android_loading);
        }

        int color = ContextCompat.getColor(context, R.color.tint);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.LoadingLayout, defStyleAttr, R.style.LoadingLayoutStyle);

        mBackground = a.getDrawable(R.styleable.LoadingLayout_llBackground);
        mTextColor = a.getColor(R.styleable.LoadingLayout_llTextColor, color);
        mTextSize = a.getDimensionPixelSize(R.styleable.LoadingLayout_llTextSize, mTextSize);
        
        mEmptyImage = a.getResourceId(R.styleable.LoadingLayout_llEmptyImage, R.drawable.widget_ic_empty);
        mEmptyText = a.getString(R.styleable.LoadingLayout_llEmptyText);
        mEmptyMargin = a.getDimensionPixelSize(R.styleable.LoadingLayout_llEmptyMargin, mEmptyMargin);
        // 错误信息
        mErrorMargin = a.getDimensionPixelSize(R.styleable.LoadingLayout_llErrorMargin, mErrorMargin);
        mErrorImage = a.getResourceId(R.styleable.LoadingLayout_llErrorImage, R.drawable.widget_ic_error);
        mErrorText = a.getString(R.styleable.LoadingLayout_llErrorText);
        // 重试按钮
        mRetryMargin = a.getDimensionPixelSize(R.styleable.LoadingLayout_llRetryMargin, mRetryMargin);
        mRetryText = a.getString(R.styleable.LoadingLayout_llRetryText);
        mRetryTextColor = a.getColor(R.styleable.LoadingLayout_llRetryTextColor, color);
        mRetryTextSize = a.getDimensionPixelSize(R.styleable.LoadingLayout_llRetryTextSize, mRetryTextSize);
        mRetryBackground = a.getDrawable(R.styleable.LoadingLayout_llRetryBackground);

        color = a.getColor(R.styleable.LoadingLayout_llLoadingColor, ContextCompat.getColor(getContext(), R.color.colorPrimary));
        mLoadingText = a.getString(R.styleable.LoadingLayout_llLoadingText);
        mLoadingSize = a.getDimensionPixelSize(R.styleable.LoadingLayout_llLoadingSize, mLoadingSize);
        mLoadingVertical = a.getInt(R.styleable.LoadingLayout_llLoadingOrientation, 0) == 0;
        mLoadingMargin = a.getDimensionPixelSize(R.styleable.LoadingLayout_llLoadingMargin, mLoadingMargin);
        mLoadingColors = new int[] {color};

        a.recycle();
    }

    public void setChildClickable(boolean clickable) {
        mChildClickable = clickable;
        if (mMaskView == null) {
            mMaskView = new FrameLayout(getContext());
            mMaskView.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
            mMaskView.setClickable(true);
            mMaskView.setFocusable(true);
            addView(mMaskView);
        }
        mMaskView.setVisibility(clickable ? GONE : VISIBLE);
        mMaskView.bringToFront();
    }

    private void initLoadingLayout() {
        LinearLayout layout = (LinearLayout) LayoutInflater.from(getContext()).inflate(LOADING_RES_ID, this, false);
        ViewCompat.setBackground(layout, mBackground);
        layout.setOrientation(mLoadingVertical ? LinearLayout.VERTICAL : LinearLayout.HORIZONTAL);
        LoadingView lv = layout.findViewById(R.id.__android_loading_loading_view);
        lv.getLayoutParams().width = mLoadingSize;
        lv.getLayoutParams().height = mLoadingSize;
        lv.setArcColors(mLoadingColors);
        TextView tv = layout.findViewById(R.id.__android_loading_loading_text);
        tv.setText(mLoadingText);
        tv.setTextColor(mTextColor);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);
        ViewGroup.MarginLayoutParams params = (MarginLayoutParams) tv.getLayoutParams();
        params.setMargins(mLoadingVertical ? 0 : mLoadingMargin, mLoadingVertical ? mLoadingMargin : 0, 0, 0);
        mLayouts.put(LOADING_RES_ID, layout);
        addView(layout);
    }

    private void initErrorLayout() {
        View layout = LayoutInflater.from(getContext()).inflate(ERROR_RES_ID, this, false);
        ViewCompat.setBackground(layout, mBackground);
        ImageView iv = layout.findViewById(R.id.__android_loading_error_img);
        iv.setImageResource(mErrorImage);
        TextView tv = layout.findViewById(R.id.__android_loading_error_text);
        tv.setText(mErrorText);
        tv.setTextColor(mTextColor);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);
        ViewGroup.MarginLayoutParams params = (MarginLayoutParams) iv.getLayoutParams();
        params.topMargin = mErrorMargin;
        TextView retry = layout.findViewById(R.id.__android_loading_error_retry);
        retry.setText(mRetryText);
        retry.setTextColor(mRetryTextColor);
        retry.setTextSize(TypedValue.COMPLEX_UNIT_PX, mRetryTextSize);
        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mRetryListener != null) {
                    mRetryListener.onClick(view);
                }
            }
        });
        params = (MarginLayoutParams) retry.getLayoutParams();
        params.topMargin = mRetryMargin;
        ViewCompat.setBackground(retry, mRetryBackground);
        mLayouts.put(ERROR_RES_ID, layout);
        addView(layout);
    }

    private void initEmptyLayout() {
        View layout = LayoutInflater.from(getContext()).inflate(EMPTY_RES_ID, this, false);
        ViewCompat.setBackground(layout, mBackground);
        ImageView iv = layout.findViewById(R.id.__android_loading_empty_img);
        iv.setImageResource(mEmptyImage);
        TextView tv = layout.findViewById(R.id.__android_loading_empty_text);
        tv.setText(mEmptyText);
        tv.setTextColor(mTextColor);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);
        ViewGroup.MarginLayoutParams params = (MarginLayoutParams) tv.getLayoutParams();
        params.topMargin = mEmptyMargin;
        mLayouts.put(EMPTY_RES_ID, layout);
        addView(layout);
    }


    private void initLayout(int layoutId) {
        if (layoutId != NO_ID && mLayouts.get(layoutId) == null) {
            if (layoutId == LOADING_RES_ID) {
                initLoadingLayout();
            } else if (layoutId == ERROR_RES_ID) {
                initErrorLayout();
            } else if (layoutId == EMPTY_RES_ID) {
                initEmptyLayout();
            }
        }
    }

    public TextView getEmptyTextView() {
        if (mLayouts.get(EMPTY_RES_ID) == null) {
            initEmptyLayout();
        }
        return mLayouts.get(EMPTY_RES_ID).findViewById(R.id.__android_loading_empty_text);
    }

    public ImageView getEmptyImageView() {
        if (mLayouts.get(EMPTY_RES_ID) == null) {
            initEmptyLayout();
        }
        return mLayouts.get(EMPTY_RES_ID).findViewById(R.id.__android_loading_empty_img);
    }

    public TextView getErrorTextView() {
        if (mLayouts.get(ERROR_RES_ID) == null) {
            initEmptyLayout();
        }
        return mLayouts.get(ERROR_RES_ID).findViewById(R.id.__android_loading_error_text);
    }

    public TextView getErrorImageView() {
        if (mLayouts.get(ERROR_RES_ID) == null) {
            initEmptyLayout();
        }
        return mLayouts.get(ERROR_RES_ID).findViewById(R.id.__android_loading_error_img);
    }

    public TextView getRetryTextView() {
        if (mLayouts.get(ERROR_RES_ID) == null) {
            initEmptyLayout();
        }
        return mLayouts.get(ERROR_RES_ID).findViewById(R.id.__android_loading_error_retry);
    }

    public TextView getLoadingTextView() {
        View layout = mLayouts.get(LOADING_RES_ID);
        if (layout != null) {
            return layout.findViewById(R.id.__android_loading_loading_text);
        }
        return null;
    }

    public LoadingView getLoadingView() {
        View layout = mLayouts.get(LOADING_RES_ID);
        if (layout != null) {
            return layout.findViewById(R.id.__android_loading_loading_view);
        }
        return null;
    }


    public LoadingLayout setTextSize(float size) {
        mTextSize = toPixel(size);
        setTextSize(EMPTY_RES_ID, R.id.__android_loading_empty_text, mTextSize);
        setTextSize(ERROR_RES_ID, R.id.__android_loading_error_text, mTextSize);
        setTextSize(LOADING_RES_ID, R.id.__android_loading_loading_text, mTextSize);
        return this;
    }

    public LoadingLayout setTextColor(int color) {
        mTextColor = color;
        setTextColor(EMPTY_RES_ID, R.id.__android_loading_empty_text, mTextColor);
        setTextColor(ERROR_RES_ID, R.id.__android_loading_error_text, mTextColor);
        setTextColor(LOADING_RES_ID, R.id.__android_loading_loading_text, mTextColor);
        return this;
    }

    public LoadingLayout setEmptyImage(@DrawableRes int resId) {
        mEmptyImage = resId;
        setImage(EMPTY_RES_ID, R.id.__android_loading_empty_img, mEmptyImage);
        return this;
    }

    public LoadingLayout setEmptyText(CharSequence text) {
        mEmptyText = text;
        setText(EMPTY_RES_ID, R.id.__android_loading_empty_text, mEmptyText);
        return this;
    }

    public LoadingLayout setEmptyMargin(float margin) {
        mEmptyMargin = toPixel(margin);
        setTextMargin(EMPTY_RES_ID, R.id.__android_loading_empty_text, mEmptyMargin);
        return this;
    }

    public LoadingLayout setErrorImage(@DrawableRes int resId) {
        mErrorImage = resId;
        setImage(ERROR_RES_ID, R.id.__android_loading_error_img, mErrorImage);
        return this;
    }

    public LoadingLayout setErrorText(String text) {
        mErrorText = text;
        setText(ERROR_RES_ID, R.id.__android_loading_empty_text, mErrorText);
        return this;
    }

    public LoadingLayout setErrorMargin(float margin) {
        mErrorMargin = toPixel(margin);
        setTextMargin(ERROR_RES_ID, R.id.__android_loading_error_text, mErrorMargin);
        return this;
    }


    public LoadingLayout setRetryMargin(float margin) {
        mRetryMargin = toPixel(margin);
        setTextMargin(ERROR_RES_ID, R.id.__android_loading_error_retry, mRetryMargin);
        return this;
    }

    public LoadingLayout setRetryText(String text) {
        mRetryText = text;
        setText(ERROR_RES_ID, R.id.__android_loading_error_retry, mRetryText);
        return this;
    }

    public LoadingLayout setRetryTextColor(int color) {
        mRetryTextColor = color;
        setTextColor(ERROR_RES_ID, R.id.__android_loading_error_retry, mRetryTextColor);
        return this;
    }

    public LoadingLayout setRetryTextSize(float size) {
        mRetryTextSize = toPixel(size);
        setTextSize(ERROR_RES_ID, R.id.__android_loading_error_retry, mRetryTextSize);
        return this;
    }

    public LoadingLayout setRetryBackgroundRes(@DrawableRes int resId) {
        mRetryBackground = ContextCompat.getDrawable(getContext(), resId);
        View layout = mLayouts.get(EMPTY_RES_ID);
        if (layout != null) {
            ViewCompat.setBackground(layout.findViewById(R.id.__android_loading_error_retry), mRetryBackground);
        }
        return this;
    }

    public LoadingLayout setRetryBackground(Drawable drawable) {
        mRetryBackground = drawable;
        View layout = mLayouts.get(EMPTY_RES_ID);
        if (layout != null) {
            ViewCompat.setBackground(layout.findViewById(R.id.__android_loading_error_retry), mRetryBackground);
        }
        return this;
    }

    public LoadingLayout setRetryBackgroundColor(int color) {
        mRetryBackground = new ColorDrawable(color);
        View layout = mLayouts.get(EMPTY_RES_ID);
        if (layout != null) {
            ViewCompat.setBackground(layout.findViewById(R.id.__android_loading_error_retry), mRetryBackground);
        }
        return this;
    }

    public LoadingLayout setRetryListener(OnClickListener listener) {
        mRetryListener = listener;
        return this;
    }

    public LoadingLayout setLoadingColor(int ...colors) {
        mLoadingColors = colors;
        View layout = mLayouts.get(LOADING_RES_ID);
        if (layout != null) {
            ((LoadingView) layout.findViewById(R.id.__android_loading_loading_view)).setArcColors(colors);
        }
        return this;
    }

    public LoadingLayout setLoadingSize(float size) {
        mLoadingSize = toPixel(size);
        View layout = mLayouts.get(LOADING_RES_ID);
        if (layout != null) {
            LoadingView lv = layout.findViewById(R.id.__android_loading_loading_view);
            lv.getLayoutParams().width = mLoadingSize;
            lv.getLayoutParams().height = mLoadingSize;
        }
        return this;
    }

    public LoadingLayout setLoadingText(String text) {
        mLoadingText = text;
        setText(LOADING_RES_ID, R.id.__android_loading_loading_text, mLoadingText);
        return this;
    }

    public LoadingLayout setLoadingMargin(float margin) {
        mLoadingMargin = toPixel(margin);
        setTextMargin(mLoadingMargin, R.id.__android_loading_loading_text, mLoadingMargin);
        return this;
    }

    public void showLoading() {
        show(LOADING_RES_ID);
    }

    public void showLoading(String text) {
        showLoading();
        setLoadingText(text);
    }

    public void showEmpty() {
        show(EMPTY_RES_ID);
    }

    public void showEmpty(String text) {
        showEmpty();
        setEmptyText(text);
    }

    public void showError() {
        show(ERROR_RES_ID);
    }

    public void showError(String text) {
        showError();
        setErrorText(text);
    }

    public void showContent() {
        show(NO_ID);
    }

    private void show(int layoutId) {
        // 初始化布局
        initLayout(layoutId);
        // 显示和隐藏相应的界面
        int count = getChildCount();
        View temp = mLayouts.get(layoutId);
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (child == mMaskView) {
                // 不处理遮罩控件
                continue;
            }
            if (temp == null) {
                // temp为空说明是显示内容
                child.setVisibility(mLayouts.indexOfValue(child) < 0 ? VISIBLE : GONE);
            } else {
                // temp不为空说明显示的为特定的状态布局
                child.setVisibility(child == temp ? VISIBLE : GONE);
            }
        }
        // 控制进度控件的显示和隐藏
        LoadingView lv = findViewById(R.id.__android_loading_loading_view);
        if (lv != null) {
            if (layoutId == LOADING_RES_ID) {
                lv.start();
            } else {
                lv.stop();
            }
        }
    }

    private void setText(int layoutId, int tvId, CharSequence value) {
        View layout = mLayouts.get(layoutId);
        if (layout != null) {
            TextView view = layout.findViewById(tvId);
            if (view != null) {
                view.setText(value);
            }
        }
    }

    private void setTextSize(int layoutId, int tvId, int size) {
        View layout = mLayouts.get(layoutId);
        if (layout != null) {
            TextView view = layout.findViewById(tvId);
            if (view != null) {
                view.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
            }
        }
    }

    private void setTextColor(int layoutId, int tvId, int color) {
        View layout = mLayouts.get(layoutId);
        if (layout != null) {
            TextView view = layout.findViewById(tvId);
            if (view != null) {
                view.setTextColor(color);
            }
        }
    }

    private void setTextMargin(int layoutId, int tvId, int margin) {
        View layout = mLayouts.get(layoutId);
        if (layout != null) {
            TextView view = layout.findViewById(tvId);
            if (view != null) {
                ViewGroup.MarginLayoutParams params = (MarginLayoutParams) view.getLayoutParams();
                params.topMargin = margin;
            }
        }
    }

    private void setImage(int layoutId, int ivId, int resId) {
        View layout = mLayouts.get(layoutId);
        if (layout != null) {
            ImageView view = layout.findViewById(ivId);
            if (view != null) {
                view.setImageResource(resId);
            }
        }
    }

    private int toPixel(float dip) {
        float density = Resources.getSystem().getDisplayMetrics().density;
        return (int) Math.ceil(dip * density);
    }
}
