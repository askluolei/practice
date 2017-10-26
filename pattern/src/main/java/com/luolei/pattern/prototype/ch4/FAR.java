package com.luolei.pattern.prototype.ch4;

/**
 * @author luolei
 * @date 2017-03-30 9:20
 */
public class FAR implements OfficialDocument {
    public OfficialDocument clone() {
        OfficialDocument far = null;
        try {
            far = (OfficialDocument) super.clone();
        } catch (CloneNotSupportedException e) {
            System.out.println("不支持复制！");
        }
        return far;
    }

    public void display() {
        System.out.println("《可行性分析报告》");
    }
}
