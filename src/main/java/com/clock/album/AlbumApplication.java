package com.clock.album;

import android.app.Application;

import com.clock.album.imageloader.UniversalAndroidImageLoader;
import com.clock.utils.crash.CrashExceptionHandler;

/**
 * Created by Clock on 2016/1/17.
 */
public class AlbumApplication extends Application {

    /**
     * app在sd卡的主目录
     */
    private final static String APP_MAIN_FOLDER_NAME = "album";
    /**
     * 本地存放闪退日志的目录
     */
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
        crashExceptionHandler.configRemoteReport(new CrashExceptionHandler.CrashExceptionRemoteReport() {
            @Override
            public void onCrash(Throwable ex) {

            }
        }); //设置报错日志回传到远程服务器上
        Thread.setDefaultUncaughtExceptionHandler(crashExceptionHandler);
    }
}
