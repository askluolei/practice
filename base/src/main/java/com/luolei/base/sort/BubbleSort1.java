package com.luolei.base.sort;

import java.util.Random;

/**
 *
 * 作者：罗雷
 * 日期：2015年4月22日
 * 内容：改进冒泡排序算法1
 */
/*
 * 设置一标志性变量pos,用于记录每趟排序中最后一次进行交换的位置。
 * 由于pos位置之后的记录均已交换到位,
 * 故在进行下一趟排序时只要扫描到pos位置即可
 */
public class BubbleSort1 {
    public static <T extends Comparable<T>> T[] genericBubbleSortGai(T[] a){
        int n = a.length;
        int i = n - 1;
        while (i > 0) {
            int pos = 0;
            for (int j = 0; j < i; j++) {
                if (a[j].compareTo(a[j+1])>0) {
                    pos = j;
                    T temp = a[j];
                    a[j] = a[j + 1];
                    a[j + 1] = temp;
                }
            }
            i = pos;
        }
        return a;
    }

    public static void main(String[] args) {
        Random random=new Random(47);
        int n=20;
        Integer[] a=new Integer[n];
        for (int i = 0; i < a.length; i++) {
            a[i]=random.nextInt(100);
        }
        Integer[] b=BubbleSort1.genericBubbleSortGai(a);
        for (int i = 0; i < b.length; i++) {
            System.out.print(b[i]+" ");
        }
    }

}
