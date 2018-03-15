package com.luolei.algorithm.hash;

import java.util.Objects;
import java.util.stream.IntStream;

/**
 * @author 罗雷
 * @date 2018/3/15 0015
 * @time 9:59
 */
public class HashTest {

    private static final int DATA_COUNT = 100000;
    private static final String PRE_KEY = "CACHE_";

    public static void testNormalHash() {
        Cluster cluster = new NormalHashCluster();
        cluster.addNode(new Node("c1.yywang.info", "192.168.0.1"));
        cluster.addNode(new Node("c2.yywang.info", "192.168.0.2"));
        cluster.addNode(new Node("c3.yywang.info", "192.168.0.3"));
        cluster.addNode(new Node("c4.yywang.info", "192.168.0.4"));

        IntStream.range(0, DATA_COUNT)
                .forEach(index -> {
                    Node node = cluster.get(PRE_KEY + index);
                    node.put(PRE_KEY + index, "Test Data");
                });

        System.out.println("数据分布情况：");
        cluster.nodes.forEach(node -> {
            System.out.println("IP:" + node.getIp() + ",数据量:" + node.getData().size());
        });

        // 正常的取模找cache节点，当遇到新增节点和删除节点的时候，受影响的缓存除了当前的，其他所有节点都将受到影响
//        cluster.addNode(new Node("c5.yywang.info", "192.168.0.5"));
//        cluster.removeNode(new Node("c4.yywang.info", "192.168.0.4"));

        //缓存命中率
        long hitCount = IntStream.range(0, DATA_COUNT)
                .filter(index -> cluster.get(PRE_KEY + index).get(PRE_KEY + index) != null)
                .count();
        System.out.println("缓存命中率：" + hitCount * 1f / DATA_COUNT);
    }

    public static void testConsistencyHash() {
        Cluster cluster = new ConsistencyHashCluster();
        cluster.addNode(new Node("c1.yywang.info", "192.168.0.1"));
        cluster.addNode(new Node("c2.yywang.info", "192.168.0.2"));
        cluster.addNode(new Node("c3.yywang.info", "192.168.0.3"));
        cluster.addNode(new Node("c4.yywang.info", "192.168.0.4"));

        IntStream.range(0, DATA_COUNT)
                .forEach(index -> {
                    Node node = cluster.get(PRE_KEY + index);
                    node.put(PRE_KEY + index, "Test Data");
                });

        System.out.println("数据分布情况：");
        cluster.nodes.forEach(node -> {
            System.out.println("IP:" + node.getIp() + ",数据量:" + node.getData().size());
        });

        // 一致性hash算法，受影响的缓存只有当前节点以及临近节点，
//        cluster.addNode(new Node("c5.yywang.info", "192.168.0.5"));
//        cluster.removeNode(new Node("c4.yywang.info", "192.168.0.4"));

        //缓存命中率
        long hitCount = IntStream.range(0, DATA_COUNT)
                .filter(index -> cluster.get(PRE_KEY + index).get(PRE_KEY + index) != null )
                .count();
        System.out.println("缓存命中率：" + hitCount * 1f / DATA_COUNT);
    }

    public static void main(String[] args) {
//        testNormalHash();
        testConsistencyHash();
    }
}
