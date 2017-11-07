package com.luolei.tools.zookeeper.cluster;

import com.google.common.collect.Maps;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.atomic.AtomicValue;
import org.apache.curator.framework.recipes.atomic.DistributedAtomicInteger;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.retry.RetryNTimes;
import org.apache.curator.test.KillSession;
import org.apache.curator.utils.ZKPaths;
import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * 应用运行时依赖节点号
 * 在启动的时候获取节点号
 *
 * @author 罗雷
 * @date 2017/11/7 0007
 * @time 10:25
 */
public class NodeManager {

    private static final String SEPARATOR = "-";
    private static final String NAMESPACE = "ebservice";
    private static final String ID_NODE = "/node";
    private static final String LOCK_NODE = "/lock";

    private static final Logger LOG = LoggerFactory.getLogger(NodeManager.class);

    /**
     * zookeeper 相关的
     */
    private CuratorFramework client;
    private TreeCache treeCache;
    private DistributedAtomicInteger distributedAtomicInteger;
    private InterProcessMutex lock;

    /**
     * 与 zookeeper 的连接状态
     */
    private volatile boolean isConnected = false;

    /**
     * 节点号
     */
    private volatile String currentNodeID = null;

    /**
     * 重连中
     */
    private boolean reconnecting = false;

    /**
     * 是否可以支付
     */
    public boolean canPay() {
        return isConnected && !reconnecting && currentNodeID != null;
    }

    /**
     * 获取当前节点号
     */
    public String getCurrentNodeID() {
        return currentNodeID;
    }

    /**
     * zookeeper 的连接信息
     */
    private final String connectString;

    /**
     * 测试 session 过期,重连
     */
    private boolean isTestSessionTimeout = false;

    /**
     * 测试 宕机
     */
    private boolean isTestShutdown = false;

    public NodeManager(String connectString) {
        this.connectString = connectString;
    }

    public NodeManager(String connectString, boolean isTestSessionTimeout) {
        this.connectString = connectString;
        this.isTestSessionTimeout = isTestSessionTimeout;
    }

    public NodeManager(String connectString, boolean isTestSessionTimeout, boolean isTestShutdown) {
        this.connectString = connectString;
        this.isTestSessionTimeout = isTestSessionTimeout;
        this.isTestShutdown = isTestShutdown;
    }

    /**
     * 初始化节点
     */
    public void initNode() {
        ConnectionStateListener connectionStateListener = (client, newState) -> {
            switch (newState) {
                case CONNECTED:
                    LOG.info("与 zookeeper 连接成功");
                    break;
                case LOST:
                    LOG.warn("与 zookeeper 连接丢失");
                    reconnecting = true;
                    break;
                case RECONNECTED:
                    LOG.info("与 zookeeper 重连成功");
                    reconnect();
                    break;
                case SUSPENDED:
                    LOG.info("与 zookeeper 连接阻塞");
                    break;
                case READ_ONLY:
                    LOG.info("与 zookeeper 处在只读连接");
                    break;
                default:
            }
            //获取当前的连接状态
            isConnected = newState.isConnected();
        };

        /**
         * 构建客户端
         */
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        client = CuratorFrameworkFactory.builder()
                .connectString(connectString)
                .namespace(NAMESPACE)
                .sessionTimeoutMs(60000)
                .connectionTimeoutMs(5000)
                .retryPolicy(retryPolicy)
                .build();

        /**
         * 监听连接状态
         */
        client.getConnectionStateListenable().addListener(connectionStateListener);
        client.start();

        //使用计数器 来记录当前运行的节点数
        distributedAtomicInteger = new DistributedAtomicInteger(client, ID_NODE, new RetryNTimes(10, 10));
        //使用 treeCache 来监听节点数
        treeCache = new TreeCache(client, ID_NODE);
        try {
            treeCache.start();
        } catch (Exception e) {
            LOG.error("treeCache 启动失败", e);
        }
        lock = new InterProcessMutex(client, LOCK_NODE);
        genNode();

        if (isTestSessionTimeout) {
            testSessionTimeout();
        }

        if (isTestShutdown) {
            testShutdown();
        }
    }

    private static final int lock_wait = 10;
    private static final int max_reconnect_count = 5;
    private AtomicInteger reconnectCount = new AtomicInteger(0);

    private void reconnect() {
        if (reconnectCount.incrementAndGet() > max_reconnect_count) {
            reconnectCount.set(0);
            LOG.info("重连次数超过最大限制，重新获取节点");
            this.currentNodeID = null;
            genNode();
        }
        LOG.info("重连次数：" + reconnectCount.get());
        try {
            if (lock.acquire(lock_wait, TimeUnit.SECONDS)) {
                try {
                    client.create().withMode(CreateMode.EPHEMERAL).forPath(ID_NODE + "/" + currentNodeID);
                    reconnectCount.set(0);
                    LOG.info("重连获取节点号成功");
                    reconnecting = false;
                } catch (Exception e) {
                    LOG.warn("创建临时节点异常", e);
                    //重复步骤
                    Thread.sleep(1000);
                    reconnect();
                }
            }
        } catch (Exception e) {
            LOG.error("建立节点失败", e);
        } finally {
            if (lock.isAcquiredInThisProcess()) {
                try {
                    lock.release();
                } catch (Exception e) {
                    LOG.error("释放锁失败", e);
                }
            }
        }
    }

    /**
     * 获取节点号
     */
    private void genNode() {
        if (canPay()) {
            LOG.info("节点处于正常状态");
            return;
        }
        try {
            Thread.sleep(1000);
            if (lock.acquire(lock_wait, TimeUnit.SECONDS)) {
                //加锁成功
                Map<String, ChildData> children = treeCache.getCurrentChildren(ID_NODE);
                if (children == null) {
                    children = Maps.newHashMap();
                }
                //获取当前运行的最大节点数
                int recordNumber = getNumber(distributedAtomicInteger);
                int childNumber = children.size();

                if (childNumber < recordNumber) {
                    LOG.info("当前运行节点数 小于 记录的节点数。");
                    String nodeId = getNodeId(children, recordNumber);
                    LOG.info("尝试创建节点：{}", nodeId);

                    try {
                        String path = client.create().withMode(CreateMode.EPHEMERAL).forPath(ID_NODE + "/" + nodeId);
                        this.currentNodeID = ZKPaths.getNodeFromPath(path);
                        LOG.info("获取本节点 ID：{}", currentNodeID);
                    } catch (Exception e) {
                        LOG.warn("创建临时节点异常", e);

                        genNode();
                    }
                } else if (childNumber == recordNumber) {
                    //这个情况是，当前节点数不够，新增运行节点
                    LOG.info("当前运行节点数 等于 记录节点数。");
                    //先自增 计数器 可能成功，可能失败
                    distributedAtomicInteger.increment();
                    //重复这个步骤
                    genNode();
                } else {
                    //异常情况，可能是外部篡改了 计数器，暂时不处理，后面应该自动在程序里面自动修复这个情况
                    LOG.info("当前运行节点数 大于 记录节点数。");
                }
            } else {
                genNode();
            }
        } catch (Exception e) {
            LOG.error("建立节点失败", e);
        } finally {
            if (lock.isAcquiredInThisProcess()) {
                try {
                    lock.release();
                } catch (Exception e) {
                    LOG.error("释放锁失败", e);
                }
            }
        }
    }

    /**
     * 获取计数器的值
     */
    private int getNumber(DistributedAtomicInteger distributedAtomicInteger) {
        int result;
        while (true) {
            try {
                AtomicValue<Integer> value = distributedAtomicInteger.get();
                if (value.succeeded()) {
                    result = value.postValue();
                    break;
                } else {
                    LOG.warn("获取 Number 失败");
                }
            } catch (Exception e) {
                LOG.error("获取 Number 异常", e);
            }
        }
        return result;
    }

    /**
     * 生成节点号 node-n
     * n 从1 开始
     */
    private String getNodeId(Map<String, ChildData> children, int number) {
        String nodeId = null;
        int childSize;
        if (Objects.isNull(children)) {
            childSize = 0;
        } else {
            childSize = children.size();
        }
        if (childSize == 0) {
            return "node" + SEPARATOR + "1";
        }
        if (childSize < number) {
            LOG.info("当前计数器为:{}", number);
            List<Integer> collect = children.keySet().stream().filter(key -> key.contains(SEPARATOR)).map(key -> key.split(SEPARATOR)[1]).map(Integer::parseInt).sorted().collect(Collectors.toList());
            LOG.info("当前运行节点为:{}", Arrays.toString(collect.toArray()));
            int id = -1;
            for (int i = 1; i <= collect.size(); i++) {
                if (i != collect.get(i - 1)) {
                    id = i;
                    break;
                }
            }
            if (id == -1) {
                id = number;
            }
            nodeId = "node" + SEPARATOR + id;
        } else if (childSize == number) {
            nodeId = "node" + SEPARATOR + (++number);
        }
        return nodeId;
    }

    public void testSessionTimeout() {
        try {
            Thread.sleep(10000);
            LOG.info("测试 session timeout 过期重连");
            KillSession.kill(client.getZookeeperClient().getZooKeeper(), connectString);
        } catch (Exception e) {
        }
    }

    public void testShutdown() {
        try {
            Thread.sleep(15000);
            LOG.info("测试宕机");
            client.close();
        } catch (Exception e) {
        }
    }

}
