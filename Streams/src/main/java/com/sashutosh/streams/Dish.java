package com.sashutosh.streams;

public class Dish {
    private final String name;
    private final int price;
    private final boolean isVegeterian;
    private final Type dishType;

    public Dish(String dishName, boolean vegeterian, int price, Type dishType) {
        this.name = dishName;
        this.dishType = dishType;
        this.isVegeterian = vegeterian;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public boolean isVegeterian() {
        return isVegeterian;
    }

    public Type getDishType() {
        return dishType;
    }

    public enum Type {
        OTHER,
        FISH,
        MEAT
    }
}
