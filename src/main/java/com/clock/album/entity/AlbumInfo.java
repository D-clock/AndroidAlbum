package com.clock.album.entity;

import java.io.File;
import java.io.Serializable;

/**
 * 相册信息
 * <p/>
 * Created by Clock on 2016/1/18.
 */
public class AlbumInfo implements Serializable {

    private static final long serialVersionUID = 4333403539604360976L;
    /**
     * 目录
     */
    private File folder;
    /**
     * 封面，相册的第一张图片
     */
    private File frontCover;
    /**
     * 文件个数
     */
    private int fileCount;

    public int getFileCount() {
        return fileCount;
    }

    public void setFileCount(int fileCount) {
        this.fileCount = fileCount;
    }

    public File getFolder() {
        return folder;
    }

    public void setFolder(File folder) {
        this.folder = folder;
    }

    public File getFrontCover() {
        return frontCover;
    }

    public void setFrontCover(File frontCover) {
        this.frontCover = frontCover;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AlbumInfo albumInfo = (AlbumInfo) o;

        if (getFileCount() != albumInfo.getFileCount()) return false;
        if (!getFolder().equals(albumInfo.getFolder())) return false;
        return getFrontCover().equals(albumInfo.getFrontCover());

    }

    @Override
    public int hashCode() {
        int result = getFolder().hashCode();
        result = 31 * result + getFrontCover().hashCode();
        result = 31 * result + getFileCount();
        return result;
    }
}
