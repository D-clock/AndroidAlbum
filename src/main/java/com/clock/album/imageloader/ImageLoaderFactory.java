package com.clock.album.imageloader;

/**
 * ImageLoader工厂类
 * <p/>
 * Created by Clock on 2016/1/18.
 */
public class ImageLoaderFactory {

    /**
     * <link>https://github.com/nostra13/Android-Universal-Image-Loader</link>
     */
    public final static int UNIVERSAL_ANDROID_IMAGE_LOADER = 1;

    private ImageLoaderFactory() {

    }

    /**
     * 获取图片加载器
     *
     * @param loaderType
     * @return
     */
    public static ImageLoaderWrapper getLoader(int loaderType) {
        if (UNIVERSAL_ANDROID_IMAGE_LOADER == loaderType) {
            return new UniversalAndroidImageLoader();
        } else {
            return new UniversalAndroidImageLoader();
        }
    }
}
