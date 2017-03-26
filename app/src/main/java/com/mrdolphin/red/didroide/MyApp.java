package com.mrdolphin.red.didroide;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LRULimitedMemoryCache;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;

import static com.nostra13.universalimageloader.utils.L.enableLogging;

/**
 * Created by mrdolphin on 02.10.16.
 */

public class MyApp extends Application {
    private static MyApp instance;
    public static final String JSON_EXTRA="p_res";

    // имя файла настроек
    public static final String APP_PREFERENCES = "didroidsettings";
    public static final String APP_PREFERENCES_IP = "ip";

    @Override
    public void onCreate() {
        super.onCreate();
        Log.w("MY", "onCreate MyApp");



        File cacheDir = StorageUtils.getCacheDirectory(getApplicationContext());
        if (cacheDir == null) {
            cacheDir = Environment.getDownloadCacheDirectory();
        }

        int maxWidth = 1024;
        int maxHeight = 1024;
        int threadPoolSize = 8;

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .threadPoolSize(threadPoolSize)
                .threadPriority(Thread.MIN_PRIORITY + 2)
                //.denyCacheImageMultipleSizesInMemory()
                .memoryCacheExtraOptions(maxWidth, maxHeight)
                .memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024))
                .diskCacheExtraOptions(maxWidth, maxHeight, null)
                .diskCache(new UnlimitedDiskCache(cacheDir))
                .diskCacheFileNameGenerator(new HashCodeFileNameGenerator())
                .imageDownloader(new BaseImageDownloader(getApplicationContext()))
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
                .build();

        com.nostra13.universalimageloader.core.ImageLoader.getInstance().init(config);
    }

    public static MyApp getInstance() {
        return instance;
    }
}
