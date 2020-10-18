package com.bsoft.mhealthp.libs.net.init;

import android.content.Context;
import android.content.res.AssetManager;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.bsoft.mhealthp.libs.shapref.CoreSharpref;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 83990 on 2018/2/8.
 */

public class NetEnvironmentUtil {

    /**
     * 初始化网络环境
     *
     * @param context
     * @return
     */
    public static boolean initConstans(Context context) {
        if (context == null) {
            throw new RuntimeException("context can't be null");
        }

        NetAddressVo vo = getCurEnvironment(context);
        if (vo != null) {
            ConstantsHttp.httpApiUrl = vo.getHttpApiUrl2();
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取网络环境参数List
     *
     * @param context
     * @return
     */
    public static ArrayList<NetAddressVo> getNetEnvironments(Context context) {
        if (context == null) {
            throw new RuntimeException("context can't be null");
        }
        AssetManager assetManager = context.getAssets();
        try {
            InputStream is = assetManager.open("wise_netConfigs");
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            StringBuffer stringBuffer = new StringBuffer();
            String str = null;
            while ((str = br.readLine()) != null) {
                stringBuffer.append(str);
            }

            if (!TextUtils.isEmpty(stringBuffer)) {
                ArrayList<NetAddressVo> arrayList = new ArrayList<>();
                List<NetAddressVo> list = JSON.parseArray(stringBuffer.toString(), NetAddressVo.class);
                if (list != null) {
                    arrayList.addAll(list);
                }
                return arrayList;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 获取当前网络环境参数
     *
     * @param context
     * @return
     */
    public static NetAddressVo getCurEnvironment(Context context) {
        if (context == null) {
            throw new RuntimeException("context can't be null");
        }

        ConstantsHttp.environment = CoreSharpref.getInstance().getNetEnviroment(ConstantsHttp.environment);

        ArrayList<NetAddressVo> vos = getNetEnvironments(context);
        if (vos != null) {
            for (NetAddressVo vo : vos) {
                if (TextUtils.equals(vo.getEnvironment(), ConstantsHttp.environment)) {
                    return vo;
                }
            }
        }

        return null;
    }

    /**
     * 设置当前网络环境
     *
     * @param context
     * @param enviroment
     * @return
     */
    public static boolean setEnvironment(Context context, String enviroment) {
        if (context == null) {
            throw new RuntimeException("context can't be null");
        }

        if (TextUtils.isEmpty(enviroment)) {
            return false;
        }

        ArrayList<NetAddressVo> vos = getNetEnvironments(context);
        if (vos != null) {
            for (NetAddressVo vo : vos) {
                if (TextUtils.equals(vo.getEnvironment(), enviroment)) {
                    CoreSharpref.getInstance().setNetEnviroment(enviroment);
                    return true;
                }
            }
        }

        return false;
    }
}
