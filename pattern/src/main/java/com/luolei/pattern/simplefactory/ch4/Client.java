package com.luolei.pattern.simplefactory.ch4;

import com.luolei.design.simplefactory.ch3.Chart;
import com.luolei.design.simplefactory.ch3.ChartFactory;

/**
 * 方案的改进
 Sunny软件公司开发人员发现在创建具体Chart对象时，每更换一个Chart对象都需要修改客户端代码中静态工厂方法的参数，客户端代码将要重新编译，这对于客户端而言，违反了“开闭原则”，有没有一种方法能够在不修改客户端代码的前提下更换具体产品对象呢？答案是肯定的，下面将介绍一种常用的实现方式。
 我们可以将静态工厂方法的参数存储在XML或properties格式的配置文件中
 再通过一个工具类XMLUtil来读取配置文件中的字符串参数
 不难发现，在上述客户端代码中不包含任何与具体图表对象相关的信息，如果需要更换具体图表对象，只需修改配置文件config.xml，无须修改任何源代码，符合“开闭原则”。

 思考
 在简单工厂模式中增加新的具体产品时是否符合“开闭原则”？如果不符合，原有系统需作出哪些修改？


 * @author luolei
 * @date 2017-03-29 15:53
 */
public class Client {
    public static void main(String args[]) {
        Chart chart;
        String type = XMLUtil.getChartType(); //读取配置文件中的参数
        chart = ChartFactory.getChart(type); //创建产品对象
        chart.display();
    }
}
