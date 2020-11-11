package com.demo.ck.videomodule.util;


import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.net.Uri;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.demo.ck.videomodule.VideoComponent;
import com.nostra13.dcloudimageloader.core.ImageLoaderL;
import com.taobao.weex.WXSDKInstance.FrameViewEventListener;
import com.taobao.weex.bridge.WXBridgeManager;
import com.taobao.weex.ui.action.GraphicSize;
import com.taobao.weex.ui.component.WXComponent;
import io.dcloud.common.util.FileUtil;
import io.dcloud.common.util.PdrUtil;
import io.dcloud.feature.weex_media.VideoInnerViewComponent;
import io.dcloud.media.weex.weex_video.ijkplayer.OnPlayerChangedListener;
import io.dcloud.media.weex.weex_video.ijkplayer.VideoR;
import io.dcloud.media.weex.weex_video.ijkplayer.media.AssetsDataSourceProvider;
import java.util.HashMap;
import java.util.Map;
import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IMediaPlayer.OnBufferingUpdateListener;
import tv.danmaku.ijk.media.player.IMediaPlayer.OnInfoListener;

public class VideoPlayerView extends FrameLayout implements OnInfoListener, OnPlayerChangedListener, OnBufferingUpdateListener {
    private String mSrc = "";
    private String willBeSetSrc = "";
    private boolean autoplay = false;
    private boolean loop = false;
    private String poster = "";
    private float duration = -1.0F;
    private float initialTime = 0.0F;
    private boolean isFinishLayout = false;
    private int seek = 0;
    private IjkPlayerView mPlayerView;
    private VideoComponent mComponent;
    private Context mContext;
    private FrameLayout subViewContainer;
    private boolean isCreate = false;
    private boolean isEnableDanmu = false;
    private boolean isEnableDanmuBtn = false;
    private GraphicSize originalSize = null;
    private GraphicSize fullScreenSize = null;

    public VideoPlayerView(Context context, VideoComponent component) {
        super(context);
        this.mContext = context;
        this.mComponent = component;
        this.subViewContainer = new FrameLayout(context);
        this.addView(this.subViewContainer, new LayoutParams(-1, -1));
        this.createVideoView();
        this.subViewContainer.bringToFront();
        if (component.getInstance().isFrameViewShow()) {
            this.mPlayerView.setVideoVisibility();
        } else {
            component.getInstance().addFrameViewEventListener(new FrameViewEventListener() {
                public void onShowAnimationEnd() {
                    VideoPlayerView.this.mPlayerView.setVideoVisibility();
                    VideoPlayerView.this.mComponent.getInstance().removeFrameViewEventListener(this);
                }
            });
        }

    }

    public void createVideoView() {
        if (!this.isCreate) {
            this.isCreate = true;
            this.mPlayerView = new IjkPlayerView(this.mContext, (AttributeSet)null, this);
            this.mPlayerView.setComponent(this.mComponent);
            this.addView(this.mPlayerView, new LayoutParams(-1, -1));
            this.mPlayerView.init().setPlayerRootView(this);
            this.mPlayerView.setOnInfoListener(this);
            this.mPlayerView.setOnPlayerChangedListener(this);
            this.mPlayerView.setOnBufferingUpdateListener(this);
            if (this.fullScreenSize == null) {
                View view = ((Activity)this.mComponent.getInstance().getContext()).getWindow().getDecorView().findViewById(16908290);
                this.fullScreenSize = new GraphicSize((float)view.getWidth(), (float)view.getHeight());
            }

        }
    }

    public ViewGroup getPlayerView() {
        return this.subViewContainer;
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (PdrUtil.isEmpty(this.mComponent.getStyles().getBackgroundColor())) {
            this.setBackgroundColor(-16777216);
        }

    }

    public void onLayoutFinished() {
        if (this.mPlayerView != null) {
            AssetsDataSourceProvider fd = null;
            if (!PdrUtil.isNetPath(this.willBeSetSrc)) {
                this.willBeSetSrc = this.mComponent.getInstance().rewriteUri(Uri.parse(this.willBeSetSrc), "video").getPath();
                if (this.willBeSetSrc != null && !PdrUtil.isDeviceRootDir(this.willBeSetSrc)) {
                    try {
                        if (this.mComponent.getIApp() != null) {
                            if (!this.willBeSetSrc.startsWith("/")) {
                                this.willBeSetSrc = "/" + this.willBeSetSrc;
                            }

                            this.willBeSetSrc = this.mComponent.getIApp().checkPrivateDirAndCopy2Temp(this.willBeSetSrc);
                            this.willBeSetSrc = this.mComponent.getInstance().rewriteUri(Uri.parse(this.willBeSetSrc), "video").getPath();
                        } else {
                            if (this.willBeSetSrc.startsWith("/")) {
                                this.willBeSetSrc = this.willBeSetSrc.replaceFirst("/", "");
                            }

                            fd = new AssetsDataSourceProvider(this.mComponent.getContext().getAssets().openFd(this.willBeSetSrc));
                        }
                    } catch (Exception var3) {
                        var3.printStackTrace();
                    }
                }

                if (!FileUtil.checkPrivatePath(this.getContext(), this.willBeSetSrc)) {
                    Uri fileUri = FileUtil.getVideoFileContentUri(this.getContext(), this.willBeSetSrc);
                    if (fileUri != null) {
                        this.willBeSetSrc = fileUri.toString();
                    }
                }
            }

            if (TextUtils.isEmpty(this.mSrc)) {
                if (fd == null) {
                    this.mPlayerView.setVideoPath(this.willBeSetSrc);
                } else {
                    this.mPlayerView.setVideoFileDescriptor(fd);
                }

                this.mPlayerView.clearDanma();
            } else if (!this.mSrc.equalsIgnoreCase(this.willBeSetSrc)) {
                if (fd == null) {
                    this.mPlayerView.switchVideoPath(this.willBeSetSrc);
                } else {
                    this.mPlayerView.switchVideoFileDescriptor(fd);
                }

                this.mPlayerView.clearDanma();
            }

            this.mSrc = this.willBeSetSrc;
            this.mPlayerView.setDuration((int)this.duration);
            if (this.initialTime > 0.0F) {
                this.mPlayerView.seekTo((int)this.initialTime);
            }

            this.mPlayerView.enableDanmaku(this.isEnableDanmu);
            this.mPlayerView.enableDanmuBtn(this.isEnableDanmuBtn);
            this.isFinishLayout = true;
            if (this.autoplay) {
                this.play();
            }

        }
    }

    public void requestFullScreen(int oritation) {
        if (PdrUtil.isEmpty(oritation)) {
            oritation = 90;
        }

        if (this.mPlayerView != null) {
            this.mPlayerView.fullScreen(oritation);
        }

    }

    public void exitFullScreen() {
        if (this.mPlayerView != null) {
            this.mPlayerView.exitFullScreen();
        }

    }

    public void play() {
        if (this.mPlayerView != null) {
            this.mPlayerView.start();
        }

    }

    public void pause() {
        if (this.mPlayerView != null) {
            this.mPlayerView.pause();
        }

    }

    public void resume() {
        if (this.mPlayerView != null) {
            this.mPlayerView.onResume();
        }

    }

    public void stop() {
        if (this.mPlayerView != null) {
            this.mPlayerView.stop();
        }

    }

    public void seek(int position) {
        if (this.mPlayerView != null) {
            this.mPlayerView.seekTo(this.seek = position);
        }

    }

    public void sendDanmu(JSONObject danmu) {
        if (this.mPlayerView != null) {
            this.mPlayerView.sendDanmaku(new org.json.JSONObject(danmu), true);
        }

    }

    public void sendPlayBackRate(String rate) {
        if (this.mPlayerView != null) {
            this.mPlayerView.playbackRate(rate);
        }

    }

    public void destory() {
        if (this.mPlayerView != null) {
            this.mPlayerView.onDestroy();
            this.mPlayerView = null;
        }

    }

    public void setSrc(String mSrc) {
        if (!TextUtils.isEmpty(mSrc)) {
            this.willBeSetSrc = mSrc;
            if (this.isFinishLayout) {
                this.onLayoutFinished();
            }

        }
    }

    public void setAutoplay(boolean autoplay) {
        this.autoplay = autoplay;
    }

    public void setLoop(boolean loop) {
        this.loop = loop;
    }

    public void setPoster(String poster) {
        if (this.mPlayerView != null && !TextUtils.isEmpty(poster) && !this.poster.equalsIgnoreCase(poster)) {
            ImageLoaderL.getInstance().displayImage(poster, this.mPlayerView.mPlayerThumb);
            this.poster = poster;
        }

    }

    public void setProgress(boolean isShow) {
        if (this.mPlayerView != null) {
            this.mPlayerView.setProgressVisibility(isShow);
        }

    }

    public void setPlayBtnVisibility(boolean isshow) {
        if (this.mPlayerView != null) {
            this.mPlayerView.setPlayBntVisibility(isshow);
        }

    }

    public void setMuted(boolean muted) {
        if (this.mPlayerView != null) {
            this.mPlayerView.setMutePlayer(muted);
        }

    }

    public void setControls(boolean controls) {
        if (this.mPlayerView != null) {
            this.mPlayerView.setControls(controls);
        }

    }

    public void setPageGesture(boolean pageGesture) {
        if (this.mPlayerView != null) {
            this.mPlayerView.setPageGesture(pageGesture);
        }

    }

    public void setShowFullScreenBtn(boolean showFullScreenBtn) {
        if (this.mPlayerView != null) {
            this.mPlayerView.setFullscreenBntVisibility(showFullScreenBtn);
        }

    }

    public void setEnableProgressGesture(boolean enableProgressGesture) {
        if (this.mPlayerView != null) {
            this.mPlayerView.setIsEnableProgressGesture(enableProgressGesture);
        }

    }

    public void setDirection(int direction) {
        if (this.mPlayerView != null) {
            this.mPlayerView.setDirection(direction);
        }

    }

    public void setEnableDanmu(boolean enableDanmu) {
        if (this.mPlayerView != null) {
            this.isEnableDanmu = enableDanmu;
            this.mPlayerView.enableDanmaku(enableDanmu);
        }

    }

    public void setDanmuBtn(boolean danmuBtn) {
        if (this.mPlayerView != null) {
            this.isEnableDanmuBtn = danmuBtn;
            this.mPlayerView.enableDanmuBtn(danmuBtn);
        }

    }

    public void setDanmuList(JSONArray danmuList) {
        if (this.mPlayerView != null) {
            this.mPlayerView.setmDanmuList(danmuList.toString());
        }

    }

    public void setObjectFit(String objectFit) {
        if (!TextUtils.isEmpty(objectFit)) {
            if (this.mPlayerView != null) {
                this.mPlayerView.setScaleType(objectFit);
            }

        }
    }

    public void setMuteBtn(boolean isshow) {
        if (this.mPlayerView != null) {
            this.mPlayerView.isMuteBtnShow(isshow);
        }

    }

    public void setTitle(String title) {
        if (!PdrUtil.isEmpty(title)) {
            if (this.mPlayerView != null) {
                this.mPlayerView.setTitle(title);
            }

        }
    }

    public void setPlayBtnPosition(String position) {
        if (this.mPlayerView != null) {
            this.mPlayerView.setPlayBtnPosition(position);
        }

    }

    public void setShowCenterPlayBtn(boolean showCenterPlayBtn) {
        if (this.mPlayerView != null) {
            this.mPlayerView.setCenterPlayBtnVisibility(showCenterPlayBtn);
        }

    }

    public void setCodec(String codec) {
        if (this.mPlayerView != null) {
            this.mPlayerView.isUseMediaCodec(codec.equals("hardware"));
        }

    }

    public void setDuration(float duration) {
        if (this.mPlayerView != null) {
            this.duration = duration * 1000.0F;
            if (this.isFinishLayout) {
                this.mPlayerView.setDuration((int)this.duration);
            }
        }

    }

    public void setInitialTime(float initialTime) {
        if (this.mPlayerView != null) {
            if (initialTime <= 0.0F) {
                return;
            }

            this.initialTime = initialTime * 1000.0F;
            if (this.isFinishLayout) {
                this.mPlayerView.seekTo((int)this.initialTime);
            }
        }

    }

    public void onChanged(String type, String msg) {
        Map<String, Object> values = new HashMap();
        if (!TextUtils.isEmpty(msg)) {
            try {
                values.put("detail", JSON.parse(msg));
            } catch (Exception var10) {
                values.put("detail", msg);
            }
        }

        this.execCallBack(type, values);
        if (this.mPlayerView != null) {
            WXComponent child = this.mComponent.getChild(0);
            if (child instanceof VideoInnerViewComponent) {
                if (this.originalSize == null) {
                    this.originalSize = child.getLayoutSize();
                }

                if (type.equals("fullscreenchange")) {
                    if (this.isFullScreen()) {
                        View view = ((Activity)this.mComponent.getInstance().getContext()).getWindow().getDecorView().findViewById(16908290);
                        GraphicSize screenSize = new GraphicSize((float)view.getMeasuredWidth(), (float)view.getMeasuredHeight());
                        if (!screenSize.equals(this.fullScreenSize)) {
                            this.fullScreenSize = screenSize;
                        }

                        float width = this.fullScreenSize.getWidth();
                        float height = this.fullScreenSize.getHeight();
                        float a;
                        if (this.mPlayerView.orientation == 0) {
                            if (width > height) {
                                a = width;
                                width = height;
                                height = a;
                            }
                        } else if (Math.abs(this.mPlayerView.orientation) == 90 && width < height) {
                            a = width;
                            width = height;
                            height = a;
                        }

                        WXBridgeManager.getInstance().setStyleHeight(child.getInstanceId(), child.getRef(), height);
                        WXBridgeManager.getInstance().setStyleWidth(child.getInstanceId(), child.getRef(), width);
                        this.removeView(this.subViewContainer);
                        this.mPlayerView.addView(this.subViewContainer);
                        this.subViewContainer.bringToFront();
                    } else {
                        WXBridgeManager.getInstance().setStyleHeight(child.getInstanceId(), child.getRef(), this.originalSize.getHeight());
                        WXBridgeManager.getInstance().setStyleWidth(child.getInstanceId(), child.getRef(), this.originalSize.getWidth());
                        this.mPlayerView.removeView(this.subViewContainer);
                        this.removeView(this.subViewContainer);
                        this.addView(this.subViewContainer);
                        this.subViewContainer.bringToFront();
                    }
                }
            }

        }
    }

    public boolean isFullScreen() {
        return this.mPlayerView != null && this.mPlayerView.isFullscreen();
    }

    public boolean onBackPress() {
        return this.mPlayerView != null && this.mPlayerView.onBackPressed();
    }

    public boolean onInfo(IMediaPlayer iMediaPlayer, int status, int i1) {
        switch(status) {
            case 331:
                this.execCallBack("error", new HashMap());
                break;
            case 332:
            case 701:
                this.execCallBack("waiting", new HashMap());
                break;
            case 334:
                this.execCallBack("play", new HashMap());
                break;
            case 335:
                this.execCallBack("pause", new HashMap());
                break;
            case 336:
                if (this.loop) {
                    this.play();
                }

                this.execCallBack("ended", new HashMap());
        }

        return false;
    }

    private void execCallBack(String type, Map<String, Object> values) {
        if (this.mComponent.getEvents().contains(type)) {
            this.mComponent.fireEvent(type, values);
        }

    }

    public void onBufferingUpdate(IMediaPlayer iMediaPlayer, int i) {
        Map<String, Object> values = new HashMap();
        Map<String, Object> detail = new HashMap();
        detail.put("buffered", i);
        values.put("detail", detail);
        this.execCallBack("progress", values);
    }

    public int getDirection() {
        if (this.mComponent != null && this.mComponent.getAttrs().containsKey("direction")) {
            try {
                return Integer.parseInt(String.valueOf(this.mComponent.getAttrs().get("direction")));
            } catch (Exception var2) {
                return -2147483648;
            }
        } else {
            return -2147483648;
        }
    }
}

