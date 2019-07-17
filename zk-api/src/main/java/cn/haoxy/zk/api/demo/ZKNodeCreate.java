package cn.haoxy.zk.api.demo;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.ACL;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Haoxy
 * Created in 2019-07-15.
 * E-mail:hxyHelloWorld@163.com
 * github:https://github.com/haoxiaoyong1014
 * 创建zk节点
 */
public class ZKNodeCreate implements Watcher {

    private ZooKeeper zooKeeper = null;

    public static final String zkServerPath = "127.0.0.1:2181";

    public static final Integer timeout = 5000;

    public ZKNodeCreate() {
    }

    public ZKNodeCreate(String connectString) {
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
        /**
         * 同步或者异步创建节点,都不支持子节点的递归创建,异步有一个 callback 函数
         * 参数:
         * path:创建的路径
         * data:存储的数据 byte[]
         * acl:控制权限策略
         *          Ids.OPEN_ACL_UNSAFE --> world:anyone:cdrwa
         * 			CREATOR_ALL_ACL --> auth:user:password:cdrwa
         * createMode：节点类型, 是一个枚举
         * 			 		PERSISTENT：持久节点
         * 			 		PERSISTENT_SEQUENTIAL：持久顺序节点
         * 			 		EPHEMERAL：临时节点
         * 			 		EPHEMERAL_SEQUENTIAL：临时顺序节点
         */
        try {
            //方式一:同步
            //result = zooKeeper.create(path, data, acls, CreateMode.EPHEMERAL);

            //方式二: 异步创建节点
            String ctx = "{'create':'success'}";
            zooKeeper.create(path, data, acls, CreateMode.PERSISTENT, new CreateCallBack(), ctx);

            //System.out.println("创建节点：\t" + result + "\t成功...");
            TimeUnit.SECONDS.sleep(2);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ZKNodeCreate zkServer = new ZKNodeCreate(zkServerPath);
        zkServer.createZKNode("/testnode", "testnode".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE);
    }

    public void process(WatchedEvent watchedEvent) {

    }
}
