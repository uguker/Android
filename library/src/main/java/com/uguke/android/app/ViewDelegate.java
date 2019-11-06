package com.uguke.android.app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.appbar.AppBarLayout;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.uguke.android.R;
import com.uguke.android.adapter.LifecycleAdapter;
import com.uguke.android.helper.TipsHelper;
import com.uguke.android.util.CloneUtils;
import com.uguke.android.widget.CommonToolbar;
import com.uguke.android.widget.LoadingLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * 界面代理
 * @author LeiJue
 */
class ViewDelegate {

    static final Integer PARENT_VIEW_TAG = Integer.MAX_VALUE - 9999;
    static final String FRAGMENTATION_ARG_CONTAINER = "fragmentation_arg_container";

    private SupportActivity mActivity;
    private SupportFragment mFragment;
    /** 布局加载器 **/
    private LayoutInflater mInflater;
    /** 总的布局 **/
    private View mGeneralView;
    /** 标题栏 **/
    private CommonToolbar mToolbar;
    /** 父容器 **/
    private ViewGroup mParentContainer;
    /** 刷新界面 **/
    private ViewGroup mRefreshLayout;
    /** 加载布局 **/
    private LoadingLayout mLoadingLayout;
    /** 提示辅助工具 **/
    private TipsHelper mTipsHelper;
    /** 简单生命周期适配器 **/
    private LifecycleAdapter mLifecycleAdapter;
    /** 页面创建完毕回调 **/
    private List<ViewCreatedCallback> mLifeCallbacks = new ArrayList<>();

    public ViewDelegate(SupportActivity activity) {
        mActivity = activity;
        initLifecycleAdapter();
    }

    public ViewDelegate(SupportFragment fragment) {
        mFragment = fragment;
        initLifecycleAdapter();
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
                mParentContainer.setTag(R.id.__android_parent_container, PARENT_VIEW_TAG);
                mParentContainer.setLayoutParams(new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT));
            }
        }
        if (mLifecycleAdapter != null) {
            mLifecycleAdapter.onCreate(savedInstanceState);
        }
    }

    public void onViewVisible() {
        if (mLifecycleAdapter != null) {
            mLifecycleAdapter.onViewVisible(mFragment == null ? mActivity : mFragment);
        }
    }

    public void onViewInvisible() {
        if (mLifecycleAdapter != null) {
            mLifecycleAdapter.onViewInvisible(mFragment == null ? mActivity : mFragment);
        }
    }

    /**
     * 布局销毁
     */
    public void onDestroy() {
        if (mTipsHelper != null) {
            // 释放资源
            mTipsHelper.release();
        }
        if (mLifecycleAdapter != null) {
            mLifecycleAdapter.onDestroy();
        }
    }

    /**
     * 设置默认布局界面，附带标题和刷新控件
     * @param id 布局界面资源ID
     */
    public void setContentView(@LayoutRes int id) {
        // 默认界面，常用的刷新界面
        mGeneralView = mInflater.inflate(R.layout.android_layout_default, mParentContainer, false);
        mRefreshLayout = mGeneralView.findViewById(R.id.__android_refresh);
        mLoadingLayout = mGeneralView.findViewById(R.id.__android_loading);
        mToolbar = mGeneralView.findViewById(R.id.__android_toolbar);
        mInflater.inflate(id, mRefreshLayout, true);
        // 初始化头部和底部
        initHeaderAndFooter();
        // 全局处理控件
        handleViews();
        // 通知状态变化
        notifyStateChanged();
    }

    /**
     * 设置默认布局界面，附带标题和刷新控件
     * @param view 布局界面
     */
    public void setContentView(View view) {
        // 默认界面，常用的刷新界面
        mGeneralView = mInflater.inflate(R.layout.android_layout_default, mParentContainer, false);
        mRefreshLayout = mGeneralView.findViewById(R.id.__android_refresh);
        mLoadingLayout = mGeneralView.findViewById(R.id.__android_loading);
        mToolbar = mGeneralView.findViewById(R.id.__android_toolbar);
        mRefreshLayout.addView(view);
        // 初始化头部和底部
        initHeaderAndFooter();
        // 全局处理控件
        handleViews();
        // 通知状态变化
        notifyStateChanged();
    }

    /**
     * 设置简单布局界面，附带标题
     * @param id 布局界面资源ID
     */
    public void setSimpleContentView(@LayoutRes int id) {
        // 简单布局界面，只有标题
        mGeneralView = mInflater.inflate(R.layout.android_layout_simple, mParentContainer, false);
        mLoadingLayout = mGeneralView.findViewById(R.id.__android_loading);
        mToolbar = mGeneralView.findViewById(R.id.__android_toolbar);
        View view = mInflater.inflate(id, mLoadingLayout, false);
        mLoadingLayout.addView(view);
        view.setId(R.id.__android_content);
        // 初始化头部和底部
        initHeaderAndFooter();
        // 全局处理控件
        handleViews();
        // 通知状态变化
        notifyStateChanged();
    }

    /**
     * 设置简单布局界面，附带标题
     * @param view 布局界面
     */
    public void setSimpleContentView(View view) {
        // 简单布局界面，只有标题
        mGeneralView = mInflater.inflate(R.layout.android_layout_simple, mParentContainer, false);
        mLoadingLayout = mGeneralView.findViewById(R.id.__android_loading);
        mToolbar = mGeneralView.findViewById(R.id.__android_toolbar);
        mLoadingLayout.addView(view);
        view.setId(R.id.__android_content);
        // 初始化头部和底部
        initHeaderAndFooter();
        // 全局处理控件
        handleViews();
        // 通知状态变化
        notifyStateChanged();
    }

    /**
     * 设置原生布局界面
     * @param id 布局界面资源ID
     */
    public void setNativeContentView(@LayoutRes int id) {
        Object tag = mParentContainer.getTag(R.id.__android_parent_container);
        if (PARENT_VIEW_TAG.equals(tag)) {
            // 如果是手动添加的容器则清空控件
            mParentContainer.removeAllViews();
            mInflater.inflate(id, mParentContainer, true);
            mGeneralView = mParentContainer;
        } else {
            mGeneralView = mInflater.inflate(id, mParentContainer, false);
        }
        // 初始化头部和底部
        initHeaderAndFooter();
        // 全局处理控件
        handleViews();
        // 全局处理控件
        notifyStateChanged();
    }

    /**
     * 设置原生布局界面
     *  @param view 布局界面
     */
    public void setNativeContentView(View view) {
        Object tag = mParentContainer.getTag(R.id.__android_parent_container);
        if (PARENT_VIEW_TAG.equals(tag)) {
            // 如果是手动添加的容器则清空控件
            mParentContainer.removeAllViews();
            mParentContainer.addView(view);
            mGeneralView = mParentContainer;
        } else {
            mGeneralView = view;
        }
        initHeaderAndFooter();
        // 全局处理控件
        handleViews();
        // 全局处理控件
        notifyStateChanged();
        // 初始化头部和底部
    }

    public void showTips(String tips) {
        if (mTipsHelper == null) {
            mTipsHelper = TipsHelper.make(mGeneralView);
        }
        mTipsHelper.setText(tips).show();
    }

    public void addLifeCallback(@NonNull ViewCreatedCallback callback) {
        mLifeCallbacks.add(callback);
    }

    void initLifecycleAdapter() {
        LifecycleAdapter adapter = AppDelegate.getInstance().getLifecycleAdapter();
        if (adapter != null) {
            mLifecycleAdapter = CloneUtils.clone(adapter);
        }
    }

    /**
     * 初始化头部和底部
     */
    void initHeaderAndFooter() {
        ViewGroup headerParent = mGeneralView.findViewById(R.id.__android_header);
        ViewGroup footerParent = mGeneralView.findViewById(R.id.__android_footer);
        ViewCreator headerCreator = mFragment == null ?
                mActivity.onCreateHeader(headerParent) :
                mFragment.onCreateHeader(headerParent);
        ViewCreator footerCreator = mFragment == null ?
                mActivity.onCreateFooter(footerParent) :
                mFragment.onCreateFooter(footerParent);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(-1, -1);
        // 初始化头部
        if (headerCreator != null) {
            headerParent.removeAllViews();
            headerParent.addView(headerCreator.getView());
            boolean floating = headerCreator.isFloating();

            if (!floating) {
                params.addRule(RelativeLayout.BELOW, R.id.__android_header);
            }
        }
        // 初始化底部
        if (footerCreator != null) {
            footerParent.removeAllViews();
            footerParent.addView(footerCreator.getView());
            boolean floating = footerCreator.isFloating();
            if (!floating) {
                params.addRule(RelativeLayout.ABOVE, R.id.__android_footer);
            }
        }
        // 针对SlidingActivity、SlidingFragment、TabbedActivity、TabbedFragment
        if (mGeneralView.findViewById(R.id.__android_fragment) != null) {
            mGeneralView.findViewById(R.id.__android_fragment).setLayoutParams(params);
        }
        // 针对常规布局
        if (mGeneralView.findViewById(R.id.__android_content) != null) {
            mGeneralView.findViewById(R.id.__android_content).setLayoutParams(params);
        }
    }

    void handleViews() {
        // 标题
        ViewHandler<AppBarLayout, CommonToolbar> toolbarHandler = AppDelegate.getInstance().getToolbarHandler();
        AppBarLayout bar = mGeneralView.findViewById(R.id.__android_bar);
        CommonToolbar toolbar = mGeneralView.findViewById(R.id.__android_toolbar);
        if (toolbar != null) {
            toolbar.setBackListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mFragment == null) {
                        mActivity.finish();
                    } else {
                        mFragment.pop();
                    }
                }
            });
        }
        if (toolbarHandler != null && toolbar != null) {
            toolbarHandler.onHandle(mFragment == null ? mActivity : mFragment, bar, toolbar);
        }
        // 刷新控件
        ViewHandler<ViewGroup, SmartRefreshLayout> refreshHandler = AppDelegate.getInstance().getRefreshHandler();
        if (refreshHandler != null && mRefreshLayout != null && mRefreshLayout instanceof SmartRefreshLayout) {
            refreshHandler.onHandle(mFragment == null ? mActivity : mFragment, null, (SmartRefreshLayout) mRefreshLayout);
        }
        // 加载控件
        ViewHandler<ViewGroup, LoadingLayout> loadingHandler = AppDelegate.getInstance().getLoadingHandler();
        LoadingLayout loadingLayout = mGeneralView.findViewById(R.id.__android_loading);
        if (loadingHandler != null && loadingLayout != null) {
            loadingHandler.onHandle(mFragment == null ? mActivity : mFragment, null, loadingLayout);
        }

    }

    /**
     * 通知状态变化
     */
    void notifyStateChanged() {
        // 设置布局
        for (ViewCreatedCallback callback : mLifeCallbacks) {
            callback.onViewCreated(mGeneralView);
        }
        // 简单生命周期
        if (mLifecycleAdapter != null) {
            mLifecycleAdapter.onViewCreated(mFragment == null ? mActivity : mFragment, mGeneralView);
        }
    }
    
    CommonToolbar getToolbar() {
        return mToolbar;
    }

    View getContentView() {
        return mGeneralView;
    }

    SmartRefreshLayout getRefreshLayout() {
        if (mRefreshLayout instanceof SmartRefreshLayout) {
            return (SmartRefreshLayout) mRefreshLayout;
        }
        return null;
    }

    LoadingLayout getLoadingLayout() {
        return mLoadingLayout;
    }

}
