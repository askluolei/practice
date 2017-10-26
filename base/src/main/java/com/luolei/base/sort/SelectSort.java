package com.luolei.base.sort;

import java.util.Random;
/**
 *
 * 作者：罗雷
 * 日期：2015年4月22日
 * 内容：二元选择排序  每趟选出最大最小
 */
public class SelectSort {
    public static <T extends Comparable<T>> T[] genericselectionsort(T[] a){
        int n=a.length;
        for(int i=0;i<n/2;i++){
            int min=i;
            int max=i;
            int j=i+1;
            while(j<n-i){
                if(a[j].compareTo(a[min])<0)
                    min=j;
                if(a[j].compareTo(a[max])>0)
                    max=j;
                j++;
            }
            T temp=a[i];
            a[i]=a[min];
            a[min]=temp;
            temp=a[n-i-1];
            a[n-i-1]=a[max];
            a[max]=temp;
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
        Integer[] b=SelectSort.genericselectionsort(a);
        for (int i = 0; i < b.length; i++) {
            System.out.print(b[i]+" ");
        }
    }

}
