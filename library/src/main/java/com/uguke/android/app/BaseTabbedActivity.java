package com.uguke.android.app;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.LayoutRes;

import com.google.android.material.appbar.AppBarLayout;
import com.uguke.android.widget.OnTabSelectedListener;
import com.uguke.android.util.ResUtils;
import com.uguke.android.widget.CommonTabLayout;
import com.uguke.android.widget.Toolbar;
import com.uguke.android.R;

import java.util.Arrays;

import me.yokeyword.fragmentation.ISupportFragment;

/**
 * 基础标签活动
 * @author LeiJue
 */
public abstract class BaseTabbedActivity extends BaseActivity {

    protected CommonTabLayout mTabLayout;
    protected ISupportFragment [] mFragments;
    private int mCurrentTab = 0;

    @Override
    public void onCreating(Bundle savedInstanceState) {
        setContentView(R.layout.android_layout_tabbed, onSwipeBackSupport() ? Style.NATIVE_SWIPE : Style.NATIVE);
        // 恢复选项位置
        if (savedInstanceState != null) {
            mCurrentTab = savedInstanceState.getInt("currentTab", 0);
        }
        mTabLayout = findViewById(R.id.android_tab);
        // 监听事件
        mTabLayout.addOnTabSelectedListener(new OnTabSelectedListener() {
                @Override
                public void onTabSelected(int position) {
                    BaseTabbedActivity.super.showHideFragment(mFragments[position]);
                    mCurrentTab = position;
                }

                @Override
                public void onTabReselected(int position) {
                    mCurrentTab = position;
                }
            });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("currentTab", mCurrentTab);
    }

    public void loadMultipleRootFragment(FragmentTab... tabs) {
        loadMultipleRootFragment(mCurrentTab, tabs);
    }

    public void loadMultipleRootFragment(int position, FragmentTab... tabs) {
        if (tabs == null || tabs.length == 0) {
            return;
        }
        mFragments = new BaseFragment[tabs.length];
        if (findFragmentByTag(tabs[0].getTag()) == null) {
            for (int i = 0, len = tabs.length; i < len; i++) {
                mFragments[i] = tabs[i].newFragment();
            }
            loadMultipleRootFragment(R.id.android_fragment, 0, mFragments);
        } else {
            for (int i = 0, len = tabs.length; i < len; i++) {
                mFragments[i] = findFragmentByTag(tabs[0].getTag());
            }
        }
        mTabLayout.setTabData(Arrays.asList(tabs));
        mTabLayout.setCurrentTab(position);
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
                Toolbar toolbar = new Toolbar(this);
                toolbar.setLayoutParams(new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ResUtils.getPixel(this, R.dimen.toolbar)));
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
