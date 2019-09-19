package com.uguke.demo.android;

import android.os.Bundle;


import androidx.annotation.Nullable;

import com.uguke.android.app.BaseActivity;
import com.uguke.android.app.Style;

/**
 * @author LeiJue
 */
public class MainActivity extends BaseActivity {

    @Override
    public void onCreating(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_main, Style.DEFAULT);
        loadRootFragment(R.id.content, new SSFragment());
    }
}
