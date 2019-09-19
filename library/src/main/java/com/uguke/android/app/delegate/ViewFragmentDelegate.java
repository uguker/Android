package com.uguke.android.app.delegate;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.FloatRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.uguke.android.util.ButterKnifeUtils;
import com.uguke.android.widget.SwipeBackLayout;

import me.yokeyword.fragmentation.SupportFragment;

/**
 * Fragment控件委托
 * @author LeiJue
 */
public final class ViewFragmentDelegate extends ViewDelegate {

    private boolean mViewAttached;
    protected SwipeBackLayout mSwipeBackLayout;

    public ViewFragmentDelegate(SupportFragment fragment, final ViewCallback callback) {
        super(fragment, callback);
        addViewCallback(new ViewCallback() {
            @Override
            public void onViewCreated(@NonNull View view) {
                callback.onViewCreated(view);
                attachSwipeBackLayout(view);
            }
        });
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        if (view instanceof SwipeBackLayout) {
            View childView = ((SwipeBackLayout) view).getChildAt(0);
            mFragment.getSupportDelegate().setBackground(childView);
        } else {
            mFragment.getSupportDelegate().setBackground(view);
        }
        attachSwipeBackLayout(view);
    }

    public View attachToSwipeBack(View view) {
        if (mSwipeBackLayout == null) {
            return view;
        }
        attachSwipeBackLayout(view);
        return mSwipeBackLayout;
    }

    public void onHiddenChanged(boolean hidden) {
        if (hidden && mSwipeBackLayout != null) {
            mSwipeBackLayout.hiddenFragment();
        }
    }

    public void onDestroyView() {
        if (mSwipeBackLayout != null) {
            mSwipeBackLayout.internalCallOnDestroyView();
            mSwipeBackLayout = null;
        }
        if (mUnBinder != null) {
            ButterKnifeUtils.unbind(mUnBinder);
            mUnBinder = null;
        }
    }

    public void setSwipeBackEnable(boolean enable) {
        if (mSwipeBackLayout != null) {
            mSwipeBackLayout.setEnableGesture(enable);
        }
    }

    public void setEdgeLevel(SwipeBackLayout.EdgeLevel edgeLevel) {
        if (mSwipeBackLayout != null) {
            mSwipeBackLayout.setEdgeLevel(edgeLevel);
        }
    }

    public void setEdgeLevel(int widthPixel) {
        if (mSwipeBackLayout != null) {
            mSwipeBackLayout.setEdgeLevel(widthPixel);
        }
    }

    public void setParallaxOffset(@FloatRange(from = 0.0f, to = 1.0f) float offset) {
        if (mSwipeBackLayout != null) {
            mSwipeBackLayout.setParallaxOffset(offset);
        }
    }

    private void attachSwipeBackLayout(View view) {
        if (mSwipeBackLayout != null && !mViewAttached) {
            mViewAttached = true;
            mSwipeBackLayout.attachToFragment(mFragment, view);
        }
    }
}
