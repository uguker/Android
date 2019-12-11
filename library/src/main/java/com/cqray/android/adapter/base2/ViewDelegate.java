package com.cqray.android.adapter.base2;

import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;

import com.cqray.android.adapter.base.BaseViewHolder;

import java.util.List;

/**
 *
 * @author LeiJue
 */
public class ViewDelegate {
    static final int HEADER_VIEW = -7777;
    static final int FOOTER_VIEW = -7776;
    static final int EMPTY_VIEW = -7775;
    static final int SIMPLE_VIEW = -7774;
    static final int INVALID_VIEW = -7770;

    /** 是否使用空布局 **/
    private boolean mUseEmpty;
    private boolean mHeaderEmptyVisible;
    private boolean mFooterEmptyVisible;

    private BaseAdapter mAdapter;
    private SparseArray<ViewGroup> mViewArray;
    private SparseIntArray mLayoutIdArray;

    public ViewDelegate(BaseAdapter adapter) {
        mAdapter = adapter;
        mViewArray = new SparseArray<>();
        mLayoutIdArray = new SparseIntArray();
    }

    public void addItemType(int type, @LayoutRes int layoutId) {
        mLayoutIdArray.put(type, layoutId);
    }

    @SuppressWarnings("unchecked")
    public <T> T createViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 移除依赖
        ViewGroup layout = mViewArray.get(viewType);
        parent.removeView(layout);
        switch (viewType) {
            case HEADER_VIEW:
            case FOOTER_VIEW:
            case EMPTY_VIEW:
                return (T) new BaseViewHolder(layout);
            default:
                View view = LayoutInflater.from(parent.getContext()).inflate(
                        mLayoutIdArray.get(viewType), parent, false);
                return (T) new BaseViewHolder(view);
        }
    }

    public int getHeaderViewCount() {
        ViewGroup view = mViewArray.get(HEADER_VIEW);
        return view == null ? 0 : view.getChildCount() == 0 ? 0 : 1;
    }

    public int getFooterViewCount() {
        ViewGroup view = mViewArray.get(FOOTER_VIEW);
        return view == null ? 0 : view.getChildCount() == 0 ? 0 : 1;
    }

    public boolean isViewEmpty(int viewType) {
        ViewGroup view = mViewArray.get(viewType);
        return view == null || view.getChildCount() == 0;
    }

    public boolean isNeedShowEmpty(List<?> data) {
        ViewGroup view = mViewArray.get(EMPTY_VIEW);
        return view != null && view.getChildCount() > 0 && mUseEmpty && data.isEmpty();
    }

    /**
     * 计算对应位置的数据
     * @param data 真实的数据集合
     * @param position 项位置信息
     */
    <T> T calculateItem(List<T> data, int position) {
        if (isNeedShowEmpty(data)) {
            return null;
        }
        return data.get(position - getHeaderViewCount());
    }

    /**
     * 计算整个适配器的项数
     * @param data 真实的数据集合
     */
    int calculateItemCount(List<?> data) {
        int headerViewCount = getHeaderViewCount();
        int footerViewCount = getFooterViewCount();
        if (isNeedShowEmpty(data)) {
            int count = 1;
            if (mHeaderEmptyVisible) {
                count += headerViewCount;
            }
            if (mFooterEmptyVisible) {
                count += footerViewCount;
            }
            return count;
        } else {
            return data.size() + headerViewCount + footerViewCount;
        }
    }

    /**
     * 计算对应位置的项类型
     * @param data 真实的数据集合
     * @param position 项位置信息
     */
    int calculateItemViewType(List<?> data, int position) {
        int headerViewCount = getHeaderViewCount();
        int footerViewCount = getFooterViewCount();
        if (isNeedShowEmpty(data)) {
            switch (position) {
                case 0:
                    if (mHeaderEmptyVisible && headerViewCount > 0) {
                        return HEADER_VIEW;
                    } else {
                        return EMPTY_VIEW;
                    }
                case 1:
                    if (mHeaderEmptyVisible && headerViewCount > 0) {
                        return EMPTY_VIEW;
                    } else {
                        return FOOTER_VIEW;
                    }
                case 2:
                    return FOOTER_VIEW;
                default:
                    return -1;
            }
        } else {
            if (headerViewCount > 0 && position == 0) {
                return HEADER_VIEW;
            }
            if (footerViewCount > 0 && position == headerViewCount + data.size()) {
                return FOOTER_VIEW;
            }
            return SIMPLE_VIEW;
        }
    }

    public int addView(View view, int type, final int index, int orientation) {
        ViewGroup layout = mViewArray.get(type);
        if (layout == null) {
            if (type == HEADER_VIEW || type == FOOTER_VIEW) {
                layout = new LinearLayout(view.getContext());
                if (orientation == LinearLayout.VERTICAL) {
                    layout.setLayoutParams(new ViewGroup.LayoutParams(-1, -2));
                    ((LinearLayout) layout).setOrientation(LinearLayout.VERTICAL);
                } else {
                    layout.setLayoutParams(new ViewGroup.LayoutParams(-2, -1));
                    ((LinearLayout) layout).setOrientation(LinearLayout.HORIZONTAL);
                }
            } else {
                layout = new FrameLayout(view.getContext());
                layout.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
            }
            mViewArray.put(type, layout);
        }
        final int childCount = layout.getChildCount();
        int i = index;
        if (index < 0 || index > childCount) {
            i = childCount;
        }
        layout.addView(view, i);
        if (layout.getChildCount() == 1) {
            // 通知界面刷新
//            int position = mAdapter.getHeaderViewPosition();
//            if (position != -1) {
//                mAdapter.notifyItemInserted(position);
//            }
            mAdapter.notifyDataSetChanged();
        }
        return i;
    }
}
