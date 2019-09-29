package com.uguke.android.app;

import android.os.Bundle;
import android.os.Parcelable;
import android.util.SparseArray;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.uguke.android.widget.TabEntity;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Fragment tab项
 * @author LeiJue
 */
public class FragmentTab implements TabEntity {

    private String mTitle;
    private int mSelectedIcon;
    private int mUnselectedIcon;
    private Bundle mArguments;
    private Class<? extends SupportFragment> mFragmentClass;

    public FragmentTab(String title, Class<? extends SupportFragment> fragmentClass) {
        mTitle = title;
        mFragmentClass = fragmentClass;
        mArguments = new Bundle();
    }

    public FragmentTab selectedIcon(int selectedIcon) {
        mSelectedIcon = selectedIcon;
        return this;
    }

    public FragmentTab unselectedIcon(int unselectedIcon) {
        mUnselectedIcon = unselectedIcon;
        return this;
    }

    public FragmentTab withString(@Nullable String key, @Nullable String value) {
        mArguments.putString(key, value);
        return this;
    }

    public FragmentTab withBoolean(@Nullable String key, boolean value) {
        mArguments.putBoolean(key, value);
        return this;
    }

    public FragmentTab withShort(@Nullable String key, short value) {
        mArguments.putShort(key, value);
        return this;
    }

    public FragmentTab withInt(@Nullable String key, int value) {
        mArguments.putInt(key, value);
        return this;
    }

    public FragmentTab withLong(@Nullable String key, long value) {
        mArguments.putLong(key, value);
        return this;
    }

    public FragmentTab withDouble(@Nullable String key, double value) {
        mArguments.putDouble(key, value);
        return this;
    }

    public FragmentTab withByte(@Nullable String key, byte value) {
        mArguments.putByte(key, value);
        return this;
    }

    public FragmentTab withChar(@Nullable String key, char value) {
        mArguments.putChar(key, value);
        return this;
    }

    public FragmentTab withFloat(@Nullable String key, float value) {
        mArguments.putFloat(key, value);
        return this;
    }

    public FragmentTab withCharSequence(@Nullable String key, @Nullable CharSequence value) {
        mArguments.putCharSequence(key, value);
        return this;
    }

    public FragmentTab withParcelable(@Nullable String key, @Nullable Parcelable value) {
        mArguments.putParcelable(key, value);
        return this;
    }

    public FragmentTab withParcelableArray(@Nullable String key, @Nullable Parcelable[] value) {
        mArguments.putParcelableArray(key, value);
        return this;
    }

    public FragmentTab withParcelableArrayList(@Nullable String key, @Nullable ArrayList<? extends Parcelable> value) {
        mArguments.putParcelableArrayList(key, value);
        return this;
    }

    public FragmentTab withSparseParcelableArray(@Nullable String key, @Nullable SparseArray<? extends Parcelable> value) {
        mArguments.putSparseParcelableArray(key, value);
        return this;
    }

    public FragmentTab withIntegerArrayList(@Nullable String key, @Nullable ArrayList<Integer> value) {
        mArguments.putIntegerArrayList(key, value);
        return this;
    }

    public FragmentTab withStringArrayList(@Nullable String key, @Nullable ArrayList<String> value) {
        mArguments.putStringArrayList(key, value);
        return this;
    }

    public FragmentTab withCharSequenceArrayList(@Nullable String key, @Nullable ArrayList<CharSequence> value) {
        mArguments.putCharSequenceArrayList(key, value);
        return this;
    }

    public FragmentTab withSerializable(@Nullable String key, @Nullable Serializable value) {
        mArguments.putSerializable(key, value);
        return this;
    }

    public FragmentTab withByteArray(@Nullable String key, @Nullable byte[] value) {
        mArguments.putByteArray(key, value);
        return this;
    }

    public FragmentTab withShortArray(@Nullable String key, @Nullable short[] value) {
        mArguments.putShortArray(key, value);
        return this;
    }

    public FragmentTab withCharArray(@Nullable String key, @Nullable char[] value) {
        mArguments.putCharArray(key, value);
        return this;
    }

    public FragmentTab withFloatArray(@Nullable String key, @Nullable float[] value) {
        mArguments.putFloatArray(key, value);
        return this;
    }

    public FragmentTab withCharSequenceArray(@Nullable String key, @Nullable CharSequence[] value) {
        mArguments.putCharSequenceArray(key, value);
        return this;
    }

    public FragmentTab withBundle(@Nullable String key, @Nullable Bundle value) {
        mArguments.putBundle(key, value);
        return this;
    }


    @Override
    public String getTitle() {
        return mTitle;
    }

    @Override
    public int getSelectedIcon() {
        return mSelectedIcon;
    }

    @Override
    public int getUnselectedIcon() {
        return mUnselectedIcon;
    }

    public String getTag() {
        return mTitle + mFragmentClass.getName();
    }

    public Class<? extends Fragment> getFragmentClass() {
        return mFragmentClass;
    }

    public SupportFragment newFragment() {
        try {
            SupportFragment fragment = mFragmentClass.newInstance();
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
