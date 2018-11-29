package com.novell.zenworks.practice;

import java.util.Stack;

public class StoneBlocks {
    public static void main(String[] args){
        int[] test = {8,8,5,7,9,8,7,4,8};
        int[] test1 = {1,1,1};
        int[] test3 = {8,8,5,7,9,8,7,7,8};
        int[] test4 = {8,8,5,7,9,8,7,7,7};
        int[] test5= {1, 2, 3, 3, 2, 1};
        StoneBlocks blocks = new StoneBlocks();
        int result= blocks.solution(test4);
        System.out.println(result);

    }
    static int index=0;
    public int solution(int[] H){
        int count=0;



        while(index< H.length)
        {
            count+=getNextSubSequenceCount(H);
        }
        return count;
        /**/
    }

    private int getNextSubSequenceCount(int[] H) {
        int count=0;
        Stack<Integer> blocks= new Stack<>();
        blocks.push(H[index]);
        index+=1;
        for(int i=index;i<H.length;i++){
            if(!blocks.isEmpty()){

                if(blocks.contains(H[i])){
                    int j=i;
                    if(blocks.peek()==H[i]){
                        blocks.pop();
                        count++;
                        continue;
                    }
                    while(j+1<H.length && H[j]==H[j+1]) {

                        j++;
                    }
                    index=j+1;

                    break;

                }
                else{
                    index=i+1;
                    blocks.push(H[i]);
                }
            }
            else{
                index=i+1;
                blocks.push(H[i]);
            }
        }
        count+=blocks.size();
        return count;
    }
}
