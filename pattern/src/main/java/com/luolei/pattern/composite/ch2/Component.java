package com.luolei.pattern.composite.ch2;

/**
 * 一般将抽象构件类设计为接口或抽象类，将所有子类共有方法的声明和实现放在抽象构件类中
 *
 * @author luolei
 * @date 2017-03-30 10:59
 */
public abstract class Component {
    public abstract void add(Component c); //增加成员
    public abstract void remove(Component c); //删除成员
    public abstract Component getChild(int i); //获取成员
    public abstract void operation();  //业务方法
}
