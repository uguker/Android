package com.uguke.android.app;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.uguke.android.adapter.LifecycleAdapter;
import com.uguke.android.swipe.SwipeBackHelper;
import com.uguke.android.widget.CommonToolbar;
import com.uguke.android.widget.LoadingLayout;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Stack;

/**
 * 应用委托
 * @author LeiJue
 */
public class AndroidDelegate {

    static final class Holder {
        static final AndroidDelegate INSTANCE = new AndroidDelegate();
    }

    private Application mApp;
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

    public static Application getApp() {
        if (Holder.INSTANCE.mApp != null) {
            return Holder.INSTANCE.mApp;
        }
        Application application = getApplicationByReflect();
        init(application);
        return application;
    }

    public static Context getContext() {
        if (Holder.INSTANCE.mApp != null) {
            return Holder.INSTANCE.mApp.getApplicationContext();
        }
        Application application = getApplicationByReflect();
        init(application);
        return application.getApplicationContext();
    }

    public static AndroidDelegate init(Application application) {
        Holder.INSTANCE.mApp = application;
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
                clearMemory(activity);
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

    private static Application getApplicationByReflect() {
        try {
            @SuppressLint("PrivateApi")
            Class<?> activityThread = Class.forName("android.app.ActivityThread");
            Object thread = activityThread.getMethod("currentActivityThread").invoke(null);
            Object app = activityThread.getMethod("getApplication").invoke(thread);
            if (app == null) {
                throw new NullPointerException("you should init first");
            }
            return (Application) app;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        throw new NullPointerException("you should init first");
    }

    private static void clearMemory(Context context) {
        if (context == null) {
            return;
        }
        InputMethodManager inputMethodManager = (InputMethodManager)
                context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager == null) {
            return;
        }
        String [] viewArray = new String[]{"mCurRootView", "mServedView", "mNextServedView"};
        Field filed;
        Object filedObject;
        for (String view:viewArray) {
            try{
                filed = inputMethodManager.getClass().getDeclaredField(view);
                if (!filed.isAccessible()) {
                    filed.setAccessible(true);
                }
                filedObject = filed.get(inputMethodManager);
                if (filedObject != null && filedObject instanceof View) {
                    View fileView = (View) filedObject;
                    // 被InputMethodManager持有的引用是需要被销毁的
                    if (fileView.getContext() == context) {
                        // 置空
                        filed.set(inputMethodManager, null);
                    } else {
                        // 不是想要销毁的目标，进入另一层界面了，不要处理，避免影响原逻辑,也就不用继续for循环了。
                        break;
                    }
                }
            } catch(Throwable t) {
                t.printStackTrace();
            }
        }
    }
}
