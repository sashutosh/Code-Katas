package com.novell.zenworks.practice;

import java.util.Stack;

public class MatchedParantheses {

    public static void main(String[] args){
        String test=  "+ )( + ()";
        MatchedParantheses p = new MatchedParantheses();
        int ret = p.check(test);
        System.out.println(ret);

    }

    public int check(String s){
        int  matching=0;
        Stack<Character> mystack= new Stack<>();

        for(int i=0;i<s.length(); i++){
            if(isBeginning(s.charAt(i))){
                mystack.push(s.charAt(i));
            }
            else{
                if(mystack.empty())
                    return 0;
                char popped = mystack.pop();
                if(!isMatching(popped,s.charAt(i)))
                    return 0;
            }
        }
        if(!mystack.empty()){
            matching=0;
        }
        else
        {
            matching =1;
        }
        return matching;
    }

    private boolean isMatching(char popped, char charAt) {
        if(popped== '{') {
            if (charAt == '}') {
                return true;
            } else {
                return false;
            }
        }
        else if(popped== '[') {
            if (charAt == ']') {
                return true;
            } else {
                return false;
            }
        }

        else if(popped== '(') {
            if (charAt == ')') {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    private boolean isBeginning(char charAt) {
        if(charAt== '(' || charAt== '{'|| charAt== '[')
            return true;
        else
            return false;
    }
}
