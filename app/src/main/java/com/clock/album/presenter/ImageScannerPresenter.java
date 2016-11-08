package com.clock.album.presenter;

import android.content.Context;
import android.support.v4.app.LoaderManager;

/**
 * 图片扫描Presenter层
 * <p/>
 * Created by Clock on 2016/3/19.
 */
public interface ImageScannerPresenter {

    /**
     * 扫描获取图片文件夹列表
     *
     * @param context
     * @param loaderManager 获取系统图片的LoaderManager
     */
    public void startScanImage(Context context, LoaderManager loaderManager);

}
