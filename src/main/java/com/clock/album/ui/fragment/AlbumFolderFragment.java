package com.clock.album.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.clock.album.R;
import com.clock.album.adapter.AlbumFolderAdapter;
import com.clock.album.entity.AlbumInfo;
import com.clock.album.imageloader.ImageLoaderFactory;
import com.clock.album.imageloader.ImageLoaderWrapper;
import com.clock.album.ui.fragment.base.BaseFragment;

import java.io.File;
import java.util.ArrayList;

/**
 * 相册目录页面
 *
 * @author Clock
 * @since 2016-01-17
 */
public class AlbumFolderFragment extends BaseFragment implements AdapterView.OnItemClickListener {

    private static final String ARG_PARAM1 = "param1";

    private OnAlbumDetailInteractionListener mInteractionListener;
    /**
     * 相册目录列表
     */
    private ArrayList<AlbumInfo> mAlbumInfoList;
    private ListView mFolderListView;

    public AlbumFolderFragment() {
        // Required empty public constructor
    }

    /**
     * @param albumInfoList 相册目录列表
     * @return
     */
    public static AlbumFolderFragment newInstance(ArrayList<AlbumInfo> albumInfoList) {
        AlbumFolderFragment fragment = new AlbumFolderFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, albumInfoList);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mAlbumInfoList = (ArrayList<AlbumInfo>) getArguments().getSerializable(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_album_directory, container, false);
        mFolderListView = (ListView) rootView.findViewById(R.id.list_album);
        ImageLoaderWrapper loaderWrapper = ImageLoaderFactory.getLoader(ImageLoaderFactory.UNIVERSAL_ANDROID_IMAGE_LOADER);
        AlbumFolderAdapter albumFolderAdapter = new AlbumFolderAdapter(mAlbumInfoList, loaderWrapper);
        mFolderListView.setAdapter(albumFolderAdapter);
        mFolderListView.setOnItemClickListener(this);
        return rootView;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnAlbumDetailInteractionListener) {
            mInteractionListener = (OnAlbumDetailInteractionListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mInteractionListener = null;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (parent == mFolderListView) {
            if (mInteractionListener != null) {
                AlbumInfo albumInfo = mAlbumInfoList.get(position);
                mInteractionListener.switchAlbumFolder(albumInfo.getFolder());
                mInteractionListener.refreshFolderName(albumInfo.getFolder().getName());
            }
        }
    }

    /**
     * 相册详情交互接口
     */
    public interface OnAlbumDetailInteractionListener {
        /**
         * 切换到相册详情页面
         *
         * @param albumFolder 指定相册的目录
         */
        public void switchAlbumFolder(File albumFolder);

        /**
         * 刷新目录名
         *
         * @param albumFolderName
         */
        public void refreshFolderName(String albumFolderName);
    }
}
