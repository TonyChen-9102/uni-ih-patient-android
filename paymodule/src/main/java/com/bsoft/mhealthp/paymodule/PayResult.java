package com.bsoft.mhealthp.paymodule;

import android.text.TextUtils;

import com.taobao.weex.bridge.JSCallback;

public class PayResult {
    public JSCallback curCallBack;

    public PayResult(JSCallback curCallBack) {
        this.curCallBack = curCallBack;
    }

    public void backResult(String type) {
        if (curCallBack != null) {
            if (TextUtils.equals(type, "success")) {
                curCallBack.invoke(2);
            } else if (TextUtils.equals(type, "fail")) {
                curCallBack.invoke(-1);
            } else if (TextUtils.equals(type, "cancel")) {
                curCallBack.invoke(0);
            }
        }
    }
}
