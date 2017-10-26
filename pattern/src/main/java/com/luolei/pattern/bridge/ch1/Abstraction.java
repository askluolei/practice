package com.luolei.pattern.bridge.ch1;

/**
 * @author luolei
 * @date 2017-03-30 10:39
 */
public abstract class Abstraction {
    protected Implementor impl; //定义实现类接口对象

    public void setImpl(Implementor impl) {
        this.impl = impl;
    }

    public abstract void operation();  //声明抽象业务方法
}
