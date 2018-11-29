package com.novell.zenworks.practice;
 import java.util.*;
import java.lang.*;
import java.io.*;

    class FivenSumPair {

        static List<Integer []> inputList= new ArrayList();
        public static void main (String[] args) {
            //code
            int input=Integer.getInteger(args[0]);
            int elemCount =0;
            for(int i=0;i<input;i++) {
                elemCount = Integer.getInteger(args[1 * i]);
                String[] elements = args[2 * i].split(" ");
                Integer[] numElements = convertToInteger(elements);
                numElements[numElements.length-1]=Integer.getInteger(args[3*i]);
                inputList.add(numElements);
            }

        }

        private static Integer[] convertToInteger(String[] elements) {

            Integer[] intArray= new Integer[elements.length+1];
            for(int i=0;i<elements.length;i++)
            {
                intArray[i]=Integer.getInteger(elements[i]);
            }
            return intArray;
        }

        private static void getInput(){

        }
    }

