package com.clock.album.activity;

import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.clock.album.R;
import com.clock.album.activity.base.BaseActivity;
import com.clock.album.entity.AlbumInfo;
import com.clock.album.fragment.AlbumDetailFragment;
import com.clock.album.fragment.AlbumFolderFragment;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

/**
 * 系统相册页面
 *
 * @author Clock
 * @since 2016-01-06
 */
public class AlbumActivity extends BaseActivity implements View.OnClickListener, AlbumFolderFragment.OnAlbumDetailInteractionListener {

    private final static String TAG = AlbumActivity.class.getSimpleName();

    /**
     * Loader的唯一ID号
     */
    private final static int IMAGE_LOADER_ID = 1000;
    /**
     * 刷新相册目录
     */
    private final static int REFRESH_ALBUM_MSG_TYPE = 1;

    private final static String[] IMAGE_PROJECTION = new String[]{
            MediaStore.Images.Media.DATA,//图片路径
            MediaStore.Images.Media.DISPLAY_NAME,//图片文件名，包括后缀名
            MediaStore.Images.Media.TITLE//图片文件名，不包含后缀
    };

    /**
     * 相册列表页面
     */
    private AlbumFolderFragment mAlbumFolderFragment;
    /**
     * 相册详情页面
     */
    private HashMap<String, AlbumDetailFragment> mAlbumDetailFragmentMap = new HashMap<>();
    /**
     * 相册目录列表
     */
    private ArrayList<File> mAlbumFolderList = new ArrayList<>();
    /**
     * 所有图片的信息列表（图片目录的绝对路径作为map的key，value是该图片目录下的所有图片文件信息）
     */
    private HashMap<String, ArrayList<File>> mAlbumImageInfoMap = new HashMap<>();
    /**
     * 刷新相册目录
     */
    private Handler mRefreshFolderHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == REFRESH_ALBUM_MSG_TYPE) {
                HashMap<String, File> albumFontImageMap = (HashMap<String, File>) msg.obj;

                ArrayList<AlbumInfo> albumInfoArrayList = new ArrayList<>();
                for (File albumFolder : mAlbumFolderList) {
                    AlbumInfo albumInfo = new AlbumInfo();
                    albumInfo.setFolder(albumFolder);//设置图片目录

                    String key = albumFolder.getAbsolutePath();
                    albumInfo.setFileCount(mAlbumImageInfoMap.get(key).size());//设置目录下图片的个数
                    albumInfoArrayList.add(albumInfo);
                }

                mAlbumFolderFragment = AlbumFolderFragment.newInstance(albumInfoArrayList, albumFontImageMap);
                switchFolderList();

            } else {
                super.handleMessage(msg);

            }
        }
    };

    /**
     * 显示图片目录的名称
     */
    private TextView mTitleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);

        mTitleView = (TextView) findViewById(R.id.tv_dir_title);
        findViewById(R.id.iv_back).setOnClickListener(this);

        loadImagePath();

    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        if (viewId == R.id.iv_back) {
            onBackPressed();
        }
    }

    private LoaderManager.LoaderCallbacks<Cursor> mImageLoaderCallback = new LoaderManager.LoaderCallbacks<Cursor>() {

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            Log.i(TAG, "onCreateLoader....");
            CursorLoader imageCursorLoader = new CursorLoader(AlbumActivity.this, MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    IMAGE_PROJECTION, null, null, MediaStore.Images.Media.DEFAULT_SORT_ORDER);
            return imageCursorLoader;
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            Log.i(TAG, "onLoadFinished....");
            int dataColumnIndex = data.getColumnIndex(MediaStore.Images.Media.DATA);
            int displayNameColumnIndex = data.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME);
            int titleColumnIndex = data.getColumnIndex(MediaStore.Images.Media.TITLE);

            ArrayList<File> albumFolderList = new ArrayList<>();//存放所有有图片的目录

            while (data.moveToNext()) {//遍历更大文件夹
                File imageFile = new File(data.getString(dataColumnIndex));//图片文件
                File albumFolder = imageFile.getParentFile();//获取图片所在的目录
                if (!albumFolderList.contains(albumFolder)) {//存放图片文件夹的目录
                    albumFolderList.add(albumFolder);
                }

                String key = albumFolder.getAbsolutePath();//将路径作为key
                ArrayList<File> imageInfoList = null;//存放一个文件夹下的所有图片
                if (mAlbumImageInfoMap.containsKey(key)) {
                    imageInfoList = mAlbumImageInfoMap.get(key);
                } else {
                    imageInfoList = new ArrayList<>();
                    mAlbumImageInfoMap.put(key, imageInfoList);
                }
                imageInfoList.add(imageFile);//把图片文件信息添加到文件夹列表中
            }

            //下面两个排序参照了微信，提高一些用户体验
            sortAlbumFolder(albumFolderList);
            sortAlbumImageFile(albumFolderList, mAlbumImageInfoMap);

            int totalImageCount = data.getCount();//所有照片总数
            if (totalImageCount > 0) {
                File allImageFolder = new File("/" + getString(R.string.all_image));//放置所有图片文件的目录，虚拟的，实际上它在SD卡上不存在
                mAlbumFolderList.add(allImageFolder);//添加汇总所有图片的目录
                mAlbumFolderList.addAll(albumFolderList);//添加各个图片的目录

                String key = allImageFolder.getAbsolutePath();
                ArrayList<File> allImage = getAllImage(albumFolderList);//获取所有的图片
                mAlbumImageInfoMap.put(key, allImage);//存放所有图片的目录
            }

            HashMap<String, File> albumFrontImageMap = getAllFrontImage();//存放每一个图片下首张图片的信息

            //onLoadFinished虽然处于UI线程，但是直接切换Fragment会报错，所以这里就用Handler了
            Message message = mRefreshFolderHandler.obtainMessage(REFRESH_ALBUM_MSG_TYPE);
            message.obj = albumFrontImageMap;
            mRefreshFolderHandler.sendMessage(message);

        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
            Log.i(TAG, "onLoaderReset....");
        }
    };

    /**
     * 加载所有的图片的路径
     */
    private void loadImagePath() {
        LoaderManager loaderManager = getSupportLoaderManager();
        loaderManager.initLoader(IMAGE_LOADER_ID, null, mImageLoaderCallback);//初始化指定id的Loader
    }

    /**
     * 获取所有图片目录的首图
     *
     * @return
     */
    private HashMap<String, File> getAllFrontImage() {
        HashMap<String, File> albumFrontImageMap = new HashMap<>();//存放每一个图片下首张图片的信息
        for (File albumFolder : mAlbumFolderList) {
            String key = albumFolder.getAbsolutePath();
            File frontImageFile = mAlbumImageInfoMap.get(key).get(0);//获取每个图片目录下第一张图片的信息
            albumFrontImageMap.put(key, frontImageFile);
        }
        return albumFrontImageMap;
    }

    /**
     * 获取目录下所有的图片文件
     *
     * @param albumFolderList
     * @return
     */
    private ArrayList<File> getAllImage(ArrayList<File> albumFolderList) {
        ArrayList<File> allImage = new ArrayList<>();//存放全部图片
        for (File albumFolder : albumFolderList) {
            ArrayList<File> albumImages = mAlbumImageInfoMap.get(albumFolder.getAbsolutePath());//获取每个目录下的图片
            allImage.addAll(albumImages);
        }
        return allImage;
    }

    /**
     * 对图片目录进行排序，修改时间距离当前时间越短的排得越前，以此类推
     *
     * @param albumFolderList
     */
    private void sortAlbumFolder(ArrayList<File> albumFolderList) {
        Collections.sort(albumFolderList, new Comparator<File>() {
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

    /**
     * 对图片目录下的图片进行排序，增加时间距离当前时间越短的拍得越前，以此类推
     *
     * @param albumFolderList
     * @param albumImageInfoMap
     */
    private void sortAlbumImageFile(ArrayList<File> albumFolderList, HashMap<String, ArrayList<File>> albumImageInfoMap) {
        for (File albumFolder : albumFolderList) {
            String key = albumFolder.getAbsolutePath();
            ArrayList<File> albumImageList = albumImageInfoMap.get(key);
            Collections.sort(albumImageList, new Comparator<File>() {
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


    @Override
    public void switchAlbumFolder(File albumFolder) {
        String key = albumFolder.getAbsolutePath();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        AlbumDetailFragment albumDetailFragment = mAlbumDetailFragmentMap.get(key);
        if (albumDetailFragment == null) {
            ArrayList<File> imageFileList = mAlbumImageInfoMap.get(key);
            albumDetailFragment = AlbumDetailFragment.newInstance(imageFileList);
            mAlbumDetailFragmentMap.put(key, albumDetailFragment);
        }
        fragmentTransaction.replace(R.id.fragment_container, albumDetailFragment);
        fragmentTransaction.addToBackStack(key);
        fragmentTransaction.commit();
    }

    @Override
    public void refreshFolderName(String albumFolderName) {
        if (!TextUtils.isEmpty(albumFolderName)) {
            mTitleView.setText(albumFolderName);
        }
    }

    /**
     * 切换到相册列表
     */
    private void switchFolderList() {
        final FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, mAlbumFolderFragment);
        fragmentManager.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                int backStackCount = fragmentManager.getBackStackEntryCount();
                if (backStackCount == 0) {
                    refreshFolderName(getString(R.string.all_image));
                }
            }
        });
        fragmentTransaction.commit();
    }
}
