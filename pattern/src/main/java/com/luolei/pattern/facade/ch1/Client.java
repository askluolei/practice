package com.luolei.pattern.facade.ch1;

/**
 * @author luolei
 * @date 2017-03-30 13:42
 */
public class Client {
    public static void main(String[] args) {
        Facade facade = new Facade();
        facade.Method();
    }
}
