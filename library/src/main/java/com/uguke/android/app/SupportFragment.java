package com.uguke.android.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;

import androidx.annotation.IdRes;
import androidx.annotation.IntDef;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.trello.rxlifecycle3.components.support.RxFragment;
import com.uguke.android.R;
import com.uguke.android.swipe.SwipeBackHelper;
import com.uguke.android.widget.CommonToolbar;
import com.uguke.android.widget.LoadingLayout;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import me.yokeyword.fragmentation.ExtraTransaction;
import me.yokeyword.fragmentation.ISupportFragment;
import me.yokeyword.fragmentation.SupportFragmentDelegate;
import me.yokeyword.fragmentation.SupportHelper;
import me.yokeyword.fragmentation.anim.FragmentAnimator;

/**
 * 基础Fragment
 * @author LeiJue
 */
public class SupportFragment extends RxFragment implements ISupportFragment, ViewProvider  {

    public static final int STANDARD = 0;
    public static final int SINGLE_TOP = 1;
    public static final int SINGLE_TASK = 2;

    public static final int RESULT_CANCELED = 0;
    public static final int RESULT_OK = -1;

    @IntDef({STANDARD, SINGLE_TOP, SINGLE_TASK})
    @Retention(RetentionPolicy.SOURCE)
    public @interface LaunchMode {}

    public View mContentView;
    /** 标题 **/
    public CommonToolbar mToolbar;
    /** 多状态控件（加载） **/
    public LoadingLayout mLoadingLayout;
    /** 刷新控件 **/
    public SmartRefreshLayout mRefreshLayout;

    final CompositeDisposable mDisposable = new CompositeDisposable();
    /** 界面布局委托 **/
    final ViewDelegate mViewDelegate = new ViewDelegate(this);
    /** Fragment管理委托 **/
    final SupportFragmentDelegate mDelegate = new SupportFragmentDelegate(this);
    public SupportActivity mActivity;
    /** 是否是首次加载 **/
    private boolean mFirstLoading = true;
    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);
        mDelegate.onAttach(activity);
        mActivity = (SupportActivity) mDelegate.getActivity();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mDelegate.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDelegate.onCreate(savedInstanceState);
        if (onSwipeBackSupport()) {
            SwipeBackHelper.onCreate(this);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mDelegate.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mDelegate.onPause();
    }

    @Nullable
    @Override
    public final View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mViewDelegate.onCreate(savedInstanceState);
        onCreating(savedInstanceState);
        // 获取设置的界面
        View view = mViewDelegate.getContentView();
        if (view == null) {
            // 若界面为空，则设置空布局提示
            view = inflater.inflate(R.layout.android_layout_fragment_null, container, false);
        }
        // 根据设置判断是否支持侧滑返回
        return onSwipeBackSupport() ? SwipeBackHelper.onAttach(this, view) : view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (onSwipeBackSupport()) {
            SwipeBackHelper.onViewCreated(this, view);
        }
    }

    @Override
    public void onDestroyView() {
        mViewDelegate.onDestroy();
        mDelegate.onDestroyView();
        if (onSwipeBackSupport()) {
            SwipeBackHelper.onDestroyView(this);
        }
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        mDisposable.dispose();
        mDelegate.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        mDelegate.onHiddenChanged(hidden);
        if (onSwipeBackSupport()) {
            SwipeBackHelper.onHiddenChanged(this, hidden);
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        mDelegate.setUserVisibleHint(isVisibleToUser);
    }


    @Override
    public SupportFragmentDelegate getSupportDelegate() {
        return mDelegate;
    }

    /**
     * Perform some extra transactions.
     * 额外的事务：自定义Tag，添加SharedElement动画，操作非回退栈Fragment
     */
    @Override
    public ExtraTransaction extraTransaction() {
        return mDelegate.extraTransaction();
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        return mDelegate.onCreateAnimation(transit, enter, nextAnim);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mDelegate.onSaveInstanceState(outState);
    }


    /**
     * 前面的事务全部执行后 执行该Action
     * @deprecated Use {@link #post(Runnable)} instead.
     */
    @Deprecated
    @Override
    public void enqueueAction(Runnable runnable) {
        mDelegate.enqueueAction(runnable);
    }

    /**
     * 前面的事务全部执行后 执行该Action
     */
    @Override
    public void post(Runnable runnable) {
        mDelegate.post(runnable);
    }

    /**
     * 入栈动画 结束时,回调
     */
    @Override
    public void onEnterAnimationEnd(Bundle savedInstanceState) {
        mDelegate.onEnterAnimationEnd(savedInstanceState);
    }

    /**
     * 同级下的 懒加载 ＋ ViewPager下的懒加载  的结合回调方法
     */
    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        mDelegate.onLazyInitView(savedInstanceState);
    }

    /**
     * 当Fragment对用户可见时回调
     * Is the combination of  [onHiddenChanged() + onResume()/onPause() + setUserVisibleHint()]
     */
    @Override
    public void onSupportVisible() {
        mDelegate.onSupportVisible();
        mViewDelegate.onViewVisible();
    }

    /**
     * 当Fragment对用户可见时回调
     * Is the combination of  [onHiddenChanged() + onResume()/onPause() + setUserVisibleHint()]
     */
    @Override
    public void onSupportInvisible() {
        mDelegate.onSupportInvisible();
        mViewDelegate.onViewInvisible();
    }

    @Override
    final public boolean isSupportVisible() {
        return mDelegate.isSupportVisible();
    }

    /**
     * 设定当前Fragment动画，优先级比在SupportActivity里高
     */
    @Override
    public FragmentAnimator onCreateFragmentAnimator() {
        return mDelegate.onCreateFragmentAnimator();
    }

    /**
     * 获取设置的全局动画 copy
     * @return FragmentAnimator
     */
    @Override
    public FragmentAnimator getFragmentAnimator() {
        return mDelegate.getFragmentAnimator();
    }

    /**
     * 设置Fragment内的全局动画
     */
    @Override
    public void setFragmentAnimator(FragmentAnimator fragmentAnimator) {
        mDelegate.setFragmentAnimator(fragmentAnimator);
    }

    /**
     * 按返回键触发,前提是SupportActivity的onBackPressed()方法能被调用
     * @return false则继续向上传递, true则消费掉该事件
     */
    @Override
    public boolean onBackPressedSupport() {
        return mDelegate.onBackPressedSupport();
    }


    @Override
    public void setFragmentResult(int resultCode, Bundle bundle) {
        mDelegate.setFragmentResult(resultCode, bundle);
    }

    @Override
    public void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        mDelegate.onFragmentResult(requestCode, resultCode, data);
    }

    /**
     * 在start(TargetFragment,LaunchMode)时,启动模式为SingleTask/SingleTop, 回调TargetFragment的该方法
     */
    @Override
    public void onNewBundle(Bundle args) {
        mDelegate.onNewBundle(args);
    }

    /**
     * 添加NewBundle,用于启动模式为SingleTask/SingleTop时
     */
    @Override
    public void putNewBundle(Bundle newBundle) {
        mDelegate.putNewBundle(newBundle);
    }


    public void onCreating(@Nullable Bundle savedInstanceState) {}

    //============ 设置界面方法 ============//

    @Override
    public void onViewCreated(View view) {
        mContentView = view;
        // 初始化控件
        mLoadingLayout = mViewDelegate.getLoadingLayout();
        mRefreshLayout = mViewDelegate.getRefreshLayout();
        mToolbar = mViewDelegate.getToolbar();
    }

    @Override
    public void showContent() {
        mViewDelegate.showContent();
    }

    @Override
    public void showEmpty(String... texts) {
        mViewDelegate.showEmpty(texts);
    }

    @Override
    public void showError(String... texts) {
        mViewDelegate.showError(texts);
    }

    @Override
    public void showLoading(String... texts) {
        mViewDelegate.showLoading(texts);
    }

    public final void setContentView(@LayoutRes int id) {
        mViewDelegate.setContentView(id);
    }

    public final void setContentView(View view) {
        mViewDelegate.setContentView(view);
    }

    public final void setNativeContentView(@LayoutRes int id) {
        mViewDelegate.setNativeContentView(id);
    }

    public final void setNativeContentView(View view) {
        mViewDelegate.setNativeContentView(view);
    }

    public ViewCreator onCreateHeader(@NonNull ViewGroup container) {
        return null;
    }

    public ViewCreator onCreateFooter(@NonNull ViewGroup container) {
        return null;
    }

    public final <T extends View> T findViewById(@IdRes int id) {
        if (mViewDelegate.getContentView() != null) {
            return mViewDelegate.getContentView().findViewById(id);
        }
        return null;
    }

    public void showTips(String tips) {
        mViewDelegate.showTips(tips);
    }

    public void showLoadingTips(String tips) {
        mViewDelegate.showLoadingTips(tips);
    }

    public void hideLoadingTips() {
        mViewDelegate.hideLoadingTips();
    }

    public void addDisposable(Disposable disposable) {
        mDisposable.add(disposable);
    }

    /**
     * 是否支持侧滑返回 true 支持 false 不支持
     * 不支持的情况下{@link SwipeBackHelper}所有方法无效
     */
    public boolean onSwipeBackSupport() {
        return AndroidDelegate.getInstance().isSwipeBackSupport();
    }

    /**
     * 隐藏软键盘
     */
    public void hideSoftInput() {
        mDelegate.hideSoftInput();
    }

    /**
     * 显示软键盘,调用该方法后,会在onPause时自动隐藏软键盘
     */
    public void showSoftInput(final View view) {
        mDelegate.showSoftInput(view);
    }

    //============ Fragment操作方法 ============//

    /**
     * 加载根Fragment, Fragment内的第一个子Fragment
     * @param containerId 容器id
     * @param toFragment  目标Fragment
     */
    public void loadRootFragment(int containerId, SupportFragment toFragment) {
        mDelegate.loadRootFragment(containerId, toFragment);
    }

    public void loadRootFragment(int containerId, SupportFragment toFragment, boolean addToBackStack, boolean allowAnim) {
        mDelegate.loadRootFragment(containerId, toFragment, addToBackStack, allowAnim);
    }

    public void start(SupportFragment toFragment) {
        mDelegate.start(toFragment);
    }

    public void start(final SupportFragment toFragment, @LaunchMode int launchMode) {
        mDelegate.start(toFragment, launchMode);
    }

    /**
     * 类型{@link Activity#startActivityForResult(Intent, int)}
     * 成功回调{@link SupportFragment#onFragmentResult(int, int, Bundle)}
     * @param toFragment 目标Fragment
     * @param requestCode 请求码
     */
    public void startForResult(SupportFragment toFragment, int requestCode) {
        mDelegate.startForResult(toFragment, requestCode);
    }

    /**
     * 启动一个Fragment同时销毁自己
     */
    public void startWithPop(SupportFragment toFragment) {
        mDelegate.startWithPop(toFragment);
    }

    /**
     * @see #popTo(Class, boolean)
     * +
     * @see #start(SupportFragment)
     */
    public void startWithPopTo(SupportFragment toFragment, Class<? extends SupportFragment> targetFragmentClass, boolean includeTargetFragment) {
        mDelegate.startWithPopTo(toFragment, targetFragmentClass, includeTargetFragment);
    }

    public void replaceFragment(SupportFragment toFragment, boolean addToBackStack) {
        mDelegate.replaceFragment(toFragment, addToBackStack);
    }

    public void pop() {
        mDelegate.pop();
    }

    /**
     * 出栈子Fragment.
     */
    public void popChild() {
        mDelegate.popChild();
    }

    /**
     * 出栈到目标fragment
     * @param targetFragmentClass   目标fragment
     * @param includeTargetFragment 是否包含该fragment
     */
    public void popTo(Class<? extends SupportFragment> targetFragmentClass, boolean includeTargetFragment) {
        mDelegate.popTo(targetFragmentClass, includeTargetFragment);
    }

    /**
     * 如果你想在出栈后, 立刻进行FragmentTransaction操作，请使用该方法
     */
    public void popTo(Class<? extends SupportFragment> targetFragmentClass, boolean includeTargetFragment, Runnable afterPopTransactionRunnable) {
        mDelegate.popTo(targetFragmentClass, includeTargetFragment, afterPopTransactionRunnable);
    }

    public void popTo(Class<? extends SupportFragment> targetFragmentClass, boolean includeTargetFragment, Runnable afterPopTransactionRunnable, int popAnim) {
        mDelegate.popTo(targetFragmentClass, includeTargetFragment, afterPopTransactionRunnable, popAnim);
    }

    public void popToChild(Class<? extends SupportFragment> targetFragmentClass, boolean includeTargetFragment) {
        mDelegate.popToChild(targetFragmentClass, includeTargetFragment);
    }

    public void popToChild(Class<? extends SupportFragment> targetFragmentClass, boolean includeTargetFragment, Runnable afterPopTransactionRunnable) {
        mDelegate.popToChild(targetFragmentClass, includeTargetFragment, afterPopTransactionRunnable);
    }

    public void popToChild(Class<? extends SupportFragment> targetFragmentClass, boolean includeTargetFragment, Runnable afterPopTransactionRunnable, int popAnim) {
        mDelegate.popToChild(targetFragmentClass, includeTargetFragment, afterPopTransactionRunnable, popAnim);
    }

    /**
     * 得到位于栈顶Fragment
     */
    public SupportFragment getTopFragment() {
        if (getFragmentManager() != null) {
            return (SupportFragment) SupportHelper.getTopFragment(getFragmentManager());
        }
        return null;
    }

    public SupportFragment getTopChildFragment() {
        return (SupportFragment) SupportHelper.getTopFragment(getChildFragmentManager());
    }

    /**
     * @return 位于当前Fragment的前一个Fragment
     */
    public SupportFragment getPreFragment() {
        return (SupportFragment) SupportHelper.getPreFragment(this);
    }

    /**
     * 获取栈内的fragment对象
     */
    public <T extends SupportFragment> T findFragment(Class<T> fragmentClass) {
        if (getFragmentManager() != null) {
            return SupportHelper.findFragment(getFragmentManager(), fragmentClass);
        }
        return null;
    }

    /**
     * 获取栈内的fragment对象
     */
    public <T extends SupportFragment> T findChildFragment(Class<T> fragmentClass) {
        return SupportHelper.findFragment(getChildFragmentManager(), fragmentClass);
    }

    /**
     * 获取栈内的fragment对象
     */
    public <T extends SupportFragment> T findFragmentByTag(String tag) {
        if (getFragmentManager() != null) {
            return SupportHelper.findFragment(getFragmentManager(), tag);
        }
        return null;
    }

    /**
     * 获取栈内的fragment对象
     */
    public <T extends ISupportFragment> T findChildFragmentByTag(String tag) {
        return SupportHelper.findFragment(getChildFragmentManager(), tag);
    }

    public void hideToolbar() {
        View view = findViewById(R.id.__android_bar);
        if (view != null) {
            view.setVisibility(View.GONE);
        } else if (mToolbar != null) {
            mToolbar.setVisibility(View.GONE);
        }
    }
}
