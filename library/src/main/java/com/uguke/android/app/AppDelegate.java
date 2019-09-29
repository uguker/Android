package com.uguke.android.app;

import android.app.Application;
import android.widget.RelativeLayout;

import com.google.android.material.appbar.AppBarLayout;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.uguke.android.adapter.DefaultLoadingAdapter;
import com.uguke.android.adapter.LoadingAdapter;
import com.uguke.android.adapter.NetworkAdapter;
import com.uguke.android.helper.ActionHelper;
import com.uguke.android.widget.CommonToolbar;

import java.util.LinkedList;

/**
 * 应用委托
 * @author LeiJue
 */
public class AppDelegate {
    private static AppDelegate sInstance;
    private NetworkAdapter mNetworkAdapter;

    private ViewHandler<AppBarLayout, CommonToolbar> mToolbarHandler;
    private ViewHandler<RelativeLayout, SmartRefreshLayout> mRefreshHandler;

    private LoadingAdapter mLoadingAdapter = new DefaultLoadingAdapter();
    private LinkedList<LayoutLifeCallback> mLifeCallbacks = new LinkedList<>();

    private boolean mSwipeBackSupport = false;

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
