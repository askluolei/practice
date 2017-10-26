package com.luolei.pattern.singleton.ch3;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author luolei
 * @date 2017-03-29 17:41
 */
public enum  LoadBalancer {
    INSTANCE
    ;
    //服务器集合
    private List<String> serverList = null;

    //私有构造函数
    LoadBalancer() {
        serverList = new ArrayList<>();
    }

    //增加服务器
    public void addServer(String server) {
        serverList.add(server);
    }

    //删除服务器
    public void removeServer(String server) {
        serverList.remove(server);
    }

    //使用Random类随机获取服务器
    public String getServer() {
        Random random = new Random();
        int i = random.nextInt(serverList.size());
        return (String)serverList.get(i);
    }
}
