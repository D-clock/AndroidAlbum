package com.clock.album.presenter;

import android.support.v4.app.LoaderManager;

import com.clock.album.ui.interaction.ImageScannerInteraction;

/**
 * 图片扫描业务接口
 * <p/>
 * Created by Clock on 2016/1/20.
 */
public interface ImageScannerPresenter {

    /**
     * 扫描SD卡上的图片
     *
     * @param loaderManager 数据加载器
     * @param interaction   扫描界面交互接口
     */
    public void scanExternalStorage(LoaderManager loaderManager, ImageScannerInteraction interaction);
}
