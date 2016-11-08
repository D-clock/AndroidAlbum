package com.clock.album.ui.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.clock.album.R;
import com.clock.album.entity.AlbumFolderInfo;
import com.clock.album.entity.ImageInfo;
import com.clock.album.presenter.ImageScannerPresenter;
import com.clock.album.presenter.ImageScannerPresenterImpl;
import com.clock.album.ui.activity.base.BaseActivity;
import com.clock.album.ui.fragment.AlbumDetailFragment;
import com.clock.album.ui.fragment.AlbumFolderFragment;
import com.clock.album.view.AlbumView;
import com.clock.album.view.ImageChooseView;
import com.clock.album.view.entity.AlbumViewData;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 系统相册页面
 *
 * @author Clock
 * @since 2016-01-06
 */
public class AlbumActivity extends BaseActivity implements View.OnClickListener, ImageChooseView, AlbumView {

    private final static String TAG = AlbumActivity.class.getSimpleName();
    private final static String FRAGMENT_BACK_STACK = "FragmentBackStack";
    private final static String PACKAGE_URL_SCHEME = "package:";

    /**
     * Android M 的Runtime Permission特性申请权限用的
     */
    private final static int REQUEST_READ_EXTERNAL_STORAGE_CODE = 1;
    /**
     * 相册列表页面
     */
    private AlbumFolderFragment mAlbumFolderFragment;
    /**
     * 相册详情页面
     */
    private HashMap<AlbumFolderInfo, AlbumDetailFragment> mAlbumDetailFragmentMap = new HashMap<>();
    /**
     * 被选中的图片文件列表
     */
    private ArrayList<File> mSelectedImageFileList = new ArrayList<>();

    private ImageScannerPresenter mImageScannerPresenter;
    /**
     * 相册目录信息列表
     */
    private List<AlbumFolderInfo> mAlbumFolderInfoList;
    /**
     * 显示图片目录的名称，选中图片的按钮
     */
    private TextView mTitleView, mSelectedView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);

        mTitleView = (TextView) findViewById(R.id.tv_dir_title);
        mSelectedView = (TextView) findViewById(R.id.tv_selected_ok);
        mSelectedView.setOnClickListener(this);

        findViewById(R.id.iv_back).setOnClickListener(this);

        mImageScannerPresenter = new ImageScannerPresenterImpl(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Toast.makeText(this, R.string.grant_advice_read_album, Toast.LENGTH_SHORT).show();
            }
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_READ_EXTERNAL_STORAGE_CODE);
        } else {
            mImageScannerPresenter.startScanImage(getApplicationContext(), getSupportLoaderManager());
        }

    }

    /**
     * 显示打开权限提示的对话框
     */
    private void showMissingPermissionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.help);
        builder.setMessage(R.string.help_content);

        builder.setNegativeButton(R.string.quit, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(AlbumActivity.this, R.string.grant_permission_failure, Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        builder.setPositiveButton(R.string.settings, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startSystemSettings();
                finish();
            }
        });

        builder.show();
    }


    /**
     * 启动系统权限设置界面
     */
    private void startSystemSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse(PACKAGE_URL_SCHEME + getPackageName()));
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_READ_EXTERNAL_STORAGE_CODE) {
            boolean granted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
            if (granted) {
                Toast.makeText(this, R.string.grant_permission_success, Toast.LENGTH_SHORT).show();
                mImageScannerPresenter.startScanImage(getApplicationContext(), getSupportLoaderManager());

            } else {
                showMissingPermissionDialog();//提示对话框
                //Toast.makeText(this, R.string.grant_permission_failure, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        if (viewId == R.id.iv_back) {
            onBackPressed();

        } else if (viewId == R.id.tv_selected_ok) {
            Intent showSelectedIntent = new Intent(this, ImageSelectActivity.class);
            showSelectedIntent.putExtra(ImageSelectActivity.EXTRA_SELECTED_IMAGE_LIST, mSelectedImageFileList);
            startActivity(showSelectedIntent);
            finish();

        }
    }

    @Override
    public void switchAlbumFolder(AlbumFolderInfo albumFolderInfo) {
        if (albumFolderInfo != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            AlbumDetailFragment albumDetailFragment = mAlbumDetailFragmentMap.get(albumFolderInfo);
            if (albumDetailFragment == null) {
                List<ImageInfo> imageInfoList = albumFolderInfo.getImageInfoList();
                albumDetailFragment = AlbumDetailFragment.newInstance(imageInfoList);
                mAlbumDetailFragmentMap.put(albumFolderInfo, albumDetailFragment);
            }
            fragmentTransaction.replace(R.id.fragment_container, albumDetailFragment);
            fragmentTransaction.addToBackStack(FRAGMENT_BACK_STACK);
            fragmentTransaction.commit();

            refreshFolderName(albumFolderInfo.getFolderName());
        }
    }

    /**
     * 刷新目录名称
     *
     * @param albumFolderName
     */
    private void refreshFolderName(String albumFolderName) {
        if (!TextUtils.isEmpty(albumFolderName)) {
            mTitleView.setText(albumFolderName);
        }
    }

    /**
     * 切换到相册列表
     */
    private void switchAlbumFolderList() {
        final FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, mAlbumFolderFragment);
        fragmentManager.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                int backStackCount = fragmentManager.getBackStackEntryCount();
                if (backStackCount == 0) {
                    AlbumFolderInfo albumFolderInfo = mAlbumFolderInfoList.get(0);
                    String folderName = albumFolderInfo.getFolderName();
                    refreshFolderName(folderName);
                }
            }
        });
        //fragmentTransaction.commit(); //会产生 java.lang.IllegalStateException: Can not perform this action after onSaveInstanceState
        fragmentTransaction.commitAllowingStateLoss();//http://stackoverflow.com/questions/25486656/java-lang-illegalstateexceptioncan-not-perform-this-action-after-onsaveinstance
    }


    /**
     * 刷新选中按钮的状态
     */
    private void refreshSelectedViewState() {
        if (mSelectedImageFileList.size() == 0) {
            mSelectedView.setVisibility(View.GONE);

        } else {
            String selectedStringFormat = getString(R.string.selected_ok);
            int selectedSize = mSelectedImageFileList.size();
            AlbumFolderInfo albumFolderInfo = mAlbumFolderInfoList.get(0);
            int totalSize = albumFolderInfo.getImageInfoList().size();
            String selectedString = String.format(selectedStringFormat, selectedSize, totalSize);
            mSelectedView.setText(selectedString);
            mSelectedView.setVisibility(View.VISIBLE);

        }
    }

    @Override
    public void refreshAlbumData(AlbumViewData albumData) {
        if (albumData != null) {
            mAlbumFolderInfoList = albumData.getAlbumFolderInfoList();
            mAlbumFolderFragment = AlbumFolderFragment.newInstance(mAlbumFolderInfoList);
            switchAlbumFolderList();

            findViewById(R.id.fragment_container).setVisibility(View.VISIBLE);//显示相册列表区域

        } else {
            findViewById(R.id.fragment_container).setVisibility(View.GONE);//隐藏显示相册列表的区域
            findViewById(R.id.tv_no_image).setVisibility(View.VISIBLE);//显示没有相片的提示

        }
    }

    @Override
    public void refreshSelectedCounter(ImageInfo imageInfo) {
        if (imageInfo != null) {
            boolean isSelected = imageInfo.isSelected();
            File imageFile = imageInfo.getImageFile();
            if (isSelected) {//选中
                if (!mSelectedImageFileList.contains(imageFile)) {
                    mSelectedImageFileList.add(imageFile);
                }
            } else {//取消选中
                if (mSelectedImageFileList.contains(imageFile)) {
                    mSelectedImageFileList.remove(imageFile);
                }
            }
            refreshSelectedViewState();
        }
    }

}
