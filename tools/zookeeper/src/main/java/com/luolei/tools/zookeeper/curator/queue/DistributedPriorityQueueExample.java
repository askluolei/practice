package com.luolei.tools.zookeeper.curator.queue;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.queue.DistributedPriorityQueue;
import org.apache.curator.framework.recipes.queue.QueueBuilder;
import org.apache.curator.framework.recipes.queue.QueueConsumer;
import org.apache.curator.framework.recipes.queue.QueueSerializer;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.CloseableUtils;

/**
 * @author 罗雷
 * @date 2017/11/1 0001
 * @time 11:21
 */
public class DistributedPriorityQueueExample {

    private static final String PATH = "/example/queue";

    private static final String connectString = "127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183";

    public static void main(String[] args) {
        CuratorFramework client = null;
        DistributedPriorityQueue<String> queue = null;

        try {
            client = CuratorFrameworkFactory.newClient(connectString, new ExponentialBackoffRetry(1000, 3));
            client.getCuratorListenable().addListener((client1, event) -> {
                System.out.println("CuratorEvent: " + event.getType().name());
            });
            client.start();

            QueueConsumer<String> consumer = createQueueConsumer();
            QueueBuilder<String> builder = QueueBuilder.builder(client, consumer, createQueueSerializer(), PATH);
            queue = builder.buildPriorityQueue(0);
            queue.start();

            for (int i = 0; i < 10; i++) {
                int priority = (int)(Math.random() * 100);
                System.out.println("test-" + i + " priority:" + priority);
                queue.put("test-" + i, priority);
                Thread.sleep((long)(50 * Math.random()));
            }

            Thread.sleep(20000);
        } catch (Exception e) {

        } finally {
            CloseableUtils.closeQuietly(queue);
            CloseableUtils.closeQuietly(client);
        }
    }

    private static QueueSerializer<String> createQueueSerializer() {
        return new QueueSerializer<String>(){

            @Override
            public byte[] serialize(String item) {
                return item.getBytes();
            }

            @Override
            public String deserialize(byte[] bytes) {
                return new String(bytes);
            }

        };
    }

    private static QueueConsumer<String> createQueueConsumer() {

        return new QueueConsumer<String>(){

            @Override
            public void stateChanged(CuratorFramework client, ConnectionState newState) {
                System.out.println("connection new state: " + newState.name());
            }

            @Override
            public void consumeMessage(String message) throws Exception {
                System.out.println("consume one message: " + message);
            }

        };
    }
}
