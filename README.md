# AndroidUtils
Android常用工具封装
SupportActivity继承于RxAppCompatActivity，实现ISupportActivity接口


SupportFragment继承于RxFragment，实现ISupportFragment接口

## 使用的第三方类库

* 上拉下拉刷新 https://github.com/scwang90/SmartRefreshLayout
* 万能Adapter https://github.com/CymChad/BaseRecyclerViewAdapterHelper
* Fragment管理 https://github.com/YoKeyword/Fragmentation
* RxJava https://github.com/ReactiveX/RxJava
* RxAndroid https://github.com/ReactiveX/RxAndroid
* Rx生命周期管理 https://github.com/trello/RxLifecycle

## 目录
-[SwipeBackHelper 侧滑返回](##SwipeBackHelper 侧滑返回)<br>
-[RxBus 事件分发](##RxBus 事件分发)<br>

## SwipeBackHelper 侧滑返回
辅助实现侧滑返回
### 启用侧滑返回
重写SupportActivity或SupportFragment中的onSwipeBackSupport
```
public boolean onSwipeBackSupport() {
    return ture;
}
```
或者设置全局启用
```
AppDelegate.getInstance().setSwipeBackSupport(true);
```
### 其他设置
Fragment只有继承了SupportFragment才能使用，如设置了全局启用，则任何Activity均可使用， 否则只有SupportActivity可用。
``` 
SwipeBackHelper.getCurrentPage()
    // 设置能否滑动
    .setSwipeBackEnable(enable);
    // 滑动中，上一个页面View的遮罩透明度（0~1），默认0.5
    .setScrimAlpha(alpha);
    // 滑动中，上一个页面View的遮罩颜色
    .setScrimColor(color);
    // 滑动方向，默认从左边开始
    .setEdgeOrientation(orientation);
    // 可以启动滑动的范围
    .setSwipeEdge(int widthPixel);
    // 可以启动滑动的范围百分比（0~1）
    .setSwipeEdgePercent(percent);
    // 设置阴影Drawable
    .setShadow(shadow, orientation);
    // 设置阴影资源
    .setShadow(resId, orientation);
    // 设置滑动时上一个界面的偏移百分比
    .setOffsetPercent(percent);
    // 设置滑动到多少百分比后松手会关闭界面
    .setClosePercent(percent);
    // 监听事件
    .addOnSwipeListener(listener);
```

## RxBus 事件分发

### 发送事件
```
RxBus.getInstance().send(object);
RxBus.getInstance().send(code, object);
```

### 接收事件
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