package com.clock.album.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.clock.album.R;
import com.clock.album.adapter.AlbumGridAdapter;

import java.io.File;
import java.util.ArrayList;

/**
 * 相册详情页面
 *
 * @author Clock
 * @since 2016-01-17
 */
public class AlbumDetailFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";

    /**
     * 相册信息列表
     */
    private ArrayList<File> mImageFileList;
    /**
     * 相册视图控件
     */
    private GridView mAlbumGridView;

    /**
     * @param imageFileList 相册列表
     * @return
     */
    public static AlbumDetailFragment newInstance(ArrayList<File> imageFileList) {
        AlbumDetailFragment fragment = new AlbumDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, imageFileList);
        fragment.setArguments(args);
        return fragment;
    }

    public AlbumDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mImageFileList = (ArrayList<File>) getArguments().getSerializable(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_album_detail, container, false);
        mAlbumGridView = (GridView) rootView.findViewById(R.id.gv_album);
        AlbumGridAdapter albumGridAdapter = new AlbumGridAdapter(mImageFileList);
        mAlbumGridView.setAdapter(albumGridAdapter);
        return rootView;
    }

}
