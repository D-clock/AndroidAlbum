package com.clock.album.ui.activity;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.clock.album.R;
import com.clock.album.entity.ImageInfo;
import com.clock.album.imageloader.ImageLoaderFactory;
import com.clock.album.imageloader.ImageLoaderWrapper;
import com.clock.album.ui.activity.base.BaseActivity;

import java.util.List;

import uk.co.senab.photoview.PhotoView;

/**
 * 图片预览界面
 *
 * @author Clock
 * @since 2016-01-25
 */
public class ImagePreviewActivity extends BaseActivity implements View.OnClickListener {

    public final static String EXTRA_IMAGE_INFO_LIST = "ImageInfoList";
    public final static String EXTRA_IMAGE_INFO = "ImageInfo";

    private ViewPager mPreviewViewPager;
    private PagerAdapter mPreviewPagerAdapter;
    private ViewPager.OnPageChangeListener mPreviewChangeListener;
    private TextView mTitleView, mSelectedOkView;

    /**
     * 所有图片的列表
     */
    private List<ImageInfo> mPreviewImageInfoList;
    /**
     * 刚进入页面显示的图片
     */
    private ImageInfo mPreviewImageInfo;

    private ImageLoaderWrapper mImageLoaderWrapper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_preview);

        mImageLoaderWrapper = ImageLoaderFactory.getLoader(ImageLoaderFactory.UNIVERSAL_ANDROID_IMAGE_LOADER);

        mPreviewImageInfo = (ImageInfo) getIntent().getSerializableExtra(EXTRA_IMAGE_INFO);
        mPreviewImageInfoList = (List<ImageInfo>) getIntent().getSerializableExtra(EXTRA_IMAGE_INFO_LIST);

        initView();

    }

    private void initView() {
        mTitleView = (TextView) findViewById(R.id.tv_title);
        if (mPreviewImageInfo != null && mPreviewImageInfoList != null) {
            if (mPreviewImageInfoList.contains(mPreviewImageInfo)) {
                setPositionToTitle(mPreviewImageInfoList.indexOf(mPreviewImageInfo));
            }
        }

        mSelectedOkView = (TextView) findViewById(R.id.tv_selected_ok);
        mSelectedOkView.setOnClickListener(this);

        mPreviewViewPager = (ViewPager) findViewById(R.id.gallery_viewpager);
        mPreviewPagerAdapter = new PreviewPagerAdapter();
        mPreviewViewPager.setAdapter(mPreviewPagerAdapter);
        if (mPreviewImageInfo != null && mPreviewImageInfoList != null && mPreviewImageInfoList.contains(mPreviewImageInfo)) {
            int initShowPosition = mPreviewImageInfoList.indexOf(mPreviewImageInfo);
            mPreviewViewPager.setCurrentItem(initShowPosition);
        }
        mPreviewChangeListener = new PreviewChangeListener();
        mPreviewViewPager.addOnPageChangeListener(mPreviewChangeListener);

        findViewById(R.id.iv_back).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        if (viewId == R.id.iv_back) {
            onBackPressed();

        } else if (viewId == R.id.tv_selected_ok) {

        }
    }

    /**
     * 相册适配器
     */
    private class PreviewPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            if (mPreviewImageInfoList == null) {
                return 0;
            }
            return mPreviewImageInfoList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            PhotoView galleryPhotoView = (PhotoView) view.findViewById(R.id.iv_show_image);
            galleryPhotoView.setScale(1.0f);//让图片在滑动过程中恢复回缩放操作前原图大小
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View galleryItemView = View.inflate(ImagePreviewActivity.this, R.layout.preview_image_item, null);

            ImageInfo imageInfo = mPreviewImageInfoList.get(position);
            PhotoView galleryPhotoView = (PhotoView) galleryItemView.findViewById(R.id.iv_show_image);
            ImageLoaderWrapper.DisplayOption displayOption = new ImageLoaderWrapper.DisplayOption();
            displayOption.loadErrorResId = R.mipmap.img_error;
            displayOption.loadingResId = R.mipmap.img_default;
            mImageLoaderWrapper.displayImage(galleryPhotoView, imageInfo.getImageFile(), displayOption);

            container.addView(galleryItemView);
            return galleryItemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

    }

    /**
     * 相册详情页面滑动监听
     */
    private class PreviewChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            setPositionToTitle(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    /**
     * 设置标题现实当前所处的位置
     *
     * @param position
     */
    private void setPositionToTitle(int position) {
        if (mPreviewImageInfoList != null) {
            String title = String.format(getString(R.string.image_index), position + 1, mPreviewImageInfoList.size());
            mTitleView.setText(title);
        }
    }
}
