package com.bsoft.mhealthp.app.nativeModule;

import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.bsoft.mhealthp.app.R;
import com.bsoft.mhealthp.libs.AppBase;
import com.bsoft.mhealthp.libs.log.LogUtil;
import com.bsoft.mhealthp.libs.net.init.ConstantsHttp;
import com.bsoft.mhealthp.libs.utils.AppUtil;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.common.WXModule;

public class NativeEnvConfigModule extends WXModule {

    @JSMethod(uiThread = false)
    public JSONObject getEnvInfoSync() {
        JSONObject data = new JSONObject();
        data.put("tenantId", AppBase.getApplication().getString(R.string.tenantId));
        data.put("appEnv", ConstantsHttp.environment);
        data.put("packageName", AppUtil.getPackageName(AppBase.getApplication()));
        LogUtil.d("NativeEnvConfigModule;getEnvInfoSync="+data.toJSONString());
        return data;
    }
}
