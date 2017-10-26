package com.luolei.pattern.state.ch2;

/**
 * @author luolei
 * @date 2017-03-31 13:49
 */
public abstract class State {
    public abstract void on(Switch s);
    public abstract void off(Switch s);
}
