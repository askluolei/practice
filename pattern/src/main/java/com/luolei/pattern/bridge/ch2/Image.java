package com.luolei.pattern.bridge.ch2;

/**
 * 抽象图像类：抽象类
 *
 * @author luolei
 * @date 2017-03-30 10:41
 */
public abstract class Image {
    protected ImageImp imp;

    public void setImageImp(ImageImp imp) {
        this.imp = imp;
    }

    public abstract void parseFile(String fileName);
}
