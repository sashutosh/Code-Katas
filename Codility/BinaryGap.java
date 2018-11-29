package com.novell.zenworks.practice;

// you can also use imports, for example:
// import java.util.*;

// you can write to stdout for debugging purposes, e.g.
// System.out.println("this is a debug message");

class BinaryGap {


    public static void main(String[] args){

        BinaryGap bg = new BinaryGap();
        bg.solution(1041);
    }
    public int solution(int N) {
        // write your code in Java SE 8
        //
        //
        // numBoolean= getBoolean(N);
        String binaryNum =getBooleanString(N);
        return getMaxBinaryGap(binaryNum);

    }

    private String getBooleanString(int number){
        return Integer.toBinaryString(number);
    }

    private int getMaxBinaryGap(String array){
        boolean started=false;
        int maxCount=0;
        int count=0;
        int startPoint = getNextStartIndex(array,0);
        System.out.println("Start point "+ startPoint);
        for(int i=startPoint+1;i< array.length(); i++){
            if(array.charAt(i)=='1' && !started)
            {
                continue;
            }
            else if(array.charAt(i)=='1' && started){
                started =false;
                System.out.println("Completed ss count "+ count);
                if(count > maxCount)
                    maxCount=count;
                count=0;
            }
            if(array.charAt(i)=='0'){
                started=true;
                count=count+1;
            }

        }
        return maxCount;

    }

    private int getNextStartIndex(String array, int startIndex){

        int index=0;
        for(int i=0; i< array.length();i++){
            if(array.charAt(i) == '0'){
                index++;
                continue;
            }
            else{
                break;
            }
        }
        System.out.print("Index is " + index);
        return index;
    }

}