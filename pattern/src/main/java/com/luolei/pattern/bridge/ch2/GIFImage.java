package com.luolei.pattern.bridge.ch2;

/**
 * GIF格式图像：扩充抽象类
 *
 * @author luolei
 * @date 2017-03-30 10:44
 */
public class GIFImage extends Image {
    public void parseFile(String fileName) {
        //模拟解析GIF文件并获得一个像素矩阵对象m;
        Matrix m = new Matrix();
        imp.doPaint(m);
        System.out.println(fileName + "，格式为GIF。");
    }
}
