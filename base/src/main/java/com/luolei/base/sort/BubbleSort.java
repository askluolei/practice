package com.luolei.base.sort;

import java.util.Random;

/**
 *
 * 作者：罗雷
 * 日期：2015年4月22日
 * 内容：冒泡排序算法
 */
public class BubbleSort {
    public static <T extends Comparable<T>> T[] genericBubbleSort(T[] a){
        int n=a.length;
        for(int i=0;i<n-1;i++){
            for(int j=0;j<n-i-1;j++){
                if(a[j].compareTo(a[j+1])>0){
                    T temp=a[j];
                    a[j]=a[j+1];
                    a[j+1]=temp;
                }
            }
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
        Integer[] b=BubbleSort.genericBubbleSort(a);
        for (int i = 0; i < b.length; i++) {
            System.out.print(b[i]+" ");
        }
    }
}
