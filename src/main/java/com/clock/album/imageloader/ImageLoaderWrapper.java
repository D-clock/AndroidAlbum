package com.clock.album.imageloader;

import android.widget.ImageView;

import java.io.File;

/**
 * 图片加载功能接口
 * <p/>
 * Created by Clock on 2016/1/18.
 */
public interface ImageLoaderWrapper {

    /**
     * 显示 图片
     *
     * @param imageView 显示图片的ImageView
     * @param imageFile 图片文件
     * @param option    显示参数设置
     */
    public void displayImage(ImageView imageView, File imageFile, DisplayOption option);

    /**
     * 显示图片
     *
     * @param imageView 显示图片的ImageView
     * @param imageUrl  图片资源的URL
     * @param option    显示参数设置
     */
    public void displayImage(ImageView imageView, String imageUrl, DisplayOption option);

    /**
     * 图片加载参数
     */
    public static class DisplayOption {
        /**
         * 加载中的资源id
         */
        public int loadingResId;
        /**
         * 加载失败的资源id
         */
        public int loadErrorResId;
    }
}
