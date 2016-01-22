package com.clock.album.presenter;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import com.clock.album.ui.interaction.ImageScannerInteraction;

/**
 * 图片扫描器
 * <p/>
 * Created by Clock on 2016/1/20.
 */
public class ImageScanner implements ImageScannerPresenter {

    /**
     * Loader的唯一ID号
     */
    private final static int IMAGE_LOADER_ID = 1000;
    /**
     * 加载数据的映射
     */
    private final static String[] IMAGE_PROJECTION = new String[]{
            MediaStore.Images.Media.DATA,//图片路径
            MediaStore.Images.Media.DISPLAY_NAME,//图片文件名，包括后缀名
            MediaStore.Images.Media.TITLE//图片文件名，不包含后缀
    };

    private Context mContext;

    public ImageScanner(Context context) {
        this.mContext = context;
    }

    @Override
    public void scanExternalStorage(LoaderManager loaderManager, ImageScannerInteraction interaction) {
        LoaderManager.LoaderCallbacks<Cursor> loaderCallbacks = createImageLoaderCallbacks(interaction);
        loaderManager.initLoader(IMAGE_LOADER_ID, null, loaderCallbacks);//初始化指定id的Loader
    }

    private LoaderManager.LoaderCallbacks<Cursor> createImageLoaderCallbacks(final ImageScannerInteraction interaction) {

        LoaderManager.LoaderCallbacks<Cursor> imageLoaderCallbacks = new LoaderManager.LoaderCallbacks<Cursor>() {
            @Override
            public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                CursorLoader imageCursorLoader = new CursorLoader(mContext, MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        IMAGE_PROJECTION, null, null, MediaStore.Images.Media.DEFAULT_SORT_ORDER);
                return imageCursorLoader;
            }

            @Override
            public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
                int dataColumnIndex = data.getColumnIndex(MediaStore.Images.Media.DATA);
                int displayNameColumnIndex = data.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME);
                int titleColumnIndex = data.getColumnIndex(MediaStore.Images.Media.TITLE);
            }

            @Override
            public void onLoaderReset(Loader<Cursor> loader) {

            }
        };
        return imageLoaderCallbacks;
    }
}
