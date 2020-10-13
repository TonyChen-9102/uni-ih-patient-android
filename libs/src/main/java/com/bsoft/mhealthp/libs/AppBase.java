package com.bsoft.mhealthp.libs;

import android.app.Application;

public class AppBase extends Application {
    private static Application application;
    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
    }

    public static Application getApplication() {
        return application;
    }
}
