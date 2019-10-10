package com.uguke.android.bus;

import com.trello.rxlifecycle3.LifecycleProvider;

import io.reactivex.Flowable;
import io.reactivex.processors.FlowableProcessor;
import io.reactivex.processors.PublishProcessor;

/**
 * 事件分发工具
 * @author LeiJue
 */
public class RxBus {

    private static final class Holder {
        private static volatile RxBus INSTANCE = new RxBus();
    }

    private final FlowableProcessor<Event<?>> mProcessor;

    private RxBus() {
        // toSerialized 方法保证线程安全
        mProcessor = PublishProcessor.<Event<?>>create().toSerialized();
    }

    public static RxBus getInstance() {
        return Holder.INSTANCE;
    }

    public void send(Event<?> event) {
        if (hasSubscribers()) {
            mProcessor.onNext(event);
        }
    }

    public void send(int code, Object obj) {
        Event event = new Event<>();
        event.code = code;
        event.body = obj;
        send(event);
    }

    public Flowable<Event<?>> toFlowable() {
        return mProcessor;
    }

    public boolean hasSubscribers() {
        return mProcessor.hasSubscribers();
    }

    public static Bus with(LifecycleProvider provider) {
        return new Bus(provider);
    }
}