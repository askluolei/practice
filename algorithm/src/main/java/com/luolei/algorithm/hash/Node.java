package com.luolei.algorithm.hash;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 罗雷
 * @date 2018/3/15 0015
 * @time 10:09
 */
@Data
public class Node {

    private String domain;
    private String ip;
    private Map<String, Object> data;

    public Node(String domain, String ip) {
        this.domain = domain;
        this.ip = ip;
        this.data = new HashMap<>();
    }

    public void put(String key, String value) {
        data.put(key, value);
    }

    public void remove(String key){
        data.remove(key);
    }

    public <T> T get(String key) {
        return (T) data.get(key);
    }
}
