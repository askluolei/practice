package com.luolei.tools.zookeeper.curator.counter;

import com.google.common.collect.Lists;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.shared.SharedCount;
import org.apache.curator.framework.recipes.shared.SharedCountListener;
import org.apache.curator.framework.recipes.shared.SharedCountReader;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.CloseableUtils;

import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author 罗雷
 * @date 2017/11/1 0001
 * @time 14:50
 */
public class SharedCounterExample {

    private static final String PATH = "/example/counter";
    private static final int QTY = 50;
    private static final String connectString = "127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183";

    public static void main(String[] args) {
        final Random rand = new Random();
        CuratorFramework client = null;

        try {
            client = CuratorFrameworkFactory.newClient(connectString, new ExponentialBackoffRetry(1000, 3));
            client.start();

            SharedCount sharedCount = new SharedCount(client, PATH, 0);
            sharedCount.addListener(new SharedCountListener() {
                @Override
                public void countHasChanged(SharedCountReader sharedCount, int newCount) throws Exception {
                    System.out.println("Counter's value is changed to " + newCount);
                }

                @Override
                public void stateChanged(CuratorFramework client, ConnectionState newState) {
                    System.out.println("State changed: " + newState.toString());
                }
            });

            sharedCount.start();

            List<SharedCount> examples = Lists.newArrayList();
            ExecutorService service = Executors.newFixedThreadPool(QTY);

            for (int i = 0; i < QTY; ++i) {
                final SharedCount count = new SharedCount(client, PATH, 0);
                examples.add(count);
                Callable<Void> task = () -> {
                    count.start();
                    Thread.sleep(rand.nextInt(10000));
                    System.out.println("Increment:" + count.trySetCount(count.getVersionedValue(), count.getCount() + rand.nextInt(10)));
                    return null;
                };
                service.submit(task);
            }

            service.shutdown();
            service.awaitTermination(10, TimeUnit.MINUTES);

            for (int i = 0; i < QTY; ++i) {
                examples.get(i).close();
            }
            sharedCount.close();

        } catch (Exception e) {

        } finally {
            CloseableUtils.closeQuietly(client);
        }
    }
}
