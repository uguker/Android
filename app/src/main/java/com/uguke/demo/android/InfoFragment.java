package com.uguke.demo.android;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.allen.library.SuperTextView;
import com.uguke.android.app.SupportFragment;

import butterknife.BindArray;
import butterknife.BindViews;

/**
 * 功能描述：
 *
 * @author LeiJue
 */
public class InfoFragment extends SupportFragment {

    @BindViews({R.id.setting, R.id.about})
    SuperTextView [] tvs;

    @BindArray(R.array.about)
    String [] names;

    @Override
    public void onCreating(@Nullable Bundle savedInstanceState) {
        super.onCreating(savedInstanceState);
        setContentView(R.layout.fragment_info);
    }


    //    @Override
//    public void onInit(Bundle savedInstanceState) {
//        super.onInit(savedInstanceState);
//        setContentView(R.layout.fragment_info);
//        hideToolbar();
//
//        for(int i = 0; i < tvs.length; i++) {
//            tvs[i].setLeftString(names[i]);
//        }
//
//
//
//        //Log.e("数据", AppUtils.getVersionName() + AppUtils.getVersionCode());
//
//       // IntentUtils.sendSms(mActivity, "17358421732", "测试短信");
//    }
}
