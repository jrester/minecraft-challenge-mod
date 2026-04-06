package com.challenge.utils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.Block;

public class Helpers {
    public static List<Block> collectAllBlocks(MinecraftServer server) {
        RegistryAccess registryAccess = server.registryAccess();
        var blockRegistry = registryAccess.lookupOrThrow(Registries.BLOCK);
        return blockRegistry.stream().collect(Collectors.toList());
    }

    public static List<EntityType> collectAllSpawnableMobs() {
        return Arrays.asList(EntityType.class.getDeclaredFields())
                .stream()
                .filter(field -> field.getType() == EntityType.class)
                .map(field -> {
                    try {
                        return field.get(null);
                    } catch (Exception e) {
                        return null;
                    }
                })
                .filter(entityType -> entityType != null)
                .map(EntityType.class::cast)
                .filter(entityType -> !entityType.getCategory().equals(MobCategory.MISC))
                .collect(Collectors.toList());
    }

    public static List<Item> collectAllItems(MinecraftServer server) {
        RegistryAccess registryAccess = server.registryAccess();
        var itemRegistry = registryAccess.lookupOrThrow(Registries.ITEM);
        return itemRegistry.stream().collect(Collectors.toList());
    }

    public static List<MobEffect> collectAllStatusEffects(MinecraftServer server) {
        RegistryAccess registryAccess = server.registryAccess();
        var enchantmentRegistry = registryAccess.lookupOrThrow(Registries.MOB_EFFECT);
        return enchantmentRegistry.stream().toList();
    }

    /*
     * Helper method for getting enchantments, because somehow it is not trivial...
     */
    public static Holder.Reference<Enchantment> getEnchantment(MinecraftServer server,
            ResourceKey<Enchantment> enchantment) {
        RegistryAccess registryAccess = server.registryAccess();
        var enchantmentRegistry = registryAccess.lookupOrThrow(Registries.ENCHANTMENT);
        return enchantmentRegistry.getOrThrow(enchantment);
    }

    public static List<ResourceKey<Enchantment>> collectAllEnchantments(MinecraftServer server) {
        RegistryAccess registryAccess = server.registryAccess();
        var enchantmentRegistry = registryAccess.lookupOrThrow(Registries.ENCHANTMENT);
        return enchantmentRegistry.entrySet().stream().map(e -> e.getKey()).toList();
    }
}
