package com.uguke.android.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.core.view.ViewCompat;

import com.uguke.android.R;

/**
 * 多状态布局
 * @author LeiJue
 */
public class LoadingLayout extends RelativeLayout {
    public interface OnInflateListener {
        void onInflate(View inflated);
    }

    int mEmptyImage;
    CharSequence mEmptyText;

    int mErrorImage;
    CharSequence mErrorText, mRetryText;
    View.OnClickListener mRetryButtonClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mRetryListener != null) {
                mRetryListener.onClick(v);
            }
        }
    };
    View.OnClickListener mRetryListener;

    OnInflateListener mOnEmptyInflateListener;
    OnInflateListener mOnErrorInflateListener;

    int mTextColor, mTextSize;
    int mButtonTextColor, mButtonTextSize;
    Drawable mButtonBackground;
    int mEmptyResId;
    int mLoadingResId;
    int mErrorResId;

    private int mEmptyMargin;
    private int mErrorMargin;
    private int mLoadingMargin;

    private SparseArray<View> mLayouts = new SparseArray<>();

    public LoadingLayout(Context context) {
        this(context, null, R.attr.styleLoadingLayout);
    }

    public LoadingLayout(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.styleLoadingLayout);
    }

    public LoadingLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mLoadingResId = R.layout._loading_layout_loading;
        mErrorResId = R.layout._loading_layout_error;
        mEmptyResId = R.layout._loading_layout_empty;

        mTextSize = getResources().getDimensionPixelSize(R.dimen.body);
        mButtonTextSize = getResources().getDimensionPixelSize(R.dimen.body);
        mEmptyMargin = getResources().getDimensionPixelSize(R.dimen.content);
        mErrorMargin = getResources().getDimensionPixelSize(R.dimen.content);
        mLoadingMargin = getResources().getDimensionPixelSize(R.dimen.content);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.LoadingLayout, defStyleAttr, R.style.LoadingLayoutStyle);
        mEmptyImage = a.getResourceId(R.styleable.LoadingLayout_llEmptyImage, R.drawable.ic_empty);
        mEmptyText = a.getString(R.styleable.LoadingLayout_llEmptyText);
        mEmptyMargin = a.getDimensionPixelSize(R.styleable.LoadingLayout_llEmptyMargin, mEmptyMargin);
        mErrorMargin = a.getDimensionPixelSize(R.styleable.LoadingLayout_llErrorMargin, mErrorMargin);
        mLoadingMargin = a.getDimensionPixelSize(R.styleable.LoadingLayout_llLoadingMargin, mLoadingMargin);

        mErrorImage = a.getResourceId(R.styleable.LoadingLayout_llErrorImage, R.drawable.ic_empty);
        mErrorText = a.getString(R.styleable.LoadingLayout_llErrorText);
        mRetryText = a.getString(R.styleable.LoadingLayout_llRetryText);

        mTextColor = a.getColor(R.styleable.LoadingLayout_llTextColor, 0xff999999);
        mTextSize = a.getDimensionPixelSize(R.styleable.LoadingLayout_llTextSize, mTextSize);

        mButtonTextColor = a.getColor(R.styleable.LoadingLayout_llRetryTextColor, 0xff999999);
        mButtonTextSize = a.getDimensionPixelSize(R.styleable.LoadingLayout_llRetryTextSize, mButtonTextSize);
        mButtonBackground = a.getDrawable(R.styleable.LoadingLayout_llRetryBackground);

        a.recycle();
    }

    private void initLoadingLayout() {
        View layout = LayoutInflater.from(getContext()).inflate(mLoadingResId, this, true);
        mLayouts.put(mLoadingResId, layout);
    }

    private void initErrorLayout() {
        View layout = LayoutInflater.from(getContext()).inflate(mErrorResId, this, true);
        ImageView iv = layout.findViewById(R.id.error_image);
        iv.setImageResource(mErrorImage);
        TextView tv = layout.findViewById(R.id.error_text);
        tv.setText(mErrorText);
        tv.setTextColor(mTextColor);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);
        TextView retry = layout.findViewById(R.id.retry_button);
        retry.setText(mRetryText);
        retry.setTextColor(mButtonTextColor);
        retry.setTextSize(TypedValue.COMPLEX_UNIT_PX, mButtonTextSize);
        retry.setOnClickListener(mRetryButtonClickListener);
        ViewCompat.setBackground(retry, mButtonBackground);
        mLayouts.put(mErrorResId, layout);
    }

    private void initEmptyLayout() {
        View layout = LayoutInflater.from(getContext()).inflate(mEmptyResId, this, true);
        ImageView iv = layout.findViewById(R.id.empty_image);
        iv.setImageResource(mEmptyImage);
        ViewGroup.MarginLayoutParams params = (MarginLayoutParams) iv.getLayoutParams();
        params.bottomMargin = mEmptyMargin;
        TextView tv = layout.findViewById(R.id.empty_text);
        tv.setText(mEmptyText);
        tv.setTextColor(mTextColor);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);
        if (mOnEmptyInflateListener != null) {
            mOnEmptyInflateListener.onInflate(layout);
        }
        mLayouts.put(mEmptyResId, layout);
    }

    private void initLayout(int layoutId) {
        if (layoutId != NO_ID && mLayouts.get(layoutId) == null) {
            if (layoutId == mLoadingResId) {
                initLoadingLayout();
            } else if (layoutId == mErrorResId) {
                initErrorLayout();
            } else if (layoutId == mEmptyResId) {
                initEmptyLayout();
            }
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() == 0) {
            return;
        }
//        if (getChildCount() > 1) {
//            removeViews(1, getChildCount() - 1);
//        }
//        View view = getChildAt(0);
//        setContentView(view);
        showEmpty();
    }


    public LoadingLayout setEmptyImage(@DrawableRes int resId) {
        mEmptyImage = resId;
        setImage(mEmptyResId, R.id.empty_image, mEmptyImage);
        return this;
    }

    public LoadingLayout setEmptyText(CharSequence text) {
        mEmptyText = text;
        setText(mEmptyResId, R.id.empty_text, mEmptyText);
        return this;
    }

    public LoadingLayout setErrorImage(@DrawableRes int resId) {
        mErrorImage = resId;
        setImage(mErrorResId, R.id.error_image, mErrorImage);
        return this;
    }

    public LoadingLayout setErrorText(String text) {
        mErrorText = text;
        setText(mErrorResId, R.id.error_text, mErrorText);
        return this;
    }

    public LoadingLayout setRetryText(String text) {
        mRetryText = text;
        setText(mErrorResId, R.id.retry_button, mRetryText);
        return this;
    }

    public LoadingLayout setRetryListener(OnClickListener listener) {
        mRetryListener = listener;
        return this;
    }

    public void showLoading() {
        show(mLoadingResId);
    }

    public void showEmpty() {
        show(mEmptyResId);
    }

    public void showError() {
        show(mErrorResId);
    }

    public void showContent() {
        show(NO_ID);
    }

    private void show(int layoutId) {
        // 初始化布局
        initLayout(layoutId);
        // 显示或隐藏相应的布局
        for (int i = 0; i < mLayouts.size(); i++) {
            int id = mLayouts.keyAt(i);
            View view = mLayouts.valueAt(i);
            view.setVisibility(layoutId == id ? VISIBLE : GONE);
            if (layoutId == id) {
                view.bringToFront();
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

    private void setImage(int layoutId, int ivId, int resId) {
        View layout = mLayouts.get(layoutId);
        if (layout != null) {
            ImageView view = layout.findViewById(ivId);
            if (view != null) {
                view.setImageResource(resId);
            }
        }
    }
}
