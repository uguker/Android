package com.uguke.android.helper.refresh;

/**
 * 刷新监听
 * @author LeiJue
 */
public interface OnRefreshListener {

    /**
     * 刷新动作监听
     * @param page  页码
     * @param pageSize 页面大小
     */
    void onRefresh(int page, int pageSize);
}
