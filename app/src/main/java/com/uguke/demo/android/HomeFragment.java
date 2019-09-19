package com.uguke.demo.android;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;

import com.uguke.android.app.BaseTabbedFragment;
import com.uguke.android.app.FragmentTab;

import butterknife.BindView;
import butterknife.Unbinder;

public class HomeFragment extends BaseTabbedFragment {

    @BindView(R.id.content)
    FrameLayout mContent;
    Unbinder unbinder;

    @Override
    public void onCreating(@Nullable Bundle savedInstanceState) {

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadMultipleRootFragment(new FragmentTab("真实", MineFragment.class));
        setSwipeBackEnable(true);
    }

//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        // TODO: inflate a fragment view
//        View rootView = super.onCreateView(inflater, container, savedInstanceState);
//        unbinder = ButterKnife.bind(this, rootView);
//        return rootView;
//    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
