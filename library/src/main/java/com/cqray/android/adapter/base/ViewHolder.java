package com.cqray.android.adapter.base;

import android.graphics.Typeface;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.annotation.StringRes;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author LeiJue
 */
public class ViewHolder extends RecyclerView.ViewHolder {

    public View itemView;

    public ViewHolder(View view) {
        super(view);
        itemView = view;
    }

    public ViewHolder setText(@IdRes int id, CharSequence text) {
        View view = findViewById(id);
        if (view instanceof TextView) {
            ((TextView) view).setText(text);
        }
        return this;
    }

    public ViewHolder setText(@IdRes int id, @StringRes int resId) {
        View view = findViewById(id);
        if (view instanceof TextView) {
            ((TextView) view).setText(resId);
        }
        return this;
    }

    public ViewHolder setTextColor(@IdRes int id, int color) {
        View view = findViewById(id);
        if (view instanceof TextView) {
            ((TextView) view).setTextColor(color);
        }
        return this;
    }

    public ViewHolder setTextSize(@IdRes int id, float size) {
        View view = findViewById(id);
        if (view instanceof TextView) {
            ((TextView) view).setTextSize(size);
        }
        return this;
    }

    public ViewHolder setTextBold(@IdRes int id, boolean bold) {
        View view = findViewById(id);
        if (view instanceof TextView) {
            ((TextView) view).setTypeface(Typeface.defaultFromStyle(bold ? Typeface.BOLD : Typeface.NORMAL));
        }
        return this;
    }

    public ViewHolder setVisible(@IdRes int id, boolean visible) {
        View view = findViewById(id);
        if (view != null) {
            view.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
        }
        return this;
    }

    public ViewHolder setGone(@IdRes int id, boolean visible) {
        View view = findViewById(id);
        if (view != null) {
            view.setVisibility(visible ? View.VISIBLE : View.GONE);
        }
        return this;
    }

    public ViewHolder setParentVisible(@IdRes int id, boolean visible) {
        View view = findViewById(id);
        if (view != null && view.getParent() != null) {
            ((View) view.getParent()).setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
        }
        return this;
    }

    public ViewHolder setParentGone(@IdRes int id, boolean visible) {
        View view = findViewById(id);
        if (view != null && view.getParent() != null) {
            ((View) view.getParent()).setVisibility(visible ? View.VISIBLE : View.GONE);
        }
        return this;
    }

    public ViewHolder setEnable(@IdRes int id, boolean enable) {
        View view = findViewById(id);
        if (view != null) {
            view.setEnabled(enable);
        }
        return this;
    }

//    public <T extends View> T getView(int viewId) {
//        return super.getView(viewId);
//    }

    public <T extends View> T findViewById(@IdRes int id) {
        if (itemView != null) {
            return itemView.findViewById(id);
        }
        return null;
    }


}
