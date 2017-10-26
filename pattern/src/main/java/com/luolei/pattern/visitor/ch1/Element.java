package com.luolei.pattern.visitor.ch1;

/**
 * @author luolei
 * @date 2017-03-31 15:54
 */
public interface Element {
    void accept(Visitor visitor);
}
