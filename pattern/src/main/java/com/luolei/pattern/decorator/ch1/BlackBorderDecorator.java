package com.luolei.pattern.decorator.ch1;

/**
 * 黑色边框装饰类：具体装饰类
 *
 * @author luolei
 * @date 2017-03-30 11:25
 */
public class BlackBorderDecorator extends ComponentDecorator {
    public BlackBorderDecorator(Component component) {
        super(component);
    }

    public void display() {
        this.setBlackBorder();
        super.display();
    }

    public void setBlackBorder() {
        System.out.println("为构件增加黑色边框！");
    }
}
