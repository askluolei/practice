package com.luolei.pattern.state.ch2;

/**
 * @author luolei
 * @date 2017-03-31 13:49
 */
public class OffState extends State {
    public void on(Switch s) {
        System.out.println("打开！");
        s.setState(Switch.getState("on"));
    }

    public void off(Switch s) {
        System.out.println("已经关闭！");
    }
}
