package com.demo.ck.videomodule.util;



import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.LayerDrawable;
import android.media.AudioManager;
import android.media.AudioManager.OnAudioFocusChangeListener;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.os.Build.VERSION;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseArray;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.OrientationEventListener;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.GestureDetector.OnGestureListener;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.SeekBar.OnSeekBarChangeListener;
import com.dcloud.android.annotation.IntDef;
import com.dcloud.android.v4.view.MotionEventCompat;
import com.taobao.weex.ui.component.WXVContainer;
import com.taobao.weex.utils.WXDataStructureUtil;
import com.taobao.weex.utils.WXViewUtils;
import io.dcloud.common.adapter.util.DeviceInfo;
import io.dcloud.common.util.PdrUtil;
import io.dcloud.feature.ui.navigator.QueryNotchTool;
import io.dcloud.media.weex.weex_video.ijkplayer.OnPlayerChangedListener;
import io.dcloud.media.weex.weex_video.ijkplayer.VideoR;
import io.dcloud.media.weex.weex_video.ijkplayer.danmaku.BaseDanmakuConverter;
import io.dcloud.media.weex.weex_video.ijkplayer.danmaku.BiliDanmukuParser;
import io.dcloud.media.weex.weex_video.ijkplayer.danmaku.OnDanmakuListener;
import io.dcloud.media.weex.weex_video.ijkplayer.danmaku.StandardDanmaKuParser;
import io.dcloud.media.weex.weex_video.ijkplayer.media.AssetsDataSourceProvider;
import io.dcloud.media.weex.weex_video.ijkplayer.utils.MotionEventUtils;
import io.dcloud.media.weex.weex_video.ijkplayer.utils.NetWorkUtils;
import io.dcloud.media.weex.weex_video.ijkplayer.utils.StringUtils;
import io.dcloud.media.weex.weex_video.ijkplayer.widgets.MarqueeTextView;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Arrays;
import java.util.Map;
import master.flame.danmaku.controller.IDanmakuView;
import master.flame.danmaku.controller.DrawHandler.Callback;
import master.flame.danmaku.danmaku.loader.ILoader;
import master.flame.danmaku.danmaku.loader.IllegalDataException;
import master.flame.danmaku.danmaku.loader.android.DanmakuLoaderFactory;
import master.flame.danmaku.danmaku.model.BaseDanmaku;
import master.flame.danmaku.danmaku.model.DanmakuTimer;
import master.flame.danmaku.danmaku.model.android.DanmakuContext;
import master.flame.danmaku.danmaku.model.android.Danmakus;
import master.flame.danmaku.danmaku.parser.BaseDanmakuParser;
import master.flame.danmaku.danmaku.parser.IDataSource;
import org.json.JSONObject;
import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkLibLoader;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;
import tv.danmaku.ijk.media.player.IMediaPlayer.OnBufferingUpdateListener;
import tv.danmaku.ijk.media.player.IMediaPlayer.OnCompletionListener;
import tv.danmaku.ijk.media.player.IMediaPlayer.OnErrorListener;
import tv.danmaku.ijk.media.player.IMediaPlayer.OnInfoListener;
import tv.danmaku.ijk.media.player.IMediaPlayer.OnPreparedListener;

public class IjkPlayerView extends FrameLayout implements OnClickListener {
    private static final int MAX_VIDEO_SEEK = 1000;
    private static final int DEFAULT_HIDE_TIMEOUT = 5000;
    private static final int MSG_UPDATE_SEEK = 10086;
    private static final int MSG_UPDATE_TIME = 10099;
    private static final int MSG_ENABLE_ORIENTATION = 10087;
    private static final int MSG_TRY_RELOAD = 10088;
    private static final int INVALID_VALUE = -1;
    private static final int INTERVAL_TIME = 1000;
    private IjkVideoView mVideoView;
    public ImageView mPlayerThumb;
    private ProgressBar mLoadingView;
    private TextView mTvVolume;
    private TextView mTvBrightness;
    private TextView mTvFastForward;
    private FrameLayout mFlTouchLayout;
    private ImageView mIvBack;
    private MarqueeTextView mTvTitle;
    private LinearLayout mFullscreenTopBar;
    private ImageView mIvPlay;
    private ImageView mIvPlayCircle;
    private TextView mTvCurTime;
    private SeekBar mPlayerSeek;
    private TextView mTvEndTime;
    private ImageView mIvFullscreen;
    private LinearLayout mLlBottomBar;
    private FrameLayout mFlVideoBox;
    private TextView mTvRecoverScreen;
    private ImageView mIVMute;
    private ImageView mIvPlayCenter;
    private Activity mAttachActivity;
    private WXVContainer component;
    private VideoPlayerView parentView;
    @SuppressLint({"HandlerLeak"})
    private Handler mHandler;
    private AudioManager mAudioManager;
    private GestureDetector mGestureDetector;
    private int mMaxVolume;
    private boolean mIsForbidTouch;
    private boolean mIsShowBar;
    private boolean mIsFullscreen;
    private boolean mIsPlayComplete;
    private boolean mIsSeeking;
    private long mTargetPosition;
    private int mCurPosition;
    private int mCurVolume;
    private float mCurBrightness;
    private int mInitHeight;
    private int mWidthPixels;
    private int mScreenUiVisibility;
    private OrientationEventListener mOrientationListener;
    private boolean mIsNeverPlay;
    private OnInfoListener mOutsideInfoListener;
    private OnCompletionListener mCompletionListener;
    private boolean mIsForbidOrientation;
    private boolean mIsAlwaysFullScreen;
    private long mExitTime;
    private Matrix mVideoMatrix;
    private Matrix mSaveMatrix;
    private boolean mIsNeedRecoverScreen;
    private int mAspectOptionsHeight;
    private int mInterruptPosition;
    private boolean mIsReady;
    private boolean isRtmpUri;
    private boolean isPageGesture;
    private boolean isProgressGesture;
    private ViewGroup mRootLayout;
    private float defaultScreenBrightness;
    private boolean isPlayBtnCenter;
    private final OnSeekBarChangeListener mSeekListener;
    private Runnable mHideBarRunnable;
    private boolean isShowProgress;
    private boolean isPlayBtnVisibility;
    private boolean isCenterPlayBtnVisibility;
    String fullCallFormat;
    private ViewGroup.LayoutParams mRawParams;
    private int mOrientation;
    private int originOrientation;
    public int orientation;
    private int defaultSystemUI;
    private int defaultDisplayCutoutMode;
    String[] rates;
    private OnGestureListener mPlayerGestureListener;
    private Runnable mHideTouchViewRunnable;
    private OnTouchListener mPlayerTouchListener;
    String timeUpdateF;
    int duration;
    private boolean isMutePlayer;
    private boolean mIsRenderingStart;
    private boolean mIsBufferingStart;
    private OnInfoListener mInfoListener;
    private OnPlayerChangedListener mOnPlayerChangedListener;
    private OnBufferingUpdateListener bufferingUpdateListener;
    private OnBufferingUpdateListener onBufferingUpdateListener;
    private SparseArray<String> mVideoSource;
    private static final int NORMAL_STATUS = 501;
    private static final int INTERRUPT_WHEN_PLAY = 502;
    private static final int INTERRUPT_WHEN_PAUSE = 503;
    private int mVideoStatus;
    private static final int DANMAKU_TAG_BILI = 701;
    private static final int DANMAKU_TAG_ACFUN = 702;
    private static final int DANMAKU_TAG_CUSTOM = 703;
    private int mDanmakuTag;
    private IDanmakuView mDanmakuView;
    private TextView mIvDanmakuControl;
    private DanmakuContext mDanmakuContext;
    private BaseDanmakuParser mDanmakuParser;
    private ILoader mDanmakuLoader;
    private BaseDanmakuConverter mDanmakuConverter;
    private OnDanmakuListener mDanmakuListener;
    private boolean mIsEnableDanmaku;
    private int mDanmakuTextColor;
    private float mDanmakuTextSize;
    private int mDanmakuType;
    private int mBasicOptionsWidth;
    private int mMoreOptionsWidth;
    private long mDanmakuTargetPosition;
    private String mDanmuList;
    private IjkPlayerView.ScreenBroadcastReceiver mScreenReceiver;
    private IjkPlayerView.NetBroadcastReceiver mNetReceiver;
    private boolean mIsScreenLocked;
    private boolean mIsNetConnected;

    public IjkPlayerView(Context context) {
        this(context, (AttributeSet)null);
    }

    public IjkPlayerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mHandler = new Handler() {
            public void handleMessage(Message msg) {
                if (msg.what == 10086) {
                    int pos = IjkPlayerView.this._setProgress();
                    if (!IjkPlayerView.this.mIsSeeking && IjkPlayerView.this.mIsShowBar && IjkPlayerView.this.mVideoView.isPlaying()) {
                        msg = this.obtainMessage(10086);
                        this.sendMessageDelayed(msg, (long)(1000 - pos % 1000));
                    }
                } else if (msg.what == 10087) {
                    if (IjkPlayerView.this.mOrientationListener != null) {
                        IjkPlayerView.this.mOrientationListener.enable();
                    }
                } else if (msg.what == 10088) {
                    if (IjkPlayerView.this.mIsNetConnected) {
                        IjkPlayerView.this.reload();
                    }

                    msg = this.obtainMessage(10088);
                    this.sendMessageDelayed(msg, 3000L);
                } else if (msg.what == 10099) {
                    IjkPlayerView.this.progressCallBack();
                    msg = this.obtainMessage(10099);
                    this.sendMessageDelayed(msg, 250L);
                }

            }
        };
        this.mIsForbidTouch = false;
        this.mIsShowBar = true;
        this.mIsPlayComplete = false;
        this.mTargetPosition = -1L;
        this.mCurPosition = -1;
        this.mCurVolume = -1;
        this.mCurBrightness = -1.0F;
        this.mIsNeverPlay = true;
        this.mIsForbidOrientation = true;
        this.mIsAlwaysFullScreen = false;
        this.mExitTime = 0L;
        this.mVideoMatrix = new Matrix();
        this.mSaveMatrix = new Matrix();
        this.mIsNeedRecoverScreen = false;
        this.mIsReady = false;
        this.isRtmpUri = false;
        this.isPageGesture = false;
        this.isProgressGesture = true;
        this.isPlayBtnCenter = false;
        this.mSeekListener = new OnSeekBarChangeListener() {
            private long curPosition;

            public void onStartTrackingTouch(SeekBar bar) {
                IjkPlayerView.this.mIsSeeking = true;
                IjkPlayerView.this._showControlBar(3600000);
                IjkPlayerView.this.mHandler.removeMessages(10086);
                this.curPosition = (long)IjkPlayerView.this.mVideoView.getCurrentPosition();
            }

            public void onProgressChanged(SeekBar bar, int progress, boolean fromUser) {
                if (fromUser) {
                    long duration = (long)IjkPlayerView.this.getDuration();
                    IjkPlayerView.this.mTargetPosition = duration * (long)progress / 1000L;
                    int deltaTime = (int)((IjkPlayerView.this.mTargetPosition - this.curPosition) / 1000L);
                    String desc;
                    if (IjkPlayerView.this.mTargetPosition > this.curPosition) {
                        desc = StringUtils.generateTime(IjkPlayerView.this.mTargetPosition) + "/" + StringUtils.generateTime(duration) + "\n+" + deltaTime + "秒";
                    } else {
                        desc = StringUtils.generateTime(IjkPlayerView.this.mTargetPosition) + "/" + StringUtils.generateTime(duration) + "\n" + deltaTime + "秒";
                    }

                    IjkPlayerView.this._setFastForward(desc);
                }
            }

            public void onStopTrackingTouch(SeekBar bar) {
                IjkPlayerView.this._hideTouchView();
                IjkPlayerView.this.mIsSeeking = false;
                IjkPlayerView.this.seekTo((int)IjkPlayerView.this.mTargetPosition);
                IjkPlayerView.this.mTargetPosition = -1L;
                IjkPlayerView.this._setProgress();
                IjkPlayerView.this._showControlBar(5000);
            }
        };
        this.mHideBarRunnable = new Runnable() {
            public void run() {
                IjkPlayerView.this._hideAllView(false);
            }
        };
        this.isShowProgress = true;
        this.isPlayBtnVisibility = true;
        this.isCenterPlayBtnVisibility = true;
        this.fullCallFormat = "{fullScreen:%b, direction:'%s'}";
        this.mOrientation = -90;
        this.orientation = 90;
        this.defaultSystemUI = 0;
        this.defaultDisplayCutoutMode = 0;
        this.rates = new String[]{"0.5", "0.8", "1.0", "1.25", "1.5", "2.0"};
        this.mPlayerGestureListener = new SimpleOnGestureListener() {
            private boolean isDownTouch;
            private boolean isVolume;
            private boolean isLandscape;
            private boolean isRecoverFromDanmaku;

            public boolean onDown(MotionEvent e) {
                this.isDownTouch = true;
                this.isRecoverFromDanmaku = IjkPlayerView.this.recoverFromEditVideo();
                return super.onDown(e);
            }

            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                if (!IjkPlayerView.this.mIsForbidTouch && !IjkPlayerView.this.mIsNeverPlay) {
                    float mOldX = e1.getX();
                    float mOldY = e1.getY();
                    float deltaY = mOldY - e2.getY();
                    float deltaX = mOldX - e2.getX();
                    if (this.isDownTouch) {
                        this.isLandscape = Math.abs(distanceX) >= Math.abs(distanceY);
                        this.isVolume = mOldX > (float)IjkPlayerView.this.getResources().getDisplayMetrics().widthPixels * 0.5F;
                        this.isDownTouch = false;
                    }

                    if (this.isLandscape) {
                        IjkPlayerView.this._onProgressSlide(-deltaX / (float)IjkPlayerView.this.mVideoView.getWidth());
                    } else {
                        float percent = deltaY / (float)IjkPlayerView.this.mVideoView.getHeight();
                        if (this.isVolume) {
                            IjkPlayerView.this._onVolumeSlide(percent);
                        } else {
                            IjkPlayerView.this._onBrightnessSlide(percent);
                        }
                    }
                }

                return super.onScroll(e1, e2, distanceX, distanceY);
            }

            public boolean onSingleTapConfirmed(MotionEvent e) {
                if (IjkPlayerView.this.component != null && IjkPlayerView.this.component.getEvents().contains("click")) {
                    Map<String, Object> param = WXDataStructureUtil.newHashMapWithExpectedSize(1);
                    Map<String, Object> position = WXDataStructureUtil.newHashMapWithExpectedSize(4);
                    position.put("x", WXViewUtils.getWebPxByWidth(0.0F, IjkPlayerView.this.component.getInstance().getInstanceViewPortWidth()));
                    position.put("y", WXViewUtils.getWebPxByWidth(0.0F, IjkPlayerView.this.component.getInstance().getInstanceViewPortWidth()));
                    position.put("width", WXViewUtils.getWebPxByWidth(IjkPlayerView.this.component.getLayoutWidth(), IjkPlayerView.this.component.getInstance().getInstanceViewPortWidth()));
                    position.put("height", WXViewUtils.getWebPxByWidth(IjkPlayerView.this.component.getLayoutHeight(), IjkPlayerView.this.component.getInstance().getInstanceViewPortWidth()));
                    param.put("position", position);
                    IjkPlayerView.this.component.fireEvent("click", param);
                }

                if (IjkPlayerView.this.isFullscreen() && IjkPlayerView.this.component != null) {
                    JSONObject object = new JSONObject();

                    try {
                        object.put("screenX", (double)WXViewUtils.getWebPxByWidth(e.getX(), IjkPlayerView.this.component.getInstance().getInstanceViewPortWidth()));
                        object.put("screenY", (double)WXViewUtils.getWebPxByWidth(e.getY(), IjkPlayerView.this.component.getInstance().getInstanceViewPortWidth()));
                        object.put("screenWidth", (double)WXViewUtils.getWebPxByWidth((float)IjkPlayerView.this.getWidth(), IjkPlayerView.this.component.getInstance().getInstanceViewPortWidth()));
                        object.put("screenHeight", (double)WXViewUtils.getWebPxByWidth((float)IjkPlayerView.this.getHeight(), IjkPlayerView.this.component.getInstance().getInstanceViewPortWidth()));
                    } catch (Exception var4) {
                    }

                    IjkPlayerView.this.mOnPlayerChangedListener.onChanged("fullscreenclick", object.toString());
                }

                if (this.isRecoverFromDanmaku) {
                    return true;
                } else if (IjkPlayerView.this.mIsForbidTouch) {
                    return true;
                } else {
                    IjkPlayerView.this._toggleControlBar();
                    return true;
                }
            }

            public boolean onDoubleTap(MotionEvent e) {
                if (!IjkPlayerView.this.mIsNeverPlay && !this.isRecoverFromDanmaku) {
                    if (!IjkPlayerView.this.mIsForbidTouch) {
                        IjkPlayerView.this._refreshHideRunnable();
                        IjkPlayerView.this._togglePlayStatus();
                    }

                    return true;
                } else {
                    return true;
                }
            }
        };
        this.mHideTouchViewRunnable = new Runnable() {
            public void run() {
                IjkPlayerView.this._hideTouchView();
            }
        };
        this.mPlayerTouchListener = new OnTouchListener() {
            private static final int NORMAL = 1;
            private static final int INVALID_POINTER = 2;
            private static final int ZOOM_AND_ROTATE = 3;
            private int mode = 1;
            private PointF midPoint = new PointF(0.0F, 0.0F);
            private float degree = 0.0F;
            private int fingerFlag = -1;
            private float oldDist;
            private float scale;

            public boolean onTouch(View v, MotionEvent event) {
                switch(MotionEventCompat.getActionMasked(event)) {
                    case 0:
                        this.mode = 1;
                        IjkPlayerView.this.mHandler.removeCallbacks(IjkPlayerView.this.mHideBarRunnable);
                    case 1:
                    case 3:
                    case 4:
                    default:
                        break;
                    case 2:
                        if (this.mode == 3) {
                            float newRotate = MotionEventUtils.rotation(event, this.fingerFlag);
                            IjkPlayerView.this.mVideoView.setVideoRotation((int)(newRotate - this.degree));
                            IjkPlayerView.this.mVideoMatrix.set(IjkPlayerView.this.mSaveMatrix);
                            float newDist = MotionEventUtils.calcSpacing(event, this.fingerFlag);
                            this.scale = newDist / this.oldDist;
                            IjkPlayerView.this.mVideoMatrix.postScale(this.scale, this.scale, this.midPoint.x, this.midPoint.y);
                            IjkPlayerView.this.mVideoView.setVideoTransform(IjkPlayerView.this.mVideoMatrix);
                        }
                        break;
                    case 5:
                        if (event.getPointerCount() == 3 && IjkPlayerView.this.mIsFullscreen) {
                            IjkPlayerView.this._hideTouchView();
                            this.mode = 3;
                            MotionEventUtils.midPoint(this.midPoint, event);
                            this.fingerFlag = MotionEventUtils.calcFingerFlag(event);
                            this.degree = MotionEventUtils.rotation(event, this.fingerFlag);
                            this.oldDist = MotionEventUtils.calcSpacing(event, this.fingerFlag);
                            IjkPlayerView.this.mSaveMatrix = IjkPlayerView.this.mVideoView.getVideoTransform();
                        } else {
                            this.mode = 2;
                        }
                        break;
                    case 6:
                        if (this.mode == 3) {
                            IjkPlayerView.this.mIsNeedRecoverScreen = IjkPlayerView.this.mVideoView.adjustVideoView(this.scale);
                            if (IjkPlayerView.this.mIsNeedRecoverScreen && IjkPlayerView.this.mIsShowBar) {
                                IjkPlayerView.this.mTvRecoverScreen.setVisibility(VISIBLE);
                            }
                        }

                        this.mode = 2;
                }

                if (this.mode == 1) {
                    if (IjkPlayerView.this.mGestureDetector.onTouchEvent(event)) {
                        return true;
                    }

                    if (MotionEventCompat.getActionMasked(event) == 1) {
                        IjkPlayerView.this._endGesture();
                    }
                }

                return true;
            }
        };
        this.timeUpdateF = "{currentTime:%f,duration:%f}";
        this.duration = -1;
        this.isMutePlayer = false;
        this.mIsRenderingStart = false;
        this.mIsBufferingStart = false;
        this.mInfoListener = new OnInfoListener() {
            public boolean onInfo(IMediaPlayer iMediaPlayer, int status, int extra) {
                IjkPlayerView.this._switchStatus(status);
                if (IjkPlayerView.this.mOutsideInfoListener != null) {
                    IjkPlayerView.this.mOutsideInfoListener.onInfo(iMediaPlayer, status, extra);
                }

                return true;
            }
        };
        this.onBufferingUpdateListener = new OnBufferingUpdateListener() {
            public void onBufferingUpdate(IMediaPlayer iMediaPlayer, int i) {
                if (IjkPlayerView.this.bufferingUpdateListener != null) {
                    IjkPlayerView.this.bufferingUpdateListener.onBufferingUpdate(iMediaPlayer, i);
                }

            }
        };
        this.mVideoSource = new SparseArray();
        this.mVideoStatus = 501;
        this.mDanmakuTag = 701;
        this.mIsEnableDanmaku = false;
        this.mDanmakuTextColor = -1;
        this.mDanmakuTextSize = -1.0F;
        this.mDanmakuType = 1;
        this.mBasicOptionsWidth = -1;
        this.mMoreOptionsWidth = -1;
        this.mDanmakuTargetPosition = -1L;
        this.mDanmuList = "";
        this.mIsScreenLocked = false;
        this._initView(context);
    }

    public IjkPlayerView(Context context, AttributeSet attrs, VideoPlayerView parentView) {
        this(context, attrs);
        this.parentView = parentView;
    }

    public void setComponent(WXVContainer container) {
        this.component = container;
    }

    private void _initView(Context context) {
        if (context instanceof Activity) {
            this.mAttachActivity = (Activity)context;
            View.inflate(context, VideoR.VIDEO_IJK_LAYOUT_PLAYER_VIEW, this);
            this.mVideoView = (IjkVideoView)this.findViewById(VideoR.VIDEO_IJK_ID_VIDEO_VIEW);
            this.mPlayerThumb = (ImageView)this.findViewById(VideoR.VIDEO_IJK_ID_IV_THUMB);
            this.mLoadingView = (ProgressBar)this.findViewById(VideoR.VIDEO_IJK_ID_PD_LOADING);
            this.mTvVolume = (TextView)this.findViewById(VideoR.VIDEO_IJK_ID_TV_VOLUME);
            this.mTvBrightness = (TextView)this.findViewById(VideoR.VIDEO_IJK_ID_TV_BRIGHTNESS);
            this.mTvFastForward = (TextView)this.findViewById(VideoR.VIDEO_IJK_ID_TV_FAST_FORWARD);
            this.mFlTouchLayout = (FrameLayout)this.findViewById(VideoR.VIDEO_IJK_ID_FL_TOUCH_LAYOUT);
            this.mIvBack = (ImageView)this.findViewById(VideoR.VIDEO_IJK_ID_IV_BACK);
            this.mTvTitle = (MarqueeTextView)this.findViewById(VideoR.VIDEO_IJK_ID_TV_TITLE);
            this.mFullscreenTopBar = (LinearLayout)this.findViewById(VideoR.VIDEO_IJK_ID_FULLSCREEN_TOP_BAR);
            this.mIvPlay = (ImageView)this.findViewById(VideoR.VIDEO_IJK_ID_IV_PLAY);
            this.mTvCurTime = (TextView)this.findViewById(VideoR.VIDEO_IJK_ID_TV_CUR_TIME);
            this.mPlayerSeek = (SeekBar)this.findViewById(VideoR.VIDEO_IJK_ID_PLAYER_SEEK);
            this.mTvEndTime = (TextView)this.findViewById(VideoR.VIDEO_IJK_ID_TV_END_TIME);
            this.mIvFullscreen = (ImageView)this.findViewById(VideoR.VIDEO_IJK_ID_IV_FULLSCREEN);
            this.mLlBottomBar = (LinearLayout)this.findViewById(VideoR.VIDEO_IJK_ID_LL_BOTTOM_BAR);
            this.mFlVideoBox = (FrameLayout)this.findViewById(VideoR.VIDEO_IJK_ID_FL_VIDEO_BOX);
            this.mIvPlayCircle = (ImageView)this.findViewById(VideoR.VIDEO_IJK_ID_IV_PLAY_CIRCLE);
            this.mTvRecoverScreen = (TextView)this.findViewById(VideoR.VIDEO_IJK_ID_TV_RECOVER_SCREEN);
            this.mIvDanmakuControl = (TextView)this.findViewById(VideoR.VIDEO_IJK_ID_IV_DANMAKU_CONTROL);
            this.mIVMute = (ImageView)this.findViewById(VideoR.VIDEO_IJK_ID_IV_MUTE);
            this.mIvPlayCenter = (ImageView)this.findViewById(VideoR.VIDEO_IJK_ID_IV_PLAY_CENTER);
            this.mAspectOptionsHeight = this.getResources().getDimensionPixelSize(VideoR.VIDEO_IJK_DIMEN_ASPECT_BNT_SIZE) * 4;
            this._initReceiver();
            this.mIvPlay.setOnClickListener(this);
            this.mIvBack.setOnClickListener(this);
            this.mIvFullscreen.setOnClickListener(this);
            this.mIvPlayCircle.setOnClickListener(this);
            this.mTvRecoverScreen.setOnClickListener(this);
            this.mIVMute.setOnClickListener(this);
            this.mIvPlayCenter.setOnClickListener(this);
            this.setOnClickListener(this);
        } else {
            throw new IllegalArgumentException("Context must be Activity");
        }
    }
    @SuppressLint("WrongConstant")
    public void setVideoVisibility() {
        if (this.mVideoView != null) {
            this.mVideoView.setVisibility(0);
        }

    }

    private void setSeekBarColor() {
        LayerDrawable drawable = (LayerDrawable)this.mPlayerSeek.getProgressDrawable();
        drawable.findDrawableByLayerId(16908288).setColorFilter(Color.parseColor("#ff00ff"), Mode.SRC_ATOP);
        drawable.findDrawableByLayerId(16908303).setColorFilter(Color.parseColor("#ffff00"), Mode.SRC_ATOP);
        drawable.findDrawableByLayerId(16908301).setColorFilter(Color.parseColor("#00ffff"), Mode.SRC_ATOP);
        this.mPlayerSeek.getThumb().setColorFilter(Color.parseColor("#0000ff"), Mode.SRC_ATOP);
    }

    @SuppressLint("WrongConstant")
    private void _initMediaPlayer() {
        IjkMediaPlayer.loadLibrariesOnce((IjkLibLoader)null);
        IjkMediaPlayer.native_profileBegin("libijkplayer.so");
        this.mAudioManager = (AudioManager)this.mAttachActivity.getSystemService("audio");
        this.mMaxVolume = this.mAudioManager.getStreamMaxVolume(3);
        android.view.WindowManager.LayoutParams lp = this.mAttachActivity.getWindow().getAttributes();
        this.defaultScreenBrightness = lp.screenBrightness;
        this.mPlayerSeek.setMax(1000);
        this.mPlayerSeek.setOnSeekBarChangeListener(this.mSeekListener);
        this.mVideoView.setOnInfoListener(this.mInfoListener);
        this.mVideoView.setOnBufferingUpdateListener(this.onBufferingUpdateListener);
        this.mGestureDetector = new GestureDetector(this.mAttachActivity, this.mPlayerGestureListener);
        this.mFlVideoBox.setClickable(true);
        this.mFlVideoBox.setOnTouchListener(this.mPlayerTouchListener);
        this.mOrientationListener = new OrientationEventListener(this.mAttachActivity) {
            public void onOrientationChanged(int orientation) {
                IjkPlayerView.this._handleOrientation(orientation);
            }
        };
        if (this.mIsForbidOrientation) {
            this.mOrientationListener.disable();
        }

    }

    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (this.mInitHeight == 0) {
            this.mInitHeight = this.getHeight();
            this.mWidthPixels = this.getResources().getDisplayMetrics().widthPixels;
        }

    }

    public void onResume() {
        if (this.mIsScreenLocked) {
            this.mIsScreenLocked = false;
        }

        this.mVideoView.setRender(2);
        this.mVideoView.resume();
        if (!this.mIsForbidTouch && !this.mIsForbidOrientation) {
            this.mOrientationListener.enable();
        }

        if (this.mCurPosition != -1) {
            this.seekTo(this.mCurPosition);
            this.mCurPosition = -1;
        }

    }

    public void onPause() {
        this.mCurPosition = this.mVideoView.getCurrentPosition();
        this.mVideoView.pause();
        this.mIvPlay.setSelected(false);
        this.mIvPlayCenter.setSelected(false);
        this.mOrientationListener.disable();
        this._pauseDanmaku();
    }

    public int onDestroy() {
        int curPosition = this.mVideoView.getCurrentPosition();
        this.mVideoView.destroy();
        IjkMediaPlayer.native_profileEnd();
        if (this.mDanmakuView != null) {
            this.mDanmakuView.release();
            this.mDanmakuView = null;
        }

        if (this.mTvTitle != null) {
            this.mTvTitle.stopMotion();
        }

        this.mHandler.removeMessages(10088);
        this.mHandler.removeMessages(10086);
        this.mHandler.removeMessages(10099);
        this.mAttachActivity.unregisterReceiver(this.mScreenReceiver);
        this.mAttachActivity.unregisterReceiver(this.mNetReceiver);
        this.mAttachActivity.getWindow().clearFlags(128);
        this.setMutePlayer(false);
        this.mAudioManager.abandonAudioFocus((OnAudioFocusChangeListener)null);
        android.view.WindowManager.LayoutParams lp = this.mAttachActivity.getWindow().getAttributes();
        lp.screenBrightness = this.defaultScreenBrightness;
        this.mAttachActivity.getWindow().setAttributes(lp);
        return curPosition;
    }

    public boolean handleVolumeKey(int keyCode) {
        if (keyCode == 24) {
            this._setVolume(true);
            return true;
        } else if (keyCode == 25) {
            this._setVolume(false);
            return true;
        } else {
            return false;
        }
    }

    public boolean onBackPressed() {
        if (this.recoverFromEditVideo()) {
            return true;
        } else if (this.mIsAlwaysFullScreen) {
            return true;
        } else if (this.mIsFullscreen) {
            this.exitFullScreen();
            if (this.mIsForbidTouch) {
                this._setControlBarVisible(this.mIsShowBar);
            }

            return true;
        } else {
            return false;
        }
    }

    public IjkPlayerView init() {
        this._initMediaPlayer();
        return this;
    }

    public IjkPlayerView switchVideoPath(String url) {
        return this.switchVideoPath(Uri.parse(url));
    }

    public IjkPlayerView switchVideoPath(Uri uri) {
        this.reset();
        this._setControlBarVisible(true);
        this.duration = -1;
        return this.setVideoPath(uri);
    }

    public IjkPlayerView setVideoPath(String url) {
        return this.setVideoPath(Uri.parse(url));
    }

    public IjkPlayerView setPlayerRootView(ViewGroup rootView) {
        this.mRootLayout = rootView;
        return this;
    }

    public IjkPlayerView setVideoPath(Uri uri) {
        if (uri.toString().startsWith("rtmp:")) {
            this.isRtmpUri = true;
            this.mPlayerSeek.setEnabled(false);
            this.mPlayerSeek.setVisibility(INVISIBLE);
            this.mTvEndTime.setVisibility(INVISIBLE);
            this.mTvCurTime.setVisibility(INVISIBLE);
        } else {
            this.isRtmpUri = false;
            this.mPlayerSeek.setEnabled(true);
            this.mPlayerSeek.setVisibility(this.isShowProgress ? VISIBLE : INVISIBLE);
            this.mTvEndTime.setVisibility(VISIBLE);
            this.mTvCurTime.setVisibility(VISIBLE);
        }

        if (this.mCurPosition != -1) {
            this.seekTo(this.mCurPosition);
            this.mCurPosition = -1;
        } else {
            this.seekTo(0);
        }

        this.mVideoView.setVideoURI(uri);
        return this;
    }

    public IjkPlayerView setVideoFileDescriptor(AssetsDataSourceProvider fd) {
        if (this.mCurPosition != -1) {
            this.seekTo(this.mCurPosition);
            this.mCurPosition = -1;
        } else {
            this.seekTo(0);
        }

        this.mVideoView.setVideoFileDescriptor(fd);
        return this;
    }

    public IjkPlayerView switchVideoFileDescriptor(AssetsDataSourceProvider fd) {
        this.reset();
        this._setControlBarVisible(true);
        this.duration = -1;
        return this.setVideoFileDescriptor(fd);
    }

    public IjkPlayerView setTitle(String title) {
        this.mTvTitle.setText(title);
        return this;
    }

    public void isMuteBtnShow(boolean isShow) {
        if (isShow) {
            this.mIVMute.setVisibility(VISIBLE);
        } else {
            this.mIVMute.setVisibility(GONE);
        }

    }

    public void setPlayBtnPosition(String position) {
        this.isPlayBtnCenter = position.equals("center");
        if (this.isPlayBtnCenter) {
            this.mIvPlay.setVisibility(GONE);
        } else if (this.isPlayBtnVisibility) {
            this.mIvPlayCenter.setVisibility(GONE);
            this.mIvPlay.setVisibility(VISIBLE);
        }

    }

    public IjkPlayerView alwaysFullScreen() {
        this.mIsAlwaysFullScreen = true;
        this.fullScreen(this.mOrientation);
        this.mIvFullscreen.setVisibility(GONE);
        return this;
    }

    public void start() {
        if (this.mIsPlayComplete) {
            if (this.mDanmakuView != null && this.mDanmakuView.isPrepared()) {
                this.mDanmakuView.seekTo(0L);
                this.mDanmakuView.pause();
            }

            this.mIsPlayComplete = false;
        }

        if (!this.mVideoView.isPlaying()) {
            this.mIvPlay.setSelected(true);
            this.mIvPlayCenter.setSelected(true);
            this.mVideoView.start();
            this.mHandler.sendEmptyMessage(10086);
            this.mHandler.sendEmptyMessage(10099);
        }

        this.mIvPlayCircle.setVisibility(GONE);
        if (this.mIsNeverPlay) {
            this.mIsNeverPlay = false;
            if (this.mVideoView.getCurrentState() != 331) {
                this.mLoadingView.setVisibility(VISIBLE);
            }

            this.mIsShowBar = false;
            this._loadDanmaku();
        }

        this.mAttachActivity.getWindow().addFlags(128);
    }

    public void hiddenLoaded(boolean isHidden) {
    }

    public void reload() {
        this.mLoadingView.setVisibility(VISIBLE);
        if (this.mIsReady) {
            if (!NetWorkUtils.isNetworkAvailable(this.mAttachActivity)) {
                if (null != this.mOnPlayerChangedListener) {
                    this.mOnPlayerChangedListener.onChanged("error", "network error");
                    this.mLoadingView.setVisibility(GONE);
                }

                return;
            }

            this.mVideoView.reload();
            this.mVideoView.start();
            if (this.mInterruptPosition > 0) {
                this.seekTo(this.mInterruptPosition);
                this.mInterruptPosition = 0;
            }
        } else {
            this.mVideoView.release(false);
            this.mVideoView.setRender(2);
            this.start();
        }

        this.mHandler.removeMessages(10086);
        this.mHandler.sendEmptyMessage(10086);
        this.mHandler.removeMessages(10099);
        this.mHandler.sendEmptyMessage(10099);
    }

    public boolean isPlaying() {
        return this.mVideoView.isPlaying();
    }

    public void pause() {
        this.mCurPosition = this.mVideoView.getCurrentPosition();
        this.mIvPlay.setSelected(false);
        this.mIvPlayCenter.setSelected(false);
        if (this.mVideoView.isPlaying()) {
            this.mVideoView.pause();
        }

        this.mHandler.removeMessages(10099);
        this._pauseDanmaku();
        this.mAttachActivity.getWindow().clearFlags(128);
    }

    public void seekTo(int position) {
        if (!this.isRtmpUri) {
            this.mVideoView.seekTo(position);
            this.mDanmakuTargetPosition = (long)position;
        }
    }

    public void stop() {
        this.pause();
        this.mVideoView.stopPlayback();
    }

    public void reset() {
        if (this.mIsEnableDanmaku && this.mDanmakuView != null) {
            this.mDanmakuView.release();
            this.mDanmakuView = null;
            this.mIsEnableDanmaku = false;
        }

        this.mIsNeverPlay = true;
        this.mCurPosition = 0;
        this.stop();
        this.mVideoView.setRender(2);
    }

    private void _hideAllView(boolean isTouchLock) {
        this.mFlTouchLayout.setVisibility(GONE);
        this.mFullscreenTopBar.setVisibility(GONE);
        this.mTvTitle.stopMotion();
        this.mIvPlayCenter.setVisibility(GONE);
        if (this.mLlBottomBar.getVisibility() == VISIBLE) {
            this.mOnPlayerChangedListener.onChanged("controlstoggle", "{'show':false}");
        }

        this.mLlBottomBar.setVisibility(GONE);
        if (!isTouchLock) {
            this.mIsShowBar = false;
        }

        if (this.mIsNeedRecoverScreen) {
            this.mTvRecoverScreen.setVisibility(GONE);
        }

    }
    @SuppressLint("WrongConstant")
    private void _setControlBarVisible(boolean isShowBar) {
        if (this.mIsNeverPlay) {
            if (this.mIvPlayCircle.getVisibility() != VISIBLE) {
                this.mLlBottomBar.setVisibility(isShowBar ? VISIBLE : GONE);
            }
        } else if (!this.mIsForbidTouch) {
            this.mLlBottomBar.setVisibility(isShowBar ?  VISIBLE : GONE);
            if (this.isPlayBtnCenter && this.isPlayBtnVisibility) {
                this.mIvPlayCenter.setVisibility(isShowBar ?  VISIBLE : GONE);
            }

            if (this.mIsFullscreen) {
                this.mFullscreenTopBar.setVisibility(isShowBar ?  VISIBLE : GONE);
                if (isShowBar) {
                    this.mTvTitle.startMotion();
                } else {
                    this.mTvTitle.stopMotion();
                }

                if (this.mIsNeedRecoverScreen) {
                    this.mTvRecoverScreen.setVisibility(isShowBar ?  VISIBLE : GONE);
                }
            } else {
                this.mFullscreenTopBar.setVisibility(8);
                this.mTvTitle.stopMotion();
                if (this.mIsNeedRecoverScreen) {
                    this.mTvRecoverScreen.setVisibility(8);
                }
            }
        }

    }

    private void _toggleControlBar() {
        this.mIsShowBar = !this.mIsShowBar;
        this.mOnPlayerChangedListener.onChanged("controlstoggle", "{'show':" + this.mIsShowBar + "}");
        this._setControlBarVisible(this.mIsShowBar);
        this.mHandler.removeCallbacks(this.mHideBarRunnable);
        if (this.mIsShowBar) {
            this.mHandler.postDelayed(this.mHideBarRunnable, 5000L);
            this.mHandler.sendEmptyMessage(10086);
        }

    }

    private void _showControlBar(int timeout) {
        if (!this.mIsShowBar) {
            this._setProgress();
            this.mIsShowBar = true;
        }

        this._setControlBarVisible(true);
        this.mHandler.sendEmptyMessage(10086);
        this.mHandler.removeCallbacks(this.mHideBarRunnable);
        if (timeout != 0) {
            this.mHandler.postDelayed(this.mHideBarRunnable, (long)timeout);
        }

    }

    private void _togglePlayStatus() {
        if (this.mVideoView.isPlaying()) {
            this.pause();
        } else {
            this.start();
        }

    }

    private void _refreshHideRunnable() {
        this.mHandler.removeCallbacks(this.mHideBarRunnable);
        this.mHandler.postDelayed(this.mHideBarRunnable, 5000L);
    }

    public void setControls(boolean isControls) {
        this.mIsForbidTouch = !isControls;
        if (this.mIsForbidTouch) {
            this.mOrientationListener.disable();
            this._hideAllView(true);
        } else {
            if (!this.mIsForbidOrientation) {
                this.mOrientationListener.enable();
            }

            if (!this.mIsNeverPlay) {
                this.mIsShowBar = false;
                this._toggleControlBar();
            } else if (this.mIvPlayCircle.getVisibility() != VISIBLE) {
                this.mIsShowBar = false;
                this._toggleControlBar();
            } else {
                this.mIsShowBar = false;
                this.mLlBottomBar.setVisibility(GONE);
            }

            if (this.mIsNeedRecoverScreen) {
                this.mTvRecoverScreen.setVisibility(VISIBLE);
            }
        }

    }

    public void setPageGesture(boolean isPageGesture) {
        this.isPageGesture = isPageGesture;
    }

    public void setProgressVisibility(boolean isShow) {
        if (this.mPlayerSeek != null && !this.isRtmpUri) {
            this.isShowProgress = isShow;
            int v = isShow ? VISIBLE : INVISIBLE;
            this.mPlayerSeek.setVisibility(v);
        }

    }

    public void setFullscreenBntVisibility(boolean isShow) {
        if (this.mIvFullscreen != null) {
            int v = isShow ? VISIBLE : INVISIBLE;
            this.mIvFullscreen.setVisibility(v);
        }

    }

    public void setPlayBntVisibility(boolean isShow) {
        if (this.mIvPlay != null) {
            this.isPlayBtnVisibility = isShow;
            int v = isShow ? VISIBLE : INVISIBLE;
            if (this.isPlayBtnCenter) {
                this.mIvPlayCenter.setVisibility(v);
            } else {
                this.mIvPlay.setVisibility(v);
            }
        }

    }

    public void setCenterPlayBtnVisibility(boolean isShow) {
        if (this.mIsNeverPlay && !this.isPlaying() && this.mIvPlayCircle != null) {
            this.isCenterPlayBtnVisibility = isShow;
            int v = isShow ? VISIBLE : GONE;
            this.mIvPlayCircle.setVisibility(v);
        }

    }

    public void setIsEnableProgressGesture(boolean isProgressGesture) {
        this.isProgressGesture = isProgressGesture;
    }

    public void onClick(View v) {
        this._refreshHideRunnable();
        int id = v.getId();
        if (id == VideoR.VIDEO_IJK_ID_IV_BACK) {
            this.onBackPressed();
        } else if (id != VideoR.VIDEO_IJK_ID_IV_PLAY && id != VideoR.VIDEO_IJK_ID_IV_PLAY_CIRCLE && id != VideoR.VIDEO_IJK_ID_IV_PLAY_CENTER) {
            if (id == VideoR.VIDEO_IJK_ID_IV_FULLSCREEN) {
                this._toggleFullScreen();
            } else if (id == VideoR.VIDEO_IJK_ID_IV_DANMAKU_CONTROL) {
                this._toggleDanmakuShow();
            } else if (id == VideoR.VIDEO_IJK_ID_TV_RECOVER_SCREEN) {
                this.mVideoView.resetVideoView(true);
                this.mIsNeedRecoverScreen = false;
                this.mTvRecoverScreen.setVisibility(GONE);
            } else if (id == VideoR.VIDEO_IJK_ID_IV_MUTE) {
                this.isMutePlayer = !this.isMutePlayer;
                this.setMutePlayer(this.isMutePlayer);
                this.mIVMute.setSelected(this.isMutePlayer);
            } else if (v == this && this.component != null && this.component.getEvents().contains("click")) {
                Map<String, Object> param = WXDataStructureUtil.newHashMapWithExpectedSize(1);
                Map<String, Object> position = WXDataStructureUtil.newHashMapWithExpectedSize(4);
                int[] location = new int[2];
                v.getLocationOnScreen(location);
                position.put("x", WXViewUtils.getWebPxByWidth((float)location[0], this.component.getInstance().getInstanceViewPortWidth()));
                position.put("y", WXViewUtils.getWebPxByWidth((float)location[1], this.component.getInstance().getInstanceViewPortWidth()));
                position.put("width", WXViewUtils.getWebPxByWidth(this.component.getLayoutWidth(), this.component.getInstance().getInstanceViewPortWidth()));
                position.put("height", WXViewUtils.getWebPxByWidth(this.component.getLayoutHeight(), this.component.getInstance().getInstanceViewPortWidth()));
                param.put("position", position);
                this.component.fireEvent("click", param);
            }
        } else {
            this._togglePlayStatus();
        }

    }

    public IjkPlayerView enableOrientation() {
        this.mIsForbidOrientation = false;
        this.mOrientationListener.enable();
        return this;
    }

    private void _toggleFullScreen() {
        if (this.isFullscreen()) {
            this.exitFullScreen();
        } else {
            if (this.parentView != null) {
                int ori = this.parentView.getDirection();
                if (ori == -2147483648 && this.mVideoView != null) {
                    if (this.mVideoView.getVideoHeight() > this.mVideoView.getVideoWidth()) {
                        ori = 0;
                    } else {
                        ori = -90;
                    }
                }

                this.mOrientation = ori;
            }

            this.fullScreen(this.mOrientation);
        }

    }

    private void _setFullScreen(boolean isFullscreen) {
        this.mIsFullscreen = isFullscreen;
        this.mIvFullscreen.setSelected(isFullscreen);
        if (this.mOnPlayerChangedListener != null) {
            String msg = null;
            if (isFullscreen) {
                msg = String.format(this.fullCallFormat, true, "horizontal");
            } else {
                msg = String.format(this.fullCallFormat, false, "vertical");
            }

            final String finalMsg = msg;
            this.mHandler.postDelayed(new Runnable() {
                public void run() {
                    IjkPlayerView.this.mOnPlayerChangedListener.onChanged("fullscreenchange", finalMsg);
                }
            }, 50L);
        }

        this.mHandler.post(this.mHideBarRunnable);
        if (this.mIsNeedRecoverScreen) {
            if (isFullscreen) {
                this.mVideoView.adjustVideoView(1.0F);
                this.mTvRecoverScreen.setVisibility(this.mIsShowBar ? VISIBLE : GONE);
            } else {
                this.mVideoView.resetVideoView(false);
                this.mTvRecoverScreen.setVisibility(GONE);
            }
        }

    }

    @SuppressLint("WrongConstant")
    private void _handleOrientation(int orientation) {
        if (!this.mIsNeverPlay) {
            if (this.mIsFullscreen && !this.mIsAlwaysFullScreen) {
                if (orientation >= 0 && orientation <= 30 || orientation >= 330) {
                    this.mAttachActivity.setRequestedOrientation(1);
                }
            } else if (orientation >= 60 && orientation <= 120) {
                this.mAttachActivity.setRequestedOrientation(8);
            } else if (orientation >= 240 && orientation <= 300) {
                this.mAttachActivity.setRequestedOrientation(0);
            }

        }
    }

    private void _refreshOrientationEnable() {
        if (!this.mIsForbidOrientation) {
            this.mOrientationListener.disable();
            this.mHandler.removeMessages(10087);
            this.mHandler.sendEmptyMessageDelayed(10087, 3000L);
        }

    }

    public boolean isFullscreen() {
        return this.mIsFullscreen;
    }

    @SuppressLint("WrongConstant")
    public void fullScreen(int orientation) {
        this._refreshOrientationEnable();
        if (!this.mIsFullscreen) {
            this.originOrientation = this.mAttachActivity.getRequestedOrientation();
            this.orientation = orientation;
            if (orientation == 0) {
                if (this.mAttachActivity.getRequestedOrientation() != 1) {
                    this.mAttachActivity.setRequestedOrientation(1);
                }
            } else if (orientation == 90) {
                if (this.mAttachActivity.getRequestedOrientation() != 8) {
                    this.mAttachActivity.setRequestedOrientation(8);
                }
            } else if (orientation == -90 && this.mAttachActivity.getRequestedOrientation() != 0) {
                this.mAttachActivity.setRequestedOrientation(0);
            }

            this.setNavigationBar(true);
            DisplayMetrics metrics = new DisplayMetrics();
            this.mAttachActivity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
            this.mRawParams = this.getLayoutParams();
            Object fullParams;
            if (this.mRawParams instanceof android.widget.RelativeLayout.LayoutParams) {
                fullParams = new android.widget.RelativeLayout.LayoutParams(metrics.widthPixels, metrics.heightPixels);
            } else if (this.mRawParams instanceof android.widget.LinearLayout.LayoutParams) {
                fullParams = new android.widget.LinearLayout.LayoutParams(metrics.widthPixels, metrics.heightPixels);
            } else {
                if (!(this.mRawParams instanceof android.widget.FrameLayout.LayoutParams)) {
                    (new Builder(this.getContext())).setMessage("nonsupport parent layout, please do it by yourself").setPositiveButton("OK", new android.content.DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                        }
                    }).setCancelable(false).show();
                    return;
                }

                fullParams = new android.widget.FrameLayout.LayoutParams(metrics.widthPixels, metrics.heightPixels);
            }

            this.setLayoutParams((LayoutParams)fullParams);
            this._setFullScreen(true);
            ViewGroup rootGroup = (ViewGroup)this.mAttachActivity.getWindow().getDecorView().findViewById(16908290);
            if (rootGroup instanceof FrameLayout && this.getParent() != rootGroup) {
                ((ViewGroup)this.getParent()).removeView(this);
                rootGroup.addView(this, new android.widget.FrameLayout.LayoutParams(-1, -1));
            }

            if (VERSION.SDK_INT >= 28 && QueryNotchTool.hasNotchInScreen(this.mAttachActivity)) {
                int statusBarHeight = DeviceInfo.getStatusHeight(this.getContext()) - PdrUtil.pxFromDp(10.0F, this.getContext().getResources().getDisplayMetrics());
                if (orientation == 0) {
                    this.mFullscreenTopBar.setPadding(0, statusBarHeight, 0, 0);
                } else {
                    this.mLlBottomBar.setPadding(statusBarHeight, 0, statusBarHeight, 0);
                    this.mFullscreenTopBar.setPadding(statusBarHeight, 0, statusBarHeight, 0);
                }
            }
        }

    }

    public void exitFullScreen() {
        this._refreshOrientationEnable();
        if (this.mIsFullscreen) {
            if (this.mAttachActivity.getRequestedOrientation() != this.originOrientation) {
                this.mAttachActivity.setRequestedOrientation(this.originOrientation);
            }

            this.setNavigationBar(false);
            this.setLayoutParams(this.mRawParams);
            this._setFullScreen(false);
            if (this.getParent() != this.mRootLayout) {
                ((ViewGroup)this.getParent()).removeView(this);
                this.mRootLayout.addView(this, new android.widget.FrameLayout.LayoutParams(-1, -1));
            }

            if (VERSION.SDK_INT >= 28 && QueryNotchTool.hasNotchInScreen(this.mAttachActivity)) {
                this.mLlBottomBar.setPadding(0, 0, 0, 0);
                this.mFullscreenTopBar.setPadding(0, 0, 0, 0);
            }
        }

    }

    private void setNavigationBar(boolean show) {
        Window window = this.mAttachActivity.getWindow();
        View decorView = window.getDecorView();
        android.view.WindowManager.LayoutParams param = window.getAttributes();
        if (!show) {
            decorView.setSystemUiVisibility(this.defaultSystemUI);
            if (QueryNotchTool.hasNotchInScreen(this.mAttachActivity) && VERSION.SDK_INT >= 28) {
                window.getAttributes().layoutInDisplayCutoutMode = this.defaultDisplayCutoutMode;
            }
        } else {
            this.defaultSystemUI = decorView.getSystemUiVisibility();
            this.mAttachActivity.getWindow().getDecorView().setSystemUiVisibility(5894);
            if (QueryNotchTool.hasNotchInScreen(this.mAttachActivity) && VERSION.SDK_INT >= 28) {
                this.defaultDisplayCutoutMode = param.layoutInDisplayCutoutMode;
                param.layoutInDisplayCutoutMode = 1;
            }
        }

        window.setAttributes(param);
    }

    public void setScaleType(String aspect) {
        if (this.mVideoView != null) {
            byte var3 = -1;
            switch(aspect.hashCode()) {
                case 3143043:
                    if (aspect.equals("fill")) {
                        var3 = 1;
                    }
                    break;
                case 94852023:
                    if (aspect.equals("cover")) {
                        var3 = 2;
                    }
                    break;
                case 951526612:
                    if (aspect.equals("contain")) {
                        var3 = 0;
                    }
            }

            switch(var3) {
                case 0:
                    this.mVideoView.setAspectRatio(0);
                    break;
                case 1:
                    this.mVideoView.setAspectRatio(3);
                    break;
                case 2:
                    this.mVideoView.setAspectRatio(1);
            }
        }

    }

    public void playbackRate(String speed) {
        if (this.mVideoView != null) {
            int value = Arrays.binarySearch(this.rates, speed);
            if (value >= 0) {
                this.mVideoView.setSpeed(Float.parseFloat(speed));
            } else {
                this.mVideoView.setSpeed(1.0F);
            }
        }

    }

    private int _setProgress() {
        if (this.mVideoView != null && !this.mIsSeeking) {
            int position = Math.max(this.mVideoView.getCurrentPosition(), this.mInterruptPosition);
            int duration = this.getDuration();
            if (duration > 0) {
                long pos = 1000L * (long)position / (long)duration;
                this.mPlayerSeek.setProgress((int)pos);
            }

            int percent = this.mVideoView.getBufferPercentage();
            this.mPlayerSeek.setSecondaryProgress(percent * 10);
            this.mTvCurTime.setText(StringUtils.generateTime((long)position));
            this.mTvEndTime.setText(StringUtils.generateTime((long)duration));
            return position;
        } else {
            return 0;
        }
    }

    private void progressCallBack() {
        if (this.mVideoView != null && !this.mIsSeeking) {
            if (this.mOnPlayerChangedListener != null) {
                float position = (float)Math.max(this.mVideoView.getCurrentPosition(), this.mInterruptPosition);
                float duration = (float)this.getDuration();
                String msg = String.format(this.timeUpdateF, position / 1000.0F, duration / 1000.0F);
                this.mOnPlayerChangedListener.onChanged("timeupdate", msg);
            }

        }
    }
    @SuppressLint("WrongConstant")
    private void _setFastForward(String time) {
        if (this.mFlTouchLayout.getVisibility() == 8) {
            this.mFlTouchLayout.setVisibility(0);
        }

        if (this.mTvFastForward.getVisibility() == 8) {
            this.mTvFastForward.setVisibility(0);
        }

        this.mTvFastForward.setText(time);
    }
    @SuppressLint("WrongConstant")
    private void _hideTouchView() {
        if (this.mFlTouchLayout.getVisibility() == 0) {
            this.mTvFastForward.setVisibility(8);
            this.mTvVolume.setVisibility(8);
            this.mTvBrightness.setVisibility(8);
            this.mFlTouchLayout.setVisibility(8);
        }

    }

    private void _onProgressSlide(float percent) {
        if (!this.isRtmpUri) {
            if (this.isProgressGesture) {
                int position = this.mVideoView.getCurrentPosition();
                long duration = (long)this.getDuration();
                long deltaMax = Math.min(100000L, duration / 2L);
                long delta = (long)((float)deltaMax * percent);
                this.mTargetPosition = delta + (long)position;
                if (this.mTargetPosition > duration) {
                    this.mTargetPosition = duration;
                } else if (this.mTargetPosition <= 0L) {
                    this.mTargetPosition = 0L;
                }

                int deltaTime = (int)((this.mTargetPosition - (long)position) / 1000L);
                String desc;
                if (this.mTargetPosition > (long)position) {
                    desc = StringUtils.generateTime(this.mTargetPosition) + "/" + StringUtils.generateTime(duration);
                } else {
                    desc = StringUtils.generateTime(this.mTargetPosition) + "/" + StringUtils.generateTime(duration);
                }

                this._setFastForward(desc);
            }
        }
    }

    @SuppressLint("WrongConstant")
    private void _setVolumeInfo(int volume) {
        if (this.mFlTouchLayout.getVisibility() == 8) {
            this.mFlTouchLayout.setVisibility(0);
        }

        if (this.mTvVolume.getVisibility() == 8) {
            this.mTvVolume.setVisibility(0);
        }

        this.mTvVolume.setText(volume * 100 / this.mMaxVolume + "%");
    }

    public void setDuration(int duration) {
        if (this.mIsNeverPlay && !this.isPlaying()) {
            if (duration > 0) {
                this.duration = duration;
            } else {
                this.duration = -1;
            }
        }

    }

    public int getDuration() {
        if (this.duration <= -1) {
            this.duration = this.mVideoView.getDuration();
        }

        return this.duration;
    }

    private void _onVolumeSlide(float percent) {
        if (!this.isMutePlayer) {
            if (this.mIsFullscreen || this.isPageGesture) {
                if (this.mCurVolume == -1) {
                    this.mCurVolume = this.mAudioManager.getStreamVolume(3);
                    if (this.mCurVolume < 0) {
                        this.mCurVolume = 0;
                    }
                }

                int index = (int)(percent * (float)this.mMaxVolume) + this.mCurVolume;
                if (index > this.mMaxVolume) {
                    index = this.mMaxVolume;
                } else if (index < 0) {
                    index = 0;
                }

                this.mAudioManager.setStreamVolume(3, index, 0);
                this._setVolumeInfo(index);
            }
        }
    }

    public void isUseMediaCodec(boolean isUse) {
        if (this.mVideoView != null) {
            this.mVideoView.setmIsUsingMediaCodec(isUse);
        }

    }

    public void setMutePlayer(boolean isMute) {
        this.isMutePlayer = isMute;
        this.mIVMute.setSelected(this.isMutePlayer);
        this.mVideoView.setvolume(isMute ? 0.0F : 1.0F);
    }

    private void _setVolume(boolean isIncrease) {
        if (!this.isMutePlayer) {
            int curVolume = this.mAudioManager.getStreamVolume(3);
            if (isIncrease) {
                curVolume += this.mMaxVolume / 15;
            } else {
                curVolume -= this.mMaxVolume / 15;
            }

            if (curVolume > this.mMaxVolume) {
                curVolume = this.mMaxVolume;
            } else if (curVolume < 0) {
                curVolume = 0;
            }

            this.mAudioManager.setStreamVolume(3, curVolume, 0);
            this._setVolumeInfo(curVolume);
            this.mHandler.removeCallbacks(this.mHideTouchViewRunnable);
            this.mHandler.postDelayed(this.mHideTouchViewRunnable, 1000L);
        }
    }

    @SuppressLint("WrongConstant")
    private void _setBrightnessInfo(float brightness) {
        if (this.mFlTouchLayout.getVisibility() == 8) {
            this.mFlTouchLayout.setVisibility(0);
        }

        if (this.mTvBrightness.getVisibility() == 8) {
            this.mTvBrightness.setVisibility(0);
        }

        this.mTvBrightness.setText(Math.ceil((double)(brightness * 100.0F)) + "%");
    }

    private void _onBrightnessSlide(float percent) {
        if (this.mIsFullscreen || this.isPageGesture) {
            if (this.mCurBrightness < 0.0F) {
                this.mCurBrightness = this.mAttachActivity.getWindow().getAttributes().screenBrightness;
                if (this.mCurBrightness < 0.0F) {
                    this.mCurBrightness = 0.5F;
                } else if (this.mCurBrightness < 0.01F) {
                    this.mCurBrightness = 0.01F;
                }
            }

            android.view.WindowManager.LayoutParams attributes = this.mAttachActivity.getWindow().getAttributes();
            attributes.screenBrightness = this.mCurBrightness + percent;
            if (attributes.screenBrightness > 1.0F) {
                attributes.screenBrightness = 1.0F;
            } else if (attributes.screenBrightness < 0.01F) {
                attributes.screenBrightness = 0.01F;
            }

            this._setBrightnessInfo(attributes.screenBrightness);
            this.mAttachActivity.getWindow().setAttributes(attributes);
        }
    }

    private void _endGesture() {
        if (this.mTargetPosition >= 0L && this.mTargetPosition != (long)this.mVideoView.getCurrentPosition() && this.getDuration() > 0) {
            this.seekTo((int)this.mTargetPosition);
            this.mPlayerSeek.setProgress((int)(this.mTargetPosition * 1000L / (long)this.getDuration()));
            this.mTargetPosition = -1L;
        }

        this._hideTouchView();
        this._refreshHideRunnable();
        this.mCurVolume = -1;
        this.mCurBrightness = -1.0F;
    }

    @SuppressLint("WrongConstant")
    private void _switchStatus(int status) {
        Log.i("IjkPlayerView", "status " + status);
        switch(status) {
            case 3:
                this.mIsRenderingStart = true;
            case 702:
                this.mIsBufferingStart = false;
                this.mLoadingView.setVisibility(8);
                this.mPlayerThumb.setVisibility(8);
                if (this.mLlBottomBar.getVisibility() == 0 && !this.mIsShowBar) {
                    this.mIsShowBar = true;
                }

                this.mHandler.removeMessages(10086);
                this.mHandler.sendEmptyMessage(10086);
                if (this.mVideoView.isPlaying() && this.mIsNetConnected) {
                    this.mInterruptPosition = 0;
                    this._resumeDanmaku();
                    if (!this.mIvPlay.isSelected()) {
                        this.mVideoView.start();
                        this.mIvPlay.setSelected(true);
                        this.mIvPlayCenter.setSelected(true);
                    }
                }
                break;
            case 331:
                this.mInterruptPosition = Math.max(this.mVideoView.getInterruptPosition(), this.mInterruptPosition);
                this.pause();
                if (this.mVideoView.getDuration() == -1 && !this.mIsReady) {
                    this.mLoadingView.setVisibility(8);
                    this.mPlayerThumb.setVisibility(8);
                    this.mIvPlayCircle.setVisibility(8);
                } else {
                    this.mLoadingView.setVisibility(0);
                    this.mHandler.sendEmptyMessage(10088);
                }
            case 332:
            default:
                break;
            case 333:
                this.mIsReady = true;
                break;
            case 334:
                this.mHandler.removeMessages(10088);
                if (this.mIsRenderingStart && !this.mIsBufferingStart && this.mVideoView.getCurrentPosition() > 0) {
                    this._resumeDanmaku();
                }
                break;
            case 336:
                this.pause();
                if (this.mVideoView.getDuration() != -1 && this.mVideoView.getInterruptPosition() + 1000 >= this.mVideoView.getDuration()) {
                    this.mIsPlayComplete = true;
                    if (this.mCompletionListener != null) {
                        this.mCompletionListener.onCompletion(this.mVideoView.getMediaPlayer());
                    }
                } else {
                    this.mInterruptPosition = Math.max(this.mVideoView.getInterruptPosition(), this.mInterruptPosition);
                    if (null != this.mOnPlayerChangedListener) {
                        this.mOnPlayerChangedListener.onChanged("error", "network error");
                    }
                }
                break;
            case 701:
                this.mIsBufferingStart = true;
                this._pauseDanmaku();
                if (!this.mIsNeverPlay) {
                    this.mLoadingView.setVisibility(0);
                }

                this.mHandler.removeMessages(10088);
        }

    }

    public void setOnPreparedListener(OnPreparedListener l) {
        this.mVideoView.setOnPreparedListener(l);
    }

    public void setOnCompletionListener(OnCompletionListener l) {
        this.mCompletionListener = l;
    }

    public void setOnErrorListener(OnErrorListener l) {
        this.mVideoView.setOnErrorListener(l);
    }

    public void setOnInfoListener(OnInfoListener l) {
        this.mOutsideInfoListener = l;
    }

    public IjkPlayerView setDanmakuListener(OnDanmakuListener danmakuListener) {
        this.mDanmakuListener = danmakuListener;
        return this;
    }

    public void setOnPlayerChangedListener(OnPlayerChangedListener listener) {
        this.mOnPlayerChangedListener = listener;
    }

    public void setOnBufferingUpdateListener(OnBufferingUpdateListener bufferingUpdateListener) {
        this.bufferingUpdateListener = bufferingUpdateListener;
    }

    public int getCurPosition() {
        return this.mVideoView.getCurrentPosition();
    }

    public void setDirection(int orientation) {
        this.mOrientation = orientation;
    }

    public void setmDanmuList(String mDanmuList) {
        this.mDanmuList = mDanmuList;
    }

    private void _initDanmaku() {
        this.mDanmakuView = (IDanmakuView)this.findViewById(VideoR.VIDEO_IJK_ID_SV_DANMAKU);
        this.mIvDanmakuControl.setOnClickListener(this);
        int oneBtnWidth = this.getResources().getDimensionPixelOffset(VideoR.VIDEO_IJK_DIMEN_DANMAKU_INPUT_BTN_SIZE);
        this.mMoreOptionsWidth = oneBtnWidth * 12;
    }

    private void _loadDanmaku() {
        if (this.mIsEnableDanmaku) {
            this.mDanmakuContext = DanmakuContext.create();
            if (this.mDanmakuParser == null) {
                this.mDanmakuParser = new BaseDanmakuParser() {
                    protected Danmakus parse() {
                        return new Danmakus();
                    }
                };
            }

            try {
                InputStream is = new ByteArrayInputStream(this.mDanmuList.getBytes("utf-8"));
                this.setDanmakuSource((InputStream)is);
            } catch (UnsupportedEncodingException var2) {
                return;
            }

            this.mDanmakuView.setCallback(new Callback() {
                public void prepared() {
                    if (IjkPlayerView.this.mVideoView.isPlaying() && !IjkPlayerView.this.mIsBufferingStart) {
                        IjkPlayerView.this.mDanmakuView.start();
                    }

                }

                public void updateTimer(DanmakuTimer timer) {
                }

                public void danmakuShown(BaseDanmaku danmaku) {
                }

                public void drawingFinished() {
                }
            });
            this.mDanmakuView.enableDanmakuDrawingCache(true);
            this.mDanmakuView.prepare(this.mDanmakuParser, this.mDanmakuContext);
        }

    }

    public IjkPlayerView enableDanmaku() {
        this.mIsEnableDanmaku = true;
        this._initDanmaku();
        return this;
    }
    @SuppressLint("WrongConstant")
    public void enableDanmaku(boolean isEnable) {
        this.mIsEnableDanmaku = isEnable;
        if (this.mIsEnableDanmaku) {
            this._initDanmaku();
        } else {
            this.mIvDanmakuControl.setVisibility(8);
        }

    }

    @SuppressLint("WrongConstant")
    public void enableDanmuBtn(boolean isEnable) {
        if (this.mIsEnableDanmaku) {
            if (isEnable) {
                this.mIvDanmakuControl.setVisibility(0);
            } else {
                this.mIvDanmakuControl.setVisibility(8);
            }
        } else {
            this.mIvDanmakuControl.setVisibility(8);
        }

    }

    public IjkPlayerView setDanmakuSource(InputStream stream) {
        if (stream == null) {
            return this;
        } else if (!this.mIsEnableDanmaku) {
            throw new RuntimeException("Danmaku is disable, use enableDanmaku() first");
        } else {
            if (this.mDanmakuLoader == null) {
                this.mDanmakuLoader = DanmakuLoaderFactory.create(DanmakuLoaderFactory.TAG_ACFUN);
            }

            try {
                this.mDanmakuLoader.load(stream);
            } catch (IllegalDataException var3) {
                var3.printStackTrace();
            }

            IDataSource<?> dataSource = this.mDanmakuLoader.getDataSource();
            this.mDanmakuParser = new StandardDanmaKuParser();
            this.mDanmakuParser.load(dataSource);
            return this;
        }
    }

    public IjkPlayerView setDanmakuSource(String uri) {
        if (TextUtils.isEmpty(uri)) {
            return this;
        } else if (!this.mIsEnableDanmaku) {
            throw new RuntimeException("Danmaku is disable, use enableDanmaku() first");
        } else {
            if (this.mDanmakuLoader == null) {
                this.mDanmakuLoader = DanmakuLoaderFactory.create(DanmakuLoaderFactory.TAG_BILI);
            }

            try {
                this.mDanmakuLoader.load(uri);
            } catch (IllegalDataException var3) {
                var3.printStackTrace();
            }

            IDataSource<?> dataSource = this.mDanmakuLoader.getDataSource();
            this.mDanmakuParser = new BiliDanmukuParser();
            this.mDanmakuParser.load(dataSource);
            return this;
        }
    }

    public IjkPlayerView setDanmakuCustomParser(BaseDanmakuParser parser, ILoader loader, BaseDanmakuConverter converter) {
        this.mDanmakuParser = parser;
        this.mDanmakuLoader = loader;
        this.mDanmakuConverter = converter;
        return this;
    }

    public IjkPlayerView showOrHideDanmaku(boolean isShow) {
        if (isShow) {
            this.mIvDanmakuControl.setSelected(false);
            this.mDanmakuView.show();
        } else {
            this.mIvDanmakuControl.setSelected(true);
            this.mDanmakuView.hide();
        }

        return this;
    }

    public void sendDanmaku(JSONObject text, boolean isLive) {
        if (this.mIsEnableDanmaku) {
            if (!TextUtils.isEmpty(text.optString("text"))) {
                if (this.mDanmakuView.isPrepared()) {
                    BaseDanmaku danmaku = this.mDanmakuContext.mDanmakuFactory.createDanmaku(this.mDanmakuType);
                    if (danmaku != null && this.mDanmakuView != null) {
                        if (this.mDanmakuTextSize == -1.0F) {
                            this.mDanmakuTextSize = 25.0F * (this.mDanmakuParser.getDisplayer().getDensity() - 0.6F);
                        }

                        danmaku.text = text.optString("text", "....");
                        danmaku.padding = 5;
                        danmaku.isLive = isLive;
                        danmaku.priority = 0;
                        danmaku.textSize = this.mDanmakuTextSize;
                        danmaku.textColor = Color.parseColor(text.optString("color", "#ffffff"));
                        danmaku.setTime(this.mDanmakuView.getCurrentTime() + 500L);
                        this.mDanmakuView.addDanmaku(danmaku);
                        if (this.mDanmakuListener != null) {
                            if (this.mDanmakuConverter != null) {
                                this.mDanmakuListener.onDataObtain(this.mDanmakuConverter.convertDanmaku(danmaku));
                            } else {
                                this.mDanmakuListener.onDataObtain(danmaku);
                            }
                        }

                    }
                }
            }
        }
    }

    public void editVideo() {
        if (this.mVideoView.isPlaying()) {
            this.pause();
            this.mVideoStatus = 502;
        } else {
            this.mVideoStatus = 503;
        }

        this._hideAllView(false);
    }

    public boolean recoverFromEditVideo() {
        if (this.mVideoStatus == 501) {
            return false;
        } else {
            if (this.mIsFullscreen) {
                this._recoverScreen();
            }

            if (this.mVideoStatus == 502) {
                this.start();
            }

            this.mVideoStatus = 501;
            return true;
        }
    }

    public void clearDanma() {
        if (this.mDanmakuView != null && this.mDanmakuView.isPrepared()) {
            this.mDanmakuView.clearDanmakusOnScreen();
        }

    }

    private void _resumeDanmaku() {
        if (this.mDanmakuView != null && this.mDanmakuView.isPrepared() && this.mDanmakuView.isPaused()) {
            if (this.mDanmakuTargetPosition != -1L) {
                this.mDanmakuView.seekTo(this.mDanmakuTargetPosition);
                this.mDanmakuTargetPosition = -1L;
            } else {
                this.mDanmakuView.resume();
            }
        }

    }

    private void _pauseDanmaku() {
        if (this.mDanmakuView != null && this.mDanmakuView.isPrepared()) {
            this.mDanmakuView.pause();
        }

    }

    private void _toggleDanmakuShow() {
        if (this.mIvDanmakuControl.isSelected()) {
            this.showOrHideDanmaku(true);
        } else {
            this.showOrHideDanmaku(false);
        }

    }

    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        this.mOnPlayerChangedListener.onChanged("onConfigurationChanged", (String)null);
    }

    private void _recoverScreen() {
    }

    @SuppressLint("WrongConstant")
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return !this.isFullscreen() && this.mIsForbidTouch && this.mIvPlayCircle.getVisibility() != 0 ? true : super.onInterceptTouchEvent(ev);
    }

    private void _toggleMoreColorOptions() {
    }

    private void _initReceiver() {
        this.mScreenReceiver = new IjkPlayerView.ScreenBroadcastReceiver();
        this.mNetReceiver = new IjkPlayerView.NetBroadcastReceiver();
        this.mAttachActivity.registerReceiver(this.mScreenReceiver, new IntentFilter("android.intent.action.SCREEN_OFF"));
        this.mAttachActivity.registerReceiver(this.mNetReceiver, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
    }

    public class NetBroadcastReceiver extends BroadcastReceiver {
        public NetBroadcastReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            if ("android.net.conn.CONNECTIVITY_CHANGE".equals(intent.getAction())) {
                IjkPlayerView.this.mIsNetConnected = NetWorkUtils.isNetworkAvailable(IjkPlayerView.this.mAttachActivity);
            }

        }
    }

    private class ScreenBroadcastReceiver extends BroadcastReceiver {
        private ScreenBroadcastReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            if ("android.intent.action.SCREEN_OFF".equals(intent.getAction())) {
                IjkPlayerView.this.mIsScreenLocked = true;
            }

        }
    }

    @Retention(RetentionPolicy.SOURCE)
    @Target({ElementType.FIELD, ElementType.PARAMETER})
    @IntDef({701L, 702L, 703L})
    public @interface DanmakuTag {
    }
}
