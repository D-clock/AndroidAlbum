package com.clock.album.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

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
        mImageScannerPresenter.startScanImage(getApplicationContext(), getSupportLoaderManager());

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
        fragmentTransaction.commit();
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
