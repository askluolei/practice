package com.luolei.tools.zookeeper.curator.barrier;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.barriers.DistributedDoubleBarrier;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author 罗雷
 * @date 2017/11/1 0001
 * @time 17:16
 */
public class DistributedDoubleBarrierExample {

    private static final int QTY = 5;
    private static final String PATH = "/examples/barrier";
    private static final String connectString = "127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183";

    public static void main(String[] args) throws Exception {
        CuratorFramework client = CuratorFrameworkFactory.newClient(connectString, new ExponentialBackoffRetry(1000, 3));
        client.start();

        ExecutorService service = Executors.newFixedThreadPool(QTY);
        for (int i = 0; i < QTY; ++i) {
            final DistributedDoubleBarrier barrier = new DistributedDoubleBarrier(client, PATH, QTY);
            final int index = i;
            Callable<Void> task = () -> {
                Thread.sleep((long) (3 * Math.random()));
                System.out.println("Client #" + index + " enters");
                barrier.enter();
                System.out.println("Client #" + index + " begins");
                Thread.sleep((long) (3000 * Math.random()));
                barrier.leave();
                System.out.println("Client #" + index + " left");
                return null;
            };
            service.submit(task);
        }

        service.shutdown();
        service.awaitTermination(10, TimeUnit.MINUTES);
    }
}
