package com.luolei.pattern.memento.ch2;

/**
 * 象棋棋子备忘录管理类：负责人
 *
 * @author luolei
 * @date 2017-03-30 19:48
 */
public class MementoCaretaker {
    private ChessmanMemento memento;

    public ChessmanMemento getMemento() {
        return memento;
    }

    public void setMemento(ChessmanMemento memento) {
        this.memento = memento;
    }
}
