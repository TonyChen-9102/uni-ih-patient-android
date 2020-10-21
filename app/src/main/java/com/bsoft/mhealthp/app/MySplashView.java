package com.bsoft.mhealthp.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.dcloud.feature.sdk.Interface.IDCUniMPAppSplashView;

public class MySplashView implements IDCUniMPAppSplashView {
    View splashView;

    @Override
    public View getSplashView(Context context, String appid) {
//        splashView = new FrameLayout(context);
//        splashView.setBackgroundColor(Color.WHITE);
        splashView = LayoutInflater.from(context).inflate(R.layout.activity_loading, null, false);
        return splashView;
    }

    @Override
    public void onCloseSplash(ViewGroup rootView) {
        if (rootView != null)
            rootView.removeView(splashView);
    }
}