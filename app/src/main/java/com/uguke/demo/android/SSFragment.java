package com.uguke.demo.android;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;

import com.uguke.android.app.BaseTabbedFragment;
import com.uguke.android.app.FragmentTab;
import com.uguke.android.app.SupportFragment;

import butterknife.OnClick;

/**
 * @author LeiJue
 */
public class SSFragment extends BaseTabbedFragment {

    @Override
    public void onCreating(@Nullable Bundle savedInstanceState) {
        super.onCreating(savedInstanceState);
        //setContentView(R.layout.fragment_info);
        //Log.e("数据", "我初始化了");
        //setBackgroundColor(Color.RED);
        //setSwipeBackEnable(true);

        loadMultipleFragment(
                new FragmentTab("我的", TestFragment.class),
                new FragmentTab("她的", TestFragment.class)
        );

    }

//    @OnClick(R.id.tv)
//    public void onClick() {
//        Intent intent = new Intent(mActivity, MainActivity.class);
//        mActivity.startActivity(intent);
//
//    }
}
