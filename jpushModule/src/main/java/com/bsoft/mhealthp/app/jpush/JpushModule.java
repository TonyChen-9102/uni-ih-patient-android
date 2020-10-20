package com.bsoft.mhealthp.app.jpush;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.ArrayMap;
import android.util.Log;
import android.widget.Toast;

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

import cn.jpush.android.api.JPushInterface;
import io.reactivex.disposables.Disposable;

public class JpushModule extends WXModule {
    public static final int REQUEST_PERMISSION = 1;
    private String accessToken;
    private Activity activity;

    @JSMethod(uiThread = true)
    public void loginSyncInfo(JSONObject options, JSCallback callback) {
        //Log.d("JpushModule", "JpushModule;loginSyncInfo;options=" + options.toJSONString());
        if (options != null) {
            accessToken = options.getString("accessToken");
            if ((mWXSDKInstance.getContext() instanceof Activity)) {
                activity = (Activity) mWXSDKInstance.getContext();
                checkPermission(activity);
            }
        }
    }

    private void checkPermission(@NonNull Activity activity) {
        String[] permissions = new String[]{Manifest.permission.READ_PHONE_STATE};
        ArrayList<String> mPermissionList = new ArrayList<>();
        for (int i = 0; i < permissions.length; i++) {
            if (ContextCompat.checkSelfPermission(activity, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                mPermissionList.add(permissions[i]);//添加还未授予的权限
            }
        }

        if (mPermissionList.size() > 0) {//有权限没有通过，需要申请
            ActivityCompat.requestPermissions(activity, permissions, REQUEST_PERMISSION);
        } else {
            updateDeviceCall(mWXSDKInstance.getContext(), accessToken);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION) {
            if (permissions.length != 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Log.d("JpushModule", "JpushModule;onRequestPermissionsResult;权限获取失败");
                Toast.makeText(mWXSDKInstance.getContext(), "权限获取失败", Toast.LENGTH_LONG).show();
            } else {
                updateDeviceCall(mWXSDKInstance.getContext(), accessToken);
            }
        }
    }

    void updateDeviceCall(Context context, String accessToken) {
        ArrayMap<String, String> head = new ArrayMap<>();
        head.put(ConstantsHttp.Head_Id, ConstantsHttp.Device_Service);
        head.put(ConstantsHttp.Head_Method, "submitDevice");
        head.put(ConstantsHttp.Head_Token, accessToken);

        ArrayList body = new ArrayList();
        ArrayMap<String, String> map = new ArrayMap<String, String>();
        map.put("productCode", context.getString(R.string.tenantId)+".patient_android");
        map.put("appVersion", AppUtil.getVersionName(context));
        map.put("pushId", JPushInterface.getRegistrationID(context));
        map.put("platform", "ANDROID");
        map.put("manufacturer", DeviceUtil.getManufacturer());
        map.put("model", DeviceUtil.getPhoneModel());
        map.put("version", DeviceUtil.getAndroidVersion());
        map.put("uuid", JPushInterface.getUdid(context));
        map.put("imei", DeviceUtil.getImei(context));
        body.add(map);

        PostManager.getInstance().post("*.jsonRequest", head, body, String.class,
                new BaseObserver<String>() {
                    @Override
                    public void onHandlePrePare(Disposable disposable) {

                    }

                    @Override
                    protected void onHandleSuccess(String s) {
                        Log.d("JpushModule","updateDeviceCall;success");
                    }

                    @Override
                    protected void onHandleError(String s, String s1) {
                        Log.d("JpushModule","updateDeviceCall;error");
                    }

                    @Override
                    protected void onHandleComplete() {

                    }
                }
        );

    }
}
