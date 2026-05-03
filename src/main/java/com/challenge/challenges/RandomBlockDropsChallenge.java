package com.challenge.challenges;


import com.challenge.utils.ItemRandomizer;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;


public class RandomBlockDropsChallenge extends AbstractBlockDropsChallenge {
    private final String name = "Random Block Drops";

    private ItemRandomizer itemRandomizer = null;

    @Override
    public void enable() {
        super.enable();
        this.itemRandomizer = new ItemRandomizer(getServer());
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    protected ItemStack getItem(Level level, Player player, BlockState state) {
        return itemRandomizer.getRandom();
    }
}
