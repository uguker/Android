# android2
Android常用工具封装


SupportActivity继承于RxAppCompatActivity，实现ISupportActivity接口


SupportFragment继承于RxFragment，实现ISupportFragment接口


# SwipeBackHelper
辅助实现侧滑返回




#RxBus
发送事件
~
    // 发送事件
    RxBus.getInstance().send(object);
    // 发送事件
    RxBus.getInstance().send(code, object);
    // 接收事件
    // RxLifecycle生命监听接口，只接收指定的class
    RxBus.with(lifecycleProvider, class)
        // 只接收指定code事件
        .setCode(code)
        // 在生命周期什么阶段结束订阅
        .setEndEvent(endEvent)
        // 订阅
        .subscribe(subscribe);
~