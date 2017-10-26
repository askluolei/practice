package com.luolei.pattern.observer.ch2;

/**
 * 抽象观察类
 *
 * @author luolei
 * @date 2017-03-31 11:20
 */
public interface Observer {
    String getName();

    void setName(String name);

    void help(); //声明支援盟友方法

    void beAttacked(AllyControlCenter acc); //声明遭受攻击方法
}
