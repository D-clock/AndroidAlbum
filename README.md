# AndroidAlbum

库如其名，做过企业的应用已经有三四个，但凡所有应用基本都有跳转到相册或者调用系统拍照的功能（例如所有应用都可以上传头像）。因此，为了方便公司或者自己的开发，抽空准备整理出一个比较完善的库，方便以后开发可以随时拉取代码。如果你对这部分的代码感兴趣，欢迎引入使用，如果引用过程中发现遇到什么闪退，麻烦在Github上给我提个issue，我会尽快定位修复。**需要导入工程运行的童鞋，请详细阅读下方的编译须知部分内容**

## 最新更新（最后编辑于2016-05-28）

- 优化代码结构
- 优化项目导入体验

## 目前已有功能

- 展示系统所有带图片的目录，以及展示图片目录下所有图片

- 点击图片预览大图功能，支持左右滑动切换和缩放功能

- 闪退日志本地化存储功能，方便开发者本地查看

- 腾讯bugly SDK的引入，用于上报crash的日志，方便远程定位错误

- 图片预览界面添加了选图功能，预览页单击图片会出现沉浸模式（Immersive-Mode ，Android 4.4开始有的系统特性）

## 目前的效果

- 本地图片选择功能

![本地图片选择功能](http://f.hiphotos.baidu.com/image/pic/item/ae51f3deb48f8c54c954df5f3d292df5e0fe7f3e.jpg)

- 图片详情预览页面，添加选图功能和沉浸模式（Immersive-Mode）效果

![选图功能和沉浸模式效果](http://b.hiphotos.baidu.com/image/pic/item/838ba61ea8d3fd1f3071ac4c374e251f95ca5f4f.jpg)

## 接下来想做的

- 引入其他第三方加载图片框架库（Picasso，Glide，Fresco）的使用，并且进行分析汇总整理


## 闪退日志处理

**1.本地闪退日志处理**

> 本地化存储闪退日志信息除了闪退的log外，还包含：设备厂商，设备名称，系统版本号，app版本号，设备id（IMEI）等。发生闪退后可以通过文件浏览器在SD卡上找到报错的log信息。（目前闪退日志是存放到SD下的album目录下的crash目录中。想要自己指定到其他目录的，可以在AlbumApplication中的configCollectCrashInfo函数）

闪退日志命名格式：发生闪退的时间（yyyyMMddHHmm 年月日时分秒）.log

![闪退后生成日志](http://g.hiphotos.baidu.com/image/pic/item/d0c8a786c9177f3ed17a360377cf3bc79f3d5676.jpg)

**2.闪退日志回传服务器处理**

> 目前已经提供闪退日志回传到远程服务器的接口，有需要可以自行在AlbumApplication配置作如下实现!（发生闪退时，会回调onCrash方法，可以在此方法中讲闪退信息传回服务器）

![配置log回传服务器](http://h.hiphotos.baidu.com/image/pic/item/dbb44aed2e738bd494f0643fa68b87d6267ff9ef.jpg)

**3.第三方上报crash功能的SDK引入**

> 目前已经引入大鹅厂的[Bugly](http://bugly.qq.com/)（不得不佩服鹅厂的科技，真心牛逼）。这里引入第三方SDK仅仅只是为了跟踪一些BUG，并没有其他意图，不需要的童鞋可以自行移除掉。

## 引用第三方库

- 图片加载框架：[Android-Universal-Image-Loader](https://github.com/nostra13/Android-Universal-Image-Loader)

- 图片缩放控件：[PhotoView](https://github.com/chrisbanes/PhotoView)

- 自己写的一个实用工具类库：[AndroidUtils](https://github.com/D-clock/AndroidUtils)

## 一些拓展处理

- 为了方便项目的拓展，对引入的一些第三方库进行多加一层的抽象封装。如：当前库中引用的加载图片框架采用了[Android-Universal-Image-Loader](https://github.com/nostra13/Android-Universal-Image-Loader)，为了降低项目对具体载图框架的依赖，特地使用工厂模式且加多了一层ImageLoaderWrapper对框架进行抽象解耦，这样为我后续替换其他加载图片框架节约了修改代码的成本。

- 项目的编码设计采用了MVP架构，尽量的分离业务和UI，使得UI层的Activity和Fragment和业务层的代码显得松耦合。 

## 编译须知

* 因为该开源项目引入了Bugly的SDK，所以在导入AndroidStudio时候，需要在Project的build.gradle加上如下配置

``` groovy
 classpath 'com.tencent.bugly:symtabfileuploader:latest.release'
```

![配置build.gradle](http://e.hiphotos.baidu.com/image/pic/item/8601a18b87d6277f529944492f381f30e924fc3c.jpg)

如果因为gradle版本太高，会在build过程中提示下面这种错误

``` groovy
Error: NDK integration is deprecated in the current plugin.  Consider trying the new experimental plugin.....
```

所以，你还要到Project的gradle.properties文件中加上 **android.useDeprecatedNdk=true**

![配置gradle.properties](http://e.hiphotos.baidu.com/image/pic/item/5366d0160924ab1803d8904632fae6cd7a890bf0.jpg)

至于其他关于bugly的问题，就需要自己去官网找答案了！

* 本库libs下有个**AndroidUtils.aar**的依赖包，是由[AndroidUtils](https://github.com/D-clock/AndroidUtils)代码编译生成，想要查看**AndroidUtils.aar**中的源代码，可以参考[这里](https://github.com/D-clock/Doc/blob/master/Android/%E4%B8%AA%E4%BA%BA%E6%94%B6%E8%97%8F/%E5%A6%82%E4%BD%95%E6%9F%A5%E7%9C%8Baar%E7%9A%84%E6%BA%90%E4%BB%A3%E7%A0%81.md)的方式进行导入。

* 本库当前使用的[Android-Universal-Image-Loader](https://github.com/nostra13/Android-Universal-Image-Loader)框架版本是1.9.5，是以jar包的方式导入。

* 本库当前使用的[PhotoView](https://github.com/chrisbanes/PhotoView)自定义控件的版本是1.2.4，以aar的方式导入。