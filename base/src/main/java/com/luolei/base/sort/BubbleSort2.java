package com.luolei.base.sort;

import java.util.Random;

/**
 *
 * 作者：罗雷
 * 日期：2015年4月22日
 * 内容：冒泡排序改进算法2
 */
/*
 * 两边同时进行  先找最大的  然后找最小的
 */
public class BubbleSort2 {
    public static <T extends Comparable<T>> T[] genericBubbleSortGai2(T[] a){
        int n=a.length;
        int low=0,high=n-1;
        int j;
        T tmp;
        while(low<high){
            for(j=low;j<high;j++){
                if(a[j].compareTo(a[j+1])>0){
                    tmp=a[j];
                    a[j]=a[j+1];
                    a[j+1]=tmp;
                }
            }
            high--;
            for(j=high;j>low;j--){
                if(a[j].compareTo(a[j-1])<0){
                    tmp=a[j];
                    a[j]=a[j-1];
                    a[j-1]=tmp;
                }
            }
            low++;
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
        Integer[] b=BubbleSort2.genericBubbleSortGai2(a);
        for (int i = 0; i < b.length; i++) {
            System.out.print(b[i]+" ");
        }
    }

}
