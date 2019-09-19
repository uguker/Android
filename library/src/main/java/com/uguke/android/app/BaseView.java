package com.uguke.android.app;

import android.content.Context;

import androidx.fragment.app.FragmentActivity;

import com.uguke.android.helper.snack.OnDismissListener;

/**
 * 基础View
 * @author LeiJue
 */
public interface BaseView {
    /**
     * 获取上下文
     * @return Content
     */
    Context getContext();

    /**
     * 获取活动对象
     * @return AppCompatActivity
     */
    FragmentActivity getActivity();

    /**
     * 显示加载框
     * @param texts 文本
     */
    void showLoading(String ...texts);

    /**
     * 隐藏加载框
     */
    void hideLoading();

    /**
     * 显示Snack文本
     * @param text 文本
     */
    void showSnack(String text);

    /**
     * 显示Snack文本
     * @param text 文本
     * @param duration 时长
     */
    void showSnack(String text, int duration);

    /**
     * 显示Snack文本
     * @param text 文本
     * @param listener 回调
     */
    void showSnack(String text, OnDismissListener listener);

    /**
     * 显示Snack文本
     * @param text 文本
     * @param duration 时长
     * @param listener 回调
     */
    void showSnack(String text, int duration, OnDismissListener listener);

    /**
     * 显示弹窗
     * @param text 文本
     */
    void showToast(String text);
}
