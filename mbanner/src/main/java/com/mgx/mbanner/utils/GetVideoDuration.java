package com.mgx.mbanner.utils;

import android.os.Handler;

import com.dueeeke.videoplayer.player.VideoView;

/**
 * 获取视频时长以及已经播放的时间
 */
public class GetVideoDuration implements Runnable{

    private VideoView videoView;
    private Runnable runnable;
    private Handler mHandler;

    public void getDelayTime(VideoView videoView, Runnable runnable) {
        this.videoView = videoView;
        this.runnable = runnable;
    }

    public void setHandler(Handler mHandler){
        this.mHandler=mHandler;
    }

    @Override
    public void run() {
        long current = videoView.getCurrentPosition();
        long duration = videoView.getDuration();
        long delyedTime = duration - current;
        mHandler.postDelayed(runnable, delyedTime);
    }
}
