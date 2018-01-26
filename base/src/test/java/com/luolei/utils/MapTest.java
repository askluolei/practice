package com.luolei.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.junit.Test;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

/**
 * @author 罗雷
 * @date 2017/10/30 0030 15:13
 */
public class MapTest {

    @Test(expected = Exception.class)
    public void test() {
        Map<String, String> map = new Hashtable<>();
        map.put("key1", null);
    }

    @Test
    public void test2() {
        Map<String, String> map = new HashMap<>();
        map.put("key1", null);
    }

    @Test
    public void test3() {
        Map<String, String> map = new Hashtable<>();
        map.put(null, "");
    }

    static class User {
        private final String username;
        private final String password;

        public User(String username, String password) {
            this.username = username;
            this.password = password;
        }

        public String getUsername() {
            return username;
        }

        public String getPassword() {
            return password;
        }
    }

    @Test
    public void test4() {
        User u1 = new User("user1", "password1");
        User u2 = new User("user2", "password2");

        List<User> users = Arrays.asList(u1, u2);
        //使用 stream 流获取 map 的时候 value不能为null，虽然返回 的 map是hashMap ，toMap 里面的限制代码如下
        /*
         return (map, element) -> {
            K k = keyMapper.apply(element);
            V v = Objects.requireNonNull(valueMapper.apply(element));
            V u = map.putIfAbsent(k, v);
            if (u != null) throw duplicateKeyException(k, u, v);
        };
         */
        Map<String, String> map = users.stream().collect(Collectors.toMap(User::getUsername, User::getPassword));
        System.out.println(map.getClass().getName());
    }

    @Test
    public void test5() {
        Map<Integer, String> map = new HashMap<>();
        Integer one = new Integer(1);
        Integer two = new Integer(2);
        map.put(one, "1");
        map.put(two, "2");
        System.out.println(map.size());
        System.out.println(map.containsKey((Integer) 1));
    }

    @Test
    public void test6() {
        ConcurrentMap<String, Boolean> map = new ConcurrentHashMap<>();
        map.put("123", false);
        map.put("123", true);

        System.out.println(map.get("123"));
    }

}
