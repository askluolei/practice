package com.luolei.pattern.simplefactory.ch2;

/**
 * @author luolei
 * @date 2017-03-29 15:41
 */
public class Client {
    public static void main(String args[]) {
        Product product;
        product = Factory.getProduct("A"); //通过工厂类创建产品对象
        product.methodSame();
        product.methodDiff();
    }
}
