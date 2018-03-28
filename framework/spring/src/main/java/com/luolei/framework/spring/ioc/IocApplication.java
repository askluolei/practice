package com.luolei.framework.spring.ioc;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author 罗雷
 * @date 2018/3/26 0026
 * @time 14:04
 */
public class IocApplication {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext("com.luolei.framework.spring.ioc");
        String[] names = annotationConfigApplicationContext.getBeanNamesForType(GenericTest.class);
        for (String name : names) {
            System.out.println(name);
        }
        GenericService genericService = annotationConfigApplicationContext.getBean(GenericService.class);
        System.out.println(genericService.hello("luolei"));
    }
}
