package com.clock.album.view.entity;

import com.clock.album.entity.AlbumFolderInfo;

import java.util.List;

/**
 * 相册界面需要的数据
 * <p/>
 * Created by Clock on 2016/3/21.
 */
public class AlbumViewData {

    /**
     * 图片总数
     */
    //private int imageTotal;
    /**
     * 所有图片的信息列表（图片目录的绝对路径作为map的key，value是该图片目录下的所有图片文件信息）
     */
    //private Map<String, ArrayList<ImageInfo>> albumImageInfoListMap;
    /**
     * 所有有图片的目录信息
     */
    //private List<AlbumInfo> albumInfoList;
    /**
     * 相册目录列表
     */
    private List<AlbumFolderInfo> albumFolderInfoList;

    /*public Map<String, ArrayList<ImageInfo>> getAlbumImageInfoListMap() {
        return albumImageInfoListMap;
    }

    public void setAlbumImageInfoListMap(Map<String, ArrayList<ImageInfo>> albumImageInfoListMap) {
        this.albumImageInfoListMap = albumImageInfoListMap;
    }

    public List<AlbumInfo> getAlbumInfoList() {
        return albumInfoList;
    }

    public void setAlbumInfoList(List<AlbumInfo> albumInfoList) {
        this.albumInfoList = albumInfoList;
    }

    public int getImageTotal() {
        return imageTotal;
    }

    public void setImageTotal(int imageTotal) {
        this.imageTotal = imageTotal;
    }*/

    public List<AlbumFolderInfo> getAlbumFolderInfoList() {
        return albumFolderInfoList;
    }

    public void setAlbumFolderInfoList(List<AlbumFolderInfo> albumFolderInfoList) {
        this.albumFolderInfoList = albumFolderInfoList;
    }
}
