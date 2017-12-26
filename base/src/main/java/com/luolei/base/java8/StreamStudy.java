package com.luolei.base.java8;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 罗雷
 * @date 2017/11/17 0017
 * @time 13:42
 */
public class StreamStudy {

    public static void testStream() {
        List<Integer> list = Arrays.asList(1,2,3,4,5,6);
        List<Integer> result = list.stream().filter(a -> a.equals(2)).collect(Collectors.toList());
        System.out.println(result.size());
        System.out.println(result.get(0));
    }

    public static void testIntegerEquals() {
        Integer a = 2;
        System.out.println(a.equals(2));
    }

    public static void main(String[] args) {
        testStream();
    }
}
