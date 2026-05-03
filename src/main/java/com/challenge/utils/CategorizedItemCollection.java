package com.challenge.utils;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.challenge.ChallengeMod;

import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;

public class CategorizedItemCollection {
    public static final Logger LOGGER = LoggerFactory.getLogger(ChallengeMod.MOD_ID);
    private final int MAX_ENCHANTMENT_NUM = 5;

    private final MinecraftServer server;
    private final Map<ItemCategory, List<Item>> categorizedItems;
    private final List<ResourceKey<Enchantment>> enchantmentKeys;
    public final int size;

    public CategorizedItemCollection(MinecraftServer server, Map<ItemCategory, List<Item>> categorizedItems, List<ResourceKey<Enchantment>> enchantments) {
        this.server = server;
        this.categorizedItems = categorizedItems;
        this.enchantmentKeys = enchantments;
        this.size = categorizedItems.values().stream().map(itemList -> itemList.size()).reduce(0, (acc, size) -> Math.max(acc, size));
    }

    public static CategorizedItemCollection create(MinecraftServer server) {
        var categorizedItems = CategorizedItemCollection.collectCategorizedItems(server);
        var enchantments = Helpers.collectAllEnchantments(server);
        return new CategorizedItemCollection(server, categorizedItems, enchantments);
    }

    public static Map<ItemCategory, List<Item>> collectCategorizedItems(MinecraftServer server) {
        final List<Item> allItems = Helpers.collectAllItems(server);
        Map<ItemCategory, List<Item>> canonicalItems = new HashMap<>();

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

    public Set<ItemCategory> getCategories() {
        return this.categorizedItems.keySet();
    }

    public ItemStack getItemStack(final int modifier) {
        int scopedModifier = modifier % (this.size + 1);
        ItemCategory category = getChoosenCategory(scopedModifier);
        return getItemStackForCategory(category, scopedModifier);
    }

    protected ItemCategory getChoosenCategory(final int modifier) {
        Set<ItemCategory> itemCategories = this.getCategories();
        ItemCategory chosen = ItemCategory.GENERAL;

        // Normalize totalModifier to a value between 0.0 and 1.0
        double normalizedModifier = (double)modifier / (double)(this.size + 1);

        double cumProb = 0.0;
        for (ItemCategory itemCategory : itemCategories) {
            if(this.categorizedItems.get(itemCategory).isEmpty()) continue;
            cumProb += itemCategory.getProbability();
            if (normalizedModifier <= cumProb) {
                chosen = itemCategory;
                break;
            }
        }

        return chosen;
    }

    protected ItemStack getItemStackForCategory(final ItemCategory category, final int modifier) {
        if(category == ItemCategory.ENCHANTED_BOOK) {
            return getItemStackForEnchantedBook(modifier);
        }

        List<Item> itemList = this.categorizedItems.get(category);
        int itemIndex = modifier % itemList.size();
        Item item = itemList.get(itemIndex);
        return new ItemStack(item);
    }

    protected ItemStack getItemStackForEnchantedBook(final int modifier) {
        ItemStack itemStack = new ItemStack(Items.ENCHANTED_BOOK);

        int enchantmentNum = Math.max(1, modifier % MAX_ENCHANTMENT_NUM);

        ItemEnchantments.Mutable itemEnchantments = new ItemEnchantments.Mutable(ItemEnchantments.EMPTY);
        for(int i = 0; i < enchantmentNum; i++) {
            int enchantmentModifier = (modifier + i) * (i + 1);

            ResourceKey<Enchantment> enchantmentKey = enchantmentKeys.get(enchantmentModifier % enchantmentKeys.size());
            Holder.Reference<Enchantment> enchantment = Helpers.getEnchantment(server, enchantmentKey);

            int minEnchantmentLevel = enchantment.value().getMinLevel();
            int maxEnchantmentLevel = enchantment.value().getMaxLevel();
            int enchantmentLevel = Math.max(minEnchantmentLevel, (enchantmentModifier % maxEnchantmentLevel));

            itemEnchantments.set(enchantment, enchantmentLevel);
        }

        itemStack.set(DataComponents.STORED_ENCHANTMENTS, itemEnchantments.toImmutable());
 
        return itemStack;

    }    
}