package com.bsoft.mhealthp;

import android.app.Application;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import io.dcloud.feature.sdk.DCSDKInitConfig;
import io.dcloud.feature.sdk.DCUniMPSDK;
import io.dcloud.feature.sdk.MenuActionSheetItem;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        //初始化 uni小程序SDK ----start----------
        DCSDKInitConfig config = new DCSDKInitConfig.Builder()
                .setCapsule(false)
                .setEnableBackground(true)
                .build();
        DCUniMPSDK.getInstance().initialize(this, config, new DCUniMPSDK.IDCUNIMPPreInitCallback() {
            @Override
            public void onInitFinished(boolean b) {
                Log.i("unimp","onInitFinished----"+b);
            }
        });
        //初始化 uni小程序SDK ----end----------
    }
}
