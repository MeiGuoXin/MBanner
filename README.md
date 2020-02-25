# MBanner
【视频图片混播】支持视频边缓存边播放，支持Glide加载图片
## 功能介绍
- [x] 支持图片和视频的混合播放
- [x] 暂时支持视频为MP4格式
- [x] 支持主流图片加载框架Glide
- [x] 支持获取视频时长
- [x] 支持图片和视频缓存
	
## 效果显示

## 开放API
### 该方法属于MBanner
方法名  | 方法描述  | 说明
 ---- | ----- | ------  
 setDataList(List<String> dataList, int defaultBitmap)  |设置图片和视频的集合  |  defaultBitmap图片占位图(未完善)暂时传入0
 setImgDelay(int imgDelay)  | 延时播放 | int类型毫秒
 startBanner()  | 开始轮播 | 设置完毕后开始调用
 startAutoPlay() | 开始视频自动播放 | 视频播放
 
## 基本使用	
### 1.添加远程仓库
```groovy
allprojects {
  repositories {
  ...
  maven { url 'https://jitpack.io' }
}
```
### 2.添加Gradle 依赖
```groovy
 implementation 'com.github.MeiGuoXin:MBanner:1.0.0'
```
### 3在xml文件中添加 MBanner
```xml
 <com.mgx.mbanner.custom.MBanner
   android:id="@+id/banner"
   android:layout_width="match_parent"
   android:layout_height="260dp">
 </com.mgx.mbanner.custom.MBanner>
```
### 4.在 Activity 或者 Fragment 中配置 MBanner 的数据源
```java
 private void initView() {
   banner = findViewById(R.id.banner);
   HttpProxyCacheServer proxy = MyApplication.getProxy(getApplication());
   String proxyUrl = proxy.getProxyUrl("http://vfx.mtime.cn/Video/2019/03/12/mp4/190312143927981075.mp4");
   list = new ArrayList<>();
   list.add(proxyUrl);
   list.add("http://img2.imgtn.bdimg.com/it/u=3817131034,1038857558&fm=27&gp=0.jpg");
   list.add("http://img1.imgtn.bdimg.com/it/u=4194723123,4160931506&fm=200&gp=0.jpg");
   list.add("http://img5.imgtn.bdimg.com/it/u=1812408136,1922560783&fm=27&gp=0.jpg");
   //数据源
   banner.setDataList(list,0);
   //设置延时播放
   banner.setImgDelay(5000);
   //开始轮播
   banner.startBanner();
   //自动播放
   banner.startAutoPlay();
}
```
### 5.在Activity的onDestroy中进行销毁
```java 
@Override
protected void onDestroy() {
     super.onDestroy();
     banner.destroy();
 }
```
### 感谢
[沉默ne](https://blog.csdn.net/a598068693/article/details/80341099)
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