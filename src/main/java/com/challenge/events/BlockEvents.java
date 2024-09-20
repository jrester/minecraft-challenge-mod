package com.challenge.events;

import org.jetbrains.annotations.Nullable;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockEvents {
    public static final Event<AfterBlockBrokenCallback> AFTER_BLOCK_BROKEN_EVENT = EventFactory.createArrayBacked(AfterBlockBrokenCallback.class, callbacks -> (world, player, pos, state, blockEntity, tool) -> {
        for (AfterBlockBrokenCallback callback : callbacks) {
           boolean replaced = callback.afterBlockBroken(world, player, pos, state, blockEntity, tool); 
           if (replaced) return replaced;
        }
        return false;
    });

    @FunctionalInterface
    public interface AfterBlockBrokenCallback {
        public boolean afterBlockBroken(World world, PlayerEntity player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack tool);
    }
    
}
