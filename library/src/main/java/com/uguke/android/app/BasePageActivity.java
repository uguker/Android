package com.uguke.android.app;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.uguke.android.R;
import com.uguke.android.helper.refresh.OnRefreshListener;
import com.uguke.android.helper.refresh.RefreshHelper;

/**
 * 基础分页界面
 * @author LeiJue
 */
public abstract class BasePageActivity<T> extends BaseActivity {

    private boolean mSupport;
    protected RecyclerView mRecycler;
    protected RefreshHelper<T> mRefreshHelper;
    protected BaseQuickAdapter<T, ? extends BaseViewHolder> mAdapter;

    @Override
    public void onCreating(@Nullable Bundle savedInstanceState) {
        super.onCreating(savedInstanceState);
        setContentView(R.layout.android_layout_refresh, mSupport ? Style.DEFAULT_SWIPE : Style.DEFAULT);
        mRecycler = findViewById(R.id.android_recycler);
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = onCreateAdapter();
        mRecycler.setAdapter(mAdapter);
        // 关闭默认动画
        RecyclerView.ItemAnimator animator = mRecycler.getItemAnimator();
        if (animator instanceof SimpleItemAnimator) {
            // mRecycler.getItemAnimator().setChangeDuration(0); // 也可以达到关闭动画的效果
            ((SimpleItemAnimator) animator).setSupportsChangeAnimations(false);
        }
        // 初始化刷新工具
        mRefreshHelper = new RefreshHelper<>(mViewDelegate.getRefreshLayout(), mAdapter);
        mRefreshHelper.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(int page, int rows) {
                BasePageActivity.this.onRefresh(mRefreshHelper, page, rows);
            }
        });
        mViewDelegate.getRefreshLayout().setEnablePureScrollMode(false);
    }

    /**
     * 是否支持滑动返回，在super.onCreating()之前调用
     * @param support 是否支持
     */
    public void setSwipeBackSupport(boolean support) {
        mSupport = support;
    }

    /**
     * 开启默认动画
     */
    public void openDefaultAnimator() {
        RecyclerView.ItemAnimator animator = mRecycler.getItemAnimator();
        if (animator instanceof SimpleItemAnimator) {
            ((SimpleItemAnimator) animator).setSupportsChangeAnimations(true);
        }
    }

    /**
     * 创建Adapter
     * @return Adapter实现
     */
    public abstract BaseQuickAdapter<T, ? extends BaseViewHolder> onCreateAdapter();

    /**
     * 刷新数据实现
     * @param helper    刷新控件辅助类
     * @param page      分页数
     * @param rows      分页大小
     */
    public abstract void onRefresh(RefreshHelper<T> helper, int page, int rows);

}
