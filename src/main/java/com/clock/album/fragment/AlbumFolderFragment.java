package com.clock.album.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.clock.album.R;
import com.clock.album.adapter.AlbumFolderAdapter;
import com.clock.album.entity.ImageInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * 相册目录页面
 *
 * @author Clock
 * @since 2016-01-17
 */
public class AlbumFolderFragment extends Fragment implements AdapterView.OnItemClickListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private OnAlbumDetailInteractionListener mInteractionListener;
    /**
     * 相册目录列表
     */
    private ArrayList<String> mFolderList;
    /**
     * 每个文件夹下面的首张图片
     */
    private HashMap<String, ImageInfo> mFrontImageMap;
    private ListView mFolderListView;

    public AlbumFolderFragment() {
        // Required empty public constructor
    }

    /**
     * @param folderList    相册目录列表
     * @param frontImageMap 每个相册目录下的第一张图片
     * @return
     */
    public static AlbumFolderFragment newInstance(ArrayList<String> folderList, HashMap<String, ImageInfo> frontImageMap) {
        AlbumFolderFragment fragment = new AlbumFolderFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, folderList);
        args.putSerializable(ARG_PARAM2, frontImageMap);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mFolderList = (ArrayList<String>) getArguments().getSerializable(ARG_PARAM1);
            mFrontImageMap = (HashMap<String, ImageInfo>) getArguments().getSerializable(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_album_directory, container, false);
        mFolderListView = (ListView) rootView.findViewById(R.id.list_album);
        AlbumFolderAdapter albumFolderAdapter = new AlbumFolderAdapter(mFolderList, mFrontImageMap);
        mFolderListView.setAdapter(albumFolderAdapter);
        mFolderListView.setOnItemClickListener(this);
        return rootView;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnAlbumDetailInteractionListener) {
            mInteractionListener = (OnAlbumDetailInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
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
                String directory = mFolderList.get(position);
                mInteractionListener.switchAlbumFolder(directory);
                mInteractionListener.refreshFolderName(new File(directory).getName());
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
         * @param directory 指定相册的目录
         */
        public void switchAlbumFolder(String directory);

        /**
         * 刷新目录名
         *
         * @param directory
         */
        public void refreshFolderName(String directory);
    }
}
