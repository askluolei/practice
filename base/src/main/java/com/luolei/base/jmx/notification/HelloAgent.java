package com.luolei.base.jmx.notification;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;

/**
 * Describe :
 *
 * MBean之间的通信是必不可少的，Notification就起到了在MBean之间沟通桥梁的作用。JMX 的通知由四部分组成：

 1、Notification这个相当于一个信息包，封装了需要传递的信息

 2、Notification broadcaster这个相当于一个广播器，把消息广播出。

 3、Notification listener 这是一个监听器，用于监听广播出来的通知信息。

 4、Notification filiter 这个一个过滤器，过滤掉不需要的通知。这个一般很少使用。

 这里我们使用日常打招呼的场景：jack与我偶遇，jack说：hi；我礼貌的回答：hello，jack。
 *
 * @author luolei
 * @email askluolei@gmail.com
 * @date 2017/10/26 23:18
 */
public class HelloAgent {

    public static void main(String[] args) throws Exception {
        MBeanServer server = ManagementFactory.getPlatformMBeanServer();
        ObjectName helloName = new ObjectName("yunge:name=Hello");
        Hello hello=new Hello();
        server.registerMBean(hello, helloName);
        Jack jack = new Jack();
        server.registerMBean(jack, new ObjectName("jack:name=Jack"));
        jack.addNotificationListener(new HelloListener(), null, hello);

        Thread.sleep(500000);
    }
}
