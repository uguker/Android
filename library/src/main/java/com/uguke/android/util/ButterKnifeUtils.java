package com.uguke.android.util;

import android.app.Activity;
import android.view.View;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * ButterKnife反射工具
 * @author LeiJue
 */
public class ButterKnifeUtils {

    private static Class<?> knifeClass;
    private static Class<?> binderClass;
    private static boolean support = true;

    private ButterKnifeUtils() {
        throw new UnsupportedOperationException("can't instantiate me.");
    }

    public static Object bind(Object target, View resource) {
        // 如果不支持ButterKnife
        if (!support) {
            return null;
        }
        try {
            if (knifeClass == null) {
                knifeClass = Class.forName("butterknife.ButterKnife");
            }
            Method method = knifeClass.getMethod("bind", Object.class, View.class);
            return method.invoke(null, target, resource);
        } catch (ClassNotFoundException e) {
            support = false;
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object bind(Activity act) {
        // 如果不支持ButterKnife
        if (!support) {
            return null;
        }
        try {
            if (knifeClass == null) {
                knifeClass = Class.forName("butterknife.ButterKnife");
            }
            Method method = knifeClass.getMethod("bind", Activity.class);
            return method.invoke(null, act);
        } catch (ClassNotFoundException e) {
            support = false;
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void unbind(Object binder) {
        // binder为空不作为或不支持ButterKnife
        if (binder == null || !support) {
            return;
        }
        try {
            if (binderClass == null) {
                binderClass = Class.forName("butterknife.Unbinder");
            }
            Method method = binderClass.getMethod("unbind");
            method.invoke(binder);
        } catch (ClassNotFoundException e) {
            support = false;
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
