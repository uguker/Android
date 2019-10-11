package com.uguke.android.bus;

import com.trello.rxlifecycle3.LifecycleProvider;
import com.trello.rxlifecycle3.LifecycleTransformer;
import com.trello.rxlifecycle3.android.ActivityEvent;
import com.trello.rxlifecycle3.android.FragmentEvent;
import com.trello.rxlifecycle3.components.RxActivity;
import com.trello.rxlifecycle3.components.support.RxAppCompatActivity;
import com.trello.rxlifecycle3.components.support.RxFragmentActivity;

import org.reactivestreams.Publisher;

import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

/**
 * 内部处理
 * @author LeiJue
 */
public class Bus<T> {

    /** RxJava生命周期管理对象 **/
    private LifecycleProvider mLifecycleProvider;
    /** Fragment订阅结束生命周期位置 **/
    private FragmentEvent mFragmentEndEvent;
    /** Activity订阅结束生命周期位置 **/
    private ActivityEvent mActivityEndEvent;
    private int mEventCode;
    private Class<?> mClass;

    public Bus(LifecycleProvider provider) {
        super();
        mLifecycleProvider = provider;
    }

    public Bus(LifecycleProvider provider, Class<?> clazz) {
        super();
        mLifecycleProvider = provider;
        // 将基础数据Class转为对应的封装类Class
        if (byte.class == clazz) {
            mClass = Byte.class;
        } else if (short.class == clazz) {
            mClass = Short.class;
        } else if (int.class == clazz) {
            mClass = Integer.class;
        } else if (long.class == clazz) {
            mClass = Long.class;
        }else if (float.class == clazz) {
            mClass = Float.class;
        }else if (double.class == clazz) {
            mClass = Double.class;
        } else if (char.class == clazz) {
            mClass = Character.class;
        } else if (boolean.class == clazz) {
            mClass = Boolean.class;
        } else {
            mClass = clazz;
        }
    }

    public Bus<T> setCode(int code) {
        mEventCode = code;
        return this;
    }

    public Bus<T> setEventCode(int code) {
        mEventCode = code;
        return this;
    }

    public Bus<T> setEndEvent(FragmentEvent event) {
        this.mFragmentEndEvent = event;
        return this;
    }

    public Bus<T> setEndEvent(ActivityEvent event) {
        this.mActivityEndEvent = event;
        return this;
    }

    public void subscribe(Consumer<T> onNext) {
        subscribe(onNext, null);
    }

    @SuppressWarnings("unchecked")
    public void  subscribe(Consumer<T> onNext, Consumer<Throwable> onError) {
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
                // 过滤不符合条件的事件
                .filter(new Predicate<Event<?>>() {
                    @Override
                    public boolean test(Event<?> e) throws Exception {
                        if (mClass == null) {
                            return mEventCode == e.code;
                        } else {
                            if (e.body != null) {
                                return mEventCode == e.code && mClass.isAssignableFrom(e.body.getClass());
                            }
                            return true;
                        }
                    }
                })
                // 转换类型
                .flatMap(new Function<Event<?>, Publisher<T>>() {
                    @Override
                    public Publisher<T> apply(Event<?> event) throws Exception {
                        return Flowable.just((T) event.body);
                    }
                })
                .subscribe(onNext, onError == null ? new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                } : onError);
    }

}
