package com.sashutosh.csproblems;

import java.util.Stack;

public class TowerOfHanoi {

    public void move(Stack<Integer> from, Stack<Integer> to, Stack<Integer> inter, int n){
        if(n==1){
            to.push(from.pop());
        }
        else if(n==0){
            return;
        }
        else {

            move(from, inter, to, n - 1);
            move(from, to, inter, 1);
            move(inter, to, from, n - 1);
        }

    }

    public static void main(String[] args) {
        Stack<Integer> towerA = new Stack<>();
        Stack<Integer> towerB = new Stack<>();
        Stack<Integer> towerC = new Stack<>();

        TowerOfHanoi towerOfHanoi = new TowerOfHanoi();
        towerA.push(3);
        towerA.push(2);
        towerA.push(1);

        towerOfHanoi.move(towerA,towerC,towerB,3);
        System.out.println(towerC.toString());

    }

}
