package com.challenge.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;

public class ItemScrambler {
    private Random random = new Random();
    private Map<String, Number> playerModifiers = new HashMap<>();
    
    public Item getScrambledForPlayer(int itemModifier, PlayerEntity player) {
        String uuid = player.getUuidAsString();
        int modifier;
        if(!playerModifiers.containsKey(uuid)) {
            modifier = Math.abs(this.random.nextInt());
            this.playerModifiers.put(uuid, modifier);
        } else {
            modifier = this.playerModifiers.get(uuid).intValue();
        }

        int totalModifier = Math.abs(itemModifier + modifier);

        Set<ItemCategory> itemCategories = this.allItems.keySet();
        ItemCategory chosen = ItemCategory.GENERAL;
        if (!itemCategories.isEmpty()) {
        // Normalize totalModifier to a value between 0.0 and 1.0
        double normalizedModifier = (totalModifier % 1000000) / 1000000.0;
        
        // Use deterministic approach based on probabilities
        double cumProb = 0.0;
        for (ItemCategory itemCategory : itemCategories) {
            if(this.allItems.get(itemCategory).isEmpty()) continue;
            cumProb += itemCategory.getProbability();
            if (normalizedModifier <= cumProb) {
                chosen = itemCategory;
                break;
            }
        }
        }      

        List<T> itemList = this.allItems.get(chosen);

        int itemIndex = totalModifier % itemList.size();
        T item = itemList.get(itemIndex);
        return item;
    }    
}
