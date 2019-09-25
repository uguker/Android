package com.uguke.demo.android;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.uguke.android.app.BaseFragment;

import butterknife.OnClick;

/**
 * @author LeiJue
 */
public class SSFragment extends BaseFragment {

    @Override
    public void onCreating(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        //setBackgroundColor(Color.RED);
        //setSwipeBackEnable(true);
    }

    @OnClick(R.id.tv)
    public void onClick() {
        Intent intent = new Intent(mActivity, MainActivity.class);
        mActivity.startActivity(intent);

    }
}
