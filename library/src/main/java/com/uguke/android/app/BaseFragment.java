package com.uguke.android.app;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.FloatRange;
import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.google.android.material.appbar.AppBarLayout;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.uguke.android.R;
import com.uguke.android.adapter.NetworkAdapter;
import com.uguke.android.app.delegate.ViewCallback;
import com.uguke.android.app.delegate.ViewCreator;
import com.uguke.android.app.delegate.ViewFragmentDelegate;
import com.uguke.android.helper.snack.OnDismissListener;
import com.uguke.android.widget.SwipeBackLayout;
import com.uguke.android.widget.Toolbar;

import java.util.List;

import me.yokeyword.fragmentation.ISupportFragment;
import me.yokeyword.fragmentation.SupportFragment;
import me.yokeyword.fragmentation.SupportHelper;

/**
 *
 * @author LeiJue
 */
public class BaseFragment extends SupportFragment implements ViewCallback, BaseView, SwipBackProvider {

    final ViewFragmentDelegate mViewDelegate = new ViewFragmentDelegate(this, this);

    private View mContentView;
    private MvpDelegate mMvpDelegate;

    /** 标题 **/
    public Toolbar mToolbar;
    /** 刷新控件 **/
    public SmartRefreshLayout mRefreshLayout;
    /** 活动 **/
    public FragmentActivity mActivity;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = _mActivity;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewDelegate.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public final View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        onCreating(savedInstanceState);
        return mViewDelegate.attachToSwipeBack(mContentView);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewDelegate.onViewCreated(view, savedInstanceState);
    }


    @Override
    public void onViewCreated(@NonNull View view) {
        mContentView = view;
        mToolbar = findViewById(R.id.android_toolbar);
        mRefreshLayout = findViewById(R.id.android_refresh);
        if (mToolbar != null) {
            mToolbar.setFinishListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pop();
                }
            });
        }
    }

    public ViewCreator onCreateHeader(ViewGroup container) {
        return null;
    }

    public ViewCreator onCreateFooter(ViewGroup container) {
        return null;
    }


    @Override
    public void onDestroyView() {
        hideSoftInput();
        if (mMvpDelegate != null) {
            mMvpDelegate.onDestroy();
            mMvpDelegate = null;
        }
        NetworkAdapter adapter = AppDelegate.getInstance().getNetworkAdapter();
        if (adapter != null) {
            adapter.cancel(this);
        }
        mViewDelegate.onDestroyView();
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        mViewDelegate.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        mViewDelegate.onHiddenChanged(hidden);
    }

    /**
     * 获取栈内的fragment对象
     */
    public <T extends ISupportFragment> T findFragmentByTag(String tag) {
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

    public final <T extends View> T findViewById(@IdRes int id) {
        if (mViewDelegate.getContentView() != null) {
            return mViewDelegate.getContentView().findViewById(id);
        }
        return null;
    }

    public final void setContentView(@LayoutRes int id) {
        mViewDelegate.setContentView(id, Style.DEFAULT);
    }

    public final void setContentView(View view) {
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

    @Override
    public boolean onSwipeBackPriority() {
        return false;
    }

    @Override
    public boolean onSwipeBackSupport() {
        return false;
    }

    public void setParallaxOffset(@FloatRange(from = 0.0f, to = 1.0f) float offset) {
        mViewDelegate.setParallaxOffset(offset);
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

    public boolean isLast() {
        List<Fragment> fragments = mActivity.getSupportFragmentManager().getFragments();
        return fragments.size() <= 1;
    }

    public boolean isLastFragment() {
        Fragment fragment = getParentFragment();
        if (fragment == null) {
            List<Fragment> fragments = mActivity.getSupportFragmentManager().getFragments();
            return fragments.size() <= 1;
        } else {
            List<Fragment> fragments = fragment.getChildFragmentManager().getFragments();
            return fragments.size() <= 1;
        }
    }


//    @Override
//    public void pop() {
//        Fragment fragment = getParentFragment();
//        if (fragment == null) {
//            List<Fragment> fragments = mActivity.getSupportFragmentManager().getFragments();
//            if (fragments.size() <= 1) {
//                mActivity.finish();
//                return;
//            }
//        } else {
//            List<Fragment> fragments = fragment.getChildFragmentManager().getFragments();
//            if (fragments.size() <= 1) {
//                ((SupportFragment) fragment).pop();
//                return;
//            }
//        }
//        super.pop();
//    }
}
