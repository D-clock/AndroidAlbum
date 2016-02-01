package com.clock.album.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.clock.album.R;
import com.clock.album.entity.AlbumInfo;
import com.clock.album.entity.ImageInfo;
import com.clock.album.presenter.ImageScanner;
import com.clock.album.presenter.ImageScannerPresenter;
import com.clock.album.ui.activity.base.BaseActivity;
import com.clock.album.ui.fragment.AlbumDetailFragment;
import com.clock.album.ui.fragment.AlbumFolderFragment;
import com.clock.album.ui.interaction.ImageScannerInteraction;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 系统相册页面
 *
 * @author Clock
 * @since 2016-01-06
 */
public class AlbumActivity extends BaseActivity implements View.OnClickListener, AlbumFolderFragment.OnAlbumDetailInteractionListener,
        AlbumDetailFragment.OnImageSelectedInteractionListener {

    private final static String TAG = AlbumActivity.class.getSimpleName();

    /**
     * 相册列表页面
     */
    private AlbumFolderFragment mAlbumFolderFragment;
    /**
     * 相册详情页面
     */
    private HashMap<String, AlbumDetailFragment> mAlbumDetailFragmentMap = new HashMap<>();
    /**
     * 所有图片的信息列表（图片目录的绝对路径作为map的key，value是该图片目录下的所有图片文件信息）
     */
    private Map<String, ArrayList<ImageInfo>> mAlbumImageInfoListMap;
    /**
     * 被选中的图片文件列表
     */
    private ArrayList<File> mSelectedImageFileList = new ArrayList<>();
    /**
     * 图片的总数
     */
    private int mImageTotalCount = 0;

    private ImageScannerPresenter mImageScannerPresenter;
    private ImageScannerInteraction mImageScannerInteraction;
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

        mImageScannerInteraction = new ImageScannerInteractionImpl();

        mImageScannerPresenter = new ImageScanner(getApplicationContext());
        mImageScannerPresenter.scanExternalStorage(getSupportLoaderManager(), mImageScannerInteraction);

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
    public void switchAlbumFolder(File albumFolder) {
        String key = albumFolder.getAbsolutePath();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        AlbumDetailFragment albumDetailFragment = mAlbumDetailFragmentMap.get(key);
        if (albumDetailFragment == null) {
            ArrayList<ImageInfo> imageInfoList = mAlbumImageInfoListMap.get(key);
            albumDetailFragment = AlbumDetailFragment.newInstance(imageInfoList);
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
    private void switchAlbumFolderList() {
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

    @Override
    public void onSelected(File imageFile) {
        if (!mSelectedImageFileList.contains(imageFile)) {
            mSelectedImageFileList.add(imageFile);
            refreshSelectedViewState();
        }
    }

    @Override
    public void onUnSelected(File imageFile) {
        if (mSelectedImageFileList.contains(imageFile)) {
            mSelectedImageFileList.remove(imageFile);
            refreshSelectedViewState();
        }
    }

    /**
     * 刷新选中按钮的状态
     */
    private void refreshSelectedViewState() {
        if (mSelectedImageFileList.size() == 0) {
            mSelectedView.setVisibility(View.GONE);

        } else {
            String selectedStringFormat = getString(R.string.selected_ok);
            String selectedString = String.format(selectedStringFormat, mSelectedImageFileList.size(), mImageTotalCount);
            mSelectedView.setText(selectedString);
            mSelectedView.setVisibility(View.VISIBLE);

        }
    }

    /**
     * 图片扫描交互接口实现
     */
    private class ImageScannerInteractionImpl implements ImageScannerInteraction {

        @Override
        public void refreshImageInfo(List<File> albumFolderList, Map<String, ArrayList<File>> albumImageListMap) {
            if (albumFolderList != null && albumFolderList.size() > 0) {

                ArrayList<AlbumInfo> albumInfoArrayList = new ArrayList<>();
                AlbumInfo totalImageAlbumInfo = new AlbumInfo();//放全部图片文件目录的信息
                File totalImageAlbumFolder = new File("/" + getString(R.string.all_image));//用来存放所有图片的虚拟文件夹，实际在SD卡上是不存在的
                totalImageAlbumInfo.setFolder(totalImageAlbumFolder);
                albumInfoArrayList.add(totalImageAlbumInfo);
                int totalImageCounter = 0;

                ArrayList<ImageInfo> totalImageInfoList = new ArrayList<>();
                mAlbumImageInfoListMap = new HashMap<>();
                mAlbumImageInfoListMap.put(totalImageAlbumFolder.getAbsolutePath(), totalImageInfoList);//全部图片

                Set<String> albumKeySet = albumImageListMap.keySet();
                for (String albumKey : albumKeySet) {//每个目录的图片
                    ArrayList<ImageInfo> imageInfoArrayList = ImageInfo.buildFromFileList(albumImageListMap.get(albumKey));
                    mAlbumImageInfoListMap.put(albumKey, imageInfoArrayList);
                }

                int albumFolderSize = albumFolderList.size();
                for (int albumFolderPos = 0; albumFolderPos < albumFolderSize; albumFolderPos++) {
                    File albumFolder = albumFolderList.get(albumFolderPos);
                    AlbumInfo albumInfo = new AlbumInfo();
                    albumInfo.setFolder(albumFolder);
                    String albumPath = albumFolder.getAbsolutePath();
                    ArrayList<ImageInfo> imageInfoList = mAlbumImageInfoListMap.get(albumPath);
                    albumInfo.setFrontCover(imageInfoList.get(0).getImageFile());//设置相册的封面
                    albumInfo.setFileCount(imageInfoList.size());
                    albumInfoArrayList.add(albumInfo);
                    if (albumFolderPos == 0) {//设置"全部图片目录"的封面
                        totalImageAlbumInfo.setFrontCover(imageInfoList.get(0).getImageFile());
                    }
                    totalImageCounter = totalImageCounter + imageInfoList.size();//计算所有图片的总数
                    totalImageInfoList.addAll(imageInfoList);//添加所有图片汇总
                }
                mImageTotalCount = totalImageCounter;
                totalImageAlbumInfo.setFileCount(totalImageCounter);

                mAlbumFolderFragment = AlbumFolderFragment.newInstance(albumInfoArrayList);
                switchAlbumFolderList();
            }
        }
    }
}
