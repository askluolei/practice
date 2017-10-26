package com.luolei.pattern.mediator.ch1;

/**
 * 抽象组件类：抽象同事类
 *
 * @author luolei
 * @date 2017-03-30 19:15
 */
public abstract class Component {
    protected Mediator mediator;

    public void setMediator(Mediator mediator) {
        this.mediator = mediator;
    }

    //转发调用
    public void changed() {
        mediator.componentChanged(this);
    }

    public abstract void update();
}
