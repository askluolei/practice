package com.luolei.base.sort;

import java.util.Random;

/**
 *
 * 作者：罗雷
 * 日期：2015年4月22日
 * 内容：插入排序
 */
public class InsertSort {
    public static <T extends Comparable<T>> T[] genericInsertSort(T[] a){
        for (int i = 1; i < a.length; i++) {
            int j=i;
            T temp=a[j];
            while(j>0){
                if(temp.compareTo(a[j-1])<0){
                    a[j]=a[j-1];
                    j--;
                }else{
                    break;
                }
            }
            a[j]=temp;
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
        Integer[] b=InsertSort.genericInsertSort(a);
        for (int i = 0; i < b.length; i++) {
            System.out.print(b[i]+" ");
        }
    }

}
