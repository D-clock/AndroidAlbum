package com.clock.album.entity;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 图片信息
 * <p/>
 * Created by Clock on 2016/1/26.
 */
public class ImageInfo implements Serializable {

    private static final long serialVersionUID = -3753345306395582567L;
    /**
     * 图片文件
     */
    private File imageFile;
    /**
     * 是否被选中
     */
    private boolean isSelected = false;

    public File getImageFile() {
        return imageFile;
    }

    public void setImageFile(File imageFile) {
        this.imageFile = imageFile;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ImageInfo imageInfo = (ImageInfo) o;

        if (isSelected() != imageInfo.isSelected()) return false;
        return getImageFile().equals(imageInfo.getImageFile());

    }

    @Override
    public int hashCode() {
        int result = getImageFile().hashCode();
        result = 31 * result + (isSelected() ? 1 : 0);
        return result;
    }

    /**
     * @param imageFileList
     * @return
     */
    public static List<ImageInfo> buildFromFileList(List<File> imageFileList) {
        if (imageFileList != null) {
            List<ImageInfo> imageInfoArrayList = new ArrayList<>();
            for (File imageFile : imageFileList) {
                ImageInfo imageInfo = new ImageInfo();
                imageInfo.imageFile = imageFile;
                imageInfo.isSelected = false;
                imageInfoArrayList.add(imageInfo);
            }
            return imageInfoArrayList;
        } else {
            return null;
        }
    }
}
