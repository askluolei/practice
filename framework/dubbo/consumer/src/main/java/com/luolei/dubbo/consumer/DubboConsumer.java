package com.luolei.dubbo.consumer;

import com.luolei.dubbo.api.DemoService;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author 罗雷
 * @date 2017/11/7 0007
 * @time 16:18
 */
public class DubboConsumer {

    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[]{"dubbo-demo-consumer.xml"});
        context.start();

        DemoService demoService = context.getBean(DemoService.class); // 获取远程服务代理
        String hello = demoService.hello("world"); // 执行远程方法

        System.out.println(hello); // 显示调用结果
    }
}
