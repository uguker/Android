package com.uguke.android.app.delegate;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.uguke.android.widget.SwipeBackLayout;

/**
 * Activity控件委托
 * @author LeiJue
 */
public final class ViewActivityDelegate extends ViewDelegate {

    private boolean mViewAttached;

    public ViewActivityDelegate(AppCompatActivity activity, final ViewCallback creator) {
        super(activity, creator);
        addViewCallback(new ViewCallback() {
            @Override
            public void onViewCreated(@NonNull View view) {
                creator.onViewCreated(view);
                attachSwipeBackLayout();
            }

        });
    }

    public void onPostCreate(Bundle savedInstanceState) {
        attachSwipeBackLayout();
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

    public boolean swipeBackPriority() {
        return false;
    }

    private void attachSwipeBackLayout() {
        if (mSwipeBackLayout != null && !mViewAttached) {
            Log.e("数据", "加入");
            mViewAttached = true;
            mSwipeBackLayout.attachToActivity(mActivity);
        }
    }
}
