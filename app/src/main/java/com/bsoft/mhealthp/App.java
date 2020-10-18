package com.bsoft.mhealthp;

import android.app.Application;
import android.util.Log;

import com.bsoft.mhealthp.jpush.JpushModule;
import com.bsoft.mhealthp.libs.AppBase;
import com.bsoft.mhealthp.paymodule.PayModule;
import com.taobao.weex.WXSDKEngine;
import com.taobao.weex.common.WXException;

import io.dcloud.feature.sdk.DCSDKInitConfig;
import io.dcloud.feature.sdk.DCUniMPSDK;

public class App extends AppBase {
    @Override
    public void onCreate() {
        super.onCreate();
        uniInit();
    }

    private void uniInit(){
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
        try {
            WXSDKEngine.registerModule("NativePaymentModule", PayModule.class);
            WXSDKEngine.registerModule("UserInfoSyncModule", JpushModule.class);
        } catch (WXException e) {
            e.printStackTrace();
        }
        //初始化 uni小程序SDK ----end----------
    }
}
