package com.luolei.pattern.observer.ch1;

/**
 * @author luolei
 * @date 2017-03-31 11:19
 */
public class ConcreteSubject extends Subject {
    //实现通知方法
    public void notifyObserver() {
        //遍历观察者集合，调用每一个观察者的响应方法
        for(Object obs:observers) {
            ((Observer)obs).update();
        }
    }
}
