package com.clock.album.ui.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.clock.album.R;
import com.clock.album.entity.AlbumInfo;
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

/**
 * 系统相册页面
 *
 * @author Clock
 * @since 2016-01-06
 */
public class AlbumActivity extends BaseActivity implements View.OnClickListener, AlbumFolderFragment.OnAlbumDetailInteractionListener {

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
     * 相册目录列表
     */
    private List<File> mAlbumFolderList;
    /**
     * 所有图片的信息列表（图片目录的绝对路径作为map的key，value是该图片目录下的所有图片文件信息）
     */
    private Map<String, ArrayList<File>> mAlbumImageListMap;

    private ImageScannerPresenter mImageScannerPresenter;
    private ImageScannerInteraction mImageScannerInteraction;
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

        mImageScannerInteraction = new ImageScannerInteractionImpl();

        mImageScannerPresenter = new ImageScanner(getApplicationContext());
        mImageScannerPresenter.scanExternalStorage(getSupportLoaderManager(), mImageScannerInteraction);

    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        if (viewId == R.id.iv_back) {
            onBackPressed();
        }
    }

    @Override
    public void switchAlbumFolder(File albumFolder) {
        String key = albumFolder.getAbsolutePath();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        AlbumDetailFragment albumDetailFragment = mAlbumDetailFragmentMap.get(key);
        if (albumDetailFragment == null) {
            ArrayList<File> imageFileList = mAlbumImageListMap.get(key);
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

    /**
     * 图片扫描交互接口实现
     */
    private class ImageScannerInteractionImpl implements ImageScannerInteraction {

        @Override
        public void refreshImageInfo(List<File> albumFolderList, Map<String, ArrayList<File>> albumImageListMap) {
            if (albumFolderList != null && albumFolderList.size() > 0) {
                mAlbumFolderList = albumFolderList;
                mAlbumImageListMap = albumImageListMap;

                ArrayList<AlbumInfo> albumInfoArrayList = new ArrayList<>();
                AlbumInfo allImageAlbumInfo = new AlbumInfo();//放全部图片文件目录的信息
                File allImageAlbumFolder = new File("/" + getString(R.string.all_image));//用来存放所有图片的虚拟文件夹，实际在SD卡上是不存在的
                allImageAlbumInfo.setFolder(allImageAlbumFolder);
                albumInfoArrayList.add(allImageAlbumInfo);
                int allImageCounter = 0;

                ArrayList<File> allImageFileList = new ArrayList<>();
                mAlbumImageListMap.put(allImageAlbumFolder.getAbsolutePath(), allImageFileList);//全部图片

                int albumFolderSize = albumFolderList.size();
                for (int albumFolderPos = 0; albumFolderPos < albumFolderSize; albumFolderPos++) {
                    File albumFolder = albumFolderList.get(albumFolderPos);
                    AlbumInfo albumInfo = new AlbumInfo();
                    albumInfo.setFolder(albumFolder);
                    String albumPath = albumFolder.getAbsolutePath();
                    ArrayList<File> albumImageFileList = albumImageListMap.get(albumPath);
                    albumInfo.setFrontCover(albumImageFileList.get(0));//设置相册的封面
                    albumInfo.setFileCount(albumImageFileList.size());
                    albumInfoArrayList.add(albumInfo);
                    if (albumFolderPos == 0) {//设置"全部图片目录"的封面
                        allImageAlbumInfo.setFrontCover(albumImageFileList.get(0));
                    }
                    allImageCounter = allImageCounter + albumImageFileList.size();//计算所有图片的总数
                    allImageFileList.addAll(albumImageFileList);//添加所有图片汇总
                }
                allImageAlbumInfo.setFileCount(allImageCounter);

                mAlbumFolderFragment = AlbumFolderFragment.newInstance(albumInfoArrayList);
                switchAlbumFolderList();
            }
        }
    }
}
