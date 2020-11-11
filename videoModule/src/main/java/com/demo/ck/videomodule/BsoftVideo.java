package com.demo.ck.videomodule;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.bsoft.mhealthp.libs.utils.ijkplayer.VideoPlayerIJK;
import com.bsoft.mhealthp.libs.utils.ijkplayer.VideoPlayerListener;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.ui.action.BasicComponentData;
import com.taobao.weex.ui.component.WXComponent;
import com.taobao.weex.ui.component.WXComponentProp;
import com.taobao.weex.ui.component.WXVContainer;

import tv.danmaku.ijk.media.player.IMediaPlayer;

public class BsoftVideo extends WXComponent<View> {
    private VideoPlayerIJK ijkPlayer;
    private TextView text;

    public BsoftVideo(WXSDKInstance instance, WXVContainer parent, String instanceId, boolean isLazy, BasicComponentData basicComponentData) {
        super(instance, parent, instanceId, isLazy, basicComponentData);
    }

    public BsoftVideo(WXSDKInstance instance, WXVContainer parent, boolean isLazy, BasicComponentData basicComponentData) {
        super(instance, parent, isLazy, basicComponentData);
    }

    public BsoftVideo(WXSDKInstance instance, WXVContainer parent, BasicComponentData basicComponentData) {
        super(instance, parent, basicComponentData);
    }

    public BsoftVideo(WXSDKInstance instance, WXVContainer parent, int type, BasicComponentData basicComponentData) {
        super(instance, parent, type, basicComponentData);
    }

    @Override
    protected View initComponentHostView(@NonNull Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_bsoft_video, null);
        ijkPlayer = view.findViewById(R.id.ijkPlayer);
        text = view.findViewById(R.id.text);
        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("chenkaid","5555555555555");
//                ijkPlayer.stop();
                //ijkPlayer.start();
            }
        });
        ijkPlayer.setListener(new VideoPlayerListener() {
            @Override
            public void onBufferingUpdate(IMediaPlayer iMediaPlayer, int i) {
                Log.i("chenkaid","onBufferingUpdate");
            }

            @Override
            public void onCompletion(IMediaPlayer iMediaPlayer) {
                Log.i("chenkaid","onCompletion");
            }

            @Override
            public boolean onError(IMediaPlayer iMediaPlayer, int i, int i1) {
                Log.i("chenkaid","onError");
                return false;
            }

            @Override
            public boolean onInfo(IMediaPlayer iMediaPlayer, int i, int i1) {
                Log.i("chenkaid","onInfo");
                return false;
            }

            @Override
            public void onPrepared(IMediaPlayer iMediaPlayer) {
                Log.i("chenkaid","onPrepared");
//                iMediaPlayer.start();
            }

            @Override
            public void onSeekComplete(IMediaPlayer iMediaPlayer) {
                Log.i("chenkaid","onSeekComplete");
            }

            @Override
            public void onVideoSizeChanged(IMediaPlayer iMediaPlayer, int i, int i1, int i2, int i3) {
                Log.i("chenkaid","onVideoSizeChanged");
            }
        });
        return view;
    }

    String mUrl = null;

    @WXComponentProp(name = "src")
    public void setUrl(String url) {
        Log.i("chenkaid", "000;setUrl=" + url);
        if (!TextUtils.isEmpty(url)) {
            mUrl = url;
            ijkPlayer.setVideoPath(url);
        }
    }

    @WXComponentProp(name = "autoPlay")
    public void setAutoPlay(boolean auto) {

    }

    @JSMethod(uiThread = true)
    public void stop() {

    }

    @JSMethod(uiThread = true)
    public void play() {
        Log.i("chenkaid", "3333;start=" + ijkPlayer.isPlaying());
//        ijkPlayer.setVideoPath(mUrl);
        //ijkPlayer.reset();
//        ijkPlayer.seekTo(0);
       ijkPlayer.start();
    }
}
