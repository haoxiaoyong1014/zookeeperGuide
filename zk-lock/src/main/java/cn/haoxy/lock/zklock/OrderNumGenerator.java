package cn.haoxy.lock.zklock;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by haoxiaoyong on 2019/8/19 下午 10:20
 * e-mail: hxyHelloWorld@163.com
 * github:https://github.com/haoxiaoyong1014
 * Blog: www.haoxiaoyong.cn
 */
public class OrderNumGenerator {

    //全局订单id;
    private static int count = 0;

    public String getNumber() {

        SimpleDateFormat simpt = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        return simpt.format(new Date()) + "-" + ++count;
    }
}
