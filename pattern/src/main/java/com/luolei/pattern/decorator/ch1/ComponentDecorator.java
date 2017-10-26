package com.luolei.pattern.decorator.ch1;

/**
 * 构件装饰类：抽象装饰类
 *
 * @author luolei
 * @date 2017-03-30 11:24
 */
public class ComponentDecorator extends Component {
    private Component component;  //维持对抽象构件类型对象的引用

    //注入抽象构件类型的对象
    public ComponentDecorator(Component component) {
        this.component = component;
    }

    public void display() {
        component.display();
    }
}
