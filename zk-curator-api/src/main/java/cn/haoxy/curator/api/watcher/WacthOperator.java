package cn.haoxy.curator.api.watcher;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.curator.retry.RetryNTimes;

import java.util.concurrent.TimeUnit;

import static org.apache.curator.framework.imps.CuratorFrameworkState.STARTED;

/**
 * @author Haoxy
 * Created in 2019-07-19.
 * E-mail:hxyHelloWorld@163.com
 * github:https://github.com/haoxiaoyong1014
 * wacth 事件监听
 */
public class WacthOperator {

    public CuratorFramework client = null;

    public static final String zkServerPath = "127.0.0.1:2181";

    /**
     * 实例化 zk客户端
     */
    public WacthOperator() {

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
        final String nodePath = "/super/imooc";
        WacthOperator curatorConntion = new WacthOperator();
        CuratorFrameworkState state = curatorConntion.client.getState();
        System.out.println("当前客户的状态：" + (state == STARTED ? "已连接" : "已关闭"));
        //watcher事件,当使用 usingWatcher的时候,监听只会触发一次,监听完毕后就销毁
        /*************监听只会触发一次***************/
        //curatorConntion.client.getData().usingWatcher(new MyCuratorWatcher()).forPath(nodePath);


        /*************一次监听N次触发***************/
        //NodeCache:监听数据节点的变更,会触发事件
        final NodeCache nodeCache = new NodeCache(curatorConntion.client, nodePath);
        //buildInitial:初始化的时候获取node的值并且缓存,默认false,不缓存
        nodeCache.start(true);
        if (nodeCache.getCurrentData() != null) {
            System.out.println("节点初始化数据为:" + new String(nodeCache.getCurrentData().getData()));
        } else {
            System.out.println("节点初始化数据为空....");
        }
        nodeCache.getListenable().addListener(new NodeCacheListener() {
            public void nodeChanged() throws Exception {
                if (nodeCache.getCurrentData() == null) {
                    System.out.println("空节点");
                    return;
                }
                String data = new String(nodeCache.getCurrentData().getData());
                System.out.println("节点路径:" + nodeCache.getCurrentData().getPath() + "数据: " + data);
            }
        });


        TimeUnit.MINUTES.sleep(2);
        curatorConntion.closeZKClient();
        CuratorFrameworkState state2 = curatorConntion.client.getState();
        System.out.println("当前客户的状态：" + (state2 == STARTED ? "已连接" : "已关闭"));

    }
}
