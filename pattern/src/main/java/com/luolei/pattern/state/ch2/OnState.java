package com.luolei.pattern.state.ch2;

/**
 * @author luolei
 * @date 2017-03-31 13:49
 */
public class OnState extends State {
    public void on(Switch s) {
        System.out.println("已经打开！");
    }

    public void off(Switch s) {
        System.out.println("关闭！");
        s.setState(Switch.getState("off"));
    }
}
