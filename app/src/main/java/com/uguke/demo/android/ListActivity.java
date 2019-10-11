package com.uguke.demo.android;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.uguke.android.app.SupportActivity;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author LeiJue
 */
@SuppressLint("Registered")
public class ListActivity extends SupportActivity {

//    @BindView(R.id.tv)
    TextView tv;

    @Override
    public void onCreating(@Nullable Bundle savedInstanceState) {
        super.onCreating(savedInstanceState);
        setContentView(R.layout.activity_main);
        //setNativeContentView(R.layout.activity_main);
        tv = findViewById(R.id.tv);
        loadRootFragment(R.id.content, new TestFragment());


        start(new SSFragment());

        mToolbar.setTitle("测试标题");

        startActivity(new Intent(this, MainActivity.class));

//        Observable.interval(1, TimeUnit.SECONDS)
//                //.compose(bindToLifecycle())
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(along -> {
//                    tv.setText("sb");
//                    Log.e("数据", "定还是");
//                }, throwable -> {
//                    Log.e("数据", throwable.getMessage());
//                });

//        showTips("你好啊你好啊你好啊你好啊你好啊你");
//        showLoading();
//
//        hideLoading();
    }

    @Override
    protected void onResume() {
        super.onResume();
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
