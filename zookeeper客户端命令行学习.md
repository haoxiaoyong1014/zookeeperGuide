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

`ephemeralOwner`:临时节点和持久节点之间的区别

`dataLength`:数据长度

`numChildren`:子节点数量

#### Session的基本原理

* 客户端与服务端之间的连接存在会话

* 每个会话都可以设置一个超时时间
* 心跳结束,session则过期
* session过期,则临时节点 znode 会被抛弃
* 心跳机制:客户端向服务端发送 ping包请求

#### zk常用命令行操作2

* create命令

  > create [-s] [-e] path data acl  
  >
  > -s: 创建顺序节点
  >
  > -e: 创建临时节点
  >
  > path: 在哪里创建
  >
  > data: 给这个创建的节点添加的数据
  >
  > acl: 创建节点的权限

  键入: `create /myzk myzk-data`

  ![image.png](https://upload-images.jianshu.io/upload_images/15181329-9db475bb5de0c6d1.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

可以看到`cversion`和`dataVersion`都是 0;

**创建一个临时节点:**

键入:`create -e /myzk/tmp myzk-data`

![image.png](https://upload-images.jianshu.io/upload_images/15181329-302d67cc18acf419.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

子节点的版本号由 0 变为 1;

![image.png](https://upload-images.jianshu.io/upload_images/15181329-3aa4258b36a8ab81.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

这个 tmp目录是之前我们创建的临时节点,可以看到`ephemeralOwner`这个的值是`0x100019e2b730001`而不是持久节点的`0x0`,这就是持久节点和临时节点的区别;

删除临时节点: `ctrl+c`断开连接;,再次进入,再次查看`ls /myzk`可能还会有,因为是有心跳检测的(时间差),等待一会再次查看tmp节点就会消失;

**创建顺序节点:**

键入: `create -s /myzk/sec seq`

![image.png](https://upload-images.jianshu.io/upload_images/15181329-5240a6af1d4c5c77.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

逐渐累加的;sec0000000001,sec0000000002,sec0000000003 …...这就是创建了临时节点;

* set命令

  > set path data [version]

![image.png](https://upload-images.jianshu.io/upload_images/15181329-72bd7358afe7c584.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

先看下 myzk 节点下数据的值为 myzk-data,dataversion的是 0;cversion是 4,因为上面我们创建了1 个临时节点和3 个顺序节点;

键入: `set /myzk new-data` 然后我在 `get /myzk`

![image.png](https://upload-images.jianshu.io/upload_images/15181329-5011a96c165003ae.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

可以看到当前节点的数据更新为 new-data ; dataVersion 的值为 1(乐观锁);

在高并发的情况下,有很多的人对这个节点进行设置(也就是 set),例如:`set /myzk 123`,在大并发的情况下这个值(dataVersion)一直是累加的,然后直接的覆盖原来的值;如果按照顺序来设置的话就要在后面加上一个版本号`set /myzk 123 1`

![image.png](https://upload-images.jianshu.io/upload_images/15181329-cbd5b71f6dee7d42.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

现在 dataVersion的值变为了 2;如果说我旁边还有别的用户也进行了这种操作,他获取的时候版本号也是之前没有修改的版本号也是 1;那么现在他的实际版号已经由 1 变成了 2;那么这个用户继续操作的话就会报一个错;

![image.png](https://upload-images.jianshu.io/upload_images/15181329-1702a64dafcf3c7a.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

`version No is not valid : /myzk` 

我们必须使用最新的版本号才能进行更新,这也是乐观锁最常用的一种方式;