package com.luolei.pattern.simplefactory.ch3;

/**
 * @author luolei
 * @date 2017-03-29 15:44
 */
public class HistogramChart implements Chart {

    public HistogramChart() {
        System.out.println("创建柱状图！");
    }

    public void display() {
        System.out.println("显示柱状图！");
    }
}
