package com.novell.zenworks.practice;

public class PermCheck {
    public static void main(String[] args){

        int[] data = {2,3,1,5,4};
        PermCheck el = new PermCheck();
        int result =el.solution(data);
        System.out.println(result);

    }

    public int solution(int[] A) {
        // write your code in Java SE 8
        int missing =0;
        long sum =0;
        int arrayLength = A.length;
        long actualSum = ((arrayLength)*(arrayLength+1 ))/2;
        for(int i=0;i<A.length;i++){
            sum = sum+ A[i];

        }
        missing = (int)(actualSum - sum);
        if(missing==0)
            return 1;
        else
            return 0;

    }

}
