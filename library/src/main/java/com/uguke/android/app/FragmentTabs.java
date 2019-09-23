package com.uguke.android.app;

import android.os.Bundle;
import android.os.Parcelable;
import android.util.SparseArray;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.io.Serializable;
import java.util.ArrayList;

import me.yokeyword.fragmentation.ISupportFragment;

public class FragmentTabs {

    private String mTitle;
    private Bundle mArguments;
    private Class<? extends BaseFragment> mFragmentClass;

    public FragmentTabs(String title, Class<? extends BaseFragment> fragmentClass) {
        mTitle = title;
        mFragmentClass = fragmentClass;
        mArguments = new Bundle();
    }

    public FragmentTabs withString(@Nullable String key, @Nullable String value) {
        mArguments.putString(key, value);
        return this;
    }

    public FragmentTabs withBoolean(@Nullable String key, boolean value) {
        mArguments.putBoolean(key, value);
        return this;
    }

    public FragmentTabs withShort(@Nullable String key, short value) {
        mArguments.putShort(key, value);
        return this;
    }

    public FragmentTabs withInt(@Nullable String key, int value) {
        mArguments.putInt(key, value);
        return this;
    }

    public FragmentTabs withLong(@Nullable String key, long value) {
        mArguments.putLong(key, value);
        return this;
    }

    public FragmentTabs withDouble(@Nullable String key, double value) {
        mArguments.putDouble(key, value);
        return this;
    }

    public FragmentTabs withByte(@Nullable String key, byte value) {
        mArguments.putByte(key, value);
        return this;
    }

    public FragmentTabs withChar(@Nullable String key, char value) {
        mArguments.putChar(key, value);
        return this;
    }

    public FragmentTabs withFloat(@Nullable String key, float value) {
        mArguments.putFloat(key, value);
        return this;
    }

    public FragmentTabs withCharSequence(@Nullable String key, @Nullable CharSequence value) {
        mArguments.putCharSequence(key, value);
        return this;
    }

    public FragmentTabs withParcelable(@Nullable String key, @Nullable Parcelable value) {
        mArguments.putParcelable(key, value);
        return this;
    }

    public FragmentTabs withParcelableArray(@Nullable String key, @Nullable Parcelable[] value) {
        mArguments.putParcelableArray(key, value);
        return this;
    }

    public FragmentTabs withParcelableArrayList(@Nullable String key, @Nullable ArrayList<? extends Parcelable> value) {
        mArguments.putParcelableArrayList(key, value);
        return this;
    }

    public FragmentTabs withSparseParcelableArray(@Nullable String key, @Nullable SparseArray<? extends Parcelable> value) {
        mArguments.putSparseParcelableArray(key, value);
        return this;
    }

    public FragmentTabs withIntegerArrayList(@Nullable String key, @Nullable ArrayList<Integer> value) {
        mArguments.putIntegerArrayList(key, value);
        return this;
    }

    public FragmentTabs withStringArrayList(@Nullable String key, @Nullable ArrayList<String> value) {
        mArguments.putStringArrayList(key, value);
        return this;
    }

    public FragmentTabs withCharSequenceArrayList(@Nullable String key, @Nullable ArrayList<CharSequence> value) {
        mArguments.putCharSequenceArrayList(key, value);
        return this;
    }

    public FragmentTabs withSerializable(@Nullable String key, @Nullable Serializable value) {
        mArguments.putSerializable(key, value);
        return this;
    }

    public FragmentTabs withByteArray(@Nullable String key, @Nullable byte[] value) {
        mArguments.putByteArray(key, value);
        return this;
    }

    public FragmentTabs withShortArray(@Nullable String key, @Nullable short[] value) {
        mArguments.putShortArray(key, value);
        return this;
    }

    public FragmentTabs withCharArray(@Nullable String key, @Nullable char[] value) {
        mArguments.putCharArray(key, value);
        return this;
    }

    public FragmentTabs withFloatArray(@Nullable String key, @Nullable float[] value) {
        mArguments.putFloatArray(key, value);
        return this;
    }

    public FragmentTabs withCharSequenceArray(@Nullable String key, @Nullable CharSequence[] value) {
        mArguments.putCharSequenceArray(key, value);
        return this;
    }

    public FragmentTabs withBundle(@Nullable String key, @Nullable Bundle value) {
        mArguments.putBundle(key, value);
        return this;
    }


    public String getTitle() {
        return mTitle;
    }

    public String getTag() {
        return mTitle + mFragmentClass.getName();
    }

    public Class<? extends Fragment> getFragmentClass() {
        return mFragmentClass;
    }



    public BaseFragment newFragment() {
        try {
            BaseFragment fragment = mFragmentClass.newInstance();
            if (!mArguments.isEmpty()) {
                fragment.setArguments(mArguments);
            }
            return fragment;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
