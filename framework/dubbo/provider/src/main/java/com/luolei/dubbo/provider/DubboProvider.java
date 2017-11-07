package com.luolei.dubbo.provider;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author 罗雷
 * @date 2017/11/7 0007
 * @time 16:14
 */
public class DubboProvider {

    public static void main(String[] args) throws Exception {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[]{"dubbo-demo-provider.xml"});
        context.start();

        System.in.read(); // 按任意键退出
    }
}
