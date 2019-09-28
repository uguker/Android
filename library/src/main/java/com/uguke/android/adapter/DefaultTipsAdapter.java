package com.uguke.android.adapter;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.uguke.android.R;
import com.uguke.android.helper.TipsHelper;
import com.uguke.android.listener.OnDismissListener;

import java.util.List;

/**
 * 使用的是SnackBar来显示消息
 * @author LeiJue
 */
public class DefaultTipsAdapter implements TipsHelper.Adapter {

    /** Snack变化文本 **/
    private static final String [] SNACK_ANIMATION_TEXTS = {" ", " .", " ..", " ..."};

    private int mCount = 0;

    public DefaultTipsAdapter() {

    }


    @Override
    public Object show(final TipsHelper helper) {
        Snackbar snackbar = Snackbar.make(helper.getView(), helper.getText(), helper.getDuration())
                .addCallback(new Snackbar.Callback() {
                    @Override
                    public void onDismissed(Snackbar bar, int event) {
                        List<OnDismissListener<TipsHelper>> listeners = helper.getDismissedListeners();
                        for (OnDismissListener<TipsHelper> listener : listeners) {
                            if (listener != null) {
                                listener.onDismiss(helper, event);
                            }
                        }
                        helper.stopChange();
                    }

                    @Override
                    public void onShown(Snackbar sb) {
//                        List<OnShowListener> listeners = helper.getShownListeners();
//                        for (OnShowListener listener : listeners) {
//                            if (listener != null) {
//                                listener.onShow(helper);
//                            }
//                        }
                    }
                })
                .setTextColor(helper.getTextColor())
                .setActionTextColor(helper.getActionTextColor())
                .setBackgroundTintList(helper.getBackgroundTintList());

        TextView text = snackbar.getView().findViewById(R.id.snackbar_text);
        TextView action = snackbar.getView().findViewById(R.id.snackbar_action);
        text.setTextSize(helper.getTextSize());
        action.setTextSize(helper.getActionTextSize());
//                .setActionTextColor(helper.getActionColor())
//                .setTextColor(helper.getTextColor());
        if (!TextUtils.isEmpty(helper.getActionText()) &&
                helper.getOnActionListener() != null) {
            snackbar.setAction(helper.getActionText(), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    helper.getOnActionListener().onClick(v);
                }
            });
        }
        snackbar.show();
        return snackbar;
    }

    @Override
    public void hide(Object obj) {
        ((Snackbar) obj).dismiss();
    }

    @Override
    public void changed(TipsHelper helper, Object obj) {
        String animationText = SNACK_ANIMATION_TEXTS[mCount % SNACK_ANIMATION_TEXTS.length];
        ((Snackbar) obj).setText(helper.getText() + animationText);
        mCount ++;
        if (mCount >= SNACK_ANIMATION_TEXTS.length) {
            mCount = 0;
        }
    }
}
