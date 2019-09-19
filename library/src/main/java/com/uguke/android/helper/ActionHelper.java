package com.uguke.android.helper;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

/**
 * 活动辅助类
 * @author LeiJue
 */
public class ActionHelper {

    /** 前台活动数量 **/
    private int mAliveCount = 0;

    private Intent mIntent;

    private Object mObject;

    private Stack<Activity> mStack;

    private List<AppStateListener> mListeners;
    
    private Application.ActivityLifecycleCallbacks mCallbacks;
    
    static class Holder {
        static final ActionHelper INSTANCE = new ActionHelper();
    }

    private ActionHelper() {}

    public static synchronized void init(@NonNull Application app) {
        if (Holder.INSTANCE.mCallbacks == null) {
            Holder.INSTANCE.mStack = new Stack<>();
            Holder.INSTANCE.mListeners = new LinkedList<>();
            Holder.INSTANCE.mCallbacks = new Application.ActivityLifecycleCallbacks() {

                @Override
                public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                    Holder.INSTANCE.mStack.add(activity);
                }

                @Override
                public void onActivityStarted(Activity activity) {
                    if (Holder.INSTANCE.mAliveCount == 0) {
                        for (AppStateListener listener : Holder.INSTANCE.mListeners) {
                            listener.onForeground();
                        }
                    }
                    Holder.INSTANCE.mAliveCount ++;
                }

                @Override
                public void onActivityResumed(Activity activity) {}

                @Override
                public void onActivityPaused(Activity activity) {}

                @Override
                public void onActivityStopped(Activity activity) {
                    if (Holder.INSTANCE.mAliveCount == 1) {
                        for (AppStateListener listener : Holder.INSTANCE.mListeners) {
                            listener.onBackground();
                        }
                    }
                    Holder.INSTANCE.mAliveCount --;
                }

                @Override
                public void onActivitySaveInstanceState(Activity activity, Bundle outState) {}

                @Override
                public void onActivityDestroyed(Activity activity) {
                    Holder.INSTANCE.mStack.remove(activity);
                    // 清理键盘内存，避免内存泄漏
                    //Keyboard.clearMemory(activity);
                }

            };
            app.registerActivityLifecycleCallbacks(Holder.INSTANCE.mCallbacks);
        }
    }

    @Nullable
    public static Activity current() {
        checkIsReady();
        if (Holder.INSTANCE.mStack.empty()) {
            return null;
        }
        return Holder.INSTANCE.mStack.lastElement();
    }

    public static boolean isTop(Class<?> clazz) {
        Activity act = current();
        if (act != null && clazz.equals(act.getClass())) {
            return true;
        }
        return false;
    }

    public static boolean contains(Class<?> clazz) {
        checkIsReady();
        if (Holder.INSTANCE.mStack.empty()) {
            return false;
        }
        for (Activity act : Holder.INSTANCE.mStack) {
            if (clazz.getName().equals(act.getClass().getName())) {
                return true;
            }
        }
        return false;
    }

    public static boolean isForeground() {
        checkIsReady();
        return Holder.INSTANCE.mAliveCount > 0;
    }

    public static int count() {
        checkIsReady();
        return Holder.INSTANCE.mStack.size();
    }

    public static void removeActivity(@NonNull Activity activity) {
        checkIsReady();
        for (Activity act : Holder.INSTANCE.mStack) {
            if (act == activity && !activity.isFinishing()) {
                activity.finish();
                Holder.INSTANCE.mStack.remove(activity);
                break;
            }
        }
    }

    public static void removeActivitiesOnly(@NonNull Activity activity) {
        checkIsReady();
        for (Activity act : Holder.INSTANCE.mStack) {
            if (act == activity) {
                continue;
            }
            if (!act.isFinishing()) {
                act.finish();
            }
        }
        Holder.INSTANCE.mStack.clear();
        Holder.INSTANCE.mStack.add(activity);
    }

    public static void removeActivitiesOnly(Class<? extends Activity> clazz) {
        checkIsReady();
        Activity activity = null;
        for (Activity act : Holder.INSTANCE.mStack) {
            if (act.getClass() == clazz) {
                activity = act;
                continue;
            }
            if (!act.isFinishing()) {
                act.finish();
            }
        }
        Holder.INSTANCE.mStack.clear();
        if (activity != null) {
            Holder.INSTANCE.mStack.add(activity);
        }
    }

    public static void removeAllActivities() {
        checkIsReady();
        for (Activity act : Holder.INSTANCE.mStack) {
            if (!act.isFinishing()) {
                act.finish();
            }
        }
        Holder.INSTANCE.mStack.clear();
    }

    public static void addAppStateListener(@NonNull AppStateListener listener) {
        checkIsReady();
        if (!Holder.INSTANCE.mListeners.contains(listener)) {
            Holder.INSTANCE.mListeners.add(listener);
        }
    }

    public static void removeAppStateListener(@NonNull AppStateListener listener) {
        checkIsReady();
        Holder.INSTANCE.mListeners.remove(listener);
    }

    public static void removeAllCallbacks() {
        checkIsReady();
        Holder.INSTANCE.mListeners.clear();
    }

    private static void checkIsReady() {
        if (Holder.INSTANCE.mCallbacks == null) {
            throw new RuntimeException("please call Action.init() first in application!");
        }
    }

//    public static Action with(Fragment fragment) {
//        Holder.INSTANCE.mObject = fragment;
//        Holder.INSTANCE.mIntent = new Intent();
//        return Holder.INSTANCE;
//    }
//
//    public static Action with(Context context) {
//        Holder.INSTANCE.mObject = context;
//        Holder.INSTANCE.mIntent = new Intent();
//        return Holder.INSTANCE;
//    }
//
//    public static Action with(View view) {
//        Holder.INSTANCE.mObject = ViewUtils.getActivity(view);
//        Holder.INSTANCE.mIntent = new Intent();
//        return Holder.INSTANCE;
//    }
//
//    public Action putExtra(String name, boolean value) {
//        mIntent.putExtra(name, value);
//        return this;
//    }
//
//    public Action putExtra(String name, byte value) {
//        mIntent.putExtra(name, value);
//        return this;
//    }
//
//    public Action putExtra(String name, char value) {
//        mIntent.putExtra(name, value);
//        return this;
//    }
//
//    public Action putExtra(String name, short value) {
//        mIntent.putExtra(name, value);
//        return this;
//    }
//
//    public Action putExtra(String name, int value) {
//        mIntent.putExtra(name, value);
//        return this;
//    }
//
//    public Action putExtra(String name, long value) {
//        mIntent.putExtra(name, value);
//        return this;
//    }
//
//    public Action putExtra(String name, float value) {
//        mIntent.putExtra(name, value);
//        return this;
//    }
//
//    public Action putExtra(String name, double value) {
//        mIntent.putExtra(name, value);
//        return this;
//    }
//
//    public Action putExtra(String name, String value) {
//        mIntent.putExtra(name, value);
//        return this;
//    }
//
//    public Action putExtra(String name, CharSequence value) {
//        mIntent.putExtra(name, value);
//        return this;
//    }
//
//    public Action putExtra(String name, Parcelable value) {
//        mIntent.putExtra(name, value);
//        return this;
//    }
//
//    public Action putExtra(String name, Serializable value) {
//        mIntent.putExtra(name, value);
//        return this;
//    }
//
//    public Action putExtra(String name, boolean [] value) {
//        mIntent.putExtra(name, value);
//        return this;
//    }
//
//    public Action putExtra(String name, byte [] value) {
//        mIntent.putExtra(name, value);
//        return this;
//    }
//
//    public Action putExtra(String name, char [] value) {
//        mIntent.putExtra(name, value);
//        return this;
//    }
//
//    public Action putExtra(String name, short [] value) {
//        mIntent.putExtra(name, value);
//        return this;
//    }
//
//    public Action putExtra(String name, int [] value) {
//        mIntent.putExtra(name, value);
//        return this;
//    }
//
//    public Action putExtra(String name, long [] value) {
//        mIntent.putExtra(name, value);
//        return this;
//    }
//
//    public Action putExtra(String name, float [] value) {
//        mIntent.putExtra(name, value);
//        return this;
//    }
//
//    public Action putExtra(String name, double [] value) {
//        mIntent.putExtra(name, value);
//        return this;
//    }
//
//    public Action putExtra(String name, String [] value) {
//        mIntent.putExtra(name, value);
//        return this;
//    }
//
//    public Action putExtra(String name, CharSequence [] value) {
//        mIntent.putExtra(name, value);
//        return this;
//    }
//
//    public Action putExtra(String name, Parcelable [] value) {
//        mIntent.putExtra(name, value);
//        return this;
//    }
//
//    public Action putExtra(String name, Bundle value) {
//        mIntent.putExtra(name, value);
//        return this;
//    }
//
//    public Action putExtras(Intent src) {
//        mIntent.putExtras(src);
//        return this;
//    }
//
//    public Action putExtras(Bundle src) {
//        mIntent.putExtras(src);
//        return this;
//    }
//
//    public Action removeExtra(String name) {
//        mIntent.removeExtra(name);
//        return this;
//    }
//
//    public Action replaceExtra(Intent src) {
//        mIntent.replaceExtras(src);
//        return this;
//    }
//
//    public Action replaceExtra(Bundle src) {
//        mIntent.replaceExtras(src);
//        return this;
//    }
//
//    public Action putParcelableArrayListExtra(String name, ArrayList<? extends Parcelable> value) {
//        mIntent.putParcelableArrayListExtra(name, value);
//        return this;
//    }
//
//    public Action putIntegerArrayListExtra(String name, ArrayList<Integer> value) {
//        mIntent.putIntegerArrayListExtra(name, value);
//        return this;
//    }
//
//    public Action putStringArrayListExtra(String name, ArrayList<String> value) {
//        mIntent.putStringArrayListExtra(name, value);
//        return this;
//    }
//
//    public Action putCharSequenceArrayListExtra(String name, ArrayList<CharSequence> value) {
//        mIntent.putCharSequenceArrayListExtra(name, value);
//        return this;
//    }
//
//    public Action resetIntent(Intent intent) {
//        this.mIntent = new Intent(intent);
//        return this;
//    }
//
//    public Action setFlags(int flags) {
//        mIntent.setFlags(flags);
//        return this;
//    }
//
//    public Action addFlags(int flags) {
//        mIntent.addFlags(flags);
//        return this;
//    }
//
//    public Action addCategory(String category) {
//        mIntent.addCategory(category);
//        return this;
//    }
//
//    public void start(Class<? extends Activity> clazz) {
//        start(clazz, null, false, Long.MIN_VALUE);
//    }
//
//    public void startWithPop(Class<? extends Activity> clazz) {
//        start(clazz, null, true, Long.MIN_VALUE);
//    }
//
//    public void startForResult(Class<? extends Activity> clazz, int requestCode) {
//        start(clazz, null, false, requestCode);
//    }
//
//    public void start(ComponentName component) {
//        start(null, component, false, Long.MIN_VALUE);
//    }
//
//    public void startWithPop(ComponentName component) {
//        start(null, component, true, Long.MIN_VALUE);
//    }
//
//    public void startForResult(ComponentName component, int requestCode) {
//        start(null, component, false, requestCode);
//    }
//
//    private void start(Class<? extends Activity> clazz, ComponentName component, boolean pop, long requestCode) {
//        if (mObject == null) {
//            throw new NullPointerException("Action start with a null context.");
//        }
//        Activity act = getActivity();
//        Context context = act;
//        if (act == null) {
//            mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            context = getContext();
//        }
//        // 设置跳转目标
//        if (clazz == null) {
//            mIntent.setComponent(component);
//        } else {
//            mIntent.setClass(context, clazz);
//        }
//        // 如果不是ForResult
//        if (Long.MIN_VALUE == requestCode) {
//            context.startActivity(mIntent);
//            if (pop && act != null) {
//                act.finish();
//            }
//        } else {
//            // 如果是ForResult
//            if (mObject instanceof Fragment) {
//                ((Fragment) mObject).startActivityForResult(mIntent, (int) requestCode);
//            } else if (act != null) {
//                act.startActivityForResult(mIntent, (int) requestCode);
//            }
//        }
//        mObject = null;
//    }
//
//    private Activity getActivity() {
//        if (mObject instanceof Activity) {
//            return (Activity) mObject;
//        } else if (mObject instanceof Fragment) {
//            return ((Fragment) mObject).getActivity();
//        }
//        return null;
//    }
//
//    private Context getContext() {
//        if (mObject instanceof Activity) {
//            return (Context) mObject;
//        } else if (mObject instanceof Fragment) {
//            return ((Fragment) mObject).getActivity();
//        } else {
//            return (Context) mObject;
//        }
//    }

    /**
     * 应用状态监听
     */
    public interface AppStateListener {
        /**
         * 在前台
         */
        void onForeground();

        /**
         * 在后台
         */
        void onBackground();
    }
}
