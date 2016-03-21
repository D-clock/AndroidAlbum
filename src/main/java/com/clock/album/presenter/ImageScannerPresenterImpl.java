package com.clock.album.presenter;

import android.content.Context;
import android.support.v4.app.LoaderManager;

import com.clock.album.presenter.entity.ImageScanResult;
import com.clock.album.model.ImageScannerModel;
import com.clock.album.model.ImageScannerModelImpl;
import com.clock.album.view.AlbumView;
import com.clock.album.view.entity.AlbumViewData;

/**
 * 图片扫描Presenter实现类
 * <p/>
 * Created by Clock on 2016/3/21.
 */
public class ImageScannerPresenterImpl implements ImageScannerPresenter {

    private ImageScannerModel mScannerModel;
    private AlbumView mAlbumView;

    public ImageScannerPresenterImpl(AlbumView albumView) {
        mScannerModel = new ImageScannerModelImpl();
        mAlbumView = albumView;
    }

    @Override
    public void startScanImage(final Context context, LoaderManager loaderManager) {
        mScannerModel.startScanImage(context, loaderManager, new ImageScannerModel.OnScanImageFinish() {
            @Override
            public void onFinish(ImageScanResult imageScanResult) {
                if (mAlbumView != null) {
                    AlbumViewData albumData = mScannerModel.archiveAlbumInfo(context, imageScanResult);
                    mAlbumView.refreshAlbumData(albumData);
                }
            }
        });
    }

}
