package com.bsoft.mhealthp.libs.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;

import static android.content.Context.TELEPHONY_SERVICE;

/**
 * Created by chenkai on 2018/3/12.
 */

public class DeviceUtil {

    /**
     * 厂商
     *
     * @return
     */
    public static String getManufacturer() {
        return Build.MANUFACTURER;
    }

    /**
     * 手机型号
     *
     * @return
     */
    public static String getPhoneModel() {
        return Build.MODEL;
    }

    /**
     * Android 版本
     *
     * @return
     */
    public static String getAndroidVersion() {
        return Build.VERSION.RELEASE;
    }

    /**
     * IMEI
     *
     * @param context
     * @return
     */
    @SuppressLint("HardwareIds")
    public static String getImei(@NonNull Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            Log.e("DeviceUtil", "DeviceUtil;getImei;Don't have permission READ_PHONE_STATE");
            return null;
        }
        TelephonyManager telephonyMgr = (TelephonyManager) context.getSystemService(TELEPHONY_SERVICE);
        return telephonyMgr != null ? telephonyMgr.getDeviceId() : null;
    }

    /**
     * 获取屏幕宽度
     *
     * @param context
     * @return
     */
    public static int getScreenWidth(@NonNull Context context) {
        Resources resources = context.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        return dm.widthPixels;
    }

    /**
     * 获取屏幕高度
     *
     * @param context
     * @return
     */
    public static int getScreenHeight(@NonNull Context context) {
        Resources resources = context.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        return dm.heightPixels;
    }

    /**
     * 获取密度
     *
     * @param context
     * @return
     */
    public static float getScreenDensity(@NonNull Context context) {
        Resources resources = context.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        return dm.density;
    }

    /**
     * 获取IMSI
     *
     * @param context
     * @return
     */
    public static String getOperators(@NonNull Context context) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            Log.e("DeviceUtil", "DeviceUtil;getOperators;Don't have permission READ_PHONE_STATE");
            return null;
        }
        TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        @SuppressLint("HardwareIds") String IMSI = tm != null ? tm.getSubscriberId() : null;
        return IMSI;
    }

    /**
     * 获取version code
     *
     * @param context
     * @return
     */
    public static int getAppVersionCode(@NonNull Context context) {
        int versionCode = 0;
        try {
            versionCode = context.getPackageManager().
                    getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    /**
     * 获取version name
     *
     * @param context
     * @return
     */
    public static String getAppVersionName(@NonNull Context context) {
        String verName = "";
        try {
            verName = context.getPackageManager().
                    getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return verName;
    }
}
