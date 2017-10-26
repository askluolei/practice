package com.luolei.pattern.strategy.ch2;

/**
 * VIP会员票折扣类：具体策略类
 *
 * @author luolei
 * @date 2017-03-31 14:55
 */
public class VIPDiscount implements Discount {
    public double calculate(double price) {
        System.out.println("VIP票：");
        System.out.println("增加积分！");
        return price * 0.5;
    }
}
