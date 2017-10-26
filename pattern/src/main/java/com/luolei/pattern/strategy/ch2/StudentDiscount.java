package com.luolei.pattern.strategy.ch2;

/**
 * 学生票折扣类：具体策略类
 *
 * @author luolei
 * @date 2017-03-31 14:55
 */
public class StudentDiscount implements Discount {
    public double calculate(double price) {
        System.out.println("学生票：");
        return price * 0.8;
    }
}
