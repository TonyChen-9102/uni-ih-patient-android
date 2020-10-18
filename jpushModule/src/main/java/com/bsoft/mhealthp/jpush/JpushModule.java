package com.bsoft.mhealthp.jpush;

import android.content.Context;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.bsoft.mhealthp.libs.net.base.BaseObserver;
import com.bsoft.mhealthp.libs.net.init.ConstantsHttp;
import com.bsoft.mhealthp.libs.net.post.PostManager;
import com.bsoft.mhealthp.libs.utils.AppUtil;
import com.bsoft.mhealthp.libs.utils.DeviceUtil;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.bridge.JSCallback;
import com.taobao.weex.common.WXModule;

import java.util.ArrayList;

import io.reactivex.disposables.Disposable;

public class JpushModule extends WXModule {

    @JSMethod(uiThread = true)
    public void loginSyncInfo(JSONObject options, JSCallback callback) {
        Log.d("PayModule", "PayModule;loginSyncInfo;options=" + options.toJSONString());
        updateDeviceCall(mWXSDKInstance.getContext(),options.getString("accessToken"));
    }

    void updateDeviceCall(Context context, String accessToken) {
//        String registrationID = JPushInterface.getRegistrationID(getApplicationContext());
        String registrationID = "";
        if (TextUtils.isEmpty(registrationID)) {
            //return;
        }

        ArrayMap<String, String> head = new ArrayMap<>();
        head.put(ConstantsHttp.Head_Id, ConstantsHttp.Device_Service);
        head.put(ConstantsHttp.Head_Method, "submitDevice");
        head.put(ConstantsHttp.Head_Token, accessToken);

        ArrayList body = new ArrayList();
        ArrayMap<String, String> map = new ArrayMap<String, String>();
        //TODO chenkaid 多项目配置需要修改
        map.put("productCode", "hcn.fs-nhqrmyy.patient_android");
        map.put("appVersion", AppUtil.getVersionName(context));
        map.put("pushId", registrationID);
        map.put("platform", "ANDROID");
        map.put("manufacturer", DeviceUtil.getManufacturer());
        map.put("model", DeviceUtil.getPhoneModel());
        map.put("version", DeviceUtil.getAndroidVersion());
//        map.put("uuid", JPushInterface.getUdid(context));
        map.put("imei", DeviceUtil.getImei(context));
        body.add(map);

        PostManager.getInstance().post("*.jsonRequest", head, body, String.class,
                new BaseObserver<String>() {
                    @Override
                    public void onHandlePrePare(Disposable disposable) {

                    }

                    @Override
                    protected void onHandleSuccess(String s) {

                    }

                    @Override
                    protected void onHandleError(String s, String s1) {

                    }

                    @Override
                    protected void onHandleComplete() {

                    }
                }
        );

    }
}
