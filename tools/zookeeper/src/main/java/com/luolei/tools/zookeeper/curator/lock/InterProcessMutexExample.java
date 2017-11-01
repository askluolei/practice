package com.luolei.tools.zookeeper.curator.lock;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.CloseableUtils;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author 罗雷
 * @date 2017/11/1 0001
 * @time 15:23
 */
public class InterProcessMutexExample {

    private static final int QTY = 5;
    private static final int REPETITIONS = QTY * 10;
    private static final String PATH = "/examples/locks";
    private static final String connectString = "127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183";

    public static void main(String[] args) {
        final FakeLimitedResource resource = new FakeLimitedResource();
        ExecutorService service = Executors.newFixedThreadPool(QTY);

        try {
            for (int i = 0; i < QTY; i++) {
                final int index = i;
                Callable<Void> task = () -> {
                    CuratorFramework client = CuratorFrameworkFactory.newClient(connectString, new ExponentialBackoffRetry(1000, 3));
                    try {
                        client.start();
                        final ExampleClientThatLocks example = new ExampleClientThatLocks(client, PATH, resource, "Client " + index);
                        for (int j = 0; j < REPETITIONS; ++j) {
                            example.doWork(10, TimeUnit.SECONDS);
                        }
                    } catch (Throwable e) {
                        e.printStackTrace();
                    } finally {
                        CloseableUtils.closeQuietly(client);
                    }
                    return null;
                };
                service.submit(task);
            }

            service.shutdown();
            service.awaitTermination(10, TimeUnit.MINUTES);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
    }
}
