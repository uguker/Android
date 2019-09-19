package com.uguke.android.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;


import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;

import com.uguke.android.R;

import java.util.Locale;

/**
 * 验证码控件
 * @author LeiJue
 */
public class CodeView extends AppCompatTextView {

    private static final String DEFAULT_COMMON = "获取验证码";
    private static final String DEFAULT_FORMAT = "%d秒";

    private int mDuration;
    private String mCommonText;
    private String mFormatText;
    private boolean mWait;

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            if (message.what == 0) {
                int s = (int) message.obj;
                if (s > 0) {
                    CodeView.this.setText(String.format(Locale.getDefault(), mFormatText, s));
                    CodeView.this.setEnabled(false);
                    Message msg = Message.obtain();
                    msg.what = 0;
                    msg.obj = (s - 1);
                    mHandler.sendMessageDelayed(msg, 1000);
                    mWait = true;
                } else {
                    CodeView.this.setText(mCommonText);
                    CodeView.this.setEnabled(true);
                    mWait = false;
                }
            }
            return false;
        }
    });

    public CodeView(Context context) {
        super(context);
        init(null);
    }

    public CodeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public CodeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mHandler.removeCallbacksAndMessages(null);
        mHandler = null;
    }

    private void init(AttributeSet attrs) {
        mCommonText = String.valueOf(getText());
        if (attrs != null) {
            TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.CodeView);
            mDuration = ta.getInt(R.styleable.CodeView_duration, 60);
            mFormatText = ta.getString(R.styleable.CodeView_formatText);
            ta.recycle();
        }
        setCommonText(mCommonText);
        setFormatText(mFormatText);
    }

    public void setCommonText(@NonNull String text) {
        mCommonText = text;
        if (TextUtils.isEmpty(mCommonText)) {
            mCommonText = DEFAULT_COMMON;
        }
        setText(mCommonText);
    }

    public void setFormatText(@NonNull String text) {
        mFormatText = text;
        if (TextUtils.isEmpty(mFormatText)) {
            mFormatText = DEFAULT_FORMAT;
        }
    }

    public void setDuration(int duration) {
        mDuration = duration;
    }

    public boolean isWait() {
        return mWait;
    }

    public void send() {
        Message message = Message.obtain();
        message.obj = mDuration;
        message.what = 0;
        mHandler.sendMessage(message);
    }
}
