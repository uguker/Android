# AndroidUtils
Android常用工具封装


## 使用的第三方类库

* 上拉下拉刷新 https://github.com/scwang90/SmartRefreshLayout
* 万能Adapter https://github.com/CymChad/BaseRecyclerViewAdapterHelper
* Fragment管理 https://github.com/YoKeyword/Fragmentation
* RxJava https://github.com/ReactiveX/RxJava
* RxAndroid https://github.com/ReactiveX/RxAndroid
* Rx生命周期管理 https://github.com/trello/RxLifecycle


SupportActivity继承于RxAppCompatActivity，实现ISupportActivity接口


SupportFragment继承于RxFragment，实现ISupportFragment接口


## SwipeBackHelper
辅助实现侧滑返回




## RxBus 事件分发

发送事件
```
RxBus.getInstance().send(object);
RxBus.getInstance().send(code, object);
```

接收事件
```
// RxLifecycle生命监听接口，只接收指定的class
RxBus.with(lifecycleProvider, class)
    // 只接收指定code事件
    .setCode(code)
    // 在生命周期什么阶段结束订阅
    .setEndEvent(endEvent)
    // 订阅
    .subscribe(subscribe);
```