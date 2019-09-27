package com.uguke.android.app;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.uguke.android.R;
import com.uguke.android.widget.SwipeBackLayout;
import com.uguke.android.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

/**
 * 界面代理
 * @author LeiJue
 */
public class LayoutDelegate {

    static final Integer PARENT_VIEW_TAG = Integer.MAX_VALUE - 9999;
    static final String FRAGMENTATION_ARG_CONTAINER = "fragmentation_arg_container";

    private SupportActivity mActivity;
    private SupportFragment mFragment;


    private Toolbar mToolbar;

    /** Activity根容器ID或加载Fragment的容器ID **/
    private int mContainerId;
    /** Activity根容器或加载Fragment的容器 **/
    private ViewGroup mParentContainer;
    private LayoutInflater mInflater;
    private View mContentView;
    private ViewGroup mContentLayout;
    private SmartRefreshLayout mRefreshLayout;
    private SwipeBackLayout mSwipeLayout;

    private List<ViewLifeCallback> mLifeCallbacks = new ArrayList<>();

    public LayoutDelegate(SupportActivity activity) {
        mActivity = activity;
    }

    public LayoutDelegate(SupportFragment fragment) {
        mFragment = fragment;
//         setContentView(0);
//         setSimpleContentView(0);
//         setNativeContentView(R.layout.android_layout_default);
//         showTips("好的");
//         setContentView(R.layout.android_layout_default);
//         setSimpleContentView(R.layout.android_layout_default);
//         setNativeContentView(R.layout.android_layout_default);
    }

    /**
     * 创建布局
     */
    public void onCreate(Bundle savedInstanceState) {
        if (mFragment == null) {
            mInflater = mActivity.getLayoutInflater();
            mContainerId = android.R.id.content;
        } else {
            mActivity = (SupportActivity) mFragment.getActivity();
            mInflater = mFragment.getLayoutInflater();
            // 获取Fragment父级容器ID
            Bundle bundle = mFragment.getArguments();
            if (savedInstanceState != null) {
                bundle = savedInstanceState;
            }
            if (bundle != null) {
                mContainerId = bundle.getInt(FRAGMENTATION_ARG_CONTAINER);
            }
        }
        initParentContainer();
        // 创建侧滑返回控件
        onCreateSwipeLayout();
        notifyStateChanged(0);
    }

    /**
     * 布局销毁
     */
    public void onDestroy() {

    }


    public void setContentView(@LayoutRes int layoutResID) {
        mContentView = mInflater.inflate(R.layout.android_layout_default, mParentContainer, false);
        mRefreshLayout = mContentView.findViewById(R.id.android_refresh);
        mInflater.inflate(layoutResID, mRefreshLayout, true);
        mToolbar = mContentView.findViewById(R.id.android_toolbar);
        notifyStateChanged(1);
    }

    public void setContentView(View view) {
        // 默认界面，常用的刷新界面
        mContentView = mInflater.inflate(R.layout.android_layout_default, mParentContainer, false);
        mRefreshLayout = mContentView.findViewById(R.id.android_refresh);
        mToolbar = mContentView.findViewById(R.id.android_toolbar);
        mRefreshLayout.addView(view);
        notifyStateChanged(1);
    }

    public void setSimpleContentView(@LayoutRes int layoutResID) {
        mContentView = mInflater.inflate(R.layout.android_layout_simple, mParentContainer, false);
        mToolbar = mContentView.findViewById(R.id.android_toolbar);
        mContentLayout = mContentView.findViewById(R.id.android_content);
        mInflater.inflate(layoutResID, mContentLayout, true);
        notifyStateChanged(1);
    }

    public void setSimpleContentView(View view) {
        // 默认界面，常用的刷新界面
        mContentView = mInflater.inflate(R.layout.android_layout_default, mParentContainer, false);
        mContentLayout = mContentView.findViewById(R.id.android_content);
        mToolbar = mContentView.findViewById(R.id.android_toolbar);
        mContentLayout.addView(view);
        notifyStateChanged(1);
    }

    public void setNativeContentView(@LayoutRes int layoutResID) {
        initParentContainer();
        Object tag = mParentContainer.getTag(R.id.android_parent_container);
        if (PARENT_VIEW_TAG.equals(tag)) {
            // 如果是手动添加的容器则清空控件
            mParentContainer.removeAllViews();
            mInflater.inflate(layoutResID, mParentContainer, true);
            mContentView = mParentContainer;
        } else {
            mContentView = mInflater.inflate(layoutResID, mParentContainer, false);
        }
        mToolbar = mContentView.findViewById(R.id.android_toolbar);
        notifyStateChanged(1);
    }

    public void setNativeContentView(View view) {
        initParentContainer();
        Object tag = mParentContainer.getTag(R.id.android_parent_container);
        if (PARENT_VIEW_TAG.equals(tag)) {
            // 如果是手动添加的容器则清空控件
            mParentContainer.removeAllViews();
            mParentContainer.addView(view);
            mContentView = mParentContainer;
        } else {
            mContentView = view;
        }
        mToolbar = mContentView.findViewById(R.id.android_toolbar);
        notifyStateChanged(1);
    }

    public void showTips(String tips) { }

    public void addLifeCallback(@NonNull ViewLifeCallback callback) {
        mLifeCallbacks.add(callback);
    }

    void notifyStateChanged(int state) {
        for (ViewLifeCallback callback : mLifeCallbacks) {
            if (state == 0) {
                callback.onCreate();
            } else if (state == 1) {
                callback.onViewCreated(mContentView);
            } else {
                callback.onDestroy();
            }
        }
    }

    /**
     * 初始化布局的父容器
     */
    void initParentContainer() {
        if (mParentContainer != null) {
            return;
        }
        // 根据容器ID获取根容器
        if (mFragment == null) {
            mParentContainer = mActivity.findViewById(mContainerId);
        } else if (mContainerId > 0) {
            Fragment parent = mFragment.getParentFragment();
            if (parent == null) {
                View view = mActivity.findViewById(mContainerId);
                if (view instanceof ViewGroup) {
                    mParentContainer = (ViewGroup) view;
                }
            } else if (parent.getView() != null) {
                View view = parent.getView().findViewById(mContainerId);
                if (view instanceof ViewGroup) {
                    mParentContainer = (ViewGroup) view;
                }
            }
        }
        // 如果不能获取到根容器，则手动生成一个根容器
        if (mParentContainer == null) {
            mParentContainer = new FrameLayout(mActivity);
            mParentContainer.setTag(R.id.android_parent_container, PARENT_VIEW_TAG);
            mParentContainer.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));
        }
    }


    /**
     * 是否支持侧滑回退
     */
    boolean isSwipeBackSupport() {
//        if (mFragment == null) {
//            return mActivity.onSwipeBackSupport();
//        }
//        return mFragment.onSwipeBackSupport();
        return false;
    }

    /**
     * 创建侧滑返回控件
     */
    void onCreateSwipeLayout() {
        if (isSwipeBackSupport()) {
            Window window = mActivity.getWindow();
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            window.getDecorView().setBackgroundColor(Color.TRANSPARENT);
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            mSwipeLayout = new SwipeBackLayout(mActivity);
            mSwipeLayout.setLayoutParams(params);
            if (mFragment == null) {
                mSwipeLayout.attachToActivity(mActivity);
            } else {
                // mSwipeLayout.attachToFragment(mFragment);
            }
        }
    }


    public Toolbar getToolbar() {
        return mToolbar;
    }

    public View getContentView() {
        return mContentView;
    }

    public SmartRefreshLayout getRefreshLayout() {
        return mRefreshLayout;
    }
}
