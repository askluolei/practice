package com.luolei.pattern.memento.ch1;

/**
 * 备忘录类，默认可见性，包内可见
 *
 * @author luolei
 * @date 2017-03-30 19:45
 */
class Memento {
    private String state;

    public Memento(Originator o) {
state = o.getState();
    }

    public void setState(String state) {
        this.state=state;
    }

    public String getState() {
        return this.state;
    }
}
