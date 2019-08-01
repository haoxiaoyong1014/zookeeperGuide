#### Apache Curator客户端的使用



* zk原生 api的不足之处
  * 超时重连,不支持自动,需要手动操作
  * watch注册一次会失效
  * 不支持递归创建节点

* apache curator 提供更多解决方案并且实现简单:比如 分布式锁

* apache curator 提供常用的 zookeeper工具类

  

##### [搭建 maven 工程,建立 curator与 zkserver的连接](https://github.com/haoxiaoyong1014/zookeeperGuide/blob/master/zk-curator-api/src/main/java/cn/haoxy/curator/api/operator/CuratorConntion.java)

##### [zk命名空间以及创建节点](https://github.com/haoxiaoyong1014/zookeeperGuide/blob/master/zk-curator-api/src/main/java/cn/haoxy/curator/api/operator/CuratorCreateNode.java)

##### [修改节点](https://github.com/haoxiaoyong1014/zookeeperGuide/blob/master/zk-curator-api/src/main/java/cn/haoxy/curator/api/operator/CuratorUpdateNode.java)

##### [删除节点](https://github.com/haoxiaoyong1014/zookeeperGuide/blob/master/zk-curator-api/src/main/java/cn/haoxy/curator/api/operator/CuratorDeleteNode.java)

##### [查询节点相关信息](https://github.com/haoxiaoyong1014/zookeeperGuide/blob/master/zk-curator-api/src/main/java/cn/haoxy/curator/api/operator/CuratorSelectDataNode.java)

##### [curator之usingWatcher](https://github.com/haoxiaoyong1014/zookeeperGuide/blob/master/zk-curator-api/src/main/java/cn/haoxy/curator/api/watcher/WacthOperator.java)

##### [curator之nodeCache一次注册N次监听](https://github.com/haoxiaoyong1014/zookeeperGuide/blob/master/zk-curator-api/src/main/java/cn/haoxy/curator/api/watcher/WacthOperator.java)

##### [curator之PathChildrenCache子节点监听](https://github.com/haoxiaoyong1014/zookeeperGuide/blob/master/zk-curator-api/src/main/java/cn/haoxy/curator/api/watcher/EnhanceWacthOperator.java)







