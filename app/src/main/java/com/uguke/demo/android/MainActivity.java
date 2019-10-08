package com.uguke.demo.android;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.uguke.android.swipe.SwipeBackHelper;
import com.uguke.android.swipe.SwipeBackLayout;

/**
 * @author LeiJue
 */
public class MainActivity extends FragmentActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_info);
        SwipeBackHelper.getCurrentPage(this)
                .setClosePercent(0.6f)
                .setSwipeEdgePercent(0.9f)
                .setOffsetPercent(0.5f)
                .addOnSwipeListener(new SwipeBackLayout.OnSwipeListener() {
                    @Override
                    public void onDragStateChange(int state) {
                        Log.e("数据", "状态");
                    }

                    @Override
                    public void onEdgeTouch(int orientation) {

                        Log.e("数据", "触摸");
                    }

                    @Override
                    public void onScrollPercent(float percent) {
                        Log.e("数据", "你离" + percent);
                    }
                });
    }

//    @Override
//    public void oncreating(@Nullable Bundle savedInstanceState) {
//        //setContentView(R.layout.activity_main, Style.DEFAULT);
//        //loadRootFragment(R.id.content, new SSFragment());
//        super.onCreating(savedInstanceState);
//        setContentView(R.layout.fragment_info);
////        Disposable d = Observable.<String>create(new ObservableOnSubscribe<String>() {
////            @Override
////            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
////                //Log.e("数据", "线程" + Thread.currentThread().getName());
////                emitter.onNext("1");
////            }
////        })
////                .subscribeOn(Schedulers.io())
////                .flatMap(s ->{
////                    Log.e("数据", "线程" + Thread.currentThread().getName());
////                    return test();
////                })
////                .observeOn(AndroidSchedulers.mainThread())
////                .subscribe(new Consumer<String>() {
////            @Override
////            public void accept(String s) throws Exception {
////                Log.e("数据", "guo");
////                Log.e("数据", "线程" + Thread.currentThread().getName());
////            }
////        });
//
//
////        loadMultipleRootFragment(
////                new FragmentTab("首页", HomeFragment.class),
////                new FragmentTab("其他", SSFragment.class),
////                new FragmentTab("ta的", InfoFragment.class),
////                new FragmentTab("我的", MineFragment.class));
//
////        loadMultipleRootFragment(
////                new FragmentTab("首页", HomeFragment.class),
////                new FragmentTab("其他", SSFragment.class),
////                new FragmentTab("我的", MineFragment.class));
////        loadMultipleRootFragment(
////                new FragmentTab.Builder("首页").setFragmentClass(HomeFragment.class).build(),
////                new FragmentTab.Builder("其他").setFragmentClass(SSFragment.class).build(),
////                new FragmentTab.Builder("我的").setFragmentClass(MineFragment.class).build());
//        //mTabLayout.setIconVisible(false);
//        //mTabLayout.showDot(0, 10);
//        //mTabLayout.setDotOffset(0, 10, 10);
//
////        mToolbar.setTitle("")
////                .setTitle("")
////                .setTitle("")
////                .setTitle("");
//
//
//
//    }
//
//    @Override
//    public boolean onSwipeBackSupport() {
//        return true;
//    }

//
//    public Observable<String> test() {
//        return Observable.<String>create(new ObservableOnSubscribe<String>() {
//            @Override
//            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
//                Log.e("数据2", "线程" + Thread.currentThread().getName());
//                emitter.onNext("1");
//            }
//        }).subscribeOn(Schedulers.io());
//    }
}
