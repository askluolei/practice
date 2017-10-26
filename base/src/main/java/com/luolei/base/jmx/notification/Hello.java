package com.luolei.base.jmx.notification;

/**
 * Describe : MBean 的实现类
 * @author luolei
 * @email askluolei@gmail.com
 * @date 2017/10/26 23:18
 */
public class Hello implements HelloMBean {

    private String name;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void printHello() {
        System.out.println("Hello : " + name);
    }

    @Override
    public void printHello(String whoName) {
        System.out.println("Hello : " + whoName);
    }
}
