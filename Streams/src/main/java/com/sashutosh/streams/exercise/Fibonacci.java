package com.sashutosh.streams.exercise;

import java.util.stream.Stream;

public class Fibonacci {

    public static void main(String[] args) {
        Fibonacci fibonacci = new Fibonacci();
        fibonacci.generateFibobacci(10);

    }

    public void generateFibobacci(int count) {
        Stream.iterate(new int[]{0, 1},
                t -> new int[]{t[1], t[0] + t[1]})
                .limit(count)
                .map(t -> t[0])
                .forEach(System.out::println);
    }
}
