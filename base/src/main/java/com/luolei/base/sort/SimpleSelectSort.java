package com.luolei.base.sort;

import java.util.Random;

/**
 *
 * 作者：罗雷
 * 日期：2015年4月22日
 * 内容：简单选择排序
 */
public class SimpleSelectSort {
    public static <T extends Comparable<T>> T[] genericsimpleselectionsort(T[] a){
        for (int i = 0; i < a.length; i++) {
            int min = i;
            int j = i + 1;
            while (j < a.length) {
                if (a[j].compareTo(a[min])<0)
                    min = j;
                j++;
            }
            T temp = a[i];
            a[i] = a[min];
            a[min] = temp;
        }
        return a;
    }

    public static void main(String[] args) {
        Random random=new Random(47);
        int n=20;
        Integer[] a=new Integer[n];
        for (int i = 0; i < a.length; i++) {
            a[i]=random.nextInt(100);
            System.out.print(a[i]+" ");
        }
        System.out.println();
        Integer[] b=SimpleSelectSort.genericsimpleselectionsort(a);
        for (int i = 0; i < b.length; i++) {
            System.out.print(b[i]+" ");
        }
    }

}
