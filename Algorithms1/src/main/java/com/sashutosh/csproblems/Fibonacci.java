package com.sashutosh.csproblems;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

public class Fibonacci {

    static Map<Integer, Integer> memo = new HashMap<>();//(Map.of(0, 0, 1, 1));
    int last = 0, next = 1;

    public static int withMemoization(int n) {
        if (n > 1 && !memo.containsKey(n)) {
            memo.put(n, (withMemoization(n - 1) + withMemoization(n - 2)));
        }
        return memo.get(n);
    }

    public static void main(String[] args) {
        System.out.println("Fibbonaci value " + Fibonacci.withMemoization(10));
        Fibonacci fibonacci = new Fibonacci();
        fibonacci.stream().limit(10).forEachOrdered(System.out::println);

    }

    public IntStream stream() {

        return IntStream.generate(() -> {
                    int oldLast = last;
                    last = next;
                    next = oldLast + last;
                    return oldLast;
                }
        );
    }
}
