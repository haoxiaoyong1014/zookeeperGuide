package cn.haoxy.curator.api.operator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.apache.curator.retry.*;

import java.util.concurrent.TimeUnit;

import static org.apache.curator.framework.imps.CuratorFrameworkState.LATENT;
import static org.apache.curator.framework.imps.CuratorFrameworkState.STARTED;

/**
 * @author Haoxy
 * Created in 2019-07-19.
 * E-mail:hxyHelloWorld@163.com
 * github:https://github.com/haoxiaoyong1014
 * 连接 zk
 */
public class CuratorConntion {

    public CuratorFramework client = null;

    public static final String zkServerPath = "127.0.0.1:2181";

    /**
     * 实例化 zk客户端
     */
    public CuratorConntion() {
        //创建zk有以下几种方式

        /**
         * 方式一:推荐使用
         * curator 连接 zookeeper的策略ExponentialBackoffRetry
         * 参数介绍:
         * int baseSleepTimeMs, int maxRetries, int maxSleepMs
         * baseSleepTimeMs:初始化 sleep的时间
         * maxRetries:最大重试次数
         * maxSleepMs:最大重试时间
         */
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 5);

        /**
         * 方式二:推荐使用
         * curator 连接 zookeeper 的策略:RetryNTimes
         * n:重试次数
         * sleepMsBetweenRetries 每次重试间隔时间
         */
        RetryPolicy retryPolicy1 = new RetryNTimes(3, 5000);

        /**
         * 方式三: 不推荐使用
         * curator链接zookeeper的策略:RetryOneTime
         * sleepMsBetweenRetry:每次重试间隔的时间,
         * 只会重置一次,第一次失败隔 3 秒之后重置一次
         */
        RetryPolicy retryPolicy2 = new RetryOneTime(3000);

        /**
         * 永远重试，不推荐使用
         */
        //RetryPolicy retryPolicy3 = new RetryForever(retryIntervalMs);

        /**
         * curator连接 zookeeper的策略:RetryUntilElapsed
         * maxElapsedTimeMs:最大重试时间
         * sleepMsBetweenRetries:每次重试间隔
         * 重试时间超过 maxElapsedTimeMs 后,就不再重试
         */
        RetryPolicy retryPolicy4 = new RetryUntilElapsed(2000, 3000);


        client = CuratorFrameworkFactory.builder().connectString(zkServerPath)
                .sessionTimeoutMs(10000)
                .retryPolicy(retryPolicy1)
                .build();

        client.start();
    }

    /**
     * @Description 关闭 zk客户端连接
     */
    public void closeZKClient() {
        if (client != null) {
            this.client.close();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        /**
         * STARTED    已启动
         * STOPPED    已停止
         */
        //实例化
        CuratorConntion curatorConntion = new CuratorConntion();
        CuratorFrameworkState state = curatorConntion.client.getState();
        System.out.println("当前客户的状态：" + (state == STARTED ? "已连接" : "已关闭"));
        TimeUnit.SECONDS.sleep(3);
        curatorConntion.closeZKClient();
        CuratorFrameworkState state2 = curatorConntion.client.getState();
        System.out.println("当前客户的状态：" + (state2 == STARTED ? "已连接" : "已关闭"));
    }
}
