package com.uguke.demo.android;

import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.uguke.android.bus.RxBus;
/**
 * @author LeiJue
 */
public class MainActivity extends FragmentActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_test);
        RxBus.getInstance().send(1, "车市成功");
        RxBus.getInstance().send(1, 10);

        FloatingActionButton btn = findViewById(R.id.btn);
//        btn.setOnClickListener(v -> {
//
//            Snackbar.make(btn, "6666", 1500).show();
//        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Snackbar.make(btn, "6666", 1500).show();
            }
        }, 3000);
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
