package com.novell.zenworks.practice;

public class Dominator {
    public static void main(String[] args){
        int[] test1={3,1,3,2,3,-1,3,3};
        int[] test2= {1};
        int[] test3= {-1, -1};
        int[] test4={3,1,3,2};
        int[] test5={3,1,3};
        int[] test6={2, 1, 1, 3, 4};
        Dominator d = new Dominator();
        int result = d.solution(test6);
        System.out.println(result);
    }

    public int solution(int[] A) {
        int index=-1;
        int size=0;
        int value=0;
        int candidate=0;
        if(A.length==1)
        {
            index=0;
            return index;
        }
        // write your code in Java SE 8
        for(int i=0;i<A.length;i++){
            if(size==0){

                value=A[i];
                size+=1;

            }
            else if(value!=A[i]){
                size-=1;
            }
            else{
                size+=1;
            }

        }
        if(size >=1)
            candidate=value;
        else{
            return index;
        }
        size=0;
        for(int j=0;j<A.length;j++){

            if(A[j]==candidate){
                size++;
                index=j;
            }

            if(size > A.length/2)
                break;

        }
        if(size > A.length/2)
            return index;
        else
            return -1;
    }



}
