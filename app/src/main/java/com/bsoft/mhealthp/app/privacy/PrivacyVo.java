package com.bsoft.mhealthp.app.privacy;


import com.bsoft.mhealthp.libs.CoreVo;

public class PrivacyVo extends CoreVo {

    /**
     {
     "id": 1,
     "tenantId": "hcn.sh-pdxqrmyy",
     "productCode": "hcn.sh-pdxqrmyy.patient_ios",
     "productName": "上海浦东新区人民医院.居民版_ios",
     "agreeType": "1",//协议类型：1隐私协议、2用户协议
     "url": "http://www.bsoft.com",//协议地址：协议页面地址
     "version": "1.1.0",//版本号规则：大版本号2.0，小版本号.0，以后发布一次小版本号加1
     "content": "协议版本说明",//版本说明
     "createDt": "2020-04-23 14:27:32",//提交时间
     "enableFlag": "1"//启用标志：0否、1是
     }

     */

    private int id;
    private String tenantId;
    private String productCode;
    private String productName;
    private String agreeType;
    private String url;
    private String version;
    private String content;
    private String createDt;
    private String enableFlag;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getAgreeType() {
        return agreeType;
    }

    public void setAgreeType(String agreeType) {
        this.agreeType = agreeType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreateDt() {
        return createDt;
    }

    public void setCreateDt(String createDt) {
        this.createDt = createDt;
    }

    public String getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
    }
}
