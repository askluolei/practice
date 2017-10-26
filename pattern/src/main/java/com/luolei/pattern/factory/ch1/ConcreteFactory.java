package com.luolei.pattern.factory.ch1;

/**
 * @author luolei
 * @date 2017-03-29 16:09
 */
public class ConcreteFactory implements Factory {
    public Product factoryMethod() {
        return new ConcreteProduct();
    }
}
