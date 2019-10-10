package com.uguke.android.bus;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 事件对象
 * @author LeiJue
 */
public class Event<T> {

    /** 点击事件 **/
    public static final int TAP = 1;
    /** 其他事件 **/
    public static final int OTHER = 21;

    //枚举
    @IntDef({TAP, OTHER})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Code {}


    public @Code int code;
    public T body;

    public static <O> Event<O> setContent(O t) {
        Event<O> event = new Event<>();
        event.body = t;
        return event;
    }

    public <T> T getContent() {
        return (T) body;
    }
}
