package com.uguke.android.app;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.uguke.android.R;

/**
 * 基础标签活动
 * @author LeiJue
 */
public abstract class BaseSlidingActivity extends BaseActivity {

    protected TabLayout mTabLayout;
    protected BaseFragment[] mFragments;
    private int mCurrentTab;
    private boolean mSupport;
    private ViewPager mPager;

    @Override
    public void onCreating(Bundle savedInstanceState) {
        setContentView(R.layout.android_layout_sliding, mSupport ? Style.NATIVE_SWIPE : Style.NATIVE);
        if (savedInstanceState != null) {
            mCurrentTab = savedInstanceState.getInt("currentTab", 0);
        }
        mPager = findViewById(R.id.android_fragment);
        mTabLayout = findViewById(R.id.android_tab);
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                showHideFragment(mFragments[tab.getPosition()]);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("currentTab", mTabLayout.getSelectedTabPosition());
    }

    /**
     * 是否支持滑动返回，在super.onCreating()之前调用
     * @param support 是否支持
     */
    public void setSwipeBackSupport(boolean support) {
        mSupport = support;
    }

    public void loadMultipleRootFragment(FragmentTabs... tabs) {
        loadMultipleRootFragment(-1, tabs);
    }

    public void loadMultipleRootFragment(int position, final FragmentTabs... tabs) {
        mFragments = new BaseFragment[tabs.length];
        // 初始化数组
        for (int i = 0, len = tabs.length; i < len; i++) {
            mFragments[i] = tabs[i].newFragment();
        }
        // 设置数据适配
        mPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager(), 0) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                return mFragments[position];
            }

            @Override
            public int getCount() {
                return mFragments.length;
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                return tabs[position].getTitle();
            }
        });
        mTabLayout.setupWithViewPager(mPager);
        // 设置当前选项
        mTabLayout.selectTab(mTabLayout.getTabAt(position == -1 ? mCurrentTab : position));
        mPager.setOffscreenPageLimit(3);
    }

    public BaseFragment [] getFragments() {
        return mFragments;
    }
}
