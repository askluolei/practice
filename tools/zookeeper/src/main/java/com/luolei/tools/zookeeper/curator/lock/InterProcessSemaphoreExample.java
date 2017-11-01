package com.luolei.tools.zookeeper.curator.lock;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessSemaphoreV2;
import org.apache.curator.framework.recipes.locks.Lease;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.CloseableUtils;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

/**
 * @author 罗雷
 * @date 2017/11/1 0001
 * @time 16:25
 */
public class InterProcessSemaphoreExample {

    private static final int MAX_LEASE = 10;
    private static final int QTY = 5;
    private static final String PATH = "/examples/locks";
    private static final String connectString = "127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183";

    public static void main(String[] args) throws Exception {
        FakeLimitedResource resource = new FakeLimitedResource();

        CuratorFramework client = CuratorFrameworkFactory.newClient(connectString, new ExponentialBackoffRetry(1000, 3));
        client.start();

        InterProcessSemaphoreV2 semaphore = new InterProcessSemaphoreV2(client, PATH, MAX_LEASE);
        Collection<Lease> leases = semaphore.acquire(5);
        System.out.println("get " + leases.size() + " leases");
        Lease lease = semaphore.acquire();
        System.out.println("get another lease");

        resource.use();

        Collection<Lease> leases2 = semaphore.acquire(5, 10, TimeUnit.SECONDS);
        System.out.println("Should timeout and acquire return " + leases2);

        System.out.println("return one lease");
        semaphore.returnLease(lease);
        System.out.println("return another 5 leases");
        semaphore.returnAll(leases);

        CloseableUtils.closeQuietly(client);
    }
}
