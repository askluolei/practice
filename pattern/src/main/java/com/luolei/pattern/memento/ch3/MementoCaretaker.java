package com.luolei.pattern.memento.ch3;

import com.luolei.pattern.memento.ch2.ChessmanMemento;

import java.util.ArrayList;

/**
 * 实现多次撤销
 *
 * @author luolei
 * @date 2017-03-30 19:51
 */
public class MementoCaretaker {
    //定义一个集合来存储多个备忘录
    private ArrayList mementolist = new ArrayList();

    public ChessmanMemento getMemento(int i) {
        return (ChessmanMemento) mementolist.get(i);
    }

    public void setMemento(ChessmanMemento memento) {
        mementolist.add(memento);
    }
}
