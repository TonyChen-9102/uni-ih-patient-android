package com.demo.ck.videomodule;



import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.demo.ck.videomodule.util.VideoPlayerView;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.dom.CSSConstants;
import com.taobao.weex.dom.WXAttr;
import com.taobao.weex.layout.ContentBoxMeasurement;
import com.taobao.weex.ui.action.BasicComponentData;
import com.taobao.weex.ui.component.WXComponent;
import com.taobao.weex.ui.component.WXComponentProp;
import com.taobao.weex.ui.component.WXVContainer;
import com.taobao.weex.ui.view.WXBaseRefreshLayout;
import com.taobao.weex.utils.WXViewUtils;
import io.dcloud.common.DHInterface.IApp;
import io.dcloud.common.DHInterface.ISysEventListener;
import io.dcloud.common.DHInterface.IWebview;
import io.dcloud.common.DHInterface.ISysEventListener.SysEventType;
import io.dcloud.common.util.PdrUtil;
import io.dcloud.feature.weex.WeexInstanceMgr;
import io.dcloud.feature.weex_media.VideoInnerViewComponent;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class VideoComponent extends WXVContainer<VideoPlayerView> implements ISysEventListener {
    private WXAttr attrs;
    private Map<String, Object> params;
    private IApp mApp;
    private AtomicBoolean isLoad = new AtomicBoolean(false);

    public VideoComponent(WXSDKInstance instance, WXVContainer parent, BasicComponentData basicComponentData) {
        super(instance, parent, basicComponentData);
        this.attrs = basicComponentData.getAttrs();
        if (!basicComponentData.getStyles().containsKey("flex")) {
            this.setContentBoxMeasurement(new ContentBoxMeasurement() {
                public void measureInternal(float width, float height, int widthMeasureMode, int heightMeasureMode) {
                    if (CSSConstants.isUndefined(width)) {
                        this.mMeasureWidth = WXViewUtils.getRealPxByWidth(300.0F, VideoComponent.this.getInstance().getInstanceViewPortWidth());
                    }

                    if (CSSConstants.isUndefined(height)) {
                        this.mMeasureHeight = WXViewUtils.getRealPxByWidth(225.0F, VideoComponent.this.getInstance().getInstanceViewPortWidth());
                    }

                }

                public void layoutBefore() {
                }

                public void layoutAfter(float computedWidth, float computedHeight) {
                }
            });
        }

    }

    protected VideoPlayerView initComponentHostView(Context context) {
        IWebview webview = WeexInstanceMgr.self().findWebview(this.getInstance());
        if (webview != null) {
            this.mApp = webview.obtainApp();
            this.mApp.registerSysEventListener(this, SysEventType.onKeyUp);
        }

        return new VideoPlayerView(this.getContext(), this);
    }

    public ViewGroup getRealView() {
        return this.getHostView() != null ? ((VideoPlayerView)this.getHostView()).getPlayerView() : null;
    }

    public void addChild(WXComponent child, int index) {
        if (child instanceof VideoInnerViewComponent) {
            super.addChild(child, index);
        }

    }

    public IApp getIApp() {
        if (this.mApp == null) {
            IWebview webview = WeexInstanceMgr.self().findWebview(this.getInstance());
            if (webview != null) {
                this.mApp = webview.obtainApp();
            }
        }

        return this.mApp;
    }

    public void addSubView(View child, int index) {
        if (child != null && this.getRealView() != null) {
            if (!(child instanceof WXBaseRefreshLayout)) {
                int count = this.getRealView().getChildCount();
                int index1 = index >= count ? -1 : index;
                if (this.getRealView().indexOfChild(child) == -1) {
                    if (index1 == -1) {
                        this.getRealView().addView(child);
                    } else {
                        this.getRealView().addView(child, index1);
                    }
                }

                child.bringToFront();
            }
        }
    }

    @WXComponentProp(
            name = "src"
    )
    public void setSrc(String src) {
        if (!PdrUtil.isEmpty(src)) {

            Log.i("chenkaid","2222222222222");
            ((VideoPlayerView)this.getHostView()).setSrc(src);
        }
    }

    @WXComponentProp(
            name = "autoplay"
    )
    public void setAutoPlay(boolean autoPlay) {
        if (!PdrUtil.isEmpty(autoPlay)) {
            ((VideoPlayerView)this.getHostView()).setAutoplay(autoPlay);
        }
    }

    @WXComponentProp(
            name = "initialTime"
    )
    public void setInitTime(float time) {
        ((VideoPlayerView)this.getHostView()).setInitialTime(time);
    }

    @WXComponentProp(
            name = "duration"
    )
    public void setDuration(float duration) {
        ((VideoPlayerView)this.getHostView()).setDuration(duration);
    }

    @WXComponentProp(
            name = "danmuList"
    )
    public void setDanmuList(JSONArray list) {
        ((VideoPlayerView)this.getHostView()).setDanmuList(list);
    }

    @WXComponentProp(
            name = "loop"
    )
    public void setLoop(boolean isloop) {
        ((VideoPlayerView)this.getHostView()).setLoop(isloop);
    }

    @WXComponentProp(
            name = "muted"
    )
    public void setMute(boolean isMute) {
        ((VideoPlayerView)this.getHostView()).setMuted(isMute);
    }

    @WXComponentProp(
            name = "direction"
    )
    public void setDirection(int direction) {
        ((VideoPlayerView)this.getHostView()).setDirection(direction);
    }

    @WXComponentProp(
            name = "objectFit"
    )
    public void setFit(String type) {
        if (!PdrUtil.isEmpty(type)) {
            ((VideoPlayerView)this.getHostView()).setObjectFit(type);
        }
    }

    @WXComponentProp(
            name = "showMuteBtn"
    )
    public void isShowMuteBtn(boolean isshow) {
        ((VideoPlayerView)this.getHostView()).setMuteBtn(isshow);
    }

    @WXComponentProp(
            name = "playBtnPosition"
    )
    public void setPlayBtnPosition(String position) {
        if (!PdrUtil.isEmpty(position)) {
            ((VideoPlayerView)this.getHostView()).setPlayBtnPosition(position);
        }
    }

    @WXComponentProp(
            name = "title"
    )
    public void setTitle(String title) {
        if (!PdrUtil.isEmpty(title)) {
            ((VideoPlayerView)this.getHostView()).setTitle(title);
        }
    }

    public void updateProperties(Map<String, Object> props) {
        if (props.size() > 0) {
            this.params = this.combinMap(this.params, props);
            ((VideoPlayerView)this.getHostView()).setProgress(!this.params.containsKey("showProgress") || Boolean.parseBoolean(this.params.get("showProgress").toString()));
            ((VideoPlayerView)this.getHostView()).setShowFullScreenBtn(!this.params.containsKey("showFullscreenBtn") || Boolean.parseBoolean(this.params.get("showFullscreenBtn").toString()));
            ((VideoPlayerView)this.getHostView()).setPlayBtnVisibility(!this.params.containsKey("showPlayBtn") || Boolean.parseBoolean(this.params.get("showPlayBtn").toString()));
            ((VideoPlayerView)this.getHostView()).setEnableProgressGesture(!this.params.containsKey("enableProgressGesture") || Boolean.parseBoolean(this.params.get("enableProgressGesture").toString()));
            if (props.containsKey("src")) {
                ((VideoPlayerView)this.getHostView()).setSrc((String)props.get("src"));
            }

            ((VideoPlayerView)this.getHostView()).setShowCenterPlayBtn(!this.params.containsKey("showCenterPlayBtn") || Boolean.parseBoolean(this.params.get("showCenterPlayBtn").toString()));
            ((VideoPlayerView)this.getHostView()).setPageGesture(!this.params.containsKey("vslideGestureInFullscreen") || Boolean.parseBoolean(this.params.get("vslideGestureInFullscreen").toString()));
            ((VideoPlayerView)this.getHostView()).setControls(!this.params.containsKey("controls") || Boolean.parseBoolean(this.params.get("controls").toString()));
        }

        super.updateProperties(props);
        if (props.size() > 0 && props.containsKey("src")) {
            ((VideoPlayerView)this.getHostView()).onLayoutFinished();
        }

    }

    private Map<String, Object> combinMap(Map<String, Object> main, Map<String, Object> second) {
        if (main == null && second == null) {
            return new HashMap();
        } else if (main == null) {
            return second;
        } else if (second == null) {
            return main;
        } else {
            main.putAll(second);
            return main;
        }
    }

    @WXComponentProp(
            name = "poster"
    )
    public void setPoster(String poster) {
        ((VideoPlayerView)this.getHostView()).setPoster(poster);
    }

    @JSMethod
    public void requestFullScreen(JSONObject param) {
        int direction;
        try {
            direction = param.getInteger("direction");
        } catch (Exception var4) {
            direction = -90;
        }

        ((VideoPlayerView)this.getHostView()).requestFullScreen(direction);
    }

    @JSMethod
    public void play() {
        Log.i("chenkaid","11111111111111111");
        ((VideoPlayerView)this.getHostView()).play();
    }

    @JSMethod
    public void pause() {
        ((VideoPlayerView)this.getHostView()).pause();
    }

    @JSMethod
    public void stop() {
        ((VideoPlayerView)this.getHostView()).stop();
    }

    @JSMethod
    public void seek(int position) {
        if (!PdrUtil.isEmpty(position)) {
            ((VideoPlayerView)this.getHostView()).seek(position * 1000);
        }
    }

    @JSMethod
    public void sendDanmu(JSONObject danmu) {
        if (danmu != null) {
            ((VideoPlayerView)this.getHostView()).sendDanmu(danmu);
        }

    }

    @JSMethod
    public void playbackRate(float rate) {
        if (!PdrUtil.isEmpty(rate)) {
            ((VideoPlayerView)this.getHostView()).sendPlayBackRate(String.valueOf(rate));
        }
    }

    @JSMethod
    public void exitFullScreen() {
        ((VideoPlayerView)this.getHostView()).exitFullScreen();
    }

    protected void onHostViewInitialized(VideoPlayerView host) {
        super.onHostViewInitialized(host);
        if (this.attrs != null && this.attrs.size() > 0) {
            ((VideoPlayerView)this.getHostView()).setEnableDanmu(this.attrs.containsKey("enableDanmu") && Boolean.parseBoolean(this.attrs.get("enableDanmu").toString()));
            ((VideoPlayerView)this.getHostView()).setDanmuBtn(this.attrs.containsKey("danmuBtn") && Boolean.parseBoolean(this.attrs.get("danmuBtn").toString()));
        }

    }

    public void onActivityResume() {
        super.onActivityResume();
    }

    public void onActivityPause() {
        super.onActivityPause();
        if (this.getHostView() != null) {
            ((VideoPlayerView)this.getHostView()).pause();
        }

    }

    public void destroy() {
        super.destroy();
        if (this.getHostView() != null) {
            ((VideoPlayerView)this.getHostView()).destory();
        }

        if (this.mApp != null) {
            this.mApp.unregisterSysEventListener(this, SysEventType.onKeyUp);
        }

    }

    public boolean onExecute(SysEventType pEventType, Object pArgs) {
        if (pEventType == SysEventType.onKeyUp) {
            Object[] _args = (Object[])((Object[])pArgs);
            int keyCode = (Integer)_args[0];
            if (keyCode == 4 && ((VideoPlayerView)this.getHostView()).isFullScreen() && this.getHostView() != null) {
                return ((VideoPlayerView)this.getHostView()).onBackPress();
            }
        }

        return false;
    }

    public void addEvent(String type) {
        if (!type.equals("click")) {
            super.addEvent(type);
        }
    }
}

