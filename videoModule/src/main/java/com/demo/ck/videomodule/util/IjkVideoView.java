package com.demo.ck.videomodule.util;


import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.AudioManager;
import android.media.AudioManager.OnAudioFocusChangeListener;
import android.net.Uri;
import android.os.Build.VERSION;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.MediaController.MediaPlayerControl;

import com.dcloud.android.annotation.NonNull;

import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.Map;

import io.dcloud.media.weex.weex_video.ijkplayer.media.AssetsDataSourceProvider;
import io.dcloud.media.weex.weex_video.ijkplayer.media.FileMediaDataSource;
import io.dcloud.media.weex.weex_video.ijkplayer.media.IMediaController;
import io.dcloud.media.weex.weex_video.ijkplayer.media.IRenderView;
import io.dcloud.media.weex.weex_video.ijkplayer.media.IRenderView.IRenderCallback;
import io.dcloud.media.weex.weex_video.ijkplayer.media.IRenderView.ISurfaceHolder;
import io.dcloud.media.weex.weex_video.ijkplayer.media.MediaPlayerCompat;
import io.dcloud.media.weex.weex_video.ijkplayer.media.SurfaceRenderView;
import io.dcloud.media.weex.weex_video.ijkplayer.media.TextureRenderView;
import tv.danmaku.ijk.media.player.AndroidMediaPlayer;
import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IMediaPlayer.OnBufferingUpdateListener;
import tv.danmaku.ijk.media.player.IMediaPlayer.OnCompletionListener;
import tv.danmaku.ijk.media.player.IMediaPlayer.OnErrorListener;
import tv.danmaku.ijk.media.player.IMediaPlayer.OnInfoListener;
import tv.danmaku.ijk.media.player.IMediaPlayer.OnPreparedListener;
import tv.danmaku.ijk.media.player.IMediaPlayer.OnSeekCompleteListener;
import tv.danmaku.ijk.media.player.IMediaPlayer.OnVideoSizeChangedListener;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;
import tv.danmaku.ijk.media.player.misc.IMediaDataSource;
import tv.danmaku.ijk.media.player.misc.ITrackInfo;

public class IjkVideoView extends FrameLayout implements MediaPlayerControl {
    private String TAG = "TTAG";
    private Uri mUri;
    private AssetsDataSourceProvider fd;
    private Map<String, String> mHeaders;
    private int mCurrentState = 330;
    private int mTargetState = 330;
    private ISurfaceHolder mSurfaceHolder = null;
    private IMediaPlayer mMediaPlayer = null;
    private int mVideoWidth;
    private int mVideoHeight;
    private int mSurfaceWidth;
    private int mSurfaceHeight;
    private int mVideoRotationDegree;
    private int mVideoTargetRotationDegree;
    private Matrix mOriginalMatrix;
    private IMediaController mMediaController;
    private OnCompletionListener mOnCompletionListener;
    private OnPreparedListener mOnPreparedListener;
    private int mCurrentBufferPercentage;
    private OnErrorListener mOnErrorListener;
    private OnInfoListener mOnInfoListener;
    private int mSeekWhenPrepared;
    private boolean mCanPause = true;
    private boolean mCanSeekBack = true;
    private boolean mCanSeekForward = true;
    private boolean mIsUsingMediaCodec = false;
    private boolean mIsUsingMediaCodecAutoRotate;
    private boolean mIsMediaCodecHandleResolutionChange;
    private boolean mIsUsingOpenSLES;
    private boolean mIsUsingMediaDataSource;
    private String mPixelFormat = "";
    private Context mAppContext;
    private IRenderView mRenderView;
    private int mVideoSarNum;
    private int mVideoSarDen;
    private float mVideoScale = 1.0F;
    private boolean mIsNormalScreen = true;
    private int mScreenOrWidth;
    private int mScreenOrHeight;
    private Matrix mSaveMatrix;
    private boolean isStoped = false;
    private long mPrepareStartTime = 0L;
    private long mPrepareEndTime = 0L;
    private long mSeekStartTime = 0L;
    private long mSeekEndTime = 0L;
    OnVideoSizeChangedListener mSizeChangedListener;
    OnPreparedListener mPreparedListener;
    private OnCompletionListener mCompletionListener;
    private OnInfoListener mInfoListener;
    private OnErrorListener mErrorListener;
    private OnBufferingUpdateListener mBufferingUpdateListener;
    private OnBufferingUpdateListener bufferingUpdateListener;
    private OnSeekCompleteListener mSeekCompleteListener;
    IRenderCallback mSHCallback;
    private boolean isMediaReady;
    private float volume;
    private static final int[] s_allAspectRatio = new int[]{0, 1, 2, 4, 5};
    private int mCurrentAspectRatio;
    public static final int RENDER_NONE = 0;
    public static final int RENDER_SURFACE_VIEW = 1;
    public static final int RENDER_TEXTURE_VIEW = 2;
    private boolean mEnableBackgroundPlay;

    public int getCurrentState() {
        return this.mCurrentState;
    }

    public IjkVideoView(Context context) {
        this(context, null);
    }

    public IjkVideoView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IjkVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    @TargetApi(21)
    public IjkVideoView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        class NamelessClass_8 implements OnVideoSizeChangedListener {
            NamelessClass_8() {
            }

            public void onVideoSizeChanged(IMediaPlayer mp, int width, int height, int sarNum, int sarDen) {
                IjkVideoView.this.mVideoWidth = mp.getVideoWidth();
                IjkVideoView.this.mVideoHeight = mp.getVideoHeight();
                IjkVideoView.this.mVideoSarNum = mp.getVideoSarNum();
                IjkVideoView.this.mVideoSarDen = mp.getVideoSarDen();
                if (IjkVideoView.this.mVideoWidth != 0 && IjkVideoView.this.mVideoHeight != 0) {
                    if (IjkVideoView.this.mRenderView != null) {
                        IjkVideoView.this.mRenderView.setVideoSize(IjkVideoView.this.mVideoWidth, IjkVideoView.this.mVideoHeight);
                        IjkVideoView.this.mRenderView.setVideoSampleAspectRatio(IjkVideoView.this.mVideoSarNum, IjkVideoView.this.mVideoSarDen);
                    }

                    IjkVideoView.this.requestLayout();
                }

            }
        }

        this.mSizeChangedListener = new NamelessClass_8();

        class NamelessClass_7 implements OnPreparedListener {
            NamelessClass_7() {
            }

            public void onPrepared(IMediaPlayer mp) {
                IjkVideoView.this.mPrepareEndTime = System.currentTimeMillis();
                IjkVideoView.this.mCurrentState = 333;
                IjkVideoView.this._notifyMediaStatus();
                if (IjkVideoView.this.mOnPreparedListener != null) {
                    IjkVideoView.this.mOnPreparedListener.onPrepared(IjkVideoView.this.mMediaPlayer);
                }

                if (IjkVideoView.this.mMediaController != null) {
                    IjkVideoView.this.mMediaController.setEnabled(true);
                }

                IjkVideoView.this.mVideoWidth = mp.getVideoWidth();
                IjkVideoView.this.mVideoHeight = mp.getVideoHeight();
                int seekToPosition = IjkVideoView.this.mSeekWhenPrepared;
                if (seekToPosition != 0) {
                    IjkVideoView.this.seekTo(seekToPosition);
                }

                if (IjkVideoView.this.mVideoWidth != 0 && IjkVideoView.this.mVideoHeight != 0) {
                    if (IjkVideoView.this.mRenderView != null) {
                        IjkVideoView.this.mRenderView.setVideoSize(IjkVideoView.this.mVideoWidth, IjkVideoView.this.mVideoHeight);
                        IjkVideoView.this.mRenderView.setVideoSampleAspectRatio(IjkVideoView.this.mVideoSarNum, IjkVideoView.this.mVideoSarDen);
                        if (!IjkVideoView.this.mRenderView.shouldWaitForResize() || IjkVideoView.this.mSurfaceWidth == IjkVideoView.this.mVideoWidth && IjkVideoView.this.mSurfaceHeight == IjkVideoView.this.mVideoHeight) {
                            if (IjkVideoView.this.mTargetState == 334) {
                                IjkVideoView.this.start();
                                if (IjkVideoView.this.mMediaController != null) {
                                    IjkVideoView.this.mMediaController.show();
                                }
                            } else if (!IjkVideoView.this.isPlaying() && (seekToPosition != 0 || IjkVideoView.this.getCurrentPosition() > 0) && IjkVideoView.this.mMediaController != null) {
                                IjkVideoView.this.mMediaController.show(0);
                            }
                        }
                    }
                } else if (IjkVideoView.this.mTargetState == 334) {
                    IjkVideoView.this.start();
                }

            }
        }

        this.mPreparedListener = new NamelessClass_7();

        class NamelessClass_6 implements OnCompletionListener {
            NamelessClass_6() {
            }

            public void onCompletion(IMediaPlayer mp) {
                Log.w(IjkVideoView.this.TAG, "OnCompletionListener:");
                IjkVideoView.this.mCurrentState = 336;
                IjkVideoView.this.mTargetState = 336;
                IjkVideoView.this._notifyMediaStatus();
                if (IjkVideoView.this.mMediaController != null) {
                    IjkVideoView.this.mMediaController.hide();
                }

                if (IjkVideoView.this.mOnCompletionListener != null) {
                    IjkVideoView.this.mOnCompletionListener.onCompletion(IjkVideoView.this.mMediaPlayer);
                }

            }
        }

        this.mCompletionListener = new NamelessClass_6();

        class NamelessClass_5 implements OnInfoListener {
            NamelessClass_5() {
            }

            public boolean onInfo(IMediaPlayer mp, int arg1, int arg2) {
                if (IjkVideoView.this.mOnInfoListener != null) {
                    IjkVideoView.this.mOnInfoListener.onInfo(mp, arg1, arg2);
                }

                switch (arg1) {
                    case 3:
                        Log.d(IjkVideoView.this.TAG, "MEDIA_INFO_VIDEO_RENDERING_START:");
                        break;
                    case 700:
                        Log.d(IjkVideoView.this.TAG, "MEDIA_INFO_VIDEO_TRACK_LAGGING:");
                        break;
                    case 701:
                        Log.d(IjkVideoView.this.TAG, "MEDIA_INFO_BUFFERING_START:");
                        break;
                    case 702:
                        Log.d(IjkVideoView.this.TAG, "MEDIA_INFO_BUFFERING_END:");
                        break;
                    case 703:
                        Log.d(IjkVideoView.this.TAG, "MEDIA_INFO_NETWORK_BANDWIDTH: " + arg2);
                        break;
                    case 800:
                        Log.d(IjkVideoView.this.TAG, "MEDIA_INFO_BAD_INTERLEAVING:");
                        break;
                    case 801:
                        Log.d(IjkVideoView.this.TAG, "MEDIA_INFO_NOT_SEEKABLE:");
                        break;
                    case 802:
                        Log.d(IjkVideoView.this.TAG, "MEDIA_INFO_METADATA_UPDATE:");
                        break;
                    case 901:
                        Log.d(IjkVideoView.this.TAG, "MEDIA_INFO_UNSUPPORTED_SUBTITLE:");
                        break;
                    case 902:
                        Log.d(IjkVideoView.this.TAG, "MEDIA_INFO_SUBTITLE_TIMED_OUT:");
                        break;
                    case 10001:
                        IjkVideoView.this.mVideoRotationDegree = arg2;
                        Log.d(IjkVideoView.this.TAG, "MEDIA_INFO_VIDEO_ROTATION_CHANGED: " + arg2);
                        if (IjkVideoView.this.mRenderView != null) {
                            IjkVideoView.this.mRenderView.setVideoRotation(arg2);
                        }
                        break;
                    case 10002:
                        Log.d(IjkVideoView.this.TAG, "MEDIA_INFO_AUDIO_RENDERING_START:");
                }

                return true;
            }
        }

        this.mInfoListener = new NamelessClass_5();

        class NamelessClass_4 implements OnErrorListener {
            NamelessClass_4() {
            }

            public boolean onError(IMediaPlayer mp, int framework_err, int impl_err) {
                Log.d("TTAG", "Error: " + framework_err + "," + impl_err);
                IjkVideoView.this.mCurrentState = 331;
                IjkVideoView.this.mTargetState = 331;
                IjkVideoView.this._notifyMediaStatus();
                if (IjkVideoView.this.mMediaController != null) {
                    IjkVideoView.this.mMediaController.hide();
                }

                return IjkVideoView.this.mOnErrorListener != null && IjkVideoView.this.mOnErrorListener.onError(IjkVideoView.this.mMediaPlayer, framework_err, impl_err) ? true : true;
            }
        }

        this.mErrorListener = new NamelessClass_4();

        class NamelessClass_3 implements OnBufferingUpdateListener {
            NamelessClass_3() {
            }

            public void onBufferingUpdate(IMediaPlayer mp, int percent) {
                IjkVideoView.this.mCurrentBufferPercentage = percent;
                if (IjkVideoView.this.bufferingUpdateListener != null) {
                    IjkVideoView.this.bufferingUpdateListener.onBufferingUpdate(mp, percent);
                }

            }
        }

        this.mBufferingUpdateListener = new NamelessClass_3();

        class NamelessClass_2 implements OnSeekCompleteListener {
            NamelessClass_2() {
            }

            public void onSeekComplete(IMediaPlayer mp) {
                IjkVideoView.this.mSeekEndTime = System.currentTimeMillis();
            }
        }

        this.mSeekCompleteListener = new NamelessClass_2();

        class NamelessClass_1 implements IRenderCallback {
            NamelessClass_1() {
            }

            public void onSurfaceChanged(@NonNull ISurfaceHolder holder, int format, int w, int h) {
                if (holder.getRenderView() != IjkVideoView.this.mRenderView) {
                    Log.e(IjkVideoView.this.TAG, "onSurfaceChanged: unmatched render callback\n");
                } else {
                    IjkVideoView.this.mSurfaceWidth = w;
                    IjkVideoView.this.mSurfaceHeight = h;
                    boolean isValidState = IjkVideoView.this.mTargetState == 334;
                    boolean hasValidSize = !IjkVideoView.this.mRenderView.shouldWaitForResize() || IjkVideoView.this.mVideoWidth == w && IjkVideoView.this.mVideoHeight == h;
                    if (IjkVideoView.this.mMediaPlayer != null && isValidState && hasValidSize) {
                        if (IjkVideoView.this.mSeekWhenPrepared != 0) {
                            IjkVideoView.this.seekTo(IjkVideoView.this.mSeekWhenPrepared);
                        }

                        IjkVideoView.this.start();
                    }

                }
            }

            public void onSurfaceCreated(@NonNull ISurfaceHolder holder, int width, int height) {
                if (holder.getRenderView() != IjkVideoView.this.mRenderView) {
                    Log.e(IjkVideoView.this.TAG, "onSurfaceCreated: unmatched render callback\n");
                } else {
                    IjkVideoView.this.mSurfaceHolder = holder;
                    if (IjkVideoView.this.mMediaPlayer != null) {
                        IjkVideoView.this.bindSurfaceHolder(IjkVideoView.this.mMediaPlayer, holder);
                    } else {
                        IjkVideoView.this.openVideo();
                    }

                }
            }

            public void onSurfaceDestroyed(@NonNull ISurfaceHolder holder) {
                if (holder.getRenderView() != IjkVideoView.this.mRenderView) {
                    Log.e(IjkVideoView.this.TAG, "onSurfaceDestroyed: unmatched render callback\n");
                } else {
                    IjkVideoView.this.mSurfaceHolder = null;
                    IjkVideoView.this.releaseWithoutStop();
                }
            }
        }

        this.mSHCallback = new NamelessClass_1();
        this.isMediaReady = true;
        this.volume = 0.0F;
        this.mCurrentAspectRatio = s_allAspectRatio[0];
        this.mEnableBackgroundPlay = false;
        this.initVideoView(context);
    }

    private void initVideoView(Context context) {
        this.mAppContext = context.getApplicationContext();
        this.initBackground();
        this.initRenders();
        this.mVideoWidth = 0;
        this.mVideoHeight = 0;
        this.setFocusable(true);
        this.setFocusableInTouchMode(true);
        this.requestFocus();
        this.mCurrentState = 330;
        this.mTargetState = 330;
        this._notifyMediaStatus();
    }

    public void setmIsUsingMediaCodec(boolean mIsUsingMediaCodec) {
        this.mIsUsingMediaCodec = mIsUsingMediaCodec;
    }

    public void setRenderView(IRenderView renderView) {
        View renderUIView;
        if (this.mRenderView != null) {
            if (this.mMediaPlayer != null) {
                this.mMediaPlayer.setDisplay((SurfaceHolder) null);
            }

            renderUIView = this.mRenderView.getView();
            this.mRenderView.removeRenderCallback(this.mSHCallback);
            this.mRenderView = null;
            this.removeView(renderUIView);
        }

        if (renderView != null) {
            this.mRenderView = renderView;
            renderView.setAspectRatio(this.mCurrentAspectRatio);
            if (this.mVideoWidth > 0 && this.mVideoHeight > 0) {
                renderView.setVideoSize(this.mVideoWidth, this.mVideoHeight);
            }

            if (this.mVideoSarNum > 0 && this.mVideoSarDen > 0) {
                renderView.setVideoSampleAspectRatio(this.mVideoSarNum, this.mVideoSarDen);
            }

            renderUIView = this.mRenderView.getView();
            LayoutParams lp = new LayoutParams(-1, -2, 17);
            renderUIView.setLayoutParams(lp);
            this.addView(renderUIView);
            this.mRenderView.addRenderCallback(this.mSHCallback);
            this.mRenderView.setVideoRotation(this.mVideoRotationDegree);
        }
    }

    public void setVideoRotation(int degree) {
        this.mVideoTargetRotationDegree = this.mVideoRotationDegree + degree;
        this.mRenderView.setVideoRotation(this.mVideoTargetRotationDegree);
    }

    public Matrix getVideoTransform() {
        if (this.mOriginalMatrix == null) {
            this.mOriginalMatrix = this.mRenderView.getTransform();
        }

        return this.mRenderView.getTransform();
    }

    public void setVideoTransform(Matrix transform) {
        this.mRenderView.setTransform(transform);
    }

    public boolean adjustVideoView(float scale) {
        this.mVideoScale *= scale;
        int degree = (this.mVideoTargetRotationDegree + 360) % 360;
        if (this.mVideoScale == 1.0F && degree == 0) {
            return false;
        } else {
            if (degree <= 315 && degree > 45) {
                if (degree > 45 && degree <= 135) {
                    this.mVideoRotationDegree = 90;
                } else if (degree > 135 && degree <= 225) {
                    this.mVideoRotationDegree = 180;
                } else if (degree > 225 && degree <= 315) {
                    this.mVideoRotationDegree = 270;
                } else {
                    this.mVideoRotationDegree = 0;
                }
            } else {
                this.mVideoRotationDegree = 0;
            }

            final int deltaDegree = this.mVideoRotationDegree - this.mVideoTargetRotationDegree;
            this.mVideoTargetRotationDegree = this.mVideoRotationDegree;
            final Matrix matrix = this.getVideoTransform();
            if (this.mScreenOrWidth == 0 || this.mScreenOrHeight == 0) {
                this.mScreenOrWidth = this.mRenderView.getView().getWidth();
                this.mScreenOrHeight = this.mRenderView.getView().getHeight();
            }

            if (!this.mIsNormalScreen) {
                matrix.preScale(this.mVideoScale, this.mVideoScale);
                matrix.postTranslate((float) this.mScreenOrWidth * (1.0F - this.mVideoScale) / 2.0F, (float) this.mScreenOrHeight * (1.0F - this.mVideoScale) / 2.0F);
                this.mRenderView.setTransform(matrix);
                this.mIsNormalScreen = true;
            } else {
                float[] points = new float[2];
                matrix.mapPoints(points);
                final float deltaX = (float) this.mScreenOrWidth * (1.0F - this.mVideoScale) / 2.0F - points[0];
                final float deltaY = (float) this.mScreenOrHeight * (1.0F - this.mVideoScale) / 2.0F - points[1];
                if (this.mSaveMatrix == null) {
                    this.mSaveMatrix = new Matrix();
                }

                ValueAnimator animator = ValueAnimator.ofFloat(new float[]{0.0F, 1.0F}).setDuration(300L);
                animator.addUpdateListener(new AnimatorUpdateListener() {
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                        float percent = (Float) valueAnimator.getAnimatedValue();
                        IjkVideoView.this.mSaveMatrix.set(matrix);
                        IjkVideoView.this.mSaveMatrix.postTranslate(deltaX * percent, deltaY * percent);
                        IjkVideoView.this.mRenderView.setTransform(IjkVideoView.this.mSaveMatrix);
                        IjkVideoView.this.mRenderView.setVideoRotation((int) ((float) IjkVideoView.this.mVideoRotationDegree - (float) deltaDegree * (1.0F - percent)));
                    }
                });
                animator.start();
            }

            return true;
        }
    }

    public void resetVideoView(boolean isForever) {
        this.mIsNormalScreen = isForever;
        this.mVideoRotationDegree = 0;
        if (isForever) {
            this.mVideoTargetRotationDegree = 0;
            this.mVideoScale = 1.0F;
        }

        this.mRenderView.setTransform(this.mOriginalMatrix);
        this.mRenderView.setVideoRotation(this.mVideoRotationDegree);
    }

    public void setRender(int render) {
        switch (render) {
            case 0:
                this.setRenderView((IRenderView) null);
                break;
            case 1:
                SurfaceRenderView renderView = new SurfaceRenderView(this.getContext());
                this.setRenderView(renderView);
                break;
            case 2:
                TextureRenderView renderView2 = new TextureRenderView(this.getContext());
                if (this.mMediaPlayer != null) {
                    renderView2.getSurfaceHolder().bindToMediaPlayer(this.mMediaPlayer);
                    renderView2.setVideoSize(this.mMediaPlayer.getVideoWidth(), this.mMediaPlayer.getVideoHeight());
                    renderView2.setVideoSampleAspectRatio(this.mMediaPlayer.getVideoSarNum(), this.mMediaPlayer.getVideoSarDen());
                    renderView2.setAspectRatio(this.mCurrentAspectRatio);
                }

                this.setRenderView(renderView2);
                break;
            default:
                Log.e(this.TAG, String.format(Locale.getDefault(), "invalid render %d\n", render));
        }

    }

    public void setVideoPath(String path) {
        this.setVideoURI(Uri.parse(path));
    }

    public void setVideoURI(Uri uri) {
        this.setVideoURI(uri, (Map) null);
    }

    public void setVideoFileDescriptor(AssetsDataSourceProvider fd) {
        this.fd = fd;
        this.mSeekWhenPrepared = 0;
        this.openVideo();
        this.requestLayout();
        this.invalidate();
    }

    private void setVideoURI(Uri uri, Map<String, String> headers) {
        this.mUri = uri;
        this.mHeaders = headers;
        this.mSeekWhenPrepared = 0;
        this.openVideo();
        this.requestLayout();
        this.invalidate();
    }

    public Uri getUri() {
        return this.mUri;
    }

    public void setSpeed(float speed) {
        if (this.mMediaPlayer instanceof IjkMediaPlayer) {
            IjkMediaPlayer ijkMedia = (IjkMediaPlayer) this.mMediaPlayer;
            ijkMedia.setSpeed(speed);
        }

    }

    public Bitmap getScreenshot() {
        return this.mRenderView != null ? this.mRenderView.getVideoScreenshot() : null;
    }

    public void stopPlayback() {
        if (this.mMediaPlayer != null) {
            this.mMediaPlayer.stop();
            this.mMediaPlayer.release();
            this.mMediaPlayer = null;
            this.mCurrentState = 330;
            this.mTargetState = 330;
            this._notifyMediaStatus();
            @SuppressLint("WrongConstant") AudioManager am = (AudioManager) this.mAppContext.getSystemService("audio");
            am.abandonAudioFocus((OnAudioFocusChangeListener) null);
            this.isStoped = true;
        }

    }

    @TargetApi(23)
    private void openVideo() {
        if ((this.mUri != null || this.fd != null) && this.mSurfaceHolder != null) {
            this.release(false);

            try {
                this.mMediaPlayer = this.createPlayer(2);
                if (!this.isMediaReady) {
                    this.setvolume(this.volume);
                    this.isMediaReady = true;
                }

                Context context = this.getContext();
                this.mMediaPlayer.setOnPreparedListener(this.mPreparedListener);
                this.mMediaPlayer.setOnVideoSizeChangedListener(this.mSizeChangedListener);
                this.mMediaPlayer.setOnCompletionListener(this.mCompletionListener);
                this.mMediaPlayer.setOnErrorListener(this.mErrorListener);
                this.mMediaPlayer.setOnInfoListener(this.mInfoListener);
                this.mMediaPlayer.setOnBufferingUpdateListener(this.mBufferingUpdateListener);
                this.mMediaPlayer.setOnSeekCompleteListener(this.mSeekCompleteListener);
                this.mCurrentBufferPercentage = 0;
                if (this.mUri == null) {
                    if (this.fd != null) {
                        this.mMediaPlayer.setDataSource(this.fd);
                    }
                } else {
                    String scheme = this.mUri.getScheme();
                    if (VERSION.SDK_INT < 23 || !this.mIsUsingMediaDataSource || !TextUtils.isEmpty(scheme) && !scheme.equalsIgnoreCase("file")) {
                        if (VERSION.SDK_INT >= 14) {
                            this.mMediaPlayer.setDataSource(this.mAppContext, this.mUri, this.mHeaders);
                        } else {
                            this.mMediaPlayer.setDataSource(this.mUri.toString());
                        }
                    } else {
                        IMediaDataSource dataSource = new FileMediaDataSource(new File(this.mUri.toString()));
                        this.mMediaPlayer.setDataSource(dataSource);
                    }
                }

                this.bindSurfaceHolder(this.mMediaPlayer, this.mSurfaceHolder);
                this.mMediaPlayer.setAudioStreamType(3);
                this.mMediaPlayer.setScreenOnWhilePlaying(true);
                this.mPrepareStartTime = System.currentTimeMillis();
                this.mMediaPlayer.prepareAsync();
                this.mCurrentState = 332;
                this.attachMediaController();
            } catch (IOException var8) {
                this.mCurrentState = 331;
                this.mTargetState = 331;
                this.mErrorListener.onError(this.mMediaPlayer, 1, 0);
            } catch (IllegalArgumentException var9) {
                this.mCurrentState = 331;
                this.mTargetState = 331;
                this.mErrorListener.onError(this.mMediaPlayer, 1, 0);
            } finally {
                this._notifyMediaStatus();
            }

        } else {
            if (this.mUri == null && this.fd == null) {
                this.mCurrentState = 331;
                this.mTargetState = 331;
                this.mErrorListener.onError(this.mMediaPlayer, 1, 0);
            }

        }
    }

    public void setMediaController(IMediaController controller) {
        if (this.mMediaController != null) {
            this.mMediaController.hide();
        }

        this.mMediaController = controller;
        this.attachMediaController();
    }

    private void attachMediaController() {
        if (this.mMediaPlayer != null && this.mMediaController != null) {
            this.mMediaController.setMediaPlayer(this);
            View anchorView = this.getParent() instanceof View ? (View) this.getParent() : this;
            this.mMediaController.setAnchorView((View) anchorView);
            this.mMediaController.setEnabled(this.isInPlaybackState());
        }

    }

    public IMediaPlayer getMediaPlayer() {
        return this.mMediaPlayer;
    }

    private void _notifyMediaStatus() {
        if (this.mOnInfoListener != null) {
            this.mOnInfoListener.onInfo(this.mMediaPlayer, this.mCurrentState, -1);
        }

    }

    public void setOnBufferingUpdateListener(OnBufferingUpdateListener bufferingUpdateListener) {
        this.bufferingUpdateListener = bufferingUpdateListener;
    }

    public void setOnPreparedListener(OnPreparedListener l) {
        this.mOnPreparedListener = l;
    }

    public void setOnCompletionListener(OnCompletionListener l) {
        this.mOnCompletionListener = l;
    }

    public void setOnErrorListener(OnErrorListener l) {
        this.mOnErrorListener = l;
    }

    public void setOnInfoListener(OnInfoListener l) {
        this.mOnInfoListener = l;
    }

    private void bindSurfaceHolder(IMediaPlayer mp, ISurfaceHolder holder) {
        if (mp != null) {
            if (holder == null) {
                mp.setDisplay((SurfaceHolder) null);
            } else {
                holder.bindToMediaPlayer(mp);
            }
        }
    }

    public void releaseWithoutStop() {
        if (this.mMediaPlayer != null) {
            this.mMediaPlayer.setDisplay((SurfaceHolder) null);
        }

    }

    public void release(boolean cleartargetstate) {
        if (this.mMediaPlayer != null) {
            this.mMediaPlayer.reset();
            this.mMediaPlayer.release();
            this.mMediaPlayer = null;
            this.mCurrentState = 330;
            this._notifyMediaStatus();
            if (cleartargetstate) {
                this.mTargetState = 330;
            }

            @SuppressLint("WrongConstant") AudioManager am = (AudioManager) this.mAppContext.getSystemService("audio");
            am.abandonAudioFocus((OnAudioFocusChangeListener) null);
        }

    }

    public void destroy() {
        this.release(true);
    }

    public boolean onTouchEvent(MotionEvent ev) {
        if (this.isInPlaybackState() && this.mMediaController != null) {
            this.toggleMediaControlsVisibility();
        }

        return false;
    }

    public boolean onTrackballEvent(MotionEvent ev) {
        if (this.isInPlaybackState() && this.mMediaController != null) {
            this.toggleMediaControlsVisibility();
        }

        return false;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        boolean isKeyCodeSupported = keyCode != 4 && keyCode != 24 && keyCode != 25 && keyCode != 164 && keyCode != 82 && keyCode != 5 && keyCode != 6;
        if (this.isInPlaybackState() && isKeyCodeSupported && this.mMediaController != null) {
            if (keyCode == 79 || keyCode == 85) {
                if (this.mMediaPlayer.isPlaying()) {
                    this.pause();
                    this.mMediaController.show();
                } else {
                    this.start();
                    this.mMediaController.hide();
                }

                return true;
            }

            if (keyCode == 126) {
                if (!this.mMediaPlayer.isPlaying()) {
                    this.start();
                    this.mMediaController.hide();
                }

                return true;
            }

            if (keyCode == 86 || keyCode == 127) {
                if (this.mMediaPlayer.isPlaying()) {
                    this.pause();
                    this.mMediaController.show();
                }

                return true;
            }

            this.toggleMediaControlsVisibility();
        }

        return super.onKeyDown(keyCode, event);
    }

    private void toggleMediaControlsVisibility() {
        if (this.mMediaController.isShowing()) {
            this.mMediaController.hide();
        } else {
            this.mMediaController.show();
        }

    }

    public void start() {
        Log.e("TTAG", "start " + this.isInPlaybackState());
        @SuppressLint("WrongConstant") AudioManager am = (AudioManager) this.mAppContext.getSystemService("audio");
        am.requestAudioFocus((OnAudioFocusChangeListener) null, 3, 4);
        if (this.isStoped && this.mMediaPlayer == null && this.mCurrentState == 330) {
            this.isStoped = false;
            this.setRender(2);
            this.openVideo();
            this.requestLayout();
            this.invalidate();
        }

        if (this.isInPlaybackState()) {
            this.mMediaPlayer.start();
            this.mCurrentState = 334;
            this._notifyMediaStatus();
        }

        this.mTargetState = 334;
        if (this.mCurrentState == 331) {
            this.mTargetState = 331;
            this.mErrorListener.onError(this.mMediaPlayer, 1, 0);
        }

    }

    public void pause() {
        if (this.isInPlaybackState() && this.mMediaPlayer.isPlaying()) {
            this.mMediaPlayer.pause();
            this.mCurrentState = 335;
            this._notifyMediaStatus();
        }

        this.mTargetState = 335;
    }

    public void suspend() {
        this.release(false);
    }

    public void resume() {
        this.openVideo();
    }

    public void reload() {
        this.mCurrentState = 334;
        this.mTargetState = 334;
    }

    public int getDuration() {
        return this.isInPlaybackState() ? (int) this.mMediaPlayer.getDuration() : -1;
    }

    public void setvolume(float volume) {
        if (this.mMediaPlayer != null) {
            this.mMediaPlayer.setVolume(volume, volume);
        } else {
            this.isMediaReady = false;
            this.volume = volume;
        }

    }

    public int getInterruptPosition() {
        return this.mMediaPlayer != null ? (int) this.mMediaPlayer.getCurrentPosition() : 0;
    }

    public int getCurrentPosition() {
        return this.isInPlaybackState() ? (int) this.mMediaPlayer.getCurrentPosition() : 0;
    }

    public void seekTo(int msec) {
        if (this.isInPlaybackState()) {
            this.mSeekStartTime = System.currentTimeMillis();
            this.mMediaPlayer.seekTo((long) msec);
            this.mSeekWhenPrepared = 0;
        } else {
            this.mSeekWhenPrepared = msec;
        }

    }

    public boolean isPlaying() {
        return this.isInPlaybackState() && this.mMediaPlayer.isPlaying();
    }

    public int getBufferPercentage() {
        return this.mMediaPlayer != null ? this.mCurrentBufferPercentage : 0;
    }

    private boolean isInPlaybackState() {
        return this.mMediaPlayer != null && this.mCurrentState != 331 && this.mCurrentState != 330 && this.mCurrentState != 332;
    }

    public boolean canPause() {
        return this.mCanPause;
    }

    public boolean canSeekBackward() {
        return this.mCanSeekBack;
    }

    public boolean canSeekForward() {
        return this.mCanSeekForward;
    }

    public int getAudioSessionId() {
        return 0;
    }

    public void setAspectRatio(int aspectRatio) {
        this.mCurrentAspectRatio = aspectRatio;
        if (this.mRenderView != null) {
            this.mRenderView.setAspectRatio(this.mCurrentAspectRatio);
        }

    }

    private void initRenders() {
        if (VERSION.SDK_INT >= 14) {
            this.setRender(2);
        } else {
            this.setRender(1);
        }

    }

    int getVideoWidth() {
        return this.mMediaPlayer == null ? 0 : this.mMediaPlayer.getVideoWidth();
    }

    int getVideoHeight() {
        return this.mMediaPlayer == null ? 0 : this.mMediaPlayer.getVideoHeight();
    }

    public IMediaPlayer createPlayer(int playerType) {
        IMediaPlayer mediaPlayer = null;
        switch (playerType) {
            case 1:
                AndroidMediaPlayer androidMediaPlayer = new AndroidMediaPlayer();
                mediaPlayer = androidMediaPlayer;
                break;
            case 2:
            default:
                IjkMediaPlayer ijkMediaPlayer = null;
                if (this.mUri != null || this.fd != null) {
                    ijkMediaPlayer = new IjkMediaPlayer();
                    if (this.mIsUsingMediaCodec) {
                        ijkMediaPlayer.setOption(4, "mediacodec", 1L);
                        if (this.mIsUsingMediaCodecAutoRotate) {
                            ijkMediaPlayer.setOption(4, "mediacodec-auto-rotate", 1L);
                        } else {
                            ijkMediaPlayer.setOption(4, "mediacodec-auto-rotate", 0L);
                        }

                        if (this.mIsMediaCodecHandleResolutionChange) {
                            ijkMediaPlayer.setOption(4, "mediacodec-handle-resolution-change", 1L);
                        } else {
                            ijkMediaPlayer.setOption(4, "mediacodec-handle-resolution-change", 0L);
                        }
                    } else {
                        ijkMediaPlayer.setOption(4, "mediacodec", 0L);
                    }

                    if (this.mIsUsingOpenSLES) {
                        ijkMediaPlayer.setOption(4, "opensles", 1L);
                    } else {
                        ijkMediaPlayer.setOption(4, "opensles", 0L);
                    }

                    if (TextUtils.isEmpty(this.mPixelFormat)) {
                        ijkMediaPlayer.setOption(4, "overlay-format", 842225234L);
                    } else {
                        ijkMediaPlayer.setOption(4, "overlay-format", this.mPixelFormat);
                    }

                    ijkMediaPlayer.setOption(4, "framedrop", 5L);
                    ijkMediaPlayer.setOption(4, "start-on-prepared", 0L);
                    ijkMediaPlayer.setOption(1, "http-detect-range-support", 0L);
                    ijkMediaPlayer.setOption(2, "skip_loop_filter", 48L);
                    ijkMediaPlayer.setOption(1, "dns_cache_clear", 1L);
                    //设置播放前的探测时间 1,达到首屏秒开效果
                    ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "analyzeduration", 1L);
                    //播放前的探测Size，默认是1M, 改小一点会出画面更快
//                    ijkMediaPlayer.setOption(1, "probesize", 600L);
                    if (this.mUri != null && (this.mUri.toString().startsWith("rtmp://") || this.mUri.toString().startsWith("rtsp://"))) {
                        ijkMediaPlayer.setOption(1, "rtsp_transport", "tcp");

                        ijkMediaPlayer.setOption(1, "flush_packets", 1L);
                        ijkMediaPlayer.setOption(1, "analyzemaxduration", 100L);
                        //是否开启预缓冲，一般直播项目会开启，达到秒开的效果，不过带来了播放丢帧卡顿的体验
                        ijkMediaPlayer.setOption(4, "packet-buffering", 0L);
                    }
                }

                mediaPlayer = ijkMediaPlayer;
        }

        return (IMediaPlayer) mediaPlayer;
    }

    private void initBackground() {
    }

    public boolean isBackgroundPlayEnabled() {
        return this.mEnableBackgroundPlay;
    }

    public void enterBackground() {
    }

    public void stopBackgroundPlay() {
    }

    private String buildTimeMilli(long duration) {
        long total_seconds = duration / 1000L;
        long hours = total_seconds / 3600L;
        long minutes = total_seconds % 3600L / 60L;
        long seconds = total_seconds % 60L;
        if (duration <= 0L) {
            return "--:--";
        } else if (hours >= 100L) {
            return String.format(Locale.US, "%d:%02d:%02d", hours, minutes, seconds);
        } else {
            return hours > 0L ? String.format(Locale.US, "%02d:%02d:%02d", hours, minutes, seconds) : String.format(Locale.US, "%02d:%02d", minutes, seconds);
        }
    }

    public ITrackInfo[] getTrackInfo() {
        return this.mMediaPlayer == null ? null : this.mMediaPlayer.getTrackInfo();
    }

    public void selectTrack(int stream) {
        MediaPlayerCompat.selectTrack(this.mMediaPlayer, stream);
    }

    public void deselectTrack(int stream) {
        MediaPlayerCompat.deselectTrack(this.mMediaPlayer, stream);
    }

    public int getSelectedTrack(int trackType) {
        return MediaPlayerCompat.getSelectedTrack(this.mMediaPlayer, trackType);
    }
}

