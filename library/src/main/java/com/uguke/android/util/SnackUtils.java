package com.uguke.android.util;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;

/**
 * Snack工具类
 * @author LeiJue
 */
public class SnackUtils {

    /** Snack变化间隔 **/
    private static final int CHANGE_DURATION = 250;
    /** Snack变化标志 **/
    private static final int CHANGE_FLAG = 100;
    /** Snack变化文本 **/
    private static final String [] SNACK_ANIMATION_TEXTS = {" ", " .", " ..", " ..."};
    private static int sDuration = 1500;


    private View mView;
    private Snackbar mSnackBar;

    private String mText;
    private Handler mHandler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == CHANGE_FLAG) {
                int count = 0;
                if (msg.obj != null) {
                    count = (int) msg.obj;
                }
                String animationText = SNACK_ANIMATION_TEXTS[count % SNACK_ANIMATION_TEXTS.length];
                mSnackBar.setText(mText + animationText);
                Message message = Message.obtain();
                message.what = msg.what;
                message.obj = count + 1;
                mHandler.sendMessageDelayed(message, CHANGE_DURATION);
            }
            return false;
        }
    });

    public SnackUtils(View view) {
        this.mView = view;
    }

    public SnackUtils text(String ...texts) {
        mText = (texts == null || texts.length == 0) ? "加载中" : texts[0];
        return this;
    }


    public void show() {
        if (mSnackBar == null) {
            mSnackBar = Snackbar.make(mView, "", 36000);
            mSnackBar.addCallback(new Snackbar.Callback() {
                @Override
                public void onDismissed(Snackbar transientBottomBar, int event) {
                    if (mHandler != null) {
                        mHandler.removeMessages(CHANGE_FLAG);
                    }
                    //mSnackBar = null;
                }
            });
        }
        mSnackBar.setText(mText);
//        if (!mSnackBar.isShown()) {
//            mSnackBar.show();
//        }
        mSnackBar.show();
        mHandler.removeMessages(CHANGE_FLAG);
        mHandler.sendEmptyMessage(CHANGE_FLAG);
    }

    public void hide() {
        mHandler.removeMessages(CHANGE_FLAG);
        if (mSnackBar != null) {
            mSnackBar.dismiss();
            mSnackBar = null;
        }
    }


    public void onDestroy() {
        mHandler.removeCallbacksAndMessages(null);
        mHandler = null;
        mSnackBar = null;
    }

    public static SnackUtils make(View view) {

        return new SnackUtils(view);
    }

    public static void show(View view, String text) {
        Snackbar.make(view, text, sDuration).show();
    }

}
