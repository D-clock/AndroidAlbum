package com.clock.album;

import android.app.Application;

import com.clock.album.imageloader.UniversalAndroidImageLoader;

/**
 * Created by Clock on 2016/1/17.
 */
public class AlbumApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        UniversalAndroidImageLoader.init(getApplicationContext());
    }


}
