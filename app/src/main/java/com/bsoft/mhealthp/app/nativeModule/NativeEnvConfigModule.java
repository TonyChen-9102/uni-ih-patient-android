package com.bsoft.mhealthp.app.nativeModule;

import com.alibaba.fastjson.JSONObject;
import com.bsoft.mhealthp.app.R;
import com.bsoft.mhealthp.libs.AppBase;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.bridge.JSCallback;
import com.taobao.weex.common.WXModule;

public class NativeEnvConfigModule extends WXModule {

    @JSMethod(uiThread = true)
    public void getEnvInfoSync(JSONObject options, JSCallback callback) {
        if (callback != null) {
            JSONObject data = new JSONObject();
            data.put("tenantId", AppBase.getApplication().getString(R.string.tenantId));
            callback.invoke(data);
        }
    }
}
