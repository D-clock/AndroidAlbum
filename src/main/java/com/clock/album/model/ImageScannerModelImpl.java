package com.clock.album.model;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;

import com.clock.album.R;
import com.clock.album.entity.AlbumFolderInfo;
import com.clock.album.entity.ImageInfo;
import com.clock.album.presenter.entity.ImageScanResult;
import com.clock.album.view.entity.AlbumViewData;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Clock on 2016/3/21.
 */
public class ImageScannerModelImpl implements ImageScannerModel {

    private final static String TAG = ImageScannerModelImpl.class.getSimpleName();
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

    private OnScanImageFinish mOnScanImageFinish;

    private Handler mRefreshHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            ImageScanResult imageScanResult = (ImageScanResult) msg.obj;
            if (mOnScanImageFinish != null && imageScanResult != null) {
                mOnScanImageFinish.onFinish(imageScanResult);
            }
        }
    };

    @Override
    public void startScanImage(final Context context, LoaderManager loaderManager, final OnScanImageFinish onScanImageFinish) {
        mOnScanImageFinish = onScanImageFinish;
        LoaderManager.LoaderCallbacks<Cursor> loaderCallbacks = new LoaderManager.LoaderCallbacks<Cursor>() {
            @Override
            public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                Log.i(TAG, "-----onCreateLoader-----");
                CursorLoader imageCursorLoader = new CursorLoader(context, MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        IMAGE_PROJECTION, null, null, MediaStore.Images.Media.DEFAULT_SORT_ORDER);
                return imageCursorLoader;
            }

            @Override
            public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
                Log.i(TAG, "-----onLoadFinished-----");
                if (data.getCount() == 0) {
                    if (onScanImageFinish != null) {
                        onScanImageFinish.onFinish(null);//无图片直接返回null
                    }

                } else {
                    int dataColumnIndex = data.getColumnIndex(MediaStore.Images.Media.DATA);
                    //int displayNameColumnIndex = data.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME);
                    //int titleColumnIndex = data.getColumnIndex(MediaStore.Images.Media.TITLE);
                    ArrayList<File> albumFolderList = new ArrayList<>();
                    HashMap<String, ArrayList<File>> albumImageListMap = new HashMap<>();
                    while (data.moveToNext()) {
                        File imageFile = new File(data.getString(dataColumnIndex));//图片文件
                        File albumFolder = imageFile.getParentFile();//图片目录
                        if (!albumFolderList.contains(albumFolder)) {
                            albumFolderList.add(albumFolder);
                        }
                        String albumPath = albumFolder.getAbsolutePath();
                        ArrayList<File> albumImageFiles = albumImageListMap.get(albumPath);
                        if (albumImageFiles == null) {
                            albumImageFiles = new ArrayList<>();
                            albumImageListMap.put(albumPath, albumImageFiles);
                        }
                        albumImageFiles.add(imageFile);//添加到对应的相册目录下面
                    }

                    sortByFileLastModified(albumFolderList);//对图片目录做排序

                    Set<String> keySet = albumImageListMap.keySet();
                    for (String key : keySet) {//对图片目录下所有的图片文件做排序
                        ArrayList<File> albumImageList = albumImageListMap.get(key);
                        sortByFileLastModified(albumImageList);
                    }

                    ImageScanResult imageScanResult = new ImageScanResult();
                    imageScanResult.setAlbumFolderList(albumFolderList);
                    imageScanResult.setAlbumImageListMap(albumImageListMap);

                    //Fix CursorLoader Bug
                    //http://stackoverflow.com/questions/7746140/android-problems-using-fragmentactivity-loader-to-update-fragmentstatepagera
                    Message message = mRefreshHandler.obtainMessage();
                    message.obj = imageScanResult;
                    mRefreshHandler.sendMessage(message);

                }

            }

            @Override
            public void onLoaderReset(Loader<Cursor> loader) {
                Log.i(TAG, "-----onLoaderReset-----");
            }
        };
        loaderManager.initLoader(IMAGE_LOADER_ID, null, loaderCallbacks);//初始化指定id的Loader
    }

    @Override
    public AlbumViewData archiveAlbumInfo(Context context, ImageScanResult imageScanResult) {
        if (imageScanResult != null) {

            List<File> albumFolderList = imageScanResult.getAlbumFolderList();
            Map<String, ArrayList<File>> albumImageListMap = imageScanResult.getAlbumImageListMap();

            if (albumFolderList != null && albumFolderList.size() > 0 && albumImageListMap != null) {

                List<AlbumFolderInfo> albumFolderInfoList = new ArrayList<>();

                AlbumFolderInfo allImageFolder = createAllImageAlbum(context, albumImageListMap);
                if (allImageFolder != null) {
                    albumFolderInfoList.add(allImageFolder);
                }

                int albumFolderSize = albumFolderList.size();
                for (int albumFolderPos = 0; albumFolderPos < albumFolderSize; albumFolderPos++) {

                    File albumFolder = albumFolderList.get(albumFolderPos);
                    AlbumFolderInfo albumFolderInfo = new AlbumFolderInfo();

                    String folderName = albumFolder.getName();
                    albumFolderInfo.setFolderName(folderName);

                    String albumPath = albumFolder.getAbsolutePath();
                    List<File> albumImageList = albumImageListMap.get(albumPath);
                    File frontCover = albumImageList.get(0);
                    albumFolderInfo.setFrontCover(frontCover);//设置首张图片

                    List<ImageInfo> imageInfoList = ImageInfo.buildFromFileList(albumImageList);
                    albumFolderInfo.setImageInfoList(imageInfoList);
                    allImageFolder.getImageInfoList().addAll(imageInfoList);//保存到 "全部图片" 目录下

                    albumFolderInfoList.add(albumFolderInfo);
                }

                AlbumViewData albumViewData = new AlbumViewData();
                albumViewData.setAlbumFolderInfoList(albumFolderInfoList);

                return albumViewData;
            }

            return null;
        } else {
            return null;
        }
    }

    /**
     * 创建一个"全部图片"目录
     *
     * @param albumImageListMap
     * @return
     */
    private AlbumFolderInfo createAllImageAlbum(Context context, Map<String, ArrayList<File>> albumImageListMap) {
        if (albumImageListMap != null) {
            AlbumFolderInfo albumFolderInfo = new AlbumFolderInfo();
            albumFolderInfo.setFolderName(context.getString(R.string.all_image));//设置目录名

            List<ImageInfo> totalImageInfoList = new ArrayList<>();
            albumFolderInfo.setImageInfoList(totalImageInfoList);//设置所有的图片文件

            boolean isFirstAlbum = true; //是否是第一个目录

            Set<String> albumKeySet = albumImageListMap.keySet();
            for (String albumKey : albumKeySet) {//每个目录的图片
                List<File> albumImageList = albumImageListMap.get(albumKey);

                if (isFirstAlbum == true) {
                    File frontCover = albumImageList.get(0);
                    albumFolderInfo.setFrontCover(frontCover);//设置第一张图片

                    isFirstAlbum = false;
                }
            }

            return albumFolderInfo;
        } else {
            return null;
        }
    }


    /**
     * 按照文件的修改时间进行排序，越最近修改的，排得越前
     */
    private void sortByFileLastModified(List<File> files) {
        Collections.sort(files, new Comparator<File>() {
            @Override
            public int compare(File lhs, File rhs) {
                if (lhs.lastModified() > rhs.lastModified()) {
                    return -1;
                } else if (lhs.lastModified() < rhs.lastModified()) {
                    return 1;
                }
                return 0;
            }
        });
    }

}
