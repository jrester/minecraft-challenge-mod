package com.challenge.utils;

import java.util.List;
import java.util.Random;


public class Randomizer<T> {
    private Random random = new Random();
    private final List<T> allItems;

    public Randomizer(List<T> allItems) {
        this.allItems = allItems;
    }


    public T getRandom() {
        return allItems.get(random.nextInt(allItems.size()));
    }
}
