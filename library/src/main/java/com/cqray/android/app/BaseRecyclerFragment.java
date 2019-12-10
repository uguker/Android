package com.cqray.android.app;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.cqray.android.R;
import com.cqray.android.adapter.base.BaseQuickAdapter;
import com.cqray.android.adapter.base.BaseViewHolder;
import com.cqray.android.helper.RefreshHelper;

/**
 * 基础分页界面
 * @author LeiJue
 */
public abstract class BaseRecyclerFragment<T> extends SupportFragment {

    protected RecyclerView mRecycler;
    protected RefreshHelper<T> mRefreshHelper;
    protected BaseQuickAdapter<T, ? extends BaseViewHolder> mAdapter;

    @Override
    public void onCreating(Bundle savedInstanceState) {
        super.onCreating(savedInstanceState);
        setNativeContentView(R.layout.android_layout_refresh);
        mRefreshLayout = findViewById(R.id.__android_refresh);
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
        mRefreshHelper = new RefreshHelper<>(mViewDelegate.getRefreshLayout(), mAdapter);
        mRefreshHelper.setOnRefreshListener(new RefreshHelper.OnRefreshListener() {
            @Override
            public void onRefresh(int page, int rows) {
                BaseRecyclerFragment.this.onRefresh(mRefreshHelper, page, rows);
            }
        });
        mViewDelegate.getRefreshLayout().setEnablePureScrollMode(false);
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
