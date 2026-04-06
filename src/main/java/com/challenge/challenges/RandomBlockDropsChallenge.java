package com.challenge.challenges;


import com.challenge.utils.Helpers;
import com.challenge.utils.Randomizer;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;


public class RandomBlockDropsChallenge extends AbstractBlockDropsChallenge {
    private final String name = "Random Block Drops";

    private Randomizer<Item> itemRandomizer = null;

    @Override
    public void enable() {
        super.enable();
        this.itemRandomizer = new Randomizer<>(Helpers.collectAllItems(getServer()));
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    protected ItemStack getItem(Level level, Player player, BlockState state) {
        return new ItemStack(itemRandomizer.getRandom());
    }
}
