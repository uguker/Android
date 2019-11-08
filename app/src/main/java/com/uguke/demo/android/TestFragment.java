package com.uguke.demo.android;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import com.uguke.android.app.SupportFragment;

public class TestFragment extends SupportFragment {

    @Override
    public void onCreating(@Nullable Bundle savedInstanceState) {
        super.onCreating(savedInstanceState);
        setContentView(R.layout.bottom);
        TextView tv = findViewById(R.id.text2);
        hideToolbar();
        showContent();
        tv.setText("填词啊");
        showLoading("我在加载中");


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
