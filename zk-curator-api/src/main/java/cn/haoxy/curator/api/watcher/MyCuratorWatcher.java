package cn.haoxy.curator.api.watcher;

import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.zookeeper.WatchedEvent;

/**
 * @author Haoxy
 * Created in 2019-07-29.
 * E-mail:hxyHelloWorld@163.com
 * github:https://github.com/haoxiaoyong1014
 */
public class MyCuratorWatcher implements CuratorWatcher {
    public void process(WatchedEvent watchedEvent) throws Exception {
        System.out.println("触发watcher，节点路径为：" + watchedEvent.getPath());
    }
}
