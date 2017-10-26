package com.luolei.pattern.state.ch1;

/**
 * 抽象状态类
 *
 * @author luolei
 * @date 2017-03-31 13:37
 */
public abstract class AccountState {
    protected Account acc;

    public abstract void deposit(double amount);

    public abstract void withdraw(double amount);

    public abstract void computeInterest();

    public abstract void stateCheck();
}