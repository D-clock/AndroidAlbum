package com.clock.album.activity;

import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.clock.album.R;

import java.io.File;
import java.io.FilenameFilter;
import java.util.LinkedList;
import java.util.List;

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

    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        if (viewId == R.id.btn_method_01) {
            //http://stackoverflow.com/questions/10381270/how-to-get-all-image-files-available-in-sdcard-in-android
        }
    }

    private LinkedList<String> findFiles() {
        LinkedList<String> fileList = new LinkedList<String>();
        String[] imageTypes = getResources().getStringArray(R.array.imageType);
        FilenameFilter[] filenameFilters = new FilenameFilter[imageTypes.length];
        for (int imageTypePos = 0; imageTypePos < imageTypes.length; imageTypePos++) {
            final String imageType = imageTypes[imageTypePos];
            filenameFilters[imageTypePos] = new FilenameFilter() {
                @Override
                public boolean accept(File dir, String filename) {
                    return filename.endsWith("." + imageType);
                }
            };
        }
        return fileList;
    }
}
