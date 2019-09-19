package com.uguke.android.app;

/**
 * 页面布局样式
 * @author LeiJue
 */
public enum Style {

    /** 默认调用（包含标题、刷新、加载） **/
    DEFAULT(0),
    /** 原生调用 **/
    NATIVE(1),
    /** 默认滑动返回调用（包含标题、刷新、加载） **/
    DEFAULT_SWIPE(2),
    /** 原生滑动返回调用 **/
    NATIVE_SWIPE(3);

    int mCode;

    Style(int code) {
        mCode = code;
    }

    /**
     * 是否是滑动样式
     */
    public static boolean isSwipe(Style style) {
        return DEFAULT_SWIPE == style || NATIVE_SWIPE == style;
    }

    /**
     * 是否是原生样式
     */
    public static boolean isNative(Style style) {
        return NATIVE == style || NATIVE_SWIPE == style;
    }

    /**
     * 是否是默认样式
     */
    public static boolean isDefault(Style style) {
        return DEFAULT == style || DEFAULT_SWIPE == style;
    }
}
