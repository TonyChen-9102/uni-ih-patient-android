package com.bsoft.mhealthp.app.jpush;

import android.content.Context;

import com.alibaba.fastjson.JSONObject;
import com.bsoft.mhealthp.libs.log.LogUtil;

import cn.jpush.android.api.NotificationMessage;
import cn.jpush.android.service.JPushMessageReceiver;
import io.dcloud.feature.sdk.DCUniMPSDK;

public class MyPushReceiver extends JPushMessageReceiver {

    @Override
    public void onNotifyMessageArrived(Context context, NotificationMessage notificationMessage) {
        super.onNotifyMessageArrived(context, notificationMessage);
        // Log.i("MyPushReceiver", "MyPushReceiver;onNotifyMessageArrived=" + notificationMessage.notificationExtras);
    }

    @Override
    public void onNotifyMessageOpened(Context context, NotificationMessage notificationMessage) {
        super.onNotifyMessageOpened(context, notificationMessage);
        LogUtil.i("MyPushReceiver", "MyPushReceiver;onNotifyMessageOpened=" + notificationMessage.notificationExtras);
        try {
            JSONObject jsonObject = (JSONObject) JSONObject.parse(notificationMessage.notificationExtras);
            DCUniMPSDK.getInstance().sendUniMPEvent("navigateToPushNotifyPage", jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRegister(Context context, String s) {
        super.onRegister(context, s);
    }

    @Override
    public void onConnected(Context context, boolean b) {
        super.onConnected(context, b);
    }
}
