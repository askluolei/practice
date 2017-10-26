package com.luolei.base.jmx.notification;

import javax.management.Notification;
import javax.management.NotificationListener;

/**
 * Describe :
 * @author luolei
 * @email askluolei@gmail.com
 * @date 2017/10/26 23:18
 */
public class HelloListener implements NotificationListener {

    @Override
    public void handleNotification(Notification notification, Object handback) {
        if(handback instanceof Hello) {
            Hello hello = (Hello)handback;
            hello.printHello(notification.getMessage());
        }
    }
}
