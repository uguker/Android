package com.uguke.android.swipe;

import android.app.Activity;
import android.view.View;

import androidx.annotation.NonNull;

import com.uguke.android.app.SupportFragment;

import java.util.Stack;

/**
 * 侧滑返回辅助类
 * @author LeiJue
 */
public class SwipeBackHelper {

    /** Activity堆 **/
    private static Stack<SwipeBackActivityPage> mActivityStack = new Stack<>();
    /** Fragment堆 **/
    private static Stack<SwipeBackFragmentPage> mFragmentStack = new Stack<>();

    private static SwipeBackActivityPage findPageByActivity(Activity activity){
        for (SwipeBackActivityPage page : mActivityStack) {
            if (page.mActivity == activity) {
                return page;
            }
        }
        return null;
    }

    private static SwipeBackFragmentPage findPageByFragment(SupportFragment fragment) {
        for (SwipeBackFragmentPage page : mFragmentStack) {
            if (page.mFragment == fragment) {
                return page;
            }
        }
        return null;
    }

    public static void onCreate(Activity activity) {
        SwipeBackActivityPage page;
        if ((page = findPageByActivity(activity)) == null) {
            page = mActivityStack.push(new SwipeBackActivityPage(activity));
        }
        page.onCreate();
    }

    public static void onCreate(SupportFragment fragment) {
        SwipeBackFragmentPage page;
        if ((page = findPageByFragment(fragment)) == null) {
            page = mFragmentStack.push(new SwipeBackFragmentPage(fragment));
        }
        page.onCreate();
    }

    public static void onPostCreate(Activity activity) {
        SwipeBackActivityPage page;
        if ((page = findPageByActivity(activity)) != null) {
            page.attachToActivity();
        }
    }

    public static View onAttach(SupportFragment fragment, View view) {
        SwipeBackFragmentPage page;
        if ((page = findPageByFragment(fragment)) != null) {
            return page.attachToFragment(view);
        }
        return null;
    }

    public static void onViewCreated(SupportFragment fragment, View view) {
        SwipeBackFragmentPage page;
        if ((page = findPageByFragment(fragment)) != null) {
            page.onViewCreated(view);
        }
    }

    public static void onHiddenChanged(SupportFragment fragment, boolean hidden) {
        SwipeBackFragmentPage page;
        if ((page = findPageByFragment(fragment)) != null) {
            page.onHiddenChanged(hidden);
        }
    }

    public static void onDestroy(Activity activity) {
        SwipeBackActivityPage page;
        if ((page = findPageByActivity(activity)) != null) {
            mActivityStack.remove(page);
            page.onDestroy();
        }
    }

    public static void onDestroyView(SupportFragment fragment) {
        SwipeBackFragmentPage page;
        if ((page = findPageByFragment(fragment)) != null) {
            mFragmentStack.remove(page);
            page.onDestroyView();
        }
    }

    @NonNull
    public static SwipeBack getCurrentPage(Activity activity) {
        SwipeBackActivityPage page;
        if ((page = findPageByActivity(activity)) != null) {
            int index = mActivityStack.indexOf(page);
            if (index >= 0) {
                return mActivityStack.get(index);
            }
        }
        return new SwipeBackActivityPage(activity);
    }

    @NonNull
    public static SwipeBack getCurrentPage(SupportFragment fragment) {
        SwipeBackFragmentPage page;
        if ((page = findPageByFragment(fragment)) != null) {
            int index = mFragmentStack.indexOf(page);
            if (index >= 0) {
                return mFragmentStack.get(index);
            }
        }
        return new SwipeBackFragmentPage(fragment);
    }

    static SwipeBackActivityPage getPrePage(Activity activity) {
        SwipeBackActivityPage page;
        if ((page = findPageByActivity(activity)) != null) {
            int preIndex = mActivityStack.indexOf(page) - 1;
            return preIndex >= 0 ? mActivityStack.get(preIndex) : null;
        }
        return null;
    }
}
