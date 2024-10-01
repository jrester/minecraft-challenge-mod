package com.challenge.challenges;

import com.challenge.events.BlockEvents;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;

public class DeleteBlocksOnBreak extends BaseChallenge {
    private final String name = "Delete Blocks";

    @Override
    public void registerEventHandlers() {
        BlockEvents.AFTER_BLOCK_BROKEN_EVENT.register((world, player, pos, state, blockEntity, tool) -> {
            if(!isActive()) return false; 
	    for(int i = -1; i < 2; i++){
	        for(int j = -1; j < 2; j++){
	            for(int k = -1; k < 2; k++){
		        if(i == 0 && j == 0 && k == 0)
			    continue;
		        BlockPos tmpPos = pos.add(i, j, k);
		        world.removeBlock(tmpPos, false);
		    }
		}
	    }
            return false;
        });
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public ItemStack  getIndicatorItemStack() {
        if(isEnabled()) {
            ItemStack itemStack = Items.BARRIER.getDefaultStack();
            return itemStack;
        } else {
            return Items.DIRT.getDefaultStack();
        }

    }
    
}
