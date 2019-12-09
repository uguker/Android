package com.cqray.demo.android;

import android.app.Application;
import android.os.Bundle;
import android.view.View;

import com.cqray.android.adapter.LifecycleAdapter;
import com.cqray.android.app.AndroidDelegate;

/**
 *
 * @author LeiJue
 */
public class App extends Application {


    @Override
    public void onCreate() {
        super.onCreate();

//        Android.getInstance()
//                .init(this)
//                .setSwipeBackSupport(true);
//        Android.getInstance()
//                .init(this)
//                .setSwipeBackSupport(true);

        AndroidDelegate.getInstance()
                .setLifecycleAdapter(null)
                .setSwipeBackSupport(true)
                .setLifecycleAdapter(new LifecycleAdapter() {
                    @Override
                    public void onCreate(Bundle savedInstanceState) {

                    }

                    @Override
                    public void onViewCreated(Object target, View view) {

                    }

                    @Override
                    public void onViewVisible(Object target) {

                    }

                    @Override
                    public void onViewInvisible(Object target) {

                    }

                    @Override
                    public void onDestroy() {

                    }
                })
                .setSwipeBackSupport(true);
    }
}
