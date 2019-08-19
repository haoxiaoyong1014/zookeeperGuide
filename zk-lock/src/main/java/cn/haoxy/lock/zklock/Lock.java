package cn.haoxy.lock.zklock;

/**
 * Created by haoxiaoyong on 2019/8/19 下午 9:52
 * e-mail: hxyHelloWorld@163.com
 * github:https://github.com/haoxiaoyong1014
 * Blog: www.haoxiaoyong.cn
 */
public interface Lock {

    //获取到锁的资源
    void getLock();
    // 释放锁
    void unLock();

}
