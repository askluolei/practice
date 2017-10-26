package com.luolei.pattern.composite.ch1;

/**
 * 图像文件类
 *
 * @author luolei
 * @date 2017-03-30 10:53
 */
public class ImageFile {
    private String name;

    public ImageFile(String name) {
        this.name = name;
    }

    public void killVirus() {
        //简化代码，模拟杀毒
        System.out.println("----对图像文件'" + name + "'进行杀毒");
    }
}
