package com.cqray.android.helper;

import android.view.View;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.List;

/**
 * 刷新辅助工具
 * @author LeiJue
 */
public class RefreshHelper<T> {

    /** 起始页 **/
    private static int sDefaultStartPage = 1;
    /** 默认分页数量 **/
    private static int sDefaultPageSize = 30;
    /** 是否开启判断每页必须要充满 **/
    private static boolean sDefaultFullPage = true;
    /** 默认动画延迟时间 **/
    private static int sFinishDelayed = 150;

    private static int sDefualtEmptyResId;

    /** 起始页码 **/
    private int mStartPage;
    /** 当前页码 **/
    private int mCurrentPage;
    /** 分页大小 **/
    private int mPageSize;
    /** 是否可以分页 **/
    private boolean mPageEnable;
    /** 是否每一页必须充满 **/
    private boolean mFullPage;
    private BaseQuickAdapter<T, ? extends BaseViewHolder> mAdapter;
    private SmartRefreshLayout mRefreshLayout;

    private OnEmptyListener mOnEmptyListener;
    private OnRefreshListener mOnRefreshListener;

    public RefreshHelper(SmartRefreshLayout refreshLayout, BaseQuickAdapter<T, ? extends BaseViewHolder> adapter) {
        mRefreshLayout = refreshLayout;
        mAdapter = adapter;
        mStartPage = sDefaultStartPage;
        mCurrentPage = sDefaultStartPage;
        mPageSize = sDefaultPageSize;
        mFullPage = sDefaultFullPage;
        mPageEnable = true;
        // 添加刷新监听事件
        refreshLayout.setOnRefreshLoadMoreListener (
                new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                if (mOnRefreshListener != null) {
                    mOnRefreshListener.onRefresh(mCurrentPage, mPageSize);
                }
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                if (mOnRefreshListener != null) {
                    mCurrentPage = mStartPage;
                    mOnRefreshListener.onRefresh(mCurrentPage, mPageSize);
                }
            }
        });
    }

    public RefreshHelper<T> setStartPage(int page) {
        mStartPage = page;
        return this;
    }

    public RefreshHelper<T> setPageSize(int pageSize) {
        mPageSize = pageSize;
        return this;
    }

    public RefreshHelper<T> setCurrentPage(int page) {
        mCurrentPage = page;
        return this;
    }

    public RefreshHelper<T> setPageEnable(boolean enable) {
        mPageEnable = enable;
        return this;
    }

    public RefreshHelper<T> setEmptyView(int layoutResId) {
        mAdapter.setEmptyView(layoutResId, mRefreshLayout);
        return this;
    }

    public RefreshHelper<T> setEmptyView(View emptyView) {
        mAdapter.setEmptyView(emptyView);
        return this;
    }


    public RefreshHelper<T> autoRefresh() {
        mRefreshLayout.autoRefresh();
        return this;
    }

    public RefreshHelper<T> setOnRefreshListener(OnRefreshListener listener) {
        mOnRefreshListener = listener;
        return this;
    }

    public RefreshHelper<T> setOnEmptyListener(OnEmptyListener listener) {
        mOnEmptyListener = listener;
        return this;
    }

    public void reset() {
        mCurrentPage = mStartPage;
    }

    public void refresh() {
        if (mOnRefreshListener != null) {
            mCurrentPage = mStartPage;
            mOnRefreshListener.onRefresh(mCurrentPage, mPageSize);
        }
    }

    public void finish(List<T> data, boolean noMoreData) {
        // 如果未设置noMoreData，则自动计算当前页是否排满，
        // 以此判定是否有下一页，会出现这页满，下页无数据的情况
        if (!noMoreData) {
            // 如果没有开启满页验证，怎么有更多数据，否则进行计算
            noMoreData = mFullPage && (data == null || data.size() < mPageSize);
        }
        // 是起始页
        if (mCurrentPage == mStartPage) {
            mAdapter.setNewData(data);
            if (noMoreData || !mPageEnable) {
                mRefreshLayout.finishRefreshWithNoMoreData();
            } else {
                mRefreshLayout.finishRefresh(sFinishDelayed);
            }
            // 数据为空
            if (mOnEmptyListener != null && data != null && !data.isEmpty()) {
                mOnEmptyListener.onEmpty();
                mRefreshLayout.setEnableLoadMore(false);
            } else {
                mRefreshLayout.setEnableLoadMore(true);
            }
        } else {
            // 加载更多页
            if (data != null) {
                mAdapter.addData(data);
            }
            if (noMoreData || !mPageEnable) {
                mRefreshLayout.finishLoadMoreWithNoMoreData();
            } else {
                mRefreshLayout.finishLoadMore(sFinishDelayed);
            }
        }

        // 有更多数据，则页码加一
        if (!noMoreData && mPageEnable) {
            mCurrentPage ++;
        }
    }

    public void finish(List<T> data) {
        finish(data, false);
    }

    public void finish(String error) {
        if (mCurrentPage == mStartPage) {
            mRefreshLayout.finishRefresh(false);
        } else {
            mRefreshLayout.finishLoadMore(false);
        }
    }

    public void finish() {
        finish((String) null);
    }

    public static void setDefaultStartPage(int page) {
        sDefaultStartPage = page;
    }

    public static void setDefaultPageSize(int size) {
        sDefaultPageSize = size;
    }

    public static void setsDefaultFullPage(boolean full) {
        sDefaultFullPage = full;
    }

    public static void setFinishDelayed(int delayed) {
        sFinishDelayed = delayed;
    }

    /**
     * 刷新监听
     */
    public interface OnRefreshListener {

        /**
         * 刷新动作监听
         * @param page  页码
         * @param pageSize 页面大小
         */
        void onRefresh(int page, int pageSize);
    }

    /**
     * 空数据回调
     */
    public interface OnEmptyListener {
        /**
         * 空数据
         */
        void onEmpty();
    }

}
