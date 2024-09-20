package com.challenge.challenges;


import net.minecraft.block.BlockState;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import com.challenge.utils.Helpers;
import com.challenge.utils.Scrambler;
import com.challenge.events.BlockEvents;

public class ScrambledBlockDropsChallenge extends AbstractBlockDropsChallenge {
    private final String name = "Scrambled Block Drops";

    private final Scrambler<Item> itemScrambler = new Scrambler<>(Helpers.collectAllItems());

    @Override
    protected Item getItem(World world, PlayerEntity player, BlockState state) {
        int itemModifier = Math.abs(state.getBlock().getName().hashCode());
        return this.itemScrambler.getScrambledForPlayer(itemModifier, player);
    }

    public void start() {
        super.start();

        BlockEvents.AFTER_BLOCK_BROKEN_EVENT.register((world, player, pos, state, blockEntity, tool) -> {
           if(!isActive()) return false; 

            // hashes can be negative, but the itemModifier must be positive as it will be used for array indexing
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
    public String getName() {
        return name;
    }

}
