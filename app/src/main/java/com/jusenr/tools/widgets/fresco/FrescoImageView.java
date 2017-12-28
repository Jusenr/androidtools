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

package com.jusenr.tools.widgets.fresco;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

/**
 * Created by riven_chris on 2017/4/2.
 */

public class FrescoImageView extends SimpleDraweeView {

    private ResizeOptions resizeOptions;
    private Context context;

    public FrescoImageView(Context context) {
        this(context, null);
    }

    public FrescoImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FrescoImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
    }

    public FrescoImageView resize(int width, int height) {
        if (width == 0 || height == 0) {
            resizeOptions = null;
            return this;
        }
        resizeOptions = new ResizeOptions(width, height);
        return this;
    }

    public void setScaleType(ScalingUtils.ScaleType scaleType) {
        GenericDraweeHierarchy hierarchy = getHierarchy();
        hierarchy.setActualImageScaleType(scaleType);
        setHierarchy(hierarchy);
    }

    public void setDefaultImage(Bitmap bitmap) {
        GenericDraweeHierarchy hierarchy = getHierarchy();
        hierarchy.setPlaceholderImage(new BitmapDrawable(bitmap), ScalingUtils.ScaleType.FOCUS_CROP);
        setHierarchy(hierarchy);
    }

    public void setDefaultImage(Drawable drawable) {
        GenericDraweeHierarchy hierarchy = getHierarchy();
        hierarchy.setPlaceholderImage(drawable, ScalingUtils.ScaleType.FOCUS_CROP);
        setHierarchy(hierarchy);
    }

    public void setImageURL(@NonNull String url) {
        if (TextUtils.isEmpty(url))
            throw new IllegalArgumentException("url can not be null");
        setImageURI(Uri.parse(url));
    }

    public void setImageRes(int res) {
        setImageURL("res://putao/" + res);
    }

    public void setImageFile(String filePath) {
        setImageURL("file://" + filePath);
    }

    /**
     * @param filePath xxx/xxx/x.jpg
     */
    public void setImageAssetsRes(String filePath) {
        setImageURL("asset:///" + filePath);
    }

    @Override
    public void setImageURI(Uri uri) {
        ImageRequestBuilder imageRequestBuilder = ImageRequestBuilder.newBuilderWithSource(uri)
                .setAutoRotateEnabled(true)
                .setLocalThumbnailPreviewsEnabled(true)
                .setResizeOptions(resizeOptions);
        ImageRequest request = imageRequestBuilder.build();
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setAutoPlayAnimations(true)
                .setImageRequest(request)
                .setTapToRetryEnabled(false)//加载失败时点击重新加载
                .setOldController(getController())
                .build();
        setController(controller);
    }
}