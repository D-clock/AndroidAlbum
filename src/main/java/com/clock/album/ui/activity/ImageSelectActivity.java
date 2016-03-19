package com.clock.album.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.clock.album.R;
import com.clock.album.imageloader.ImageLoaderFactory;
import com.clock.album.imageloader.ImageLoaderWrapper;
import com.clock.album.ui.activity.base.BaseActivity;
import com.clock.utils.common.RuleUtils;

import java.io.File;
import java.util.List;

/**
 * 显示选中图片的界面
 *
 * @author Clock
 * @since 2016-01-26
 */
public class ImageSelectActivity extends BaseActivity implements View.OnClickListener {

    public final static String EXTRA_SELECTED_IMAGE_LIST = "selectImage";

    private GridView mSelectedImageGridView;
    private List<File> mSelectedImageList;
    private ImageLoaderWrapper mImageLoaderWrapper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_select);

        findViewById(R.id.iv_back).setOnClickListener(this);

        mImageLoaderWrapper = ImageLoaderFactory.getLoader();
        mSelectedImageList = (List<File>) getIntent().getSerializableExtra(EXTRA_SELECTED_IMAGE_LIST);

        mSelectedImageGridView = (GridView) findViewById(R.id.gv_image_selected);
        mSelectedImageGridView.setAdapter(new SelectedImageGridAdapter());

    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        if (viewId == R.id.iv_back) {
            onBackPressed();

        }
    }

    private class SelectedImageGridAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            if (mSelectedImageList == null) {
                return 0;
            }
            return mSelectedImageList.size();
        }

        @Override
        public Object getItem(int position) {
            return mSelectedImageList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            SelectedImageHolder holder = null;
            if (convertView == null) {
                holder = new SelectedImageHolder();
                convertView = View.inflate(parent.getContext(), R.layout.selected_image_item, null);

                int gridItemSpacing = (int) RuleUtils.convertDp2Px(parent.getContext(), 2);
                int gridEdgeLength = (RuleUtils.getScreenWidth(parent.getContext()) - gridItemSpacing * 2) / 3;

                AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams(gridEdgeLength, gridEdgeLength);
                convertView.setLayoutParams(layoutParams);
                holder.selectedImageView = (ImageView) convertView.findViewById(R.id.iv_selected_item);
                convertView.setTag(holder);

            } else {
                holder = (SelectedImageHolder) convertView.getTag();

            }

            ImageLoaderWrapper.DisplayOption displayOption = new ImageLoaderWrapper.DisplayOption();
            displayOption.loadingResId = R.mipmap.img_default;
            displayOption.loadErrorResId = R.mipmap.img_error;
            mImageLoaderWrapper.displayImage(holder.selectedImageView, mSelectedImageList.get(position), displayOption);

            return convertView;
        }
    }

    private static class SelectedImageHolder {
        ImageView selectedImageView;
    }
}
