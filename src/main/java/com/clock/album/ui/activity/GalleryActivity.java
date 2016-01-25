package com.clock.album.ui.activity;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.clock.album.R;
import com.clock.album.imageloader.ImageLoaderFactory;
import com.clock.album.imageloader.ImageLoaderWrapper;
import com.clock.album.ui.activity.base.BaseActivity;

import java.io.File;
import java.util.List;

import uk.co.senab.photoview.PhotoView;

/**
 * 图片相册浏览界面
 *
 * @author Clock
 * @since 2016-01-25
 */
public class GalleryActivity extends BaseActivity {

    public final static String EXTRA_SHOW_IMAGE_LIST = "ShowImageList";
    public final static String EXTRA_SHOW_IMAGE = "ShowImage";

    private ViewPager mGalleryViewPager;
    private PagerAdapter mGalleryPagerAdapter;

    /**
     * 所有图片的列表
     */
    private List<File> mShowImageFileList;
    /**
     * 刚进入页面显示的图片
     */
    private File mShowImageFile;

    private ImageLoaderWrapper mImageLoaderWrapper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        mImageLoaderWrapper = ImageLoaderFactory.getLoader(ImageLoaderFactory.UNIVERSAL_ANDROID_IMAGE_LOADER);

        mShowImageFile = (File) getIntent().getSerializableExtra(EXTRA_SHOW_IMAGE);
        mShowImageFileList = (List<File>) getIntent().getSerializableExtra(EXTRA_SHOW_IMAGE_LIST);

        initView();

    }

    private void initView() {
        mGalleryViewPager = (ViewPager) findViewById(R.id.gallery_viewpager);
        mGalleryPagerAdapter = new GalleryPagerAdapter();
        mGalleryViewPager.setAdapter(mGalleryPagerAdapter);
        if (mShowImageFile != null && mShowImageFileList != null && mShowImageFileList.contains(mShowImageFile)) {
            int initShowPosition = mShowImageFileList.indexOf(mShowImageFile);
            mGalleryViewPager.setCurrentItem(initShowPosition);
        }
    }

    /**
     * 相册适配器
     */
    private class GalleryPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            if (mShowImageFileList == null) {
                return 0;
            }
            return mShowImageFileList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            PhotoView galleryPhotoView = (PhotoView) view.findViewById(R.id.iv_show_image);
            galleryPhotoView.setScale(1.0f);//让图片在滑动过程中恢复回缩放操作前原图大小
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View galleryItemView = View.inflate(GalleryActivity.this, R.layout.gallery_item, null);

            File imageFile = mShowImageFileList.get(position);
            PhotoView galleryPhotoView = (PhotoView) galleryItemView.findViewById(R.id.iv_show_image);
            ImageLoaderWrapper.DisplayOption displayOption = new ImageLoaderWrapper.DisplayOption();
            displayOption.loadErrorResId = R.mipmap.img_error;
            displayOption.loadingResId = R.mipmap.img_default;
            mImageLoaderWrapper.displayImage(galleryPhotoView, imageFile, displayOption);

            container.addView(galleryItemView);
            return galleryItemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }


    }
}
