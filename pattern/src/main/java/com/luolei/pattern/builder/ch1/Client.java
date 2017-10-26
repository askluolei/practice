package com.luolei.pattern.builder.ch1;

/**
 * @author luolei
 * @date 2017-03-30 10:06
 */
public class Client {
    public static void main(String[] args) {
        Builder  builder = new ConcreteBuilder(); //可通过配置文件实现
        Director director = new  Director(builder);
        Product product = director.construct();
    }
}
