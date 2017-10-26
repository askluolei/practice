package com.luolei.pattern.prototype.ch2;

/**
 * @author luolei
 * @date 2017-03-30 9:11
 */
public class Attachment {
    private String name; //附件名

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void download() {
        System.out.println("下载附件，文件名为" + name);
    }
}
