package com.uguke.android.bus;

import android.util.Log;

import androidx.annotation.NonNull;

import com.trello.rxlifecycle3.LifecycleProvider;
import com.trello.rxlifecycle3.LifecycleTransformer;
import com.trello.rxlifecycle3.android.ActivityEvent;
import com.trello.rxlifecycle3.android.FragmentEvent;
import com.trello.rxlifecycle3.components.RxActivity;
import com.trello.rxlifecycle3.components.support.RxAppCompatActivity;
import com.trello.rxlifecycle3.components.support.RxFragmentActivity;
import com.uguke.android.gen.GenClass;

import org.reactivestreams.Publisher;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

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
    private Class<T> mClass;

    @SuppressWarnings("unchecked")
    public Bus(LifecycleProvider provider) {
        super();
        mLifecycleProvider = provider;

    }

    public Bus<T> setCode(int code) {
        mEventCode = code;
        return this;
    }

//    public Bus setClass(Class<?> clazz) {
//
//        return this;
//    }

    public Bus<T> setEventCode(int code) {
        mEventCode = code;
        return this;
    }

//    public Bus setEventClass(Class<?> clazz) {
//        mEventClass = clazz;
//        return this;
//    }

    public Bus<T> setEndEvent(FragmentEvent event) {
        this.mFragmentEndEvent = event;
        return this;
    }

    public Bus<T> setEndEvent(ActivityEvent event) {
        this.mActivityEndEvent = event;
        return this;
    }

    public void subscribe(Consumer<Event<T>> onNext) {
        subscribe(onNext, null);
    }

    @SuppressWarnings("unchecked")
    public void subscribe(Consumer<Event<T>> onNext, Consumer<Throwable> onError) {
        if (mLifecycleProvider == null) {
            return;
        }

        //mClass =  (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        ParameterizedType clazz = (ParameterizedType) getClass().getGenericSuperclass();
        //Log.e("数据", "" + getClass().getName());

        T t = (T) new Object();
        Log.e("数据", "" + clazz.toString() + "|" + (clazz.getActualTypeArguments()[0]));

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
                .flatMap(new Function<Event<?>, Publisher<Event<T>>>() {
                    @Override
                    public Publisher<Event<T>> apply(Event<?> event) throws Exception {
                        Log.e("数据", "" + event.getClass().getName());
                        return Flowable.just((Event<T>)event);
                    }
                })
                .filter(new Predicate<Event<T>>() {
                    @Override
                    public boolean test(Event<T> e) throws Exception {
                        return mEventCode == e.code;
                    }
                })
                //过滤 根据code判断返回事件
                .subscribe(onNext, onError == null ? new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                } : onError);
    }

}
