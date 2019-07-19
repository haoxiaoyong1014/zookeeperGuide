package cn.haoxy.zk.api.demo;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.ACL;

import java.io.IOException;
import java.util.List;

/**
 * @author Haoxy
 * Created in 2019-07-19.
 * E-mail:hxyHelloWorld@163.com
 * github:https://github.com/haoxiaoyong1014
 */
public class ZKNodeAclAnyone implements Watcher {

    private ZooKeeper zookeeper = null;

    public static final String zkServerPath = "127.0.0.1:2181";
    public static final Integer timeout = 5000;

    public ZKNodeAclAnyone() {}

    public ZKNodeAclAnyone(String connectString) {
        try {
            zookeeper = new ZooKeeper(connectString, timeout, new ZKNodeAclAnyone());
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

    public void createZKNode(String path, byte[] data, List<ACL> acls) {

        String result = "";
        try {
            result = zookeeper.create(path, data, acls, CreateMode.PERSISTENT);
            System.out.println("创建节点：\t" + result + "\t成功...");
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ZKNodeAclAnyone zkServer=new ZKNodeAclAnyone(zkServerPath);
        //acl 任何人都可以访问
        zkServer.createZKNode("/aclmyzk","test".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE);
    }


    @Override
    public void process(WatchedEvent watchedEvent) {

    }
}
