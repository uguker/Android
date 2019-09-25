package com.uguke.demo.android;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;

import com.uguke.android.app.BaseTabbedActivity;
import com.uguke.android.app.FragmentTab;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * @author LeiJue
 */
public class MainActivity extends BaseTabbedActivity {

    @Override
    public void onCreating(@Nullable Bundle savedInstanceState) {
        //setContentView(R.layout.activity_main, Style.DEFAULT);
        //loadRootFragment(R.id.content, new SSFragment());
        super.onCreating(savedInstanceState);
        Disposable d = Observable.<String>create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                //Log.e("数据", "线程" + Thread.currentThread().getName());
                emitter.onNext("1");
            }
        })
                .subscribeOn(Schedulers.io())
                .flatMap(s ->{
                    Log.e("数据", "线程" + Thread.currentThread().getName());
                    return test();
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Log.e("数据", "guo");
                Log.e("数据", "线程" + Thread.currentThread().getName());
            }
        });


        loadMultipleRootFragment(
                new FragmentTab("首页", HomeFragment.class),
                new FragmentTab("其他", SSFragment.class),
                new FragmentTab("ta的", InfoFragment.class),
                new FragmentTab("我的", MineFragment.class));

        loadMultipleRootFragment(
                new FragmentTab("首页", HomeFragment.class),
                new FragmentTab("其他", SSFragment.class),
                new FragmentTab("我的", MineFragment.class));
//        loadMultipleRootFragment(
//                new FragmentTab.Builder("首页").setFragmentClass(HomeFragment.class).build(),
//                new FragmentTab.Builder("其他").setFragmentClass(SSFragment.class).build(),
//                new FragmentTab.Builder("我的").setFragmentClass(MineFragment.class).build());
        //mTabLayout.setIconVisible(false);
        //mTabLayout.showDot(0, 10);
        //mTabLayout.setDotOffset(0, 10, 10);
        hideToolbar();

        setSwipeBackEnable(true);
    }

    @Override
    public boolean onSwipeBackSupport() {
        return true;
    }


    public Observable<String> test() {
        return Observable.<String>create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                Log.e("数据2", "线程" + Thread.currentThread().getName());
                emitter.onNext("1");
            }
        }).subscribeOn(Schedulers.io());
    }
}
