package com.clock.album.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.clock.album.R;
import com.clock.album.adapter.AlbumGridAdapter;
import com.clock.album.entity.ImageInfo;
import com.clock.album.imageloader.ImageLoaderFactory;
import com.clock.album.imageloader.ImageLoaderWrapper;

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
     * 图片选择交互接口
     */
    private OnImageSelectedInteractionListener mOnImageSelectedInteractionListener;
    /**
     * 相册信息列表
     */
    private ArrayList<ImageInfo> mImageInfoList;
    /**
     * 相册视图控件
     */
    private GridView mAlbumGridView;

    /**
     * @param imageInfoList 相册列表
     * @return
     */
    public static AlbumDetailFragment newInstance(ArrayList<ImageInfo> imageInfoList) {
        AlbumDetailFragment fragment = new AlbumDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, imageInfoList);
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
            mImageInfoList = (ArrayList<ImageInfo>) getArguments().getSerializable(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_album_detail, container, false);
        mAlbumGridView = (GridView) rootView.findViewById(R.id.gv_album);
        ImageLoaderWrapper loaderWrapper = ImageLoaderFactory.getLoader(ImageLoaderFactory.UNIVERSAL_ANDROID_IMAGE_LOADER);
        AlbumGridAdapter albumGridAdapter = new AlbumGridAdapter(mImageInfoList, loaderWrapper, mOnImageSelectedInteractionListener);
        mAlbumGridView.setAdapter(albumGridAdapter);
        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (getActivity() instanceof OnImageSelectedInteractionListener) {
            mOnImageSelectedInteractionListener = (OnImageSelectedInteractionListener) getActivity();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mOnImageSelectedInteractionListener = null;
    }

    /**
     * 图片选择交互交互接口
     */
    public interface OnImageSelectedInteractionListener {

        /**
         * 选中一张图片
         *
         * @param imageFile 被选中的图片
         */
        public void onSelected(File imageFile);

        /**
         * 取消选中的图片
         *
         * @param imageFile 被取消选择的图片
         */
        public void onUnSelected(File imageFile);

    }
}
