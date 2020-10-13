package com.bsoft.mhealthp.paymodule.ali;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.alipay.sdk.app.PayTask;
import com.bsoft.mhealthp.paymodule.PayResult;

import java.util.Map;

public class AlipayUtil {
    /*Util*/
    private Activity activity;
    private PayResult payResultListener;

    public AlipayUtil(@NonNull Activity activity, @NonNull PayResult payResultListener) {
        this.payResultListener = payResultListener;
        this.activity = activity;
    }

    public void goAlipay(final String payInfo) {
        if (TextUtils.isEmpty(payInfo)) {
            Log.d("PayModule", "AlipayUtil;goAlipay;支付参数错误");
            payResultListener.backResult("fail");
        }
        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask(activity);
                // 调用支付接口，获取支付结果
                Map<String, String> result = alipay.payV2(payInfo, true);
                Message msg = new Message();
                msg.what = 1;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };
        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    //支付宝支付handler返回
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1: {
                    AliPayResult payResult = new AliPayResult((Map<String, String>) msg.obj);
                    /**
                     * 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
                     * detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
                     * docType=1) 建议商户依赖异步通知
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    if (TextUtils.equals(resultStatus, "9000")) {
                        payResultListener.backResult("success");
                    } else if (TextUtils.equals(resultStatus, "6001")) {
                        payResultListener.backResult("cancel");
                    } else {
                        payResultListener.backResult("error");
                    }
                    break;
                }
                default:
                    payResultListener.backResult("error");
                    break;
            }
        }
    };


}
