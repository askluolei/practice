package com.luolei.pattern.visitor.ch2;

/**
 * 部门类：抽象访问者类
 *
 * @author luolei
 * @date 2017-03-31 15:58
 */
public abstract class Department {
    //声明一组重载的访问方法，用于访问不同类型的具体元素
    public abstract void visit(FulltimeEmployee employee);

    public abstract void visit(ParttimeEmployee employee);
}
