package com.novell.zenworks.practice;

public class NailingPlanks {


    public static void main(String[] args){

        int A[] ={1,4,5,8,};
        int B[] ={4,5,9,10};
        int C[] = {4,6,7,10,2};
        NailingPlanks np = new NailingPlanks();
        int count = np.solution(A,B,C);
        System.out.println(count);

    }

    public int solution(int[] a, int[] b, int[] c) {

        int plankCount = a.length;
        boolean[] planksCovered= new boolean[plankCount];
        int coveredCount=0;
        int index=-1;
        for(int i=0;i< c.length;i++){

            int count =getPlanksCovered(a,b,c,i,planksCovered);
            coveredCount+=count;
            if(coveredCount>=plankCount){
                index++;
                break;
            }

        }
        if(index!=-1){
            index++;
        }
        return index;
    }


    private int getPlanksCovered(int[] a, int[] b, int[] c, int i, boolean[] planksCovered) {
        int count=0;
        int start=0;
        int end = a.length-1;
        int mid = 0;
        while(start<=end){
            mid = (start+end)/2;
            if(checkHittingPlank(a,b,mid, c[i]))
            {
                if(planksCovered[mid]!=true){
                    count++;
                    planksCovered[mid]=true;
                }
            }
            if(c[i] <= a[mid]){
                end = mid-1;
            }
            else{
                start= mid+1;
            }

        }
        return count;

    }

    private boolean checkHittingPlank(int[] a, int[] b,int mid, int nailpos) {

        if((nailpos>= a[mid]) && (nailpos <= b[mid]))
            return true;
        else
            return false;
    }
}
