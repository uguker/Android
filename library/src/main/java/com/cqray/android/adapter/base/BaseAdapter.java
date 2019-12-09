package com.cqray.android.adapter.base;

import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.List;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public abstract class BaseAdapter<T> extends RecyclerView.Adapter<ViewHolder> {

    public static final int HEADER_VIEW = 0x00000111;
    public static final int LOADING_VIEW = 0x00000222;
    public static final int FOOTER_VIEW = 0x00000333;
    public static final int EMPTY_VIEW = 0x00000555;

    private List<T> mData;
    private RecyclerView mParent;

    private LinearLayout mHeaderLayout;
    private LinearLayout mFooterLayout;
    private FrameLayout mEmptyLayout;

    BaseQuickAdapter a;

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

    @Override
    public int getItemViewType(int position) {
//        if (getEmptyViewCount() == 1) {
//            boolean header = mHeadAndEmptyEnable && getHeaderLayoutCount() != 0;
//            switch (position) {
//                case 0:
//                    if (header) {
//                        return HEADER_VIEW;
//                    } else {
//                        return EMPTY_VIEW;
//                    }
//                case 1:
//                    if (header) {
//                        return EMPTY_VIEW;
//                    } else {
//                        return FOOTER_VIEW;
//                    }
//                case 2:
//                    return FOOTER_VIEW;
//                default:
//                    return EMPTY_VIEW;
//            }
//        }
        int headerCount = getHeaderCount();
        if (position < headerCount) {
            return HEADER_VIEW;
        } else {
            int adjPosition = position - headerCount;
            int adapterCount = mData.size();
            if (adjPosition < adapterCount) {
                return getDefItemViewType(adjPosition);
            } else {
                adjPosition = adjPosition - adapterCount;
                int footerCount = getFooterCount();
                if (adjPosition < footerCount) {
                    return FOOTER_VIEW;
                } else {
                    return LOADING_VIEW;
                }
            }
        }
    }

    protected int getDefItemViewType(int position) {
//        if (mMultiTypeDelegate != null) {
//            return mMultiTypeDelegate.getDefItemViewType(mData, position);
//        }
        return super.getItemViewType(position);
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

//    /**
//     * Called when a view created by this adapter has been attached to a window.
//     * simple to solve item will layout using all
//     * {@link #setFullSpan(RecyclerView.ViewHolder)}
//     *
//     * @param holder
//     */
//    @Override
//    public void onViewAttachedToWindow(K holder) {
//        super.onViewAttachedToWindow(holder);
//        int type = holder.getItemViewType();
//        if (type == EMPTY_VIEW || type == HEADER_VIEW || type == FOOTER_VIEW || type == LOADING_VIEW) {
//            setFullSpan(holder);
//        } else {
//            addAnimation(holder);
//        }
//    }

//    @Override
//    public void onAttachedToRecyclerView(final RecyclerView recyclerView) {
//        super.onAttachedToRecyclerView(recyclerView);
//        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
//        if (manager instanceof GridLayoutManager) {
//            final GridLayoutManager gridManager = ((GridLayoutManager) manager);
//            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
//                @Override
//                public int getSpanSize(int position) {
//                    int type = getItemViewType(position);
//                    if (type == HEADER_VIEW && isHeaderViewAsFlow()) {
//                        return 1;
//                    }
//                    if (type == FOOTER_VIEW && isFooterViewAsFlow()) {
//                        return 1;
//                    }
//                    if (mSpanSizeLookup == null) {
//                        return isFixedViewType(type) ? gridManager.getSpanCount() : 1;
//                    } else {
//                        return (isFixedViewType(type)) ? gridManager.getSpanCount() : mSpanSizeLookup.getSpanSize(gridManager,
//                                position - getHeaderLayoutCount());
//                    }
//                }
//
//
//            });
//        }
//    }
}
