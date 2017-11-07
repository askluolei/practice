package com.luolei.tools.zookeeper;

import com.luolei.tools.zookeeper.my.NodeIdManager;
import com.luolei.tools.zookeeper.my.NodeIdManager2;
import org.apache.curator.test.TestingServer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author 罗雷
 * @date 2017/11/3 0003
 * @time 17:12
 */
public class NodeIdManagerTest {

    public static void main(String[] args) throws Exception {
        testMultiManagerWithLockAdd();
    }

    /**
     * 测试 先启动5个节点，然后关闭2个节点模拟宕机，
     * 后面补充启动3个节点
     */
    public static void testMultiManagerWithLockAdd() throws Exception {
        int n = 5;
        ExecutorService executorService = Executors.newFixedThreadPool(n);
        TestingServer server = new TestingServer();
        server.start();

        List<NodeIdManager2> managers = new ArrayList<>(n);

        for (int i = 0; i < n; i++) {
            NodeIdManager2 manager = new NodeIdManager2(server.getConnectString(), i % 2 == 0);
            executorService.submit(manager);
            managers.add(manager);
        }
        Thread.sleep(5000);
        managers.forEach(m -> {
            System.out.println("canPay:" + m.canPay());
            System.out.println("nodeID:" + m.getCurrentNodeID());
        });


        Thread.sleep(15000);
        System.out.println("========== 邪恶的分隔符 ========== 新起3个节点");

        NodeIdManager2 manager = new NodeIdManager2(server.getConnectString());
        executorService.submit(manager);

        manager = new NodeIdManager2(server.getConnectString());
        executorService.submit(manager);

        manager = new NodeIdManager2(server.getConnectString());
        executorService.submit(manager);

        manager = new NodeIdManager2(server.getConnectString());
        executorService.submit(manager);

        Thread.sleep(5000);
        managers.forEach(m -> {
            System.out.println("canPay:" + m.canPay());
            System.out.println("nodeID:" + m.getCurrentNodeID());
        });
    }

    /**
     * 建立5个节点，完成建立后 断开5个连接，模拟session过期，然后测试重连机制
     */
    public static void testMultiManagerWithLock() throws Exception {
        int n = 5;
        ExecutorService executorService = Executors.newFixedThreadPool(n);
        TestingServer server = new TestingServer();
        server.start();

        List<NodeIdManager2> managers = new ArrayList<>(n);

        for (int i = 0; i < n; i++) {
            NodeIdManager2 manager = new NodeIdManager2(server.getConnectString());
            executorService.submit(manager);
            managers.add(manager);
        }

        Thread.sleep(15000);
        managers.forEach(m -> {
            System.out.println("canPay:" + m.canPay());
            System.out.println("nodeID:" + m.getCurrentNodeID());
        });
    }

    public static void testMultiManager() throws Exception {
        int n = 5;
        ExecutorService executorService = Executors.newFixedThreadPool(n);
        TestingServer server = new TestingServer();
        server.start();

        List<NodeIdManager> managers = new ArrayList<>(n);

        for (int i = 0; i < n; i++) {
            NodeIdManager manager = new NodeIdManager(server.getConnectString());
            executorService.submit(manager);
            managers.add(manager);
        }

        Thread.sleep(15000);
        managers.forEach(m -> {
            System.out.println("canPay:" + m.canPay());
            System.out.println("nodeID:" + m.getCurrentNodeID());
        });
    }

    public static void testSingleManager() throws Exception {
        Executor executor = Executors.newSingleThreadExecutor();
        TestingServer server = new TestingServer();
        server.start();

        NodeIdManager manager1 = new NodeIdManager(server.getConnectString());
        executor.execute(manager1);

        Thread.sleep(5000);

        System.out.println("canPay:" + manager1.canPay());
        System.out.println("nodeID:" + manager1.getCurrentNodeID());
    }
}
