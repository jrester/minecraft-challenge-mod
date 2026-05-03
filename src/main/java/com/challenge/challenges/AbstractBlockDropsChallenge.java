package com.challenge.challenges;

import com.challenge.events.BlockEvents;

import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;


public abstract class AbstractBlockDropsChallenge extends BaseChallenge {
    protected abstract ItemStack getItem(Level level, Player player, BlockState state); 

    @Override
    public void registerEventHandlers() {
        BlockEvents.AFTER_PLAYER_DESTROY_EVENT.register((world, player, pos, state, blockEntity, tool, isBlockReplaced) -> {
            try{
                if(!isActive() || isBlockReplaced) return false; 
                ItemStack itemStack = this.getItem(world, player, state);
                // Make sure to spawn the item at the canter of the original block, because otherwise it will glitch into the blocks around
                Vec3 blockCenterPos = pos.getCenter();
                ItemEntity itemEntity = new ItemEntity(world, blockCenterPos.x(), blockCenterPos.y(), blockCenterPos.z(), itemStack);
                itemEntity.setDefaultPickUpDelay();
                world.addFreshEntity(itemEntity); 
                return true;
            } catch(Exception e) {
              LOGGER.error("Exception occured while trying to replace block drop for {}: {}", state.getBlock().getName().toString(), e);
              return false;   
            }
        });
    }

    @Override
    public ItemStack getIndicatorItemStack() {
        if(this.isEnabled()) {
            return asEnchantedIndicatorItemStack(Items.NETHERITE_PICKAXE, Enchantments.FORTUNE, 3);
        } else {
            return asIndicatorItemStack(Items.WOODEN_PICKAXE);
        }
    }    
    
}
