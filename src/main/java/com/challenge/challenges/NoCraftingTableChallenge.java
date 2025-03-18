package com.challenge.challenges;

import com.challenge.events.BlockEvents;
import com.challenge.utils.Helpers;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.ActionResult;

public class NoCraftingTableChallenge extends BaseChallenge {
    private final String name = "No Crafting Table";

    @Override
    public void registerEventHandlers() {
        BlockEvents.ON_CRAFTING_BLOCK_USE.register((state, world, pos, player, hit) -> {
            if(!this.isActive()) return ActionResult.PASS;

            return ActionResult.FAIL;
        });
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public ItemStack  getIndicatorItemStack() {
        if(isEnabled()) {
            ItemStack itemStack = Items.CRAFTING_TABLE.getDefaultStack();
            RegistryEntry<Enchantment> curseOfBinding = Helpers.getEnchantment(this.getServer(), Enchantments.BINDING_CURSE);
            itemStack.addEnchantment(curseOfBinding, 1);
            return itemStack;
        } else {
            return Items.CRAFTING_TABLE.getDefaultStack();
        }

    }
    
}
