package com.uguke.android.app;

import android.app.Application;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;

import com.google.android.material.appbar.AppBarLayout;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.uguke.android.adapter.DefaultLoadingAdapter;
import com.uguke.android.adapter.DefaultToastAdapter;
import com.uguke.android.adapter.LoadingAdapter;
import com.uguke.android.adapter.NetworkAdapter;
import com.uguke.android.adapter.ToastAdapter;
import com.uguke.android.app.delegate.ViewHandler;
import com.uguke.android.helper.ActionHelper;
import com.uguke.android.adapter.DefaultSnackAdapter;
import com.uguke.android.adapter.SnackAdapter;
import com.uguke.android.widget.Toolbar;

import java.util.LinkedList;

/**
 * 应用委托
 * @author LeiJue
 */
public class AppDelegate {
    private static AppDelegate sInstance;
    private NetworkAdapter mNetworkAdapter;

    private SnackAdapter mSnackAdapter = new DefaultSnackAdapter();

    private ViewHandler<AppBarLayout, Toolbar> mToolbarHandler;
    private ViewHandler<RelativeLayout, SmartRefreshLayout> mRefreshHandler;

    private LoadingAdapter mLoadingAdapter = new DefaultLoadingAdapter();
    private ToastAdapter mToastAdapter = new DefaultToastAdapter();
    private LinkedList<ViewLifeCallback> mLifeCallbacks = new LinkedList<>();

    private boolean mSwipeBackSupport = false;

    public static AppDelegate getInstance() {
        if (sInstance == null) {
            sInstance = new AppDelegate();
        }
        return sInstance;
    }

    private AppDelegate() { }

    public AppDelegate setSnackAdapter(@NonNull SnackAdapter adapter) {
        mSnackAdapter = adapter;
        return this;
    }

    public AppDelegate setToastAdapter(@NonNull ToastAdapter adapter) {
        mToastAdapter = adapter;
        return this;
    }

    public AppDelegate setNetworkAdapter(NetworkAdapter adapter) {
        mNetworkAdapter = adapter;
        return this;
    }

    public AppDelegate setActionInit(Application application) {
        ActionHelper.init(application);
        return this;
    }


    public AppDelegate setToolbarHandler(ViewHandler<AppBarLayout, Toolbar> handler) {
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

    public SnackAdapter getSnackAdapter() {
        return mSnackAdapter;
    }

    public @NonNull ToastAdapter getToastAdapter() {
        return mToastAdapter;
    }

    public NetworkAdapter getNetworkAdapter() {
        return mNetworkAdapter;
    }

    public LoadingAdapter getLoadingAdapter() {
        return mLoadingAdapter;
    }

    public ViewHandler<AppBarLayout, Toolbar> getToolbarHandler() {
        return mToolbarHandler;
    }

    public ViewHandler<RelativeLayout, SmartRefreshLayout> getRefreshHandler() {
        return mRefreshHandler;
    }

}
