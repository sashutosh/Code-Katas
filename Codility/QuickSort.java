package com.novell.zenworks.practice;

public class QuickSort {

    public static void main(String[] args){
        int[] array = {7,6,3,2,1,5,8,4};
        QSort(array,0,array.length-1);
        for (int i = 0; i <array.length ; i++) {
            System.out.println(array[i] + ",");
        }

    }

    public static void QSort(int[] array, int start, int end){
        if(start<end){
            int pindex= partition(array,start,end);
            QSort(array,start,pindex-1);
            QSort(array, pindex+1,end);
        }
    }

    public static int partition(int[] array, int start, int end){
        int pindex=start;
        int pivot = array[end];
        for(int i=start; i< end; i++){
            if(array[i] <= pivot){
                swap(array, i , pindex);
                pindex++;
            }

        }
        swap(array, pindex,end);
        return pindex;
    }

    public static void swap(int[] array, int i1, int i2){
        int temp = array[i1];
        array[i1]=array[i2];
        array[i2]=temp;
    }
}
