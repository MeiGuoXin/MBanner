package com.mgx.mbanner.myInterface;

import android.widget.ImageView;

import java.util.List;

public interface IStateListener {
    void setDataList(List<String> dataList);
    void setImgDelay(int imgDelay);
    void startBanner();
    void startAutoPlay();
    void setPlaceholder(ImageView imageViewId, int defaultBitmap);
    //改变资源时要重新开启循环
    void dataChange(List<String> list);
}
