package com.uguke.android.bus;

import com.trello.rxlifecycle3.LifecycleProvider;
import com.trello.rxlifecycle3.LifecycleTransformer;
import com.trello.rxlifecycle3.android.ActivityEvent;
import com.trello.rxlifecycle3.android.FragmentEvent;
import com.trello.rxlifecycle3.components.RxActivity;
import com.trello.rxlifecycle3.components.support.RxAppCompatActivity;
import com.trello.rxlifecycle3.components.support.RxFragmentActivity;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;

/**
 * 内部处理
 * @author LeiJue
 */
public class Bus {

    /** RxJava生命周期管理对象 **/
    private LifecycleProvider mLifecycleProvider;
    /** Fragment订阅结束生命周期位置 **/
    private FragmentEvent mFragmentEndEvent;
    /** Activity订阅结束生命周期位置 **/
    private ActivityEvent mActivityEndEvent;
    /** 事件处理 **/
    private Consumer<? super Event<?>> mOnNext;
    /** 异常处理 **/
    private Consumer<Throwable> mOnError;
    private int mEventCode;
    private Class<?> mEventClass;

    public Bus(LifecycleProvider provider) {
        mLifecycleProvider = provider;
    }

    public Bus setEventCode(int code) {
        mEventCode = code;
        return this;
    }

    public Bus setEventClass(Class<?> clazz) {
        mEventClass = clazz;
        return this;
    }

    public Bus setEndEvent(FragmentEvent event) {
        this.mFragmentEndEvent = event;
        return this;
    }

    public Bus setEndEvent(ActivityEvent event) {
        this.mActivityEndEvent = event;
        return this;
    }

    public Bus onNext(Consumer<? super Event<?>> consumer) {
        mOnNext = consumer;
        return this;
    }

    public Bus onError(Consumer<Throwable> consumer) {
        mOnError = consumer;
        return this;
    }

    @SuppressWarnings("unchecked")
    public void create() {
        if (mLifecycleProvider == null) {
            return;
        }
        LifecycleTransformer<Event<?>> composer;
        if (mLifecycleProvider instanceof RxAppCompatActivity ||
                mLifecycleProvider instanceof RxFragmentActivity ||
                mLifecycleProvider instanceof RxActivity) {
            if (mActivityEndEvent == null) {
                composer = mLifecycleProvider.bindToLifecycle();
            } else {
                composer = mLifecycleProvider.bindUntilEvent(mActivityEndEvent);
            }
        } else {
            if (mFragmentEndEvent == null) {
                composer = mLifecycleProvider.bindToLifecycle();
            } else {
                composer = mLifecycleProvider.bindUntilEvent(mFragmentEndEvent);
            }
        }
        RxBus.getInstance()
                .toFlowable()
                // 绑定生命周期
                .compose(composer)
                .filter(new Predicate<Event<?>>() {
                    @Override
                    public boolean test(Event<?> e) throws Exception {
                        if (mEventClass == null) {
                            return mEventCode == e.code;
                        } else if (e.body != null) {
                            return mEventCode == e.code && mEventClass == e.body.getClass();
                        }
                        return true;
                    }
                })
                //过滤 根据code判断返回事件
                .subscribe(mOnNext, mOnError == null ? new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                } : mOnError);
    }
}
