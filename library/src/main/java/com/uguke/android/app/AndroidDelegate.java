package com.uguke.android.app;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.uguke.android.adapter.LifecycleAdapter;
import com.uguke.android.swipe.SwipeBackHelper;
import com.uguke.android.util.KeyboardUtils;
import com.uguke.android.widget.CommonToolbar;
import com.uguke.android.widget.LoadingLayout;

import java.util.Stack;

/**
 * 应用委托
 * @author LeiJue
 */
public class AndroidDelegate {

    static final class Holder {
        static final AndroidDelegate INSTANCE = new AndroidDelegate();
    }

    private Stack<Activity> mStack = new Stack<>();
    private ViewHandler<CommonToolbar> mToolbarHandler;
    private ViewHandler<SmartRefreshLayout> mRefreshHandler;
    private ViewHandler<LoadingLayout> mLoadingHandler;
    private LifecycleAdapter mLifecycleAdapter;
    private int mAliveCount = 0;
    private boolean mSwipeBackSupport = false;

    public static AndroidDelegate getInstance() {
        return Holder.INSTANCE;
    }

    private AndroidDelegate() {}

    static void checkInit() {
        throw new RuntimeException("");
    }

    public static AndroidDelegate init(Application application) {
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
        return Holder.INSTANCE;
    }

    public AndroidDelegate setLifecycleAdapter(LifecycleAdapter adapter) {
        mLifecycleAdapter = adapter;
        return this;
    }

    public AndroidDelegate setToolbarHandler(ViewHandler<CommonToolbar> handler) {
        mToolbarHandler = handler;
        return this;
    }

    public AndroidDelegate setRefreshHandler(ViewHandler<SmartRefreshLayout> handler) {
        mRefreshHandler = handler;
        return this;
    }

    public AndroidDelegate setLoadingHandler(ViewHandler<LoadingLayout> handler) {
        mLoadingHandler = handler;
        return this;
    }

    public AndroidDelegate setSwipeBackSupport(boolean support) {
        mSwipeBackSupport = support;
        return this;
    }

    public boolean isSwipeBackSupport() {
        return mSwipeBackSupport;
    }

    public LifecycleAdapter getLifecycleAdapter() {
        return mLifecycleAdapter;
    }

    public ViewHandler<CommonToolbar> getToolbarHandler() {
        return mToolbarHandler;
    }

    public ViewHandler<SmartRefreshLayout> getRefreshHandler() {
        return mRefreshHandler;
    }

    public ViewHandler<LoadingLayout> getLoadingHandler() {
        return mLoadingHandler;
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
