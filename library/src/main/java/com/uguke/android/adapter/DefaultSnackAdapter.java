package com.uguke.android.adapter;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.uguke.android.R;
import com.uguke.android.helper.snack.OnDismissListener;
import com.uguke.android.helper.snack.OnShowListener;
import com.uguke.android.helper.snack.SnackHelper;

import java.util.List;

/**
 * 默认Snack适配器
 * @author LeiJue
 */
public class DefaultSnackAdapter implements SnackAdapter {

    /** Snack变化文本 **/
    private static final String [] SNACK_ANIMATION_TEXTS = {" ", " .", " ..", " ..."};

    private int mCount = 0;

    @Override
    public Object onShow(final SnackHelper helper) {
        Snackbar snackbar = Snackbar.make(helper.getView(), helper.getText(), helper.getDuration())
                .addCallback(new Snackbar.Callback() {
                    @Override
                    public void onDismissed(Snackbar transientBottomBar, int event) {
                        List<OnDismissListener> listeners = helper.getDismissedListeners();
                        for (OnDismissListener listener : listeners) {
                            if (listener != null) {
                                listener.onDismiss(helper, event);
                            }
                        }
                        helper.stopChange();
                    }

                    @Override
                    public void onShown(Snackbar sb) {
                        List<OnShowListener> listeners = helper.getShownListeners();
                        for (OnShowListener listener : listeners) {
                            if (listener != null) {
                                listener.onShow(helper);
                            }
                        }
                    }
                })
                .setTextColor(helper.getTextColor())
                .setActionTextColor(helper.getActionTextColor())
                .setBackgroundTintList(helper.getBackgroundTintList());

        TextView text = snackbar.getView().findViewById(R.id.snackbar_text);
        TextView action = snackbar.getView().findViewById(R.id.snackbar_action);
        text.setTextSize(helper.getTextSize());
        action.setTextSize(helper.getActionTextSize());

        onShow(snackbar);
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
    public void onHide(Object bar) {
        ((Snackbar) bar).dismiss();
    }

    @Override
    public void onChange(SnackHelper helper, Object bar) {
        String animationText = SNACK_ANIMATION_TEXTS[mCount % SNACK_ANIMATION_TEXTS.length];
        ((Snackbar) bar).setText(helper.getText() + animationText);
        mCount ++;
        if (mCount >= SNACK_ANIMATION_TEXTS.length) {
            mCount = 0;
        }
    }

    public void onShow(Snackbar snackbar) {}
}
