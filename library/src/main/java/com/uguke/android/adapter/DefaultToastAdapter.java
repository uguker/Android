package com.uguke.android.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.Toast;

/**
 * 默认弹窗适配
 * @author LeiJue
 */
public class DefaultToastAdapter implements ToastAdapter {

    private Toast mToast;

    @SuppressLint("ShowToast")
    @Override
    public void show(Context context, String text, int duration) {
        if (mToast == null) {
            mToast = Toast.makeText(context.getApplicationContext(), "", Toast.LENGTH_SHORT);
        }
        mToast.setText(text);
        mToast.show();
    }
}
