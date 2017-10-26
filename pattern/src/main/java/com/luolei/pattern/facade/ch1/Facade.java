package com.luolei.pattern.facade.ch1;

/**
 * @author luolei
 * @date 2017-03-30 13:42
 */
public class Facade {
    private SubSystemA obj1 = new SubSystemA();
    private SubSystemB obj2 = new SubSystemB();
    private SubSystemC obj3 = new SubSystemC();

    public void Method() {
        obj1.methodA();
        obj2.methodB();
        obj3.methodC();
    }
}
