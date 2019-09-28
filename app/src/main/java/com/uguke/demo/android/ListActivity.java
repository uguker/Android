package com.uguke.demo.android;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.uguke.android.app.SupportActivity;

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

        mToolbar.setMaterialStyle(true);

        post(() -> {

            List<Fragment> fragmentList = getSupportFragmentManager().getFragments();

            for (Fragment fragment : fragmentList) {
                Log.e("数据", fragment.getClass().getName());
            }

        });

        showTips("你好啊");

    }

}
