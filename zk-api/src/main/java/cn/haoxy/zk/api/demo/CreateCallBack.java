package cn.haoxy.zk.api.demo;

import org.apache.zookeeper.AsyncCallback;

/**
 * @author Haoxy
 * Created in 2019-07-15.
 * E-mail:hxyHelloWorld@163.com
 * github:https://github.com/haoxiaoyong1014
 */
public class CreateCallBack implements AsyncCallback.StringCallback {

    public void processResult(int i, String path, Object ctx, String name) {

        System.out.println("创建节点:" + path);
        System.out.println((String) ctx);
    }
}
