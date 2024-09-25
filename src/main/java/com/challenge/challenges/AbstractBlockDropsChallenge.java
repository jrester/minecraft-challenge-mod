package com.challenge.challenges;

import com.challenge.events.BlockEvents;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.world.World;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.Vec3d;

public abstract class AbstractBlockDropsChallenge extends BaseChallenge {
    protected abstract Item getItem(World world, PlayerEntity player, BlockState state); 

    @Override
    public void registerEventHandlers() {
        BlockEvents.AFTER_BLOCK_BROKEN_EVENT.register((world, player, pos, state, blockEntity, tool) -> {
            if(!isActive()) return false; 

            Item item = this.getItem(world, player, state);
            ItemStack itemStack = new ItemStack(item);
            // Make sure to spawn the item at the canter of the original block, because otherwise it will glitch into the blocks around
            Vec3d blockCenterPos = pos.toCenterPos();
            ItemEntity itemEntity = new ItemEntity(world, blockCenterPos.getX(), blockCenterPos.getY(), blockCenterPos.getZ(), itemStack);
            itemEntity.setToDefaultPickupDelay();
            world.spawnEntity(itemEntity); 
            return true;
        });
    }

    @Override
    public ItemStack getIndicatorItemStack() {
        if(this.isEnabled()) {
            ItemStack itemStack = Items.NETHERITE_PICKAXE.getDefaultStack();
            RegistryEntry<Enchantment> fortune = getEnchantment(Enchantments.FORTUNE);
            itemStack.addEnchantment(fortune, 3);
            return itemStack;
        } else {
            return Items.WOODEN_PICKAXE.getDefaultStack();
        }
    }    
    
}
