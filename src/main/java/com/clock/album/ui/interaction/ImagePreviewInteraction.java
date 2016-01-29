package com.clock.album.ui.interaction;

import com.clock.album.entity.ImageInfo;

/**
 * 图片预览交互接口
 * <p/>
 * Created by Clock on 2016/1/29.
 */
public interface ImagePreviewInteraction {

    /**
     * 预览图片
     *
     * @param imageInfo
     */
    public void previewImage(ImageInfo imageInfo);
}
