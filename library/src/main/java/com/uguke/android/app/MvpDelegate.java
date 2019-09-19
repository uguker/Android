package com.uguke.android.app;

import java.util.HashMap;
import java.util.Map;

/**
 * Mvp委托
 * @author LeiJue
 */
public class MvpDelegate {

    private Map<Class, BasePresenter> mMap = new HashMap<>();

    public MvpDelegate() {}

    public <T extends BaseView> void attach(T view, BasePresenter<T> presenter) {
        if (presenter.isAttached()) {
            presenter.attachView(view);
        }
        mMap.put(presenter.getClass(), presenter);
    }

    public <T extends BaseView> void attachViewAndPresenter(T view, BasePresenter<T> presenter) {
        if (presenter.isAttached()) {
            presenter.attachView(view);
        }
        mMap.put(presenter.getClass(), presenter);
    }

    @SuppressWarnings("unchecked")
    public <T> T getPresenter(Class<T> clazz) {
        return (T) mMap.get(clazz);
    }


    public void onDestroy() {
        for (Class clazz : mMap.keySet()) {
            mMap.get(clazz).onDestroy();
        }
        mMap.clear();
    }
}
