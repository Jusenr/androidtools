package com.jusenr.tools.widgets.dialog;

import android.content.Context;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jusenr.tools.R;
import com.jusenr.tools.widgets.fresco.FrescoImageView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Description:
 * Copyright  : Copyright (c) 2016
 * Email      : jusenr@163.com
 * Author     : Jusenr
 * Date       : 2016/10/26 18:14.
 */
public class SelectDialog extends BasicDialog {

    public interface OnNegativeClickListener {
        void onClick(SelectDialog dialog, Button button);
    }

    public interface OnPositiveClickListener {
        void onClick(SelectDialog dialog, Button button);
    }

    @BindView(R.id.tv_message)
    TextView mTvMessage;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.tv_des)
    TextView mTvDesc;
    @BindView(R.id.fiv_icon)
    FrescoImageView mFivIcon;
    @BindView(R.id.tv_icon_desc)
    TextView mTvIconDesc;
    @BindView(R.id.btn_left)
    Button mBtnLeft;
    @BindView(R.id.btn_right)
    Button mBtnRight;

    private OnNegativeClickListener mOnNegativeClickListener;
    private OnPositiveClickListener mOnPositiveClickListener;

    @Override
    protected int getLayoutId() {
        return R.layout.layout_confirmation;
    }

    private SelectDialog(Builder builder) {
        super(builder.mContext, R.style.TranslucentDialogTheme);
        setCancelable(builder.cancelable);
        setCanceledOnTouchOutside(builder.canceledOnTouchOutside);

        String message = builder.mMessage;
        int messageColor = builder.mMessageColor;
        if (!TextUtils.isEmpty(message)) {
            mTvMessage.setVisibility(View.VISIBLE);
            mTvMessage.setText(message);
            if (messageColor != 0) {
                mTvMessage.setTextColor(messageColor);
            }
        }

        String title = builder.mTitle;
        int titleColor = builder.mTitleColor;
        if (!TextUtils.isEmpty(title)) {
            mTvTitle.setVisibility(View.VISIBLE);
            mTvTitle.setText(title);
            if (titleColor != 0) {
                mTvTitle.setTextColor(titleColor);
            }
        }

        String desc = builder.mDesc;
        int descColor = builder.mDescColor;
        if (!TextUtils.isEmpty(desc)) {
            mTvDesc.setVisibility(View.VISIBLE);
            mTvDesc.setText(desc);
            if (descColor != 0) {
                mTvDesc.setTextColor(descColor);
            }
        }

        String iconUrl = builder.mIconUrl;
        if (!TextUtils.isEmpty(iconUrl)) {
            mFivIcon.setVisibility(View.VISIBLE);
            mFivIcon.setImageURL(iconUrl);
        }

        int iconRes = builder.mIconRes;
        if (iconRes != -1) {
            mFivIcon.setVisibility(View.VISIBLE);
            mFivIcon.setImageRes(iconRes);
        }

        String iconDesc = builder.mIconDesc;
        int iconDescColor = builder.mIconDescColor;
        if (!TextUtils.isEmpty(iconDesc)) {
            mTvIconDesc.setVisibility(View.VISIBLE);
            mTvIconDesc.setText(iconDesc);
            if (iconDescColor != 0) {
                mTvIconDesc.setTextColor(iconDescColor);
            }
        }

        if (builder.transparent && getWindow() != null) {
            getWindow().setDimAmount(0);
        }

        String leftButton = builder.mLeftButton;
        int leftButtonColor = builder.mLeftButtonColor;
        int leftButtonBg = builder.mLeftButtonBg;
        if (!TextUtils.isEmpty(leftButton)) {
            mBtnLeft.setVisibility(View.VISIBLE);
            mBtnLeft.setText(leftButton);
            if (leftButtonColor != 0) {
                mBtnLeft.setTextColor(leftButtonColor);
            }
            if (leftButtonBg != 0) {
                mBtnLeft.setBackgroundResource(leftButtonBg);
            }
        }

        String rightButton = builder.mRightButton;
        int rightButtonColor = builder.mRightButtonColor;
        int rightButtonBg = builder.mRightButtonBg;
        if (!TextUtils.isEmpty(rightButton)) {
            mBtnRight.setVisibility(View.VISIBLE);
            mBtnRight.setText(rightButton);
            if (rightButtonColor != 0) {
                mBtnRight.setTextColor(rightButtonColor);
            }
            if (rightButtonBg != 0) {
                mBtnRight.setBackgroundResource(rightButtonBg);
            }
        }

        this.mOnNegativeClickListener = builder.mOnNegativeClickListener;
        this.mOnPositiveClickListener = builder.mOnPositiveClickListener;
    }

    @OnClick({R.id.btn_left, R.id.btn_right})
    public void onViewClicked(View v) {
        switch (v.getId()) {
            case R.id.btn_left:
                if (mOnNegativeClickListener != null) {
                    mOnNegativeClickListener.onClick(this, mBtnLeft);
                }
                break;
            case R.id.btn_right:
                if (mOnPositiveClickListener != null) {
                    mOnPositiveClickListener.onClick(this, mBtnRight);
                }
                break;
        }
    }

    public static class Builder {

        public Builder(Context context) {
            this.mContext = context;
        }

        Context mContext;

        String mMessage;
        String mTitle;
        String mDesc;
        String mIconDesc;

        String mIconUrl;

        String mLeftButton;
        String mRightButton;

        int mMessageColor;
        int mTitleColor;
        int mDescColor;
        int mIconDescColor;

        int mIconRes = -1;

        int mRightButtonBg;
        int mLeftButtonBg;

        int mRightButtonColor;
        int mLeftButtonColor;

        boolean cancelable = true;
        boolean canceledOnTouchOutside = true;
        boolean transparent;

        OnNegativeClickListener mOnNegativeClickListener;
        OnPositiveClickListener mOnPositiveClickListener;

        public Builder setMessage(String message) {
            this.mMessage = message;
            return this;
        }

        public Builder setMessage(@StringRes int resid) {
            this.mMessage = mContext.getResources().getString(resid);
            return this;
        }

        public Builder setTitle(String title) {
            this.mTitle = title;
            return this;
        }

        public Builder setTitle(@StringRes int resid) {
            this.mTitle = mContext.getResources().getString(resid);
            return this;
        }

        public Builder setDesc(String desc) {
            this.mDesc = desc;
            return this;
        }

        public Builder setDesc(@StringRes int resid) {
            this.mDesc = mContext.getResources().getString(resid);
            return this;
        }

        public Builder setIconUrl(String iconUrl) {
            this.mIconUrl = iconUrl;
            return this;
        }

        public Builder setIconUrl(int iconRes) {
            this.mIconRes = iconRes;
            return this;
        }

        public Builder setIconDesc(String iconDesc) {
            this.mIconDesc = iconDesc;
            return this;
        }

        public Builder setIconDesc(@StringRes int resid) {
            this.mIconDesc = mContext.getResources().getString(resid);
            return this;
        }

        public Builder setMessageColor(@ColorInt int color) {
            this.mMessageColor = color;
            return this;
        }

        public Builder setTitleColor(@ColorInt int color) {
            this.mTitleColor = color;
            return this;
        }

        public Builder setDescColor(@ColorInt int color) {
            this.mDescColor = color;
            return this;
        }

        public Builder setIconDescColor(@ColorInt int color) {
            this.mIconDescColor = color;
            return this;
        }

        public Builder setCancelable(boolean cancelable) {
            this.cancelable = cancelable;
            return this;
        }

        public Builder setCanceledOnTouchOutside(boolean canceledOnTouchOutside) {
            this.canceledOnTouchOutside = canceledOnTouchOutside;
            return this;
        }

        public Builder setNegativeButton(String button, OnNegativeClickListener clickListener) {
            setNegativeButton(button, 0, 0, clickListener);
            return this;
        }

        public Builder setNegativeButton(String button,
                                         @DrawableRes int buttonBg,
                                         @ColorInt int color,
                                         OnNegativeClickListener clickListener) {
            this.mLeftButton = button;
            this.mLeftButtonBg = buttonBg;
            this.mLeftButtonColor = color;
            this.mOnNegativeClickListener = clickListener;
            return this;
        }

        public Builder setPositiveButton(String button, OnPositiveClickListener clickListener) {
            setPositiveButton(button, 0, 0, clickListener);
            return this;
        }

        public Builder setPositiveButton(String button,
                                         @DrawableRes int buttonBg,
                                         @ColorInt int color,
                                         OnPositiveClickListener clickListener) {
            this.mRightButton = button;
            this.mRightButtonBg = buttonBg;
            this.mRightButtonColor = color;
            this.mOnPositiveClickListener = clickListener;
            return this;
        }

        public SelectDialog build() {
            return new SelectDialog(this);
        }
    }

    public FrescoImageView getmFivIcon() {
        return mFivIcon;
    }
}
