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
import com.clock.album.entity.ImageInfo;
import com.clock.album.fragment.AlbumDetailFragment;
import com.clock.album.fragment.AlbumFolderFragment;
import com.clock.utils.text.StringUtils;

import java.io.File;
import java.util.ArrayList;
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
    private ArrayList<String> mAlbumFolderNameList = new ArrayList<>();
    /**
     * 所有图片的信息列表（图片目录的绝对路径作MD5转化后作为map的key，value是该图片目录下的所有图片文件信息）
     */
    private HashMap<String, ArrayList<ImageInfo>> mAlbumImageInfoMap = new HashMap<>();
    /**
     * 刷新相册目录
     */
    private Handler mRefreshFolderHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == REFRESH_ALBUM_MSG_TYPE) {
                HashMap<String, ImageInfo> albumFontImageMap = (HashMap<String, ImageInfo>) msg.obj;
                mAlbumFolderFragment = AlbumFolderFragment.newInstance(mAlbumFolderNameList, albumFontImageMap);
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

            while (data.moveToNext()) {

                File imageFile = new File(data.getString(dataColumnIndex));//图片文件

                String folderName = imageFile.getParent();//获取目录名
                if (!mAlbumFolderNameList.contains(folderName)) {//存放图片文件夹的目录
                    mAlbumFolderNameList.add(folderName);
                }

                String folderNameMd5 = StringUtils.md5(folderName);
                ArrayList<ImageInfo> imageInfoList = null;//存放一个文件夹下的所有图片
                if (mAlbumImageInfoMap.containsKey(folderNameMd5)) {
                    imageInfoList = mAlbumImageInfoMap.get(folderNameMd5);

                } else {
                    imageInfoList = new ArrayList<>();
                    mAlbumImageInfoMap.put(folderNameMd5, imageInfoList);

                }
                ImageInfo imageInfo = new ImageInfo();
                imageInfo.setFile(imageFile);
                imageInfoList.add(imageInfo);

            }

            //这里要做两个排序

            //1.文件夹最近修改的排越上

            //2.文件最近增加的排越上

            HashMap<String, ImageInfo> albumFrontImageMap = new HashMap<>();//存放每一个图片下首张图片的信息
            for (String folderName : mAlbumFolderNameList) {
                String folderNameMd5 = StringUtils.md5(folderName);
                ImageInfo imageInfo = mAlbumImageInfoMap.get(folderNameMd5).get(0);//获取每个图片目录下第一张图片的信息
                albumFrontImageMap.put(folderNameMd5, imageInfo);
            }

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
        //http://blog.csdn.net/xiaanming/article/details/18730223
        //http://developer.android.com/reference/android/provider/MediaStore.Images.Media.html
        //http://developer.android.com/reference/android/app/LoaderManager.html
        //http://developer.android.com/reference/android/content/ContentProvider.html
        LoaderManager loaderManager = getSupportLoaderManager();
        loaderManager.initLoader(IMAGE_LOADER_ID, null, mImageLoaderCallback);//初始化指定id的Loader
    }


    @Override
    public void switchAlbumFolder(String directory) {
        String directoryMd5 = StringUtils.md5(directory);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        AlbumDetailFragment albumDetailFragment = mAlbumDetailFragmentMap.get(directoryMd5);
        if (albumDetailFragment == null) {
            ArrayList<ImageInfo> imageInfoList = mAlbumImageInfoMap.get(directoryMd5);
            albumDetailFragment = AlbumDetailFragment.newInstance(imageInfoList);
            mAlbumDetailFragmentMap.put(directoryMd5, albumDetailFragment);
        }
        fragmentTransaction.replace(R.id.fragment_container, albumDetailFragment);
        fragmentTransaction.addToBackStack(directoryMd5);
        fragmentTransaction.commit();
    }

    @Override
    public void refreshFolderName(String directory) {
        if (!TextUtils.isEmpty(directory)) {
            mTitleView.setText(directory);
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
