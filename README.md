# MBanner
【视频图片混播】支持视频边缓存边播放，支持Glide加载图片

## 功能介绍
- [x] 支持图片和视频的混合播放
- [x] 暂时支持视频为MP4格式
- [x] 支持主流图片加载框架Glide
- [x] 支持获取视频时长
- [x] 支持图片和视频缓存(接入DKVideoPlayer带缓冲的播放器)
	
## 效果显示

## 开放API
### 该方法属于MBanner
方法名  | 方法描述  | 说明
 ---- | ----- | ------  
 setDataList(List<String> dataList)  |设置图片和视频的集合
 setPlaceholder(ImageView imageViewId, int defaultBitmap)|imageViewId 占位图id  defaultBitmap占位图片资源|请注意调用顺序
 setImgDelay(int imgDelay)  | 延时播放 | int类型毫秒
 startBanner()  | 开始轮播 | 设置完毕后开始调用
 startAutoPlay() | 开始视频自动播放 | 自动播放视频
 
### 1.1添加轮Gradle依赖
```groovy
implementation 'com.meiguoxin:mbanner:1.0.0'
```
### 1.2添加相关Gradle依赖(推荐使用DKVideoPlayer 3.1.4版本)
```groovy
//必选，内部默认使用系统mediaplayer进行解码
implementation 'com.github.dueeeke.dkplayer:dkplayer-java:3.1.4'
//可选，包含StandardVideoController的实现
implementation 'com.github.dueeeke.dkplayer:dkplayer-ui:3.1.4'
//可选，使用exoplayer进行解码（推荐）
implementation 'com.github.dueeeke.dkplayer:player-exo:3.1.4'
//可选，使用ijkplayer进行解码
implementation 'com.github.dueeeke.dkplayer:player-ijk:3.1.4'
//可选，如需要缓存或者抖音预加载功能请引入此库
implementation 'com.github.dueeeke.dkplayer:videocache:3.1.4'
//可选，如需要缓存或者抖音预加载功能请引入此库
implementation 'com.github.dueeeke.dkplayer:videocache:3.1.4'
implementation 'com.github.bumptech.glide:glide:4.9.0'
implementation 'com.google.sitebricks:slf4j:0.8.3'
```
### 2.在xml文件中添加 MBanner
```xml
  <!--占位图控件-->
<ImageView
     android:id="@+id/splash_logos"
     android:layout_width="360dp"
     android:layout_height="match_parent"
     android:layout_gravity="center" />

<com.mgx.mbanner.custom.MBanner
    android:id="@+id/banner"
    android:layout_width="match_parent"
    android:layout_height="200dp"
    app:layout_constraintBottom_toBottomOf="parent"
    app:layout_constraintLeft_toLeftOf="parent"
    app:layout_constraintRight_toRightOf="parent"
    app:layout_constraintTop_toTopOf="parent">

</com.mgx.mbanner.custom.MBanner>
```
### 3.在 Activity 或者 Fragment 中配置 MBanner 的数据源
```java
 private void initView() {
   mImageView = findViewById(R.id.image);
   mBanner = findViewById(R.id.banner);
   mList = new ArrayList<>();
   mList.add("http://vfx.mtime.cn/Video/2019/03/12/mp4/190312143927981075.mp4");
   mList.add("http://img2.imgtn.bdimg.com/it/u=3817131034,1038857558&fm=27&gp=0.jpg");
   mList.add("http://img1.imgtn.bdimg.com/it/u=4194723123,4160931506&fm=200&gp=0.jpg");
   mList.add("http://img5.imgtn.bdimg.com/it/u=1812408136,1922560783&fm=27&gp=0.jpg");
   //数据源
   mBanner.setDataList(mList);
   //没有数据时占位图
   mBanner.setPlaceholder(mImageView, R.mipmap.boan_ico);
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

	Copyright 2020 MeiGuoXin

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.