package cn.haoxy.zk.api.demo;

import cn.haoxy.zk.api.utils.AclUtils;
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
public class ZKNodeAclDigest implements Watcher {


    private ZooKeeper zookeeper = null;

    public static final String zkServerPath = "127.0.0.1:2181";

    public static final Integer timeout = 5000;

    public ZKNodeAclDigest() {
    }

    public ZKNodeAclDigest(String connectString) {
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

    public static void main2(String[] args) throws Exception {
        ZKNodeAclDigest zkServer = new ZKNodeAclDigest(zkServerPath);
        //自定义用户认证访问
        List<ACL> acls = new ArrayList<>();
        Id myzk1 = new Id("digest", AclUtils.getDigestUserPwd("haoxy1:123456"));
        Id myzk2 = new Id("digest", AclUtils.getDigestUserPwd("haoxy2:123456"));
        acls.add(new ACL(ZooDefs.Perms.ALL, myzk1));
        acls.add(new ACL(ZooDefs.Perms.READ, myzk2));
        acls.add(new ACL(ZooDefs.Perms.DELETE | ZooDefs.Perms.CREATE, myzk2));
        //这里是不能递归创建的,必须要先创建 /aclmyzk 父节点,才能创建 testdigest子节点
        zkServer.createZKNode("/aclmyzk/testdigest", "testdigest".getBytes(), acls);

        //到这里我们就已经创建带有权限的用户节点,我们将这个main改写为main2,下面用main来验证一下

    }

    //验证
    public static void main(String[] args) throws KeeperException, InterruptedException {
        ZKNodeAclDigest zkServer = new ZKNodeAclDigest(zkServerPath);

        //上面我们已经创建了两个用户,haoxy1和haoxy2,haoxy1是拥有所有的权限,haoxy2只拥有读删和创建

        zkServer.getZookeeper().addAuthInfo("digest", "haoxy2:123456".getBytes());
        /**--------------------验证创建子节点-----------------------**/
		//zkServer.createZKNode("/aclmyzk/testdigest/childtest", "childtest".getBytes(), ZooDefs.Ids.CREATOR_ALL_ACL);
		//打印结果创建成功,登录客户端查看是否创建成功时,需要登录之后才能查看不然会报Authentication is not valid : /aclmyzk/testdigest
        //登录方式前面已经说过: addauth digest haoxy2:123456
        /**---------------------验证读取数据--------------------**/
        /*Stat stat = new Stat();
		byte[] data = zkServer.getZookeeper().getData("/aclmyzk/testdigest", false, stat);
		System.out.println(new String(data));//打印结果 testdigest*/
        /**---------------------验证修改数据--------------------**/
        zkServer.getZookeeper().setData("/aclmyzk/testdigest", "now".getBytes(), 1);
        //抛出异常 KeeperErrorCode = NoAuth for /aclmyzk/testdigest 没有权限,我们在上面没有给用户haoxy2添加修改的权限;
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
