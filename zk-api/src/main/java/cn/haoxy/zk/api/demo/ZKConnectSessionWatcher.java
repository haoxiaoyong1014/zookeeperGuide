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
 * 恢复之前的会话连接demo演示
 */
public class ZKConnectSessionWatcher implements Watcher {

    final static Logger log = LoggerFactory.getLogger(ZKConnectSessionWatcher.class);

    public static final String zkServerPath = "127.0.0.1:2181";

    public static final Integer timeout = 5000;

    public static void main(String[] args) throws IOException, InterruptedException {
        ZooKeeper zk = new ZooKeeper(zkServerPath, timeout, new ZKConnectSessionWatcher());
        long sessionId = zk.getSessionId();
        String ssid = "0x" + Long.toHexString(sessionId);
        log.warn("sessionId:{}", ssid);
        byte[] sessionPasswd = zk.getSessionPasswd();
        log.warn("客户端开始连接 zookeeper服务器..");
        log.warn("连接状态:{}", zk.getState());
        TimeUnit.SECONDS.sleep(1);
        log.warn("连接状态:{}", zk.getState());
        TimeUnit.SECONDS.sleep(1);
        //开始会话重连
        log.warn("开始会话重连...");
        ZooKeeper zooKeeper = new ZooKeeper(zkServerPath,
                timeout,
                new ZKConnectSessionWatcher(),
                sessionId,
                sessionPasswd);
        log.warn("重新连接状态zooKeeper:{}", zooKeeper.getState());
        TimeUnit.SECONDS.sleep(1);
        log.warn("重新连接状态zooKeeper:{}", zooKeeper.getState());

    }


    public void process(WatchedEvent watchedEvent) {
        log.warn("接受到watch通知：{}", watchedEvent);
    }
}
