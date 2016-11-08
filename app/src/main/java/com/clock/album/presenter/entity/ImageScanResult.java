package com.clock.album.presenter.entity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 扫描图片的结果
 * <p/>
 * Created by Clock on 2016/3/21.
 */
public class ImageScanResult {

    /**
     * 系统所有有图片的文件夹
     */
    private List<File> albumFolderList;
    /**
     * 每个有图片文件夹下面所包含的图片
     */
    private Map<String, ArrayList<File>> albumImageListMap;

    /**
     * 获取手机上所有有图片的目录
     *
     * @return
     */
    public List<File> getAlbumFolderList() {
        return albumFolderList;
    }

    public void setAlbumFolderList(List<File> albumFolderList) {
        this.albumFolderList = albumFolderList;
    }

    /**
     * 获取手机上所有图片目录下包含的图片
     *
     * @return 一个Map，key是图片目录路径，value是对应目录下包含的所有图片文件
     */
    public Map<String, ArrayList<File>> getAlbumImageListMap() {
        return albumImageListMap;
    }

    public void setAlbumImageListMap(Map<String, ArrayList<File>> albumImageListMap) {
        this.albumImageListMap = albumImageListMap;
    }

}
