package com.novell.zenworks.practice;

public class CountSums {

    public static void main(String[] args){
        int a=0;
        int b=11;
        int k=10;
        CountSums cs = new CountSums();
        int count= cs.solution(a,b,k);
        System.out.println(count);
    }

    public int solution(int A, int B, int K) {
        // write your code in Java SE 8
        int count=0;
        if(K>B)
            return count;
        int i=0;
        i=getNextDivisibleNumber(A, B, K);
        count = (B-(A+i))/K;


        return count+1;
    }

    private int getNextDivisibleNumber(int A, int B, int K) {
        int i;
        int index=0;
        int start =A;
        if(A==0){
            start= A+1;
            index=index+1;
        }
        for(i=start; i<=B; i++,index++ ){

            if(i%K ==0)
                break;        }
        return index;
    }
}
