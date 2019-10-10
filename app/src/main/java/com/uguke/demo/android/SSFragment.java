package com.uguke.demo.android;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;

import com.uguke.android.app.BaseTabbedFragment;
import com.uguke.android.app.FragmentTab;
import com.uguke.android.bus.RxBus;

/**
 * @author LeiJue
 */
public class SSFragment extends BaseTabbedFragment {

    @Override
    public void onCreating(@Nullable Bundle savedInstanceState) {
        super.onCreating(savedInstanceState);
        //setContentView(R.layout.fragment_info);
        Log.e("数据", "我初始化了222");
        //setBackgroundColor(Color.RED);
        //setSwipeBackEnable(true);

        loadMultipleFragment(
                new FragmentTab("我的", TestFragment.class),
                new FragmentTab("她的", TestFragment.class)
        );

        RxBus.with(this)
                .setEventCode(1)
                .onNext(event -> {
                    Log.e("数据", "我收到了bb");
                })
                .create();

        //RxBus.getInstance().send(1, "车市成功");
    }

//    @OnClick(R.id.tv)
//    public void onClick() {
//        Intent intent = new Intent(mActivity, MainActivity.class);
//        mActivity.startActivity(intent);
//
//    }
}
