package com.uguke.android.helper;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.core.content.ContextCompat;

import com.uguke.android.R;
import com.uguke.android.adapter.DefaultTipsAdapter;
import com.uguke.android.listener.OnDismissListener;
import com.uguke.android.listener.OnShowListener;

import java.util.LinkedList;
import java.util.List;

/**
 * 消息提示辅助类
 * @author LeiJue
 */
public class TipsHelper {

    /** 时长为1个小时，故需要手动取消 **/
    public static final int DURATION_MANUAL = 1000 * 60 * 60;
    /** 一个较长的时间 **/
    public static final int DURATION_LONG = 3000;
    /** 一个较短的时间 **/
    public static final int DURATION_SHORT = 1500;
    /** 一个需要手动取消的时间，设置为大于30秒就需要手动取消 **/
    private static final int DURATION_NEED_MANUAL_DISMISS = 30000;

    /** Snack变化间隔 **/
    private static final int CHANGE_DURATION = 250;
    /** Snack变化标志 **/
    private static final int CHANGE_FLAG = 100;

    private int mDuration;
    private int mMaxLines;
    private CharSequence mText;
    private CharSequence mActionText;
    private ColorStateList mTextColor;
    private ColorStateList mActionTextColor;
    private ColorStateList mBackgroundTintList;
    private float mTextSize;
    private float mActionTextSize;
    private View mView;
    private Object mObj;
    private Adapter mAdapter = new DefaultTipsAdapter();

    /** Action按钮点击事件 **/
    private View.OnClickListener mOnActionListener;
    /** 取消监听集合 **/
    private List<OnDismissListener<TipsHelper>> mOnDismissListeners;
    /** 显示监听集合 **/
    private List<OnShowListener<TipsHelper>> mOnShowListeners;

    /** 用以通知变化 **/
    private Handler mHandler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == CHANGE_FLAG && mAdapter != null) {
                // 通知变化
                mAdapter.changed(TipsHelper.this, mObj);
                Message message = Message.obtain();
                message.what = msg.what;
                mHandler.sendMessageDelayed(message, CHANGE_DURATION);
            }
            return false;
        }
    });

    public static TipsHelper make(View view) {
        return new TipsHelper(view);
    }

    private TipsHelper(View view) {
        mView = view;
        mDuration = DURATION_SHORT;
        mMaxLines = 2;
        mTextSize = 14;
        mActionTextSize = 14;
        mOnDismissListeners = new LinkedList<>();
        mOnShowListeners = new LinkedList<>();
        mTextColor = ColorStateList.valueOf(Color.WHITE);
        mActionTextColor = ColorStateList.valueOf(ContextCompat.getColor(view.getContext(), R.color.colorAccent));
        mBackgroundTintList = ColorStateList.valueOf(Color.parseColor("#313332"));
    }

    public TipsHelper setView(View view) {
        mView = view;
        return this;
    }

    public TipsHelper setMaxLines(int maxLines) {
        mMaxLines = maxLines;
        return this;
    }

    public TipsHelper setDuration(int duration) {
        mDuration = duration;
        return this;
    }

    public TipsHelper setText(CharSequence text) {
        mText = text;
        return this;
    }

    public TipsHelper setTextColor(@ColorInt int color) {
        mTextColor = ColorStateList.valueOf(color);
        return this;
    }

    public TipsHelper setTextColor(ColorStateList color) {
        mTextColor = color;
        return this;
    }

    public TipsHelper setTextSize(float size) {
        mTextSize = size;
        return this;
    }

    public TipsHelper setActionTextColor(@ColorInt int color) {
        mActionTextColor = ColorStateList.valueOf(color);
        return this;
    }

    public TipsHelper setActionTextColor(ColorStateList color) {
        mActionTextColor = color;
        return this;
    }

    public TipsHelper setActionSize(float size) {
        mActionTextSize = size;
        return this;
    }

    public TipsHelper setAction(CharSequence action, View.OnClickListener listener) {
        mActionText = action;
        mOnActionListener = listener;
        return this;
    }

    public TipsHelper setAdapter(Adapter adapter) {
        mAdapter = adapter;
        return this;
    }

    public TipsHelper setBackgroundTint(@ColorInt int color) {
        mBackgroundTintList = ColorStateList.valueOf(color);
        return this;
    }

    public TipsHelper setBackgroundTintList(ColorStateList colorStateList) {
        mBackgroundTintList = colorStateList;
        return this;
    }

    public TipsHelper addOnDismissListener(OnDismissListener<TipsHelper> listener) {
        mOnDismissListeners.add(listener);
        return this;
    }

    public TipsHelper addOnShowListener(OnShowListener<TipsHelper> listener) {
        mOnShowListeners.add(listener);
        return this;
    }


    public void show() {
        if (mAdapter != null) {
            mObj = mAdapter.show(this);
            // 如果时长大于需要手动取消的界限
            if (mDuration >= DURATION_NEED_MANUAL_DISMISS) {
                // 发送变化消息
                mHandler.removeMessages(CHANGE_FLAG);
                mHandler.sendEmptyMessage(CHANGE_FLAG);
            }
        }
    }

    public void release() {
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
        }
        mObj = null;
    }

    public void stopChanging() {
        if (mHandler != null) {
            mHandler.removeMessages(CHANGE_FLAG);
        }
        mObj = null;
    }

    public int getMaxLines() {
        return mMaxLines;
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

    public List<OnDismissListener<TipsHelper>> getOnDismissListeners() {
        return mOnDismissListeners;
    }

    public List<OnShowListener<TipsHelper>> getOnShowListeners() {
        return mOnShowListeners;
    }

    public View.OnClickListener getOnActionListener() {
        return mOnActionListener;
    }

    public void hide() {
        if (mAdapter != null && mObj != null) {
            mAdapter.hide(mObj);
            stopChanging();
        }
    }

    public interface Adapter {

        /**
         * 显示提示
         * @param helper 提示辅助类实体
         * @return 提示辅助类内置的实现体，如Snackbar。
         */
        Object show(TipsHelper helper);

        /**
         * 隐藏持续显示的提示
         * @param obj show方法返回的对象
         */
        void hide(Object obj);

        /**
         * 持续显示的提示发生变化，每次变化回调
         * @param helper 提示辅助类实体
         * @param obj show方法返回的对象
         */
        void changed(TipsHelper helper, Object obj);
    }
}
