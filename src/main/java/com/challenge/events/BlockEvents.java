package com.challenge.events;

import org.jetbrains.annotations.Nullable;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
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
    
    public static final Event<OnCraftingBlockCallback> ON_CRAFTING_BLOCK_USE =EventFactory.createArrayBacked(OnCraftingBlockCallback.class, callbacks -> (state, world, pos, player, hit) -> {
        for(OnCraftingBlockCallback callback : callbacks) {
            ActionResult result = callback.onCraftingBlockUse(state, world, pos, player, hit);
            if(result != ActionResult.PASS) {
                return result;
            }
        }
        return ActionResult.PASS;
    });

    @FunctionalInterface
    public interface OnCraftingBlockCallback {
        public ActionResult onCraftingBlockUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit);
    }
}
