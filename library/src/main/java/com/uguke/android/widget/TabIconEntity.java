package com.uguke.android.widget;

import androidx.annotation.DrawableRes;

public interface TabIconEntity extends TabEntity {

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
