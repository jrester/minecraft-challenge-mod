package com.challenge.challenges;

import com.challenge.events.BlockEvents;
import com.challenge.utils.Helpers;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;


public class NoCraftingTableChallenge extends BaseChallenge {
    private final String name = "No Crafting Table";

    @Override
    public void registerEventHandlers() {
        BlockEvents.ON_CRAFTING_BLOCK_USE.register((state, world, pos, player, hit) -> {
            if(!this.isActive()) return InteractionResult.PASS;

            return InteractionResult.FAIL;
        });
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public ItemStack getIndicatorItemStack() {
        if(isEnabled()) {
            return asEnchantedIndicatorItemStack(Items.CRAFTING_TABLE, Enchantments.BINDING_CURSE, 1);
        } else {
            return asIndicatorItemStack(Items.CRAFTING_TABLE);
        }

    }
    
}
