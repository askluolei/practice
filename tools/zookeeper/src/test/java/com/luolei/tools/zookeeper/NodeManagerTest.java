package com.luolei.tools.zookeeper;

import com.luolei.tools.zookeeper.cluster.NodeManager;
import org.apache.curator.test.TestingServer;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author 罗雷
 * @date 2017/11/7 0007
 * @time 10:49
 */
public class NodeManagerTest {

    public static void main(String[] args) throws Exception{
        testMultiWithShutdownAndTimeout();
    }

    /**
     * 测试多节点启动获取节点号
     */
    public static void testMulti() throws Exception {
        TestingServer server = new TestingServer();
        server.start();

        int n = 5;
        ExecutorService executorService = Executors.newFixedThreadPool(n);
        for (int i = 0; i < n; i++) {
            executorService.submit(() -> {
                NodeManager nodeManager = new NodeManager(server.getConnectString());
                nodeManager.initNode();

                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                }
                printStatus(nodeManager);
            });
        }
    }

    /**
     * 测试多节点启动获取节点号
     * 其中几个节点模拟 过期 重连
     */
    public static void testMultiWithTimeout() throws Exception {
        TestingServer server = new TestingServer();
        server.start();

        int n = 5;
        ExecutorService executorService = Executors.newFixedThreadPool(n);
        for (int i = 0; i < n; i++) {
            boolean sessionTimeout = i % 2 == 1;
            executorService.submit(() -> {
                NodeManager nodeManager = new NodeManager(server.getConnectString(), sessionTimeout);
                nodeManager.initNode();
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                }
                printStatus(nodeManager);
            });
        }
    }

    /**
     * 测试多节点启动获取节点号
     * 其中几个节点模拟宕机
     */
    public static void testMultiWithShutdownAdd() throws Exception {
        TestingServer server = new TestingServer();
        server.start();

        int n = 5;
        int m = 3;
        ExecutorService executorService = Executors.newFixedThreadPool(n + m);
        for (int i = 0; i < n; i++) {
            boolean shutdown = i % 2 == 1;
            executorService.submit(() -> {
                NodeManager nodeManager = new NodeManager(server.getConnectString(), false, shutdown);
                nodeManager.initNode();
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                }
                printStatus(nodeManager);
            });
        }

        Thread.sleep(30000);

        for (int i = 0; i < m; i++) {
            executorService.submit(() -> {
                NodeManager nodeManager = new NodeManager(server.getConnectString());
                nodeManager.initNode();
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                }
                printStatus(nodeManager);
            });
        }

    }

    /**
     * 测试多节点启动获取节点号
     * 其中几个节点模拟宕机 几个节点模拟过期
     * 然后补充几个节点
     */
    public static void testMultiWithShutdownAndTimeout() throws Exception {
        TestingServer server = new TestingServer();
        server.start();

        int n = 5;
        int m = 3;
        ExecutorService executorService = Executors.newFixedThreadPool(n + m);
        for (int i = 0; i < n; i++) {
            boolean shutdown = i % 2 == 1;
            boolean timeout = i % 2 == 0;
            executorService.submit(() -> {
                NodeManager nodeManager = new NodeManager(server.getConnectString(), timeout, shutdown);
                nodeManager.initNode();
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                }
                printStatus(nodeManager);
            });
        }

        Thread.sleep(30000);

        for (int i = 0; i < m; i++) {
            executorService.submit(() -> {
                NodeManager nodeManager = new NodeManager(server.getConnectString());
                nodeManager.initNode();
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                }
                printStatus(nodeManager);
            });
        }

    }

    /**
     * 测试单节点启动获取节点号
     */
    public static void testSingle() throws Exception {
        TestingServer server = new TestingServer();
        server.start();

        NodeManager nodeManager = new NodeManager(server.getConnectString());
        nodeManager.initNode();

        Thread.sleep(10000);
        printStatus(nodeManager);
    }

    /**
     * 测试单节点启动获取节点号后模拟session过去
     * 重连
     */
    public static void testSingleWithReconnect() throws Exception {
        TestingServer server = new TestingServer();
        server.start();

        NodeManager nodeManager = new NodeManager(server.getConnectString(), true);
        nodeManager.initNode();

        printStatus(nodeManager);
    }

    /**
     * 测试单节点启动获取节点号后
     * 模拟宕机
     */
    public static void testSingleWithShutdown() throws Exception {
        TestingServer server = new TestingServer();
        server.start();

        NodeManager nodeManager = new NodeManager(server.getConnectString(), false, true);
        nodeManager.initNode();

        printStatus(nodeManager);
    }

    private static void printStatus(NodeManager nodeManager) {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
        }
        System.out.println("节点号为：" + nodeManager.getCurrentNodeID());
        System.out.println("是否可支付：" + nodeManager.canPay());
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
        }
        System.out.println("节点号为：" + nodeManager.getCurrentNodeID());
        System.out.println("是否可支付：" + nodeManager.canPay());
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
        }
        System.out.println("节点号为：" + nodeManager.getCurrentNodeID());
        System.out.println("是否可支付：" + nodeManager.canPay());
    }
}
