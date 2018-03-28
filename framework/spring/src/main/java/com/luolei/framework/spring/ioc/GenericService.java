package com.luolei.framework.spring.ioc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 罗雷
 * @date 2018/3/26 0026
 * @time 14:09
 */
@Service
public class GenericService {

    @Autowired
    private GenericTest<String> test;

    public String hello(String greeting) {
        return test.hello(greeting);
    }
}
