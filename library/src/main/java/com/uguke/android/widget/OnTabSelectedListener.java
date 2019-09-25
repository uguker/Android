package com.uguke.android.widget;

/**
 * Tab项选择监听
 * @author LeiJue
 */
public interface OnTabSelectedListener {
    /**
     * 被选择
     * @param position 位置
     */
    void onTabSelected(int position);
    /**
     * 再次被选择
     * @param position 位置
     */
    void onTabReselected(int position);
}
