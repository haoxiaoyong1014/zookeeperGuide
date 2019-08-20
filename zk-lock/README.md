### zookeeper实现分布式锁
 
#### 什么多线程

多线程为了能够提高应用程序的运行效率，在一个进程中有多条不同的执行路径，同时并行执行，互不影响。

这里关于线程的介绍就不多阐述，想了解更多关于线程的介绍请移步 https://github.com/haoxiaoyong1014/recording

下面我们只针对分布式环境下实现分布式锁介绍;

#### 什么是java内存模型 
共享内存模型指的就是Java内存模型(简称JMM)，JMM决定一个线程对共享变量的写入时,能对另一个线程可见。从抽象的角度来看，JMM定义了线程和主内存之间的抽象关系：线程之间的共享变量存储在主内存（main memory）中，每个线程都有一个私有的本地内存（local memory），本地内存中存储了该线程以读/写共享变量的副本。本地内存是JMM的一个抽象概念，并不真实存在。它涵盖了缓存，写缓冲区，寄存器以及其他的硬件和编译器优化

![image.png](https://upload-images.jianshu.io/upload_images/15181329-7893f8ab9de62c8f.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

从上图来看，线程A与线程B之间如要通信的话，必须要经历下面2个步骤：

1. 首先，线程A把本地内存A中更新过的共享变量刷新到主内存中去。

2. 然后，线程B到主内存中去读取线程A之前已更新过的共享变量。 

![image.png](https://upload-images.jianshu.io/upload_images/15181329-4b54d9fdc509cbe9.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

如上图所示，本地内存A和B有主内存中共享变量x的副本。假设初始时，这三个内存中的x值都为0。线程A在执行时，把更新后的x值（假设值为1）临时存放在自己的本地内存A中。当线程A和线程B需要通信时，线程A首先会把自己本地内存中修改后的x值刷新到主内存中，此时主内存中的x值变为了1。随后，线程B到主内存中去读取线程A更新后的x值，此时线程B的本地内存的x值也变为了1。
从整体来看，这两个步骤实质上是线程A在向线程B发送消息，而且这个通信过程必须要经过主内存。JMM通过控制主内存与每个线程的本地内存之间的交互，来为java程序提供内存可见性保证。

总结：什么是Java内存模型：java内存模型简称jmm，定义了一个线程对另一个线程可见。共享变量存放在主内存中，每个线程都有自己的本地内存，当多个线程同时访问一个数据的时候，可能本地内存没有及时刷新到主内存，所以就会发生线程安全问题。

#### 传统方式生成订单号ID

生成订单类
```java
public class OrderNumGenerator {

    //全局订单id;
    public static int count = 0;

    public String getNumber() {

        try {
            //TimeUnit.SECONDS.sleep(2);
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        SimpleDateFormat simpt = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        return simpt.format(new Date()) + "-" + ++count;
    }
}
```

使用多线程情况模拟生成订单号
```java
public class OrderService implements Runnable {

    private OrderNumGenerator orderNumGenerator = new OrderNumGenerator();

    public void run() {
        getNumber();
    }

    public void getNumber() {
        String number = orderNumGenerator.getNumber();
        System.out.println(Thread.currentThread().getName() + ",生成订单ID:" + number);
    }

    public static void main(String[] args) {
        System.out.println("####生成唯一订单号###");
        for (int i = 0; i < 100; i++) {
            new Thread(new OrderService()).start();
        }

    }
}
```

这时候会出现线程安全问题；

![image.png](https://upload-images.jianshu.io/upload_images/15181329-b5a5aaf6748b4617.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

下面解决这种线程安全问题的方式有很多

例如:使用synchronized或者lock锁

这里对synchronized就不做过的说明了。想了解更多关于synchronized的语义及使用请移步 https://github.com/haoxiaoyong1014/recording

使用lock锁解决线程安全问题:

生成订单类
```java
public class OrderNumGenerator {

    //全局订单id;
    public static int count = 0;

    public String getNumber() {

        try {
            //TimeUnit.SECONDS.sleep(2);
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        SimpleDateFormat simpt = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        return simpt.format(new Date()) + "-" + ++count;
    }
}

```
没有做任何的改变；

使用多线程情况模拟生成订单号(lock锁):

```java
public class OrderService implements Runnable {

    private OrderNumGenerator orderNumGenerator = new OrderNumGenerator();

    // 使用lock锁
    private java.util.concurrent.locks.Lock lock = new ReentrantLock();

    public void run() {
        getNumber();
    }

    public void getNumber() {
        try {
            lock.lock();
            String number = orderNumGenerator.getNumber();
            System.out.println(Thread.currentThread().getName() + ",生成订单ID:" + number);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) {
        System.out.println("####生成唯一订单号###");
        OrderService orderService = new OrderService();
        for (int i = 0; i < 100; i++) {
            new Thread(orderService).start();
        }

    }
}

```
对比和之前和那些改变？

`lock.lock();`上锁，

`lock.unlock();`释放锁

同时我们要注意main方法中的OrderService对象；这里只实例化一次；

![image.png](https://upload-images.jianshu.io/upload_images/15181329-88d07d8d22a96461.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

很完美的打印到100;

#### 下面介绍在分布式环境下生成订单ID;

在分布式(集群)环境下，每台JVM不能实现同步，在分布式场景下使用时间戳生成订单号可能会重复

#### 使用分布式锁生成订单号技术

1.使用数据库实现分布式锁
缺点:性能差、线程出现异常时，容易出现死锁
2.使用redis实现分布式锁
缺点:锁的失效时间难控制、容易产生死锁、非阻塞式、不可重入
3.使用zookeeper实现分布式锁
实现相对简单、可靠性强、使用临时节点，失效时间容易控制

#### 什么是分布式锁

分布式锁一般用在分布式系统或者多个应用中，用来控制同一任务是否执行或者任务的执行顺序。在项目中，部署了多个tomcat应用，在执行定时任务时就会遇到同一任务可能执行多次的情况，我们可以借助分布式锁，保证在同一时间只有一个tomcat应用执行了定时任务

#### 使用Zookeeper实现分布式锁

**Zookeeper实现分布式锁原理**

使用zookeeper创建临时序列节点来实现分布式锁，适用于顺序执行的程序，大体思路就是创建临时序列节点，找出最小的序列节点，获取分布式锁，程序执行完成之后此序列节点消失，通过watch来监控节点的变化，从剩下的节点的找到最小的序列节点，获取分布式锁，执行相应处理，依次类推……

添加依赖
```xml
<dependency>
	<groupId>com.101tec</groupId>
	<artifactId>zkclient</artifactId>
	<version>0.10</version>
</dependency>
```

**创建Lock接口**

```java
public interface Lock {

    //获取到锁的资源
    void getLock();
    // 释放锁
    void unLock();

}
```

**创建ZookeeperAbstractLock抽象类**

```java
public abstract class ZookeeperAbstractLock implements Lock {

    // zk连接地址
    private static final String CONNECTSTRING = "127.0.0.1:2181";

    // 创建zk连接
    protected ZkClient zkClient = new ZkClient(CONNECTSTRING);

    protected static final String PATH = "/lock";

    public void getLock(){
        if(tryLock()){
            System.out.println("##获取lock锁的资源####");
        }else {
            //等待
            waitLock();
            //重新获取资源
            getLock();
        }
    }

    //获取锁资源
    abstract boolean tryLock();

    //等待
    abstract void waitLock();

    public void unLock() {
        if (zkClient != null) {
            zkClient.close();
            System.out.println("释放锁资源...");
        }
    }
}
```
**ZookeeperDistrbuteLock类**

```java
public class ZookeeperDistrbuteLock extends ZookeeperAbstractLock {

    private CountDownLatch countDownLatch = null;

    boolean tryLock() {

        try {
            zkClient.createEphemeral(PATH);
            return true;
        } catch (Exception e) {
//			e.printStackTrace();
            return false;
        }
    }

    void waitLock() {
        IZkDataListener izkDataListener = new IZkDataListener() {

            public void handleDataDeleted(String path) throws Exception {
                // 唤醒被等待的线程
                if (countDownLatch != null) {
                    countDownLatch.countDown();
                }
            }

            public void handleDataChange(String path, Object data) throws Exception {

            }
        };
        // 注册事件
        zkClient.subscribeDataChanges(PATH, izkDataListener);
        if (zkClient.exists(PATH)) {
            countDownLatch = new CountDownLatch(1);
            try {
                countDownLatch.await();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // 删除监听
        zkClient.unsubscribeDataChanges(PATH, izkDataListener);
    }

}
```

**使用Zookeeper锁运行效果**

```java
public class OrderService implements Runnable {
    
	private OrderNumGenerator orderNumGenerator = new OrderNumGenerator();
	// 使用lock锁
	// private java.util.concurrent.locks.Lock lock = new ReentrantLock();
	private Lock lock = new ZookeeperDistrbuteLock();
	public void run() {
		getNumber();
	}
	public void getNumber() {
		try {
			lock.getLock();
			String number = orderNumGenerator.getNumber();
			System.out.println(Thread.currentThread().getName() + ",生成订单ID:" + number);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			lock.unLock();
		}
	}
	public static void main(String[] args) {
		System.out.println("####生成唯一订单号###");
//		OrderService orderService = new OrderService();
		for (int i = 0; i < 100; i++) {
			new Thread( new OrderService()).start();
		}
	}
}
```

执行main方法：

```
##获取lock锁的资源####
Thread-1,生成订单ID:2019-08-19-22-32-50-1
释放锁资源...
##获取lock锁的资源####
Thread-3,生成订单ID:2019-08-19-22-32-59-2
释放锁资源...
##获取lock锁的资源####
Thread-5,生成订单ID:2019-08-19-22-33-08-3
释放锁资源...
##获取lock锁的资源####
Thread-7,生成订单ID:2019-08-19-22-33-17-4
释放锁资源...
##获取lock锁的资源####
Thread-9,生成订单ID:2019-08-19-22-33-26-5
释放锁资源...
##获取lock锁的资源####
Thread-11,生成订单ID:2019-08-19-22-33-35-6
释放锁资源...
##获取lock锁的资源####
Thread-13,生成订单ID:2019-08-19-22-33-44-7
释放锁资源...
##获取lock锁的资源####
Thread-15,生成订单ID:2019-08-19-22-33-53-8
释放锁资源...
##获取lock锁的资源####
Thread-17,生成订单ID:2019-08-19-22-34-02-9
释放锁资源...
##获取lock锁的资源####
Thread-19,生成订单ID:2019-08-19-22-34-11-10
释放锁资源...
##获取lock锁的资源####

```
