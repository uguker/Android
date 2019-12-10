package com.cqray.android.adapter.base2;

import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.annotation.NonNull;

/**
 * 布局委托
 * @author LeiJue
 */
public class LayoutDelegate {
    static final int HEADER = 9999999;
    static final int FOOTER = 8888888;
    static final int EMPTY = 7777777;
    static final int LOADING = 6666666;

    private int mType;
    private BaseAdapter mAdapter;
    private ViewGroup mLayout;

    public LayoutDelegate(BaseAdapter adapter, int type) {
        mAdapter = adapter;
        mType = type;
    }

    public ViewGroup getLayout() {
        return mLayout;
    }

    public boolean isNotEmpty() {
        return mLayout != null && mLayout.getChildCount() > 0;
    }

    public <T> T createViewHolder(@NonNull ViewGroup parent, int viewType) {
        T viewHolder = null;
        switch (viewType) {
            case LOADING:
            case HEADER:
            case EMPTY:
            case FOOTER:
                ViewParent footerLayoutVp = mLayout.getParent();
                if (footerLayoutVp instanceof ViewGroup) {
                    ((ViewGroup) footerLayoutVp).removeView(mLayout);
                }
                //viewHolder = createBaseViewHolder();
                break;
            default:
        }
        return viewHolder;
    }

    public void notifyItemInserted() {
        // 通知界面刷新
        int position = mAdapter.getViewPosition(mType);
        if (position != -1) {
            mAdapter.notifyItemInserted(position);
        }
    }

    public void notifyItemRemoved() {
        int position = mAdapter.getViewPosition(mType);
        if (position != -1) {
            mAdapter.notifyItemRemoved(position);
        }
    }
}
