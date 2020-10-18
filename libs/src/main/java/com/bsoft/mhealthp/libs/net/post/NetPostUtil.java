package com.bsoft.mhealthp.libs.net.post;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.util.ArrayMap;

import com.alibaba.fastjson.JSON;
import com.bsoft.mhealthp.libs.net.base.BaseObserver;
import com.bsoft.mhealthp.libs.net.base.CoreResponse;
import com.bsoft.mhealthp.libs.net.beans.NullResponse;
import com.bsoft.mhealthp.libs.net.interceptor.HeaderInterceptor;
import com.bsoft.mhealthp.libs.net.interceptor.NetLogInterceptor;
import com.bsoft.mhealthp.libs.net.utils.SSLTools;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ConnectionPool;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.fastjson.FastJsonConverterFactory;


/**
 *
 */

public class NetPostUtil {
    /*Default*/
    /*Util*/
    private Retrofit retrofit;

    /**
     * 构造方法
     *
     * @param context context
     * @param baseUrl baseUrl
     */
    public NetPostUtil(Context context, String baseUrl, Interceptor... interceptors) {
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder()
                .connectionPool(new ConnectionPool(8, 30, TimeUnit.SECONDS))
                .retryOnConnectionFailure(false)
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                //Head
                .addInterceptor(new HeaderInterceptor());

        if (interceptors != null) {
            for (Interceptor interceptor : interceptors) {
                okHttpClientBuilder.addInterceptor(interceptor);
            }
        }

        //Log
        okHttpClientBuilder.addInterceptor(new NetLogInterceptor());

        //ssl
        SSLTools.SSLParams sslParams = SSLTools.getSslSocketFactory(null, null, null);
        if (sslParams != null) {
            okHttpClientBuilder.sslSocketFactory(sslParams.sslSocketFactory, sslParams.x509TrustManager);
            okHttpClientBuilder.hostnameVerifier(new HostnameVerifier() {
                @SuppressLint("BadHostnameVerifier")
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
        }

        retrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(FastJsonConverterFactory.create())
                .client(okHttpClientBuilder.build())
                .baseUrl(baseUrl)
                .build();
    }

    /**
     * 动态代理创建 api
     *
     * @param api api 接口
     * @param <T> T
     * @return api 实体类
     */
    public <T> T create(Class<T> api) {
        return retrofit.create(api);
    }

    //********************************  post return null **********************************

    /**
     * baseUrl+url 或者 url
     * post 请求
     *
     * @param heads
     * @param body
     * @param observer
     * @param <T>
     */
    public <T> void post(String url, ArrayMap<String, String> heads, Object body,
                         BaseObserver<NullResponse> observer) {

        Observable observable = create(ApiService.class)
                .post(url, heads, body)
                .map(new Function<String, NullResponse>() {
                    @Override
                    public NullResponse apply(String s) {
                        NullResponse response = new NullResponse();
                        response.setCode(CoreResponse.SUCCESS);
                        return response;
                    }
                })
                .subscribeOn(Schedulers.io());

        observable.observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    //********************************  post **********************************

    /**
     * baseUrl+url 或者 url
     * post 请求
     *
     * @param heads
     * @param body
     * @param observer
     * @param <T>
     */
    public <T> void post(String url, ArrayMap<String, String> heads, Object body,
                         final Class<T> clazz, BaseObserver<T> observer) {
        Observable observable = create(ApiService.class)
                .post(url, heads, body)
                .map(new Function<String, CoreResponse<T>>() {
                    @Override
                    public CoreResponse<T> apply(String s) throws Exception {
                        return convert(s, clazz);
                    }
                })
                .subscribeOn(Schedulers.io());

        observable.observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    //******************************* postList *********************************************

    /**
     * baseUrl+url 或者 url
     * post list 请求
     *
     * @param heads
     * @param body
     * @param observer
     * @param <T>
     */
    public <T> void postList(String url, ArrayMap<String, String> heads, Object body,
                             final Class<T> clazz, BaseObserver<ArrayList<T>> observer) {
        Observable observable = create(ApiService.class)
                .post(url, heads, body)
                .map(new Function<String, CoreResponse<ArrayList<T>>>() {
                    @Override
                    public CoreResponse<ArrayList<T>> apply(String s) throws Exception {
                        return convertList(s, clazz);
                    }
                })
                .subscribeOn(Schedulers.io());

        observable.observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }


    private <T> CoreResponse<T> convert(String s, Class<T> clazz) {
        CoreResponse baseResponse = JSON.parseObject(s, CoreResponse.class);
        String detail = JSON.toJSONString(baseResponse.getDetails());
        baseResponse.setDetails(JSON.parseObject(detail, clazz));
        return baseResponse;
    }

    private <T> CoreResponse<ArrayList<T>> convertList(String s, Class<T> clazz) {
        CoreResponse baseResponse = JSON.parseObject(s, CoreResponse.class);
        String detail = JSON.toJSONString(baseResponse.getDetails());
        ArrayList<T> arrayList = new ArrayList<>();
        List<T> list = JSON.parseArray(detail, clazz);
        if (list != null) {
            arrayList.addAll(list);
        }
        baseResponse.setDetails(arrayList);
        return baseResponse;
    }
}
