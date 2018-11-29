package com.novell.zenworks.practice;

public class FrogRiverOne {
    public static void main(String[] args){

        int result=0;
        int[] A = {1,3,1,4,2,3,5,4};
        int[] B = {1,3,1,4,2,3,1,4};
        int[] c = {1,1,1,1,1,1,1,1};
        FrogRiverOne fr= new FrogRiverOne();
        result =fr.solution(5,c);

        System.out.println(result);


    }

    public int solution(int X, int[] A) {
        // write your code in Java SE 8
        int visitPoint=0;
        boolean[] visited = new boolean[X];
        int totalVisited=0;
        for(int i=0;i< A.length;i++){
            if(A[i] <= X){
                if(!visited[A[i]-1]){
                    visited[A[i]-1] =true;
                    totalVisited++;
                    if(totalVisited==X) {
                        visitPoint=i;
                        break;
                    }
                }
            }

        }
        if(totalVisited==X)
            return visitPoint;
        else
            return -1;

    }


}
