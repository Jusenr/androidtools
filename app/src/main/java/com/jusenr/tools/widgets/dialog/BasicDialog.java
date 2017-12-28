/*
 * Copyright 2017 androidtools Jusenr
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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