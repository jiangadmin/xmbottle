package com.wt.piaoliuping;

import android.app.Application;
import android.graphics.Bitmap;

import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

/**
 * Created by wangtao on 2017/10/20.
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        initImageLoader();
    }

    /**
     * 初始化图片加载类
     */
    private void initImageLoader() {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true).cacheOnDisc(true)
                .bitmapConfig(Bitmap.Config.RGB_565)// 防止内存溢出的，图片太多就这这个。还有其他设置
                .cacheOnDisk(true)
//                .cacheInMemory(true)
                // 如Bitmap.Config.ARGB_8888
//                .showImageOnLoading(R.drawable.image_default) // 默认图片
//                .showImageForEmptyUri(R.drawable.image_default) //
//                        // url爲空會显示该图片，自己放在drawable里面的
//                .showImageOnFail(R.drawable.image_default)// 加载失败显示的图片
                .build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                this).memoryCacheExtraOptions(480, 800)
                // 缓存在内存的图片的宽和高度
//                .discCacheExtraOptions(480, 800, CompressFormat.PNG, 70, null)
                // CompressFormat.PNG类型，70质量（0-100）
                .memoryCache(new WeakMemoryCache())
                .memoryCacheSize(2 * 1024 * 1024) // 缓存到内存的最大数据
                .discCacheSize(50 * 1024 * 1024) // 缓存到文件的最大数据
                .discCacheFileCount(1000) // 文件数量
                .defaultDisplayImageOptions(options) // 上面的options对象，一些属性配置
                .build();
        ImageLoader.getInstance().init(config); // 初始化
    }

}