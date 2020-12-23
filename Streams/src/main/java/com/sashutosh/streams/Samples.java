package com.sashutosh.streams;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiPredicate;

import static java.util.stream.Collectors.toList;

public class Samples {

    public static List<int[]> combineTwoStreams() {
        List<Integer> numbers1 = Arrays.asList(1, 2, 3);
        List<Integer> numbers2 = Arrays.asList(3, 4);

        return numbers1.stream()
                .flatMap(x ->
                        numbers2.stream().map(y -> new int[]{x, y}))
                .collect(toList());
    }

    public static List<int[]> combineTwoStreamsWithCondition(BiPredicate<Integer, Integer> biPredicate) {
        List<Integer> numbers1 = Arrays.asList(1, 2, 3);
        List<Integer> numbers2 = Arrays.asList(3, 4);

        return numbers1.stream()
                .flatMap(x ->
                        numbers2.stream()
                                .filter(y -> biPredicate.test(x, y))
                                .map(y -> new int[]{x, y}))
                .collect(toList());
    }

    public static int countDishes() {
        List<Dish> specialMenu = Arrays.asList(
                new Dish("seasonal fruit", true, 120, Dish.Type.OTHER),
                new Dish("prawns", false, 300, Dish.Type.FISH),
                new Dish("rice", true, 350, Dish.Type.OTHER),
                new Dish("chicken", false, 400, Dish.Type.MEAT),
                new Dish("french fries", true, 530, Dish.Type.OTHER));

        return specialMenu.stream()
                .map(x -> 1)
                .reduce(0, Integer::sum);

    }

    public static void main(String[] args) {
        List<int[]> pairs = Samples.combineTwoStreams();
        pairs.forEach(Samples::prettyPrint);

        System.out.println("Pairs with some 3");
        List<int[]> pairsWithSum3 = Samples.combineTwoStreamsWithCondition((x, y) -> ((x + y) % 3) == 0);
        pairsWithSum3.forEach(Samples::prettyPrint);

        System.out.println("Count of dishes " + Samples.countDishes());
    }


    private static void prettyPrint(int[] pair) {
        System.out.print("(");
        Arrays.stream(pair).asLongStream().forEach(x -> System.out.print(" " + x + " "));
        System.out.println(")");
    }
}
