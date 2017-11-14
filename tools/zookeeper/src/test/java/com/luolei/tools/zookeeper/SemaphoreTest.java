package com.luolei.tools.zookeeper;

import com.google.common.collect.Lists;
import com.luolei.tools.zookeeper.cluster.NodeManager;
import com.luolei.tools.zookeeper.my.FakedResource;
import org.apache.curator.framework.recipes.locks.InterProcessSemaphoreV2;
import org.apache.curator.framework.recipes.locks.Lease;
import org.apache.curator.test.TestingServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author 罗雷
 * @date 2017/11/9 0009
 * @time 14:27
 */
public class SemaphoreTest {

    private static final Logger LOG = LoggerFactory.getLogger(SemaphoreTest.class);

    public static void main(String[] args) throws Exception {
        test1();
    }

    public static void test1() throws Exception {
        TestingServer server = new TestingServer();
        server.start();

        List<String> logins = Lists.newArrayList("ebg_sim-1", "icbc_cmp-1");
        String bankLoginId = "ebg_sim";
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

                //等待初始化完成
                int useSize = 10;
                for (int j = 0; j < useSize; j++) {
                    InterProcessSemaphoreV2 semaphore = nodeManager.getSemaphore(bankLoginId, FakedResource.maxSize());
                    Lease lease = null;
                    try {
                        lease = semaphore.acquire();
                        FakedResource.use();
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        if (Objects.nonNull(lease)) {
                            semaphore.returnLease(lease);
                        }
                    }
                }

            });
        }
    }
}
