package com.cqray.android.util;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

public class RecyclerUtils {

    public static void setScollCompat(RecyclerView recyclerView) {
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
    }

    public static void closeItemAnimator(RecyclerView recycler) {
        // 关闭默认动画
        RecyclerView.ItemAnimator animator = recycler.getItemAnimator();
        if (animator instanceof SimpleItemAnimator) {
            // mRecycler.getItemAnimator().setChangeDuration(0); // 也可以达到关闭动画的效果
            ((SimpleItemAnimator) animator).setSupportsChangeAnimations(false);
        }
    }
}
