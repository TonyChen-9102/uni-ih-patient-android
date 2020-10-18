package com.bsoft.mhealthp.libs.net.post;

import android.content.Context;

import com.bsoft.mhealthp.libs.AppBase;
import com.bsoft.mhealthp.libs.net.init.ConstantsHttp;

import okhttp3.Interceptor;

public class PostManager extends NetPostUtil {

    private static class Holer {
        private static final PostManager INSTANCE = new PostManager(AppBase.getApplication(),
                ConstantsHttp.httpApiUrl);
    }

    public static PostManager getInstance() {
        return Holer.INSTANCE;
    }

    /**
     * 构造方法
     *
     * @param context      context
     * @param baseUrl      baseUrl
     * @param interceptors
     */
    public PostManager(Context context, String baseUrl, Interceptor... interceptors) {
        super(context, baseUrl, interceptors);
    }
}
