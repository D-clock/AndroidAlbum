package com.clock.album.ui.interaction;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 图片扫描UI层交互接口
 * <p/>
 * Created by Clock on 2016/1/20.
 */
public interface ImageScannerInteraction {

    /**
     * 刷新照片信息
     *
     * @param albumFolderList   所有包含图片的目录
     * @param albumImageListMap 相册图片列表信息，key是图片目录的绝对路径，value是目录下所有图片的路径
     */
    public void refreshImageInfo(List<File> albumFolderList, Map<String, ArrayList<File>> albumImageListMap);
}
