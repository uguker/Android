package com.uguke.demo.android;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.uguke.android.app.SupportFragment;
import com.uguke.android.bus.Event;
import com.uguke.android.bus.RxBus;

import org.w3c.dom.Text;

import java.util.List;

import io.reactivex.functions.Consumer;

public class TestFragment extends SupportFragment {

    @Override
    public void onCreating(@Nullable Bundle savedInstanceState) {
        super.onCreating(savedInstanceState);
        setContentView(R.layout.bottom);
        Log.e("数据", "我初始化了");
        TextView tv = findViewById(R.id.text2);

        RxBus.with(this)
                .setEventCode(1)
                .onNext(event -> {
                    Log.e("数据", "我收到了");
                    tv.setText(event.body.toString());
                })
                .create();
//        Glide.with(mActivity).load("").into(new ImageView(mActivity));
//
//        post(() -> {
//
//            List<Fragment> fragmentList = getFragmentManager().getFragments();
//
//            for (Fragment fragment : fragmentList) {
//                Log.e("数据", fragment.getClass().getName());
//            }
//
//        });
    }
}
