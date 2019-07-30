package cn.haoxy.curator.api.watcher;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.RetryNTimes;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.apache.curator.framework.imps.CuratorFrameworkState.STARTED;

/**
 * @author Haoxy
 * Created in 2019-07-19.
 * E-mail:hxyHelloWorld@163.com
 * github:https://github.com/haoxiaoyong1014
 * 增强 wacth
 */
public class EnhanceWacthOperator {

    public CuratorFramework client = null;

    public static final String zkServerPath = "127.0.0.1:2181";

    /**
     * 实例化 zk客户端
     */
    public EnhanceWacthOperator() {

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
        EnhanceWacthOperator curatorConntion = new EnhanceWacthOperator();
        CuratorFrameworkState state = curatorConntion.client.getState();
        System.out.println("当前客户的状态：" + (state == STARTED ? "已连接" : "已关闭"));

        final PathChildrenCache childrenCache = new PathChildrenCache(curatorConntion.client, nodePath, true);
        /**
         * StartMode:初始化方式
         * POST_INITIALIZED_EVENT:异步初始化,初始化之后会触发事件
         * NORMAL:异步初始化
         * BUILD_INITAL_CACHE:同步初始化
         */
        childrenCache.start(PathChildrenCache.StartMode.POST_INITIALIZED_EVENT);
        List<ChildData> childDataList = childrenCache.getCurrentData();
        System.out.println("当前数据节点的子节点数据列表: ");
        for (ChildData cd : childDataList) {
            String childData = new String(cd.getData());
            System.out.println(childData);
        }
        childrenCache.getListenable().addListener(new PathChildrenCacheListener() {
            public void childEvent(CuratorFramework curatorFramework, PathChildrenCacheEvent event) throws Exception {
                if (event.getType().equals(PathChildrenCacheEvent.Type.INITIALIZED)) {
                    System.out.println("子节点初始化 ok....");
                } else if (event.getType().equals(PathChildrenCacheEvent.Type.CHILD_ADDED)) {
                    String path = event.getData().getPath();
                    System.out.println("添加子节点:" + path);
                    System.out.println("子节点数据为:" + new String(event.getData().getData()));
                } else if (event.getType().equals(PathChildrenCacheEvent.Type.CHILD_REMOVED)) {
                    System.out.println("删除子节点:" + event.getData().getPath());
                } else if (event.getType().equals(PathChildrenCacheEvent.Type.CHILD_UPDATED)) {
                    System.out.println("修改子节点路径:" + event.getData().getPath());
                    System.out.println("修改子节点数据:" + new String(event.getData().getData()));
                }
            }
        });


        TimeUnit.MINUTES.sleep(2);
        curatorConntion.closeZKClient();
        CuratorFrameworkState state2 = curatorConntion.client.getState();
        System.out.println("当前客户的状态：" + (state2 == STARTED ? "已连接" : "已关闭"));

    }
}
