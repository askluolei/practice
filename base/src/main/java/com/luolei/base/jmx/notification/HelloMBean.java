package com.luolei.base.jmx.notification;

/**
 * Describe : 注册的MBean 必须先定义接口 接口名必须以MBean结尾
 * @author luolei
 * @email askluolei@gmail.com
 * @date 2017/10/26 23:18
 */
public interface HelloMBean {

    String getName();

    void setName(String name);

    void printHello();

    void printHello(String whoName);
}
