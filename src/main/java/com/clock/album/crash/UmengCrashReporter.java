package com.clock.album.crash;

import android.content.Context;

import com.clock.utils.crash.CrashExceptionHandler;

/**
 * 利用友盟SDK将闪退日志发送回后台服务器
 * <p/>
 * Created by Clock on 2016/1/27.
 */
@Deprecated
public class UmengCrashReporter implements CrashExceptionHandler.CrashExceptionRemoteReport {

    private Context mContext;

    public UmengCrashReporter(Context context) {
        this.mContext = context;
    }

    @Override
    public void onCrash(Throwable ex) {
        //MobclickAgent.reportError(mContext, ex);
    }

}
