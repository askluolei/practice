package com.luolei.pattern.visitor.ch2;

/**
 * 员工类：抽象元素类
 *
 * @author luolei
 * @date 2017-03-31 15:57
 */
public interface Employee {
    void accept(Department handler); //接受一个抽象访问者访问
}
