package com.cqray.demo.android;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cqray.android.adapter.base.BaseViewHolder;
import com.cqray.android.adapter.base2.BaseAdapter;
import com.cqray.android.app.SupportActivity;
import com.cqray.android.util.RecyclerUtils;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import jp.wasabeef.recyclerview.animators.FadeInDownAnimator;
import jp.wasabeef.recyclerview.animators.FadeInUpAnimator;
import jp.wasabeef.recyclerview.animators.ScaleInAnimator;


/**
 * @author LeiJue
 */
public class ListActivity extends SupportActivity {

    @Override
    public void onCreating(@Nullable Bundle savedInstanceState) {
        super.onCreating(savedInstanceState);

        setContentView(R.layout.activity_main);

        RecyclerView recycler = findViewById(R.id.recycler);
        BaseAdapter<Integer> adapter = new BaseAdapter<Integer>(R.layout.item_test) {
            @Override
            protected void convert(BaseViewHolder holder, Integer item) {
                holder.setText(R.id.tv, String.valueOf(item));
            }
        };

        recycler.setLayoutManager(new GridLayoutManager(this, 3, RecyclerView.VERTICAL, false));
        recycler.setAdapter(adapter);

//        adapter.addData(0);
//        adapter.addData(0);
//        adapter.addData(0);
//        adapter.addData(0);
//        adapter.addData(0);

        View view = LayoutInflater.from(this).inflate(R.layout.item_header, recycler, false);
        adapter.addHeaderView(view);

        View view2 = LayoutInflater.from(this).inflate(R.layout.item_header, recycler, false);
        adapter.addHeaderView(view2);
        View view3 = LayoutInflater.from(this).inflate(R.layout.item_header, recycler, false);
        adapter.addFooterView(view3);

        //RecyclerUtils.closeItemAnimator(recycler);

        RecyclerView.ItemAnimator animator = new ScaleInAnimator();
        animator.setAddDuration(250);

        recycler.setItemAnimator(animator);

        CompositeDisposable disposable = new CompositeDisposable();
        Disposable d = Observable.interval(1000, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> {
                    if (adapter.getItemCount() < 10) {
                        adapter.addData(adapter.getItemCount());
                        adapter.addData(adapter.getItemCount());
                        adapter.addData(adapter.getItemCount());
                        adapter.addData(adapter.getItemCount());
                        adapter.addData(adapter.getItemCount());
                        adapter.addData(adapter.getItemCount());
                    } else {
                        disposable.dispose();
                    }
                });
        disposable.add(d);

    }


    @Override
    protected void onResume() {
        super.onResume();
    }

}
