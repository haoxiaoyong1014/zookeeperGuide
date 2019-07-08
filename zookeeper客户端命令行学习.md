#### zk常用命令行操作

* 通过``./zkCli.sh`打开 zk的客户端进行命令行后台

  >  进入 zookeeper安装的 bin目录,ls会看到如下:

![image.png](https://upload-images.jianshu.io/upload_images/15181329-de01f007adf9182f.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

> 键入: `zkCli.sh`,出现如图说明已经是连接状态

![image.png](https://upload-images.jianshu.io/upload_images/15181329-bbc358f72198e9e8.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

* `ls` 与 `ls2` 命令

  > ls path [watch]  
  >
  > ls2 path [watch]

键入:`ls /`

![image.png](https://upload-images.jianshu.io/upload_images/15181329-4b502498a90bd1b5.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

会看到有 dubbo,zookeeper,test,这里我们吧 dubbo和 test先忽略,这是之前项目中使用,后续会介绍到;

这里我们先关注 zookeeper 节点 ;

我们如果想看zookeeper下面还有什么,我们可以键入: `ls /zookeeper`,会看到一个quota目录,quota目录是 zookeeper一个节点;

![image.png](https://upload-images.jianshu.io/upload_images/15181329-4701d67f7455cdf7.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

下面我们看下`ls2`命令:

![image.png](https://upload-images.jianshu.io/upload_images/15181329-e17e2e1eaff99fe4.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

会发现除了显示了quota节点以外还出现了一些节点的状态信息;下面会说到;

* `get`与 `stat` 命令

> stat 就是 status 的简写,键入: `stat /zookeeper`

![image.png](https://upload-images.jianshu.io/upload_images/15181329-d6d228511a026601.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

可以看到`ls2`与`stat`命令输出的基本是一样的;也就是说`ls2`整合了`stat`命令;

`get`:键入`get /zookeeper`

![image.png](https://upload-images.jianshu.io/upload_images/15181329-bb94e760d63c5dd6.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

这个命令会把当前节点中的数据取出来,当前节点数据为空;

`cZxid`:节点Id

`ctime`:这个节点创建的时间

`mZxid`:修改之后的 id

`mtime`:修改的时间,如果没有被修改就是和创建时间是一致的;

`pZxid`:子节点 id

`cversion`:子节点的版本,如果子节点发生变化这个值就会发生变化

`dataVersion`:当前节点一个数据的版本号,如果当前节点的数据被修改这个值会累加1;

`aclVersion`:权限版本,如果权限发生变化会累加1

`dataLength`:数据长度

`numChildren`:子节点数量