package com.clock.album.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.clock.album.R;
import com.clock.album.imageloader.ImageLoaderFactory;
import com.clock.album.imageloader.ImageLoaderWrapper;
import com.clock.utils.common.SystemUtils;

import java.io.File;
import java.io.IOException;

/**
 * 调用系统相机拍照
 *
 * @author Clock
 * @since 2016-05-12
 */
public class TakePhotoActivity extends AppCompatActivity {

    private final static String TEMP_FILE_NAME = "temp";
    private final static String PNG_SUFFIX = ".png";
    private final static String PHOTO_FOLDER_NAME = "photos";
    //private final static String TIMESTAMP_FORMAT = "yyyyMMddHHmmss";
    private final static int TAKE_PHOTO_REQUEST = 1000;

    private File mTempPhotoFile;
    private ImageView mPhotoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_photo);

        mPhotoView = (ImageView) findViewById(R.id.iv_photo_preview);

        if (SystemUtils.mountedSdCard()) {
            File photoFolder = new File(Environment.getExternalStorageDirectory(), PHOTO_FOLDER_NAME);
            if (!photoFolder.exists()) {
                photoFolder.mkdir();
            }
            if (photoFolder.exists()) {
                //String photoName = new SimpleDateFormat(TIMESTAMP_FORMAT).format(new Date());
                mTempPhotoFile = new File(photoFolder, TEMP_FILE_NAME + PNG_SUFFIX);
                if (mTempPhotoFile.exists()) {
                    mTempPhotoFile.delete();
                }
                try {
                    mTempPhotoFile.createNewFile();
                    startCamera(mTempPhotoFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(TEMP_FILE_NAME, mTempPhotoFile);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mTempPhotoFile = (File) savedInstanceState.getSerializable(TEMP_FILE_NAME);
    }

    /**
     * 启动系统相机
     *
     * @param photoFile 图片保存路径
     */
    private void startCamera(File photoFile) {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri photoUri = Uri.fromFile(photoFile);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
        startActivityForResult(cameraIntent, TAKE_PHOTO_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == TAKE_PHOTO_REQUEST) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    ImageLoaderWrapper.DisplayOption displayOption = new ImageLoaderWrapper.DisplayOption();
                    displayOption.loadingResId = R.mipmap.img_default;
                    displayOption.loadErrorResId = R.mipmap.img_error;
                    ImageLoaderFactory.getLoader().displayImage(mPhotoView, mTempPhotoFile, displayOption);
                }

            } else {
                finish();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

}
