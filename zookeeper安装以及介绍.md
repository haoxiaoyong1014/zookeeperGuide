###																zookeeper安装以及介绍

#### 概述

zookeeper实际上是yahoo开发的，用于分布式中一致性处理的框架。最初其作为研发Hadoop时的副产品。由于分布式系统中一致性处理较为困难，其他的分布式系统没有必要 费劲重复造轮子，故随后的分布式系统中大量应用了zookeeper，以至于zookeeper成为了各种分布式系统的基础组件，其地位之重要，可想而知。著名的hadoop，kafka，dubbo 都是基于zookeeper而构建。

#### zookeeper安装

	*  **下载**

> 稳定版：
> http://mirror.bit.edu.cn/apache/zookeeper/stable/

* 提取tar文件

  ```
  cd /opt/
  tar -zxf  zookeeper-3.4.12.tar.gz
  cd zookeeper-3.4.12
  ```

   * 创建data文件夹 用于存储数据文件

     `mkdir data `

   * 创建logs文件夹 用于存储日志

     `mkdir logs `

* **创建配置文件**

  使用命令 vi conf/zoo.cfg 创建配置文件并打开，ps (其实目录conf 下有默认的配置文件，但是注释太多，英文一大堆，太乱,所有还不如自己新建一个配置文件,复制一份zoo_sample.cfg并命名为zoo.cfg)

  * 输入命令：

    `vi /opt/zookeeper-3.4.12/conf/zoo.cfg`

  * 编辑填写内容如下:

    ```
    tickTime = 2000
    dataDir = /opt/zookeeper-3.4.12/data
    dataLogDir = /opt/zookeeper-3.4.12/logs
    tickTime = 2000
    clientPort = 2181
    initLimit = 5
    syncLimit = 2
    ```

#### ZooKeeper操作

 * **启动服务**

   * `/opt/zookeeper-3.4.12/bin/zkServer.sh start`

   * 响应

     ```
     ZooKeeper JMX enabled by default
     Using config: /opt/zookeeper-3.4.12/bin/../conf/zoo.cfg
     Starting zookeeper ... STARTED
     ```

     

 * **连接服务**

   * `/opt/zookeeper-3.4.12/bin/zkCli.sh`

   * 响应

     ```
     Connecting to localhost:2181
     2017-08-22 16:43:05,954 [myid:] - INFO  [main:Environment@100] - Client environment:zookeeper.version=3.4.12-1757313, built on 08/23/2016 06:50 GMT
     2017-08-22 16:43:05,958 [myid:] - INFO  [main:Environment@100] - Client environment:host.name=node1
     2017-08-22 16:43:05,958 [myid:] - INFO  [main:Environment@100] - Client environment:java.version=1.8.0_144
     2017-08-22 16:43:05,967 [myid:] - INFO  [main:Environment@100] - Client environment:java.vendor=Oracle Corporation
     2017-08-22 16:43:05,967 [myid:] - INFO  [main:Environment@100] - Client environment:java.home=/usr/lib/jvm/jre
     2017-08-22 16:43:05,967 [myid:] - INFO  [main:Environment@100] - Client environment:java.class.path=/opt/zookeeper-3.4.12/bin/../build/classes:/opt/zookeeper-3.4.12/bin/../build/lib/*.jar:/opt/zookeeper-3.4.12/bin/../lib/slf4j-log4j12-1.6.1.jar:/opt/zookeeper-3.4.12/bin/../lib/slf4j-api-1.6.1.jar:/opt/zookeeper-3.4.12/bin/../lib/netty-3.10.5.Final.jar:/opt/zookeeper-3.4.12/bin/../lib/log4j-1.2.16.jar:/opt/zookeeper-3.4.12/bin/../lib/jline-0.9.94.jar:/opt/zookeeper-3.4.12/bin/../zookeeper-3.4.12.jar:/opt/zookeeper-3.4.12/bin/../src/java/lib/*.jar:/opt/zookeeper-3.4.12/bin/../conf:.:/lib/jvm/lib:/lib/jvm/jre/lib
     2017-08-22 16:43:05,967 [myid:] - INFO  [main:Environment@100] - Client environment:java.library.path=/usr/java/packages/lib/amd64:/usr/lib64:/lib64:/lib:/usr/lib
     2017-08-22 16:43:05,967 [myid:] - INFO  [main:Environment@100] - Client environment:java.io.tmpdir=/tmp
     2017-08-22 16:43:05,967 [myid:] - INFO  [main:Environment@100] - Client environment:java.compiler=<NA>
     2017-08-22 16:43:05,967 [myid:] - INFO  [main:Environment@100] - Client environment:os.name=Linux
     2017-08-22 16:43:05,967 [myid:] - INFO  [main:Environment@100] - Client environment:os.arch=amd64
     2017-08-22 16:43:05,967 [myid:] - INFO  [main:Environment@100] - Client environment:os.version=3.10.0-514.26.2.el7.x86_64
     2017-08-22 16:43:05,968 [myid:] - INFO  [main:Environment@100] - Client environment:user.name=root
     2017-08-22 16:43:05,968 [myid:] - INFO  [main:Environment@100] - Client environment:user.home=/root
     2017-08-22 16:43:05,968 [myid:] - INFO  [main:Environment@100] - Client environment:user.dir=/opt/zookeeper-3.4.12
     2017-08-22 16:43:05,969 [myid:] - INFO  [main:ZooKeeper@438] - Initiating client connection, connectString=localhost:2181 sessionTimeout=30000 watcher=org.apache.zookeeper.ZooKeeperMain$MyWatcher@506c589e
     Welcome to ZooKeeper!
     2017-08-22 16:43:06,011 [myid:] - INFO  [main-SendThread(localhost:2181):ClientCnxn$SendThread@1032] - Opening socket connection to server localhost/0:0:0:0:0:0:0:1:2181. Will not attempt to authenticate using SASL (unknown error)
     JLine support is enabled
     2017-08-22 16:43:06,164 [myid:] - INFO  [main-SendThread(localhost:2181):ClientCnxn$SendThread@876] - Socket connection established to localhost/0:0:0:0:0:0:0:1:2181, initiating session
     2017-08-22 16:43:06,237 [myid:] - INFO  [main-SendThread(localhost:2181):ClientCnxn$SendThread@1299] - Session establishment complete on server localhost/0:0:0:0:0:0:0:1:2181, sessionid = 0x15e091bf2020000, negotiated timeout = 30000
     
     WATCHER::
     
     WatchedEvent state:SyncConnected type:None path:null
     
     [zk: localhost:2181(CONNECTED) 0] 
     
     ```

 * **服务状态**

   * `/opt/zookeeper-3.4.12/bin/zkServer.sh status`

   * 响应

     ```
     ZooKeeper JMX enabled by default
     Using config: /opt/zookeeper-3.4.12/bin/../conf/zoo.cfg
     Mode: standalone
     ```

     

 * **停止服务**

   * `/opt/zookeeper-3.4.12/bin/zkServer.sh stop`

   * 响应

     ```
     bin/zkServer.sh stop
     ZooKeeper JMX enabled by default
     Using config: /opt/zookeeper-3.4.12/bin/../conf/zoo.cfg
     Stopping zookeeper ... STOPPED
     ```

至此 zookeeper 的安装就结束了;

#### zookeeper镜像安装

docker镜像安装参考:https://segmentfault.com/a/1190000006907443

#### zookeeper集群安装

zookeeper集群安装参见:https://segmentfault.com/a/1190000010807875

#### zookeeper配置文件描述

* tickTime

  > tickTime则是上述两个超时配置的基本单位，例如对于initLimit，其配置值为5，说明其超时时间为 2000ms * 5 = 10秒。

* dataDir

  > 其配置的含义跟单机模式下的含义类似，不同的是集群模式下还有一个myid文件。myid文件的内容只有一行，且内容只能为1 - 255之间的数字，这个数字亦即上面介绍server.id中的id，表示zk进程的id。

* dataLogDir

  > 如果没提供的话使用的则是dataDir。zookeeper的持久化都存储在这两个目录里。dataLogDir里是放到的顺序日志(WAL)。而dataDir里放的是内存数据结构的snapshot，便于快速恢复。为了达到性能最大化，一般建议把dataDir和dataLogDir分到不同的磁盘上，这样就可以充分利用磁盘顺序写的特性。

* initLimit

  > ZooKeeper集群模式下包含多个zk进程，其中一个进程为leader，余下的进程为follower,当follower最初与leader建立连接时，它们之间会传输相当多的数据，尤其是follower的数据落后leader很多。initLimit配置follower与leader之间建立连接后进行同步的最长时间

* syncLimit

  > 配置follower和leader之间发送消息，请求和应答的最大时间长度。

#### zookeeper文件夹主要目录介绍

* bin

  > 进入 bin目录会看到,zkCleanup.sh,zkCli.cmd,zkCli.sh,zkServer.cmd,zkServer.sh等等.主要的一些运行命令,其中 cmd结尾是在 windows上运行的命令,sh是在 linux/Mac上运行的命令

* conf

  > 主要是存放配置文件,进入 conf目录会看到, configuration.xsl,log4j.properties和 zoo_sample.cfg文件,其中两个不做介绍,这里只关注zoo_sample.cfg,前面提到的将zoo_sample.cfg复制并重命名为zoo.cfg;

* contrib

  > 附加的一些功能

* dist-maven

  > mvn 编译后的目录,pom文件,jar包等

* docs

  > 存放文档的地方,index.html,或者 index.pdf

* lib

  > 需要依赖的 jar包

* recipes

  > 存放一些案例

* src

  > 存放源码的地方



