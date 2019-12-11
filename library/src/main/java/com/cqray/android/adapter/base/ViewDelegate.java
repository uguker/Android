package com.cqray.android.adapter.base;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class ViewDelegate {

    static final int HEADER_VIEW = 9999999;
    static final int FOOTER_VIEW = 8888888;
    static final int EMPTY_VIEW = 7777777;
    static final int LOADING_VIEW = 6666666;

    public BaseQuickAdapter mAdapter;
    int mType;
    ViewGroup mLayout;

    public ViewDelegate(BaseQuickAdapter adapter, int type) {
        mAdapter = adapter;
        mType = type;
    }

    public boolean isEmpty() {
        return mLayout == null || mLayout.getChildCount() == 0;
    }

    public ViewGroup getLayout() {
        return mLayout;
    }

    /**
     * Add footer view to mFooterLayout and set footer view position in mFooterLayout.
     * When index = -1 or index >= child count in mFooterLayout,
     * the effect of this method is the same as that of {@link #addFooterView(View)}.
     *
     * @param footer
     * @param index  the position in mFooterLayout of this footer.
     *               When index = -1 or index >= child count in mFooterLayout,
     *               the effect of this method is the same as that of {@link #addFooterView(View)}.
     */
    public int addView(View view, final int index, int orientation) {
        if (mLayout == null) {
            LinearLayout layout = new LinearLayout(view.getContext());
            if (orientation == LinearLayout.VERTICAL) {
                layout.setOrientation(LinearLayout.VERTICAL);
                layout.setLayoutParams(new ViewGroup.LayoutParams(-1, -2));
            } else {
                layout.setOrientation(LinearLayout.HORIZONTAL);
                layout.setLayoutParams(new ViewGroup.LayoutParams(-2, -1));
            }
            mLayout = layout;
        }
        final int childCount = mLayout.getChildCount();
        int i = index;
        if (index < 0 || index > childCount) {
            i = childCount;
        }
        mLayout.addView(view, i);
        if (mLayout.getChildCount() == 1) {
            // 通知界面刷新
            int position = mAdapter.getHeaderViewPosition();
            if (position != -1) {
                mAdapter.notifyItemInserted(position);
            }
        }
        return i;
    }

    public int setView(View view, int index, int orientation) {
        if (mLayout == null || mLayout.getChildCount() <= index) {
            return addView(view, index, orientation);
        } else {
            mLayout.removeViewAt(index);
            mLayout.addView(view, index);
            return index;
        }
    }

    public <T> T createViewHolder(@NonNull ViewGroup parent, int viewType) {
//        this.mContext = parent.getContext();
//        this.mLayoutInflater = LayoutInflater.from(mContext);
        T baseViewHolder = null;
        switch (viewType) {
            case LOADING_VIEW:
                //baseViewHolder = getLoadingView(parent);
                break;
            case HEADER_VIEW:
                ViewParent headerLayoutVp = mLayout.getParent();
                if (headerLayoutVp instanceof ViewGroup) {
                    ((ViewGroup) headerLayoutVp).removeView(mLayout);
                }
                baseViewHolder = createBaseViewHolder();
                break;
            case EMPTY_VIEW:
                ViewParent emptyLayoutVp = mLayout.getParent();
                if (emptyLayoutVp instanceof ViewGroup) {
                    ((ViewGroup) emptyLayoutVp).removeView(mLayout);
                }
                baseViewHolder = createBaseViewHolder();
                break;
            case FOOTER_VIEW:
                ViewParent footerLayoutVp = mLayout.getParent();
                if (footerLayoutVp instanceof ViewGroup) {
                    ((ViewGroup) footerLayoutVp).removeView(mLayout);
                }
                baseViewHolder = createBaseViewHolder();
                break;
            default:
                //baseViewHolder = onCreateDefViewHolder(parent, viewType);
                //bindViewClickListener(baseViewHolder);
        }
        //baseViewHolder.setAdapter(this);
        return baseViewHolder;
    }


    /**
     * if you want to use subclass of BaseViewHolder in the adapter,
     * you must override the method to create new ViewHolder.
     *
     * @return new ViewHolder
     */
    @SuppressWarnings("unchecked")
    protected <T> T createBaseViewHolder() {
        Class temp = getClass();
        Class z = null;
        while (z == null && null != temp) {
            z = getInstancedGenericKClass(temp);
            temp = temp.getSuperclass();
        }
        T t;
        // 泛型擦除会导致z为null
        if (z == null) {
            t = (T) new BaseViewHolder(mLayout);
        } else {
            t = createGenericKInstance(z, mLayout);
        }
        return t != null ? t : (T) new BaseViewHolder(mLayout);
    }

    /**
     * try to create Generic K instance
     */
    @SuppressWarnings("unchecked")
    private <T> T createGenericKInstance(Class z, View view) {
        try {
            Constructor constructor;
            // inner and unstatic class
            if (z.isMemberClass() && !Modifier.isStatic(z.getModifiers())) {
                constructor = z.getDeclaredConstructor(mAdapter.getClass(), View.class);
                constructor.setAccessible(true);
                return (T) constructor.newInstance(mAdapter, view);
            } else {
                constructor = z.getDeclaredConstructor(View.class);
                constructor.setAccessible(true);
                return (T) constructor.newInstance(view);
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * get generic parameter K
     */
    private Class getInstancedGenericKClass(Class z) {
        Type type = z.getGenericSuperclass();
        if (type instanceof ParameterizedType) {
            Type[] types = ((ParameterizedType) type).getActualTypeArguments();
            for (Type temp : types) {
                if (temp instanceof Class) {
                    Class tempClass = (Class) temp;
                    if (BaseViewHolder.class.isAssignableFrom(tempClass)) {
                        return tempClass;
                    }
                } else if (temp instanceof ParameterizedType) {
                    Type rawType = ((ParameterizedType) temp).getRawType();
                    if (rawType instanceof Class && BaseViewHolder.class.isAssignableFrom((Class<?>) rawType)) {
                        return (Class<?>) rawType;
                    }
                }
            }
        }
        return null;
    }

    /**
     * remove header view from mHeaderLayout.
     * When the child count of mHeaderLayout is 0, mHeaderLayout will be set to null.
     *
     */
    public void removeView(View view) {
        if (!isEmpty()) {
            mLayout.removeView(view);
            if (isEmpty()) {
                int position = mAdapter.getHeaderViewPosition();
                if (position != -1) {
                    mAdapter.notifyItemRemoved(position);
                }
            }
        }
    }

    public void removeAllViews() {
        if (!isEmpty()) {
            mLayout.removeAllViews();
            int position = mAdapter.getHeaderViewPosition();
            if (position != -1) {
                mAdapter.notifyItemRemoved(position);
            }
        }
    }

    public void setEmptyView(View emptyView) {
//        int oldItemCount = getItemCount();
//        boolean insert = false;
//        if (mEmptyLayout == null) {
//            mEmptyLayout = new FrameLayout(emptyView.getContext());
//            final ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(-1, -1);
//            final ViewGroup.LayoutParams lp = emptyView.getLayoutParams();
//            if (lp != null) {
//                layoutParams.width = lp.width;
//                layoutParams.height = lp.height;
//            }
//            mEmptyLayout.setLayoutParams(layoutParams);
//            insert = true;
//        }
//        mEmptyLayout.removeAllViews();
//        mEmptyLayout.addView(emptyView);
//        mIsUseEmpty = true;
//        if (insert && getEmptyViewCount() == 1) {
//            int position = 0;
//            if (mHeadAndEmptyEnable && getHeaderLayoutCount() != 0) {
//                position++;
//            }
//            if (getItemCount() > oldItemCount) {
//                notifyItemInserted(position);
//            } else {
//                notifyDataSetChanged();
//            }
//        }
    }
}
