package com.uguke.android.app;

/**
 * 布局状态接口
 * @author LeiJue
 */
public interface LoadingProvider {

    /**
     * 显示内容
     */
    void showContent();

    /**
     * 显示空布局
     */
    void showEmpty();

    /**
     * 显示空布局
     * @param text 文本
     */
    void showEmpty(String text);

    /**
     * 显示错误布局
     */
    void showError();

    /**
     * 显示错误布局
     * @param text 文本
     */
    void showError(String text);

    /**
     * 显示加载布局
     */
    void showLoading();

    /**
     * 显示加载布局
     * @param text 文本
     */
    void showLoading(String text);
}
