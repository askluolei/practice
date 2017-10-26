package com.luolei.pattern.composite.ch3;

/**
 * 抽象文件类：抽象构件
 * @author luolei
 * @date 2017-03-30 11:07
 */
public abstract class AbstractFile {
    public abstract void add(AbstractFile file);
    public abstract void remove(AbstractFile file);
    public abstract AbstractFile getChild(int i);
    public abstract void killVirus();
}
