package com.luolei.pattern.proxy.ch1;

/**
 * @author luolei
 * @date 2017-03-30 14:41
 */
public class RealSubject extends Subject{
    @Override
    public void Request() {
        System.out.println("real Subject");
    }
}
