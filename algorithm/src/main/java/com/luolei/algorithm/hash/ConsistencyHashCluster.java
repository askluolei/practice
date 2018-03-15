package com.luolei.algorithm.hash;

import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.IntStream;

/**
 * hash 一致性算法
 * @author 罗雷
 * @date 2018/3/15 0015
 * @time 10:25
 */
public class ConsistencyHashCluster extends Cluster {

    private SortedMap<Long, Node> virNodes = new TreeMap<Long, Node>();

    private static final int VIR_NODE_COUNT = 512;

    private static final String SPLIT = "#";

    public ConsistencyHashCluster() {
        super();
    }

    @Override
    public void addNode(Node node) {
        this.nodes.add(node);
        IntStream.range(0, VIR_NODE_COUNT)
                .forEach(index -> {
                    long hash = hash(node.getIp() + SPLIT + index);
                    virNodes.put(hash, node);
                });
    }

    @Override
    public void removeNode(Node node) {
        nodes.removeIf(o -> node.getIp().equals(o.getIp()));
        IntStream.range(0, VIR_NODE_COUNT)
                .forEach(index -> {
                    long hash = hash(node.getIp() + SPLIT + index);
                    virNodes.remove(hash);
                });
    }

    @Override
    public Node get(String key) {
        long hash = hash(key);
        //这里是取 key 大于 hash的节点中最靠近的一个，如果大于 最后插入的key，代表hash超过最后一个点，因此从0开始
        SortedMap<Long, Node> subMap = hash >= virNodes.lastKey() ? virNodes.tailMap(0L) : virNodes.tailMap(hash);
        if (subMap.isEmpty()) {
            return null;
        }
        return subMap.get(subMap.firstKey());
    }
}
