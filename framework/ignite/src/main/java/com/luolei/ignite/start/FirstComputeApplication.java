package com.luolei.ignite.start;

import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.apache.ignite.lang.IgniteCallable;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Ignite
 * 第一个 计算应用
 * 环境搭建：
 * 从官网下载 二进制包
 * 将 examples\config 目录下的 几个xml文件复制到 bin/config 目录下
 * 这里的config 是自己建的
 * 然后启动：./ignite config/example-ignite.xml
 *
 * 为啥要这样启动，因为程序里面的配置文件也是那几个xml文件
 * ignite 节点（服务端，客户端），只有相同版本号才能组成集群（注意这里的版本 2.2.0）
 * 还有注意一点，对等类加载配置，要一样
 * example-default.xml 配置文件里面的 <property name="peerClassLoadingEnabled" value="true"/>
 * 所以服务端节点启动的时候也要开这个配置
 *
 * @author 罗雷
 * @date 2017/11/7 0007
 * @time 16:49
 */
public class FirstComputeApplication {

    /**
     * 这里做的事情很简单
     * 就是将一个句子 通过 空格分割后，每个计算单元计算单词的长度 最后汇总
     * 至于这个计算是在哪里进行的，我也不知道（。。。）
     * 不过可以通过服务端启动的命令行那里看到确实有一个客户端节点连接过去了
     */
    public static void main(String[] args) {
        /**
         * 这里这是本节点为客户端节点
         */
        Ignition.setClientMode(true);
        try (Ignite ignite = Ignition.start("example-ignite.xml")) {
            Collection<IgniteCallable<Integer>> calls = new ArrayList<>();
            // Iterate through all the words in the sentence and create Callable jobs.
            for (final String word : "Count characters using callable".split(" ")) {
                calls.add(word::length);
            }
            // Execute collection of Callables on the grid.
            Collection<Integer> res = ignite.compute().call(calls);
            // Add up all the results.
            int sum = res.stream().mapToInt(Integer::intValue).sum();
            System.out.println("Total number of characters is '" + sum + "'.");
        }
    }
}
