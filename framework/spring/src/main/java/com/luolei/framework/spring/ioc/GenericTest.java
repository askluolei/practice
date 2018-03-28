package com.luolei.framework.spring.ioc;

/**
 * @author 罗雷
 * @date 2018/3/26 0026
 * @time 14:05
 */
public interface GenericTest<T> {

    String hello(T obj);
}
