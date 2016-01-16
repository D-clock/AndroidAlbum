package com.clock.album.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.clock.album.R;
import com.clock.utils.common.RuleUtils;

/**
 * 相册视图适配器
 * Created by Clock on 2016/1/16.
 */
public class AlbumGridAdapter extends BaseAdapter {
    @Override
    public int getCount() {
        return 20;
    }

    @Override
    public Object getItem(int position) {
        return null;
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
            int gridEdgeLength = RuleUtils.getScreenWidth(parent.getContext()) / 3;//以屏幕宽度的三分之一作为相册item大小
            AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams(gridEdgeLength, gridEdgeLength);
            convertView.setLayoutParams(layoutParams);
            holder.albumItem = (ImageView) convertView.findViewById(R.id.iv_album_item);
            convertView.setTag(holder);

        } else {
            holder = (AlbumViewHolder) convertView.getTag();

        }

        return convertView;
    }

    private static class AlbumViewHolder {
        /**
         * 显示图片的位置
         */
        ImageView albumItem;
    }
}
