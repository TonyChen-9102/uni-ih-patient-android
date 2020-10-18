package com.bsoft.mhealthp.paymodule;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Debug;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;

import com.bsoft.mhealthp.paymodule.ali.AlipayUtil;
import com.bsoft.mhealthp.paymodule.wx.WxPayManager;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.bridge.JSCallback;
import com.taobao.weex.common.WXModule;

import java.util.ArrayList;

public class PayModule extends WXModule {
    public static final int REQUEST_STORAGE_PERMISSION = 1;
    private PayResult payResult;
    private AlipayUtil alipayUtil;
    private String mode = "";//ali wx
    private String orderInfo;
    private Activity activity;


    @JSMethod(uiThread = true)
    public void startAliPay(String options, JSCallback callback) {
        //Log.d("PayModule", "PayModule;startAliPay;options=" + options);
        this.orderInfo = options;
        mode = "ali";
        payResult = new PayResult(callback);
        if (!(mWXSDKInstance.getContext() instanceof Activity)) {
            Log.d("PayModule", "PayModule;startAliPay;context error");
            payResult.backResult("fail");
            return;
        }
        activity = (Activity) mWXSDKInstance.getContext();
        alipayUtil = new AlipayUtil(activity, payResult);

        checkPermission(activity);
    }

    @JSMethod(uiThread = true)
    public void startWxPay(String options, JSCallback callback) {
        //Log.d("PayModule", "PayModule;startWxPay;options=" + options);
        this.orderInfo = options;
        mode = "wx";
        payResult = new PayResult(callback);
        if (!(mWXSDKInstance.getContext() instanceof Activity)) {
            Log.d("PayModule", "PayModule;startAliPay;context error");
            payResult.backResult("fail");
            return;
        }
        activity = (Activity) mWXSDKInstance.getContext();
        checkPermission(activity);
    }

    private void pay() {
        if (TextUtils.equals(mode, "ali")) {
            alipayUtil.goAlipay(orderInfo);
        } else if (TextUtils.equals(mode, "wx")) {
            WxPayManager.getInstance().goWechatPay(activity, payResult, orderInfo);
        }
    }

    @Override
    public void onActivityResume() {
        super.onActivityResume();
        WxPayManager.getInstance().onActivityResume();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_STORAGE_PERMISSION) {
            if (permissions.length != 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Log.d("PayModule", "PayModule;onRequestPermissionsResult;权限获取失败");
                payResult.backResult("fail");
            } else {
                pay();
            }
        }

    }

    private void checkPermission(@NonNull Activity activity) {
        String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_PHONE_STATE};
        ArrayList<String> mPermissionList = new ArrayList<>();
        for (int i = 0; i < permissions.length; i++) {
            if (ContextCompat.checkSelfPermission(activity, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                mPermissionList.add(permissions[i]);//添加还未授予的权限
            }
        }

        if (mPermissionList.size() > 0) {//有权限没有通过，需要申请
            ActivityCompat.requestPermissions(activity, permissions, REQUEST_STORAGE_PERMISSION);
        } else {
            pay();
        }
    }

}

