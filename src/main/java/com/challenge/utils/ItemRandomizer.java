package com.challenge.utils;

import java.util.Random;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.ItemStack;

public class ItemRandomizer  {
    private Random random = new Random();
    private final CategorizedItemCollection categorizedItems;

    public ItemRandomizer(MinecraftServer server) {
        this.categorizedItems = CategorizedItemCollection.create(server);
    }
    
    public ItemStack getRandom() {
        int modifier = random.nextInt(categorizedItems.size + 1);
        return categorizedItems.getItemStack(modifier);
    }    
    
}
