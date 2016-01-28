package com.clock.album.crash;

import com.clock.utils.crash.CrashExceptionHandler;

/**
 * 自定义的回传闪退日志到远程服务器
 * <p/>
 * Created by Clock on 2016/1/27.
 */
public class SimpleCrashReporter implements CrashExceptionHandler.CrashExceptionRemoteReport {

    @Override
    public void onCrash(Throwable ex) {
        //接下来要在此处加入将闪退日志回传到服务器的功能
    }

}
