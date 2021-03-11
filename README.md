# MBanner
【视频图片混播】支持视频边缓存边播放，支持Glide加载图片，支持空数据展示占位图

## 功能介绍
- [x] 支持图片和视频的混合播放
- [x] 支持视频mp4、m3u8、flv、avi、file、mkv、https、http播放格式
- [x] 支持主流图片加载框架Glide
- [x] 支持获取视频时长
- [x] 支持图片和视频缓存(接入DKVideoPlayer带缓冲的播放器)
- [x] 图片占位图(待开发)
- [x] 图片错误图(待开发)
- [x] 轮播图指示器(待开发)
- [x] 轮播图指示器位置(待开发)
- [x] 轮播图上打水印(待开发) 
- [x] 无数据占位图(待开发) 
- [x] 点击图片时间回调(待开发) 

## 效果显示
![image](https://github.com/MeiGuoXin/MBanner/blob/master/app/src/main/java/com/mgx/mbanner/sample/image/1585732787919.gif)

## 开放API
方法名  | 方法描述  | 说明
 ---- | ----- | ------  
 setDataList(List<String> dataList)  |设置图片和视频的集合
 startBanner()  | 开始轮播 | 设置完毕后开始调用
 startAutoPlay() | 开始视频自动播放 | 自动播放视频
  
## 属性介绍
属性名  | 描述
 ----  | -----  | ------  
 imgDelay  | 轮播图的时间间隔
 isAutomaticVideoPlayback  | 自动播放视频
 isVideoController  | 是否显示视频控制器
 isPreloading  | 是否预加载 
 preloadingPages  | 预加载页数

  
### 1.添加主Gradle依赖
```groovy
implementation 'com.meiguoxin:mbanner:1.0.1'
```
### 2.在xml文件中添加 MBanner
```xml
  <com.mgx.mbanner.custom.MBanner
        android:id="@+id/banner"
        android:layout_width="match_parent"
        android:layout_height="260dp"
        app:imgDelay="2000"
		app:isPreloading="true"
		app:preloadingPages="1"
        app:isAutomaticVideoPlayback="true"
        app:isDisplayIndicator="true"
        app:isVideoController="false">
       
    </com.mgx.mbanner.custom.MBanner>
```
### 3.在 Activity 或者 Fragment 中配置 MBanner 的数据源
```java
 private void initView() {
   mBanner = findViewById(R.id.banner);
   mList = new ArrayList<>();
   list.add("https://mp4.vjshi.com/2020-12-02/6abe9321e8911b4f8671c4e8e2ad2d05.mp4");
   list.add("https://seopic.699pic.com/photo/40187/0349.jpg_wh1200.jpg");
   list.add("https://seopic.699pic.com/photo/40180/4128.jpg_wh1200.jpg");
   list.add("https://seopic.699pic.com/photo/40186/8031.jpg_wh1200.jpg");
   //数据源
   mBanner.setDataList(mList);
   //设置延时播放
   mBanner.setImgDelay(5000);
   //开始轮播
   mBanner.startBanner();
   //自动播放
   mBanner.startAutoPlay();
}
```
### 4.在Activity的onDestroy中进行销毁
```java 
@Override
protected void onDestroy() {
     super.onDestroy();
     banner.destroy();
 }
```
### 感谢
[沉默ne](https://blog.csdn.net/a598068693/article/details/80341099)
[DKVideoPlayer](https://github.com/dueeeke/DKVideoPlayer)
## License

	Copyright 2021 MeiGuoXin

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.