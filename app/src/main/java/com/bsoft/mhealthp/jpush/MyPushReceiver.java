package com.bsoft.mhealthp.jpush;

import android.content.Context;

import cn.jpush.android.api.NotificationMessage;
import cn.jpush.android.service.JPushMessageReceiver;
import io.dcloud.feature.sdk.DCUniMPSDK;

public class MyPushReceiver extends JPushMessageReceiver {

    @Override
    public void onNotifyMessageOpened(Context context, NotificationMessage notificationMessage) {
        super.onNotifyMessageOpened(context, notificationMessage);
        //Log.i("MyPushReceiver", "MyPushReceiver;onNotifyMessageOpened=" + notificationMessage.notificationExtras);
        try {
            DCUniMPSDK.getInstance().sendUniMPEvent("navigateToPushNotifyPage", notificationMessage.notificationExtras);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onNotifyMessageArrived(Context context, NotificationMessage notificationMessage) {
        super.onNotifyMessageArrived(context, notificationMessage);
        // Log.i("MyPushReceiver", "MyPushReceiver;onNotifyMessageArrived=" + notificationMessage.notificationExtras);
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
