package com.uguke.android.helper.snack;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.core.content.ContextCompat;

import com.uguke.android.R;
import com.uguke.android.adapter.SnackAdapter;
import com.uguke.android.app.AppDelegate;

import java.util.LinkedList;
import java.util.List;

/**
 * Snack弹窗
 * @author LeiJue
 */
public class SnackHelper {

    /** 时长为1个小时，故需要手动取消 **/
    public static final int DURATION_MANUAL = 1000 * 60 * 60;
    /** 一个较长的时间 **/
    public static final int DURATION_LONG = 3500;
    /** 一个较短的时间 **/
    public static final int DURATION_SHORT = 1500;
    /** 一个需要手动取消的时间，设置为大于30秒就需要手动取消 **/
    private static final int DURATION_NEED_MANUAL_DISMISS = 30000;

    /** Snack变化间隔 **/
    private static final int CHANGE_DURATION = 250;
    /** Snack变化标志 **/
    private static final int CHANGE_FLAG = 100;

    private int mDuration;
    private CharSequence mText;
    private CharSequence mActionText;
    private ColorStateList mTextColor;
    private ColorStateList mActionTextColor;
    private ColorStateList mBackgroundTintList;
    private float mTextSize;
    private float mActionTextSize;
    private View mView;
    private Object mSnackbar;
    private SnackAdapter mAdapter;

    /** Action按钮点击事件 **/
    private View.OnClickListener mOnActionListener;
    /** 取消监听集合 **/
    private List<OnDismissListener> mOnDismissListeners;
    /** 显示监听集合 **/
    private List<OnShowListener> mOnShowListeners;

    /** 用以通知变化 **/
    private Handler mHandler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == CHANGE_FLAG) {
                // 通知变化
                if (mAdapter != null) {
                    mAdapter.onChange(SnackHelper.this, mSnackbar);
                }
                Message message = Message.obtain();
                message.what = msg.what;
                mHandler.sendMessageDelayed(message, CHANGE_DURATION);
            }
            return false;
        }
    });

    public static SnackHelper make(View view) {
        return new SnackHelper(view);
    }

    private SnackHelper(View view) {
        mView = view;
        mDuration = DURATION_SHORT;
        mTextSize = 14;
        mActionTextSize = 14;
        mOnDismissListeners = new LinkedList<>();
        mOnShowListeners = new LinkedList<>();
        mTextColor = ColorStateList.valueOf(Color.WHITE);
        mActionTextColor = ColorStateList.valueOf(ContextCompat.getColor(view.getContext(), R.color.colorAccent));
        mBackgroundTintList = ColorStateList.valueOf(Color.parseColor("#313332"));
        mAdapter = AppDelegate.getInstance().getSnackAdapter();
    }

    public SnackHelper setView(View view) {
        this.mView = view;
        return this;
    }

    public SnackHelper setDuration(int duration) {
        this.mDuration = duration;
        return this;
    }

    public SnackHelper setText(CharSequence text) {
        this.mText = text;
        return this;
    }

    public SnackHelper setTextColor(@ColorInt int color) {
        this.mTextColor = ColorStateList.valueOf(color);
        return this;
    }

    public SnackHelper setTextColor(ColorStateList color) {
        this.mTextColor = color;
        return this;
    }

    public SnackHelper setTextSize(float size) {
        this.mTextSize = size;
        return this;
    }

    public SnackHelper setActionTextColor(@ColorInt int color) {
        mActionTextColor = ColorStateList.valueOf(color);
        return this;
    }

    public SnackHelper setActionTextColor(ColorStateList color) {
        mActionTextColor = color;
        return this;
    }

    public SnackHelper setActionSize(float size) {
        mActionTextSize = size;
        return this;
    }

    public SnackHelper setAction(CharSequence action, View.OnClickListener listener) {
        this.mActionText = action;
        this.mOnActionListener = listener;
        return this;
    }

    public SnackHelper setAdapter(SnackAdapter adapter) {
        mAdapter = adapter;
        return this;
    }

    public SnackHelper setBackgroundTint(@ColorInt int color) {
        mBackgroundTintList = ColorStateList.valueOf(color);
        return this;
    }

    public SnackHelper setBackgroundTintList(ColorStateList colorStateList) {
        mBackgroundTintList = colorStateList;
        return this;
    }
    
    public SnackHelper addOnDismissListener(OnDismissListener listener) {
        mOnDismissListeners.add(listener);
        return this;
    }

    public SnackHelper addOnShowListener(OnShowListener listener) {
        mOnShowListeners.add(listener);
        return this;
    }

    public void show() {
        if (mAdapter != null) {
            mSnackbar = mAdapter.onShow(this);
            // 如果时长大于需要手动取消的界限
            if (mDuration >= DURATION_NEED_MANUAL_DISMISS) {
                // 发送变化消息
                mHandler.removeMessages(CHANGE_FLAG);
                mHandler.sendEmptyMessage(CHANGE_FLAG);
            }
        }
    }

    public void onDestroy() {
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
        }
        mSnackbar = null;
    }

    public void stopChange() {
        if (mHandler != null) {
            mHandler.removeMessages(CHANGE_FLAG);
        }
        mSnackbar = null;
    }

    public int getDuration() {
        return mDuration;
    }

    public CharSequence getText() {
        return mText;
    }

    public CharSequence getActionText() {
        return mActionText;
    }

    public ColorStateList getTextColor() {
        return mTextColor;
    }

    public ColorStateList getActionTextColor() {
        return mActionTextColor;
    }

    public ColorStateList getBackgroundTintList() {
        return mBackgroundTintList;
    }

    public float getTextSize() {
        return mTextSize;
    }

    public float getActionTextSize() {
        return mActionTextSize;
    }

    public View getView() {
        return mView;
    }

    public List<OnDismissListener> getDismissedListeners() {
        return mOnDismissListeners;
    }

    public List<OnShowListener> getShownListeners() {
        return mOnShowListeners;
    }

    public View.OnClickListener getOnActionListener() {
        return mOnActionListener;
    }

    public void dismiss() {
        if (mAdapter != null && mSnackbar != null) {
            mAdapter.onHide(mSnackbar);
            stopChange();
        }
    }

}
