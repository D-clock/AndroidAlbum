# AndroidAblum

库如其名，做过企业的应用已经有三四个，但凡所有应用基本都有跳转到相册或者调用系统拍照的功能（例如所有应用都可以上传头像）。因此，为了方便公司或者自己的开发，抽空准备整理出一个比较完善的库，方便以后开发可以随时拉取代码。如果你对这部分的代码感兴趣，欢迎引入使用，如果引用过程中发现遇到什么闪退，麻烦在Github上给我提个issue，我会尽快定位修复。

## 目前已有功能（编辑于2016-01-25）

- 展示系统所有带图片的目录，以及展示图片目录下所有图片

- 点击图片预览大图功能，支持左右滑动切换和缩放功能

- 闪退日志本地化存储功能。

## 闪退日志处理

**1.本地闪退日志处理**

> 本地化存储闪退日志信息除了闪退的log外，还包含：设备厂商，设备名称，系统版本号，app版本号，设备id（IMEI）等。发生闪退后可以通过文件浏览器在SD卡上找到报错的log信息。（目前闪退日志是存放到SD下的album目录下的crash目录中。想要自己指定到其他目录的，可以在AlbumApplication中的configCollectCrashInfo函数）

闪退日志命名格式：发生闪退的时间（yyyyMMddHHmm 年月日时分秒）.log

![闪退后生成日志](http://g.hiphotos.baidu.com/image/pic/item/d0c8a786c9177f3ed17a360377cf3bc79f3d5676.jpg)

**2.闪退日志回传服务器处理**

> 目前已经提供闪退日志回传到远程服务器的接口，有需要可以自行在AlbumApplication配置作如下实现!（发生闪退时，会回调onCrash方法，可以在此方法中讲闪退信息传回服务器）

![配置log回传服务器](http://h.hiphotos.baidu.com/image/pic/item/dbb44aed2e738bd494f0643fa68b87d6267ff9ef.jpg)

## 引用第三方库

- 图片加载框架：[Android-Universal-Image-Loader](https://github.com/nostra13/Android-Universal-Image-Loader)

- 图片缩放控件：[PhotoView](https://github.com/chrisbanes/PhotoView)

- 自己写的一个实用工具类库：[AndroidUtils](https://github.com/D-clock/AndroidUtils)