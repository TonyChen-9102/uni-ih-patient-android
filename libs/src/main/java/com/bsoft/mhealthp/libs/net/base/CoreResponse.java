package com.bsoft.mhealthp.libs.net.base;


import com.alibaba.fastjson.annotation.JSONField;
import com.bsoft.mhealthp.libs.CoreVo;

import java.io.Serializable;
import java.util.Collection;

/**
 * Created by kai.chen on 2017/6/22.
 */

public class CoreResponse<E> extends CoreVo {
    public static final String SUCCESS = "200";

    @JSONField(name = "code")
    private String code;

    @JSONField(name = "msg")
    private String message;

    @JSONField(name = "body")
    private E details;

    public boolean isSuccess() {
        if (code == null) {
            return false;
        }
        return SUCCESS.equals(code);
    }

    public boolean isEmpty() {
        if (details instanceof Collection) {
            return details == null || ((Collection) details).isEmpty();
        }
        return details == null;
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public E getDetails() {
        return details;
    }

    public void setDetails(E details) {
        this.details = details;
    }


}
