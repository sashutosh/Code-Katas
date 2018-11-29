package com.novell.zenworks.practice;

public class MissingElement {
    public static void main(String[] args){
        int[] data = {2,3,1,5};
        MissingElement el = new MissingElement();
        int missing =el.solution(data);
        System.out.println(missing);
    }

    public int solution(int[] A) {
        // write your code in Java SE 8
        int missing =0;
        long sum =0;
        int arrayLength = A.length;
        long actualSum = ((arrayLength+1)*(arrayLength +2))/2;
        for(int i=0;i<A.length;i++){
            sum = sum+ A[i];

        }
        missing = (int)(actualSum - sum);
        return missing;

    }

}
