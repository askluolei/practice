package com.luolei.pattern.factory.ch1;

/**
 * @author luolei
 * @date 2017-03-29 16:10
 */
public class Client {
    public static void main(String[] args) {
        Factory factory;
        factory = new ConcreteFactory(); //可通过配置文件实现
        Product product;
        product = factory.factoryMethod();
    }
}
