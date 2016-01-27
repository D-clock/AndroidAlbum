package com.clock.album;

import android.app.Application;

import com.clock.album.crash.CustomCrashReporter;
import com.clock.album.imageloader.UniversalAndroidImageLoader;
import com.clock.utils.crash.CrashExceptionHandler;
import com.tencent.bugly.crashreport.CrashReport;

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

        //此处用于配置本地生成闪退的日志文件，需要在其他第三方上报crash log类型的sdk初始化之前，
        // 进行初始化。否则会导致第三方的SDK无法上报crash log
        configCollectCrashInfo();

        initBuglyConfig();

        UniversalAndroidImageLoader.init(getApplicationContext());

    }

    /**
     * 配置奔溃信息的搜集
     */
    private void configCollectCrashInfo() {
        CrashExceptionHandler crashExceptionHandler = new CrashExceptionHandler(this, APP_MAIN_FOLDER_NAME, CRASH_FOLDER_NAME);
        CrashExceptionHandler.CrashExceptionRemoteReport remoteReport = new CustomCrashReporter();
        crashExceptionHandler.configRemoteReport(remoteReport); //设置友盟统计报错日志回传到远程服务器上
        Thread.setDefaultUncaughtExceptionHandler(crashExceptionHandler);
    }

    /**
     * 初始化bugly的设置
     */
    private void initBuglyConfig() {
        CrashReport.initCrashReport(getApplicationContext(), "900019014", false);
    }
}
