package cn.haoxy.zk.api.demo;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.ACL;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Haoxy
 * Created in 2019-07-17.
 * E-mail:hxyHelloWorld@163.com
 * github:https://github.com/haoxiaoyong1014
 */
public class ZKNodeDelete implements Watcher {

    private ZooKeeper zooKeeper = null;

    public static final String zkServerPath = "127.0.0.1:2181";

    public static final Integer timeout = 5000;

    public ZKNodeDelete() {
    }

    public ZKNodeDelete(String connectString) {
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

    //创建zk节点
    public void createZKNode(String path, byte[] data, List<ACL> acls) {
        String result = "";
        try {
            result = zooKeeper.create(path, data, acls, CreateMode.PERSISTENT);
            TimeUnit.SECONDS.sleep(2);
            System.out.println("创建节点：\t" + result + "\t成功...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws KeeperException, InterruptedException {
        ZKNodeDelete zkServer = new ZKNodeDelete(zkServerPath);
        //再删除之间先创建一个节点
        zkServer.createZKNode("/test-delete-node", "123".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE);
        //删除节点和创建修改一样都是有两个方法;
        //方式一: 同步
        //注意:你要是在这里 debug 看是否创建节点时候,注意超时时间;不然会报: KeeperErrorCode = ConnectionLoss for xxx;这个异常
        zkServer.getZooKeeper().delete("/test-delete-node", 0);
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
