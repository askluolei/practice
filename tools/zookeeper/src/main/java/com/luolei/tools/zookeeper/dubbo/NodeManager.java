package com.luolei.tools.zookeeper.dubbo;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentMap;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessSemaphoreV2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;

public class NodeManager {

	private Logger log = LoggerFactory.getLogger(NodeManager.class);
	
	private static final String SEPARATOR = "-";
	private static final int max_reconnect_count = 3;
	private static final String SEMAPHORE_NODE = "/semaphore";
	private static final String ID_NODE = "/node";
	
	private ZookeeperClient zookeeperClient;
	private long sleepTime = 1000;
	
	private CuratorFramework curatorFramework;
	private volatile String currentNodeId = null;
	private boolean reconnect = false;
	private int reconnectCount = 0;
	private ConcurrentMap<String, InterProcessSemaphoreV2> semaphoreCache = Maps.newConcurrentMap();
	
	public NodeManager(String connectString) {
		URL url = new URL(connectString, "ebservice");
		this.zookeeperClient = new CuratorZookeeperClient(url);
		this.zookeeperClient.addStateListener(new StateListener() {
			
			@Override
			public void stateChanged(int connected) {
				switch (connected) {
				case StateListener.CONNECTED:
					log.info("zookeeper 连接成功");
					createParent();
					createNode();
					break;
				case StateListener.DISCONNECTED:
					log.info("zookeeper 连接丢失");
					break;
				case StateListener.RECONNECTED:
					log.info("zookeeper 重连成功");
					reconnect = true;
					createNode();
					break;
				default:
					break;
				}
			}
		});
		
		this.zookeeperClient.addChildListener(ID_NODE, new ChildListener() {
			
			@Override
			public void childChanged(String path, List<String> children) {
				log.info("子节点发生变化：" + children.size());
			}
		});
		// 这里拿到 curatorFramework 客户端句柄
		this.curatorFramework = ((CuratorZookeeperClient)this.zookeeperClient).getCuratorFramework();
	}
	
	public String getNodeId() {
		return this.currentNodeId;
	}
	
	private void createParent() {
		try {
			this.zookeeperClient.create(ID_NODE, "", false);
		} catch (Exception e) {
		}
	}
	
    /**
     * 获取分布式信号量
     * @param bankLoginId
     * @param maxSize 信号量的size
     * @return
     */
    public InterProcessSemaphoreV2 getSemaphore(String bankLoginId, int maxSize) {
        if (!Strings.isNullOrEmpty(bankLoginId)) {
            InterProcessSemaphoreV2 v2 = new InterProcessSemaphoreV2(curatorFramework, SEMAPHORE_NODE + "/" + bankLoginId, maxSize);
            semaphoreCache.putIfAbsent(bankLoginId, v2);
        }
        return semaphoreCache.get(bankLoginId);
    }
	
	/**
	 * 是否可以支付，要满足3个条件：
	 * 1. 有节点号
	 * 2. 与 zookeeper 正常连接
	 * 3. 不是处在重连中
	 * @return
	 */
	public boolean canPay() {
		return this.currentNodeId != null && this.zookeeperClient.isConnected() && !reconnect;
	}
	
	public ZookeeperClient getZookeeperClient() {
		return this.zookeeperClient;
	}
	
	public void close() {
		this.zookeeperClient.close();
	}
	
	private void createNode() {
		List<String> children = this.zookeeperClient.getChildren(ID_NODE);
		String currentNodeId = nodeId(children);
		log.info("计算当前节点应该使用的节点号为:{}", currentNodeId);
		try {
			this.zookeeperClient.create(ID_NODE + "/" +  currentNodeId, "", true);
			sleepTime = 1000;
			reconnectCount = 0;
			reconnect = false;
			this.currentNodeId = currentNodeId;
			log.info("创建成功:{}", this.currentNodeId);
		} catch (Exception e) {
			log.error("创建节点失败:" + currentNodeId, e);
			sleepQuietly();
			createNode();
		}
	}
	
	private void sleepQuietly() {
		try {
			log.info("sleep : {} 毫秒 后重试", sleepTime );
			Thread.sleep(sleepTime);
			sleepTime = sleepTime * 2;
		} catch (InterruptedException e) {
		}
	}
	
	private String nodeId(List<String> children) {
		if (reconnect && reconnectCount < max_reconnect_count) {
			log.info("重连次数 : {}, 尝试节点号 : {}", reconnectCount++, this.currentNodeId);
			return this.currentNodeId;
		}
		if (children == null || children.size() == 0) {
			log.info("当前没有节点");
			return "node" + SEPARATOR + 1;
		}
		log.info("当前拥有节点:{}", Arrays.toString(children.toArray()));
		Collections.sort(children);
		for (int i = 0; i < children.size(); i++) {
			int expectNumber = i + 1;
			int nodeNumber = getNodeNumber(children.get(i));
			if (nodeNumber != expectNumber) {
				return "node" + SEPARATOR + expectNumber;
			}
		}
		return "node" + SEPARATOR + (children.size() + 1);
	}
	
	private int getNodeNumber(String nodeName) {
		int index = nodeName.lastIndexOf("/");
		if (index > 0 && index < nodeName.length()) {
			nodeName = nodeName.substring(index + 1);
		}
		String[] pairs = nodeName.split(SEPARATOR);
		if (pairs.length != 2) {
			return -1;
		}
		try {
			return Integer.parseInt(pairs[1]);
		} catch (NumberFormatException e) {
			return -1;
		}
	}
}
