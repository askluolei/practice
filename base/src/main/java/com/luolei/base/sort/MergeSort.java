package com.luolei.base.sort;

import java.util.Random;

/**
 *
 * 作者：罗雷
 * 日期：2015年4月22日
 * 内容：归并排序
 */
public class MergeSort {
    public static <T extends Comparable<T>> T[] mergesort(T[] a){
        T[] temp=a.clone();
        a=msort(a,temp,0,a.length);
        return a;
    }

    public static <T extends Comparable<T>> T[] msort(T[] a,T[] temp,int first,int last){
        if(first+1<last){
            int mid=(first+last)/2;
            msort(a,temp,first,mid);
            msort(a,temp,mid,last);

            int index1=first;
            int index2=mid;
            int index3=first;
            while(index1<mid&&index2<last){
                if(a[index1].compareTo(a[index2])<0){
                    temp[index3]=a[index1];
                    index1++;
                }else{
                    temp[index3]=a[index2];
                    index2++;
                }
                index3++;
            }
            while(index1<mid){
                temp[index3++]=a[index1++];
            }
            while(index2<last){
                temp[index3++]=a[index2++];
            }
            for(int i=first;i<last;i++)
                a[i]=temp[i];

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
        Integer[] b=MergeSort.mergesort(a);
        for (int i = 0; i < b.length; i++) {
            System.out.print(b[i]+" ");
        }
    }

}
