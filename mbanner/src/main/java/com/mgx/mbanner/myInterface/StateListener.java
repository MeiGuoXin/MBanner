package com.mgx.mbanner.myInterface;

import java.util.List;

public interface StateListener {
    void setDataList(List<String> dataList, int defaultBitmap);
    void setImgDelay(int imgDelay);
    void startBanner();
    void startAutoPlay();
    //改变资源时要重新开启循环
    void dataChange(List<String> list);
}
