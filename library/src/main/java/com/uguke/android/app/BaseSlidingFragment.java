package com.uguke.android.app;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.uguke.android.R;

import me.yokeyword.fragmentation.ISupportFragment;

import static androidx.fragment.app.FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT;

/**
 * 基础标签活动
 * @author LeiJue
 */
public abstract class BaseSlidingFragment extends BaseFragment {

    protected SlidingTabLayout mTabLayout;
    protected ISupportFragment[] mFragments;
    protected int mCurrentTab = 0;
    private boolean mSupport;
    protected ViewPager mPager;

    @Override
    public void onCreating(Bundle savedInstanceState) {
        setContentView(R.layout.android_layout_sliding, mSupport ? Style.NATIVE_SWIPE : Style.NATIVE);
        // 恢复选项位置
        if (savedInstanceState != null) {
            mCurrentTab = savedInstanceState.getInt("currentTab", 0);
        }
        mPager = findViewById(R.id.android_fragment);
        mTabLayout = findViewById(R.id.android_tab);
        mPager.setOffscreenPageLimit(2);

        mTabLayout.addOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                showHideFragment(mFragments[position]);
                mCurrentTab = position;
            }

            @Override
            public void onTabReselect(int position) {
                mCurrentTab = position;
            }
        });

        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                showHideFragment(mFragments[position]);
                mCurrentTab = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("currentTab", mCurrentTab);
    }


    /**
     * 是否支持滑动返回，在super.onCreating()之前调用
     * @param support 是否支持
     */
    public void setSwipeBackSupport(boolean support) {
        mSupport = support;
    }


    public void loadMultipleRootFragment(FragmentTab... tabs) {
        // Fragment数组
        mFragments = new ISupportFragment[tabs.length];
        FragmentTab temp = tabs[0];
        if (findChildFragmentByTag(temp.getTabTitle() + temp.getFragmentClass().getName()) == null) {
            // 初始化数组
            for (int i = 0, len = tabs.length; i < len; i++) {
                mFragments[i] = tabs[i].newFragment();
            }
            // 设置数据适配
            mPager.setAdapter(new FragmentPagerAdapter(getChildFragmentManager(),
                    BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
                @NonNull
                @Override
                public Fragment getItem(int position) {
                    return (Fragment) mFragments[position];
                }

                @Override
                public int getCount() {
                    return mFragments.length;
                }
            });
            mTabLayout.setViewPager(mPager, FragmentTab.toStrArray(tabs));
        } else {
            for (int i = 0, len = tabs.length; i < len; i++) {
                mFragments[i] = findChildFragmentByTag(tabs[i].getTabTitle() + tabs[i].getFragmentClass().getName());
            }
        }
        // 设置当前选项
        mTabLayout.setCurrentTab(mCurrentTab);
        mPager.setOffscreenPageLimit(tabs.length);
    }

    public void reloadMultipleRootFragment(FragmentTab... tabs) {
        // Fragment数组
        mFragments = new ISupportFragment[tabs.length];
        for (int i = 0, len = tabs.length; i < len; i++) {
            mFragments[i] = tabs[i].newFragment();
        }
        // 设置数据适配
        mPager.setAdapter(new FragmentStatePagerAdapter(getChildFragmentManager(), BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

            @NonNull
            @Override
            public Fragment getItem(int position) {
                return (Fragment) mFragments[position];
            }

            @Override
            public int getCount() {
                return mFragments.length;
            }
        });
        mTabLayout.setViewPager(mPager, FragmentTab.toStrArray(tabs));
        // 设置当前选项
        mTabLayout.setCurrentTab(mCurrentTab);
        mPager.setOffscreenPageLimit(tabs.length);
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

    public void applyBottomTab() {
        ViewGroup parent = (ViewGroup) mTabLayout.getParent();
        parent.removeView(mTabLayout);
        parent.addView(mTabLayout);
        //mTabLayout.setIndicatorHeight(0);
        //mTabLayout.setUnderlineGravity(Gravity.TOP);
    }

}
