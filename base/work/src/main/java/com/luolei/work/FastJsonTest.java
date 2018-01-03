package com.luolei.work;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 罗雷
 * @date 2018/1/3 0003
 * @time 10:53
 */
public class FastJsonTest {

    public static void main(String[] args) throws Exception {
        JSONObject root = JSON.parseObject(FastJsonTest.class.getClassLoader().getResourceAsStream("test.json"), null);
        String accNo = root.getJSONObject("header").getString("accNo");
        System.out.println(accNo);

        List<String> list = new ArrayList<>();
        allTexts(list, root);
        list.forEach(System.out::println);
    }

    public static void allTexts(List<String> texts, JSONObject root) {
        for (String key : root.keySet()) {
            Object o = root.get(key);
            if (o instanceof JSONObject) {
                allTexts(texts, (JSONObject) o);
            } else {
                texts.add(String.valueOf(o));
            }
        }
    }
}
