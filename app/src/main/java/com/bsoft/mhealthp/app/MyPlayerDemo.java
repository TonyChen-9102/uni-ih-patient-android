package com.bsoft.mhealthp.app;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.bsoft.mhealthp.libs.utils.ijkplayer.VideoPlayerIJK;
import com.bsoft.mhealthp.libs.utils.ijkplayer.VideoPlayerListener;

import tv.danmaku.ijk.media.player.IMediaPlayer;

public class MyPlayerDemo extends AppCompatActivity {
    private VideoPlayerIJK ijkPlayer;

    public static void appStart(Context context){
        Intent intent = new Intent(context,MyPlayerDemo.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_player_demo);
        ijkPlayer = findViewById(R.id.ijkPlayer);
        ijkPlayer.setListener(new VideoPlayerListener() {
            @Override
            public void onBufferingUpdate(IMediaPlayer mp, int percent) {
            }

            @Override
            public void onCompletion(IMediaPlayer mp) {
                Log.i("chenkaid","111111;onCompletion=");
//                mp.seekTo(0);
                //mp.start();
            }

            @Override
            public boolean onError(IMediaPlayer mp, int what, int extra) {
                return false;
            }

            @Override
            public boolean onInfo(IMediaPlayer mp, int what, int extra) {
                return false;
            }

            @Override
            public void onPrepared(IMediaPlayer mp) {
                mp.start();
            }

            @Override
            public void onSeekComplete(IMediaPlayer mp) {

            }

            @Override
            public void onVideoSizeChanged(IMediaPlayer mp, int width, int height, int sar_num, int sar_den) {
                //获取到视频的宽和高
            }
        });
        Log.i("chenkaid","222222222222222222");
        ijkPlayer.setVideoPath("rtmp://59.111.190.24:6060/live/52587886446564_15052343827_15648476521");
    }
}
