package com.bsoft.mhealthp;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.bsoft.mhealthp.app.R;

import io.dcloud.feature.sdk.Interface.IDCUniMPAppSplashView;

public class MySplashView implements IDCUniMPAppSplashView {
    View splashView;
    @Override
    public View getSplashView(Context context, String appid) {
        splashView = LayoutInflater.from(context).inflate(R.layout.activity_loading, null, false);
        return splashView;
    }

    @Override
    public void onCloseSplash(ViewGroup rootView) {
        if(rootView != null)
            rootView.removeView(splashView);
    }
}
