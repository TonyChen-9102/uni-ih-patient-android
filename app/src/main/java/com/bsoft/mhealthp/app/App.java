package com.bsoft.mhealthp.app;

import android.util.Log;

import com.bsoft.mhealthp.app.jpush.JpushModule;
import com.bsoft.mhealthp.app.nativeModule.NativeEnvConfigModule;
import com.bsoft.mhealthp.libs.AppBase;
import com.bsoft.mhealthp.libs.net.init.ConstantsHttp;
import com.bsoft.mhealthp.libs.shapref.CoreSharpref;
import com.bsoft.mhealthp.paymodule.PayModule;
import com.demo.ck.videomodule.BsoftVideo;
import com.demo.ck.videomodule.PusherComponent;
import com.demo.ck.videomodule.VideoComponent;
import com.taobao.weex.WXSDKEngine;
import com.taobao.weex.common.WXException;

import cn.jpush.android.api.JPushInterface;
import io.dcloud.common.util.RuningAcitvityUtil;
import io.dcloud.feature.sdk.DCSDKInitConfig;
import io.dcloud.feature.sdk.DCUniMPSDK;

public class App extends AppBase {
    @Override
    public void onCreate() {
        //初始化环境
        ConstantsHttp.environment = BuildConfig.environment;
        super.onCreate();

        uniInit();

        if (!RuningAcitvityUtil.getAppName(getBaseContext()).contains("io.dcloud.unimp")) {
            JPushInterface.setDebugMode(CoreSharpref.getInstance().isEngineering());
            JPushInterface.init(this);
        }
    }

    private void uniInit() {
        //初始化 uni小程序SDK ----start----------
        DCSDKInitConfig config = new DCSDKInitConfig.Builder()
                .setCapsule(false)
                .setEnableBackground(true)
                .build();
        DCUniMPSDK.getInstance().initialize(this, config, new DCUniMPSDK.IDCUNIMPPreInitCallback() {
            @Override
            public void onInitFinished(boolean b) {
                Log.i("unimp", "onInitFinished----" + b);
            }
        });
        try {
            WXSDKEngine.registerModule("NativePaymentModule", PayModule.class);
            WXSDKEngine.registerModule("UserInfoSyncModule", JpushModule.class);
            WXSDKEngine.registerModule("NativeEnvConfigModule", NativeEnvConfigModule.class);
            WXSDKEngine.registerComponent("bsoftVideo", VideoComponent.class);
            WXSDKEngine.registerComponent("bsoftPush", PusherComponent.class);
//            WXSDKEngine.registerComponent("bsoftVideo", BsoftVideo.class);
        } catch (WXException e) {
            e.printStackTrace();
        }
        //初始化 uni小程序SDK ----end----------
    }
}
