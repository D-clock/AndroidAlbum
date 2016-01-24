package com.clock.album;

import android.app.Application;

import com.clock.album.imageloader.UniversalAndroidImageLoader;
import com.clock.utils.crash.CrashExceptionHandler;

/**
 * Created by Clock on 2016/1/17.
 */
public class AlbumApplication extends Application {

    private final static String APP_MAIN_FOLDER_NAME = "album";
    private final static String CRASH_FOLDER_NAME = "crash";

    @Override
    public void onCreate() {
        super.onCreate();

        configCollectCrashInfo();

        Thread.getDefaultUncaughtExceptionHandler();
        UniversalAndroidImageLoader.init(getApplicationContext());
    }

    /**
     * 配置奔溃信息的搜集
     */
    private void configCollectCrashInfo() {
        CrashExceptionHandler crashExceptionHandler = new CrashExceptionHandler(this, APP_MAIN_FOLDER_NAME, CRASH_FOLDER_NAME);
        //crashExceptionHandler.configRemoteReport(null);
        Thread.setDefaultUncaughtExceptionHandler(crashExceptionHandler);
    }
}
