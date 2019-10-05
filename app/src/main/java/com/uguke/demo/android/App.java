package com.uguke.demo.android;

import android.app.Application;

import com.uguke.android.app.AppDelegate;
import com.uguke.android.helper.ActionHelper;


/**
 *
 * @author LeiJue
 */
public class App extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        ActionHelper.init(this);

        AppDelegate.getInstance()
                .setSwipeBackSupport(true);

    }
}
