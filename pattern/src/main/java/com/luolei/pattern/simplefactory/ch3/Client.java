package com.luolei.pattern.simplefactory.ch3;

/**
 * @author luolei
 * @date 2017-03-29 15:46
 */
public class Client {
    public static void main(String args[]) {
        Chart chart;
        chart = ChartFactory.getChart("histogram"); //通过静态工厂方法创建产品
        chart.display();
    }
}
