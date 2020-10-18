package com.bsoft.mhealthp.libs.net.base;

import android.widget.Toast;

import com.bsoft.mhealthp.libs.AppBase;
import com.bsoft.mhealthp.libs.log.LogUtil;
import com.bsoft.mhealthp.libs.net.utils.ApiException;
import com.bsoft.mhealthp.libs.net.utils.NetworkErrorUtil;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by kai.chen on 2017/6/22.
 */

public abstract class BaseObserver<T> implements Observer<CoreResponse<T>> {

    public abstract void onHandlePrePare(Disposable d);

    protected abstract void onHandleSuccess(T value);

    protected abstract void onHandleError(String errorType, String msg);

    protected abstract void onHandleComplete();

    protected BaseObserver() {
    }

    @Override
    public void onSubscribe(Disposable d) {
        onHandlePrePare(d);
    }

    @Override
    public void onNext(CoreResponse<T> value) {
        if (value.isSuccess()) {
            onHandleSuccess(value.getDetails());
        } else {
            if (AppBase.isEngineering) {
                Toast.makeText(AppBase.getApplication(),
                        "服务端返回错误:code=" + value.getCode() + ";msg=" + value.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
            onHandleError(value.getCode(), value.getMessage());
        }
    }

    @Override
    public void onError(Throwable e) {
        ApiException apiException = NetworkErrorUtil.getErrorTypeByThrow(e);
        LogUtil.e("BaseGetObserver;onError;type="
                + apiException.getCode() + ";msg=" + apiException.getMsg(), e);
        if (AppBase.isEngineering) {
            Toast.makeText(AppBase.getApplication(),
                    "本地网络错误:type=" + apiException.getCode() + ";msg=" + apiException.getMsg(),
                    Toast.LENGTH_LONG).show();
        }
        onHandleError(apiException.getCode(), apiException.getMsg());
        onHandleComplete();
    }

    @Override
    public void onComplete() {
        onHandleComplete();
    }
}
