/*
 * Copyright (C) 2012 www.amsoft.cn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.bsoft.mhealthp.libs.utils;
/************************************************************************
 *History:
 *
 *1.Id:none 咨询聊天 chenkai 20170916
 *  Description: sd卡检测
 *
 ************************************************************************/

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.util.Iterator;
import java.util.List;


/**
 * 描述：应用工具类.
 */
public class AppUtil {

    /**
     * 启动第三方apk
     * 直接打开  每次都会启动到启动界面，每次都会干掉之前的，从新启动
     * XXXXX ： 包名
     */
    public static void launchAPK(Context context, String pkgName) {
        PackageManager packageManager = context.getPackageManager();
        Intent it = packageManager.getLaunchIntentForPackage(pkgName);
        context.startActivity(it);
    }

    /**
     * 获取应用程序名称
     */
    public static String getAppName(Context context) {
        ApplicationInfo appInfo;
        try {
            appInfo = context.getPackageManager().getApplicationInfo(getPackageName(context), PackageManager.GET_META_DATA);
            return appInfo.loadLabel(context.getPackageManager()) + "";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getPackageName(Context context) {
        return context.getPackageName();
    }

    public static String getVersionName(Context context) {
        PackageManager pm = context.getPackageManager();

        try {
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            return pi.versionName;
        } catch (PackageManager.NameNotFoundException var4) {
            var4.printStackTrace();
            return "";
        }
    }

    public static int getVersionCode(Context context) {
        PackageManager pm = context.getPackageManager();

        try {
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            return pi.versionCode;
        } catch (PackageManager.NameNotFoundException var4) {
            var4.printStackTrace();
            return 100000;
        }
    }


    public static boolean isAppRunOnForeground(Context context) {
        String packageName = context.getPackageName();
        ActivityManager activityManager = (ActivityManager) context.getSystemService("activity");
        List tasksInfo = activityManager.getRunningTasks(1);
        return tasksInfo.size() > 0 && packageName.equals(((ActivityManager.RunningTaskInfo) tasksInfo.get(0)).topActivity.getPackageName());
    }

    public static boolean isBackground(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService("activity");
        List appProcesses = activityManager.getRunningAppProcesses();
        Iterator var3 = appProcesses.iterator();

        ActivityManager.RunningAppProcessInfo appProcess;
        do {
            if (!var3.hasNext()) {
                return false;
            }

            appProcess = (ActivityManager.RunningAppProcessInfo) var3.next();
        } while (!appProcess.processName.equals(context.getPackageName()));

        if (appProcess.importance == 400) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * 用来判断服务是否运行.
     *
     * @param ctx       the ctx
     * @param className 判断的服务名字 "com.xxx.xx..XXXService"
     * @return true 在运行 false 不在运行
     */
    public static boolean isServiceRunning(Context ctx, String className) {
        boolean isRunning = false;
        ActivityManager activityManager = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningServiceInfo> servicesList = activityManager.getRunningServices(Integer.MAX_VALUE);
        Iterator<RunningServiceInfo> l = servicesList.iterator();
        while (l.hasNext()) {
            RunningServiceInfo si = l.next();
            if (className.equals(si.service.getClassName())) {
                isRunning = true;
            }
        }
        return isRunning;
    }

    /**
     * 停止服务.
     *
     * @param ctx       the ctx
     * @param className the class name
     * @return true, if successful
     */
    public static boolean stopRunningService(Context ctx, String className) {
        Intent intent_service = null;
        boolean ret = false;
        try {
            intent_service = new Intent(ctx, Class.forName(className));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (intent_service != null) {
            ret = ctx.stopService(intent_service);
        }
        return ret;
    }

    /**
     * 检测程序是否安装
     *
     * @param packageName
     * @return
     */
    public static boolean isInstalled(Context context, String packageName) {
        PackageManager manager = context.getPackageManager();
        //获取所有已安装程序的包信息
        List<PackageInfo> installedPackages = manager.getInstalledPackages(0);
        if (installedPackages != null) {
            for (PackageInfo info : installedPackages) {
                if (info.packageName.equals(packageName))
                    return true;
            }
        }
        return false;
    }

}
