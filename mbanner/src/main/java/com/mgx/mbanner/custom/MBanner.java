package com.mgx.mbanner.custom;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.dueeeke.videocontroller.StandardVideoController;
import com.dueeeke.videoplayer.player.VideoView;
import com.mgx.mbanner.R;
import com.mgx.mbanner.adapter.BannerViewAdapter;
import com.mgx.mbanner.utils.PixelConversion;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

/**
 * 创建:2020年2月16
 * 说明:图片视频混播
 * 版本号:1.0.1
 * 备注:2021年3月10日修改
 */
public class MBanner extends LinearLayout implements ViewPager.OnPageChangeListener {
    /**
     * 是否开启视频缓存
     * 默认不开启 不开启:false 开启:true
     */
    private boolean mIsVideoCaching;
    /**
     * 显示占位图
     */
    private int mShowOccupationMap;

    /**
     * 轮播图的时间间隔
     */
    private long mImgDelay;

    /**
     * 默认轮播图的时间间隔
     */
    private int mDefaultImgDelay = 2000;
    /**
     * 水印的位置
     */
    private int mWatermarkLocation;
    /**
     * 水印的文字颜色
     */
    private int mWatermarkTextColor;
    /**
     * 水印文字
     */
    private String mWatermarkText;
    /**
     * 指示器的位置
     */
    private int mIndicatorPosition;
    /**
     * 图片加载错误
     */
    private int mLoadingErrorPicture;
    /**
     * ViewPager是否预加载
     */
    private boolean mIsPreloading;
    /**
     * 预加载的页数
     */
    private int mPreloadingPages;
    /**
     * 是否显示视频控制器
     */
    private boolean mIsVideoController;

    /**
     * 指示器宽度
     */
    private float mIndicatorWidth;
    /**
     * 指示器高度
     */
    private float mIndicatorHeight;
    /**
     * 指示器未选中的背景颜色
     */
    private int mIndicatorNotSelectedBackgroundColor;
    /**
     * 指示器的实线宽度
     */
    private float mIndicatorStrokeWidth;

    /**
     * 普通指示器的容器
     */
    private LinearLayout mIndicatorLayout;
    /**
     * 指示器的间隔时间
     */
    private int mIndicatorInterval;

    /**
     * 指示器的类型
     */
    private int mIndicatorType;

    private ViewPager mViewPager;
    private List<String> mListData;
    private List<View> mViews;
    private int autoCurrIndex = 0;
    private CacheVideoView mVideoView;
    private BannerViewAdapter mAdapter;
    //默认显示位置
    private static final int DEFAULT_DISPLAY_LOCATION = 100;
    private GetVideoDuration mGetVideoDuration;
    private IndicatorGravity mIndicatorGravity = IndicatorGravity.CENTER;
    private IndicatorStyle mIndicatorStyle = IndicatorStyle.ORDINARY;

    //每个位置默认时间间隔
    private long delayTime = 2000;
    private LinearLayout.LayoutParams lp;

    public MBanner(Context context) {
        this(context, null);
    }

    public MBanner(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MBanner(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    /**
     * 初始化view
     */
    private void initView(Context context, @Nullable AttributeSet attrs) {
        setWillNotDraw(false);
        mGetVideoDuration = new GetVideoDuration();
        mViewPager = new ViewPager(getContext());
        LinearLayout.LayoutParams vp_param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        mViewPager.setLayoutParams(vp_param);
        this.addView(mViewPager);
        //轮播图
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MBanner);
        mIsVideoCaching = typedArray.getBoolean(R.styleable.MBanner_isVideoCaching, false);
        mShowOccupationMap = typedArray.getResourceId(R.styleable.MBanner_showOccupationMap, R.mipmap.seat);
        mLoadingErrorPicture = typedArray.getResourceId(R.styleable.MBanner_loadingErrorPicture, R.mipmap.err);
        mImgDelay = typedArray.getInt(R.styleable.MBanner_imgDelay, mDefaultImgDelay);
        mIsPreloading = typedArray.getBoolean(R.styleable.MBanner_isPreloading, false);
        mPreloadingPages = typedArray.getInt(R.styleable.MBanner_preloadingPages, 0);
        mIsVideoController = typedArray.getBoolean(R.styleable.MBanner_isVideoController, false);

        //水印
        mWatermarkLocation = typedArray.getInt(R.styleable.MBanner_watermarkLocation, -1);
        mWatermarkTextColor = typedArray.getColor(R.styleable.MBanner_watermarkTextColor, getResources().getColor(R.color.dkplayer_background_color));
        mWatermarkText = typedArray.getString(R.styleable.MBanner_watermarkText);

        //指示器
        mIndicatorWidth = typedArray.getDimension(R.styleable.MBanner_indicatorWidth, PixelConversion.sp2px(6, getContext()));
        mIndicatorHeight = typedArray.getDimension(R.styleable.MBanner_indicatorHeight, PixelConversion.sp2px(6, getContext()));
        mIndicatorPosition = typedArray.getInt(R.styleable.MBanner_indicatorPosition, -1);
        mIndicatorNotSelectedBackgroundColor = typedArray.getColor(R.styleable.MBanner_indicatorNotSelectedBackgroundColor, getResources().getColor(R.color.dkplayer_background_color));
        mIndicatorStrokeWidth = typedArray.getDimension(R.styleable.MBanner_indicatorStrokeWidth, PixelConversion.sp2px(4, getContext()));
        mIndicatorType = typedArray.getInt(R.styleable.MBanner_indicatorStyle, 3);

        if (mIndicatorType == 1) {
            mIndicatorStyle = IndicatorStyle.NONE;
        } else if (mIndicatorType == 2) {
            mIndicatorStyle = IndicatorStyle.NUMBER;
        } else if (mIndicatorType == 3) {
            mIndicatorStyle = IndicatorStyle.ORDINARY;
        }

        if (mIndicatorPosition == 0x01) {
            mIndicatorGravity = IndicatorGravity.LEFT;
        } else if (mIndicatorPosition == 0x02) {
            mIndicatorGravity = IndicatorGravity.RIGHT;
        } else if (mIndicatorPosition == 0x03) {
            mIndicatorGravity = IndicatorGravity.CENTER;
        }
        //回收
        typedArray.recycle();

        if (mImgDelay <= 0) {
            //图片时间间隔必须大于0
            throw new RuntimeException("The time interval must be greater than zero");
        }

        addIndicatorLayout(context);
    }

    /**
     * 指示器方向
     */
    public enum IndicatorGravity {
        LEFT, RIGHT, CENTER
    }

    /**
     * 指示器类型
     */
    public enum IndicatorStyle {
        //没有指示器
        NONE,
        //数字指示器
        NUMBER,
        //普通指示器
        ORDINARY
    }


    protected void addIndicatorLayout(Context context) {
        mIndicatorLayout = new LinearLayout(context);
        LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = analysisGravity(mIndicatorGravity);
        int margins = PixelConversion.sp2px(8, context);
        layoutParams.setMargins(margins, 0, margins, margins);
        mIndicatorLayout.setGravity(Gravity.CENTER);
        mIndicatorLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        mIndicatorLayout.setDividerDrawable(getDividerDrawable(mIndicatorInterval));
        this.addView(mIndicatorLayout, lp);
        mIndicatorLayout.setVisibility(mIndicatorStyle == IndicatorStyle.ORDINARY ? VISIBLE : GONE);
    }

    private int analysisGravity(IndicatorGravity gravity) {
        if (gravity == IndicatorGravity.LEFT) {
            return Gravity.BOTTOM | Gravity.LEFT;
        } else if (gravity == IndicatorGravity.RIGHT) {
            return Gravity.BOTTOM | Gravity.RIGHT;
        } else {
            return Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
        }
    }

    private Drawable getDividerDrawable(int interval) {
        /*ShapeDrawable drawable = (ShapeDrawable) mContext.getResources().getDrawable(R.drawable.indicator_divider);
        drawable.setIntrinsicWidth(interval);*/
        ShapeDrawable drawable = new ShapeDrawable();
        drawable.getPaint().setColor(Color.TRANSPARENT);
        drawable.setIntrinsicWidth(interval);
        return drawable;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int indicatorWidth = MeasureSpec.getSize(widthMeasureSpec);
        int indicatorHeight = MeasureSpec.getSize(heightMeasureSpec);
        if (indicatorWidth == MeasureSpec.UNSPECIFIED) {
            throw new RuntimeException("Width must specify a fixed size");
        }
        if (indicatorHeight == MeasureSpec.UNSPECIFIED) {
            throw new RuntimeException("Height must specify a fixed dimension");
        }
        setMeasuredDimension(indicatorWidth, indicatorHeight);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

    }

    /**
     * 控件大小尺寸改变才会调用
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

    }

    /**
     * 设置数据
     */
    public void setDataList(List<String> dataList) {
        mListData = dataList;
        if (mViews == null) {
            mViews = new ArrayList<>(dataList.size());
        } else {
            mViews.clear();
        }
        lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
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
                if (videoType(url)) {
                    setVideoSelection(url, lp);
                } else {
                    setImageSelection(lp, url);
                }
            }
        } else if (mListData.size() == 1) {
            autoCurrIndex = 0;
            String url = mListData.get(0);
            if (videoType(url)) {
                setVideoSelection(url, lp);
            } else {
                setImageSelection(lp, url);
            }
        }
    }

    /**
     * 支持视频的格式
     */
    protected boolean videoType(String url) {
        if (MimeTypeMap.getFileExtensionFromUrl(url).equals("mp4")
                || MimeTypeMap.getFileExtensionFromUrl(url).equals("m3u8")
                || MimeTypeMap.getFileExtensionFromUrl(url).equals("flv")
                || MimeTypeMap.getFileExtensionFromUrl(url).equals("avi")
                || MimeTypeMap.getFileExtensionFromUrl(url).equals("file")
                || MimeTypeMap.getFileExtensionFromUrl(url).equals("mkv")
                || MimeTypeMap.getFileExtensionFromUrl(url).equals("https")
                || MimeTypeMap.getFileExtensionFromUrl(url).equals("http")
        ) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 显示图片
     */
    @SuppressLint("CheckResult")
    protected void setImageSelection(LinearLayout.LayoutParams lp, String url) {
        ImageView imageView = new ImageView(getContext());
        imageView.setLayoutParams(lp);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Glide.with(getContext()).load(url).apply(new RequestOptions().centerCrop().placeholder(mShowOccupationMap).error(mLoadingErrorPicture)).into(imageView);
        mViews.add(imageView);
    }

    /**
     * 视频
     */
    protected void setVideoSelection(String url, LinearLayout.LayoutParams lp) {
        mVideoView = new CacheVideoView(getContext());
        if (mIsVideoController) {
            StandardVideoController controller = new StandardVideoController(getContext());
            mVideoView.setVideoController(controller);
        }
        mVideoView.setScreenScaleType(VideoView.SCREEN_SCALE_MATCH_PARENT);
        mVideoView.setLayoutParams(lp);
        mVideoView.setUrl(url);
        mVideoView.start();
        mViews.add(mVideoView);
    }

    /**
     * 开始轮播
     */
    public void startBanner() {
        mAdapter = new BannerViewAdapter(mViews);
        mViewPager.setAdapter(mAdapter);
        if (mIsPreloading && mPreloadingPages >= 1) {
            mViewPager.setOffscreenPageLimit(mPreloadingPages);
        }
        mViewPager.setCurrentItem(autoCurrIndex);
        mViewPager.addOnPageChangeListener(this);
    }

    /**
     * 开始自动播放
     */
    public void startAutoPlay() {
        if (mViews.size() > 1) {
            getDelayedTime(autoCurrIndex);
            if (delayTime <= 0) {
                mHandler.postDelayed(mGetVideoDuration, mImgDelay);
            } else {
                mHandler.postDelayed(runnable, delayTime);
            }
        }
    }

    private Runnable runnable = () -> {
        this.mHandler.sendEmptyMessage(DEFAULT_DISPLAY_LOCATION);
    };

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

    /**
     * 设置占位图
     */
    public void setPlaceholder(ImageView imageViewId, int defaultBitmap) {

    }

    /**
     * 数据改变
     */
    public void dataChange(List<String> list) {
        if (list != null && list.size() > 0) {
            //改变资源时要重新开启循环，否则会把视频的时长赋给图片，或者相反
            mHandler.removeCallbacks(runnable);
            setDataList(list);
            mAdapter.setDataList(mViews);
            mAdapter.notifyDataSetChanged();
            mViewPager.setCurrentItem(autoCurrIndex, false);
            //开启循环
            if (mViews.size() > 1) {
                getDelayedTime(autoCurrIndex);
                if (delayTime <= 0) {
                    mHandler.postDelayed(mGetVideoDuration, mImgDelay);
                } else {
                    mHandler.postDelayed(runnable, delayTime);
                }
            }
        }
    }

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
        if (state == 0 && mViews.size() > 1) {
            View view1 = mViews.get(pageIndex);
            if (view1 instanceof VideoView) {
                final VideoView videoView = (VideoView) view1;
                long current = videoView.getCurrentPosition();
                long duration = videoView.getDuration();
                delayTime = (duration - current);
                if (delayTime <= 0) {
                    mGetVideoDuration.getDelayTime(videoView, runnable);
                    mHandler.postDelayed(mGetVideoDuration, mImgDelay);
                } else {
                    mHandler.postDelayed(runnable, delayTime);
                }
            } else {
                delayTime = mImgDelay;
                mHandler.postDelayed(runnable, delayTime);
            }
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
            delayTime = mImgDelay;
        }
    }

    /**
     * 销毁 轮播图
     */
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

    /**
     * 暂停 视频播放器
     */
    public void onPause() {
        if (mVideoView != null) {
            mVideoView.pause();
        }
    }

    /**
     * 恢复 视频播放器
     */
    public void onResume() {
        if (mVideoView != null) {
            mVideoView.resume();
        }
    }

    /**
     * 销毁 视频播放器
     */
    public void onDestroy() {
        if (mVideoView != null) {
            mVideoView.release();
        }
    }
}
