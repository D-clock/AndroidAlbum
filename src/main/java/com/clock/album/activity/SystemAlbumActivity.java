package com.clock.album.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

        findViewById(R.id.btn_method_01).setOnClickListener(this);
        findViewById(R.id.btn_method_02).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        if (viewId == R.id.btn_method_01) {
            //http://stackoverflow.com/questions/10381270/how-to-get-all-image-files-available-in-sdcard-in-android
        } else if (viewId == R.id.btn_method_02) {
            //http://stackoverflow.com/questions/11481784/how-to-get-all-images-and-photos-from-my-android-device-not-from-sdcard
            //http://stackoverflow.com/questions/8737054/how-to-get-path-by-mediastore-images-media
            //http://www.stormzhang.com/android/2014/07/24/android-save-image-to-gallery/
        }
    }
}
