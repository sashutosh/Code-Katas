package com.novell.zenworks.practice;

import org.apache.kafka.common.metrics.stats.Max;

public class MaxCounters {

    public static void main(String[] args){

        int[] input= {3,4,4,6,1,4,4,};
        int[] input1= {6,6,6,6,6,4,4,};
        MaxCounters mCounters = new MaxCounters();
        int[] result =mCounters.solution(5,input1);
        for (int i = 0; i <result.length ; i++) {
            System.out.println(result[i]);
        }


    }
    public int[] solution(int N, int[] a){
        int[] counters = new int[N];
        int currentMax=0;
        for(int i=0;i<a.length;i++){
            if(a[i]==(N+1)){
                currentMax=getMax(counters);
                max(counters,currentMax);

            }
            else{
                increment(counters, a[i]-1);
            }
        }

        return counters;
    }

    private int getMax(int[] counters) {
        int max=0;
        for(int i=0;i<counters.length;i++){
            if(counters[i]>max){
                max=counters[i];
            }

        }
        return max;
    }

    private void max(int[] counters, int currentMax){
        for(int j=0;j<counters.length;j++){
            counters[j]=currentMax;
        }
     }

     private void increment(int[] counters, int index){
        counters[index]= counters[index]+1;
     }
}
