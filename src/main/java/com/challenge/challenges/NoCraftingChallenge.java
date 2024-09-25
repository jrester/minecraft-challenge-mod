package com.challenge.challenges;

import com.challenge.events.BlockEvents;

import net.minecraft.util.ActionResult;

public class NoCraftingChallenge extends BaseChallenge {
    private static final String name = "No Craafting";

    @Override
    public void registerEventHandlers() {
        BlockEvents.ON_CRAFTING_BLOCK_USE.register((state, world, pos, player, hit) -> {
            if(!this.isActive()) return ActionResult.PASS;

            return ActionResult.FAIL;
        });
    }

    public String getName() {
        return this.name;
    }
    
}
