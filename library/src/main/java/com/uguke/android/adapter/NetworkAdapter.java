package com.uguke.android.adapter;

/**
 * 网络请求适配
 * @author LeiJue
 */
public interface NetworkAdapter {

    /**
     * 添加网络请求
     * @param tag       标识
     * @param request   请求
     */
    void add(Object tag, Object request);

    /**
     * 取消网络请求
     * @param tag 标识
     */
    void cancel(Object tag);
}
