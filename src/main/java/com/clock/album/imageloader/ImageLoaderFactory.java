package com.clock.album.imageloader;

/**
 * ImageLoader工厂类
 * <p/>
 * Created by Clock on 2016/1/18.
 */
public class ImageLoaderFactory {


    private ImageLoaderFactory() {

    }

    /**
     * 获取图片加载器
     *
     * @return
     */
    public static ImageLoaderWrapper getLoader() {
        return new UniversalAndroidImageLoader();//<link>https://github.com/nostra13/Android-Universal-Image-Loader</link>
    }
}
