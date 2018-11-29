package com.novell.zenworks.practice;

import static java.lang.Math.abs;

public class TapeEquilibrium {

    public static void main(String[] args){
        int[] A = {3,1,2,4,3};
        int[] B = {-1000,1000};
        int[] C = {3,1,2};
        int[] D = {1,2,3};
        TapeEquilibrium te = new TapeEquilibrium();
        int result = te.solution(D);
        System.out.println(result);
    }

    public int solution(int[] A) {
        // write your code in Java SE 8
        int minDiff =10000;
        int curDiff =0;
        if(A.length==2)
        {
            return abs(A[0]-A[1]);
        }

        for(int i=1;i< A.length;i++){
            curDiff= abs(sum(0,i-1, A) -sum(i,A.length-1,A));
            if(curDiff<minDiff)
                minDiff=curDiff;

        }

        return minDiff;
    }

    private int sum(int start, int end, int[] a) {
        int sum =0;
        if(start==end){
            sum = a[start];
            return sum;
        }
        for(int j=start; j<=end;j++){
            sum+= a[j];
        }
        return sum;
    }
}
