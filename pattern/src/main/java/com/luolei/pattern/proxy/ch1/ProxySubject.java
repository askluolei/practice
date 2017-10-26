package com.luolei.pattern.proxy.ch1;

/**
 * @author luolei
 * @date 2017-03-30 14:41
 */
public class ProxySubject extends Subject {

    private RealSubject realSubject = new RealSubject(); //维持一个对真实主题对象的引用

    @Override
    public void Request() {
        preRequest();
        realSubject.Request(); //调用真实主题对象的方法
        postRequest();
    }

    public void preRequest() {

    }

    public void postRequest() {

    }
}
