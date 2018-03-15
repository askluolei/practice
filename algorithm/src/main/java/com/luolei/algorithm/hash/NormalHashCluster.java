package com.luolei.algorithm.hash;

/**
 * 取模hash
 * @author 罗雷
 * @date 2018/3/15 0015
 * @time 10:11
 */
public class NormalHashCluster extends Cluster {

    public NormalHashCluster() {
        super();
    }

    @Override
    public void addNode(Node node) {
        this.nodes.add(node);
    }

    @Override
    public void removeNode(Node node) {
        this.nodes.removeIf(o -> o.getIp().equals(node.getIp()) ||
                o.getDomain().equals(node.getDomain()));
    }

    @Override
    public Node get(String key) {
        long hash = Math.abs(hash(key));
        long index =  hash % nodes.size();
        return nodes.get((int)index);
    }
}
