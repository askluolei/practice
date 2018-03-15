package com.luolei.algorithm.hash;

import cn.hutool.core.util.HashUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author 罗雷
 * @date 2018/3/15 0015
 * @time 10:10
 */
public abstract class Cluster {

    protected List<Node> nodes;

    public Cluster() {
        this.nodes = new ArrayList<>();
    }

    public abstract void addNode(Node node);

    public abstract void removeNode(Node node);

    public abstract Node get(String key);

    public long hash(String key) {
        return Math.abs(HashUtil.fnvHash(key));
    }
}
