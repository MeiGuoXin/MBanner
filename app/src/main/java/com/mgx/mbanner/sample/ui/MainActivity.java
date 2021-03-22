package com.mgx.mbanner.sample.ui;

import android.os.Bundle;

import com.mgx.mbanner.custom.MBanner;
import com.mgx.mbanner.sample.R;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private MBanner banner;
    private List<String> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_layout);
        initView();
    }

    private void initView() {
        banner = findViewById(R.id.banner);
        list = new ArrayList<>();
        list.add("https://mp4.vjshi.com/2020-12-02/6abe9321e8911b4f8671c4e8e2ad2d05.mp4");
        list.add("https://seopic.699pic.com/photo/40187/0349.jpg_wh1200.jpg");
        list.add("https://seopic.699pic.com/photo/40180/4128.jpg_wh1200.jpg");
        list.add("https://seopic.699pic.com/photo/40186/8031.jpg_wh1200.jpg");
        banner.setDataList(list);
        banner.setIndicatorRes(R.drawable.shape_point_select,R.drawable.shape_point_unselect);
        banner.startBanner();
        banner.startAutoPlay();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        banner.destroy();
    }
}
