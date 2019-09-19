package com.uguke.demo.android;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.uguke.android.app.BaseFragment;

/**
 *
 *
 * @author LeiJue
 */
public class SSFragment extends BaseFragment {

    @Override
    public void onCreating(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        //setBackgroundColor(Color.RED);
        setSwipeBackEnable(true);
    }

}
