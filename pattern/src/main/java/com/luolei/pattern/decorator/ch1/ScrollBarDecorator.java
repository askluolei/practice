package com.luolei.pattern.decorator.ch1;

/**
 * 滚动条装饰类：具体装饰类
 *
 * @author luolei
 * @date 2017-03-30 11:25
 */
public class ScrollBarDecorator extends ComponentDecorator {
    public ScrollBarDecorator(Component component) {
        super(component);
    }

    public void display() {
        this.setScrollBar();
        super.display();
    }

    public void setScrollBar() {
        System.out.println("为构件增加滚动条！");
    }
}
