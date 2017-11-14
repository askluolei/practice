package com.luolei.tools.zookeeper.my;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessSemaphoreV2;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.test.KillSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentMap;

/**
 * @author 罗雷
 * @date 2017/11/9 0009
 * @time 14:51
 */
public class InterProcessSemaphoreTry {

    private static final Logger LOG = LoggerFactory.getLogger(InterProcessSemaphoreTry.class);
    private static final String SEMAPHORE_NODE = "/semaphore";

    private CuratorFramework client;

    private String connectString;
    public InterProcessSemaphoreTry(String connectString) {
        this.connectString = connectString;

        ConnectionStateListener connectionStateListener = (client, newState) -> {
            switch (newState) {
                case CONNECTED:
                    LOG.info("与 zookeeper 连接成功");
                    break;
                case LOST:
                    LOG.warn("与 zookeeper 连接丢失");
                    break;
                case RECONNECTED:
                    LOG.info("与 zookeeper 重连成功");
                    break;
                case SUSPENDED:
                    LOG.info("与 zookeeper 连接阻塞");
                    break;
                case READ_ONLY:
                    LOG.info("与 zookeeper 处在只读连接");
                    break;
                default:
            }

        };

        /**
         * 构建客户端
         */
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        client = CuratorFrameworkFactory.builder()
                .connectString(connectString)
                .sessionTimeoutMs(60000)
                .connectionTimeoutMs(5000)
                .retryPolicy(retryPolicy)
                .build();

        /**
         * 监听连接状态
         */
        client.getConnectionStateListenable().addListener(connectionStateListener);
        client.start();
    }

    private ConcurrentMap<String, InterProcessSemaphoreV2> semaphoreCache = Maps.newConcurrentMap();

    /**
     * 获取分布式信号量
     * @param bankLoginId
     * @param maxSize 信号量的size
     * @return
     */
    public InterProcessSemaphoreV2 getSemaphore(String bankLoginId, int maxSize) {
        if (!Strings.isNullOrEmpty(bankLoginId)) {
            InterProcessSemaphoreV2 v2 = new InterProcessSemaphoreV2(client, SEMAPHORE_NODE + "/" + bankLoginId, maxSize);
            semaphoreCache.putIfAbsent(bankLoginId, v2);
        }
        return semaphoreCache.get(bankLoginId);
    }

    public void killSession() {
        try {
            KillSession.kill(client.getZookeeperClient().getZooKeeper(), connectString);
        } catch (Exception e) {
        }
    }

    public void shutdownClient() {
        client.close();
    }

}
