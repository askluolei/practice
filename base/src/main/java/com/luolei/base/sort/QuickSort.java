package com.luolei.base.sort;

import java.util.Random;
/**
 *
 * 作者：罗雷
 * 日期：2015年4月22日
 * 内容：快速排序
 */
public class QuickSort {
    /*
     * <快速排序> 基本思想是：通过一趟排序将要排序的数据分割成独立的两部分，其中一部分的所有数据都比另外一部分的所有数据都要小，
     * 然后再按此方法对这两部分数据分别进行快速排序，整个排序过程可以递归进行，以此达到整个数据变成有序序列。快速排序是一种不稳定的排序算法
     */
    public static <T extends Comparable<T>> T[] QuickSortStart(T[] list) {
        quickSort(list, 0, list.length-1);
        return list;
    }

    private static <T extends Comparable<T>> void quickSort(T[] list, int first, int last) {
        if (last > first) {
            int povitIndex=partition(list,first,last);
            quickSort(list,first,povitIndex-1);
            quickSort(list,povitIndex+1,last);
        }
    }

    private static <T extends Comparable<T>> int partition(T[] list, int first, int last) {
		/*
		 * 把数组分为两组，将比povit小的数放在它前面，比povit大的数放在它后面
		 */
        T povit = list[first];
        int low = first + 1;
        int high = last;

        while (high > low) {
            while (high > low && list[low].compareTo(povit)<=0)
                low++;
            while (low <= high && list[high].compareTo(povit)>0)
                high--;
            if (high > low) {
                T temp = list[high];
                list[high] = list[low];
                list[low] = temp;
            }
        }
        while(high>first&&list[high].compareTo(povit)>=0)
            high--;
        if(povit.compareTo(list[high])>0){
            list[first]=list[high];
            list[high]=povit;
            return high;
        }
        return first;
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
        Integer[] b=QuickSort.QuickSortStart(a);
        for (int i = 0; i < b.length; i++) {
            System.out.print(b[i]+" ");
        }
    }

}
