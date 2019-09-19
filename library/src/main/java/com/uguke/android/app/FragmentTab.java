package com.uguke.android.app;

import android.os.Bundle;

import com.flyco.tablayout.listener.CustomTabEntity;

import me.yokeyword.fragmentation.ISupportFragment;

/**
 * Tab项
 * @author LeiJue
 */
public class FragmentTab implements CustomTabEntity {

    private String mTitle;
    private int mSelectedRes;
    private int mUnselectedRes;
    private Bundle mArguments;
    private Class<? extends BaseFragment> mFragmentClass;


    public FragmentTab(String title) {
        this.mTitle = title;
    }


    public FragmentTab(String title, Class<? extends BaseFragment> clazz) {
        this.mTitle = title;
        this.mFragmentClass = clazz;
    }

    public FragmentTab(String title, Class<? extends BaseFragment> clazz, Bundle arguments) {
        this.mTitle = title;
        this.mFragmentClass = clazz;
        this.mArguments = arguments;
    }

    @Override
    public String getTabTitle() {
        return mTitle;
    }

    @Override
    public int getTabSelectedIcon() {
        return mSelectedRes;
    }

    @Override
    public int getTabUnselectedIcon() {
        return mUnselectedRes;
    }

    public Class getFragmentClass() {
        return mFragmentClass;
    }

    public ISupportFragment newFragment() {
        try {
            BaseFragment fragment =  mFragmentClass.newInstance();
            fragment.setArguments(mArguments);
            return fragment;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ISupportFragment [] toFragmentArray() {

        return null;
    }

    public static String [] toStrArray(FragmentTab[] tabs) {
        String [] array = new String[tabs.length];
        for(int i = 0; i < tabs.length; i++) {
            array[i] = tabs[i].mTitle;
        }
        return array;
    }

    public static class Builder {
        private String mTitle;
        private int mSelectedRes;
        private int mUnselectedRes;
        private Bundle mArguments;
        private Class<? extends BaseFragment> mFragmentClass;

        public Builder(String title) {
            mTitle = title;
        }

        public Builder setSelectedRes(int selectedRes) {
            mSelectedRes = selectedRes;
            return this;
        }

        public Builder setUnselectedRes(int unselectedRes) {
            mUnselectedRes = unselectedRes;
            return this;
        }

        public Builder setArguments(Bundle arguments) {
            mArguments = arguments;
            return this;
        }

        public Builder setFragmentClass(Class<? extends BaseFragment> fragmentClass) {
            mFragmentClass = fragmentClass;
            return this;
        }

        public FragmentTab build() {
            FragmentTab tab = new FragmentTab(null);
            tab.mTitle = mTitle;
            tab.mArguments = mArguments;
            tab.mSelectedRes = mSelectedRes;
            tab.mUnselectedRes = mUnselectedRes;
            tab.mFragmentClass = mFragmentClass;
            return tab;
        }
    }

}
