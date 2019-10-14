package com.uguke.demo.android;

import android.app.Application;

import com.uguke.android.app.AppDelegate;

/**
 *
 * @author LeiJue
 */
public class App extends Application {


    @Override
    public void onCreate() {
        super.onCreate();

        AppDelegate.getInstance()
                .init(this)
                .setSwipeBackSupport(true);

    }
}
