package cn.haoxy.zk.api.demo;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Haoxy
 * Created in 2019-07-19.
 * E-mail:hxyHelloWorld@163.com
 * github:https://github.com/haoxiaoyong1014
 */
public class ZKNodeAclIp implements Watcher {

    private ZooKeeper zookeeper = null;

    public static final String zkServerPath = "127.0.0.1:2181";

    public static final Integer timeout = 5000;

    public ZKNodeAclIp() {
    }

    public ZKNodeAclIp(String connectString) {
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

    public static void main(String[] args) throws KeeperException, InterruptedException {
        ZKNodeAclIp zkServer = new ZKNodeAclIp(zkServerPath);
        /*List<ACL> aclsIP = new ArrayList<>();
        Id ipId1 = new Id("ip", "127.0.0.1");
        aclsIP.add(new ACL(ZooDefs.Perms.ALL, ipId1));
        zkServer.createZKNode("/aclmyzk/testIp", "testIp".getBytes(), aclsIP);*/

        /**--------------------验证Ip-----------------------**/

        zkServer.getZookeeper().setData("/aclmyzk/testIp", "now".getBytes(), 1);
        Stat stat = new Stat();
        byte[] data = zkServer.getZookeeper().getData("/aclmyzk/testIp", false, stat);
        System.out.println(new String(data));
        System.out.println(stat.getVersion());
    }

    @Override
    public void process(WatchedEvent watchedEvent) {

    }

    public ZooKeeper getZookeeper() {
        return zookeeper;
    }

    public void setZookeeper(ZooKeeper zookeeper) {
        this.zookeeper = zookeeper;
    }
}
