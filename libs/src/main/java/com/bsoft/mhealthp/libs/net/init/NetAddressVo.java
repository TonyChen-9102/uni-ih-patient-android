package com.bsoft.mhealthp.libs.net.init;


import com.bsoft.mhealthp.libs.CoreVo;

import java.io.Serializable;

/**
 * Created by 83990 on 2018/2/7.
 */

public class NetAddressVo extends CoreVo {
    private String environment;
    private String environmentText;
    private String HttpApiUrl;
    private String HttpApiUrl2;

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    public String getEnvironmentText() {
        return environmentText;
    }

    public void setEnvironmentText(String environmentText) {
        this.environmentText = environmentText;
    }

    public String getHttpApiUrl() {
        return HttpApiUrl;
    }

    public void setHttpApiUrl(String httpApiUrl) {
        HttpApiUrl = httpApiUrl;
    }

    public String getHttpApiUrl2() {
        return HttpApiUrl2;
    }

    public void setHttpApiUrl2(String httpApiUrl2) {
        HttpApiUrl2 = httpApiUrl2;
    }
}
