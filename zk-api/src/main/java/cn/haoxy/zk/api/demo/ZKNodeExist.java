package cn.haoxy.zk.api.demo;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * @author Haoxy
 * Created in 2019-07-19.
 * E-mail:hxyHelloWorld@163.com
 * github:https://github.com/haoxiaoyong1014
 */
public class ZKNodeExist implements Watcher {


    private ZooKeeper zookeeper = null;

    public static final String zkServerPath = "127.0.0.1:2181";

    public static final Integer timeout = 5000;

    public ZKNodeExist() {
    }


    public ZKNodeExist(String connectString) {
        try {
            zookeeper = new ZooKeeper(connectString, timeout, new ZKNodeExist());
        } catch (IOException e) {
            e.printStackTrace();
            if (zookeeper != null) {
                try {
                    zookeeper.close();
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    private static CountDownLatch downLatch = new CountDownLatch(1);

    public static void main(String[] args) throws KeeperException, InterruptedException {
        ZKNodeExist zkServer = new ZKNodeExist(zkServerPath);
        /**
         * 参数：
         * path：节点路径
         * watch：watch
         */
        Stat my_zk = zkServer.getZookeeper().exists("/my_zk", true);
        if (my_zk != null) {
            System.out.println("查询的节点版本为dataVersion：" + my_zk.getVersion());
        } else {
            System.out.println("该节点不存在");
        }
        downLatch.await();
    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        if (watchedEvent.getType() == Event.EventType.NodeCreated) {
            System.out.println("节点创建");
            downLatch.countDown();
        } else if (watchedEvent.getType() == Event.EventType.NodeDataChanged) {
            System.out.println("节点数据改变");
            downLatch.countDown();
        } else if (watchedEvent.getType() == Event.EventType.NodeDeleted) {
            System.out.println("节点删除");
            downLatch.countDown();
        }
    }

    public ZooKeeper getZookeeper() {
        return zookeeper;
    }

    public void setZookeeper(ZooKeeper zookeeper) {
        this.zookeeper = zookeeper;
    }
}
