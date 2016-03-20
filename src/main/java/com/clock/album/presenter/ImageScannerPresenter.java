package com.clock.album.presenter;

import android.support.v4.app.LoaderManager;

import com.clock.album.view.AlbumFolderView;

/**
 * 图片扫描Presenter层
 * <p/>
 * Created by Clock on 2016/3/19.
 */
public interface ImageScannerPresenter {

    /**
     * 扫描获取图片文件夹列表
     *
     * @param albumFolderView 显示图片文件夹列表的View层接口
     * @param loaderManager   获取系统图片的LoaderManager
     */
    public void scanImageFolder(AlbumFolderView albumFolderView, LoaderManager loaderManager);
}
