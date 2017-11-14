package com.luolei.tools.zookeeper;

import com.luolei.tools.zookeeper.my.FakedResource;
import com.luolei.tools.zookeeper.my.InterProcessSemaphoreTry;
import org.apache.curator.framework.recipes.locks.InterProcessSemaphoreV2;
import org.apache.curator.framework.recipes.locks.Lease;
import org.apache.curator.test.TestingServer;
import org.junit.Test;

import java.util.Collection;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author 罗雷
 * @date 2017/11/9 0009
 * @time 14:55
 */
public class InterProcessSemaphoreTryTest {

    @Test
    public void test() throws Exception {
        TestingServer server = new TestingServer();
        server.start();

        InterProcessSemaphoreTry semaphoreTry1 = new InterProcessSemaphoreTry(server.getConnectString());
        InterProcessSemaphoreTry semaphoreTry2 = new InterProcessSemaphoreTry(server.getConnectString());

        InterProcessSemaphoreV2 semaphore1 = semaphoreTry1.getSemaphore("ebg_sim", FakedResource.maxSize());
        InterProcessSemaphoreV2 semaphore2 = semaphoreTry2.getSemaphore("ebg_sim", FakedResource.maxSize());


        Collection<Lease> leases = semaphore1.acquire(5);
        System.out.println("semaphore1 获取成功");
        FakedResource.use();
        semaphoreTry1.shutdownClient();
        semaphore1.returnAll(leases);

        Lease lease = semaphore2.acquire(10, TimeUnit.SECONDS);
        if (Objects.nonNull(lease)) {
            System.out.println("semaphore2 获取成功");
            FakedResource.use();
            semaphore2.returnLease(lease);
        } else {
            System.out.println("获取锁失败");
        }

    }
}
