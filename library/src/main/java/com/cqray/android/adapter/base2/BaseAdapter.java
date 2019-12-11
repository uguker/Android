package com.cqray.android.adapter.base2;

import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cqray.android.adapter.base.BaseViewHolder;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * 基础适配器
 * @author LeiJue
 */
public abstract class BaseAdapter<T> extends RecyclerView.Adapter<BaseViewHolder> {

    final ViewDelegate mViewDelegate = new ViewDelegate(this);

    protected List<T> mData = new LinkedList<>();

    public BaseAdapter(int layoutResId) {
        mViewDelegate.addItemType(ViewDelegate.SIMPLE_VIEW, layoutResId);
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return mViewDelegate.createViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        int itemType = getItemViewType(position);
        switch (itemType) {
            case ViewDelegate.HEADER_VIEW:
            case ViewDelegate.FOOTER_VIEW:
            case ViewDelegate.EMPTY_VIEW:
                break;
            default:
                convert(holder, getItem(position));
        }
    }

    /**
     * 实现控件布局
     * @param holder 控件初始化辅助类
     * @param item 数据项
     */
    protected abstract void convert(BaseViewHolder holder, T item);

    public T getItem(int position) {
        return mViewDelegate.calculateItem(mData, position);
    }

    @Override
    public int getItemCount() {
        return mViewDelegate.calculateItemCount(mData);
    }

    @Override
    public int getItemViewType(int position) {
        return mViewDelegate.calculateItemViewType(mData, position);
    }

    public void setNewData(Collection<T> data) {
        mData.clear();
        if (data != null) {
            mData.addAll(data);
        }
        notifyDataSetChanged();
    }

    public void addData(@NonNull Collection<? extends T> newData) {
        mData.addAll(newData);
        notifyItemRangeInserted(mData.size() - newData.size() + mViewDelegate.getHeaderViewCount(), newData.size());
        compatibilityDataSizeChanged(newData.size());
    }

    public void addData(@IntRange(from = 0) int position, @NonNull Collection<? extends T> newData) {
        mData.addAll(position, newData);
        notifyItemRangeInserted(position + mViewDelegate.getHeaderViewCount(), newData.size());
        compatibilityDataSizeChanged(newData.size());
    }

    public void setData(@IntRange(from = 0) int position, @NonNull T data) {
        mData.set(position, data);
        notifyItemChanged(position + mViewDelegate.getHeaderViewCount());
    }

    public void addData(@NonNull T data) {
        mData.add(data);
        notifyItemInserted(mData.size() + mViewDelegate.getHeaderViewCount());
        compatibilityDataSizeChanged(1);
    }

    public void addData(@IntRange(from = 0) int position, @NonNull T data) {
        mData.add(position, data);
        notifyItemInserted(position + mViewDelegate.getHeaderViewCount());
        compatibilityDataSizeChanged(1);
    }

    /**
     * compatible getLoadMoreViewCount and getEmptyViewCount may change
     *
     * @param size Need compatible data size
     */
    private void compatibilityDataSizeChanged(int size) {
        int dataSize = mData == null ? 0 : mData.size();
        if (dataSize == size) {
            notifyDataSetChanged();
        }
    }

    public void addHeaderView(View view) {
        mViewDelegate.addView(view, ViewDelegate.HEADER_VIEW, -1, LinearLayout.VERTICAL);
    }

    public void addFooterView(View view) {
        mViewDelegate.addView(view, ViewDelegate.FOOTER_VIEW, -1, LinearLayout.VERTICAL);
    }




}
