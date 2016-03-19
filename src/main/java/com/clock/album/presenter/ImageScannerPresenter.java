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
     * 扫描获取
     *
     * @param albumFolderView
     * @param loaderManager
     */
    public void scanImageFolder(AlbumFolderView albumFolderView, LoaderManager loaderManager);
}
