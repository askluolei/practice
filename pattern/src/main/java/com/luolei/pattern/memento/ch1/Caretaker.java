package com.luolei.pattern.memento.ch1;

/**
 * @author luolei
 * @date 2017-03-30 19:46
 */
public class Caretaker {
    private Memento memento;

    public Memento getMemento() {
        return memento;
    }

    public void setMemento(Memento memento) {
        this.memento=memento;
    }
}
