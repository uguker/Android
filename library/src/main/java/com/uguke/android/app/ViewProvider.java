package com.uguke.android.app;

import android.view.View;

interface ViewProvider {

    /**
     * 布局创建已创建
     * @param view 界面
     */
    void onViewCreated(View view);
//    /**
//     * 显示内容
//     */
//    void showContent();
//
//    /**
//     * 显示空布局
//     * @param texts 文本
//     */
//    void showEmpty(String ...texts);
//
//    /**
//     * 显示错误布局
//     * @param texts 文本
//     */
//    void showError(String ...texts);
//
//    /**
//     * 显示加载布局
//     * @param texts 文本
//     */
//    void showLoading(String ...texts);

}
