package cn.haoxy.lock.zklock;

import org.I0Itec.zkclient.ZkClient;

/**
 * Created by haoxiaoyong on 2019/8/19 下午 9:53
 * e-mail: hxyHelloWorld@163.com
 * github:https://github.com/haoxiaoyong1014
 * Blog: www.haoxiaoyong.cn
 */
public abstract class ZookeeperAbstractLock implements Lock {

    // zk连接地址
    private static final String CONNECTSTRING = "47.100.102.136:2181";

    // 创建zk连接
    protected ZkClient zkClient = new ZkClient(CONNECTSTRING);

    protected static final String PATH = "/lock";

    public void getLock(){
        if(tryLock()){
            System.out.println("##获取lock锁的资源####");
        }else {
            //等待
            waitLock();
            //重新获取资源
            getLock();
        }
    }

    //获取锁资源
    abstract boolean tryLock();

    //等待
    abstract void waitLock();

    public void unLock() {
        if (zkClient != null) {
            zkClient.close();
            System.out.println("释放锁资源...");
        }
    }
}
