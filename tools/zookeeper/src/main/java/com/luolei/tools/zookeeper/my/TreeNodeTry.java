package com.luolei.tools.zookeeper.my;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.test.TestingServer;
import org.apache.curator.utils.CloseableUtils;

import java.util.Map;

/**
 * @author 罗雷
 * @date 2017/11/3 0003
 * @time 15:36
 */
public class TreeNodeTry {

    private static final String treePath = "/tree";

    public static void main(String[] args) throws Exception{
        TestingServer server = new TestingServer();
        server.start();

        CuratorFramework client = CuratorFrameworkFactory.newClient(server.getConnectString(), new ExponentialBackoffRetry(1000, 3) );
        client.start();

        client.create().forPath(treePath , "tree".getBytes());

        TreeCache treeCache = new TreeCache(client, treePath);
        treeCache.start();

        client.create().forPath(treePath + "/t1", "t1".getBytes());
        client.create().forPath(treePath + "/t2", "t2".getBytes());

        printTree(treeCache);

        client.create().forPath(treePath + "/t3", "t3".getBytes());
        client.create().forPath(treePath + "/t4", "t4".getBytes());

        //这里停1s 等待 zookeeper 处理完成
        Thread.sleep(1000);
        printTree(treeCache);

        CloseableUtils.closeQuietly(client);
        CloseableUtils.closeQuietly(server);
    }

    private static void printTree(TreeCache treeCache) {
        System.out.println("=========== 邪恶的分割线 ================");
        Map<String, ChildData> children = treeCache.getCurrentChildren(treePath);
        for (Map.Entry<String, ChildData> entry : children.entrySet()) {
            System.out.println("key:" + entry.getKey());
            ChildData data = entry.getValue();
            System.out.println("path:" + data.getPath());
        }
    }
}
