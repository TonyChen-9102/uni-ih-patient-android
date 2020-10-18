package com.bsoft.mhealthp.libs.utils.dialog;


import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Handler;

import com.bsoft.mhealthp.libs.log.LogUtil;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

public class CoreDialogFragment extends DialogFragment {
    protected boolean isShow;
    protected boolean keyBackEnable;
    private Handler handler = new Handler();

    public boolean isShow() {
        return isShow;
    }

    private Runnable keyBackRun = new Runnable() {
        @Override
        public void run() {
            keyBackEnable = true;
        }
    };

    public void show(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            if (activity.isDestroyed()) {
                LogUtil.e("CoreDialogFragment;show;activity destroy");
                return;
            }
        }
        if (activity.isFinishing()) {
            LogUtil.e("CoreDialogFragment;show;activity destroy");
            return;
        }
        show(activity.getFragmentManager(), "CoreDialogFragment");
    }

    public void show(Activity activity, String tag) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            if (activity.isDestroyed()) {
                LogUtil.e("CoreDialogFragment;show;activity destroy");
                return;
            }
        }
        if (activity.isFinishing()) {
            LogUtil.e("CoreDialogFragment;show;activity destroy");
            return;
        }
        show(activity.getFragmentManager(), tag);
    }

    public void show(FragmentManager manager) {
        show(manager, "CoreDialogFragment");
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        if (!isAdded() && !isShow()) {
            isShow = true;
            try {
                Class c = Class.forName("android.app.DialogFragment");
                Constructor con = c.getConstructor();
                Object obj = con.newInstance();
                Field dismissed = c.getDeclaredField("mDismissed");
                dismissed.setAccessible(true);
                dismissed.set(obj, false);
                Field shownByMe = c.getDeclaredField("mShownByMe");
                shownByMe.setAccessible(true);
                shownByMe.set(obj, true);
            } catch (Exception e) {
                e.printStackTrace();
            }
            FragmentTransaction ft = manager.beginTransaction();
            ft.add(this, tag);
            ft.commitAllowingStateLoss();
            keyBackEnable = false;
            handler.removeCallbacks(keyBackRun);
            handler.postDelayed(keyBackRun, 500);
        }
    }

    public void show(FragmentTransaction transaction) {
        show(transaction, "CoreConfirmDialog");
    }

    @Override
    public int show(FragmentTransaction transaction, String tag) {
        if (!isAdded() && !isShow()) {
            isShow = true;
            try {
                Class c = Class.forName("android.app.DialogFragment");
                Constructor con = c.getConstructor();
                Object obj = con.newInstance();
                Field dismissed = c.getDeclaredField("mDismissed");
                dismissed.setAccessible(true);
                dismissed.set(obj, false);
                Field shownByMe = c.getDeclaredField("mShownByMe");
                shownByMe.setAccessible(true);
                shownByMe.set(obj, true);
                Field mViewDestroyed = c.getDeclaredField("mViewDestroyed");
                mViewDestroyed.setAccessible(true);
                mViewDestroyed.set(obj, false);
            } catch (Exception e) {
                e.printStackTrace();
            }
            transaction.add(this, tag);
            keyBackEnable = false;
            handler.removeCallbacks(keyBackRun);
            handler.postDelayed(keyBackRun, 500);
            return transaction.commitAllowingStateLoss();
        } else {
            return 0;
        }
    }

    @Override
    public void dismiss() {
        if (getActivity() != null && isShow() && !getActivity().isFinishing()) {
            super.dismiss();
        }
    }

    @Override
    public void dismissAllowingStateLoss() {
        if (getActivity() != null && isShow() && !getActivity().isFinishing()) {
            super.dismissAllowingStateLoss();
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        isShow = false;
    }
}
