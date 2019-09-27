package com.uguke.demo.android;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.allen.library.SuperButton;
import com.uguke.android.app.BaseFragment;

import butterknife.BindView;

public class MineFragment extends BaseFragment {

    @BindView(R.id.text)
    EditText mText;
    @BindView(R.id.text2)
    SuperButton mText2;
    @BindView(R.id.content)
    FrameLayout mContent;


    @Override
    public void onCreating(@Nullable Bundle savedInstanceState) {

        super.onCreating(savedInstanceState);
        setContentView(R.layout.fragment_info);
    }

//    @Override
//    public void onInit(Bundle savedInstanceState) {
//        setContentView(R.layout.activity_main);
//        hideToolbar();
//        mToolbar.setTitle("我的");
//        mText.setText("编号牌");
//        mText2.setText("编号牌");
//        mRefreshLayout.setEnablePureScrollMode(false);
//        new RefreshHelper<Integer>(mRefreshLayout)
//                .autoRefresh();
//        setSwipeBackEnable(true);
//        mContent.setBackgroundColor(Color.BLUE);
//        //setSwipeBackEnable(false);
//        Log.e("数据", "呵呵");
//
//
//    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

//    @Override
//    public View onCreateBottomView(LayoutInflater inflater, @Nullable ViewGroup container) {
//        return inflater.inflate(R.layout.bottom, container, false);
//    }

    //    @Override
//    protected View onCreateLayoutView(LayoutInflater inflater, @Nullable ViewGroup container) {
//        return inflater.inflate(R.layout.bottom, container, false);
//    }
}
