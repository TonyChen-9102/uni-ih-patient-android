package com.bsoft.mhealthp.libs.net.interceptor;


import android.text.TextUtils;

import com.bsoft.mhealthp.libs.AppBase;
import com.bsoft.mhealthp.libs.R;
import com.bsoft.mhealthp.libs.net.utils.HeadUtil;

import java.io.IOException;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by kai.chen on 2017/6/24.
 * 统一添加head
 */

public class HeaderInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Request.Builder requestBuilder = request.newBuilder();
        Headers headers = request.headers();
        if (headers != null) {
            String digest = HeadUtil.getSignature(request);
            if (!TextUtils.isEmpty(digest)) {
                requestBuilder.addHeader("X-Signature", digest);
            }
        }
        if (headers == null || (!headers.names().contains("B-Product-Code"))) {
            requestBuilder.addHeader("B-Product-Code", AppBase.getApplication().getString(R.string.tenantId)+".patient_android");
        }
        requestBuilder.addHeader("User-Agent", HeadUtil.getUserAgent());

        request = requestBuilder.build();
        //********************Response*************************
        Response response = chain.proceed(request);
        return response;
    }


}
