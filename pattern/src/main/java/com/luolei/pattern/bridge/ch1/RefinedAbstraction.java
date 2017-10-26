package com.luolei.pattern.bridge.ch1;

/**
 * @author luolei
 * @date 2017-03-30 10:39
 */
public class RefinedAbstraction extends Abstraction {
    public void operation() {
        //业务代码
        impl.operationImpl();  //调用实现类的方法
        //业务代码
    }
}
