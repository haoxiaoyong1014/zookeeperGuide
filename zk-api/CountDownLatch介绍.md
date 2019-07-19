#### CountDownLacth 的使用

##### CountDownLacth介绍:

```
他经常用于监听某些初始化操作,等初始化执行完成后,通知主线程继续工作
```

##### 使用场景:

![image.png](https://upload-images.jianshu.io/upload_images/15181329-cb551408fd338c87.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

##### 使用实例

```java
public class UseCountDownLatch {

    public static void main(String[] args) {
        CountDownLatch countDownLatch = new CountDownLatch(2);
        Thread t1 = new Thread(() -> {
            try {
                System.out.println("进入线程t1" + "等待其他线程处理完成...");
                countDownLatch.await();
                System.out.println("t1线程继续执行...");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "t1");
        Thread t2 = new Thread(() -> {
            try {
                System.out.println("t2线程进行初始化操作...");
                TimeUnit.SECONDS.sleep(3);
                System.out.println("t2线程初始化完毕,通知t1线程继续");
                countDownLatch.countDown(); //类似通知
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },"t2");
        Thread t3 = new Thread(() -> {
            try {
                System.out.println("t3线程进行初始化操作...");
                TimeUnit.SECONDS.sleep(4);
                System.out.println("t3线程初始化完毕,通知t1线程继续");
                countDownLatch.countDown();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },"t3");
        t1.start();
        t2.start();
        t3.start();
    }
}
```

当前执行结果:

```
进入线程t1等待其他线程处理完成...
t2线程进行初始化操作...
t3线程进行初始化操作...
t2线程初始化完毕,通知t1线程继续
t3线程初始化完毕,通知t1线程继续
t1线程继续执行...
```

分析:

一开始t1进入线程,遇到`countDownLatch.await();`阻塞到当前的地方,随后t2和t3进入线程,当线程t2和t3都初始化完毕之后t1才会被唤醒.如果我们把t3注释掉或者把CountDownLatch设置为4`CountDownLatch countDownLatch = new CountDownLatch(4);`

参看执行结果:

```
进入线程t1等待其他线程处理完成...
t2线程进行初始化操作...
t2线程初始化完毕,通知t1线程继续
```

t1线程将永远不会执行

