package com.bsoft.mhealthp.libs.net.utils;

import android.net.ParseException;
import android.text.TextUtils;

import org.json.JSONException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import javax.net.ssl.SSLHandshakeException;

import retrofit2.HttpException;


/**
 * Created by chenkai on 2018/3/13.
 */

public class NetworkErrorUtil {
    //error
    public static final String ERROR_TYPE_UNKNOW = "-1";
    public static final String ERROR_TYPE_OFFLINE = "-2";
    public static final String ERROR_TYPE_UNCONNECT = "-3";
    public static final String ERROR_TYPE_TIMEOUT = "-4";
    public static final String ERROR_TYPE_SERVER = "-5";
    public static final String ERROR_TYPE_SSL = "-6";
    public static final String ERROR_PARSE = "-7";
    public static final String ERROR_FORBIDDEN = "-8";
    public static final String ERROR_TYPE_SHOW = "0";//必须显示的log的errorCode，服务端返回

    /**
     * 解析网络错误类型
     *
     * @param e
     * @return
     */
    public static ApiException getErrorTypeByThrow(Throwable e) {
        ApiException ex;
        if (e instanceof HttpException && TextUtils.equals("HTTP 403 Forbidden", e.getMessage())) {
            ex = new ApiException(e, ERROR_FORBIDDEN);
            ex.setMsg("token失效");
            return ex;
        } else if (e instanceof HttpException) {
            ex = new ApiException(e, ERROR_TYPE_UNCONNECT);
            ex.setMsg("服务端连接失败");
            return ex;
        } else if (e instanceof JSONException
                || e instanceof ParseException) {
            ex = new ApiException(e, ERROR_PARSE);
            ex.setMsg("解析错误");
            return ex;
        } else if (e instanceof ConnectException) {
            ex = new ApiException(e, ERROR_TYPE_OFFLINE);
            ex.setMsg("网络掉线");
            return ex;
        } else if (e instanceof SocketTimeoutException) {
            ex = new ApiException(e, ERROR_TYPE_TIMEOUT);
            ex.setMsg("请求超时");
            return ex;
        } else if (e instanceof SSLHandshakeException) {
            ex = new ApiException(e, ERROR_TYPE_SSL);
            ex.setMsg("ssl证书错误");
            return ex;
        } else if (e instanceof UnknownHostException) {
            ex = new ApiException(e, ERROR_TYPE_OFFLINE);
            ex.setMsg("网络掉线");
            return ex;
        } else {
            ex = new ApiException(e, ERROR_TYPE_UNKNOW);
            ex.setMsg("网络未知错误");
            return ex;
        }
    }
}
