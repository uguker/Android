package com.uguke.android.app;

import com.uguke.android.adapter.NetworkAdapter;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * 基础Presenter
 * @author LeiJue
 */
public abstract class BasePresenter<T extends BaseView> {

    private Reference<T> mReference;

    public void attachView(T view) {
        if (mReference == null) {
            mReference = new WeakReference<>(view);
        }
    }

    public boolean isAttached() {
        return mReference != null && mReference.get() != null;
    }

    public void request(Object request) {
        NetworkAdapter adapter = AppDelegate.getInstance().getNetworkAdapter();
        if (adapter != null) {
            adapter.add(this, request);
        }
    }

    public void onDestroy() {
        NetworkAdapter adapter = AppDelegate.getInstance().getNetworkAdapter();
        if (adapter != null) {
            adapter.cancel(this);
        }
        if (mReference != null) {
            mReference.clear();
            mReference = null;
        }
    }
}
