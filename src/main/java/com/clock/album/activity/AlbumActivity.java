package com.clock.album.activity;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.View;
import android.widget.GridView;

import com.clock.album.R;
import com.clock.album.activity.base.BaseActivity;
import com.clock.album.adapter.AlbumGridAdapter;

/**
 * 系统相册页面
 *
 * @author Clock
 * @since 2016-01-06
 */
public class AlbumActivity extends BaseActivity implements View.OnClickListener {

    private final static String TAG = AlbumActivity.class.getSimpleName();
    /**
     * Loader的唯一ID号
     */
    private final static int IMAGE_LOADER_ID = 1000;

    private final static String[] IMAGE_PROJECTION = new String[]{
            MediaStore.Images.Media.DATA,//图片路径
            MediaStore.Images.Media.DISPLAY_NAME,//图片文件名，包括后缀名
            MediaStore.Images.Media.TITLE//图片文件名，不包含后缀
    };

    /**
     * 相册视图控件
     */
    private GridView mAlbumGridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);

        mAlbumGridView = (GridView) findViewById(R.id.gv_album);
        mAlbumGridView.setAdapter(new AlbumGridAdapter());

    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
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
                Log.i(TAG, "data: " + data.getString(dataColumnIndex));
                Log.i(TAG, "title: " + data.getString(titleColumnIndex));
                Log.i(TAG, "displayName: " + data.getString(displayNameColumnIndex));
            }

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
}
