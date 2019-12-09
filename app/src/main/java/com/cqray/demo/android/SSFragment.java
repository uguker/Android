package com.cqray.demo.android;

import android.graphics.Color;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.cqray.android.app.FragmentTab;
import com.cqray.android.app.SlidingFragment;
import com.cqray.android.app.ViewCreator;
import com.cqray.android.widget.CommonToolbar;

/**
 * @author LeiJue
 */
public class SSFragment extends SlidingFragment {

    TextView tv;

    @Override
    public void onCreating(@Nullable Bundle savedInstanceState) {
        super.onCreating(savedInstanceState);
        //setContentView(R.layout.fragment_info);
        //setBackgroundColor(Color.RED);
        //setSwipeBackEnable(true);

//        SwipeBackHelper.getCurrentPage(this)
//                .setSwipeEdgePercent(0.1f);
        //applyToolbar();



        mToolbar.setRippleEnable(true)
                .setBackIcon(R.drawable.def_back_material_dark)
                .setActionIcon(1, R.drawable.widget_ic_empty)
                .setActionText(0, "你好")
                .setActionTextBold(true)
                .setActionTextColor(Color.WHITE)
                .setActionTextSize(18)
                .setBackText("返回")
                .setBackIconVisible(true)
                .setBackTextVisible(false)
                .setTitle("你好啊")
                .setTitleSpace(16)
                .setBackSpace(0)
                .setTitleGravity(CommonToolbar.CENTER)
                .setActionText(1, "6666")
                .setPadding(16, 16);

        //showLoading("我在加载中");


        loadMultipleFragment(
                new FragmentTab("我的", TestFragment.class),
                new FragmentTab("她的", TestFragment.class)
        );


    }

    @Override
    public ViewCreator onCreateHeader(ViewGroup container) {
        ViewCreator vc = ViewCreator.create(R.layout.bottom2, container);
        TextView tv = vc.findViewById(R.id.text2);
        tv.setText("我城东了");
        return vc;
    }

}
