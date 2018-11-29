package com.novell.zenworks.practice;

public class FrogJump {
    public static void main(String[] args){
        FrogJump j = new FrogJump();
        int result =j.solution(10,20,30);
        System.out.println(result);
    }
    public int solution(int X, int Y, int D) {
        // write your code in Java SE 8
        int hops=0;
        if (X==Y)
            return 0;
        else{
            int distance = Y-X;
            hops = (Y-X)/D;
            if(((Y-X)%D) ==0)
                return hops;
            else
                hops= hops+1;
        }
        return hops;
    }
}
