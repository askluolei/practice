package com.luolei.tools.zookeeper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.luolei.tools.zookeeper.dubbo.CuratorZookeeperClient;
import com.luolei.tools.zookeeper.dubbo.NodeManager;
import com.luolei.tools.zookeeper.dubbo.URL;
import com.luolei.tools.zookeeper.dubbo.ZookeeperClient;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;
import org.apache.curator.test.KillSession;
import org.apache.curator.test.TestingServer;
import org.apache.zookeeper.CreateMode;


/**
 * @author 罗雷
 * @date 2017/12/21 0021
 * @time 9:15
 */
public class NodeManagerTestDubbo {

    private static final String connectString = "";

    public static void main(String[] args) throws Exception {
        testMulti();
    }

    public static void test2() throws Exception {
        TestingServer server = new TestingServer();
        server.start();

        CuratorFrameworkFactory.Builder builder = CuratorFrameworkFactory.builder()
                .connectString(server.getConnectString())
                .retryPolicy(new RetryNTimes(Integer.MAX_VALUE, 1000))
                .connectionTimeoutMs(5000);
        builder.namespace("test");
        CuratorFramework client = builder.build();
        client.start();

        client.create().withMode(CreateMode.PERSISTENT).forPath("/node");
        sleepQuietly(1000);
        client.create().withMode(CreateMode.EPHEMERAL).forPath("/node/node1");
        sleepQuietly(1000);
        client.create().withMode(CreateMode.EPHEMERAL).forPath("/node/node2");
        sleepQuietly(1000);

        client.close();
        server.close();
    }

    public static void test() throws Exception {
        TestingServer server = new TestingServer();
        server.start();
        ZookeeperClient zookeeperClient = new CuratorZookeeperClient(new URL(server.getConnectString(), "test"));
        sleepQuietly(5000);
        zookeeperClient.create("/node", "", false);
        sleepQuietly(1000);
        zookeeperClient.create("/node/node1", "", true);
        System.out.println("--- 创建成功 ---");
        sleepQuietly(1000);
        zookeeperClient.create("/node/node2", "", true);
        System.out.println("--- 创建成功 ---");
        sleepQuietly(1000);
        zookeeperClient.create("/node/node3", "", true);
        System.out.println("--- 创建成功 ---");

        sleepQuietly(5000);

        List<String> children = zookeeperClient.getChildren("/node");
        System.out.println("children : " + Arrays.toString(children.toArray()));
        zookeeperClient.close();
        server.close();
    }

    /**
     * 单节点启动
     */
    public static void testSingle() throws Exception {
        TestingServer server = new TestingServer();
        server.start();
        NodeManager nodeManager = new NodeManager(server.getConnectString());
        sleepQuietly(10000);
        nodeManager.close();
        sleepQuietly(10000);
        server.close();
    }

    /**
     * 单节点启动，与 zookeeper 连接断开后重连机制
     */
    public static void testSingleWithKill() throws Exception {
        TestingServer server = new TestingServer();
        server.start();
        NodeManager nodeManager = new NodeManager(server.getConnectString());
        sleepQuietly(10000);
        CuratorZookeeperClient zookeeperClient = (CuratorZookeeperClient) nodeManager.getZookeeperClient();
        KillSession.kill(zookeeperClient.getCuratorFramework().getZookeeperClient().getZooKeeper(),
                server.getConnectString());
        System.out.println("------------ 我是分割线 ------------");
        sleepQuietly(30000);
        nodeManager.close();
        server.close();
    }

    /**
     * 多节点启动
     */
    public static void testMulti() throws Exception {
        TestingServer server = new TestingServer();
        server.start();

        int nThreads = 5;
        List<Thread> threads = new ArrayList<Thread>(nThreads);
        for (int i = 0; i < nThreads; i++) {
            Thread t = new Thread(() -> {
                NodeManager nodeManager = new NodeManager(server.getConnectString());
                sleepQuietly(30000);
                nodeManager.close();
            });
            t.start();
            threads.add(t);
        }
        for (Thread t : threads) {
            t.join();
        }
        sleepQuietly(10000);
        server.close();
    }

    /**
     * 多节点启动
     */
    public static void testMultiWithKill() throws Exception {
        TestingServer server = new TestingServer();
        server.start();

        int nThreads = 5;
        List<Thread> threads = new ArrayList<Thread>(nThreads);
        for (int i = 0; i < nThreads; i++) {
            Thread t = new Thread(() -> {
                NodeManager nodeManager = new NodeManager(server.getConnectString());
                sleepQuietly(30000);
                CuratorZookeeperClient zookeeperClient = (CuratorZookeeperClient) nodeManager.getZookeeperClient();
                try {
                    KillSession.kill(zookeeperClient.getCuratorFramework().getZookeeperClient().getZooKeeper(),
                            server.getConnectString());
                } catch (Exception e) {
                }
                System.out.println("------------ 我是分割线 ------------");
                sleepQuietly(30000);
                nodeManager.close();
            });
            t.start();
            threads.add(t);
        }
        for (Thread t : threads) {
            t.join();
        }
        sleepQuietly(10000);
        server.close();
    }

    private static void sleepQuietly(long sleepTime) {
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
        }
    }
}
