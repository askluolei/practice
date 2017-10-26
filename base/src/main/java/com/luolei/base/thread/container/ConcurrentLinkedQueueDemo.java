package com.luolei.base.thread.container;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * ConcurrentLinkedQueue是一个高并发队列，性能应该算是在高并发环境中性能最好的队列
 *
 * @author luolei
 * @date 2017-03-13 16:28
 */
public class ConcurrentLinkedQueueDemo {
    public static ConcurrentLinkedQueue<String> c = new ConcurrentLinkedQueue<>();
    public static CopyOnWriteArrayList<String> l = new CopyOnWriteArrayList<>();
    public static ConcurrentHashMap<String, String> m = new ConcurrentHashMap<>();
}
