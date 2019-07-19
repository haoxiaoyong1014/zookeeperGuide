package cn.haoxy.curator.api.operator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.apache.curator.retry.RetryNTimes;

import java.util.concurrent.TimeUnit;

import static org.apache.curator.framework.imps.CuratorFrameworkState.STARTED;

/**
 * @author Haoxy
 * Created in 2019-07-19.
 * E-mail:hxyHelloWorld@163.com
 * github:https://github.com/haoxiaoyong1014
 * 删除zk节点
 */
public class CuratorDeleteNode {

    public CuratorFramework client = null;

    public static final String zkServerPath = "127.0.0.1:2181";

    /**
     * 实例化 zk客户端
     */
    public CuratorDeleteNode() {

        /**
         * 使用方式二
         * curator 连接 zookeeper 的策略:RetryNTimes
         * n:重试次数
         * sleepMsBetweenRetries 每次重试间隔时间
         */
        RetryPolicy retryPolicy1 = new RetryNTimes(3, 5000);


        client = CuratorFrameworkFactory.builder().connectString(zkServerPath)
                .sessionTimeoutMs(10000)
                .retryPolicy(retryPolicy1)
                //这里添加了一个namespace,类似于一个工作站,所有操作都在这个站中,操作成功之后会在根节点创建一个workspace节点
                .namespace("workspace")
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

    public static void main(String[] args) throws Exception {
        /**
         * STARTED    已启动
         * STOPPED    已停止
         */
        //实例化
        CuratorDeleteNode curatorConntion = new CuratorDeleteNode();
        CuratorFrameworkState state = curatorConntion.client.getState();
        System.out.println("当前客户的状态：" + (state == STARTED ? "已连接" : "已关闭"));

        // 删除节点
        String nodePath = "/super/imooc";
        curatorConntion.client.delete()
                // 如果删除失败，那么在后端还是继续会删除，直到成功
                .guaranteed()
                // 如果有子节点，就删除,如果 imooc下还有其他子节点都会删除例如: /super/imooc/a/b/c/d;只会删除imooc下子节点,imooc的父节点super还是会在的;
                //如果把 super删除workspace还是会存在的;
                .deletingChildrenIfNeeded()
                .withVersion(1)
                .forPath(nodePath);

        System.out.println("删除节点成功....");
        TimeUnit.SECONDS.sleep(3);
        curatorConntion.closeZKClient();
        CuratorFrameworkState state2 = curatorConntion.client.getState();
        System.out.println("当前客户的状态：" + (state2 == STARTED ? "已连接" : "已关闭"));

    }
}
