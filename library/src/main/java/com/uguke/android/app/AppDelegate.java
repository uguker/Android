package com.uguke.android.app;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.widget.RelativeLayout;

import com.google.android.material.appbar.AppBarLayout;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.uguke.android.adapter.DefaultLoadingAdapter;
import com.uguke.android.adapter.LoadingAdapter;
import com.uguke.android.adapter.NetworkAdapter;
import com.uguke.android.helper.ActionHelper;
import com.uguke.android.swipe.SwipeBackHelper;
import com.uguke.android.widget.CommonToolbar;

import java.util.LinkedList;
import java.util.Stack;

/**
 * 应用委托
 * @author LeiJue
 */
public class AppDelegate {
    private static AppDelegate sInstance;
    private NetworkAdapter mNetworkAdapter;

    private Stack<Activity> mStack = new Stack<>();
    private Application.ActivityLifecycleCallbacks mCallbacks;
    private ViewHandler<AppBarLayout, CommonToolbar> mToolbarHandler;
    private ViewHandler<RelativeLayout, SmartRefreshLayout> mRefreshHandler;

    private LoadingAdapter mLoadingAdapter = new DefaultLoadingAdapter();
    private LinkedList<LayoutLifeCallback> mLifeCallbacks = new LinkedList<>();

    private boolean mSwipeBackSupport = false;

    public static void init(Application application) {
        getInstance();
        application.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                sInstance.mStack.add(activity);
                if (!(activity instanceof SupportActivity) && sInstance.mSwipeBackSupport) {
                    SwipeBackHelper.onCreate(activity);
                    SwipeBackHelper.onPostCreate(activity);
                }
            }

            @Override
            public void onActivityStarted(Activity activity) {
//                    if (sInstance.mAliveCount == 0) {
//                        for (ActionHelper.AppStateListener listener : sInstance.mListeners) {
//                            listener.onForeground();
//                        }
//                    }
//                    sInstance.mAliveCount ++;
            }

            @Override
            public void onActivityResumed(Activity activity) {


            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {
//                    if (sInstance.mAliveCount == 1) {
//                        for (ActionHelper.AppStateListener listener : sInstance.mListeners) {
//                            listener.onBackground();
//                        }
//                    }
//                    sInstance.mAliveCount --;
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                sInstance.mStack.remove(activity);
                // 清理键盘内存，避免内存泄漏
                //Keyboard.clearMemory(activity);
                if (!(activity instanceof SupportActivity) && sInstance.mSwipeBackSupport) {
                    SwipeBackHelper.onDestroy(activity);
                }
            }
        });
    }

    public static AppDelegate getInstance() {
        if (sInstance == null) {
            sInstance = new AppDelegate();
        }
        return sInstance;
    }

    private AppDelegate() { }

    public AppDelegate setNetworkAdapter(NetworkAdapter adapter) {
        mNetworkAdapter = adapter;
        return this;
    }

    public AppDelegate setActionInit(Application application) {
        ActionHelper.init(application);
        return this;
    }


    public AppDelegate setToolbarHandler(ViewHandler<AppBarLayout, CommonToolbar> handler) {
        mToolbarHandler = handler;
        return this;
    }

    public AppDelegate setRefreshHandler(ViewHandler<RelativeLayout, SmartRefreshLayout> handler) {
        mRefreshHandler = handler;
        return this;
    }

    public AppDelegate setSwipeBackSupport(boolean support) {
        mSwipeBackSupport = support;
        return this;
    }

    public boolean isSwipeBackSupport() {
        return mSwipeBackSupport;
    }

    public NetworkAdapter getNetworkAdapter() {
        return mNetworkAdapter;
    }

    public LoadingAdapter getLoadingAdapter() {
        return mLoadingAdapter;
    }

    public ViewHandler<AppBarLayout, CommonToolbar> getToolbarHandler() {
        return mToolbarHandler;
    }

    public ViewHandler<RelativeLayout, SmartRefreshLayout> getRefreshHandler() {
        return mRefreshHandler;
    }

}
