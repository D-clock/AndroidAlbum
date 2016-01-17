package com.clock.album.adapter;

import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.clock.album.R;
import com.clock.album.entity.ImageInfo;
import com.clock.utils.text.StringUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.download.ImageDownloader;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * 相册目录适配器
 * Created by Clock on 2016/1/17.
 */
public class AlbumFolderAdapter extends BaseAdapter {

    private List<String> mFolderList;
    private Map<String, ImageInfo> mFrontImageMap;

    public AlbumFolderAdapter(List<String> folderList, Map<String, ImageInfo> frontImageMap) {
        this.mFolderList = folderList;
        this.mFrontImageMap = frontImageMap;
    }

    @Override
    public int getCount() {
        if (mFolderList == null) {
            return 0;
        }
        return mFolderList.size();
    }

    @Override
    public Object getItem(int position) {
        return mFolderList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = View.inflate(parent.getContext(), R.layout.album_directory_item, null);
            holder = new ViewHolder();
            holder.ivAlbumCover = (ImageView) convertView.findViewById(R.id.iv_album_cover);
            holder.tvDirectoryName = (TextView) convertView.findViewById(R.id.tv_directory_name);
            holder.tvChildCount = (TextView) convertView.findViewById(R.id.tv_child_count);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();

        }

        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.img_default)
                .showImageForEmptyUri(R.mipmap.img_error)
                .showImageOnFail(R.mipmap.img_error)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
        String folderName = mFolderList.get(position);
        String folderNameMd5 = StringUtils.md5(folderName);
        String imagePath = mFrontImageMap.get(folderNameMd5).getFile().getAbsolutePath();
        String uri = ImageDownloader.Scheme.FILE.wrap(imagePath);
        ImageLoader.getInstance().loadImage(uri, options, null);
        ImageLoader.getInstance().displayImage(uri, holder.ivAlbumCover, options);

        File directory = new File(folderName);
        holder.tvDirectoryName.setText(directory.getName());

        int childCount = directory.listFiles().length;
        holder.tvChildCount.setText(childCount + "");

        return convertView;
    }

    private static class ViewHolder {
        ImageView ivAlbumCover;
        TextView tvDirectoryName;
        TextView tvChildCount;
    }
}
