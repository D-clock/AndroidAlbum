# 我的 Android 项目开发实战经验总结

以前一直想写一篇总结 Android 开发经验的文章，估计当时的我还达不到某种水平，所以思路跟不上，下笔又捉襟见肘。近日，思路较为明朗，于是重新操起键盘开始码字一番。**先声明一下哈，本人不是大厂的程序猿。去年毕业前，就一直在当前创业小团队从事自己热爱的打码事业至今。下面总结是建立在我当前的技术水平和认知上写的，如有不同看法欢迎留下评论互相交流。**

## 1.理解抽象，封装变化

目前 Android 平台上绝大部分开发都是用着 Java ，而跟 Java 这样一门面向对象的语言打交道，不免要触碰到 **抽象** 和 **封装** 的概念。我身边接触过的一些开发者，有一部分还对这些概念停留在写一个抽象类、接口、或者一个方法（或抽象方法）。至于为什么，我不大清楚是他们表达不出来，还是不理解。下面我也不高谈阔论，直接举例子来解释我所理解的抽象。

```java

//Activity 间使用 Intent 传递数据的两种写法 下面均是伪代码形式，请忽略一些细节

//写法一

//SrcActivity 传递数据给 DestActivity
Intent intent = new Intent(this,DestActivity.class);
intent.putExtra("param", "clock");
SrcActivity.startActivity(intent);

//DestActivity 获取 SrcActivity 传递过来的数据
String param = getIntent.getStringExtra("param");

//写法二

//SrcActivity 传递数据给 DestActivity
Intent intent = new Intent(this,DestActivity.class);
intent.putExtra(DestActivity.EXTRA_PARAM, "clock");
SrcActivity.startActivity(intent);

//DestActivity 获取 SrcActivity 传递过来的数据
public final static String EXTRA_PARAM = "param";
String param = getIntent.getStringExtra(EXTRA_PARAM);

```

写法一，存在的问题是，如果 SrcActivity 和 DestActivity 哪个把 "param" 打错成 "para" 或者 "paran" ，传递的数据都无法成功接收到。而写法二则不会出现此类问题，因为两个 Activity 之间传递数据只需要知道 EXTRA_PARAM 变量即可，至于 EXTRA_PARAM 变量到底是  "param" 、 "para" 、"paran" 这一点并不需要关心，这就是一种对可能发生变化的地方进行抽象封装的体现，它所带来的好处就是降低手抖出错的概率，同时方便我们进行修改。

基于抽象和封装，Java 本身很多 API 在设计上就有这样的提现，如 Collections 中的很多排序方法：

![Collections中的排序API](http://f.hiphotos.baidu.com/image/pic/item/f9dcd100baa1cd11f75a7e98be12c8fcc2ce2d9d.jpg)

这些方法都是基于 List 这个抽象的列表接口进行排序，至于这是一个用什么样的数据结构实现 List（ArrayList 还是 LinkedList），排序方法本身并不关心。看，是不是体现了 JDK 的设计人员的一种抽象编程的思维，因为 List 的具体实现可能有千万种，如果每一类 List 都要写一套排序方法，估计要哭瞎了。

> 小结：把容易出现变化的部分进行抽象，就是对变化的一种封装。

## 2.选好"车轮"

一个项目的开发，我们不可能一切从0做起，如果真是这样，那同样要哭瞎。因此，善于借用已经做好的 "车轮" 非常重要，如：

网络访问框架：okhttp、retrofit、android-async-http、volley
图片加载框架：Android-Universal-Image-Loader、Glide、Fresco、Picasso
缓存框架：DiskLruCache、 Robospice
Json解析框架：Gson、Fastjson、Jackson
事件总线：EventBus、Otto
ORM框架：GreenDAO、Litepal
第三方SDK：友盟统计，腾讯bugly、七牛...
...
...
还有其他各种各样开源的自定义控件、动画等。

一般情况下，我在选择是否引入一些开源框架主要基于以下几个因素：

- 借助搜索引擎，如果网上有一大波资料，说明使用的人多，出了问题好找解决方案；当然，如果普遍出现差评，就可以直接Pass掉了
- 看框架的作者或团队，如 [JakeWharton大神](https://github.com/JakeWharton)、[Facebook团队](https://code.facebook.com/)等。大神和大公司出品的框架质量相对较高，可保证后续的维护和bug修复，不容易烂尾；
- 关注开源项目的 commit密度，issue的提交、回复、关闭数量，watch数，start数，fork数等。像那种个基本不怎么提交代码、提issue又不怎么回复和修复的项目，最好就pass掉；

针对第三方SDK的选择，也主要基于以下几点去考虑：

- 借助搜索引擎，查明口碑；
- 很多第三方SDK的官网首页都会告诉你，多少应用已经接入了此SDK，如果你看到有不少知名应用在上面，那这个SDK可以考虑尝试一下了。诸如，友盟官网：

![接入友盟的App](http://b.hiphotos.baidu.com/image/pic/item/b3119313b07eca807488435f962397dda04483fc.jpg)

- 查看SDK使用文档、它们的开发者社区、联系客服。好的SDK，使用文档肯定会详细指引你。出了问题，上开发者社区提问，他们的开发工程师也会社区上回答。实在不行只能联系客服，如果客服的态度都让你不爽，那就可以考虑换别家的SDK了。

> 小结：选好 "车轮" ，事半功倍

## 3.抽象依赖第三方框架

为什么要抽象依赖于第三方框架呢？这里和第1点是互相照应的，就是降低我们对具体某个框架的依赖性，从而方便我们快速切换到不同的框架去。说到这里，你可能觉得很抽象，那我直接举一个加载图片的例子好了。

假设你当前为项目引入一个加载图片的框架 —— Android-Universal-Image-Loader，最简单的做法就是加入相应的依赖包后，在任何需要加载图片的地方写上下面这样的代码段。

```java

ImageLoader imageLoader = ImageLoader.getInstance(); // Get singleton instance
// Load image, decode it to Bitmap and display Bitmap in ImageView (or any other view 
//  which implements ImageAware interface)
imageLoader.displayImage(imageUri, imageView);
// Load image, decode it to Bitmap and return Bitmap to callback
imageLoader.loadImage(imageUri, new SimpleImageLoadingListener() {
    @Override
    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
        // Do whatever you want with Bitmap
    }
});

```

这种做法最简单粗暴，但是带来的问题也最严重的。如果我有几十上百个地方都这么写，而在某一天，我听说Facebook出了个神器 Fresco，想要换掉 Android-Universal-Image-Loader ，你就会发现你需要丧心病狂的去改动几十上百个地方的代码，不仅工作量大，而且还容易出错。造成这样的原因，就在于项目和加载图片的框架之间形成了**强耦合**，而实际上，项目本身不应该知道我具体用了哪个加载图片的框架。

正确的方式，应该是对框架做一个抽象的封装，以应对未来发生的变化，我直接举自己的开源项目 [AndroidAlbum](https://github.com/D-clock/AndroidAlbum) 中的一种封装做法好了。

![AndroidAlbum](http://c.hiphotos.baidu.com/image/pic/item/377adab44aed2e7339a6e4948001a18b87d6fa3f.jpg)

大致代码如下：

```java
//1、声明 ImageLoaderWrapper 接口，定义一些抽象的加载接口方法

public interface ImageLoaderWrapper {

    /**
     * 显示 图片
     *
     * @param imageView 显示图片的ImageView
     * @param imageFile 图片文件
     * @param option    显示参数设置
     */
    public void displayImage(ImageView imageView, File imageFile, DisplayOption option);

    /**
     * 显示图片
     *
     * @param imageView 显示图片的ImageView
     * @param imageUrl  图片资源的URL
     * @param option    显示参数设置
     */
    public void displayImage(ImageView imageView, String imageUrl, DisplayOption option);

    /**
     * 图片加载参数
     */
    public static class DisplayOption {
        /**
         * 加载中的资源id
         */
        public int loadingResId;
        /**
         * 加载失败的资源id
         */
        public int loadErrorResId;
    }
}

// 2、将 UniversalAndroidImageLoader 封装成继承 ImageLoaderWrapper 接口的 UniversalAndroidImageLoader ，
//这里代码有点长，感兴趣可以查看项目源码中的实现 https://github.com/D-clock/AndroidAlbum

// 3、做一个ImageLoaderFactory

public class ImageLoaderFactory {

    private static ImageLoaderWrapper sInstance;

    private ImageLoaderFactory() {

    }

    /**
     * 获取图片加载器
     *
     * @return
     */
    public static ImageLoaderWrapper getLoader() {
        if (sInstance == null) {
            synchronized (ImageLoaderFactory.class) {
                if (sInstance == null) {
                    sInstance = new UniversalAndroidImageLoader();//<link>https://github.com/nostra13/Android-Universal-Image-Loader</link>
                }
            }
        }
        return sInstance;
    }
}

//4、在所有需要加载图片的地方作如下的调用

ImageLoaderWrapper loaderWrapper = ImageLoaderFactory.getLoader();
ImageLoaderWrapper.DisplayOption displayOption = new ImageLoaderWrapper.DisplayOption();
displayOption.loadingResId = R.mipmap.img_default;
displayOption.loadErrorResId = R.mipmap.img_error;
loaderWrapper.displayImage(imagview, url, displayOption);

```

这样一来，切换框架所带来的代价就会变得很小，这就是不直接依赖于框架所带来的好处。当然，以上只是我比较简单的封装，你也可以进行更加细致的处理。

> 小结：预留变更，不强耦合于第三方框架

## 4.从 MVC 到 MVP

说实话，在没接触 MVP 的架构之前，一直都是使用 MVC 的模式进行开发。而随着项目越来越大，Activity或者 Fragment里面代码越来越臃肿，看的时候想吐，改的时候想屎...这里撇开其他各种各样的架构不谈，只对比MVC 和 MVP 。

![MVC](http://d.hiphotos.baidu.com/image/pic/item/9e3df8dcd100baa1961910034010b912c8fc2e54.jpg)

- View：布局的xml文件
- Controller：Activity、Fragment、Dialog等
- Model:相关的业务操作处理数据（如对数据库的操作、对网络等的操作都应该在Model层里）

你会发现，如果 View 层只包含了xml文件，那我们 Android 项目中对 View 层可做操作的程度并不大，顶多就是用include复用一下布局。而 Activity 等简直就是一个奇葩，它虽然归属于 Controller 层，但实际上也干着 View 层的活（View 的初始化和相关操作都是在Activity中）。就是这种既是 View 又是 Controller 的结构，违背了单一责任原则，也使得 Activity 等出现了上述的臃肿问题。

![MVP](http://g.hiphotos.baidu.com/image/pic/item/d833c895d143ad4baf58147885025aafa40f0617.jpg)

- View：Activity、Fragment、Dialog、Adapter等，该层不包含任何业务逻辑
- Presenter：中介，View 与 Model 不发生联系，都通过 Presenter 传递
- Model:相关的业务操作处理数据（如对数据库的操作、对网络等的操作都应该在Model层里）

相比 MVC，MVP在层次划分上更加清晰了，不会出现一人身兼二职的情况（有些单元测试的童鞋，会发现单元测试用例更好写了）。在此处你可以看到 View 和 Model 之间是互不知道对方存在的，这样应对变更的好处更大，很多时候都是 View 层的变化，而 Model 层发生的变化会相对较少，遵循 MVP 的结构开发后，改起来代码来也没那么蛋疼。
这里也有地方需要注意，因为大量的交互操作集中在 Presenter 层中，所以需要把握好 Presenter 的粒度，一个 Activity 可以持有多个 View 和 Presenter，这样也就可以避开一个硕大的 View 和 Presenter 的问题了。

推荐两个不错的 MVP 架构的项目给大家，还不明白的童鞋，可以自行体会一下其设计思想：

https://github.com/pedrovgs/EffectiveAndroidUI
https://github.com/antoniolg/androidmvp

> 小结：去加以实践的理解 MVP 吧

## 5.归档代码

把一些常用的工具类或业务流程代码进行归类整理，加入自己的代码库（还没有自己个人代码仓库的童鞋可以考虑建一个了）。如加解密、拍照、裁剪图片、获取系统所有图片的路径、自定义的控件或动画以及其其他他一些常用的工具类等。归档有助于提高你的开发效率，在遇到新项目的时候随手即可引入使用。如果你想要更好的维护自己的代码库，**不妨在不泄露公司机密的前提下，把这个私人代码库加上详细文档给开源出去。** 这样能够吸引更多开发者来使用这些代码，也可以获得相应的bug反馈，以便于着手定位修复问题，增强这个仓库代码的稳定性。

> 小结：合理归档代码，可以的话，加以开源维护

## 6.性能优化

关于性能优化的问题，大体都还是关注那几个方面：内存、CPU、耗电、卡顿、渲染、进程存活率等。对于这些地方的性能优化思路和分析方法，网络上已经有很多答案了，此处不做赘述。我只想说以下几点：

- 不要过早的做性能优化，app先求能用再求好用。在需求都还没完成的时候把大量时间花在优化上是本末倒置的；
- 优化要用实际数据说话，借助测试工具进行检测（如：网易的Emmagee、腾讯的GT和APT，科大讯飞的iTest，Google的Battery Historian）。毕竟老板问你比以前耗电降低多少，总不能回答降低了一些吧???
- 任何不以减低性能损耗来做保活的手段，都是耍流氓。

> 小结：合理优化，数据量化

## 7.实践新技术

Rxjava、React Native、Kotlin...开始兴起后，身边有很多开发者会跟风直上。学习新技术的精神是非常值得鼓励的，但没有经过一段时间实践观察，就擅自把新技术引入到商业项目中，则有失妥当。对于大公司的团队来说，会有专门团队或项目去研究这些新兴技术，以确定是否在自己的产品线开发中引入。但作为小公司，是不是就意味着没有实践尝试新技术的机会呢？并不是！个人有以下几点建议：

- 借助搜索引擎。看此项技术坑多不多，口碑不错但是坑多的话，则说明当前技术不成熟，可以耐心等待更新；
- 考虑学习成本。学习成本太大且不容易招到懂这方面的开发者的情况下，建议不要引入该技术；
- 高仿一个项目并开源。如果你想引入 React Native 做商业开发，最好先高仿实现一个应用然后将其开源。这样一些对 RN 感兴趣的开发者会运行你的代码并反馈 bug 给你，有助于你知道一些新技术的坑，并寻找相应的解决方案，最终确定是否引入该技术；
- 降低入门门槛。实践新技术的过程尽量加以详细的文档记录，这会有助于降低项目组其他同事对新技术的入门门槛，可以的话，也将学习文档开源，获得更多开发者对此份文档的反馈，也可纠正一些文档中的错误；
- 结合实际业务。所有新技术的引入都要考虑是否符合当下的业务需求，我听过有些程序猿想引入新技术的原因是因为觉得这种技术很酷，网上说很好用，很啥啥啥...自己完全没弄过就人云亦云。有时候好无语，感觉在会用一些技术就像在炫技一样；

> 小结：空谈误国，实干兴邦

## 8.UML

UML，驯服代码和了解项目结构的利器，本人也在学习和体验其好处的路途上。不管遇到大小项目，有了它，可以更好的理清一些脉络结构。对付旧的庞大项目代码，或者有志阅读某些开源项目代码的开发者，绝对是居家必备。

> 小结：工欲善其事，必先利其器

## 9.自造"车轮"

前面 2 提到，项目不可能从0开始，是需要引入很多第三方框架的。这里并不与 2 互相违背，而是建议有想提高技术逼格的开发者，可以在空暇时间去编码实现一个框架。如果你对网络访问、图片加载方面很有研究见解，不妨把这些脑海里的思想落实成具体的代码。也许你会发现，你动手去实践的时候，考虑的东西会多得多，自己最终得到的也会更多。（**特别建议那些看过很多开源代码，又至今未自己动手自撸一发的**）

> 小结：不要停留在 api 调用的层面

## 10.扩大技术圈

有空又经济能力承受得起的时候，不妨去参加一些自己感兴趣的技术交流会。很多都有大牛上台演讲，听听人家的解决方案，拓宽一下自己看问题的思路，也可以多参加一些含金量高的线上活动。我有挺多开发者朋友，就是参加活动的时候认识的，有时候遇到一些技术问题，还会互相探讨交换一下解决思路。挺赞的！

> 小结：拓宽技术视野

## 11.写博客总结

这个可能没什么好说的，大家看了标题就懂了。它最大的好处在于：

- 系统化记录自己的解决方案；
- 方便日后自己回顾；
- 有问题也会有读者评论反馈，促进技术交流；
- 增强自己书面表达能力；

> 小结：认真总结，不断完善

## 12.找个对象

程序猿不要老是对着电脑，赶紧找个对象提升一下幸福感。据说幸福感高的程序猿，编码效率高，出bug几率小...

> 总结：做个面向对象的程序员


大概就想到这些了，以后要是再有想写的，另开新篇。絮絮叨叨写了这么多，最关键的还是自己要落实，千万不要听说过太多道理，却依然过不好这一生哈！！！！