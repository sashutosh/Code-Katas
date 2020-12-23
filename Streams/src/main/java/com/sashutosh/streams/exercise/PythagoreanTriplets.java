package com.sashutosh.streams.exercise;

import java.util.stream.IntStream;
import java.util.stream.Stream;

public class PythagoreanTriplets {

    public static void printPythagoreanTriplets(int count) {
        Stream<int[]> tripletStream = IntStream.rangeClosed(1, 100)
                .boxed()
                .flatMap(a -> IntStream.rangeClosed(a, 100)
                        .filter(b -> Math.sqrt(a * a + b * b) % 1 == 0)
                        .mapToObj(b -> new int[]{a, b, (int) Math.sqrt(a * a + b * b)}));

        tripletStream.limit(count).forEach(t -> System.out.println(t[0] + ", " + t[1] + ", " + t[2]));

    }

    public static void main(String[] args) {
        PythagoreanTriplets.printPythagoreanTriplets(5);
    }
}
