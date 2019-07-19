#### Apache Curator客户端的使用



* zk原生 api的不足之处
  * 超时重连,不支持自动,需要手动操作
  * watch注册一次会失效
  * 不支持递归创建节点

* apache curator 提供更多解决方案并且实现简单:比如 分布式锁

* apache curator 提供常用的 zookeeper工具类

  

##### 搭建 maven 工程,建立 curator与 zkserver的连接























