package com.cqray.android.adapter;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.cqray.android.R;
import com.cqray.android.helper.TipsHelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 使用的是SnackBar来显示消息
 * @author LeiJue
 */
public class DefaultTipsAdapter implements TipsHelper.Adapter {

    /** Snack变化文本 **/
    private static final String [] SNACK_ANIMATION_TEXTS = {" ", " .", " ..", " ..."};
    /** 文本长度超过25算是长文本，则延长显示时间 **/
    private static final int LONG_TEXT_LENGTH = 25;
    /** 计数器 **/
    private Map<Object, Integer> mCountMap = new HashMap<>(1);

    public DefaultTipsAdapter() {}

    @Override
    public Object show(final TipsHelper helper) {

        if (helper.getDuration() == TipsHelper.DURATION_SHORT &&
                helper.getText().length() > LONG_TEXT_LENGTH) {
            // 长文本显示时间不够，所以变更显示时间
            helper.setDuration(TipsHelper.DURATION_LONG);
        }

        Snackbar snackbar = Snackbar.make(helper.getView(), helper.getText(), helper.getDuration())
                .addCallback(new Snackbar.Callback() {
                    @Override
                    public void onDismissed(Snackbar bar, int event) {
                        List<TipsHelper.OnDismissListener> listeners = helper.getOnDismissListeners();
                        for (TipsHelper.OnDismissListener listener : listeners) {
                            if (listener != null) {
                                listener.onDismiss(helper, event);
                            }
                        }
                        helper.stopChanging();
                    }

                    @Override
                    public void onShown(Snackbar sb) {
                        List<TipsHelper.OnShowListener> listeners = helper.getOnShowListeners();
                        for (TipsHelper.OnShowListener listener : listeners) {
                            if (listener != null) {
                                listener.onShow(helper, null);
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
        text.setMaxLines(helper.getMaxLines());
        action.setTextSize(helper.getActionTextSize());
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
        // 将计数器添加到Map
        mCountMap.put(snackbar, 0);
        return snackbar;
    }

    @Override
    public void hide(Object obj) {
        ((Snackbar) obj).dismiss();
        // 移除计数器
        mCountMap.remove(obj);
    }

    @Override
    public void changed(TipsHelper helper, Object obj) {
        // 获取计数器
        Integer temp = mCountMap.get(obj);
        int count = temp == null ? 0 : temp;
        String animationText = SNACK_ANIMATION_TEXTS[count % SNACK_ANIMATION_TEXTS.length];
        ((Snackbar) obj).setText(helper.getText() + animationText);
        count ++;
        if (count >= SNACK_ANIMATION_TEXTS.length) {
            count = 0;
        }
        // 缓存计数器
        mCountMap.put(obj, count);
    }
}
