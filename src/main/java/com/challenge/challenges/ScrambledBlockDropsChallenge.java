package com.challenge.challenges;


import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import com.challenge.utils.ItemScrambler;


public class ScrambledBlockDropsChallenge extends AbstractBlockDropsChallenge {
    private final String name = "Scrambled Block Drops";

    private final ItemScrambler itemScrambler = new ItemScrambler();

    @Override
    protected ItemStack getItem(World world, PlayerEntity player, BlockState state) {
        int itemModifier = Math.abs(state.getBlock().hashCode());
        return this.itemScrambler.getScrambledForPlayer(itemModifier, player, this.getServer());
    }

    @Override
    public String getName() {
        return name;
    }

}
