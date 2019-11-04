package com.uguke.android.app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

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
public class SlidingActivity extends SupportActivity {

    protected ViewPager mViewPager;
    protected SlidingTabLayout mTabLayout;
    protected SupportFragment[] mFragments;
    private int mCurrentTab;

    @Override
    public void onCreating(Bundle savedInstanceState) {
        super.onCreating(savedInstanceState);
        setNativeContentView(R.layout.android_layout_sliding);
        if (savedInstanceState != null) {
            mCurrentTab = savedInstanceState.getInt("currentTab", 0);
        }
        mViewPager = findViewById(R.id.android_fragment);
        mTabLayout = findViewById(R.id.android_tab);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("currentTab", mCurrentTab);
    }

    @Override
    protected void onHandleCreators(View view) {
        ViewGroup headerParent = view.findViewById(R.id.android_header);
        ViewGroup footerParent = view.findViewById(R.id.android_footer);
        // 创建Header和Footer内容
        ViewCreator headerCreator = onCreateHeader(headerParent);
        ViewCreator footerCreator = onCreateFooter(footerParent);
        RelativeLayout.LayoutParams params;
        // 初始化头部
        if (headerCreator != null) {
            if (headerParent.getChildCount() == 0) {
                View header = LayoutInflater.from(this).inflate(headerCreator.getLayoutResId(), headerParent, true);
                header.bringToFront();
            }
            boolean floating = headerCreator.isFloating();
            params = new RelativeLayout.LayoutParams(-1, -1);
            if (!floating) {
                params.addRule(RelativeLayout.BELOW, R.id.android_header);
            }
            findViewById(R.id.android_fragment).setLayoutParams(params);
        }
        // 初始化底部
        if (footerCreator != null) {
            if (footerParent.getChildCount() == 0) {
                View footer = LayoutInflater.from(this).inflate(footerCreator.getLayoutResId(), footerParent, true);
                footer.bringToFront();
            }
            boolean floating = footerCreator.isFloating();
            params = new RelativeLayout.LayoutParams(-1, -1);
            if (!floating) {
                params.addRule(RelativeLayout.ABOVE, R.id.android_header);
            }
            findViewById(R.id.android_fragment).setLayoutParams(params);
        }
    }

    /**
     * 加载多个Fragment页面
     * @param tabs Fragment项
     */
    public void loadMultipleFragment(FragmentTab... tabs) {
        loadMultipleFragment(tabs.length, tabs);
    }

    /**
     * 加载多个Fragment页面
     * @param pageLimit 可以缓存的页面数
     * @param tabs Fragment项
     */
    public void loadMultipleFragment(int pageLimit, final FragmentTab... tabs) {
        mFragments = new SupportFragment[tabs.length];
        // 初始化数组
        for (int i = 0, len = tabs.length; i < len; i++) {
            mFragments[i] = tabs[i].newFragment();
        }
        // 设置数据适配
        mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager(), 0) {
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
        mViewPager.setOffscreenPageLimit(pageLimit);
        // 设置当前选项
        mTabLayout.setViewPager(mViewPager);
        // 设置当前展示的项
        mTabLayout.setCurrentTab(mCurrentTab > tabs.length - 1 ? tabs.length - 1 : mCurrentTab);
    }

    public SupportFragment [] getFragments() {
        return mFragments;
    }
}
