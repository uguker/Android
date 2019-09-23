package com.uguke.android.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.uguke.android.app.BaseFragment;

import java.util.Arrays;
import java.util.List;

public class FragmentAdapter extends FragmentPagerAdapter {

    private List<Fragment> mFragments;

    public FragmentAdapter(@NonNull FragmentManager fm, @NonNull List<Fragment> fragments) {
        super(fm);
        mFragments = fragments;
    }

    public FragmentAdapter(@NonNull FragmentManager fm, @NonNull BaseFragment[] fragments) {
        super(fm);
        mFragments.addAll(Arrays.asList(fragments));
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments == null ? 0 : mFragments.size();
    }
}
