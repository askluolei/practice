package com.luolei.pattern.flyweight.ch1;

/**
 * 围棋棋子类：抽象享元类
 *
 * @author luolei
 * @date 2017-03-30 13:53
 */
public abstract class IgoChessman {
    public abstract String getColor();

    public void display(Coordinates coord){
        System.out.println("棋子颜色：" + this.getColor() + "，棋子位置：" + coord.getX() + "，" + coord.getY() );
    }
}
