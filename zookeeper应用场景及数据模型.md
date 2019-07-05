#### zookeeper基本数据类型

* zookeeper是一个树形结构,类似于前端开发中的 tree.js 组件;

  ![image.png](https://upload-images.jianshu.io/upload_images/15181329-4e9ed23f9c1ee2fc.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)



* zk的数据模型也可以理解为 linux/unix 的文件目录:/usr/local

* 每一个节点都称之为 znode,它可以有子节点,也可以有数据

  > 子节点: 就是父目录下的一个子目录,在 zk中称之为节点,每一个节点中都有一些相应的数据,就像目录下有一些文件数据一样

* 每个节点分为临时节点和永久节点,临时节点在客户端断开后消失

  > 

https://www.jianshu.com/p/2e970fe35c3f>

