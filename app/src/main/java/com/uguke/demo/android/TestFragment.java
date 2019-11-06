package com.uguke.demo.android;

import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.uguke.android.app.SupportFragment;
import com.uguke.android.app.ViewCreator;
import com.uguke.android.bus.Event;
import com.uguke.android.bus.RxBus;

import org.w3c.dom.Text;

import java.util.List;

import io.reactivex.functions.Consumer;

public class TestFragment extends SupportFragment {

    @Override
    public void onCreating(@Nullable Bundle savedInstanceState) {
        super.onCreating(savedInstanceState);
        setSimpleContentView(R.layout.bottom);
        TextView tv = findViewById(R.id.text2);
        hideToolbar();
        showContent();
        tv.setText("填词啊");
        //showLoading("我在加载中");


//                .onNext(event -> {
//                    Log.e("数据", "我收到了");
//                    tv.setText(event.body.toString());
//                })
//                .create();
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

    @Override
    public boolean onSwipeBackSupport() {
        return false;
    }

//    @Override
//    public ViewCreator onCreateHeader(ViewGroup container) {
//        return ViewCreator.create(R.layout.bottom2, container);
//    }
//
//    @Override
//    public ViewCreator onCreateFooter(ViewGroup container) {
//        return ViewCreator.create(R.layout.bottom2, container);
//    }
}
