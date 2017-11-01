package com.luolei.tools.zookeeper.curator.counter;

import com.google.common.collect.Lists;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.atomic.AtomicValue;
import org.apache.curator.framework.recipes.atomic.DistributedAtomicLong;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.retry.RetryNTimes;
import org.apache.curator.utils.CloseableUtils;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author 罗雷
 * @date 2017/11/1 0001
 * @time 15:12
 */
public class DistributedAtomicLongExample {

    private static final int QTY = 5;
    private static final String PATH = "/examples/counter";
    private static final String connectString = "127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183";

    public static void main(String[] args) {
        CuratorFramework client = null;

        try {
            client = CuratorFrameworkFactory.newClient(connectString, new ExponentialBackoffRetry(1000, 3));
            client.start();

            List<DistributedAtomicLong> examples = Lists.newArrayList();
            ExecutorService service = Executors.newFixedThreadPool(QTY);

            for (int i = 0; i < QTY; ++i) {
                final DistributedAtomicLong count = new DistributedAtomicLong(client, PATH, new RetryNTimes(10, 10));

                examples.add(count);
                Callable<Void> task = () -> {
                    try {
                        //Thread.sleep(rand.nextInt(1000));
                        AtomicValue<Long> value = count.increment();
                        //AtomicValue<Long> value = count.decrement();
                        //AtomicValue<Long> value = count.add((long)rand.nextInt(20));
                        System.out.println("succeed: " + value.succeeded());
                        if (value.succeeded())
                            System.out.println("Increment: from " + value.preValue() + " to " + value.postValue());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return null;
                };
                service.submit(task);
            }

            service.shutdown();
            service.awaitTermination(10, TimeUnit.MINUTES);
        } catch (Exception e) {

        } finally {
            CloseableUtils.closeQuietly(client);
        }
    }
}
