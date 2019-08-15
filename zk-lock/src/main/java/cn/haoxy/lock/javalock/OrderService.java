package cn.haoxy.lock.javalock;

import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by haoxiaoyong on 2019/8/14 下午 10:00
 * e-mail: hxyHelloWorld@163.com
 * github:https://github.com/haoxiaoyong1014
 * Blog: www.haoxiaoyong.cn
 */
//使用多线程模拟生成订单号
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
