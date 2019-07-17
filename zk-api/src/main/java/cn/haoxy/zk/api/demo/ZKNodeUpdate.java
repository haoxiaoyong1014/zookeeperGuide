package cn.haoxy.zk.api.demo;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;

/**
 * @author Haoxy
 * Created in 2019-07-17.
 * E-mail:hxyHelloWorld@163.com
 * github:https://github.com/haoxiaoyong1014
 * 修改节点数据
 */
public class ZKNodeUpdate implements Watcher {

    private ZooKeeper zooKeeper = null;

    public static final String zkServerPath = "127.0.0.1:2181";

    public static final Integer timeout = 5000;

    public ZKNodeUpdate() {
    }

    public ZKNodeUpdate(String connectString) {
        try {
            zooKeeper = new ZooKeeper(zkServerPath, timeout, new ZKNodeCreate());
        } catch (IOException e) {
            e.printStackTrace();
            if (zooKeeper != null) {
                try {
                    zooKeeper.close();
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        }

    }

    public static void main(String[] args) throws KeeperException, InterruptedException {
        ZKNodeUpdate zkServer = new ZKNodeUpdate(zkServerPath);

        /**
         * 参数说明
         * String path, byte[] data, int version, StatCallback cb, Object ctx
         * path:节点路径
         * data:节点数据
         * version: 节点状态(dataVersion版本),每次修改会累加1
         * cb:回调函数(类似于用第二种异步方式创建节点一样)
         * ctx:(同创建节点一样)
         * 这里不使用异步的方式;
         * 这个返回的 Stat对象就是我们用 get /testnode 命令获取结果是一致的;
         */
        Stat stat = zkServer.getZooKeeper().setData("/testnode", "1234".getBytes(), 0);
        System.out.println(stat.getVersion());//1

    }

    public void process(WatchedEvent watchedEvent) {

    }

    public ZooKeeper getZooKeeper() {
        return zooKeeper;
    }

    public void setZooKeeper(ZooKeeper zooKeeper) {
        this.zooKeeper = zooKeeper;
    }
}
