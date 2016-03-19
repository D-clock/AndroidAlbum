package com.clock.album.model;

import android.support.v4.app.LoaderManager;

import com.clock.album.entity.AlbumInfo;

import java.util.ArrayList;
import java.util.Map;

/**
 * 图片扫描Model层接口
 * <p/>
 * Created by Clock on 2016/3/19.
 */
public interface ImageScannerModel {

    /**
     * 获取所有图片的信息列表（图片目录的绝对路径作为map的key，value是该图片目录下的所有图片文件信息）
     *
     * @param loaderManager
     * @return
     */
    public Map<String, ArrayList<AlbumInfo>> getAlbumImageInfo(LoaderManager loaderManager);


}
