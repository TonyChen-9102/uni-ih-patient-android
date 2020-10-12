package com.bsoft.mhealthp;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.demo.ck.uni_ih_patient_android.R;

import io.dcloud.feature.sdk.Interface.IDCUniMPAppSplashView;

public class MySplashView implements IDCUniMPAppSplashView {
    View splashView;
    @Override
    public View getSplashView(Context context, String appid) {
       // splashView = new FrameLayout(context);
//        splashView.setBackgroundColor(Color.WHITE);
//        TextView textView = new TextView(context);
//        textView.setText(appid);
//        textView.setTextColor(Color.WHITE);
//        textView.setTextSize(20);
//        textView.setGravity(Gravity.CENTER);
//        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 100);
//        lp.gravity = Gravity.CENTER;
//        splashView.addView(textView, lp);
        splashView = LayoutInflater.from(context).inflate(R.layout.activity_loading, null, false);
        return splashView;
    }

    @Override
    public void onCloseSplash(ViewGroup rootView) {
        if(rootView != null)
            rootView.removeView(splashView);
    }
}
