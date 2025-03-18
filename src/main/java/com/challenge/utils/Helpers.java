package com.challenge.utils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.minecraft.item.Item;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffects;

public class Helpers { 
    public static List<Block> collectAllBlocks() {
        return Registries.BLOCK.stream().collect(Collectors.toList());
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
        return Registries.ITEM.stream().collect(Collectors.toList());
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

  /* Helper method for getting enchantments, because somehow it is not trivial... */
  public static RegistryEntry<Enchantment> getEnchantment(MinecraftServer server, RegistryKey<Enchantment> enchantment) {
      World world = server.getWorld(RegistryKey.of(RegistryKeys.WORLD, Identifier.of("overworld"))); 
      return RegistryEntry.of(world.getRegistryManager().get(RegistryKeys.ENCHANTMENT).get(enchantment));
  }

      public static List<RegistryKey<Enchantment>> collectAllEnchantments() {
      return Arrays.asList(Enchantments.class.getDeclaredFields())
          .stream()
    			.map(field -> { try {
                    return field.get(null); 
                } catch(IllegalAccessException e) {
                    return null;
                }})
    			.filter(item -> item != null)
    			.map(e -> (RegistryKey<Enchantment>) e)
    			.collect(Collectors.toList());
  }
}
