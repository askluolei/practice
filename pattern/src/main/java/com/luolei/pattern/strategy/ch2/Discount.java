package com.luolei.pattern.strategy.ch2;

/**
 * 折扣类：抽象策略类
 *
 * @author luolei
 * @date 2017-03-31 14:54
 */
public interface Discount {
    double calculate(double price);
}
