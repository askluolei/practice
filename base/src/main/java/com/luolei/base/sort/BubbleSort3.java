package com.luolei.base.sort;

import java.util.Random;

/**
 *
 * 作者：罗雷
 * 日期：2015年4月22日
 * 内容：冒泡排序改进算法3
 */
/*
 * 设置一个标志  如果有一趟没有发生交换  则排序完成
 */
public class BubbleSort3 {
    public static <T extends Comparable<T>> T[] genericbubblesortgai3(T[] a){
        int n=a.length;
        boolean flag=true;
        for(int i=0;i<n-1;i++){
            if(!flag)
            {
                return a;
            }
            flag=false;
            for(int j=0;j<n-i-1;j++){
                if(a[j].compareTo(a[j+1])>0){
                    flag=true;
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
        Integer[] b=BubbleSort3.genericbubblesortgai3(a);
        for (int i = 0; i < b.length; i++) {
            System.out.print(b[i]+" ");
        }
    }

}
