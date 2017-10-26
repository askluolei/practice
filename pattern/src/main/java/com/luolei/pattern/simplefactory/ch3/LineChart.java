package com.luolei.pattern.simplefactory.ch3;

/**
 * @author luolei
 * @date 2017-03-29 15:45
 */
public class LineChart implements Chart {
    public LineChart() {
        System.out.println("创建折线图！");
    }

    public void display() {
        System.out.println("显示折线图！");
    }
}
