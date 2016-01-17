package com.clock.album.entity;

import java.io.File;
import java.io.Serializable;

/**
 * 相册图片信息
 * <p/>
 * Created by Clock on 2016/1/17.
 */
public class ImageInfo implements Serializable {

    private static final long serialVersionUID = 7280675987071586126L;

    private File file;

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ImageInfo imageInfo = (ImageInfo) o;

        return getFile().equals(imageInfo.getFile());

    }

    @Override
    public int hashCode() {
        return getFile().hashCode();
    }
}
