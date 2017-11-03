package com.luolei.tools.zookeeper.my;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.atomic.AtomicValue;
import org.apache.curator.framework.recipes.atomic.DistributedAtomicInteger;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.retry.RetryNTimes;
import org.apache.curator.test.TestingServer;
import org.apache.curator.utils.CloseableUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author 罗雷
 * @date 2017/11/2 0002
 * @time 16:07
 */
public class AtomicIntegerTry {

    private static final String namespace = "example";
    private static final String node = "/node";
    private static final String connectString = "127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183";
    private static Logger logger = LoggerFactory.getLogger(AtomicIntegerTry.class);

    public static void main(String[] args) throws Exception {
        ConnectionStateListener connectionStateListener = (client, newState) -> {
            switch (newState) {
                case CONNECTED:logger.info("与 zookeeper 连接成功");break;
                case LOST:logger.warn("与 zookeeper 连接丢失");break;
                case RECONNECTED:logger.info("与 zookeeper 重连成功");break;
                case SUSPENDED: logger.info("与 zookeeper 连接阻塞");break;
                case READ_ONLY:logger.info("与 zookeeper 处在只读连接");break;
                default:
            }
        };
        TestingServer server = new TestingServer(18123);
        server.start();
        server.start();
        CuratorFramework client = CuratorFrameworkFactory.builder()
//                .connectString(connectString)
                .connectString(server.getConnectString())
                .sessionTimeoutMs(60000)
                .connectionTimeoutMs(15000)
                .namespace(namespace)
                .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                .build();
        client.getConnectionStateListenable().addListener(connectionStateListener);
        client.start();
        DistributedAtomicInteger atomicInteger = new DistributedAtomicInteger(client, node, new RetryNTimes(10, 10));

        checkValue(atomicInteger.get());

        checkValue(atomicInteger.increment());

        CloseableUtils.closeQuietly(client);
        CloseableUtils.closeQuietly(server);
    }

    private static void checkValue(AtomicValue<Integer> value) {
        logger.info("=========== 邪恶的分隔符 ================");
        if (value.succeeded()) {
            logger.info("----------- 操作成功 ------------------");
        } else {
            logger.info("----------- 操作失败 ------------------");
        }
        logger.info("操作前的value:{} , 操作后的value:{}", value.preValue(), value.postValue());
        logger.info("=========== 邪恶的分隔符 ================");
    }
}
