package com.clock.album.presenter.entity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * 扫描图片的结果
 * <p/>
 * Created by Clock on 2016/3/21.
 */
public class ImageScanResult {

    /**
     * 系统所有有图片的文件夹
     */
    private ArrayList<File> albumFolderList;
    /**
     * 每个有图片文件夹下面所包含的图片
     */
    private HashMap<String, ArrayList<File>> albumImageListMap;

    /**
     * 获取手机上所有有图片的目录
     *
     * @return
     */
    public ArrayList<File> getAlbumFolderList() {
        return albumFolderList;
    }

    public void setAlbumFolderList(ArrayList<File> albumFolderList) {
        this.albumFolderList = albumFolderList;
    }

    /**
     * 获取手机上所有图片目录下包含的图片
     *
     * @return 一个Map，key是图片目录路径，value是对应目录下包含的所有图片文件
     */
    public HashMap<String, ArrayList<File>> getAlbumImageListMap() {
        return albumImageListMap;
    }

    public void setAlbumImageListMap(HashMap<String, ArrayList<File>> albumImageListMap) {
        this.albumImageListMap = albumImageListMap;
    }

}
