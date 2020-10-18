package com.bsoft.mhealthp.libs;

import android.app.Application;

import com.bsoft.mhealthp.libs.log.LogUtil;
import com.bsoft.mhealthp.libs.net.init.NetEnvironmentUtil;

public class AppBase extends Application {
    private static Application application;
    //工程模式-log之类
    public static boolean isEngineering = true;
    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        LogUtil.init();
        NetEnvironmentUtil.initConstans(this);
    }

    public static Application getApplication() {
        return application;
    }
}
