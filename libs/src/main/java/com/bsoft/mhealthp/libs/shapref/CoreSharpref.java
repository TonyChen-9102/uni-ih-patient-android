package com.bsoft.mhealthp.libs.shapref;

import android.content.Context;
import android.support.annotation.NonNull;

import com.bsoft.mhealthp.libs.AppBase;

/**
 * Created by kai.chen on 2017/7/3.
 * one SHARED_NAME,one manager
 */

public class CoreSharpref {
    /*Default*/
    private static final int SHARED_MODE = Context.MODE_PRIVATE;
    private static final String SHARED_NAME = "share_core";

    //工程模式
    private static final String KEY_ENGINEERING = "engineering";
    //环境
    public static final String KEY_HTTP_ENVIROMENT = "enviroment";

    /*Util*/
    private Context context;
    /*Flag*/
    private volatile static CoreSharpref instance;

    public static CoreSharpref getInstance() {
        if (instance == null) {
            synchronized (CoreSharpref.class) {
                if (instance == null) {
                    instance = new CoreSharpref(AppBase.getApplication());
                }
            }
        }
        return instance;
    }

    private CoreSharpref(@NonNull Context context) {
        this.context = context.getApplicationContext();
    }

    /**
     * @return
     */
    public boolean isEngineering() {
        synchronized (this) {
            return context
                    .getSharedPreferences(SHARED_NAME, SHARED_MODE)
                    .getBoolean(KEY_ENGINEERING, AppBase.isEngineering);
        }
    }

    public boolean setEngineering(boolean debug) {
        synchronized (this) {
            return context
                    .getSharedPreferences(SHARED_NAME, SHARED_MODE)
                    .edit()
                    .putBoolean(KEY_ENGINEERING, debug)
                    .commit();
        }
    }

    /**
     * 获取当前网络环境
     *
     * @return
     */
    public String getNetEnviroment(String def) {
        synchronized (this) {
            return context
                    .getSharedPreferences(SHARED_NAME, SHARED_MODE)
                    .getString(KEY_HTTP_ENVIROMENT, def);
        }
    }

    /**
     * 设置当前网络环境
     *
     * @param enviroment
     * @return
     */
    public boolean setNetEnviroment(String enviroment) {
        synchronized (this) {
            return context
                    .getSharedPreferences(SHARED_NAME, SHARED_MODE)
                    .edit()
                    .putString(KEY_HTTP_ENVIROMENT, enviroment)
                    .commit();
        }
    }
}
