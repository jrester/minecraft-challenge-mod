package com.challenge.utils;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;

public class ItemScrambler extends Scrambler<ItemStack> {
    public enum ItemCategory {
        GENERAL(0.85), 
        POTTERY_SHERD(0.025), 
        BANNER_PATTERN(0.025), 
        COPPER(0.025),
        SMITHING_TEMPLATE(0.025),
        ENCHANTED_BOOK(0.00);

        private double probability;

        ItemCategory(double probability) {
            this.probability = probability;
        }

        public double getProbability() {
            return this.probability;
        }
    }
    private final Map<ItemCategory, List<Item>> allItems;
    private final int maxIndex;

    public ItemScrambler(MinecraftServer server) {
        allItems = ItemScrambler.collectCanonicalItems(server);
        maxIndex = allItems.values().stream().map(itemList -> itemList.size()).reduce(0, (acc, size) -> Math.max(acc, size));
    }

    private static HashMap<ItemCategory, List<Item>> collectCanonicalItems(MinecraftServer server) {
        final List<Item> allItems = Helpers.collectAllItems(server);
        HashMap<ItemCategory, List<Item>> canonicalItems = new HashMap<>();

        for(ItemCategory itemCategory : ItemCategory.values()) {
            canonicalItems.put(itemCategory, new LinkedList<>());
        }

        for(Item item : allItems) {
            if(item.getName(item.getDefaultInstance()).toString().endsWith("smithing_templae")) {
                canonicalItems.get(ItemCategory.SMITHING_TEMPLATE).add(item);
            } else if (item.getName(item.getDefaultInstance()).toString().endsWith("pottery_sherd")) {
                canonicalItems.get(ItemCategory.POTTERY_SHERD).add(item);
            } else if (item == Items.ENCHANTED_BOOK) {
                canonicalItems.get(ItemCategory.ENCHANTED_BOOK).add(item);
            } else if (item.getName(item.getDefaultInstance()).toString().contains("copper")) {
                canonicalItems.get(ItemCategory.COPPER).add(item);
            } else {
                canonicalItems.get(ItemCategory.GENERAL).add(item);
            }
        }

        return canonicalItems;
    }
    
    @Override
    public ItemStack getScrambledForPlayer(int itemModifier, Player player, MinecraftServer server) {
        int playerModifier = this.getModifierForPlayer(player);
        int totalModifier = Math.abs(itemModifier * playerModifier + playerModifier) % (maxIndex + 1);

        Set<ItemCategory> itemCategories = this.allItems.keySet();
        ItemCategory chosen = ItemCategory.GENERAL;

        // Normalize totalModifier to a value between 0.0 and 1.0
        double normalizedModifier = (double)totalModifier / (double)(maxIndex + 1);
    
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
        if(chosen == ItemCategory.ENCHANTED_BOOK) {
            ItemStack itemStack = new ItemStack(this.allItems.get(chosen).get(0));
            List<ResourceKey<Enchantment>> enchantmentKeys = Helpers.collectAllEnchantments(server);
            ResourceKey<Enchantment> enchantmentKey = enchantmentKeys.get(totalModifier % enchantmentKeys.size());
            Holder.Reference<Enchantment> enchantment = Helpers.getEnchantment(server, enchantmentKey);

            int enchantmentLevel;
            if(enchantment.value().getMaxLevel() == 1) {
                enchantmentLevel = 1;
            } else {
                enchantmentLevel = (totalModifier % (enchantment.value().getMaxLevel() - 1)) + 1;
            }
            System.out.println("Selected enchanted book with enchantment " + enchantment.value().toString() + " at level " + enchantmentLevel);

            itemStack.enchant(enchantment, enchantmentLevel);
            return itemStack;
        }

        List<Item> itemList = this.allItems.get(chosen);
        int itemIndex = totalModifier % itemList.size();
        Item item = itemList.get(itemIndex);
        return new ItemStack(item);
    }    
}
