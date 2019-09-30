package com.uguke.android.swipe;

import android.view.View;

import androidx.fragment.app.FragmentActivity;

import com.uguke.android.app.SupportFragment;

import java.util.Stack;


/**
 * 侧滑返回辅助类
 * @author LeiJue
 */
public class SwipeBackHelper {

    /** Activity堆 **/
    private static Stack<SwipeBackPage> mActivityStack = new Stack<>();
    /** Fragment堆 **/
    private static Stack<SwipeBackPage> mFragmentStack = new Stack<>();

    private static SwipeBackPage findPageByActivity(FragmentActivity activity){
        for (SwipeBackPage page : mActivityStack) {
            if (page.mActivity == activity) {
                return page;
            }
        }
        return null;
    }

    private static SwipeBackPage findPageByFragment(SupportFragment fragment) {
        for (SwipeBackPage page : mFragmentStack) {
            if (page.mFragment == fragment) {
                return page;
            }
        }
        return null;
    }

    public static void onCreate(FragmentActivity activity) {
        SwipeBackPage page;
        if ((page = findPageByActivity(activity)) == null) {
            page = mActivityStack.push(new SwipeBackPage(activity));
        }
        page.onCreate();
    }

    public static void onCreate(SupportFragment fragment) {
        SwipeBackPage page;
        if ((page = findPageByFragment(fragment)) == null) {
            page = mFragmentStack.push(new SwipeBackPage(fragment));
        }
        page.onCreate();
    }

    public static void onPostCreate(FragmentActivity activity) {
        SwipeBackPage page;
        if ((page = findPageByActivity(activity)) != null) {
            page.attachToActivity();
        }
    }

    public static View onAttach(SupportFragment fragment, View view) {
        SwipeBackPage page;
        if ((page = findPageByFragment(fragment)) != null) {
            return page.attachToFragment(view);
        }
        return null;
    }

    public static void onViewCreated(SupportFragment fragment, View view) {
        SwipeBackPage page;
        if ((page = findPageByFragment(fragment)) != null) {
            page.onViewCreated(view);
        }
    }

    public static void onHiddenChanged(SupportFragment fragment, boolean hidden) {
        SwipeBackPage page;
        if ((page = findPageByFragment(fragment)) != null) {
            page.onHiddenChanged(hidden);
        }
    }

    public static void onDestroy(FragmentActivity activity) {
        SwipeBackPage page;
        if ((page = findPageByActivity(activity)) != null) {
            mActivityStack.remove(page);
            page.onDestroy();
        }
    }

    public static void onDestroy(SupportFragment fragment) {
        SwipeBackPage page;
        if ((page = findPageByFragment(fragment)) != null) {
            mFragmentStack.remove(page);
            page.onDestroy();
        }
    }

    public static SwipeBackPage getCurrentPage(FragmentActivity activity) {
        SwipeBackPage page;
        if ((page = findPageByActivity(activity)) != null) {
            int index = mActivityStack.indexOf(page);
            return index >= 0 ? mActivityStack.get(index) : null;
        }
        return null;
    }

    public static SwipeBackPage getCurrentPage(SupportFragment fragment) {
        SwipeBackPage page;
        if ((page = findPageByFragment(fragment)) != null) {
            int index = mFragmentStack.indexOf(page);
            return index >= 0 ? mFragmentStack.get(index) : null;
        }
        return null;
    }

    public static SwipeBackPage getPrePage(FragmentActivity activity) {
        SwipeBackPage page;
        if ((page = findPageByActivity(activity)) != null) {
            int preIndex = mActivityStack.indexOf(page) - 1;
            return preIndex >= 0 ? mActivityStack.get(preIndex) : null;
        }
        return null;
    }
}
