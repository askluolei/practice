package com.luolei.pattern.builder.ch1;

/**
 * @author luolei
 * @date 2017-03-30 10:06
 */
public class ConcreteBuilder extends Builder {

    @Override
    public void buildPartA() {
        product.setPartA("A");
    }

    @Override
    public void buildPartB() {
        product.setPartB("B");
    }

    @Override
    public void buildPartC() {
        product.setPartC("C");
    }
}
