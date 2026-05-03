package com.challenge.utils;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class ItemScrambler extends Scrambler<ItemStack> {
    private final CategorizedItemCollection categorizedItems;

    public ItemScrambler(MinecraftServer server) {
        this.categorizedItems = CategorizedItemCollection.create(server);
    }
    
    @Override
    public ItemStack getScrambledForPlayer(int itemModifier, Player player, MinecraftServer server) {
        int playerModifier = this.getModifierForPlayer(player);
        int totalModifier = Math.abs(itemModifier * playerModifier + playerModifier);
        return categorizedItems.getItemStack(totalModifier);
    }    
}
