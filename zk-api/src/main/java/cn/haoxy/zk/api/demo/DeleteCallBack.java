package cn.haoxy.zk.api.demo;

import org.apache.zookeeper.AsyncCallback;

/**
 * Created by haoxiaoyong on 2019/7/18 下午 6:50
 * e-mail: hxyHelloWorld@163.com
 * github:https://github.com/haoxiaoyong1014
 * Blog: www.haoxiaoyong.cn
 */
public class DeleteCallBack implements AsyncCallback.VoidCallback {

    public void processResult(int rc, String path, Object ctx) {
        System.out.println("删除节点" + path);
        System.out.println((String)ctx);
    }
}
