package com.luolei.pattern.composite.ch1;

/**
 * 文本文件类
 *
 * @author luolei
 * @date 2017-03-30 10:54
 */
public class TextFile {
    private String name;

    public TextFile(String name) {
        this.name = name;
    }

    public void killVirus() {
        //简化代码，模拟杀毒
        System.out.println("----对文本文件'" + name + "'进行杀毒");
    }
}
