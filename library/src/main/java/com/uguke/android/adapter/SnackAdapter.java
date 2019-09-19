package com.uguke.android.adapter;

import com.uguke.android.helper.snack.SnackHelper;

/**
 * Snack适配器
 * @author LeiJue
 */
public interface SnackAdapter {

    /**
     * 显示Snackbar
     * @param helper helper对象
     * @return Snackbar对象
     */
    Object onShow(SnackHelper helper);

    /**
     * 手动取消
     * @param bar Snackbar对象（onShow返回的Snackbar对象）
     */
    void onHide(Object bar);

    /**
     * Snackbar持续存在期间变化
     * @param helper helper对象
     * @param bar Snackbar对象（onShow返回的Snackbar对象）
     */
    void onChange(SnackHelper helper, Object bar);
}
