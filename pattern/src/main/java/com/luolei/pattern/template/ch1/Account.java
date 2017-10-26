package com.luolei.pattern.template.ch1;

/**
 * @author luolei
 * @date 2017-03-31 15:38
 */
public abstract class Account {
    //基本方法——具体方法
    public boolean validate(String account, String password) {
        System.out.format("账号：%s", account);
        System.out.println();
        System.out.format("密码：%s", password);
        System.out.println();
        //模拟登录
        if (account.equals("张无忌") && password.equals("123456")) {
            return true;
        } else {
            return false;
        }
    }

    //基本方法——抽象方法
    public abstract void calculateInterest();

    //基本方法——具体方法
    public void display() {
        System.out.println("显示利息！");
    }

    //模板方法
    public void handle(String account, String password) {
        if (!validate(account, password)) {
            System.out.println("账户或密码错误！");
            return;
        }
        calculateInterest();
        display();
    }
}
