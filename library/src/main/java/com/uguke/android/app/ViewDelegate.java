package com.uguke.android.app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.LayoutRes;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.uguke.android.R;
import com.uguke.android.adapter.LifecycleAdapter;
import com.uguke.android.helper.TipsHelper;
import com.uguke.android.widget.CommonToolbar;
import com.uguke.android.widget.LoadingLayout;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * 界面代理
 * @author LeiJue
 */
class ViewDelegate implements ViewProvider {

    private SupportActivity mActivity;
    private SupportFragment mFragment;
    /** 布局加载器 **/
    private LayoutInflater mInflater;
    /** 总的布局 **/
    private View mContentView;
    /** 标题栏 **/
    private CommonToolbar mToolbar;
    /** 刷新界面 **/
    private ViewGroup mRefreshLayout;
    /** 加载布局 **/
    private LoadingLayout mLoadingLayout;
    /** 提示辅助工具 **/
    private TipsHelper mTipsHelper;
    /** 简单生命周期适配器 **/
    private LifecycleAdapter mLifecycleAdapter;

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
        } else {
            mActivity = (SupportActivity) mFragment.getActivity();
            mInflater = mFragment.getLayoutInflater();
        }
        if (mLifecycleAdapter != null) {
            mLifecycleAdapter.onCreate(savedInstanceState);
        }
    }

    /**
     * 界面显示
     */
    public void onViewVisible() {
        if (mLifecycleAdapter != null) {
            mLifecycleAdapter.onViewVisible(mFragment == null ? mActivity : mFragment);
        }
    }

    /**
     * 界面隐藏
     */
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
        ViewGroup root = mActivity.findViewById(android.R.id.content);
        mContentView = mInflater.inflate(R.layout.android_layout_default, root, false);
        mRefreshLayout = mContentView.findViewById(R.id.__android_refresh);
        mLoadingLayout = mContentView.findViewById(R.id.__android_loading);
        mToolbar = mContentView.findViewById(R.id.__android_toolbar);
        mInflater.inflate(id, mRefreshLayout, true);
        // 全局处理控件
        onHandleViews();
        // 创建头部和底部
        onCreateHeaderAndFooter();
        // 通知状态变化
        onViewCreated(mContentView);
    }

    /**
     * 设置默认布局界面，附带标题和刷新控件
     * @param view 布局界面
     */
    public void setContentView(View view) {
        // 默认界面，常用的刷新界面
        ViewGroup root = mActivity.findViewById(android.R.id.content);
        mContentView = mInflater.inflate(R.layout.android_layout_default, root, false);
        mRefreshLayout = mContentView.findViewById(R.id.__android_refresh);
        mLoadingLayout = mContentView.findViewById(R.id.__android_loading);
        mToolbar = mContentView.findViewById(R.id.__android_toolbar);
        mRefreshLayout.addView(view);
        // 全局处理控件
        onHandleViews();
        // 创建头部和底部
        onCreateHeaderAndFooter();
        // 通知状态变化
        onViewCreated(mContentView);
    }

    /**
     * 设置原生布局界面
     * @param id 布局界面资源ID
     */
    public void setNativeContentView(@LayoutRes int id) {
        ViewGroup root = mActivity.findViewById(android.R.id.content);
        // 布局控件
        mContentView = mInflater.inflate(id, root, false);
        // 全局处理控件
        onHandleViews();
        // 创建头部和底部
        onCreateHeaderAndFooter();
        // 全局处理控件
        onViewCreated(mContentView);
    }

    /**
     * 设置原生布局界面
     *  @param view 布局界面
     */
    public void setNativeContentView(View view) {
        mContentView = view;
        // 全局处理控件
        onHandleViews();
        // 创建头部和底部
        onCreateHeaderAndFooter();
        // 全局处理控件
        onViewCreated(mContentView);
    }

    public void showTips(String tips) {
        if (mTipsHelper == null) {
            mTipsHelper = TipsHelper.make(mContentView);
        }
        mTipsHelper.setText(tips).show();
    }

    public void showLoadingTips(String tips) {
        if (mTipsHelper == null) {
            mTipsHelper = TipsHelper.make(mContentView);
        }
        mTipsHelper.setText(tips)
                .setDuration(TipsHelper.DURATION_MANUAL)
                .show();
    }

    public void hideLoadingTips() {
        if (mTipsHelper != null) {
            if (mLoadingLayout != null) {
                mLoadingLayout.setChildClickable(true);
            }
            mTipsHelper.hide();
        }
    }

    void initLifecycleAdapter() {
        LifecycleAdapter adapter = AndroidDelegate.getInstance().getLifecycleAdapter();
        try {
            //写入字节流
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ObjectOutputStream obs = new ObjectOutputStream(out);
            obs.writeObject(adapter);
            obs.close();
            //分配内存，写入原始对象，生成新对象
            ByteArrayInputStream ios = new ByteArrayInputStream(out.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(ios);
            //返回生成的新对象
            mLifecycleAdapter = (LifecycleAdapter) ois.readObject();
            ois.close();
        } catch (Exception ignored) {}
    }

    /**
     * 创建头部和底部
     */
    void onCreateHeaderAndFooter() {
        ViewGroup headerParent = mContentView.findViewById(R.id.__android_header);
        ViewGroup footerParent = mContentView.findViewById(R.id.__android_footer);
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
        if (mContentView.findViewById(R.id.__android_fragment) != null) {
            mContentView.findViewById(R.id.__android_fragment).setLayoutParams(params);
        }
        // 针对常规布局
        if (mContentView.findViewById(R.id.__android_content) != null) {
            mContentView.findViewById(R.id.__android_content).setLayoutParams(params);
        }
    }

    void onHandleViews() {
        Object target = mFragment == null ? mActivity : mFragment;
        // 标题
        ViewHandler<CommonToolbar> toolbarHandler = AndroidDelegate.getInstance().getToolbarHandler();
        if (mToolbar != null) {
            mToolbar.setBackListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mFragment == null) {
                        mActivity.finish();
                    } else {
                        mFragment.pop();
                    }
                }
            });
            if (toolbarHandler != null) {
                toolbarHandler.onHandle(target, mToolbar);
            }
        }
        // 刷新控件
        ViewHandler<SmartRefreshLayout> refreshHandler = AndroidDelegate.getInstance().getRefreshHandler();
        if (refreshHandler != null && mRefreshLayout != null && mRefreshLayout instanceof SmartRefreshLayout) {
            refreshHandler.onHandle(target, (SmartRefreshLayout) mRefreshLayout);
        }
        // 加载控件
        ViewHandler<LoadingLayout> loadingHandler = AndroidDelegate.getInstance().getLoadingHandler();
        if (loadingHandler != null && mLoadingLayout != null) {
            loadingHandler.onHandle(target, mLoadingLayout);
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

    @Override
    public void onViewCreated(View view) {
        // 设置布局
        if (mFragment == null) {
            mActivity.onViewCreated(view);
        } else {
            mFragment.onViewCreated(view);
        }
        // 简单生命周期
        if (mLifecycleAdapter != null) {
            mLifecycleAdapter.onViewCreated(mFragment == null ? mActivity : mFragment, view);
        }
    }

//    @Override
//    public void showContent() {
//        if (mLoadingLayout != null) {
//            mLoadingLayout.showContent();
//        }
//    }
//
//    @Override
//    public void showEmpty(String... texts) {
//        if (mLoadingLayout != null) {
//            if (texts == null || texts.length == 0) {
//                mLoadingLayout.showEmpty();
//            } else {
//                mLoadingLayout.showEmpty(texts[0]);
//            }
//        }
//    }
//
//    @Override
//    public void showError(String... texts) {
//        if (mLoadingLayout != null) {
//            if (texts == null || texts.length == 0) {
//                mLoadingLayout.showError();
//            } else {
//                mLoadingLayout.showError(texts[0]);
//            }
//        }
//    }
//
//    @Override
//    public void showLoading(String... texts) {
//        if (mLoadingLayout != null) {
//            if (texts == null || texts.length == 0) {
//                mLoadingLayout.showLoading();
//            } else {
//                mLoadingLayout.showLoading(texts[0]);
//            }
//        }
//    }
}
