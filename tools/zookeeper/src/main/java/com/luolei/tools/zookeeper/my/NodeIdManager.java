package com.luolei.tools.zookeeper.my;

import com.google.common.collect.Lists;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.BackgroundCallback;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.framework.recipes.atomic.AtomicValue;
import org.apache.curator.framework.recipes.atomic.DistributedAtomicInteger;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.framework.state.ConnectionState;
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
import java.util.stream.Collectors;

/**
 * @author 罗雷
 * @date 2017/11/2 0002
 * @time 13:53
 */
public class NodeIdManager implements Runnable {
    private static final String separator = "-";
    private static final String namespace = "ebservice";
    private static final String idNode = "/node";
    private static Logger logger = LoggerFactory.getLogger(NodeIdManager.class);

    private CuratorFramework client;
    private TreeCache treeCache;
    private DistributedAtomicInteger distributedAtomicInteger;

    private final String connectString;

    /**
     * 与 zookeeper 的连接状态
     */
    private volatile boolean isConnected = false;

    private volatile String currentNodeID = null;

    public boolean canPay() {
        return isConnected && currentNodeID != null;
    }

    public String getCurrentNodeID() {
        return currentNodeID;
    }

    public NodeIdManager(String connectString) {
        this.connectString = connectString;
    }

    @Override
    public void run() {
        ConnectionStateListener connectionStateListener = new ConnectionStateListener() {
            @Override
            public void stateChanged(CuratorFramework client, ConnectionState newState) {
                switch (newState) {
                    case CONNECTED:
                        logger.info("与 zookeeper 连接成功");
                        break;
                    case LOST:
                        logger.warn("与 zookeeper 连接丢失");
                        currentNodeID = null;
                        break;
                    case RECONNECTED:
                        logger.info("与 zookeeper 重连成功");
                        genNode();
                        break;
                    case SUSPENDED:
                        logger.info("与 zookeeper 连接阻塞");
                        break;
                    case READ_ONLY:
                        logger.info("与 zookeeper 处在只读连接");
                        break;
                    default:
                }
                //获取当前的连接状态
                isConnected = newState.isConnected();
            }
        };
        //构建客户端
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        client = CuratorFrameworkFactory.builder()
                .connectString(connectString)
                .namespace(namespace)
                .sessionTimeoutMs(60000)
                .connectionTimeoutMs(5000)
                .retryPolicy(retryPolicy)
                .build();

        //添加 连接状态监听
        client.getConnectionStateListenable().addListener(connectionStateListener);
        client.start();
        //使用计数器 来记录当前运行的节点数
        distributedAtomicInteger = new DistributedAtomicInteger(client, idNode, new RetryNTimes(10, 10));
        //使用 treeCache 来监听节点数
        treeCache = new TreeCache(client, idNode);
        try {
            treeCache.start();
        } catch (Exception e) {
            logger.error("treeCache 启动失败", e);
        }

        genNode();
        try {
            Thread.sleep(5000);
            KillSession.kill(client.getZookeeperClient().getZooKeeper(), this.connectString);
        } catch (Exception e) {

        }
    }

    private void genNode() {
        try {
            //获取当前运行的最大节点数
            int recordNumber = getNumber(distributedAtomicInteger);
            int childNumber = getChildNumber(treeCache);

            if (childNumber < recordNumber) {
                //这个情况是有节点宕机了，新起节点将接替之前节点的工作
                logger.info("当前运行节点数 小于 记录的节点数。");
                String nodeId = getNodeId(treeCache, recordNumber);
                logger.info("尝试创建节点：{}", nodeId);
                try {
                    String path = client.create().withMode(CreateMode.EPHEMERAL).forPath(idNode + "/" + nodeId);
                    this.currentNodeID = ZKPaths.getNodeFromPath(path);
                    logger.info("获取本节点 ID：{}", currentNodeID);
                } catch (Exception e) {
                    logger.warn("创建临时节点异常", e);
                    //重复步骤
                    genNode();
                }
            } else if (childNumber == recordNumber) {
                //这个情况是，当前节点数不够，新增运行节点
                logger.info("当前运行节点数 等于 记录节点数。");
                //先自增 计数器 可能成功，可能失败
                distributedAtomicInteger.increment();
                //重复这个步骤
                genNode();
            } else {
                //异常情况，可能是外部篡改了 计数器，暂时不处理，后面应该自动在程序里面自动修复这个情况
                logger.info("当前运行节点数 大于 记录节点数。");
            }
        } catch (Exception e) {
            logger.error("创建节点异常", e);
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
                    logger.warn("获取 Number 失败");
                }
            } catch (Exception e) {

            }
        }
        return result;
    }

    /**
     * 获取 子节点数量（当前运行节点数）
     */
    private int getChildNumber(TreeCache treeCache) {
        Map<String, ChildData> children = treeCache.getCurrentChildren(idNode);
        if (Objects.isNull(children)) {
            return 0;
        } else {
            return children.size();
        }
    }

    /**
     * 生成节点号 node-n
     * n 从1 开始
     */
    private String getNodeId(TreeCache treeCache, int number) {
        String nodeId = null;
        Map<String, ChildData> children = treeCache.getCurrentChildren(idNode);
        int childSize;
        if (Objects.isNull(children)) {
            childSize = 0;
        } else {
            childSize = children.size();
        }
        if (childSize == 0) {
            return "node" + separator + "1";
        }
        if (childSize < number) {
            logger.info("当前计数器为:{}", number);
            List<Integer> collect = children.keySet().stream().filter(key -> key.contains(separator)).map(key -> key.split(separator)[1]).map(Integer::parseInt).sorted().collect(Collectors.toList());
            logger.info("当前运行节点为:{}", Arrays.toString(collect.toArray()));
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
            nodeId = "node" + separator + id;
        } else if (childSize == number) {
            nodeId = "node" + separator + (++number);
        }
        return nodeId;
    }
}
