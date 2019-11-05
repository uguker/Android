package com.uguke.android.app;

import android.os.Bundle;
import android.util.Log;
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
import com.uguke.android.adapter.LoadingAdapter;
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
    private TipsHelper mTipsHelper;
    private CommonToolbar mToolbar;
    private LayoutInflater mInflater;
    private View mContentView;
    private ViewGroup mParentContainer;
    private ViewGroup mRefreshLayout;
    private LoadingLayout mLoadingLayout;
    private LifecycleAdapter mLifecycleAdapter;
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
                mParentContainer.setTag(R.id.android_parent_container, PARENT_VIEW_TAG);
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
        mContentView = mInflater.inflate(R.layout.android_layout_default, mParentContainer, false);
        mRefreshLayout = mContentView.findViewById(R.id.android_refresh);
        mLoadingLayout = mContentView.findViewById(R.id.android_loading);
        mToolbar = mContentView.findViewById(R.id.android_toolbar);
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
        mContentView = mInflater.inflate(R.layout.android_layout_default, mParentContainer, false);
        mRefreshLayout = mContentView.findViewById(R.id.android_refresh);
        mLoadingLayout = mContentView.findViewById(R.id.android_loading);
        mToolbar = mContentView.findViewById(R.id.android_toolbar);
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
        mContentView = mInflater.inflate(R.layout.android_layout_simple, mParentContainer, false);
        mLoadingLayout = mContentView.findViewById(R.id.android_loading);
        mToolbar = mContentView.findViewById(R.id.android_toolbar);
        View view = mInflater.inflate(id, mLoadingLayout, false);
        mLoadingLayout.addView(view);
        view.setId(R.id.android_content);
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
        mContentView = mInflater.inflate(R.layout.android_layout_simple, mParentContainer, false);
        mLoadingLayout = mContentView.findViewById(R.id.android_loading);
        mToolbar = mContentView.findViewById(R.id.android_toolbar);
        mLoadingLayout.addView(view);
        view.setId(R.id.android_content);
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
        Object tag = mParentContainer.getTag(R.id.android_parent_container);
        if (PARENT_VIEW_TAG.equals(tag)) {
            // 如果是手动添加的容器则清空控件
            mParentContainer.removeAllViews();
            mInflater.inflate(id, mParentContainer, true);
            mContentView = mParentContainer;
        } else {
            mContentView = mInflater.inflate(id, mParentContainer, false);
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
        Object tag = mParentContainer.getTag(R.id.android_parent_container);
        if (PARENT_VIEW_TAG.equals(tag)) {
            // 如果是手动添加的容器则清空控件
            mParentContainer.removeAllViews();
            mParentContainer.addView(view);
            mContentView = mParentContainer;
        } else {
            mContentView = view;
        }

        initHeaderAndFooter();
        // 全局处理控件
        handleViews();
        // 全局处理控件
        notifyStateChanged();
        // 初始化头部和底部
    }

//    public ViewCreator onCreateHeader(ViewGroup container) {
//        return null;
//    }
//
//    public ViewCreator onCreateFooter(ViewGroup container) {
//        return null;
//    }

    public void showTips(String tips) {
        if (mTipsHelper == null) {
            mTipsHelper = TipsHelper.make(mContentView);
        }
        mTipsHelper.setText(tips).show();
    }


//    /**
//     * 显示加载
//     */
//    public void showLoading(String ...texts) {
//        if (mContentView == null) {
//            throw new IllegalStateException("can't request showLoading before contentView inflated.");
//        }
//        // 初始化加载容器
//        initLoadingContainer();
//        LoadingAdapter adapter = AppDelegate.getInstance().getLoadingAdapter();
//        boolean isUseTexts = adapter.isUseTexts();
//        boolean isEmptyText = texts == null || texts.length == 0;
//        // 如果加载框使用文本信息或文本信息为空
//        if (isUseTexts || isEmptyText) {
//            adapter.show(texts);
//        } else {
//            if (mTipsHelper == null) {
//                mTipsHelper = TipsHelper.make(mLoadingLayout);
//            }
//            mLoadingLayout.setVisibility(View.VISIBLE);
//            mTipsHelper.setView(mLoadingLayout)
//                    .setDuration(TipsHelper.DURATION_MANUAL)
//                    .setText(texts[0])
//                    .show();
//        }
//    }

    /**
     * 隐藏加载
     */
    public void hideLoading() {
        LoadingAdapter adapter = AppDelegate.getInstance().getLoadingAdapter();
        adapter.hide();
        if (mTipsHelper != null) {
            mTipsHelper.hide();
        }
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

    void initHeaderAndFooter() {
        ViewGroup headerParent = mContentView.findViewById(R.id.android_header);
        ViewGroup footerParent = mContentView.findViewById(R.id.android_footer);
        ViewCreator headerCreator = mFragment == null ?
                mActivity.onCreateHeader(headerParent) :
                mFragment.onCreateHeader(headerParent);
        ViewCreator footerCreator = mFragment == null ?
                mActivity.onCreateFooter(footerParent) :
                mFragment.onCreateFooter(footerParent);
//        View contentView = null;
        if (mContentView.getId() == R.id.android_fragment ||
                mContentView.getId() == R.id.android_content) {
            Log.e("数据", "是这样的");
//            ViewGroup parent = (ViewGroup) mContentView;
//            if (parent.getChildCount() > 0) {
//                contentView = parent.getChildAt(0).findViewById(R.id.android_fragment);
//                if (contentView == null) {
//                    contentView = parent.getChildAt(0).findViewById(R.id.android_content);
//                }
//            }
        }
//        else {
//            Log.e("数据", "是这样的2");
//            contentView = mContentView.findViewById(R.id.android_fragment);
//            if (contentView == null) {
//                contentView = mContentView.findViewById(R.id.android_content);
//            }
//        }
//        if (contentView == null) {
//            return;
//        }
//
//        Log.e("数据", "是这样的3");
        // 初始化头部
        if (headerCreator != null) {
            if (headerParent.getChildCount() == 0) {
                View header = LayoutInflater.from(mActivity).inflate(headerCreator.getLayoutResId(), headerParent, true);
                //header.bringToFront();
            }
            boolean floating = headerCreator.isFloating();
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(-1, -1);
            if (!floating) {
                params.addRule(RelativeLayout.BELOW, R.id.android_header);
            }
            if (mContentView.findViewById(R.id.android_fragment) != null) {
                mContentView.findViewById(R.id.android_fragment).setLayoutParams(params);
            }

            if (mContentView.findViewById(R.id.android_content) != null) {
                Log.e("数据", "是这样的2");
                mContentView.findViewById(R.id.android_content).setLayoutParams(params);
            }
            //contentView.setLayoutParams(params);
//
//            params = (RelativeLayout.LayoutParams) headerParent.getLayoutParams();
//            params.addRule(RelativeLayout.BELOW, R.id.android_tab);

//            headerParent.removeAllViews();
//            // 根据额外数据来判定是否浮动
//            Object extras = headerCreator.getExtras();
//            boolean floating = extras != null && (extras instanceof Boolean ? (Boolean) extras : true);
//            View header = LayoutInflater.from(mActivity).inflate(headerCreator.getLayoutResId(), headerParent, true);
//            header.bringToFront();
//            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(-1, -1);
//            if (!floating) {
//                params.addRule(RelativeLayout.BELOW, R.id.android_header);
//            }
//            contentView.setLayoutParams(params);
        }
        // 初始化底部
        if (footerCreator != null) {
//            // 根据额外数据来判定是否浮动
//            Object extras = footerCreator.getExtras();
//            // 额外数据为空，直接判定为不浮动
//            // 额外数据为布尔类型，false为不浮动
//            boolean floating = extras != null && (extras instanceof Boolean ? (Boolean) extras : true);
//            View footer = LayoutInflater.from(mActivity).inflate(footerCreator.getLayoutResId(), footerParent, true);
//            footer.bringToFront();
//
//            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(-1, -1);
//            if (!floating) {
//                params.addRule(RelativeLayout.ABOVE, R.id.android_header);
//            }
//            contentView.setLayoutParams(params);

            if (footerParent.getChildCount() == 0) {
                View footer = LayoutInflater.from(mActivity).inflate(footerCreator.getLayoutResId(), footerParent, true);
                //footer.bringToFront();
            }
            boolean floating = footerCreator.isFloating();
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(-1, -1);
            if (!floating) {
                params.addRule(RelativeLayout.ABOVE, R.id.android_footer);
            }
            if (mContentView.findViewById(R.id.android_fragment) != null) {
                mContentView.findViewById(R.id.android_fragment).setLayoutParams(params);
            }

            if (mContentView.findViewById(R.id.android_content) != null) {
                mContentView.findViewById(R.id.android_content).setLayoutParams(params);
            }


            //contentView.setLayoutParams(params);
        }
    }

    void handleViews() {
        // 标题
        ViewHandler<AppBarLayout, CommonToolbar> toolbarHandler = AppDelegate.getInstance().getToolbarHandler();
        AppBarLayout bar = mContentView.findViewById(R.id.android_bar);
        CommonToolbar toolbar = mContentView.findViewById(R.id.android_toolbar);
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
        LoadingLayout loadingLayout = mContentView.findViewById(R.id.android_loading);
        if (loadingHandler != null && loadingLayout != null) {
            loadingHandler.onHandle(mFragment == null ? mActivity : mFragment, null, loadingLayout);
        }

    }

//    /**
//     * 初始化加载容器
//     */
//    void initLoadingContainer() {
//        // 添加布局
//        if (mLoadingLayout == null) {
//            ViewGroup parent = mContentLayout == null ? mParentContainer : mContentLayout;
//            // 初始化加载容器
//            mLoadingLayout = (CoordinatorLayout) mInflater.inflate(R.layout.android_layout_loading, parent, false);
//            parent.addView(mLoadingLayout);
//            LoadingView view = (LoadingView) mLoadingLayout.getChildAt(0);
//            AppDelegate.getInstance()
//                    .getLoadingAdapter()
//                    .convert(mFragment == null ? mActivity : mFragment, mLoadingLayout, view);
//        }
//    }

    /**
     * 通知状态变化
     */
    void notifyStateChanged() {
        // 设置布局
        for (ViewCreatedCallback callback : mLifeCallbacks) {
            callback.onViewCreated(mContentView);
        }
        // 简单生命周期
        if (mLifecycleAdapter != null) {
            mLifecycleAdapter.onViewCreated(mFragment == null ? mActivity : mFragment, mContentView);
        }
    }
    
    CommonToolbar getToolbar() {
        return mToolbar;
    }

    View getContentView() {
        return mContentView;
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
