package com.uguke.android.adapter;

import com.uguke.android.app.SupportActivity;
import com.uguke.android.app.SupportFragment;

public class TipsAdapter implements Cloneable {

    private SupportActivity mActivity;
    private SupportFragment mFragment;

    public TipsAdapter(SupportActivity activity) {
        mActivity = activity;
    }

    public TipsAdapter(SupportFragment fragment) {

    }

    public TipsAdapter copy() {
        try {
            return (TipsAdapter) clone();
        } catch (CloneNotSupportedException e) {
            //e.printStackTrace();
            return null;
        }
        //return null;
    }
}
