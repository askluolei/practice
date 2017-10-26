package com.luolei.pattern.builder.ch1;

/**
 * @author luolei
 * @date 2017-03-30 10:04
 */
public abstract class Builder {

    //创建产品对象
    protected  Product product=new Product();

    public  abstract void buildPartA();
    public  abstract void buildPartB();
    public  abstract void buildPartC();

    //返回产品对象
    public  Product getResult() {
        return  product;
    }
}
