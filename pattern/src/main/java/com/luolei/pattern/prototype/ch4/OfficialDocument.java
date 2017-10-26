package com.luolei.pattern.prototype.ch4;

/**
 * @author luolei
 * @date 2017-03-30 9:18
 */
public interface OfficialDocument extends Cloneable {
    OfficialDocument clone();
    void display();
}
