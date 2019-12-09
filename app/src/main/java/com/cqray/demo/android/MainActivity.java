package com.cqray.demo.android;

import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

/**
 * @author LeiJue
 */
public class MainActivity extends FragmentActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_test);


        FloatingActionButton btn = findViewById(R.id.btn);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Snackbar.make(btn, "6666", 1500).show();
            }
        }, 3000);


    }

}
