package com.uguke.android.app;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.DrawableRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.uguke.android.helper.SwipeBackHelper;
import com.uguke.android.widget.CommonToolbar;

import me.yokeyword.fragmentation.ExtraTransaction;
import me.yokeyword.fragmentation.ISupportActivity;
import me.yokeyword.fragmentation.ISupportFragment;
import me.yokeyword.fragmentation.SupportActivityDelegate;
import me.yokeyword.fragmentation.SupportHelper;
import me.yokeyword.fragmentation.anim.FragmentAnimator;

/**
 * 基础Activity
 * @author LeiJue
 */
public class SupportActivity extends AppCompatActivity implements ISupportActivity {

    /** 标题 **/
    public CommonToolbar mToolbar;
    /** 刷新控件 **/
    public SmartRefreshLayout mRefreshLayout;

    final LayoutDelegate mLayoutDelegate = new LayoutDelegate(this);
    final SupportActivityDelegate mDelegate = new SupportActivityDelegate(this);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDelegate.onCreate(savedInstanceState);
        mLayoutDelegate.onCreate(savedInstanceState);
        mLayoutDelegate.addLifeCallback(new LayoutLifeCallback() {
            @Override
            public void onCreate() {}

            @Override
            public void onViewCreated(View view) {
                // 设置布局
                getDelegate().setContentView(view);
                // 初始化控件
                mToolbar = mLayoutDelegate.getToolbar();
                mRefreshLayout = mLayoutDelegate.getRefreshLayout();
            }

            @Override
            public void onDestroy() {}
        });
        onCreating(savedInstanceState);
        //SwipeBackHelper.create(this);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDelegate.onPostCreate(savedInstanceState);
        //SwipeBackHelper.attach(this);

    }

    @Override
    protected void onDestroy() {
        mLayoutDelegate.onDestroy();
        mDelegate.onDestroy();
        //SwipeBackHelper.destory(this);
        super.onDestroy();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return mDelegate.dispatchTouchEvent(ev) || super.dispatchTouchEvent(ev);
    }

    /**
     * 不建议复写该方法,请使用 {@link #onBackPressedSupport} 代替
     */
    @Override
    final public void onBackPressed() {
        mDelegate.onBackPressed();
    }

    @Override
    public SupportActivityDelegate getSupportDelegate() {
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

    /**
     * 该方法回调时机为,Activity回退栈内Fragment的数量 小于等于1 时,默认finish Activity
     * 请尽量复写该方法,避免复写onBackPress(),以保证SupportFragment内的onBackPressedSupport()回退事件正常执行
     */
    @Override
    public void onBackPressedSupport() {
        mDelegate.onBackPressedSupport();
    }

    /**
     * 获取设置的全局动画 copy
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
     * 构建Fragment转场动画
     * 如果是在Activity内实现,则构建的是Activity内所有Fragment的转场动画,
     * 如果是在Fragment内实现,则构建的是该Fragment的转场动画,此时优先级 > Activity的onCreateFragmentAnimator()
     * @return FragmentAnimator对象
     */
    @Override
    public FragmentAnimator onCreateFragmentAnimator() {
        return mDelegate.onCreateFragmentAnimator();
    }

    @Override
    public void post(Runnable runnable) {
        mDelegate.post(runnable);
    }

    public void onCreating(@Nullable Bundle savedInstanceState) {}

    @Override
    public final void setContentView(@LayoutRes int id) {
        mLayoutDelegate.setContentView(id);
    }

    @Override
    public final void setContentView(View view) {
        mLayoutDelegate.setContentView(view);
    }

    @Override
    public final void setContentView(View view, ViewGroup.LayoutParams params) {
        view.setLayoutParams(params);
        mLayoutDelegate.setContentView(view);
    }

    public final void setSimpleContentView(@LayoutRes int id) {
        mLayoutDelegate.setSimpleContentView(id);
    }

    public final void setSimpleContentView(View view) {
        mLayoutDelegate.setSimpleContentView(view);
    }

    public final void setNativeContentView(@LayoutRes int id) {
        mLayoutDelegate.setNativeContentView(id);
    }

    public final void setNativeContentView(View view) {
        mLayoutDelegate.setNativeContentView(view);
    }

    public LayoutCreator onCreateHeader(ViewGroup container) {
        return null;
    }

    public LayoutCreator onCreateFooter(ViewGroup container) {
        return null;
    }

    public void showTips(String tips) {
        mLayoutDelegate.showTips(tips);
    }

    public void showLoading(String ...texts) {
        mLayoutDelegate.showLoading(texts);
    }

    public void hideLoading() {
        mLayoutDelegate.hideLoading();
    }

    /****************************************以下为可选方法(Optional methods)******************************************************/

    /**
     * 加载根Fragment, 即Activity内的第一个Fragment 或 Fragment内的第一个子Fragment
     *
     * @param containerId 容器id
     * @param toFragment  目标Fragment
     */
    public void loadRootFragment(int containerId, @NonNull ISupportFragment toFragment) {
        mDelegate.loadRootFragment(containerId, toFragment);
    }

    public void loadRootFragment(int containerId, ISupportFragment toFragment, boolean addToBackStack, boolean allowAnimation) {
        mDelegate.loadRootFragment(containerId, toFragment, addToBackStack, allowAnimation);
    }

    /**
     * 加载多个同级根Fragment,类似Wechat, QQ主页的场景
     */
    public void loadMultipleRootFragment(int containerId, int showPosition, ISupportFragment... toFragments) {
        mDelegate.loadMultipleRootFragment(containerId, showPosition, toFragments);
    }

    /**
     * show一个Fragment,hide其他同栈所有Fragment
     * 使用该方法时，要确保同级栈内无多余的Fragment,(只有通过loadMultipleRootFragment()载入的Fragment)
     * <p>
     * 建议使用更明确的{@link #showHideFragment(ISupportFragment, ISupportFragment)}
     *
     * @param showFragment 需要show的Fragment
     */
    public void showHideFragment(com.uguke.android.app.SupportFragment showFragment) {
        mDelegate.showHideFragment(showFragment);
    }

    /**
     * show一个Fragment,hide一个Fragment ; 主要用于类似微信主页那种 切换tab的情况
     */
    public void showHideFragment(ISupportFragment showFragment, ISupportFragment hideFragment) {
        mDelegate.showHideFragment(showFragment, hideFragment);
    }

    /**
     * It is recommended to use {@link me.yokeyword.fragmentation.SupportFragment#start(ISupportFragment)}.
     */
    public void start(ISupportFragment toFragment) {
        mDelegate.start(toFragment);
    }

    /**
     * It is recommended to use {@link me.yokeyword.fragmentation.SupportFragment#start(ISupportFragment, int)}.
     *
     * @param launchMode Similar to Activity's LaunchMode.
     */
    public void start(ISupportFragment toFragment, @ISupportFragment.LaunchMode int launchMode) {
        mDelegate.start(toFragment, launchMode);
    }

    /**
     * It is recommended to use {@link me.yokeyword.fragmentation.SupportFragment#startForResult(ISupportFragment, int)}.
     * Launch an fragment for which you would like a result when it poped.
     */
    public void startForResult(ISupportFragment toFragment, int requestCode) {
        mDelegate.startForResult(toFragment, requestCode);
    }

    /**
     * It is recommended to use {@link me.yokeyword.fragmentation.SupportFragment#startWithPop(ISupportFragment)}.
     * Start the target Fragment and pop itself
     */
    public void startWithPop(ISupportFragment toFragment) {
        mDelegate.startWithPop(toFragment);
    }

    /**
     * It is recommended to use {@link me.yokeyword.fragmentation.SupportFragment#startWithPopTo(ISupportFragment, Class, boolean)}.
     *
     * @see #popTo(Class, boolean)
     * +
     * @see #start(ISupportFragment)
     */
    public void startWithPopTo(ISupportFragment toFragment, Class<?> targetFragmentClass, boolean includeTargetFragment) {
        mDelegate.startWithPopTo(toFragment, targetFragmentClass, includeTargetFragment);
    }

    /**
     * It is recommended to use {@link SupportFragment#replaceFragment(ISupportFragment, boolean)}.
     */
    public void replaceFragment(ISupportFragment toFragment, boolean addToBackStack) {
        mDelegate.replaceFragment(toFragment, addToBackStack);
    }

    /**
     * Pop the fragment.
     */
    public void pop() {
        mDelegate.pop();
    }

    /**
     * Pop the last fragment transition from the manager's fragment
     * back stack.
     * <p>
     * 出栈到目标fragment
     *
     * @param targetFragmentClass   目标fragment
     * @param includeTargetFragment 是否包含该fragment
     */
    public void popTo(Class<?> targetFragmentClass, boolean includeTargetFragment) {
        mDelegate.popTo(targetFragmentClass, includeTargetFragment);
    }

    /**
     * If you want to begin another FragmentTransaction immediately after popTo(), use this method.
     * 如果你想在出栈后, 立刻进行FragmentTransaction操作，请使用该方法
     */
    public void popTo(Class<?> targetFragmentClass, boolean includeTargetFragment, Runnable afterPopTransactionRunnable) {
        mDelegate.popTo(targetFragmentClass, includeTargetFragment, afterPopTransactionRunnable);
    }

    public void popTo(Class<?> targetFragmentClass, boolean includeTargetFragment, Runnable afterPopTransactionRunnable, int popAnim) {
        mDelegate.popTo(targetFragmentClass, includeTargetFragment, afterPopTransactionRunnable, popAnim);
    }

    /**
     * 当Fragment根布局 没有 设定background属性时,
     * Fragmentation默认使用Theme的android:windowbackground作为Fragment的背景,
     * 可以通过该方法改变其内所有Fragment的默认背景。
     */
    public void setDefaultFragmentBackground(@DrawableRes int backgroundRes) {
        mDelegate.setDefaultFragmentBackground(backgroundRes);
    }

    /**
     * 得到位于栈顶Fragment
     */
    public ISupportFragment getTopFragment() {
        return SupportHelper.getTopFragment(getSupportFragmentManager());
    }

    /**
     * 获取栈内的fragment对象
     */
    public <T extends ISupportFragment> T findFragment(Class<T> fragmentClass) {
        return SupportHelper.findFragment(getSupportFragmentManager(), fragmentClass);
    }


    /**
     * 获取栈内的fragment对象
     */
    public <T extends ISupportFragment> T findFragmentByTag(String tag) {
        if (getFragmentManager() != null) {
            return SupportHelper.findFragment(getSupportFragmentManager(), tag);
        }
        return null;
    }

    /**
     * 获取栈内的fragment对象
     */
    public <T extends ISupportFragment> T findChildFragmentByTag(String tag) {
        return SupportHelper.findFragment(getSupportFragmentManager(), tag);
    }
}
