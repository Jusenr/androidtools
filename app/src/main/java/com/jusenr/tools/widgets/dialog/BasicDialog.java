package com.jusenr.tools.widgets.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;

import butterknife.ButterKnife;

/**
 * Description:
 * Copyright  : Copyright (c) 2016
 * Email      : jusenr@163.com
 * Author     : Jusenr
 * Date       : 2016/10/26 18:39.
 */

public abstract class BasicDialog extends Dialog {
    protected Context mContext;
    protected View mRootView;
    protected boolean cancel = false;

    /**
     * Set Layout
     *
     * @return Layout id
     */
    protected abstract int getLayoutId();

    public BasicDialog(Context context) {
        this(context, 0);
    }

    public BasicDialog(Context context, int themeResId) {
        super(context, themeResId);
        mContext = context;
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        int layoutId = getLayoutId();
        if (layoutId == 0)
            throw new RuntimeException("Can not find Layout resources, Fragment failed to initialize!");
        mRootView = LayoutInflater.from(context).inflate(getLayoutId(), null);
        setContentView(mRootView);//Set Layout
        ButterKnife.bind(this, mRootView);

    }

    @Override
    public void setCanceledOnTouchOutside(boolean cancel) {
        super.setCanceledOnTouchOutside(cancel);
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }
}
