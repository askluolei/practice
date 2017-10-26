package com.luolei.pattern.template.ch1;

/**
 * 定期账户类，充当具体子类
 *
 * @author luolei
 * @date 2017-03-31 15:42
 */
public class SavingAccount extends Account {
    @Override
    public void calculateInterest() {
        System.out.println("按定期利率计算利息");
    }
}
