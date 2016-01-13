package com.clock.album.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.clock.album.R;

/**
 * 系统相册页面
 *
 * @author Clock
 * @since 2016-01-06
 */
public class SystemAlbumActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_album);

        findViewById(R.id.btn_get_path).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        if (viewId == R.id.btn_get_path) {
            //http://blog.csdn.net/xiaanming/article/details/18730223
            //http://developer.android.com/reference/android/provider/MediaStore.Images.Media.html
            //http://developer.android.com/reference/android/app/LoaderManager.html
            //http://developer.android.com/reference/android/content/ContentProvider.html

        }
    }

}
