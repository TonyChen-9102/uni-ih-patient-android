package com.bsoft.mhealthp.libs.utils;

import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.widget.Toast;

import com.bsoft.mhealthp.libs.AppBase;

/**
 * Created by chenkai on 2018/6/5.
 */

public class ToastUtil {
    public static void toast(String msg) {
        if (!TextUtils.isEmpty(msg))
            Toast.makeText(AppBase.getApplication(), msg, Toast.LENGTH_SHORT).show();
    }

    public static void toast(@StringRes int resId) {
        Toast.makeText(AppBase.getApplication(), AppBase.getApplication().getText(resId),
                Toast.LENGTH_SHORT).show();
    }
}
