package com.uguke.android.app;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;

import com.google.android.material.appbar.AppBarLayout;
import com.uguke.android.listener.CustomTabEntity;
import com.uguke.android.listener.OnTabSelectedListener;
import com.uguke.android.util.ResUtils;
import com.uguke.android.widget.CommonTabLayout;
import com.uguke.android.widget.Toolbar;
import com.uguke.android.R;

import java.util.ArrayList;
import java.util.Arrays;

import me.yokeyword.fragmentation.ISupportFragment;

/**
 * 基础标签活动
 * @author LeiJue
 */
public abstract class BaseTabbedFragment extends BaseFragment {

    protected CommonTabLayout mTabLayout;
    protected ISupportFragment [] mFragments;
    private int mCurrentTab = 0;
    private boolean mSupport;

    @Override
    public void onCreating(Bundle savedInstanceState) {
        setContentView(R.layout.android_layout_tabbed, mSupport ? Style.NATIVE_SWIPE : Style.NATIVE);
        // 恢复选项位置
        if (savedInstanceState != null) {
            mCurrentTab = savedInstanceState.getInt("currentTab", 0);
        }
        // Tab选项
        mTabLayout = findViewById(R.id.android_tab);
        mTabLayout.addOnTabSelectedListener(new OnTabSelectedListener() {

            @Override
            public void onTabSelected(int position) {
                BaseTabbedFragment.super.showHideFragment(mFragments[position]);
                mCurrentTab = position;
            }

            @Override
            public void onTabReselected(int position) {
                mCurrentTab = position;
            }

        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("currentTab", mCurrentTab);
    }

    @Override
    public void showHideFragment(ISupportFragment showFragment) {
        showHideFragment(showFragment, null);
    }

    @Override
    public void showHideFragment(ISupportFragment showFragment, ISupportFragment hideFragment) {
        int index = 0;
        for (int i = 0; i < mFragments.length; i++) {
            ISupportFragment fragment = mFragments[i];
            if (showFragment == fragment) {
                index = i;
                break;
            }
        }
        super.showHideFragment(showFragment, hideFragment);
        mTabLayout.setCurrentTab(index);
    }

    public void showFragment(int position) {
        super.showHideFragment(mFragments[position]);
        mTabLayout.setCurrentTab(position);
    }

    public void showFragment(ISupportFragment fragment) {
        int index = 0;
        for (int i = 0; i < mFragments.length; i++) {
            if (mFragments[i] == fragment) {
                index = i;
                break;
            }
        }
        super.showHideFragment(mFragments[index]);
        mTabLayout.setCurrentTab(index);
    }

    public void showFragment(Class<? extends ISupportFragment> showClass) {
        int index = 0;
        for (int i = 0; i < mFragments.length; i++) {
            ISupportFragment fragment = mFragments[i];
            if (showClass == fragment.getClass()) {
                index = i;
                break;
            }
        }
        super.showHideFragment(mFragments[index]);
        mTabLayout.setCurrentTab(index);
    }
    /**
     * 是否支持滑动返回，在super.onCreating()之前调用
     * @param support 是否支持
     */
    public void setSwipeBackSupport(boolean support) {
        mSupport = support;
    }

    public void loadMultipleRootFragment(FragmentTab... tabs) {
        if (tabs == null || tabs.length == 0) {
            return;
        }
        mFragments = new BaseFragment[tabs.length];
        ArrayList<CustomTabEntity> tabEntities = new ArrayList<CustomTabEntity>(Arrays.asList(tabs));
        mTabLayout.setTabData(tabEntities);
        if (findFragment(tabs[0].getFragmentClass()) == null) {
            for (int i = 0, len = tabs.length; i < len; i++) {
                mFragments[i] = tabs[i].newFragment();
            }
            loadMultipleRootFragment(R.id.android_fragment, 0, mFragments);
        } else {
            for (int i = 0, len = tabs.length; i < len; i++) {
                mFragments[i] = findFragment(tabs[i].getFragmentClass());
            }
        }
        mTabLayout.setCurrentTab(mCurrentTab);
        // CommonTabLayout有一个Bug，3个的时候的有点击效果
//        for (int i = 0, len = tabs.length; i < len; i++) {
//            ViewCompat.setBackground((View) mTabLayout.getMsgView(i).getParent(), null);
//        }
    }

    /**
     * 应用顶部TabLayout
     */
    public void applyTopTab() {
        ViewGroup parent = (ViewGroup) mTabLayout.getParent();
        parent.removeView(mTabLayout);
        parent.addView(mTabLayout, 0);
        mTabLayout.setIndicatorHeight(2);
        mTabLayout.setUnderlineGravity(Gravity.BOTTOM);
    }

    public void applyToolbar() {
        applyToolbar(null);
    }

    public void applyToolbar(@LayoutRes int layoutRes) {
        AppBarLayout appBarLayout = findViewById(R.id.android_bar);
        if (mToolbar == null && appBarLayout != null) {
            applyToolbar(getLayoutInflater().inflate(layoutRes, appBarLayout, false));
        }
    }

    public void applyToolbar(View view) {
        AppBarLayout appBarLayout = findViewById(R.id.android_bar);
        if (mToolbar == null && appBarLayout != null) {
            if (view == null) {
                Toolbar toolbar = new Toolbar(mActivity);
                toolbar.setLayoutParams(new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ResUtils.getPixel(mActivity, R.dimen.toolbar)));
                toolbar.setDividerVisible(false);
                toolbar.setMaterialStyle(false);
                appBarLayout.addView(toolbar);
                mToolbar = toolbar;
            } else {
                mToolbar = null;
                appBarLayout.removeAllViews();
                appBarLayout.addView(view);
            }
        }
    }

}
