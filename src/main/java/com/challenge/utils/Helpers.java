package com.challenge.utils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


import net.minecraft.item.Items;
import net.minecraft.item.Item;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;

public class Helpers { 
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
}
