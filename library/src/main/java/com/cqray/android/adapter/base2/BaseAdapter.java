package com.cqray.android.adapter.base2;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cqray.android.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

public class BaseAdapter extends RecyclerView.Adapter<BaseViewHolder> implements LayoutProvider {

    final LayoutDelegate mEmptyDelegate = new LayoutDelegate(this, LayoutDelegate.EMPTY);
    final LayoutDelegate mFooterDelegate = new LayoutDelegate(this, LayoutDelegate.FOOTER);
    final LayoutDelegate mHeaderDelegate = new LayoutDelegate(this, LayoutDelegate.HEADER);
    final LayoutDelegate mLoadingDelegate = new LayoutDelegate(this, LayoutDelegate.LOADING);

    private List mData = new ArrayList();

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    @Override
    public int getViewPosition(int type) {
        switch (type) {
            case LayoutDelegate.EMPTY:

                break;
            case LayoutDelegate.FOOTER:
                if (mEmptyDelegate.isNotEmpty()) {
                    int position = 1;
//                    if (mHeadAndEmptyEnable && getHeaderLayoutCount() != 0) {
//                        position++;
//                    }
//                    if (mFootAndEmptyEnable) {
//                        return position;
//                    }
                } else {
                    return mHeaderDelegate.isNotEmpty() ? 1 + mData.size() : mData.size();
                }
                break;
            case LayoutDelegate.HEADER:
                if (mEmptyDelegate.isNotEmpty()) {

                } else {
                    return 0;
                }

//                if (getEmptyViewCount() == 1) {
//                    if (mHeadAndEmptyEnable) {
//                        return 0;
//                    }
//                } else {
//                    return 0;
//                }
                break;
            case LayoutDelegate.LOADING:

                break;
            default:
        }
        return -1;
    }
}
