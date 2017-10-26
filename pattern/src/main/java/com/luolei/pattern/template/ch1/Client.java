package com.luolei.pattern.template.ch1;

/**
 * @author luolei
 * @date 2017-03-31 15:43
 */
public class Client {
    public static void main(String[] args) {
        Account account = new CurrentAccount();
        account.handle("张无忌", "123456");
    }
}
