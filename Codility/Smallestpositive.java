package com.novell.zenworks.practice;

public class Smallestpositive {

    public static void main(String[] args){
        int[]A = {1, 3, 6, 4, 1, 2};
        int []B ={-1,-1};
        Smallestpositive sp = new Smallestpositive();
        int result=sp.solution(B);
        System.out.println(result);

    }
    public int solution(int[] A) {
        // write your code in Java SE 8
        int result=0;
        QSort(A,0,A.length-1);

        int index =findFirstPositive(A);
        if(index== A.length)
            result =1;
        else{
            result= findMissing(A,index);
        }

        return result;
    }

    private int findMissing(int[] a, int index) {
        int missing=0;
        int next=1;
        for(int i=index;i<a.length;i++,missing++){

            if(i+1 < a.length && a[i+1]==a[i])
                continue;
            if(a[i]==next) {
                next=next+1;
                continue;

            }
            else
            {
                break;
            }

        }
        return next;
    }

    private int findFirstPositive(int[] a) {
        int index=0;
        for(int i=0;i<a.length ;i++,index++){
            if(a[i]> 0)
                break;
        }
        return index;
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
