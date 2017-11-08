package com.luolei.ignite.cluster;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCluster;
import org.apache.ignite.IgniteCompute;
import org.apache.ignite.Ignition;

/**
 * @author 罗雷
 * @date 2017/11/8 0008
 * @time 9:22
 */
public class ClusterStart02 {

    public static void main(String[] args) {
        try (Ignite ignite = Ignition.start("example-cluster.xml")) {
            /**
             * 这里做的事情是将一个任务广播到 所有远程节点
             *
             * 我这里的环境是 下载 ignite 二进制包
             * 启动两个节点 作为服务端节点
             *
             * 这里启动的是客户端节点
             * 这里看到这里启动后远程发现了这个客户端节点，并且执行任务 打印出 hello ***
             */
            IgniteCluster cluster = ignite.cluster();
            IgniteCompute compute = ignite.compute(cluster.forRemotes());
            compute.broadcast(() -> System.out.println("Hello Node: " + ignite.cluster().localNode().id()));
        }
    }
}
