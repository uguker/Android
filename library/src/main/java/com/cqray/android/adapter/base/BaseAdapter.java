package com.cqray.android.adapter.base;

import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import java.util.List;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public abstract class BaseAdapter<T> extends RecyclerView.Adapter<ViewHolder> {

    private List<T> mData;
    private RecyclerView mParent;

    private LinearLayout mHeaderLayout;
    private LinearLayout mFooterLayout;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        initParent(parent);


        return new ViewHolder(parent);
    }

    private void initParent(ViewGroup parent) {
        if (mParent != null && parent instanceof RecyclerView) {
            // 获取父容器
            mParent = (RecyclerView) parent;
            // 关闭默认动画。默认动画效果对有图片的项有影响
            RecyclerView.ItemAnimator animator = mParent.getItemAnimator();
            if (animator instanceof SimpleItemAnimator) {
                ((SimpleItemAnimator) animator).setSupportsChangeAnimations(false);
            }
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    public abstract void convert(ViewHolder holder, T item);

    @Override
    public int getItemCount() {
//        if (getEmptyViewCount() == 1) {
//            count = 1;
//            if (mHeadAndEmptyEnable && getHeaderLayoutCount() != 0) {
//                count++;
//            }
//            if (mFootAndEmptyEnable && getFooterLayoutCount() != 0) {
//                count++;
//            }
//        } else {
//            count = getHeaderLayoutCount() + mData.size() + getFooterLayoutCount() + getLoadMoreViewCount();
//        }

        int count;
        count = getHeaderCount() + (mData == null ? 0 : mData.size()) + getFooterCount();
        return count;
    }

    public int getHeaderCount() {
        if (mHeaderLayout == null || mHeaderLayout.getChildCount() == 0) {
            return 0;
        }
        return 1;
    }

    public int getFooterCount() {
        if (mFooterLayout == null || mFooterLayout.getChildCount() == 0) {
            return 0;
        }
        return 1;
    }

    public int addHeaderView(View header) {
        return addHeaderView(header, -1);
    }

    public int addHeaderView(View header, int index) {
        return addHeaderView(header, index, LinearLayout.VERTICAL);
    }

    public int addHeaderView(View header, int index, int orientation) {
        if (mHeaderLayout == null) {
            mHeaderLayout = new LinearLayout(header.getContext());
            if (orientation == LinearLayout.VERTICAL) {
                mHeaderLayout.setOrientation(LinearLayout.VERTICAL);
                mHeaderLayout.setLayoutParams(new RecyclerView.LayoutParams(MATCH_PARENT, WRAP_CONTENT));
            } else {
                mHeaderLayout.setOrientation(LinearLayout.HORIZONTAL);
                mHeaderLayout.setLayoutParams(new RecyclerView.LayoutParams(WRAP_CONTENT, MATCH_PARENT));
            }
        }
        final int childCount = mHeaderLayout.getChildCount();
        if (index < 0 || index > childCount) {
            index = childCount;
        }
        mHeaderLayout.addView(header, index);
        if (mHeaderLayout.getChildCount() == 1) {
//            int position = getHeaderViewPosition();
//            if (position != -1) {
//                notifyItemInserted(position);
//            }
        }
        return index;
    }

    public int setHeaderView(View header) {
        return setHeaderView(header, 0, LinearLayout.VERTICAL);
    }

    public int setHeaderView(View header, int index) {
        return setHeaderView(header, index, LinearLayout.VERTICAL);
    }

    public int setHeaderView(View header, int index, int orientation) {
        if (mHeaderLayout == null || mHeaderLayout.getChildCount() <= index) {
            return addHeaderView(header, index, orientation);
        } else {
            mHeaderLayout.removeViewAt(index);
            mHeaderLayout.addView(header, index);
            return index;
        }
    }


//    /**
//     * if show empty view will be return 1 or not will be return 0
//     *
//     * @return
//     */
//    public int getEmptyViewCount() {
//        if (mEmptyLayout == null || mEmptyLayout.getChildCount() == 0) {
//            return 0;
//        }
//        if (!mIsUseEmpty) {
//            return 0;
//        }
//        if (mData.size() != 0) {
//            return 0;
//        }
//        return 1;
//    }
}
