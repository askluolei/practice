package com.luolei.pattern.adapter.ch1;

/**
 * 抽象成绩操作类：目标接口
 *
 * @author luolei
 * @date 2017-03-30 10:23
 */
public interface ScoreOperation {
    int[] sort(int array[]); //成绩排序
    int search(int array[], int key); //成绩查找
}
