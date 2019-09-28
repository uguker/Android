package com.uguke.android.app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.uguke.android.R;
import com.uguke.android.helper.TipsHelper;
import com.uguke.android.widget.CommonToolbar;

import java.util.ArrayList;
import java.util.List;

/**
 * 界面代理
 * @author LeiJue
 */
public class LayoutDelegate {

    private static final int STATE_CREATE = 0;
    private static final int STATE_CREATED = 1;
    private static final int STATE_DESTROY = 2;

    static final Integer PARENT_VIEW_TAG = Integer.MAX_VALUE - 9999;
    static final String FRAGMENTATION_ARG_CONTAINER = "fragmentation_arg_container";

    private SupportActivity mActivity;
    private SupportFragment mFragment;

    private TipsHelper mTipsHelper;

    private CommonToolbar mToolbar;
    private LayoutInflater mInflater;
    private View mContentView;
    private ViewGroup mParentContainer;
    private ViewGroup mContentLayout;
    private SmartRefreshLayout mRefreshLayout;
    private List<ViewLifeCallback> mLifeCallbacks = new ArrayList<>();

    public LayoutDelegate(SupportActivity activity) {
        mActivity = activity;
    }

    public LayoutDelegate(SupportFragment fragment) {
        mFragment = fragment;
    }

    /**
     * 创建布局
     */
    public void onCreate(Bundle savedInstanceState) {
        if (mFragment == null) {
            mInflater = mActivity.getLayoutInflater();
            mParentContainer = mActivity.findViewById(android.R.id.content);
        } else {
            mActivity = (SupportActivity) mFragment.getActivity();
            mInflater = mFragment.getLayoutInflater();
            // 获取Fragment父级容器ID
            Bundle bundle = mFragment.getArguments() == null ? savedInstanceState : mFragment.getArguments();
            int containerId = bundle == null ? -1 : bundle.getInt(FRAGMENTATION_ARG_CONTAINER);
            // 获取父级容器ID
            Fragment parent = mFragment.getParentFragment();
            if (parent == null) {
                View view = mActivity.findViewById(containerId);
                if (view instanceof ViewGroup) {
                    mParentContainer = (ViewGroup) view;
                }
            } else if (parent.getView() != null) {
                View view = parent.getView().findViewById(containerId);
                if (view instanceof ViewGroup) {
                    mParentContainer = (ViewGroup) view;
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
        // 通知状态变化
        notifyStateChanged(STATE_CREATE);
    }

    /**
     * 布局销毁
     */
    public void onDestroy() {

        // 通知状态变化
        notifyStateChanged(STATE_DESTROY);
    }

    /**
     * 设置默认布局界面，附带标题和刷新控件
     * @param id 布局界面资源ID
     */
    public void setContentView(@LayoutRes int id) {
        // 默认界面，常用的刷新界面
        mContentView = mInflater.inflate(R.layout.android_layout_default, mParentContainer, false);
        mRefreshLayout = mContentView.findViewById(R.id.android_refresh);
        mToolbar = mContentView.findViewById(R.id.android_toolbar);
        mInflater.inflate(id, mRefreshLayout, true);
        // 通知状态变化
        notifyStateChanged(STATE_CREATED);
    }

    /**
     * 设置默认布局界面，附带标题和刷新控件
     * @param view 布局界面
     */
    public void setContentView(View view) {
        // 默认界面，常用的刷新界面
        mContentView = mInflater.inflate(R.layout.android_layout_default, mParentContainer, false);
        mRefreshLayout = mContentView.findViewById(R.id.android_refresh);
        mToolbar = mContentView.findViewById(R.id.android_toolbar);
        mRefreshLayout.addView(view);
        // 通知状态变化
        notifyStateChanged(STATE_CREATED);
    }

    /**
     * 设置简单布局界面，附带标题
     * @param id 布局界面资源ID
     */
    public void setSimpleContentView(@LayoutRes int id) {
        // 简单布局界面，只有标题
        mContentView = mInflater.inflate(R.layout.android_layout_simple, mParentContainer, false);
        mContentLayout = mContentView.findViewById(R.id.android_content);
        mToolbar = mContentView.findViewById(R.id.android_toolbar);
        mInflater.inflate(id, mContentLayout, true);
        // 通知状态变化
        notifyStateChanged(STATE_CREATED);
    }

    /**
     * 设置简单布局界面，附带标题
     * @param view 布局界面
     */
    public void setSimpleContentView(View view) {
        // 简单布局界面，只有标题
        mContentView = mInflater.inflate(R.layout.android_layout_default, mParentContainer, false);
        mContentLayout = mContentView.findViewById(R.id.android_content);
        mToolbar = mContentView.findViewById(R.id.android_toolbar);
        mContentLayout.addView(view);
        // 通知状态变化
        notifyStateChanged(STATE_CREATED);
    }

    /**
     * 设置原生布局界面
     * @param id 布局界面资源ID
     */
    public void setNativeContentView(@LayoutRes int id) {
        Object tag = mParentContainer.getTag(R.id.android_parent_container);
        if (PARENT_VIEW_TAG.equals(tag)) {
            // 如果是手动添加的容器则清空控件
            mParentContainer.removeAllViews();
            mInflater.inflate(id, mParentContainer, true);
            mContentView = mParentContainer;
        } else {
            mContentView = mInflater.inflate(id, mParentContainer, false);
        }
        // 通知状态变化
        notifyStateChanged(STATE_CREATED);
    }

    /**
     * 设置原生布局界面
     *  @param view 布局界面
     */
    public void setNativeContentView(View view) {
        Object tag = mParentContainer.getTag(R.id.android_parent_container);
        if (PARENT_VIEW_TAG.equals(tag)) {
            // 如果是手动添加的容器则清空控件
            mParentContainer.removeAllViews();
            mParentContainer.addView(view);
            mContentView = mParentContainer;
        } else {
            mContentView = view;
        }
        // 通知状态变化
        notifyStateChanged(STATE_CREATED);
    }

    public void showTips(String tips) {
        if (mTipsHelper == null) {
            mTipsHelper = TipsHelper.make(mContentView);
        }
        mTipsHelper.setText(tips).show();
    }

    public void addLifeCallback(@NonNull ViewLifeCallback callback) {
        mLifeCallbacks.add(callback);
    }

    /**
     * 通知状态变化
     */
    void notifyStateChanged(int state) {
        for (ViewLifeCallback callback : mLifeCallbacks) {
            if (state == STATE_CREATE) {
                // 布局创建
                callback.onCreate();
            } else if (state == STATE_CREATED) {
                // 布局创建完成
                callback.onViewCreated(mContentView);
            } else if (state == STATE_DESTROY) {
                // 布局销毁
                callback.onDestroy();
            }
        }
    }

//    /**
//     * 是否支持侧滑回退
//     */
//    boolean isSwipeBackSupport() {
////        if (mFragment == null) {
////            return mActivity.onSwipeBackSupport();
////        }
////        return mFragment.onSwipeBackSupport();
//        return false;
//    }
//
//    /**
//     * 创建侧滑返回控件
//     */
//    void onCreateSwipeLayout() {
//        if (isSwipeBackSupport()) {
//            Window window = mActivity.getWindow();
//            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//            window.getDecorView().setBackgroundColor(Color.TRANSPARENT);
//            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
//                    ViewGroup.LayoutParams.MATCH_PARENT,
//                    ViewGroup.LayoutParams.MATCH_PARENT);
//            mSwipeLayout = new SwipeBackLayout(mActivity);
//            mSwipeLayout.setLayoutParams(params);
//            if (mFragment == null) {
//                mSwipeLayout.attachToActivity(mActivity);
//            } else {
//                // mSwipeLayout.attachToFragment(mFragment);
//            }
//        }
//    }


    public CommonToolbar getToolbar() {
        return mToolbar;
    }

    public View getContentView() {
        return mContentView;
    }

    public SmartRefreshLayout getRefreshLayout() {
        return mRefreshLayout;
    }
}
