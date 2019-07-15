package cn.haoxy.zk.api.demo;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @author Haoxy
 * Created in 2019-07-15.
 * E-mail:hxyHelloWorld@163.com
 * github:https://github.com/haoxiaoyong1014
 */

public class ZKConnect implements Watcher {

    private final static Logger log = LoggerFactory.getLogger(ZKConnect.class);

    public static final String zkServerPath = "127.0.0.1:2181";
    //集群下
    //public static final String zkServerPath ="192.168.1.111:2181,192.168.1.111:2182,192.168.1.111:2183";

    public static final Integer timeout = 5000;

    public static void main(String[] args) throws IOException, InterruptedException {
        /**
         * 参数说明:
         * connectString, sessionTimeout, watcher, sessionId, sessionPasswd,canBeReadOnly
         * connectString: 连接服务器的ip字符串，比如: "192.168.1.1:2181,192.168.1.2:2181,192.168.1.3:2181"
         * 可以是一个ip，也可以是多个ip，一个ip代表单机，多个ip代表集群,也可以在ip后加路径
         *
         *
         */
        ZooKeeper zk = new ZooKeeper(zkServerPath, timeout, new ZKConnect());

        log.warn("客户端开始连接zk服务端");


        log.warn("连接状态:{}", zk.getState());

        TimeUnit.SECONDS.sleep(3);

        log.warn("连接状态:{}", zk.getState());
    }

    public void process(WatchedEvent watchedEvent) {
        log.warn("接受到watch通知：{}", watchedEvent);
    }
}
