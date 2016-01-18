package com.clock.album.adapter;

import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.clock.album.R;
import com.clock.album.imageloader.ImageLoaderWrapper;
import com.clock.utils.common.RuleUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.download.ImageDownloader;

import java.io.File;
import java.util.List;

/**
 * 相册视图适配器
 * <p/>
 * Created by Clock on 2016/1/16.
 */
public class AlbumGridAdapter extends BaseAdapter {

    private List<File> mImageFileList;
    private ImageLoaderWrapper mImageLoaderWrapper;

    public AlbumGridAdapter(List<File> imageFileList, ImageLoaderWrapper imageLoaderWrapper) {
        this.mImageFileList = imageFileList;
        this.mImageLoaderWrapper = imageLoaderWrapper;
    }

    @Override
    public int getCount() {
        if (mImageFileList == null) {
            return 0;
        }
        return mImageFileList.size();
    }

    @Override
    public Object getItem(int position) {
        return mImageFileList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        AlbumViewHolder holder = null;
        if (convertView == null) {
            holder = new AlbumViewHolder();
            convertView = View.inflate(parent.getContext(), R.layout.album_grid_item, null);

            int gridItemSpacing = (int) RuleUtils.convertDp2Px(parent.getContext(), 2);
            int gridEdgeLength = (RuleUtils.getScreenWidth(parent.getContext()) - gridItemSpacing * 2) / 3;

            AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams(gridEdgeLength, gridEdgeLength);
            convertView.setLayoutParams(layoutParams);
            holder.albumItem = (ImageView) convertView.findViewById(R.id.iv_album_item);
            convertView.setTag(holder);

        } else {
            holder = (AlbumViewHolder) convertView.getTag();

        }
        File imageFile = mImageFileList.get(position);
        ImageLoaderWrapper.DisplayOption displayOption = new ImageLoaderWrapper.DisplayOption();
        displayOption.loadingResId = R.mipmap.img_default;
        displayOption.loadErrorResId = R.mipmap.img_error;
        mImageLoaderWrapper.displayImage(holder.albumItem, imageFile, displayOption);
        return convertView;
    }

    private static class AlbumViewHolder {
        /**
         * 显示图片的位置
         */
        ImageView albumItem;
    }
}
