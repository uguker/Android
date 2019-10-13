package com.uguke.demo.android;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;

import com.uguke.android.app.BaseTabbedFragment;
import com.uguke.android.app.FragmentTab;
import com.uguke.android.bus.Event;
import com.uguke.android.bus.RxBus;

import io.reactivex.functions.Consumer;

/**
 * @author LeiJue
 */
public class SSFragment extends BaseTabbedFragment {

    @Override
    public void onCreating(@Nullable Bundle savedInstanceState) {
        super.onCreating(savedInstanceState);
        //setContentView(R.layout.fragment_info);
        //setBackgroundColor(Color.RED);
        //setSwipeBackEnable(true);

        loadMultipleFragment(
                new FragmentTab("我的", TestFragment.class),
                new FragmentTab("她的", TestFragment.class)
        );

        RxBus.with(this, int.class)
                .setCode(1)
                .subscribe(event -> {
                    Log.e("数据", "我接到:" + event);
                }, throwable -> {
                    Log.e("数据", "失败:" + throwable.getMessage());
                });
        RxBus.with(this, CharSequence.class)
                .setCode(1)
                .subscribe(event -> {
                    Log.e("数据", "我接到2:" + event);
                }, throwable -> {
                    Log.e("数据", "失败2:" + throwable.getMessage());
                });

    }

//    @OnClick(R.id.tv)
//    public void onClick() {
//        Intent intent = new Intent(mActivity, MainActivity.class);
//        mActivity.startActivity(intent);
//
//    }
}
