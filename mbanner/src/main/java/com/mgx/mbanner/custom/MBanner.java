package com.mgx.mbanner.custom;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.RequiresApi;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.dueeeke.videoplayer.player.VideoView;
import com.mgx.mbanner.adapter.BannerViewAdapter;
import com.mgx.mbanner.myInterface.IStateListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 2020年2月16
 * 说明:图片视频混播
 */
public class MBanner extends RelativeLayout implements IStateListener {
    private ViewPager mViewPager;
    //图片默认时间间隔
    private long imgDelay = 2000;
    //每个位置默认时间间隔，因为有视频的原因
    private long delayTime = 2000;
    //是否自动播放
    private boolean isAutoPlay = false;
    private GetVideoDuration mGetVideoDuration;
    private List<View> mViews;
    private BannerViewAdapter mAdapter;
    private int autoCurrIndex = 0;
    //默认显示位置
    private final int DEFAULT_DISPLAY_LOCATION = 100;
    //第三方播放器 带缓冲
    private CacheVideoView mVideoView;
    private List<String> mListData;

    public MBanner(Context context) {
        super(context);
        init();
    }

    public MBanner(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MBanner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public MBanner(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        mGetVideoDuration = new GetVideoDuration();
        mViewPager = new ViewPager(getContext());
        LinearLayout.LayoutParams vp_param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        mViewPager.setLayoutParams(vp_param);
        this.addView(mViewPager);
    }

    /**
     * @param dataList 图片和视频的数据源 必填
     */
    @Override
    public void setDataList(List<String> dataList) {
        mListData = dataList;
        //用于显示的数组
        if (mViews == null) {
            mViews = new ArrayList<>();
        } else {
            mViews.clear();
        }
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        RequestOptions options = new RequestOptions();
        options.centerCrop();
        //数据大于一条，才可以循环
        if (mListData.size() > 1) {
            autoCurrIndex = 1;
            //循环数组，将首位各加一条数据
            for (int i = 0, len = mListData.size() + 2; i < len; i++) {
                String url;
                if (i == 0) {
                    url = mListData.get(mListData.size() - 1);
                } else if (i == mListData.size() + 1) {
                    url = mListData.get(0);
                } else {
                    url = mListData.get(i - 1);
                }
                //视频
                if (MimeTypeMap.getFileExtensionFromUrl(url).equals("mp4")) {
                    setVideoSelection(url, lp);
                } else {
                    setImageSelection(lp, url, options);
                }
            }
        } else if (mListData.size() == 1) {
            autoCurrIndex = 0;
            String url = mListData.get(0);
            if (MimeTypeMap.getFileExtensionFromUrl(url).equals("mp4")) {
                setVideoSelection(url, lp);
            } else {
                setImageSelection(lp, url, options);
            }
        }
    }

    /**
     * 延时播放
     *
     * @param imgDelay 延时播放的时间
     */
    @Override
    public void setImgDelay(int imgDelay) {
        this.imgDelay = imgDelay;
    }

    /**
     * 开始播放
     */
    @Override
    public void startBanner() {
        mAdapter = new BannerViewAdapter(mViews);
        mViewPager.setAdapter(mAdapter);
        //预加载
        mViewPager.setOffscreenPageLimit(1);
        mViewPager.setCurrentItem(autoCurrIndex);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //当前位置
                autoCurrIndex = position;
                getDelayedTime(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                //移除自动计时
                mHandler.removeCallbacks(runnable);
                //ViewPager跳转
                int pageIndex = autoCurrIndex;
                if (autoCurrIndex == 0) {
                    pageIndex = mViews.size() - 2;
                } else if (autoCurrIndex == mViews.size() - 1) {
                    pageIndex = 1;
                }
                if (pageIndex != autoCurrIndex) {
                    //无滑动动画，直接跳转
                    mViewPager.setCurrentItem(pageIndex, false);
                }
                //停止滑动时，重新自动倒计时
                if (state == 0 && isAutoPlay && mViews.size() > 1) {
                    View view1 = mViews.get(pageIndex);
                    if (view1 instanceof VideoView) {
                        final VideoView videoView = (VideoView) view1;
                        long current = videoView.getCurrentPosition();
                        long duration = videoView.getDuration();
                        delayTime = duration - current;
                        //某些时候，某些视频，获取的时间无效，就延时10秒，重新获取
                        if (delayTime <= 0) {
                            mGetVideoDuration.getDelayTime(videoView, runnable);
                            mHandler.postDelayed(mGetVideoDuration, imgDelay);
                        } else {
                            mHandler.postDelayed(runnable, delayTime);
                        }
                    } else {
                        delayTime = imgDelay;
                        mHandler.postDelayed(runnable, delayTime);
                    }
                    Log.d("TAG", "" + pageIndex + "--" + autoCurrIndex);
                }
            }
        });
    }

    @Override
    public void dataChange(List<String> list) {
        if (list != null && list.size() > 0) {
            //改变资源时要重新开启循环，否则会把视频的时长赋给图片，或者相反
            mHandler.removeCallbacks(runnable);
            setDataList(list);
            mAdapter.setDataList(mViews);
            mAdapter.notifyDataSetChanged();
            mViewPager.setCurrentItem(autoCurrIndex, false);
            //开启循环
            if (isAutoPlay && mViews.size() > 1) {
                getDelayedTime(autoCurrIndex);
                if (delayTime <= 0) {
                    mHandler.postDelayed(mGetVideoDuration, imgDelay);
                } else {
                    mHandler.postDelayed(runnable, delayTime);
                }
            }
        }
    }

    @Override
    public void startAutoPlay() {
        isAutoPlay = true;
        if (mViews.size() > 1) {
            getDelayedTime(autoCurrIndex);
            if (delayTime <= 0) {
                mHandler.postDelayed(mGetVideoDuration, imgDelay);
            } else {
                mHandler.postDelayed(runnable, delayTime);
            }
        }
    }

    /**
     * 占位图片
     *
     * @param imageViewId   控件的id
     * @param defaultBitmap 占位图
     *                      注意:注意调用顺序setDataList()-->setPlaceholder()
     */
    @Override
    public void setPlaceholder(ImageView imageViewId, int defaultBitmap) {
        getPlaceholder(imageViewId, defaultBitmap);
    }


    protected void getPlaceholder(ImageView imageViewId, int defaultBitmap) {
        if (null != mListData && mListData.size() <= 0) {
            imageViewId.setVisibility(View.VISIBLE);
            imageViewId.setImageResource(defaultBitmap);
        } else {
            imageViewId.setVisibility(View.GONE);
        }
    }

    /**
     * 延时播放
     */
    private void getDelayedTime(int position) {
        View view1 = mViews.get(position);
        if (view1 instanceof VideoView) {
            VideoView videoView = (VideoView) view1;
            videoView.start();
            videoView.seekTo(0);
            delayTime = videoView.getDuration();
            mGetVideoDuration.getDelayTime(videoView, runnable);
        } else {
            delayTime = imgDelay;
        }
    }

    /**
     * 发消息，进行循环
     */
    private Runnable runnable = () -> {
        this.mHandler.sendEmptyMessage(DEFAULT_DISPLAY_LOCATION);
    };

    /**
     * @param lp      显示图片的位置
     * @param url     没有数据默认图片或有数据要显示的图片
     * @param options
     */
    protected void setImageSelection(LinearLayout.LayoutParams lp, String url, RequestOptions options) {
        ImageView imageView = new ImageView(getContext());
        imageView.setLayoutParams(lp);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Glide.with(getContext()).load(url).apply(options).into(imageView);
        mViews.add(imageView);
    }

    /**
     * @param url 视频地址
     * @param lp  要显示的位置
     */
    protected void setVideoSelection(String url, LinearLayout.LayoutParams lp) {
        mVideoView = new CacheVideoView(getContext());
        //画面填充
        mVideoView.setScreenScaleType(VideoView.SCREEN_SCALE_MATCH_PARENT);
        mVideoView.setLayoutParams(lp);
        mVideoView.setUrl(url);
        mVideoView.start();
        mViews.add(mVideoView);
    }

    /**
     * 获取视频时长以及已经播放的时间
     */
    protected class GetVideoDuration implements Runnable {
        private VideoView videoView;
        private Runnable runnable;

        public void getDelayTime(VideoView videoView, Runnable runnable) {
            this.videoView = videoView;
            this.runnable = runnable;
        }

        @Override
        public void run() {
            long current = videoView.getCurrentPosition();
            long duration = videoView.getDuration();
            long delyedTime = duration - current;
            mHandler.postDelayed(runnable, delyedTime);
        }
    }

    //接受消息实现轮播
    @SuppressLint("HandlerLeak")
    protected Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DEFAULT_DISPLAY_LOCATION:
                    mViewPager.setCurrentItem(autoCurrIndex + 1);
                    break;
            }
        }
    };

    //销毁
    public void destroy() {
        mHandler.removeCallbacksAndMessages(null);
        mHandler = null;
        mGetVideoDuration = null;
        runnable = null;
        mViews.clear();
        mViews = null;
        mViewPager = null;
        mAdapter = null;
    }
}
