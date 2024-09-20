package com.challenge.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import net.minecraft.entity.player.PlayerEntity;

public class Scrambler<T> {
    private final List<T> allItems;
    private Random random = new Random();
    private Map<String, Number> playerModifiers = new HashMap<>();

    public Scrambler(List<T> allItems) {
        this.allItems = allItems;
    }

    public T getScrambledForPlayer(int itemModifier, PlayerEntity player) {
        String uuid = player.getUuidAsString();
        int modifier;
        if(!playerModifiers.containsKey(uuid)) {
            modifier = Math.abs(this.random.nextInt());
            this.playerModifiers.put(uuid, modifier);
        } else {
            modifier = this.playerModifiers.get(uuid).intValue();
        }

        int itemIndex = Math.abs(itemModifier + modifier) % this.allItems.size();
        T item = this.allItems.get(itemIndex);
        return item;
    }    
}
