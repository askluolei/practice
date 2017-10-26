package com.luolei.base.jmx.notification;

import javax.management.Notification;
import javax.management.NotificationBroadcasterSupport;

/**
 * Describe :
 * @author luolei
 * @email askluolei@gmail.com
 * @date 2017/10/26 23:18
 */
public class Jack extends NotificationBroadcasterSupport implements JackMBean {

    private int seq = 0;

    @Override
    public void hi() {
        //创建一个信息包
        // 通知名称；谁发起的通知；序列号；发起通知时间；发送的消息
        Notification notify = new Notification("jack.hi",this,++seq,System.currentTimeMillis(),"jack");
        sendNotification(notify);
    }
}
