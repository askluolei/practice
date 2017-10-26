package com.luolei.pattern.simplefactory.ch2;

/**
 * @author luolei
 * @date 2017-03-29 15:38
 */
public abstract class Product {
    //所有产品类的公共业务方法
    public void methodSame() {
        //公共方法的实现
    }

    //声明抽象业务方法
    public abstract void methodDiff();
}
