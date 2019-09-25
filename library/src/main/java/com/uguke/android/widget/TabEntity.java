package com.uguke.android.widget;

import androidx.annotation.DrawableRes;

/**
 * TabLayout实体
 * @author LeiJue
 */
public interface TabEntity {
    /**
     * 获取标题
     * @return 标题
     */
    CharSequence getTitle();

    /**
     * 获取选择图片资源ID
     * @return 图片资源ID
     */
    @DrawableRes
    int getSelectedIcon();

    /**
     * 获取未选择图片资源ID
     * @return 图片资源ID
     */
    @DrawableRes
    int getUnselectedIcon();
}
