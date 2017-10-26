package com.luolei.pattern.template.ch1;

/**
 * 活期账户类，充当具体子类。
 *
 * @author luolei
 * @date 2017-03-31 15:42
 */
public class CurrentAccount extends Account {
    @Override
    public void calculateInterest() {
        System.out.println("按活期利率计算利息");
    }
}
