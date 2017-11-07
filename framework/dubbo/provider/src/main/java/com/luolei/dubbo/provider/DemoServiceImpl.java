package com.luolei.dubbo.provider;

import com.luolei.dubbo.api.DemoService;

/**
 * @author 罗雷
 * @date 2017/11/7 0007
 * @time 16:14
 */
public class DemoServiceImpl implements DemoService {

    @Override
    public String hello(String name) {
        return "Hello " + name;
    }
}
