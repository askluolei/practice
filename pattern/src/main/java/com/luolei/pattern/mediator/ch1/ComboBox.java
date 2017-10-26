package com.luolei.pattern.mediator.ch1;

/**
 * 组合框类：具体同事类
 *
 * @author luolei
 * @date 2017-03-30 19:17
 */
public class ComboBox extends Component {
    public void update() {
        System.out.println("组合框增加一项：张无忌。");
    }

    public void select() {
        System.out.println("组合框选中项：小龙女。");
    }
}