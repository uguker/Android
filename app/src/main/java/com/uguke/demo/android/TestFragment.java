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
        tv.setText("填词啊");

    }

    @Override
    public boolean onSwipeBackSupport() {
        return false;
    }
}
