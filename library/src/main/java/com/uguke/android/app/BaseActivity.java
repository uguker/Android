package com.uguke.android.app;

import android.content.Context;
import android.os.Bundle;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.AppBarLayout;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.uguke.android.R;
import com.uguke.android.adapter.NetworkAdapter;
import com.uguke.android.app.delegate.ViewActivityDelegate;
import com.uguke.android.app.delegate.ViewCallback;
import com.uguke.android.app.delegate.ViewCreator;
import com.uguke.android.helper.snack.OnDismissListener;
import com.uguke.android.widget.SwipeBackLayout;
import com.uguke.android.widget.Toolbar;

import me.yokeyword.fragmentation.ISupportFragment;
import me.yokeyword.fragmentation.SupportActivity;
import me.yokeyword.fragmentation.SupportHelper;

/**
 * 基础Activity
 * @author LeiJue
 */
public class BaseActivity extends SupportActivity implements ViewCallback, BaseView, SwipBackProvider {

    final ViewActivityDelegate mViewDelegate = new ViewActivityDelegate(this, this);

    /** 标题 **/
    public Toolbar mToolbar;
    /** 刷新控件 **/
    public SmartRefreshLayout mRefreshLayout;

    private MvpDelegate mMvpDelegate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewDelegate.onCreate(savedInstanceState);
        onCreating(savedInstanceState);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mViewDelegate.onPostCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        if (mMvpDelegate != null) {
            mMvpDelegate.onDestroy();
            mMvpDelegate = null;
        }
        NetworkAdapter adapter = AppDelegate.getInstance().getNetworkAdapter();
        if (adapter != null) {
            adapter.cancel(this);
        }
        mViewDelegate.onDestroy();
        super.onDestroy();
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public AppCompatActivity getActivity() {
        return this;
    }

    @Override
    public void onViewCreated(@NonNull View view) {
        getDelegate().setContentView(view);
        mToolbar = findViewById(R.id.android_toolbar);
        mRefreshLayout = findViewById(R.id.android_refresh);
    }

    public ViewCreator onCreateHeader(ViewGroup container) {
        return null;
    }

    public ViewCreator onCreateFooter(ViewGroup container) {
        return null;
    }

    /**
     * 获取栈内的fragment对象
     */
    public <T extends ISupportFragment> T findFragmentByTag(String tag) {
        return SupportHelper.findFragment(getSupportFragmentManager(), tag);
    }


    @Override
    public final void setContentView(@LayoutRes int id) {
        mViewDelegate.setContentView(id, Style.DEFAULT);
    }

    @Override
    public final void setContentView(View view) {
        mViewDelegate.setContentView(view, Style.DEFAULT);
    }

    @Override
    public final void setContentView(View view, ViewGroup.LayoutParams params) {
        view.setLayoutParams(params);
        mViewDelegate.setContentView(view, Style.DEFAULT);
    }

    public final void setContentView(@LayoutRes int layoutRes, Style style) {
        mViewDelegate.setContentView(layoutRes, style);
    }

    public final void setContentView(View view, Style style) {
        mViewDelegate.setContentView(view, style);
    }

    /**
     * 创建界面
     * @param savedInstanceState 存储信息
     */
    public void onCreating(@Nullable Bundle savedInstanceState) {}

    @Override
    public SwipeBackLayout getSwipeBackLayout() {
        return mViewDelegate.getSwipeBackLayout();
    }

    @Override
    public void setSwipeBackEnable(boolean enable) {
        mViewDelegate.setSwipeBackEnable(enable);

    }

    @Override
    public void setEdgeLevel(SwipeBackLayout.EdgeLevel edgeLevel) {
        mViewDelegate.setEdgeLevel(edgeLevel);
    }

    @Override
    public void setEdgeLevel(int widthPixel) {
        mViewDelegate.setEdgeLevel(widthPixel);
    }

    public boolean onSwipeBackPriority() {
        return mViewDelegate.swipeBackPriority();
    }

    @Override
    public boolean onSwipeBackSupport() {
        return AppDelegate.getInstance().isSwipeBackSupport();
    }

    /**
     * 限制SwipeBack的条件,默认栈内Fragment数 <= 1时 , 优先滑动退出Activity , 而不是Fragment
     * @return true: Activity优先滑动退出;  false: Fragment优先滑动退出
     */
    public boolean swipeBackPriority() {
        return mViewDelegate.swipeBackPriority();
    }

    @Override
    public final void showLoading(String ...args) {
        mViewDelegate.showLoading(args);
    }

    @Override
    public final void hideLoading() {
        mViewDelegate.hideLoading();
    }

    @Override
    public final void showSnack(String text) {
        mViewDelegate.showSnack(text, 1500, null);
    }

    @Override
    public final void showSnack(String text, int duration) {
        mViewDelegate.showSnack(text, duration, null);
    }

    @Override
    public final void showSnack(String text, OnDismissListener listener) {
        mViewDelegate.showSnack(text, 1500, listener);
    }

    @Override
    public final void showSnack(String text, int duration, OnDismissListener listener) {
        mViewDelegate.showSnack(text, duration, listener);
    }

    @Override
    public final void showToast(String text) {
        mViewDelegate.showToast(text, 1500);
    }

    public void showToolbar() {
        AppBarLayout appbar = findViewById(R.id.android_bar);
        if (appbar != null) {
            appbar.setVisibility(View.VISIBLE);
        }
        if (mToolbar != null) {
            mToolbar.setVisibility(View.VISIBLE);
        }
    }

    public void hideToolbar() {
        AppBarLayout appbar = findViewById(R.id.android_bar);
        if (appbar != null) {
            appbar.setVisibility(View.GONE);
        }
        if (mToolbar != null) {
            mToolbar.setVisibility(View.GONE);
        }
    }

//    public void postDelayed(Runnable runnable, long delayMillis) {
//        mViewDelegate.postDelayed(runnable, delayMillis);
//    }

    public void request(Object request) {
        NetworkAdapter adapter = AppDelegate.getInstance().getNetworkAdapter();
        if (adapter != null) {
            adapter.add(this, request);
        }
    }

    public <T extends BaseView> void attachPresenter(T view, BasePresenter<T> presenter) {
        if (mMvpDelegate == null) {
            mMvpDelegate = new MvpDelegate();
        }
        mMvpDelegate.attachViewAndPresenter(view, presenter);
    }

    public <T extends BasePresenter> T getPresenter(Class<T> clazz) {
        if (mMvpDelegate == null) {
            throw new NullPointerException("You should attach View and Presenter first.");
        }
        return mMvpDelegate.getPresenter(clazz);
    }


}
