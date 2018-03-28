package com.luolei.framework.spring.ioc;

import org.springframework.stereotype.Component;

/**
 * @author 罗雷
 * @date 2018/3/26 0026
 * @time 14:05
 */
@Component
public class GenericTestString implements GenericTest<String> {

    @Override
    public String hello(String obj) {
        return "hello " + obj;
    }
}
