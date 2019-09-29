package com.uguke.android.app;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.uguke.android.R;
import com.uguke.android.widget.SlidingTabLayout;

/**
 * 基础标签活动
 * @author LeiJue
 */
public abstract class BaseSlidingFragment extends SupportFragment {

    protected SlidingTabLayout mTabLayout;
    protected SupportFragment[] mFragments;

    private int mCurrentTab;
    private ViewPager mViewPager;

    @Override
    public void onCreating(Bundle savedInstanceState) {
        setNativeContentView(R.layout.android_layout_sliding);
        if (savedInstanceState != null) {
            mCurrentTab = savedInstanceState.getInt("currentTab", 0);
        }
        mViewPager = findViewById(R.id.android_fragment);
        mTabLayout = findViewById(R.id.android_tab);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("currentTab", mCurrentTab);
    }

    public void loadMultipleRootFragment(FragmentTab... tabs) {
        loadMultipleRootFragment(mCurrentTab, tabs);
    }

    public void loadMultipleRootFragment(int position, final FragmentTab... tabs) {
        mFragments = new SupportFragment[tabs.length];
        // 初始化数组
        for (int i = 0, len = tabs.length; i < len; i++) {
            mFragments[i] = tabs[i].newFragment();
        }
        // 设置数据适配
        mViewPager.setAdapter(new FragmentPagerAdapter(getChildFragmentManager(), 0) {
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
        // 设置当前选项
        mTabLayout.setViewPager(mViewPager);
        mTabLayout.setCurrentTab(position);
    }

    public SupportFragment [] getFragments() {
        return mFragments;
    }
}
