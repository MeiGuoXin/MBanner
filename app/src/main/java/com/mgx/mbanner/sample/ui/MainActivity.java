package com.mgx.mbanner.sample.ui;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.mgx.mbanner.custom.MBanner;
import com.mgx.mbanner.sample.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private MBanner banner;
    private List<String> list;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_layout);
        initView();
    }

    private void initView() {
        imageView = findViewById(R.id.image);
        banner = findViewById(R.id.banner);
        //HttpProxyCacheServer proxy = MyApplication.getProxy(getApplication());
        //String proxyUrl = proxy.getProxyUrl("http://vfx.mtime.cn/Video/2019/03/12/mp4/190312143927981075.mp4");
        list = new ArrayList<>();
        //list.add(proxyUrl);
        list.add("http://vfx.mtime.cn/Video/2019/03/12/mp4/190312143927981075.mp4");
        list.add("http://img2.imgtn.bdimg.com/it/u=3817131034,1038857558&fm=27&gp=0.jpg");
        list.add("http://img1.imgtn.bdimg.com/it/u=4194723123,4160931506&fm=200&gp=0.jpg");
        list.add("http://img5.imgtn.bdimg.com/it/u=1812408136,1922560783&fm=27&gp=0.jpg");
        banner.setDataList(list);
        banner.setPlaceholder(imageView, R.mipmap.ic_launcher);
        banner.setImgDelay(5000);
        banner.startBanner();
        banner.startAutoPlay();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        banner.destroy();
    }
}
