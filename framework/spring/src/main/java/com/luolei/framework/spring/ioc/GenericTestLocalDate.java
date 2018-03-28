package com.luolei.framework.spring.ioc;

import org.springframework.stereotype.Component;

import java.time.LocalDate;

/**
 * @author 罗雷
 * @date 2018/3/26 0026
 * @time 14:06
 */
@Component
public class GenericTestLocalDate implements GenericTest<LocalDate> {

    @Override
    public String hello(LocalDate obj) {
        return "hello " + obj.toString();
    }
}
