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

import com.facebook.cache.disk.DiskCacheConfig;
import com.facebook.common.internal.Supplier;
import com.facebook.common.util.ByteConstants;
import com.facebook.imagepipeline.backends.okhttp3.OkHttpImagePipelineConfigFactory;
import com.facebook.imagepipeline.cache.MemoryCacheParams;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.decoder.SimpleProgressiveJpegConfig;

import java.io.File;

import okhttp3.OkHttpClient;

/**
 * Created by riven_chris on 2017/4/2.
 */

public class ImagePipelineFactory {

    static ImagePipelineConfig sOkHttpImagePipelineConfig;

    public static ImagePipelineConfig imagePipelineConfig(Context context, OkHttpClient okHttpClient, String cachePath) {
        if (sOkHttpImagePipelineConfig == null)
            sOkHttpImagePipelineConfig = configureCaches(context, okHttpClient, cachePath);
        return sOkHttpImagePipelineConfig;
    }

    /**
     * ImagePipeline配置
     *
     * @param context      context
     * @param okHttpClient okHttp客户端
     * @return ImagePipeline配置实例
     */
    private static ImagePipelineConfig configureCaches(Context context, OkHttpClient okHttpClient, String cachePath) {
        //内存配置
        final MemoryCacheParams bitmapCacheParams = new MemoryCacheParams(
                ConfigConstants.MAX_MEMORY_CACHE_SIZE,// 内存缓存中总图片的最大大小,以字节为单位
                Integer.MAX_VALUE,// 内存缓存中图片的最大数量
                ConfigConstants.MAX_MEMORY_CACHE_SIZE,// 内存缓存中准备清除但尚未被删除的总图片的最大大小,以字节为单位
                Integer.MAX_VALUE,// 内存缓存中准备清除的总图片的最大数量
                Integer.MAX_VALUE);// 内存缓存中单个图片的最大大小
        //修改内存图片缓存数量,空间策略
        Supplier<MemoryCacheParams> mSupplierMemoryCacheParams = new Supplier<MemoryCacheParams>() {
            @Override
            public MemoryCacheParams get() {
                return bitmapCacheParams;
            }
        };
//        String cachePath = Environment.getExternalStorageDirectory().getAbsolutePath();//获得存储路径(SDcard)
        String file = "fresco";
        //小图片的磁盘配置
        DiskCacheConfig diskSmallCacheConfig = DiskCacheConfig.newBuilder(context)
                .setBaseDirectoryPath(new File(cachePath))//缓存图片基路径
                .setBaseDirectoryName(ConfigConstants.IMAGE_PIPELINE_SMALL_CACHE_DIR)//文件夹名
//            .setCacheErrorLogger(cacheErrorLogger)//日志记录器用于日志错误的缓存。
//            .setCacheEventListener(cacheEventListener)//缓存事件侦听器。
//            .setDiskTrimmableRegistry(diskTrimmableRegistry)//类将包含一个注册表的缓存减少磁盘空间的环境。
                .setMaxCacheSize(ConfigConstants.MAX_DISK_CACHE_SIZE)//默认缓存的最大大小。
                .setMaxCacheSizeOnLowDiskSpace(ConfigConstants.MAX_SMALL_DISK_CACHE_LOW_SIZE)//缓存的最大大小,使用设备时低磁盘空间。
                .setMaxCacheSizeOnVeryLowDiskSpace(ConfigConstants.MAX_SMALL_DISK_CACHE_VERY_LOW_SIZE)//缓存的最大大小,当设备极低磁盘空间
//            .setVersion(version)
                .build();
        //默认图片的磁盘配置
        DiskCacheConfig diskCacheConfig = DiskCacheConfig.newBuilder(context)
                .setBaseDirectoryPath(new File(cachePath))//缓存图片基路径
                .setBaseDirectoryName(ConfigConstants.IMAGE_PIPELINE_CACHE_DIR)//文件夹名
//            .setCacheErrorLogger(cacheErrorLogger)//日志记录器用于日志错误的缓存。
//            .setCacheEventListener(cacheEventListener)//缓存事件侦听器。
//            .setDiskTrimmableRegistry(diskTrimmableRegistry)//类将包含一个注册表的缓存减少磁盘空间的环境。
                .setMaxCacheSize(ConfigConstants.MAX_DISK_CACHE_SIZE)//默认缓存的最大大小。
                .setMaxCacheSizeOnLowDiskSpace(ConfigConstants.MAX_DISK_CACHE_LOW_SIZE)//缓存的最大大小,使用设备时低磁盘空间。
                .setMaxCacheSizeOnVeryLowDiskSpace(ConfigConstants.MAX_DISK_CACHE_VERY_LOW_SIZE)//缓存的最大大小,当设备极低磁盘空间
//            .setVersion(version)
                .build();
        //缓存图片配置
        ImagePipelineConfig.Builder configBuilder = null;
        if (okHttpClient != null)
            configBuilder = OkHttpImagePipelineConfigFactory.newBuilder(context, okHttpClient);
        else
            configBuilder = ImagePipelineConfig.newBuilder(context);
//            .setAnimatedImageFactory(AnimatedImageFactory animatedImageFactory)//图片加载动画
        configBuilder.setBitmapMemoryCacheParamsSupplier(mSupplierMemoryCacheParams)//内存缓存配置(一级缓存，已解码的图片)
//            .setCacheKeyFactory(cacheKeyFactory)//缓存Key工厂
//            .setEncodedMemoryCacheParamsSupplier(encodedCacheParamsSupplier)//内存缓存和未解码的内存缓存的配置(二级缓存)
//            .setExecutorSupplier(executorSupplier)//线程池配置
//            .setImageCacheStatsTracker(imageCacheStatsTracker)//统计缓存的命中率
//            .setImageDecoder(ImageDecoder imageDecoder) //图片解码器配置
//            .setIsPrefetchEnabledSupplier(Supplier<Boolean> isPrefetchEnabledSupplier)//图片预览(缩略图，预加载图等)预加载到文件缓存
                .setMainDiskCacheConfig(diskCacheConfig)//磁盘缓存配置(总，三级缓存)
//            .setMemoryTrimmableRegistry(memoryTrimmableRegistry) //内存用量的缩减,有时我们可能会想缩小内存用量。比如应用中有其他数据需要占用内存，不得不把图片缓存清除或者减小 或者我们想检查看看手机是否已经内存不够了。
//            .setNetworkFetchProducer(networkFetchProducer)//自定的网络层配置：如OkHttp，Volley
//            .setPoolFactory(poolFactory)//线程池工厂配置
                .setProgressiveJpegConfig(new SimpleProgressiveJpegConfig())//渐进式JPEG图
//            .setRequestListeners(requestListeners)//图片请求监听
//            .setResizeAndRotateEnabledForNetwork(boolean resizeAndRotateEnabledForNetwork)//调整和旋转是否支持网络图片
                .setSmallImageDiskCacheConfig(diskSmallCacheConfig);//磁盘缓存配置(小图片，可选～三级缓存的小图优化缓存)
        return configBuilder.build();
    }

    /**
     * 配置常量
     */
    private static class ConfigConstants {
        private static final int MAX_HEAP_SIZE = (int) Runtime.getRuntime().maxMemory();//分配内存
        public static final int MAX_MEMORY_CACHE_SIZE = MAX_HEAP_SIZE / 4;//使用的缓存数量

        public static final int MAX_SMALL_DISK_CACHE_VERY_LOW_SIZE = 5 * ByteConstants.MB;//小图极低磁盘空间缓存的最大值(特性：可将大量的小图放到额外放在另一个磁盘空间防止大图占用磁盘空间而删除了大量的小图)
        public static final int MAX_SMALL_DISK_CACHE_LOW_SIZE = 10 * ByteConstants.MB;//小图低磁盘空间缓存的最大值(特性：可将大量的小图放到额外放在另一个磁盘空间防止大图占用磁盘空间而删除了大量的小图)
        public static final int MAX_SMALL_DISK_CACHE_SIZE = 20 * ByteConstants.MB;//小图磁盘缓存的最大值(特性：可将大量的小图放到额外放在另一个磁盘空间防止大图占用磁盘空间而删除了大量的小图)

        public static final int MAX_DISK_CACHE_VERY_LOW_SIZE = 10 * ByteConstants.MB;//默认图极低磁盘空间缓存的最大值
        public static final int MAX_DISK_CACHE_LOW_SIZE = 30 * ByteConstants.MB;//默认图低磁盘空间缓存的最大值
        public static final int MAX_DISK_CACHE_SIZE = 50 * ByteConstants.MB;//默认图磁盘缓存的最大值

        public static final String IMAGE_PIPELINE_SMALL_CACHE_DIR = "image_small";//小图所放路径的文件夹名
        public static final String IMAGE_PIPELINE_CACHE_DIR = "image";//默认图所放路径的文件夹名
    }
}