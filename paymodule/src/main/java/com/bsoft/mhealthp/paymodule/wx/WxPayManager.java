package com.bsoft.mhealthp.paymodule.wx;

import android.app.Activity;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.bsoft.mhealthp.paymodule.PayResult;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.json.JSONObject;


public class WxPayManager {
    /*Util*/
    private PayResult payResultListener;
    private Handler handler = new Handler();
    /*Flag*/
    public static String weixinAppId = "";
    private boolean localWeixinEnable;

    private static class SingletonHolder {
        private static final WxPayManager INSTANCE = new WxPayManager();
    }

    private WxPayManager() {

    }

    public static final WxPayManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void onActivityResume() {
        //处理微信支付过程中后台切回来
        if (localWeixinEnable) {
            handler.removeCallbacks(resumedRun);
            handler.postDelayed(resumedRun, 500);
        }
    }

    private Runnable resumedRun = new Runnable() {
        @Override
        public void run() {
            if (!localWeixinEnable) {
                return;
            }

            localWeixinEnable = false;
            if (payResultListener != null) {
                payResultListener.backResult("fail");
            }
        }
    };

    public void goWechatPay(@NonNull Activity activity, PayResult payResultListener, String payInfo) {
        this.payResultListener = payResultListener;

        if (TextUtils.isEmpty(payInfo)) {
            Log.d("PayModule", "WeixinPayUtil;goWechatPay;支付参数错误");
            if (payResultListener != null) {
                payResultListener.backResult("fail");
            }
        }

        try {
            JSONObject ob = new JSONObject(payInfo);
            PayReq req = new PayReq();
            req.appId = ob.getString("appid");
            weixinAppId = req.appId;
            req.partnerId = ob.getString("partnerid");
            req.packageValue = ob.getString("package");
            req.timeStamp = ob.getString("timestamp");
            req.sign = ob.getString("sign");
            req.nonceStr = ob.getString("noncestr");
            req.prepayId = ob.getString("prepayid");
            if (ob.has("extData")) {
                req.extData = ob.getString("extData");
            }
            IWXAPI wxApi = WXAPIFactory.createWXAPI(activity, null);
            wxApi.registerApp(req.appId);
            if (!wxApi.isWXAppInstalled()) {
                Log.d("PayModule", "WeixinPayUtil;goWechatPay;没有安装微信");
                Toast.makeText(activity, "没有安装微信", Toast.LENGTH_LONG).show();
                if (payResultListener != null) {
                    payResultListener.backResult("fail");
                }
                return;
            } else {
                localWeixinEnable = true;
                wxApi.sendReq(req);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("PayModule", "WeixinPayUtil;goWechatPay;支付参数错误");
            if (payResultListener != null) {
                payResultListener.backResult("fail");
            }
        }
    }

    public void event(BaseResp baseResp) {
        if (!localWeixinEnable) {
            return;
        }
        //appid 初始化
        handler.removeCallbacks(resumedRun);
        weixinAppId = "";
        localWeixinEnable = false;

        if (payResultListener != null) {
            if (baseResp.errCode == 0) {
                payResultListener.backResult("success");
            } else if (baseResp.errCode == -2) {
                payResultListener.backResult("cancel");
            } else {
                payResultListener.backResult("fail");
            }
        }
    }


}
