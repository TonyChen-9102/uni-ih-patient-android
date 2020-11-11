package com.demo.ck.videomodule;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import com.alibaba.fastjson.JSONObject;
import com.demo.ck.videomodule.push.TCPusherView;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.bridge.JSCallback;
import com.taobao.weex.dom.CSSConstants;
import com.taobao.weex.dom.WXAttr;
import com.taobao.weex.layout.ContentBoxMeasurement;
import com.taobao.weex.ui.action.BasicComponentData;
import com.taobao.weex.ui.component.WXComponent;
import com.taobao.weex.ui.component.WXComponentProp;
import com.taobao.weex.ui.component.WXVContainer;
import com.taobao.weex.utils.WXViewUtils;
import io.dcloud.common.util.DialogUtil;
import io.dcloud.common.util.PdrUtil;

public class PusherComponent extends WXComponent<TCPusherView> {
    private WXAttr attr;
    private boolean isInit = false;

    public PusherComponent(WXSDKInstance instance, WXVContainer parent, BasicComponentData basicComponentData) {
        super(instance, parent, basicComponentData);
        this.attr = basicComponentData.getAttrs();
        if (!basicComponentData.getStyles().containsKey("flex")) {
            this.setContentBoxMeasurement(new ContentBoxMeasurement() {
                public void measureInternal(float width, float height, int widthMeasureMode, int heightMeasureMode) {
                    if (CSSConstants.isUndefined(width)) {
                        this.mMeasureWidth = WXViewUtils.getRealPxByWidth(300.0F, PusherComponent.this.getInstance().getInstanceViewPortWidth());
                    }

                    if (CSSConstants.isUndefined(height)) {
                        this.mMeasureHeight = WXViewUtils.getRealPxByWidth(225.0F, PusherComponent.this.getInstance().getInstanceViewPortWidth());
                    }

                }

                public void layoutBefore() {
                }

                public void layoutAfter(float computedWidth, float computedHeight) {
                }
            });
        }

    }

    public PusherComponent(WXSDKInstance instance, WXVContainer parent, int type, BasicComponentData basicComponentData) {
        super(instance, parent, type, basicComponentData);
    }

    protected TCPusherView initComponentHostView(Context context) {
        if (Build.CPU_ABI.equalsIgnoreCase("x86")) {
            if (!this.isInit) {
                this.isInit = true;
                DialogUtil.showDialog((Activity)context, (String)null, context.getString(R.string.livepusher_error_tips), new String[]{null});
            }

            return null;
        } else {
            Object position = this.attr.get("devicePosition");
            return new TCPusherView(context, this, position == null || position.equals("front"));
        }
    }

    protected void onFinishLayout() {
        super.onFinishLayout();
    }

    protected boolean setProperty(String key, Object param) {
        return this.getHostView() == null ? true : super.setProperty(key, param);
    }

    @WXComponentProp(
            name = "url"
    )
    public void setSrc(String src) {
        ((TCPusherView)this.getHostView()).setSrc(src);
    }

    @WXComponentProp(
            name = "mode"
    )
    public void setMode(String mode) {
        ((TCPusherView)this.getHostView()).setMode(mode);
    }

    @WXComponentProp(
            name = "autopush"
    )
    public void setAutoPusher(boolean isAuto) {
        ((TCPusherView)this.getHostView()).setAutoPush(isAuto);
    }

    @WXComponentProp(
            name = "muted"
    )
    public void isMute(boolean mote) {
        ((TCPusherView)this.getHostView()).setMute(mote);
    }

    @WXComponentProp(
            name = "enableCamera"
    )
    public void setEnableCamera(boolean isEnable) {
        ((TCPusherView)this.getHostView()).enableCamera(isEnable);
    }

    @WXComponentProp(
            name = "autoFocus"
    )
    public void setAutoFocus(boolean isAuto) {
        ((TCPusherView)this.getHostView()).autoFocus(isAuto);
    }

    @WXComponentProp(
            name = "orientation"
    )
    public void setorientation(String orientation) {
        ((TCPusherView)this.getHostView()).setOritation(orientation);
    }

    @WXComponentProp(
            name = "beauty"
    )
    public void setBeauty(int beauty) {
        ((TCPusherView)this.getHostView()).setBeauty(beauty);
    }

    @WXComponentProp(
            name = "whiteness"
    )
    public void setWhiteness(int whiteness) {
        ((TCPusherView)this.getHostView()).setWhite(whiteness);
    }

    @WXComponentProp(
            name = "aspect"
    )
    public void setAspect(String aspect) {
    }

    @WXComponentProp(
            name = "minBitrate"
    )
    public void setMinBitrate(int minBitrate) {
        ((TCPusherView)this.getHostView()).setMinBitrate(minBitrate);
    }

    @WXComponentProp(
            name = "maxBitrate"
    )
    public void setMaxBitrate(int maxBitrate) {
        ((TCPusherView)this.getHostView()).setMaxBitrate(maxBitrate);
    }

    @WXComponentProp(
            name = "waitingImage"
    )
    public void setWaitingImage(String waitingImage) {
        ((TCPusherView)this.getHostView()).setWaintImage(waitingImage);
    }

    @WXComponentProp(
            name = "zoom"
    )
    public void setZoom(boolean zoom) {
        ((TCPusherView)this.getHostView()).setZoom(zoom);
    }

    @WXComponentProp(
            name = "backgroundMute"
    )
    public void setBackgroundMute(boolean backgroundMute) {
        ((TCPusherView)this.getHostView()).setBGMute(backgroundMute);
    }

    @JSMethod
    public void start(JSCallback callback) {
        ((TCPusherView)this.getHostView()).start(callback);
    }

    @JSMethod
    public void stop(JSCallback callback) {
        ((TCPusherView)this.getHostView()).stopPusher(callback);
    }

    @JSMethod
    public void pause(JSCallback callback) {
        ((TCPusherView)this.getHostView()).pause(callback);
    }

    @JSMethod
    public void resume(JSCallback callback) {
        ((TCPusherView)this.getHostView()).resume(callback);
    }

    @JSMethod
    public void switchCamera(JSCallback callback) {
        ((TCPusherView)this.getHostView()).sCamera(callback);
    }

    @JSMethod
    public void snapshot(JSCallback success) {
        ((TCPusherView)this.getHostView()).snapShot(success);
    }

    @JSMethod
    public void toggleTorch(JSCallback callback) {
        ((TCPusherView)this.getHostView()).toggleTorch(callback);
    }

    @JSMethod
    public void playBGM(JSONObject param, JSCallback success) {
        String url = param.getString("url");
        ((TCPusherView)this.getHostView()).playBGM(url, success);
    }

    @JSMethod
    public void stopBGM(JSCallback success) {
        ((TCPusherView)this.getHostView()).stopBGM(success);
    }

    @JSMethod
    public void pauseBGM(JSCallback success) {
        ((TCPusherView)this.getHostView()).pauseBGM(success);
    }

    @JSMethod
    public void resumeBGM(JSCallback success) {
        ((TCPusherView)this.getHostView()).resumeBGM(success);
    }

    @JSMethod
    public void setBGMVolume(JSONObject param, JSCallback success) {
        int volume = param.getInteger("volume");
        if (!PdrUtil.isEmpty(volume)) {
            ((TCPusherView)this.getHostView()).setBGNVolume(volume, success);
        }
    }

    @JSMethod
    public void startPreview(JSCallback callback) {
        ((TCPusherView)this.getHostView()).preview(callback);
    }

    @JSMethod
    public void stopPreview(JSCallback callback) {
        ((TCPusherView)this.getHostView()).stopPreview(callback);
    }

    public void onActivityPause() {
        super.onActivityPause();
        if (this.getHostView() != null) {
            ((TCPusherView)this.getHostView()).pause((JSCallback)null);
        }

    }

    public void onActivityResume() {
        super.onActivityResume();
    }

    public void destroy() {
        super.destroy();
        ((TCPusherView)this.getHostView()).stopPusher((JSCallback)null);
        ((TCPusherView)this.getHostView()).destory();
    }
}

