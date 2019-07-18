package cn.haoxy.zk.api.demo;

import org.apache.zookeeper.AsyncCallback;

import java.util.List;

/**
 * Created by haoxiaoyong on 2019/7/18 下午 9:41
 * e-mail: hxyHelloWorld@163.com
 * github:https://github.com/haoxiaoyong1014
 * Blog: www.haoxiaoyong.cn
 */
public class ChildrenCallBack implements AsyncCallback.ChildrenCallback {

    public void processResult(int rc, String path, Object ctx, List<String> children) {
        for (String s : children) {
            System.out.println(s);
        }
        System.out.println("ChildrenCallback:" + path);
        System.out.println((String)ctx);
    }
}
