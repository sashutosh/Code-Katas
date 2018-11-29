package com.novell.zenworks.practice;

public class MinMaxDivision {

    public static void main(String[] args){

        int[] A= {2,1,5,1,2,2,2};
        MinMaxDivision mmd= new MinMaxDivision();
        int minSum= mmd.solution(3,5,A,A.length);
    }

    public int solution(int k, int m, int[] A, int n){
        int minSum=getSum(A,0,n/3);;
        int index1= n/3;
        int index2= (2*n)/3;
        int curminsum=m;
        int sum1,sum2,sum3;
        int iterationMin=minSum;
        do{
            if(iterationMin<minSum) {
                minSum = iterationMin;
            }
            sum1= getSum(A,0,index1);
            sum2= getSum(A,index1+1,index2);
            sum3= getSum(A,index2+1,n-1);
            iterationMin = max(sum1,sum2,sum3);
            if(iterationMin < minSum){
                index1--;
                if(index1<0)
                    index1=0;
                index2--;
                if(index2<0)
                    index2=0;
            }
            else{
                index1++;
                index2++;
                if(index2>n-1)
                    index2=n-1;
                if(index1>n-1)
                    index1=n-1;
            }



        }while(minSum >= iterationMin);


        return minSum;
    }

    int getSum(int[] A,int start, int end){
        int sum=0;
        for(int i=start;i<=end;i++){
            sum+=A[i];
        }
        return  sum;

    }

    int max(int a, int b, int c){
        int maxofThree =0;
        return Math.max(Math.max(a,b),c);
    }
}
