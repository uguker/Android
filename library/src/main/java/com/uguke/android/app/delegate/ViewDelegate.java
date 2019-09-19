package com.uguke.android.app.delegate;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.LayoutRes;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.google.android.material.appbar.AppBarLayout;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.uguke.android.R;
import com.uguke.android.adapter.LoadingAdapter;
import com.uguke.android.adapter.ToastAdapter;
import com.uguke.android.app.AppDelegate;
import com.uguke.android.app.BaseActivity;
import com.uguke.android.app.BaseFragment;
import com.uguke.android.app.BasePageActivity;
import com.uguke.android.app.BasePageFragment;
import com.uguke.android.app.BaseSlidingActivity;
import com.uguke.android.app.BaseSlidingFragment;
import com.uguke.android.app.BaseTabbedActivity;
import com.uguke.android.app.BaseTabbedFragment;
import com.uguke.android.app.Style;
import com.uguke.android.helper.snack.OnDismissListener;
import com.uguke.android.helper.snack.SnackHelper;
import com.uguke.android.util.ButterKnifeUtils;
import com.uguke.android.widget.LoadingView;
import com.uguke.android.widget.SwipeBackLayout;
import com.uguke.android.widget.Toolbar;

import java.util.LinkedList;

import me.yokeyword.fragmentation.SupportFragment;

/**
 * 布局委托
 * @author LeiJue
 */
public class ViewDelegate {

    static final Integer PARENT_VIEW_TAG = Integer.MAX_VALUE - 9999;
    static final String FRAGMENTATION_ARG_CONTAINER = "fragmentation_arg_container";

    protected SupportFragment mFragment;
    protected FragmentActivity mActivity;
    protected Object mUnBinder;

    /** 根布局 **/
    private ViewGroup mParentContainer;
    /** 布局 **/
    private View mContentView;
    /** 顶部容器 **/
    private FrameLayout mHeaderLayout;
    /** 底部容器 **/
    private FrameLayout mFooterLayout;
    /** 内容容器 **/
    private ViewGroup mContentLayout;
    /** 加载容器 **/
    private CoordinatorLayout mLoadingLayout;
    /** 刷新控件 **/
    private SmartRefreshLayout mRefreshLayout;

    private LinkedList<ViewCallback> mViewCallbacks;

    private int mContainerId;
    private LayoutInflater mInflater;

    protected Style mStyle;
    protected SwipeBackLayout mSwipeBackLayout;


    private SnackHelper mSnackHelper;

    public ViewDelegate(FragmentActivity activity, ViewCallback callback) {
        mViewCallbacks = new LinkedList<>();
        mViewCallbacks.add(callback);
        mActivity = activity;
    }

    public ViewDelegate(SupportFragment fragment, ViewCallback callback) {
        mViewCallbacks = new LinkedList<>();
        mViewCallbacks.add(callback);
        mFragment = fragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        if (mFragment == null) {
            mInflater = mActivity.getLayoutInflater();
            mContainerId = android.R.id.content;
        } else {
            mActivity = mFragment.getActivity();
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
    }

    public void onDestroy() {
        if (mUnBinder != null) {
            ButterKnifeUtils.unbind(mUnBinder);
            mUnBinder = null;
        }
        if (mSnackHelper != null) {
            mSnackHelper.onDestroy();
        }
    }

    /**
     * 设置内容布局
     */
    public void setContentView(@LayoutRes int id, Style style) {
        mStyle = style;
        // 检测是否支持设置布局
        checkContentSupport();
        // 初始化布局父级容器
        initParentContainer();
        // 设置布局文件
        if (Style.isNative(mStyle)) {
            Object tag = mParentContainer.getTag(R.id.android_parent_container);
            if (PARENT_VIEW_TAG.equals(tag)) {
                // 如果是手动添加的容器则清空控件
                mParentContainer.removeAllViews();
                mInflater.inflate(id, mParentContainer, true);
                mContentView = mParentContainer;
            } else {
                mContentView = mInflater.inflate(id, mParentContainer, false);
            }
        } else {
            // 默认界面，常用的刷新界面
            mContentView = mInflater.inflate(R.layout.android_layout_default, mParentContainer, false);
            mRefreshLayout = mContentView.findViewById(R.id.android_refresh);
            mInflater.inflate(id, mRefreshLayout, true);
        }
        initView();
    }

    /**
     * 设置内容布局
     */
    public void setContentView(View view, Style style) {
        mStyle = style;
        checkContentSupport();
        // 初始化布局父级容器
        initParentContainer();
        // 设置布局文件
        if (Style.isNative(mStyle)) {
            Object tag = mParentContainer.getTag(R.id.android_parent_container);
            if (PARENT_VIEW_TAG.equals(tag)) {
                // 如果是手动添加的容器则清空控件
                mParentContainer.removeAllViews();
                mParentContainer.addView(view);
                mContentView = mParentContainer;
            } else {
                mContentView = view;
            }
        } else {
            // 默认界面，常用的刷新界面
            mContentView = mInflater.inflate(R.layout.android_layout_default, mParentContainer, false);
            mRefreshLayout = mContentView.findViewById(R.id.android_refresh);
            mRefreshLayout.addView(view);
        }
        initView();
    }

    /**
     * 显示加载
     */
    public void showLoading(String ...texts) {
        if (mContentView == null) {
            throw new IllegalStateException("can't request showLoading before contentView inflated.");
        }
        // 初始化加载容器
        initLoadingContainer();
        LoadingAdapter adapter = AppDelegate.getInstance().getLoadingAdapter();
        boolean isUseTexts = adapter.isUseTexts();
        boolean isEmptyText = texts == null || texts.length == 0;
        // 如果加载框使用文本信息或文本信息为空
        if (isUseTexts || isEmptyText) {
            adapter.show(texts);
        } else {
            if (mSnackHelper == null) {
                mSnackHelper = SnackHelper.make(mLoadingLayout);
            }
            mLoadingLayout.setVisibility(View.VISIBLE);
            mSnackHelper.setView(mLoadingLayout)
                    .setDuration(SnackHelper.DURATION_MANUAL)
                    .setText(texts[0])
                    .show();
        }
    }

    /**
     * 隐藏加载
     */
    public void hideLoading() {
        LoadingAdapter adapter = AppDelegate.getInstance().getLoadingAdapter();
        adapter.hide();
        if (mSnackHelper != null) {
            mSnackHelper.dismiss();
        }
    }

    /**
     * 显示Snackbar
     */
    public void showSnack(String text, int duration, OnDismissListener listener) {
        if (mContentView == null) {
            return;
        }
        if (mSnackHelper == null) {
            mSnackHelper = SnackHelper.make(mContentView);
            mSnackHelper.setAdapter(AppDelegate.getInstance().getSnackAdapter());
        }
        mSnackHelper.setView(mContentView)
                .setText(text)
                .setDuration(duration)
                .addOnDismissListener(listener)
                .show();
    }

    /**
     * 显示Toast
     */
    public void showToast(String text, int duration) {
        ToastAdapter adapter = AppDelegate.getInstance().getToastAdapter();
        adapter.show(mActivity, text, duration);
    }

    public View getContentView() {
        return mContentView;
    }

    public SmartRefreshLayout getRefreshLayout() {
        return mRefreshLayout;
    }

    public SwipeBackLayout getSwipeBackLayout() {
        return mSwipeBackLayout;
    }

    void initView() {
        mContentLayout = mContentView.findViewById(R.id.android_content);
        mRefreshLayout = mContentView.findViewById(R.id.android_refresh);


        onCreateSwipeBackLayout();
        initViewHandler();
        initHeaderAndFooter();

        for (ViewCallback callback : mViewCallbacks) {
            callback.onViewCreated(mContentView);
        }
        mUnBinder = ButterKnifeUtils.bind(mFragment == null ? mActivity : mFragment, mContentView);
    }

    void initViewHandler() {
        AppDelegate appDelegate = AppDelegate.getInstance();
        Object context = mFragment == null ? mActivity : mFragment;
        // 标题栏
        Toolbar toolbar = mContentView.findViewById(R.id.android_toolbar);
        AppBarLayout appbar = mContentView.findViewById(R.id.android_bar);
        ViewHandler<AppBarLayout, Toolbar> toolbarViewHandler = appDelegate.getToolbarHandler();
        if (toolbar != null && appbar != null && toolbarViewHandler != null) {
            toolbarViewHandler.onHandle(context, appbar, toolbar);
        }
        // 刷新控件
        SmartRefreshLayout refresh = mContentView.findViewById(R.id.android_refresh);
        RelativeLayout content = mContentView.findViewById(R.id.android_content);
        ViewHandler<RelativeLayout, SmartRefreshLayout> refreshViewHandler = appDelegate.getRefreshHandler();
        if (refresh != null && content != null && refreshViewHandler != null) {
            refreshViewHandler.onHandle(context, content, refresh);
        }
    }

    void initHeaderAndFooter() {
        if (Style.isNative(mStyle)) {
            // 如果使用原生的布局，则不继续操作
            return;
        }
        ViewGroup headerParent = mContentView.findViewById(R.id.android_header);
        ViewGroup footerParent = mContentView.findViewById(R.id.android_footer);
        ViewCreator headerCreator = null;
        ViewCreator footerCreator  = null;
        boolean headerFloating = true;
        boolean footerFloating = true;
        // 移除头部和底部的控件
        headerParent.removeAllViews();
        footerParent.removeAllViews();
        // 获取ViewCreator对象
        if (mFragment == null && mActivity instanceof BaseActivity) {
            headerCreator = ((BaseActivity) mActivity).onCreateHeader(headerParent);
            footerCreator = ((BaseActivity) mActivity).onCreateFooter(footerParent);
        } else if (mFragment instanceof BaseFragment) {
            headerCreator = ((BaseFragment) mFragment).onCreateHeader(headerParent);
            footerCreator = ((BaseFragment) mFragment).onCreateFooter(footerParent);
        }
        if (headerCreator != null) {
            // 根据额外数据来判定是否浮动
            Object extras = headerCreator.getExtras();
            headerFloating = extras != null && (extras instanceof Boolean ? (Boolean) extras : true);
            View header = mInflater.inflate(headerCreator.getLayoutResId(), headerParent, true);
            header.bringToFront();
        }
        if (footerCreator != null) {
            // 根据额外数据来判定是否浮动
            Object extras = footerCreator.getExtras();
            // 额外数据为空，直接判定为不浮动
            // 额外数据为布尔类型，false为不浮动
            footerFloating = extras != null && (extras instanceof Boolean ? (Boolean) extras : true);
            View footer = mInflater.inflate(footerCreator.getLayoutResId(), footerParent, true);
            footer.bringToFront();
        }
        if (headerCreator != null || footerCreator != null) {
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            if (!headerFloating) {
                params.addRule(RelativeLayout.BELOW, R.id.android_header);
            }
            if (!footerFloating) {
                params.addRule(RelativeLayout.ABOVE, R.id.android_footer);
            }
            mRefreshLayout.setLayoutParams(params);
        }
     }

    /**
     * 初始化加载容器
     */
    void initLoadingContainer() {
        // 添加布局
        if (mLoadingLayout == null) {
            ViewGroup parent = Style.isDefault(mStyle) ? mContentLayout : mParentContainer;
            // 初始化加载容器
            mLoadingLayout = (CoordinatorLayout) mInflater.inflate(R.layout.android_layout_loading, parent, false);
            parent.addView(mLoadingLayout);
            LoadingView view = (LoadingView) mLoadingLayout.getChildAt(0);
            AppDelegate.getInstance()
                    .getLoadingAdapter()
                    .convert(mFragment == null ? mActivity : mFragment, mLoadingLayout, view);
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
     * 创建侧滑控件
     */
   void onCreateSwipeBackLayout() {
        if (Style.isSwipe(mStyle)) {
            Window window = mActivity.getWindow();
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            window.getDecorView().setBackgroundColor(Color.TRANSPARENT);
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            mSwipeBackLayout = new SwipeBackLayout(mActivity);
            mSwipeBackLayout.setLayoutParams(params);
        }
    }

    /**
     * 检测是否支持SetContentView方法
     */
    void checkContentSupport() {
        if (mFragment == null) {
            if (mActivity instanceof BasePageActivity ||
                    mActivity instanceof BaseSlidingActivity ||
                    mActivity instanceof BaseTabbedActivity) {
                if (mContentView != null) {
                    throw new UnsupportedOperationException("Can't request setContentView.");
                }
            }
        } else {
            if (mFragment instanceof BasePageFragment ||
                    mFragment instanceof BaseSlidingFragment ||
                    mFragment instanceof BaseTabbedFragment) {
                if (mContentView != null) {
                    throw new UnsupportedOperationException("Can't request setContentView.");
                }
            }
        }
    }


    /**
     * 检测是否支持SetBottomView方法
     */
    void checkFooterSupport() {
        if (mContentView == null) {
            throw new IllegalStateException("Can't request setFooterView before setContentView call.");
        }
        if (Style.isNative(mStyle)) {
            throw new UnsupportedOperationException("Can't request setFooterView while the style is NATIVE style.");
        }
    }

    /**
     * 检测是否支持SetBottomView方法
     */
    void checkHeaderSupport() {
        if (mContentView == null) {
            throw new IllegalStateException("Can't request setHeaderView before setContentView call.");
        }
        if (Style.isNative(mStyle)) {
            throw new UnsupportedOperationException("Can't request setHeaderView while the style is NATIVE style.");
        }
    }

    void addViewCallback(ViewCallback callback) {
        mViewCallbacks.add(callback);
    }
}
