package com.challenge.challenges;


import com.challenge.utils.Helpers;
import com.challenge.utils.Randomizer;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.world.World;

public class RandomBlockDropsChallenge extends AbstractBlockDropsChallenge {
    private final String name = "Random Block Drops";

    private final Randomizer<Item> itemRandomizer = new Randomizer<>(Helpers.collectAllItems());


    @Override
    public String getName() {
        return name;
    }

    @Override
    protected Item getItem(World world, PlayerEntity player, BlockState state) {
        return itemRandomizer.getRandom();
    }
}
