package com.luolei.pattern.state.ch3;

/**
 * 所有的状态转换操作都由环境类Screen来实现，此时，环境类充当了状态管理器角色。如果需要增加新的状态，例如“八倍状态类”，
 * 需要修改环境类，这在一定程度上违背了“开闭原则”，但对其他状态类没有任何影响。
 *
 * @author luolei
 * @date 2017-03-31 13:56
 */
public class Client {
    public static void main(String args[]) {
        Screen screen = new Screen();
        screen.onClick();
        screen.onClick();
        screen.onClick();
    }
}
