package com.luolei.ignite.cluster;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCluster;
import org.apache.ignite.IgniteCompute;
import org.apache.ignite.Ignition;
import org.apache.ignite.cluster.ClusterGroup;
import org.apache.ignite.cluster.ClusterMetrics;
import org.apache.ignite.cluster.ClusterNode;

import java.util.Collection;

/**
 * 集群相关
 *
 * 集群的功能是通过IgniteCluster接口提供的
 *
 通过IgniteCluster接口可以：
 1. 启动和停止一个远程集群节点；
 2. 获取集群成员的列表；
 3. 创建逻辑集群组；

 *
 * @author 罗雷
 * @date 2017/11/8 0008
 * @time 9:13
 */
public class ClusterStart {

    public static void main(String[] args) {
        try (Ignite ignite = Ignition.start("example-cluster.xml")) {
            /**
             * 这里获取整个集群的信息
             */
            IgniteCluster cluster = ignite.cluster();

            /**
             * 这里可以根据配置的集群属性来获取 逻辑集群组
             */
            ClusterGroup workers = ignite.cluster().forAttribute("ROLE", "worker");
            Collection<ClusterNode> nodes = workers.nodes();


            /**
             * 这里获取本地节点
             * 获取本地节点的指标数据
             */
            ClusterNode localNode = cluster.localNode();
            ClusterMetrics metrics = localNode.metrics();
            double cpuLoad = metrics.getCurrentCpuLoad();
            long usedHeap = metrics.getHeapMemoryUsed();
            int numberOfCores = metrics.getTotalCpus();
            int activeJobs = metrics.getCurrentActiveJobs();
        }
    }
}
