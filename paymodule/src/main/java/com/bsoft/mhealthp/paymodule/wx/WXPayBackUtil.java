package com.bsoft.mhealthp.paymodule.wx;

import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseResp;

public class WXPayBackUtil {
    public static void onResp(BaseResp resp) {
        //微信支付
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            WxPayManager.getInstance().event(resp);
        }
    }
}
