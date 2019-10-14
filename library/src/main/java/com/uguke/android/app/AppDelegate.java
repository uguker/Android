package com.uguke.android.app;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.appbar.AppBarLayout;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.uguke.android.adapter.DefaultLoadingAdapter;
import com.uguke.android.adapter.LoadingAdapter;
import com.uguke.android.swipe.SwipeBackHelper;
import com.uguke.android.util.KeyboardUtils;
import com.uguke.android.widget.CommonToolbar;

import java.util.Stack;

/**
 * 应用委托
 * @author LeiJue
 */
public class AppDelegate {

    static final class Holder {
        static final AppDelegate INSTANCE = new AppDelegate();
    }

    private Stack<Activity> mStack = new Stack<>();
    private ViewHandler<AppBarLayout, CommonToolbar> mToolbarHandler;
    private ViewHandler<RelativeLayout, SmartRefreshLayout> mRefreshHandler;
    private LoadingAdapter mLoadingAdapter = new DefaultLoadingAdapter();
    private int mAliveCount = 0;
    private boolean mSwipeBackSupport = false;

    public static AppDelegate getInstance() {
        return Holder.INSTANCE;
    }

    private AppDelegate() {}

    public AppDelegate init(Application application) {
        application.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                Holder.INSTANCE.mStack.add(activity);
                if (!(activity instanceof SupportActivity) && Holder.INSTANCE.mSwipeBackSupport) {
                    SwipeBackHelper.onCreate(activity);
                    SwipeBackHelper.onPostCreate(activity);
                }
            }

            @Override
            public void onActivityStarted(Activity activity) {
                Holder.INSTANCE.mAliveCount ++;
            }

            @Override
            public void onActivityResumed(Activity activity) {}

            @Override
            public void onActivityPaused(Activity activity) {}

            @Override
            public void onActivityStopped(Activity activity) {
                Holder.INSTANCE.mAliveCount --;
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {}

            @Override
            public void onActivityDestroyed(Activity activity) {
                Holder.INSTANCE.mStack.remove(activity);
                // 清理键盘内存，避免内存泄漏
                KeyboardUtils.clearMemory(activity);
                if (!(activity instanceof SupportActivity) && Holder.INSTANCE.mSwipeBackSupport) {
                    SwipeBackHelper.onDestroy(activity);
                }
            }
        });
        return this;
    }

    public AppDelegate setToolbarHandler(ViewHandler<AppBarLayout, CommonToolbar> handler) {
        mToolbarHandler = handler;
        return this;
    }

    public AppDelegate setRefreshHandler(ViewHandler<RelativeLayout, SmartRefreshLayout> handler) {
        mRefreshHandler = handler;
        return this;
    }

    public AppDelegate setSwipeBackSupport(boolean support) {
        mSwipeBackSupport = support;
        return this;
    }

    public boolean isSwipeBackSupport() {
        return mSwipeBackSupport;
    }

    public LoadingAdapter getLoadingAdapter() {
        return mLoadingAdapter;
    }

    public ViewHandler<AppBarLayout, CommonToolbar> getToolbarHandler() {
        return mToolbarHandler;
    }

    public ViewHandler<RelativeLayout, SmartRefreshLayout> getRefreshHandler() {
        return mRefreshHandler;
    }

    // ======== Activity管理 ======== //

    @Nullable
    public Activity getCurrentActivity() {
        if (mStack.empty()) {
            return null;
        }
        return mStack.lastElement();
    }

    public int getActivityCount() {
        return mStack.size();
    }

    public boolean isCurrentActivity(@NonNull Class<? extends Activity> clazz) {
        Activity act = getCurrentActivity();
        return act != null && clazz.equals(act.getClass());
    }

    public boolean isCurrentActivity(@NonNull Activity act) {
        return act == getCurrentActivity();
    }

    public boolean isActivityContains(@NonNull Class<? extends Activity> clazz) {
        if (mStack.empty()) {
            return false;
        }
        for (Activity act : mStack) {
            if (clazz == act.getClass()) {
                return true;
            }
        }
        return false;
    }

    public boolean isActivityContains(Activity act) {
        if (mStack.empty()) {
            return false;
        }
        for (Activity a : mStack) {
            if (act == a) {
                return true;
            }
        }
        return false;
    }

    public boolean isAppForeground() {
        return mAliveCount > 0;
    }

    public void removeActivity(@NonNull Activity activity) {
        for (Activity act : mStack) {
            if (act == activity && !activity.isFinishing()) {
                activity.finish();
                mStack.remove(activity);
                break;
            }
        }
    }

    public void removeActivity(Class<? extends Activity> clazz) {
        for (Activity act : mStack) {
            if (clazz == act.getClass()) {
                act.finish();
                mStack.remove(act);
            }
        }
    }

    public void removeActivitiesOnly(@NonNull Activity activity) {
        for (Activity act : mStack) {
            if (act == activity) {
                continue;
            }
            if (!act.isFinishing()) {
                act.finish();
            }
        }
        mStack.clear();
        mStack.add(activity);
    }

    public void removeActivitiesOnly(Class<? extends Activity> clazz) {
        Activity activity = null;
        for (Activity act : mStack) {
            if (act.getClass() == clazz) {
                activity = act;
                continue;
            }
            if (!act.isFinishing()) {
                act.finish();
            }
        }
        mStack.clear();
        if (activity != null) {
            mStack.add(activity);
        }
    }

    public void removeAllActivities() {
        for (Activity act : mStack) {
            if (!act.isFinishing()) {
                act.finish();
            }
        }
        mStack.clear();
    }

}
