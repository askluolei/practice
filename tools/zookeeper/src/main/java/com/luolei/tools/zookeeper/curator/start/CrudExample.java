package com.luolei.tools.zookeeper.curator.start;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.BackgroundCallback;
import org.apache.curator.framework.api.CuratorListener;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.Watcher;

import java.util.List;

/**
 * @author 罗雷
 * @date 2017/11/1 0001 10:18
 */
public class CrudExample {

    public static void main(String[] args) {

    }

    public static void create(CuratorFramework client, String path, byte[] payload) throws Exception {
        //创建一个持久节点，附带数据
        client.create().forPath(path, payload);
    }

    public static void createEphemeral(CuratorFramework client, String path, byte[] payload) throws Exception {
        //创建一个临时节点，附带数据
        client.create().withMode(CreateMode.EPHEMERAL).forPath(path, payload);
    }

    public static String createEphemeralSequential(CuratorFramework client, String path, byte[] payload) throws Exception {
        //创建一个临时有序节点，附带数据
        //使用 curator 的保护模式 ACL 认证
        return client.create().withProtection().withMode(CreateMode.EPHEMERAL_SEQUENTIAL).forPath(path, payload);
    }

    public static void setData(CuratorFramework client, String path, byte[] payload) throws Exception {
        // 设置数据
        client.setData().forPath(path, payload);
    }

    public static void setDataAsync(CuratorFramework client, String path, byte[] payload) throws Exception {
        //异步设置数据，并且设置监听
        CuratorListener listener = (client1, event) -> {
            //这里处理事件
        };
        client.getCuratorListenable().addListener(listener);
        client.setData().inBackground().forPath(path, payload);
    }

    public static void setDataAsyncWithCallback(CuratorFramework client, BackgroundCallback callback, String path, byte[] payload) throws Exception {
        //异步设置数据，并且添加回调方法
        client.setData().inBackground(callback).forPath(path, payload);
    }

    public static void delete(CuratorFramework client, String path) throws Exception {
        //删除 path
        client.delete().forPath(path);
    }

    public static void guaranteedDelete(CuratorFramework client, String path) throws Exception {
        //保证 删除给定的 path
        client.delete().guaranteed().forPath(path);
    }

    public static List<String> watchedGetChildren(CuratorFramework client, String path) throws Exception {
        //获取子节点，并且在给定path上设置一个 监听，监听通知将通过 CuratorListener 传回
        return client.getChildren().watched().forPath(path);
    }

    public static List<String> watchedGetChildren(CuratorFramework client, String path, Watcher watcher) throws Exception {
        //获取子节点，并且在给定path上设置一个给定  watcher
        return client.getChildren().usingWatcher(watcher).forPath(path);
    }
}
