package com.luolei.pattern.interpreter.ch2;

/**
 * 抽象节点类：抽象表达式
 *
 * @author luolei
 * @date 2017-03-30 16:20
 */
public abstract class Node {
    public abstract void interpret(Context text); //声明一个方法用于解释语句

    public abstract void execute(); //声明一个方法用于执行标记对应的命令
}
