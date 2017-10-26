package com.luolei.pattern.bridge.ch2;

/**
 * Windows操作系统实现类：具体实现类
 *
 * @author luolei
 * @date 2017-03-30 10:42
 */
public class WindowsImp implements ImageImp {
    public void doPaint(Matrix m) {
        //调用Windows系统的绘制函数绘制像素矩阵
        System.out.print("在Windows操作系统中显示图像：");
    }
}
