package com.novell.zenworks.practice;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.Math.max;

public class MaxSubArray {

    public static void main(String[] args ){
      int[] A=   {-2,1,-3,4,-1,2,1,-5,4};
       MaxSubArray msa= new MaxSubArray();
       //int result= msa.maxSubArraySum(Arrays.stream(A).boxed().collect(Collectors.toList()));
       //int result= msa.maxSubArraySum(A,0,A.length);
        int result= msa.maxSubarray_Kadane(A);

       System.out.println(result);
    }

    public int maxSubarray_Kadane(int[] A){
        int sum=0, ans=0;
        for(int i=0;i<A.length;i++){

            if(sum+ A[i] > 0){
                sum+=A[i];
            }
            else{
                sum=0;
            }
            ans = max(ans,sum);
        }
        return ans;
    }
    // DO NOT MODIFY THE LIST. IT IS READ ONLY
    public int maxSubArraySum(int[] A, int startindex,int n){
        if(n==1){
            return A[startindex];
        }

        int m= n/2;
        int left_MSS = maxSubArraySum(A,0,m);
        int right_MSS = maxSubArraySum(A,m,n-m);
        int leftSum=0,rightSum=0,sum=0;
        for(int i=m;i<n;i++){
            sum+=A[i];
            if(sum>rightSum)
                rightSum=sum;

        }
        for(int i=m-1; i>=0;i--){
            sum+=A[i];
            if(sum>leftSum)
                leftSum=sum;


        }
        int answer = max(leftSum,rightSum);
        return  max(answer,leftSum+rightSum);
    }


    public int maxSubArray(final List<Integer> A) {
        int sum = Integer.MIN_VALUE;
        int[] prefix_sums= new int[A.size()];
        prefix_sums[0] =A.get(0);
        for(int i=1;i<A.size();i++){

            prefix_sums[i]= A.get(i)+prefix_sums[i-1];

        }

        for(int j=0;j<A.size()-1; j++){
            for(int k=1;k<A.size(); k++){
                int cur_sum = prefix_sums[k]-prefix_sums[j];
                if(cur_sum >sum)
                    sum=cur_sum;
            }
        }
        return  sum;


    }


}
