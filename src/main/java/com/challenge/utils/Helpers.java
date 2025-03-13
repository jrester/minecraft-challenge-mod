package com.challenge.utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;


import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.item.Item;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffects;

public class Helpers { 
    public enum ItemCategory {
        GENERAL(0.7), 
        POTTERY_SHERD(0.1), 
        BANNER_PATTERN(0.1), 
        SMITHING_TEMPLATE(0.05), 
        ENCHANTED_BOOK(0.05);

        private double probability;

        ItemCategory(double probability) {
            this.probability = probability;
        }

        public double getProbability() {
            return this.probability;
        }

    }

    public static List<EntityType> collectAllSpawnableMobs() {
        return Arrays.asList(EntityType.class.getDeclaredFields())
            .stream()
            .map(field -> {
                try {
                    return field.get(null);
                } catch (Exception e) {
                    return null;
                }
            })
            .filter(entityType -> entityType != null)
            .map(EntityType.class::cast)
            .filter(entityType -> !entityType.getSpawnGroup().equals(SpawnGroup.MISC))
            .collect(Collectors.toList());
    }
 
    public static List<Item> collectAllItems() {
        return Arrays.asList(Items.class.getDeclaredFields())
    			.stream()
    			.map(field -> { try {
                    return field.get(null); 
                } catch(IllegalAccessException e) {
                    return null;
                }})
    			.filter(item -> item != null)
    			.map(Item.class::cast)
    			.collect(Collectors.toList());
    }

    public static HashMap<ItemCategory, List<Item>> collectCanonicalItems() {
        final List<Item> allItems = Helpers.collectAllItems();
        HashMap<ItemCategory, List<Item>> canonicalItems = new HashMap<>();

        for(ItemCategory itemCategory : ItemCategory.values()) {
            canonicalItems.put(itemCategory, new LinkedList<>());
        }

        for(Item item : allItems) {
            if(item.getName().toString().endsWith("smithing_templae")) {
                canonicalItems.get(ItemCategory.SMITHING_TEMPLATE).add(item);
            } else if (item.getName().toString().endsWith("pottery_sherd")) {
                canonicalItems.get(ItemCategory.POTTERY_SHERD).add(item);
            } else if (item == Items.ENCHANTED_BOOK) {
                canonicalItems.get(ItemCategory.ENCHANTED_BOOK).add(item);
            } else {
                canonicalItems.get(ItemCategory.GENERAL).add(item);
            }
        }

        return canonicalItems;
    }

    public static List<StatusEffect> collectAllStatusEffects() {
        return Arrays.asList(StatusEffects.class.getDeclaredFields())
            .stream()
            .map(field -> {
                try {
                    return field.get(null);
                } catch(IllegalAccessException e) {
                    return null;
                }
            })
            .filter(effect -> effect != null)
            .map(RegistryEntry.class::cast)
            .map(entry -> entry.getKey().orElseThrow())
            .map(RegistryKey.class::cast)
            .map(key -> Registries.STATUS_EFFECT.get(key))
            .collect(Collectors.toList());
    }
}
