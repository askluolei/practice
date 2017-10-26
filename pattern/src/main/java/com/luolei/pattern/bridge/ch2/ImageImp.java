package com.luolei.pattern.bridge.ch2;

/**
 * 抽象操作系统实现类：实现类接口
 *
 * @author luolei
 * @date 2017-03-30 10:41
 */
public interface ImageImp {
    void doPaint(Matrix m);  //显示像素矩阵m
}
