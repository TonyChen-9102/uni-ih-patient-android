package com.demo.ck.videomodule.push;



import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory.Options;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.bridge.JSCallback;
import com.taobao.weex.ui.component.WXComponent;
import com.tencent.rtmp.ITXLivePushListener;
import com.tencent.rtmp.TXLiveConstants;
import com.tencent.rtmp.TXLivePushConfig;
import com.tencent.rtmp.TXLivePusher;
import com.tencent.rtmp.TXLivePusher.ITXSnapshotListener;
import com.tencent.rtmp.TXLivePusher.OnBGMNotify;
import com.tencent.rtmp.ui.TXCloudVideoView;
import io.dcloud.common.adapter.util.PermissionUtil;
import io.dcloud.common.adapter.util.PermissionUtil.Request;
import io.dcloud.common.util.PdrUtil;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TCPusherView extends LinearLayout implements ITXLivePushListener, OnBGMNotify {
    private TXLivePusher mLivePusher;
    private TXLivePushConfig mLivePushConfig;
    private TXCloudVideoView pusherView;
    private WXComponent component;
    private WXSDKInstance mInstance;
    private boolean cameraType = true;
    private boolean isAutoFocus;
    private int videoQulity = 6;
    private int videoResolution;
    private int beautyLevel = 0;
    private int whiteLevel = 0;
    private String mSrc;
    private String BGMPath;
    private boolean autoPush;
    @SuppressLint({"HandlerLeak"})
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                TCPusherView.this.init();
            }

        }
    };
    private boolean isPreview = false;
    private boolean torchIsOn = false;
    private List<String> permissions = new ArrayList();

    public void setAutoPush(boolean autoPush) {
        this.autoPush = autoPush;
    }

    public TCPusherView(Context context, WXComponent component, boolean isFront) {
        super(context);
        this.component = component;
        this.mInstance = component.getInstance();
        this.mLivePushConfig = new TXLivePushConfig();
        this.mLivePushConfig.setVideoEncodeGop(5);
        this.mLivePushConfig.enableNearestIP(true);
        this.mLivePushConfig.setFrontCamera(isFront);
        //开启回声消除：连麦时必须开启，非连麦时不要开启
        this.mLivePushConfig.enableAEC(true);
        this.mLivePushConfig.setANS(true);
        //设置是否开启码率自适应.
        this.mLivePushConfig.setAutoAdjustBitrate(true);
        //启用或禁用硬件加速.
        this.mLivePushConfig.setHardwareAcceleration(TXLiveConstants.ENCODE_VIDEO_AUTO);
        //设置RTMP传输通道的类型
        this.mLivePushConfig.setRtmpChannelType(TXLiveConstants.RTMP_CHANNEL_TYPE_AUTO);
        //设置声道数.
//        this.mLivePushConfig.setAudioChannels(2);
        this.cameraType = isFront;
        if (!isFront) {
            this.mLivePushConfig.setTouchFocus(false);
        }

        this.mLivePushConfig.setPauseFlag(3);
        this.mLivePusher = new TXLivePusher(this.component.getContext());
        this.init();
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (PdrUtil.isEmpty(this.component.getStyles().getBackgroundColor())) {
            this.setBackgroundColor(-16777216);
        }

    }

    public void init() {
        this.mLivePusher.setConfig(this.mLivePushConfig);
        this.mLivePusher.setPushListener(this);
        this.mLivePusher.setBGMNofify(this);
        this.mLivePusher.setVideoQuality(this.videoQulity, false, false);
        ((Activity)this.getContext()).getWindow().addFlags(128);
        this.pusherView = new TXCloudVideoView(this.getContext());
        this.addView(this.pusherView, new LayoutParams(-1, -1));
        PermissionUtil.requestSystemPermissions((Activity)this.mInstance.getContext(), new String[]{"android.permission.RECORD_AUDIO", "android.permission.CAMERA"}, 20190419, new Request() {
            public void onGranted(String streamPerName) {
                TCPusherView.this.permissions.add(streamPerName);
                if (TCPusherView.this.permissions.size() == 2 && TCPusherView.this.autoPush) {
                    if (TCPusherView.this.mSrc == null) {
                        TCPusherView.this.mSrc = (String)TCPusherView.this.component.getAttrs().get("url");
                    }

                    TCPusherView.this.start((JSCallback)null);
                }

            }

            public void onDenied(String streamPerName) {
                Map<String, Object> values = new HashMap();
                if (streamPerName.equals("CAMERA")) {
                    values.put("errCode", 10001);
                    values.put("errMsg", "用户禁止使用摄像头");
                } else if (streamPerName.equals("RECORD")) {
                    values.put("errCode", 10002);
                    values.put("errMsg", "用户禁止使用录音");
                }

                HashMap<String, Object> detail = new HashMap(1);
                detail.put("detail", values);
                TCPusherView.this.fireEvent("error", detail);
            }
        });
    }

    public void switchCamera(String type) {
        this.cameraType = type.equals("front");
        if (this.mLivePusher.isPushing()) {
            this.mLivePusher.switchCamera();
        }

        this.mLivePushConfig.setFrontCamera(this.cameraType);
        this.mLivePusher.setConfig(this.mLivePushConfig);
    }

    public void sCamera(JSCallback callback) {
        Map<String, Object> data = new HashMap();
        this.cameraType = !this.cameraType;
        this.mLivePusher.switchCamera();
        data.put("type", "success");
        if (callback != null) {
            callback.invoke(data);
        }

    }

    public void autoFocus(boolean isAuto) {
        if (!this.cameraType) {
            this.isAutoFocus = isAuto;
            this.mLivePushConfig.setTouchFocus(isAuto);
        }
    }

    public void setMute(boolean mote) {
        this.mLivePusher.setMute(mote);
    }

    public void preview(JSCallback callback) {
        this.mLivePushConfig.setFrontCamera(this.cameraType);
        this.mLivePusher.setConfig(this.mLivePushConfig);
        this.isPreview = true;
        this.mLivePusher.startCameraPreview(this.pusherView);
        Map<String, Object> data = new HashMap();
        data.put("type", "success");
        if (callback != null) {
            callback.invoke(data);
        }

    }

    public void stopPreview(JSCallback callback) {
        this.isPreview = false;
        this.mLivePusher.stopCameraPreview(false);
        Map<String, Object> data = new HashMap();
        data.put("type", "success");
        if (callback != null) {
            callback.invoke(data);
        }

    }

    public void setBGMute(boolean ismute) {
        this.mLivePushConfig.setPauseFlag(3);
    }

    public void setMode(String mode) {
        byte var3 = -1;
        switch(mode.hashCode()) {
            case 2300:
                if (mode.equals("HD")) {
                    var3 = 1;
                }
                break;
            case 2641:
                if (mode.equals("SD")) {
                    var3 = 0;
                }
                break;
            case 69570:
                if (mode.equals("FHD")) {
                    var3 = 2;
                }
                break;
            case 81473:
                if (mode.equals("RTC")) {
                    var3 = 4;
                }
        }

        switch(var3) {
            case 0:
                this.videoQulity = 1;
                this.videoResolution = 0;
                this.mLivePushConfig.setAutoAdjustBitrate(false);
                this.mLivePushConfig.setVideoBitrate(700);
                this.mLivePusher.setConfig(this.mLivePushConfig);
                break;
            case 1:
                this.videoQulity = 2;
                this.videoResolution = 1;
                break;
            case 2:
                this.videoQulity = 3;
                this.videoResolution = 2;
                break;
            case 3:
            case 4:
            default:
                this.videoQulity = 6;
                this.videoResolution = 0;
        }

        this.mLivePusher.setVideoQuality(this.videoQulity, false, false);
        this.mLivePushConfig.setVideoResolution(this.videoResolution);
    }

    public void setOritation(String oritation) {
        if (oritation.equals("vertical")) {
            this.mLivePushConfig.setHomeOrientation(1);
            this.mLivePusher.setRenderRotation(0);
        } else if (oritation.equals("horizontal")) {
            this.mLivePushConfig.setHomeOrientation(2);
            this.mLivePusher.setRenderRotation(270);
        }

    }

    public void setBeauty(int level) {
        if (level < 0) {
            level = 0;
        } else if (level > 9) {
            level = 9;
        }

        this.beautyLevel = level;
        this.mLivePushConfig.setBeautyFilter(this.beautyLevel, this.whiteLevel, 0);
        this.mLivePusher.setBeautyFilter(0, this.beautyLevel, this.whiteLevel, 0);
    }

    public void setWhite(int level) {
        if (level < 0) {
            level = 0;
        } else if (level > 9) {
            level = 9;
        }

        this.whiteLevel = level;
        this.mLivePushConfig.setBeautyFilter(this.beautyLevel, this.whiteLevel, 0);
        this.mLivePusher.setBeautyFilter(0, this.beautyLevel, this.whiteLevel, 0);
    }

    public void setMinBitrate(int bitrate) {
        this.mLivePushConfig.setMinVideoBitrate(bitrate);
    }

    public void setMaxBitrate(int bitrate) {
        this.mLivePushConfig.setMaxVideoBitrate(bitrate);
    }

    public void setWaintImage(String imagePath) {
        String realPath = this.component.getInstance().rewriteUri(Uri.parse(imagePath), "image").getPath();
        Bitmap bitmap = this.decodeResource(realPath);
        if (bitmap != null) {
            this.mLivePushConfig.setPauseImg(bitmap);
            this.mLivePushConfig.setPauseFlag(1);
        }

    }

    private Bitmap decodeResource(String id) {
        Options opts = new Options();
        return BitmapFactory.decodeFile(id, opts);
    }

    public void enableCamera(boolean isEnable) {
        this.mLivePushConfig.enablePureAudioPush(!isEnable);
    }

    public void setZoom(boolean isZoom) {
        this.mLivePushConfig.setEnableZoom(isZoom);
    }

    public void start(JSCallback callback) {
        Map<String, Object> data = new HashMap();
        if (PdrUtil.isEmpty(this.mSrc)) {
            data.put("type", "fail");
            if (callback != null) {
                callback.invoke(data);
            }

        } else {
            this.mLivePushConfig.setFrontCamera(this.cameraType);
            this.mLivePushConfig.setBeautyFilter(this.beautyLevel, this.whiteLevel, 0);
            this.mLivePusher.setConfig(this.mLivePushConfig);
            if (!this.isPreview) {
                this.mLivePusher.startCameraPreview(this.pusherView);
            }

            if (this.mLivePusher.startPusher(this.mSrc) == 0) {
                data.put("type", "success");
            } else {
                data.put("type", "fail");
            }

            if (callback != null) {
                callback.invoke(data);
            }

        }
    }

    public void pause(JSCallback callback) {
        this.pusherView.onPause();
        this.mLivePusher.pausePusher();
        Map<String, Object> data = new HashMap();
        data.put("type", "success");
        if (callback != null) {
            callback.invoke(data);
        }

    }

    public void resume(JSCallback callback) {
        this.pusherView.onResume();
        this.mLivePusher.resumePusher();
        Map<String, Object> data = new HashMap();
        data.put("type", "success");
        if (callback != null) {
            callback.invoke(data);
        }

    }

    public void setSrc(String src) {
        if (PdrUtil.isEmpty(this.mSrc) && !PdrUtil.isEmpty(src) || !this.mSrc.equals(src)) {
            this.mSrc = src;
        }
    }

    private void changeSrc(String src) {
        this.mLivePushConfig.setCustomModeType(0);
        this.mLivePushConfig.setPauseImg(300, 5);
        this.mLivePusher.startPusher(src);
    }

    public void stopPusher(JSCallback callback) {
        ((Activity)this.getContext()).getWindow().clearFlags(128);
        this.mLivePusher.stopBGM();
        this.mLivePusher.stopCameraPreview(false);
        this.mLivePusher.stopScreenCapture();
        this.mLivePusher.stopPusher();
        Map<String, Object> data = new HashMap();
        data.put("type", "success");
        if (callback != null) {
            callback.invoke(data);
        }

    }

    public void destory() {
        this.pusherView.stop(true);
        this.mLivePusher.setPushListener((ITXLivePushListener)null);
        this.mLivePusher.setBGMNofify((OnBGMNotify)null);
    }

    public void playBGM(String url, JSCallback success) {
        Map<String, Object> data = new HashMap();
        if (PdrUtil.isEmpty(url)) {
            data.put("type", "fail");
            if (success != null) {
                success.invoke(data);
            }

        } else {
            if (PdrUtil.isNetPath(url)) {
                this.BGMPath = url;
            } else {
                this.BGMPath = this.mInstance.rewriteUri(Uri.parse(url), "image").getPath();
            }

            if (this.mLivePusher.playBGM(this.BGMPath)) {
                data.put("type", "success");
                if (success != null) {
                    success.invoke(data);
                }
            } else {
                data.put("type", "fail");
                if (success != null) {
                    success.invoke(data);
                }
            }

        }
    }

    public void setBGNVolume(int volume, JSCallback success) {
        HashMap data;
        if (this.mLivePusher.setBGMVolume((float)volume)) {
            data = new HashMap();
            data.put("type", "success");
            if (success != null) {
                success.invoke(data);
            }
        } else {
            data = new HashMap();
            data.put("type", "fail");
            if (success != null) {
                success.invoke(data);
            }
        }

    }

    public void pauseBGM(JSCallback success) {
        HashMap data;
        if (this.mLivePusher.pauseBGM()) {
            data = new HashMap();
            data.put("type", "success");
            if (success != null) {
                success.invoke(data);
            }
        } else {
            data = new HashMap();
            data.put("type", "fail");
            if (success != null) {
                success.invoke(data);
            }
        }

    }

    public void resumeBGM(JSCallback success) {
        HashMap data;
        if (this.mLivePusher.resumeBGM()) {
            data = new HashMap();
            data.put("type", "success");
            if (success != null) {
                success.invoke(data);
            }
        } else {
            data = new HashMap();
            data.put("type", "fail");
            if (success != null) {
                success.invoke(data);
            }
        }

    }

    public void stopBGM(JSCallback success) {
        HashMap data;
        if (this.mLivePusher.stopBGM()) {
            data = new HashMap();
            data.put("type", "success");
            if (success != null) {
                success.invoke(data);
            }
        } else {
            data = new HashMap();
            data.put("type", "fail");
            if (success != null) {
                success.invoke(data);
            }
        }

    }

    public void snapShot(final JSCallback success) {
        this.mLivePusher.snapshot(new ITXSnapshotListener() {
            public void onSnapshot(Bitmap bitmap) {
                if (bitmap != null) {
                    String path = "_doc/snapshot/snapshot_" + System.currentTimeMillis() + ".jpg";
                    path = TCPusherView.this.mInstance.rewriteUri(Uri.parse(path), "image").getPath();

                    try {
                        File snapImage = new File(path);
                        if (!snapImage.exists()) {
                            if (!snapImage.getParentFile().exists()) {
                                snapImage.getParentFile().mkdirs();
                            }

                            snapImage.createNewFile();
                        }

                        FileOutputStream fos = new FileOutputStream(snapImage);
                        bitmap.compress(CompressFormat.JPEG, 100, fos);
                        fos.flush();
                        fos.close();
                        Map<String, Object> values = new HashMap();
                        HashMap<String, Object> message = new HashMap();
                        message.put("width", bitmap.getWidth());
                        message.put("height", bitmap.getHeight());
                        message.put("tempImagePath", path);
                        values.put("message", message);
                        values.put("type", "success");
                        values.put("code", 0);
                        if (success != null) {
                            success.invoke(values);
                        }
                    } catch (Exception var7) {
                        Map<String, Object> error = new HashMap();
                        error.put("code", "-99");
                        error.put("message", var7.getMessage());
                        error.put("type", "fail");
                        if (success != null) {
                            success.invoke(error);
                        }
                    }
                } else {
                    Map<String, Object> data = new HashMap();
                    data.put("type", "fail");
                    data.put("code", "-99");
                    data.put("message", "data error");
                    if (success != null) {
                        success.invoke(data);
                    }
                }

            }
        });
    }

    public void toggleTorch(JSCallback callback) {
        this.torchIsOn = !this.torchIsOn;
        HashMap data;
        if (this.mLivePusher.turnOnFlashLight(this.torchIsOn)) {
            data = new HashMap();
            data.put("type", "success");
            if (callback != null) {
                callback.invoke(data);
            }
        } else {
            data = new HashMap();
            data.put("type", "fail");
            if (callback != null) {
                callback.invoke(data);
            }
        }

    }

    private void fireEvent(String event, Map<String, Object> params) {
        if (this.component.containsEvent(event)) {
            this.component.fireEvent(event, params);
        }

    }

    public void onPushEvent(int i, Bundle bundle) {
        Map<String, Object> values = new HashMap();
        values.put("code", i);
        values.put("message", bundle == null ? "" : (bundle.getString("EVT_MSG") == null ? "" : bundle.getString("EVT_MSG")));
        HashMap<String, Object> detail = new HashMap(1);
        detail.put("detail", values);
        this.fireEvent("statechange", detail);
    }

    public void onNetStatus(Bundle bundle) {
        Map<String, Object> infos = new HashMap();
        HashMap values = new HashMap();

        try {
            values.put("videoBitrate", bundle.getInt("VIDEO_BITRATE"));
            values.put("audioBitrate", bundle.getInt("AUDIO_BITRATE"));
            values.put("videoFPS", bundle.getInt("VIDEO_FPS"));
            values.put("videoGOP", bundle.getInt("VIDEO_GOP"));
            values.put("netSpeed", bundle.getInt("NET_SPEED"));
            values.put("netJitter", 0);
            values.put("videoWidth", bundle.getInt("VIDEO_WIDTH"));
            values.put("videoHeight", bundle.getInt("VIDEO_HEIGHT"));
        } catch (Exception var5) {
        }

        infos.put("info", values);
        HashMap<String, Object> detail = new HashMap(1);
        detail.put("detail", infos);
        this.fireEvent("netstatus", detail);
    }

    public void onBGMStart() {
        this.fireEvent("bgmstart", new HashMap());
    }

    public void onBGMProgress(long l, long l1) {
        Map<String, Object> values = new HashMap();
        values.put("progress", l);
        values.put("duration", l1);
        HashMap<String, Object> detail = new HashMap(1);
        detail.put("detail", values);
        this.fireEvent("bgmprogress", detail);
    }

    public void onBGMComplete(int i) {
        this.fireEvent("bgmcomplete", new HashMap());
    }
}

