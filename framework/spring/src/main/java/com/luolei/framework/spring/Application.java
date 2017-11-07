package com.luolei.framework.spring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author 罗雷
 * @date 2017/11/6 0006
 * @time 16:36
 */
public class Application {

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("bean01.xml");
        SimpleDateFormat format = context.getBean(SimpleDateFormat.class);
        System.out.println(format.format(new Date()));
    }
}
