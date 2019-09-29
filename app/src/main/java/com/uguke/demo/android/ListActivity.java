package com.uguke.demo.android;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.uguke.android.app.SupportActivity;
import com.uguke.android.app.LayoutCreator;

import java.util.List;

/**
 *
 * @author LeiJue
 */
@SuppressLint("Registered")
public class ListActivity extends SupportActivity {

    @Override
    public void onCreating(@Nullable Bundle savedInstanceState) {
        super.onCreating(savedInstanceState);
        setContentView(R.layout.activity_main);
        //setNativeContentView(R.layout.activity_main);

        loadRootFragment(R.id.content, new TestFragment());


        start(new SSFragment());

        mToolbar.setMaterialStyle(true);
        mToolbar.setTitle("测试标题");

        post(() -> {

            List<Fragment> fragmentList = getSupportFragmentManager().getFragments();

            for (Fragment fragment : fragmentList) {
                Log.e("数据", fragment.getClass().getName());
            }

        });

//        showTips("你好啊你好啊你好啊你好啊你好啊你");
//        showLoading();
//
//        hideLoading();
    }
//
//    @Override
//    public LayoutCreator onCreateHeader(ViewGroup container) {
//        return LayoutCreator.create(R.layout.bottom, container, false);
//    }
//
//    @Override
//    public LayoutCreator onCreateFooter(ViewGroup container) {
//        return LayoutCreator.create(R.layout.bottom, container, false);
//    }
}
