package com.uguke.android.app;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.uguke.android.R;
import com.uguke.android.helper.RefreshHelper;

/**
 * 基础分页界面
 * @author LeiJue
 */
public abstract class BasePageFragment<T> extends SupportFragment {

    protected RecyclerView mRecycler;
    protected RefreshHelper<T> mRefreshHelper;
    protected BaseQuickAdapter<T, ? extends BaseViewHolder> mAdapter;

    @Override
    public void onCreating(Bundle savedInstanceState) {
        super.onCreating(savedInstanceState);
        setContentView(R.layout.android_layout_refresh);
        mRecycler = findViewById(R.id.android_recycler);
        mRecycler.setLayoutManager(new LinearLayoutManager(mActivity));
        mAdapter = onCreateAdapter();
        mRecycler.setAdapter(mAdapter);
        // 关闭默认动画
        RecyclerView.ItemAnimator animator = mRecycler.getItemAnimator();
        if (animator instanceof SimpleItemAnimator) {
            // mRecycler.getItemAnimator().setChangeDuration(0); // 也可以达到关闭动画的效果
            ((SimpleItemAnimator) animator).setSupportsChangeAnimations(false);
        }
        // 初始化刷新工具
        mRefreshHelper = new RefreshHelper<>(mLayoutDelegate.getRefreshLayout(), mAdapter);
        mRefreshHelper.setOnRefreshListener(new RefreshHelper.OnRefreshListener() {
            @Override
            public void onRefresh(int page, int rows) {
                BasePageFragment.this.onRefresh(mRefreshHelper, page, rows);
            }
        });
        mLayoutDelegate.getRefreshLayout().setEnablePureScrollMode(false);
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
