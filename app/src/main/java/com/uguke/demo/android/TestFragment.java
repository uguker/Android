package com.uguke.demo.android;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.uguke.android.app.SupportFragment;

import java.util.List;

public class TestFragment extends SupportFragment {

    @Override
    public void onCreating(@Nullable Bundle savedInstanceState) {
        super.onCreating(savedInstanceState);
        Glide.with(mActivity).load("").into(new ImageView(mActivity));

        post(() -> {

            List<Fragment> fragmentList = getFragmentManager().getFragments();

            for (Fragment fragment : fragmentList) {
                Log.e("数据", fragment.getClass().getName());
            }

        });
    }
}
