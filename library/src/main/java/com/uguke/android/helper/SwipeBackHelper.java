package com.uguke.android.helper;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.fragment.app.FragmentActivity;

import com.uguke.android.app.SupportActivity;
import com.uguke.android.app.SupportFragment;
import com.uguke.android.widget.SwipeBackLayout;

import java.util.concurrent.ConcurrentHashMap;


public class SwipeBackHelper {


    private static ConcurrentHashMap<FragmentActivity, SwipeBackLayout> mActivityMap = new ConcurrentHashMap<>();
    private static ConcurrentHashMap<SupportFragment, SwipeBackLayout> mFragmentMap = new ConcurrentHashMap<>();



//    private static final Stack<SwipeBackPage> mPageStack = new Stack<>();
//
//    private static SwipeBackPage findHelperByActivity(Activity activity){
//        for (SwipeBackPage swipeBackPage : mPageStack) {
//            if (swipeBackPage.mActivity == activity) return swipeBackPage;
//        }
//        return null;
//    }
//
//    public static SwipeBackPage getCurrentPage(Activity activity){
//        SwipeBackPage page;
//        if ((page = findHelperByActivity(activity)) == null){
//            throw new RuntimeException("You Should call SwipeBackHelper.onCreate(activity) first");
//        }
//        return page;
//    }
//
    public static void create(FragmentActivity activity) {
//        SwipeBackPage page;
//        if ((page = findHelperByActivity(activity)) == null){
//            page = mPageStack.push(new SwipeBackPage(activity));
//        }
//        page.onCreate();

//        SwipeBackFragment fragment;
//        SwipeBackActivity ac;

        if (mActivityMap.get(activity) == null) {

            Window window = activity.getWindow();
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            window.getDecorView().setBackgroundColor(Color.TRANSPARENT);
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            SwipeBackLayout layout = new SwipeBackLayout(activity);
            layout.setLayoutParams(params);
//            if (mFragment == null) {
//                mSwipeLayout.attachToActivity(mActivity);
//            } else {
//                // mSwipeLayout.attachToFragment(mFragment);
//            }
            mActivityMap.put(activity, layout);
        }
    }

    public static void create(SupportFragment fragment) {
//        SwipeBackPage page;
//        if ((page = findHelperByActivity(activity)) == null){
//            page = mPageStack.push(new SwipeBackPage(activity));
//        }
//        page.onCreate();

        if (mFragmentMap.get(fragment) == null) {

            SwipeBackLayout layout = new SwipeBackLayout(fragment.getContext());
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(-1, -1);
            layout.setLayoutParams(params);
            layout.setBackgroundColor(Color.TRANSPARENT);
//            Window window = activity.getWindow();
//            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//            window.getDecorView().setBackgroundColor(Color.TRANSPARENT);
//            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
//                    ViewGroup.LayoutParams.MATCH_PARENT,
//                    ViewGroup.LayoutParams.MATCH_PARENT);
//            SwipeBackLayout layout = new SwipeBackLayout(activity);
//            layout.setLayoutParams(params);
//            if (mFragment == null) {
//                mSwipeLayout.attachToActivity(mActivity);
//            } else {
//                // mSwipeLayout.attachToFragment(mFragment);
//            }
            mFragmentMap.put(fragment, layout);
        }
    }

    public static void attach(FragmentActivity activity){
        SwipeBackLayout layout;
        if ((layout = mActivityMap.get(activity)) == null) {
            throw new RuntimeException("You Should call SwipeBackHelper.onCreate() first");
        }
        layout.setEnableGesture(false);
        layout.attachToActivity(activity);
    }

    public static View attach(SupportFragment fragment, View view){
        SwipeBackLayout layout;
        if ((layout = mFragmentMap.get(fragment)) == null) {
            throw new RuntimeException("You Should call SwipeBackHelper.onCreate() first" );
        }
        layout.attachToFragment(fragment, view);
        return layout;
    }

    public static void destory(SupportActivity activity) {
        mActivityMap.remove(activity);
    }

    public static void destroy(SupportFragment fragment){
        SwipeBackLayout layout;
        if ((layout = mFragmentMap.get(fragment)) == null) {
            throw new RuntimeException("You Should call SwipeBackHelper.onCreate() first");
        }
        layout.internalCallOnDestroyView();
        // 移除Fragment
        mFragmentMap.remove(fragment);
    }
//
//    public static void finish(Activity activity){
//        SwipeBackPage page;
//        if ((page = findHelperByActivity(activity)) == null){
//            throw new RuntimeException("You Should call SwipeBackHelper.onCreate(activity) first");
//        }
//        page.scrollToFinishActivity();
//    }
//
//    static SwipeBackPage getPrePage(SwipeBackPage activity){
//        int index = mPageStack.indexOf(activity);
//        if (index>0)return mPageStack.get(index-1);
//        else return null;
//    }
//
}
