package com.luolei.pattern.strategy.ch2;

/**
 * 儿童票折扣类：具体策略类
 *
 * @author luolei
 * @date 2017-03-31 14:55
 */
public class ChildrenDiscount implements Discount {
    public double calculate(double price) {
        System.out.println("儿童票：");
        return price - 10;
    }
}
